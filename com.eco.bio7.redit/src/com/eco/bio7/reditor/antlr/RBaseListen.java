/*******************************************************************************
 * Copyright (c) 2007-2014 M. Austenfeld
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     M. Austenfeld
 *******************************************************************************/
package com.eco.bio7.reditor.antlr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;
import com.eco.bio7.reditor.Bio7REditorPlugin;
import com.eco.bio7.reditor.antlr.RParser.FormContext;
import com.eco.bio7.reditor.antlr.ref.RFunctionSymbol;
import com.eco.bio7.reditor.antlr.ref.RGlobalScope;
import com.eco.bio7.reditor.antlr.ref.RSymbol;
import com.eco.bio7.reditor.antlr.ref.RVariableSymbol;
import com.eco.bio7.reditor.antlr.ref.Scope;
import com.eco.bio7.reditor.outline.REditorOutlineNode;
import com.eco.bio7.reditors.REditor;

public class RBaseListen extends RBaseListener {

	private CommonTokenStream tokens;
	public ArrayList<String> startStop = new ArrayList<String>();
	// public ClassModel cm = new ClassModel();
	private REditor editor;
	private Parser parser;
	private Stack<REditorOutlineNode> methods = new Stack<REditorOutlineNode>();// A
																				// stack
																				// for
																				// nested
																				// nodes!
	private Stack<RScope> scopes = new Stack<RScope>();// Just for variable
														// lookup in current
														// scope!
	/* A stack to store the method declarations and calls in scope! */
	private Stack<DeclCallStore> storeDeclCall = new Stack<DeclCallStore>();
	public Set<String> finalFuncDecl = new HashSet<String>();
	public Set<String> finalVarDecl = new HashSet<String>();

	private IPreferenceStore store;
	public ParseTreeProperty<Scope> scopeNew = new ParseTreeProperty<Scope>();
	public RGlobalScope globals;
	public Scope currentScope; // define symbols in this scop

	public RBaseListen(CommonTokenStream tokens, REditor editor, Parser parser) {
		this.tokens = tokens;
		this.editor = editor;
		this.parser = parser;

		store = Bio7REditorPlugin.getDefault().getPreferenceStore();

	}

	public void enterProg(RParser.ProgContext ctx) {

		scopes.push(new RScope(null));

		globals = new RGlobalScope(null);
		currentScope = globals;

		storeDeclCall.push(new DeclCallStore());

	}

	public void exitProg(RParser.ProgContext ctx) {
		// storeDeclCall.pop();
		// System.out.println(globals);
		/* Exit scope! */
		scopes.pop();
		if (methods.empty() == false) {
			methods.pop();
		}

		/* Has the function be called in this scope? */
		DeclCallStore st = storeDeclCall.peek();
		Set<String> subScope = st.substract();
		Set<String> subScopeVar = st.substractVars();

		/* Add the scoped uncalled functions to the global store! */
		finalFuncDecl.addAll(subScope);
		/* Add the scoped uncalled variables to the global store! */
		finalVarDecl.addAll(subScopeVar);
		/* Leave the scope (remove from stack)! */
		storeDeclCall.pop();
		currentScope = currentScope.getEnclosingScope(); // pop scope
	}

	public void exitE19DefFunction(RParser.E19DefFunctionContext ctx) {
		/* Exit scope! */
		scopes.pop();
		if (methods.empty() == false) {
			methods.pop();
		}
		/* Has the function be called in this scope? */
		DeclCallStore st = storeDeclCall.peek();
		/* Make a snaphot of function call for bottom up transfer! */
		Set<String> tempCall = st.call;
		/* Make a snaphot of variable call for bottom up transfer! */
		Set<String> tempVarCall = st.varCall;

		Set<String> subScope = st.substract();

		Set<String> subScopeVar = st.substractVars();
		/* Add the scoped uncalled functions to the global store! */

		finalFuncDecl.addAll(subScope);

		finalVarDecl.addAll(subScopeVar);
		/* Leave the scope (remove from stack)! */
		storeDeclCall.pop();

		DeclCallStore stAdd = storeDeclCall.peek();
		/*
		 * Add the function calls to the parent scope (function) if it is called
		 * there!
		 */
		stAdd.call.addAll(tempCall);

		stAdd.varCall.addAll(tempVarCall);
		currentScope = currentScope.getEnclosingScope(); // pop scope

	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * The default implementation does nothing.
	 */
	@Override
	public void enterE19DefFunction(RParser.E19DefFunctionContext ctx) {
		/*
		 * Insert function as current scope with a parent current scope
		 * (scope.peek)!
		 */
		scopes.push(new RScope(scopes.peek()));

		Token firstToken = ctx.getStart();
		Token lastToken = ctx.getStop();

		Interval sourceInterval = ctx.getSourceInterval();
		int start = sourceInterval.a;

		int lineStart = firstToken.getStartIndex();

		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;

		// Add to the editor folding action if enabled in the preferences!
		if (store.getBoolean("FUNCTIONS_FOLDING")) {
			startStop.add(lineStart + "," + lineEnd);
		}
		int lineMethod = calculateLine(lineStart);

		/*
		 * If we have at least 2 tokens else we create a function without
		 * variable assignment!
		 */
		if ((start - 2) >= 0 && ctx.getParent().getChild(1) != null && ctx.getParent().getChild(2) != null) {

			String op = ctx.getParent().getChild(1).getText();
			String name = ctx.getParent().getChild(0).getText();
			/*
			 * Check if we have an assignment symbol available! else we create a
			 * function without variable assignment!
			 */
			if (op.equals("<-") || op.equals("<<-") || op.equals("=")) {
				/* Create a new scope and add the function (symbol)! */
				RFunctionSymbol function = new RFunctionSymbol(name, currentScope, ctx.formlist());
				currentScope.define(function); // Define function in current
												// //
												// scope
				scopeNew.put(ctx, function);
				currentScope = function;

				/* Put the method declaration name in the call set! */
				/* Get the current scope stack elements! */
				DeclCallStore st = storeDeclCall.peek();
				/* add the called method to the call set! */
				st.decl.add(name);
				storeDeclCall.push(new DeclCallStore());
				/*
				 * Create the function arguments as known symbol table vars in
				 * current scope!
				 */
				if (ctx.formlist() != null) {
					List<FormContext> formList = ctx.formlist().form();
					int functionDefSize = formList.size();
					for (int i = 0; i < functionDefSize; i++) {
						FormContext fo = formList.get(i);

						TerminalNode ar = fo.ID();
						if (ar != null) {
							RVariableSymbol var = new RVariableSymbol(ar.getText());
							currentScope.define(var);
						}

					}
				}

				/* Here we create the outline nodes in the Outline view! */
				if (methods.size() == 0) {

					methods.push(new REditorOutlineNode(name, lineMethod, "function", editor.baseNode));

				} else {
					methods.push(new REditorOutlineNode(name, lineMethod, "function", methods.peek()));

				}

			} else {

				createFunctionWithoutName(ctx, lineMethod);

			}
		}

		else {
			createFunctionWithoutName(ctx, lineMethod);

		}

	}

	private void createFunctionWithoutName(RParser.E19DefFunctionContext ctx, int lineMethod) {
		/* Create a new scope and add the function (symbol)! */
		RFunctionSymbol function = new RFunctionSymbol(ctx.start.getText(), currentScope, ctx.formlist());
		currentScope.define(function); // Define function in current scope
		scopeNew.put(ctx, function);
		currentScope = function;
		/* Put the method declaration name in the call set! */
		/* Get the current scope stack elements! */
		DeclCallStore st = storeDeclCall.peek();
		/* add the called method to the call set! */
		st.decl.add(ctx.start.getText());
		storeDeclCall.push(new DeclCallStore());
		/* Here we create the outline nodes in the Outline view! */
		if (methods.size() == 0) {

			methods.push(new REditorOutlineNode(ctx.start.getText(), lineMethod, "function", editor.baseNode));

		} else {
			methods.push(new REditorOutlineNode(ctx.start.getText(), lineMethod, "function", methods.peek()));

		}
	}

	/* if condition! */
	public void enterE21(RParser.E21Context ctx) {

		Token firstToken = ctx.getStart();
		Token lastToken = ctx.getStop();
		int lineStart = firstToken.getStartIndex();

		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;

		// Add to the editor folding action if enabled in the preferences!
		if (store.getBoolean("IF_CONDITION_FOLDING")) {
			startStop.add(lineStart + "," + lineEnd);
		}

	}

	/* if condition 2 of grammar file! */
	public void enterE22(RParser.E22Context ctx) {

		Token firstToken = ctx.getStart();
		Token lastToken = ctx.getStop();
		int lineStart = firstToken.getStartIndex();

		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;

		// Add to the editor folding action if enabled in the preferences!
		if (store.getBoolean("IF_CONDITION_FOLDING")) {
			startStop.add(lineStart + "," + lineEnd);
		}

	}

	/* for loop! */
	public void enterE23(RParser.E23Context ctx) {

		Token firstToken = ctx.getStart();
		Token lastToken = ctx.getStop();
		int lineStart = firstToken.getStartIndex();

		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;

		// Add to the editor folding action if enabled in the preferences!
		if (store.getBoolean("FOR_LOOP_FOLDING")) {
			startStop.add(lineStart + "," + lineEnd);
		}

	}

	/* while loop! */
	public void enterE24(RParser.E24Context ctx) {

		Token firstToken = ctx.getStart();
		Token lastToken = ctx.getStop();

		int lineStart = firstToken.getStartIndex();

		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;

		// Add to the editor folding action if enabled in the preferences!
		if (store.getBoolean("WHILE_LOOP_FOLDING")) {
			startStop.add(lineStart + "," + lineEnd);
		}

	}

	/* repeat loop! */
	public void enterE25(RParser.E25Context ctx) {

		Token firstToken = ctx.getStart();
		Token lastToken = ctx.getStop();

		int lineStart = firstToken.getStartIndex();

		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;

		// Add to the editor folding action if enabled in the preferences!
		if (store.getBoolean("REPEAT_LOOP_FOLDING")) {
			startStop.add(lineStart + "," + lineEnd);
		}

	}

	@Override
	public void enterE17VariableDeclaration(RParser.E17VariableDeclarationContext ctx) {

		Interval sourceInterval = ctx.getSourceInterval();

		Token firstToken = ctx.getStart();
		int start = sourceInterval.a;
		int stop = sourceInterval.b;
		String isFunc = ctx.expr(1).start.getText();

		if (isFunc.equals("function") == false) {

			int lineStart = firstToken.getStartIndex();

			int line = calculateLine(lineStart);

			/*
			 * Extract the token with the assignment operator and (to exclude
			 * whitespace in stream because of the hidden() rule the whitespace
			 * is present in the CommonTokenStream!)
			 */
			int i = start + 1;
			Token assignOp = null;
			while (i <= stop) {
				Token tok = tokens.get(i);
				if (tok.getType() != RParser.WS) {
					assignOp = tok;
					break;
				}
				if (tok.getType() == RParser.EOF) {
					break;
				}
				i++;
			}

			String op = assignOp.getText();
			if (op.equals("<-") || op.equals("<<-") || op.equals("=")) {
				String name = firstToken.getText();
				if (methods.size() == 0) {
					if (checkVarName(name)) {
						RScope scope = scopes.peek();
						scope.add(name);

						/* Create a new a new var in current scope! */
						RVariableSymbol var = new RVariableSymbol(name);
						currentScope.define(var);

						DeclCallStore st = storeDeclCall.peek();
						/*
						 * Add the called var to the call set to detect unused
						 * vaiables!
						 */
						st.varDecl.add(name);

						new REditorOutlineNode(name, line, "variable", editor.baseNode);
					}

				} else {
					if (checkVarName(name)) {
						RScope scope = scopes.peek();
						scope.add(name);
						/* Create a new a new var in current scope! */
						RVariableSymbol var = new RVariableSymbol(name);
						currentScope.define(var); // Define symbol in
													// current scope
						DeclCallStore st = storeDeclCall.peek();
						/*
						 * Add the called var to the call set to detect unused
						 * vaiables!
						 */
						st.varDecl.add(name);

						new REditorOutlineNode(name, line, "variable", methods.peek());
					}

				}

			}

			else if (op.equals("->") || op.equals("->>")) {
				String name = tokens.get(start + 2).getText();
				if (methods.size() == 0) {
					if (checkVarName(name)) {
						RScope scope = scopes.peek();
						scope.add(name);
						/* Create a new a new var in current scope! */
						RVariableSymbol var = new RVariableSymbol(name);
						currentScope.define(var); // Define symbol in
													// current scope
						DeclCallStore st = storeDeclCall.peek();
						/*
						 * Add the called var to the call set to detect unused
						 * vaiables!
						 */
						st.varDecl.add(name);

						new REditorOutlineNode(name, line, "variable", editor.baseNode);
					}

				} else {
					if (checkVarName(name)) {
						RScope scope = scopes.peek();
						scope.add(name);
						/* Create a new a new var in current scope! */
						RVariableSymbol var = new RVariableSymbol(name);
						currentScope.define(var);

						DeclCallStore st = storeDeclCall.peek();
						/*
						 * Add the called var to the call set to detect unused
						 * vaiables!
						 */
						st.varDecl.add(name);

						new REditorOutlineNode(name, line, "variable", methods.peek());
					}
				}

			}
		}
		// }

	}

	/* Calculates the line from the editor document! */
	private int calculateLine(int lineStart) {
		IDocumentProvider provider = editor.getDocumentProvider();
		IDocument document = provider.getDocument(editor.getEditorInput());
		int line = 0;
		try {
			line = document.getLineOfOffset(lineStart) + 1;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			System.out.println("Bad line location!");
			// e.printStackTrace();
		}
		return line;
	}

	@Override
	public void exitE17VariableDeclaration(RParser.E17VariableDeclarationContext ctx) {

	}

	@Override
	public void enterE20CallFunction(RParser.E20CallFunctionContext ctx) {
		/*
		 * Interval sourceInterval = ctx.getSourceInterval(); int start =
		 * sourceInterval.a; Token assign = tokens.get(start);
		 */

		Token start = ctx.start;
		String startText = start.getText();
		/* Get the current scope stack elements! */
		DeclCallStore st = storeDeclCall.peek();
		/* add the called method to the call set! */
		st.call.add(startText);

		/* Detect libraries and add them to the outline! */
		if (startText.equals("library") || startText.equals("require")) {
			Token firstToken = start;

			int lineStart = firstToken.getStartIndex();

			int line = calculateLine(lineStart);

			if (ctx.getParent().getChild(1) != null) {
				String name = ctx.getChild(2).getText();
				// The third token should be a parenthesis!

				String parenthesis = ctx.getChild(3).getText();
				if (parenthesis.equals(")")) {
					if (methods.size() == 0) {

						new REditorOutlineNode(name, line, "library", editor.baseNode);

					} else {

						new REditorOutlineNode(name, line, "library", methods.peek());

					}
				}
			}
		}

	}

	@Override
	public void exitE20CallFunction(RParser.E20CallFunctionContext ctx) {

	}

	/*
	 * Adapted method source from:
	 * http://stackoverflow.com/questions/15050137/once
	 * -grammar-is-complete-whats-the-best-way-to-walk-an-antlr-v4-tree
	 */
	private boolean checkVarName(String varName) {
		boolean check;
		RScope scope = scopes.peek();
		if (scope.inScope(varName)) {

			check = false;
		} else {
			check = true;
		}
		return check;
	}

	public void exitErr1(RParser.Err1Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err1:Too many parentheses!", null);

	}

	public void exitErr3(RParser.Err3Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err3:Too many parentheses in function call!", null);

	}

	public void exitErr5(RParser.Err5Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err5:Too many parentheses in function definition!", null);

	}

	/*
	 * public void exitErr6(@NotNull RParser.Err6Context ctx) {
	 * 
	 * parser.notifyErrorListeners(ctx.extra,
	 * "Err6:Too many parentheses in left if definition!", null);
	 * 
	 * 
	 * 
	 * }
	 */

	public void exitErr7(RParser.Err7Context ctx) {

		// int index = ctx.extra.getStartIndex();

		parser.notifyErrorListeners(ctx.extra, "Err7:Too many parentheses in if condition!", null);

	}

	public void exitErr8(RParser.Err8Context ctx) {

		// int index = ctx.extra.getStartIndex();

		parser.notifyErrorListeners(ctx.extra, "Err8:Too many brackets!", null);

	}

	public void exitErr9(RParser.Err9Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err9:Too many brackets!", null);

	}

	public void exitErr11(RParser.Err11Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err11:Too many braces!", null);

	}

	/* Here we create some warnings from the parser! */
	public void exitWarn12(RParser.Warn12Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Warn12:Wrong constant: 'TRUE' required!", null);

	}

	public void exitWarn13(RParser.Warn13Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Warn13:Wrong constant: 'FALSE' required!", null);

	}

	public void exitWarn14(RParser.Warn14Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Warn14:Wrong constant: 'NULL' required!", null);

	}

	public void exitWarn15(RParser.Warn15Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Warn15:Wrong constant: 'NA' required!", null);

	}

	/*
	 * With this error message we produce QuickFixes. The errors start with
	 * 'Err' to seperate them later in the RBaseListen class!
	 */
	public void exitErr16(RParser.Err16Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err16:Too many braces in while statement!", null);

	}

	public void exitErr18(RParser.Err18Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err18:Too many braces in for statement!", null);

	}

	public void exitErr20(RParser.Err20Context ctx) {

		Token firstToken = tokens.get(ctx.getChild(1).getSourceInterval().a);
		parser.notifyErrorListeners(firstToken, "Err20:Wrong comparison!", null);

	}

	public void exitErr21(RParser.Err21Context ctx) {

		Token firstToken = tokens.get(ctx.getChild(1).getSourceInterval().a);
		parser.notifyErrorListeners(firstToken, "Err21:Wrong comparison!", null);

	}

	public void exitErr22(RParser.Err22Context ctx) {

		Token firstToken = ctx.start;
		parser.notifyErrorListeners(firstToken, "Err22:Unknown Token!", null);

	}

	/* ID call (variables) Need to calculate position of <-  */
	public void enterE30(RParser.E30Context ctx) {
		Token tok = ctx.ID().getSymbol();
		// System.out.println("Token Text: "+tok.getText());
		String varName = tok.getText();
		int index = tok.getTokenIndex();
		Token idNextToken = tokens.get(index + 1);
		// System.out.println("Next Symbol= "+idNextToken.getText());
		if (idNextToken != null) {
			if (idNextToken.getText().equals("=") || idNextToken.getText().equals("<-") || idNextToken.getText().equals("(")) {
				return;
			}

			else {
				/* Get the current scope stack elements! */
				DeclCallStore st = storeDeclCall.peek();
				/* Add the called method to the call set! */
				st.varCall.add(varName);

			}
		}

		/*
		 * Interval sourceInterval = ctx.getSourceInterval(); int start =
		 * sourceInterval.a; String name = tokens.get(start).getText(); String
		 * op = tokens.get(start + 1).getText();
		 * if(op.equals("<-")||op.equals("=")||op.equals("<<-")){
		 * name=tokens.get(start + 2).getText(); }
		 * System.out.println("ID:"+name);
		 */
	}

}