package com.eco.bio7.reditor.antlr;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.eco.bio7.reditor.outline.REditorOutlineNode;
import com.eco.bio7.reditors.REditor;

public class RBaseListen extends RBaseListener {

	private CommonTokenStream tokens;
	public ArrayList<String> startStop = new ArrayList<String>();
	// public ClassModel cm = new ClassModel();
	private REditor editor;
	private Parser parser;
	private Stack<REditorOutlineNode> methods;// A stack for nested nodes!
	private Stack<RScope> scopes;

	public RBaseListen(CommonTokenStream tokens, REditor editor, Parser parser) {
		this.tokens = tokens;
		this.editor = editor;
		this.parser = parser;
		methods = new Stack<REditorOutlineNode>();
		scopes = new Stack<RScope>();
		scopes.push(new RScope(null));

	}

	@Override
	/*
	 * public void exitExprError(@NotNull RParser.ExprErrorContext ctx) {
	 * 
	 * parser.notifyErrorListeners(ctx.start,
	 * "One Opening Parentheses to much!", null);
	 * 
	 * }
	 *//**
		 * {@inheritDoc}
		 * <p/>
		 * The default implementation does nothing.
		 *//*
		 * @Override public void exitExprError2(@NotNull
		 * RParser.ExprError2Context ctx) {
		 * 
		 * 
		 * 
		 * Notify the parser! parser.notifyErrorListeners(ctx.stop,
		 * "One Closing Parentheses to much!", null); }
		 */
	/*
	 * public void enterAmountRightBraceError(@NotNull
	 * RParser.AmountRightBraceErrorContext ctx) { }
	 * 
	 * public void exitAmountRightBraceError(@NotNull
	 * RParser.AmountRightBraceErrorContext ctx) {
	 * 
	 * Interval sourceInterval = ctx.getSourceInterval(); int count = -1; int
	 * start = sourceInterval.a; We calculate the token position from the
	 * expression! List<Token> firstToken = tokens.get(sourceInterval.a,
	 * sourceInterval.b); for (int i = 0; i < firstToken.size(); i++) { //
	 * System.out.println(firstToken.get(i).getText()); if
	 * (firstToken.get(i).getText().equals("(")) { count = i; break; } } //
	 * System.out.println(count);
	 * 
	 * Notify the parser! parser.notifyErrorListeners(ctx.stop,
	 * "One Closing Brace to much!", null); }
	 */
	/*
	 * public void enterAmountLeftBraceError2(@NotNull
	 * RParser.AmountLeftBraceError2Context ctx) {
	 * 
	 * 
	 * }
	 * 
	 * public void exitAmountLeftBraceError2(@NotNull
	 * RParser.AmountLeftBraceError2Context ctx) { Interval sourceInterval =
	 * ctx.getSourceInterval();
	 * 
	 * int count = -1; int start = sourceInterval.a; We calculate the token
	 * position from the expression! List<Token> firstToken =
	 * tokens.get(sourceInterval.a, sourceInterval.b); for (int i = 0; i <
	 * firstToken.size(); i++) { //
	 * System.out.println(firstToken.get(i).getText()); if
	 * (firstToken.get(i).getText().equals("(")) { count = i; break; } }
	 * 
	 * ctx.getChild(2).getSourceInterval();
	 * System.out.println("This brace is at:"
	 * +ctx.start.getCharPositionInLine());
	 * 
	 * Notify the parser! parser.notifyErrorListeners(ctx.start,
	 * "One Opening Brace to much!", null);
	 * 
	 * }
	 */
	/*
	 * public void enterClosingRightBraceError(@NotNull
	 * RParser.ClosingRightBraceErrorContext ctx) {
	 * 
	 * 
	 * }
	 * 
	 * public void exitClosingRightBraceError(@NotNull
	 * RParser.ClosingRightBraceErrorContext ctx) {
	 * 
	 * Notify the parser! parser.notifyErrorListeners(ctx.stop,
	 * "Closing Brace Missing!", null);
	 * 
	 * }
	 * 
	 * public void exitClosingLeftBraceError(@NotNull
	 * RParser.ClosingLeftBraceErrorContext ctx) {
	 * 
	 * Notify the parser! parser.notifyErrorListeners(ctx.start,
	 * "Opening Brace Missing!", null);
	 * 
	 * }
	 */
	/*
	 * public void exitClosingLeftEmptyBraceError(@NotNull
	 * RParser.ClosingLeftEmptyBraceErrorContext ctx) {
	 * 
	 * Notify the parser! parser.notifyErrorListeners(ctx.start,
	 * "Right Brace Missing!", null);
	 * 
	 * }
	 */
	public void exitE19DefFunction(@NotNull RParser.E19DefFunctionContext ctx) {
		/* Exit scope! */
		scopes.pop();
		if (methods.empty() == false) {
			methods.pop();
		}

	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * The default implementation does nothing.
	 */
	@Override
	public void enterE19DefFunction(@NotNull RParser.E19DefFunctionContext ctx) {
		/*
		 * Insert function as current scope with a parent current scope
		 * (scope.peek)!
		 */
		scopes.push(new RScope(scopes.peek()));

		// Interval sourceInterval = ctx.getSourceInterval();

		Token firstToken = ctx.start;
		// System.out.println(ctx.getParent().getChild(0).getText());
		int lineStart = firstToken.getStartIndex();
		// String ct=ctx.getText();

		// System.out.println("function start at line:"+lineStart);
		Token lastToken = ctx.stop;
		int lineEnd = lastToken.getStopIndex() + 1 - lineStart;
		// String ct2=ctx.getText();

		// Add to the editor folding action.
		startStop.add(lineStart + "," + lineEnd);
		int lineMethod = calculateLine(lineStart);
		int childs = ctx.getParent().getChildCount();
		int posTree = 0;
		for (int i = 0; i < childs; i++) {
			if (ctx.getText().equals(ctx.getParent().getChild(i).getText())) {
				posTree = i;
			}
		}
		if (ctx.getParent().getChild(posTree - 1) != null && ctx.getParent().getChild(posTree - 2) != null) {
			String op = ctx.getParent().getChild(posTree - 1).getText();
			String name = ctx.getParent().getChild(posTree - 2).getText();

			if (op.equals("<-") || op.equals("<<-")) {

				if (methods.size() == 0) {

					methods.push(new REditorOutlineNode(name, lineMethod, "function", editor.baseNode));

				} else {
					methods.push(new REditorOutlineNode(name, lineMethod, "function", methods.peek()));

				}

			}
		} else if (posTree == 0) {
			if (methods.size() == 0) {

				methods.push(new REditorOutlineNode(ctx.getText(), lineMethod, "function", editor.baseNode));

			} else {
				methods.push(new REditorOutlineNode(ctx.getText(), lineMethod, "function", methods.peek()));

			}

		}

	}

	@Override
	public void enterE17VariableDeclaration(@NotNull RParser.E17VariableDeclarationContext ctx) {

		// Interval sourceInterval = ctx.getSourceInterval();
		// int start = sourceInterval.a;

		String subExpr = ctx.getChild(2).getText();

		if (subExpr.equals("function") == false) {
			Token firstToken = ctx.start;

			int lineStart = firstToken.getStartIndex();

			int line = calculateLine(lineStart);

			if (ctx.getParent().getChild(1) != null) {

				String op = ctx.getChild(1).getText();

				if (op.equals("<-") || op.equals("<<-") || op.equals("=")) {
					String name = ctx.getChild(0).getText();
					if (methods.size() == 0) {
						if (checkVarName(name)) {
							RScope scope = scopes.peek();
							scope.add(name);

							new REditorOutlineNode(name, line, "variable", editor.baseNode);
						}

					} else {
						if (checkVarName(name)) {
							RScope scope = scopes.peek();
							scope.add(name);

							new REditorOutlineNode(name, line, "variable", methods.peek());
						}

					}

				}

				else if (op.equals("->") || op.equals("->>")) {
					String name = ctx.getChild(2).getText();
					if (methods.size() == 0) {
						if (checkVarName(name)) {
							RScope scope = scopes.peek();
							scope.add(name);

							new REditorOutlineNode(name, line, "variable", editor.baseNode);
						}

					} else {
						if (checkVarName(name)) {
							RScope scope = scopes.peek();
							scope.add(name);

							new REditorOutlineNode(name, line, "variable", methods.peek());
						}
					}

				}
			}
		}

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
			e.printStackTrace();
		}
		return line;
	}

	@Override
	public void exitE17VariableDeclaration(@NotNull RParser.E17VariableDeclarationContext ctx) {

	}

	@Override
	public void enterE20CallFunction(@NotNull RParser.E20CallFunctionContext ctx) {
		/*
		 * Interval sourceInterval = ctx.getSourceInterval(); int start =
		 * sourceInterval.a; Token assign = tokens.get(start);
		 */
		Token start = ctx.start;
		String subExpr = start.getText();
		/* Detect libraries and add them to the outline! */
		if (subExpr.equals("library") || subExpr.equals("require")) {
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
	public void exitE20CallFunction(@NotNull RParser.E20CallFunctionContext ctx) {

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

	public void exitErr1(@NotNull RParser.Err1Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err1:Too many parentheses!", null);

	}

	public void exitErr2(@NotNull RParser.Err2Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err2:Missing closing parentheses in function call!", null);

	}

	public void exitErr3(@NotNull RParser.Err3Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err3:Too many parentheses in function call!", null);

	}

	public void exitErr4(@NotNull RParser.Err4Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err4:Missing closing parentheses in function definition!", null);

	}

	public void exitErr5(@NotNull RParser.Err5Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err5:Too many parentheses in function definition!", null);

	}

	public void exitErr6(@NotNull RParser.Err6Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err6:Missing closing parentheses in if condition!", null);

	}

	public void exitErr7(@NotNull RParser.Err7Context ctx) {
		
		//int index = ctx.extra.getStartIndex();

		parser.notifyErrorListeners(ctx.extra, "Err7:Too many parentheses in if condition!", null);

	}

	public void exitErr8(@NotNull RParser.Err8Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err8:Missing closing brackets!", null);

	}

	public void exitErr9(@NotNull RParser.Err9Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err9:Too many brackets!", null);

	}

	public void exitErr10(@NotNull RParser.Err10Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err10:Missing closing braces!", null);

	}

	public void exitErr11(@NotNull RParser.Err11Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err11:Too many braces!", null);

	}
	public void exitErr12(@NotNull RParser.Err12Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err12:Wrong constant: 'TRUE' required!", null);

	}
	
	public void exitErr13(@NotNull RParser.Err13Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err13:Wrong constant: 'FALSE' required!", null);

	}
	
	public void exitErr14(@NotNull RParser.Err14Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err14:Wrong constant: 'NULL' required!", null);

	}
	public void exitErr15(@NotNull RParser.Err15Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err15:Wrong constant: 'NA' required!", null);

	}
	public void exitErr16(@NotNull RParser.Err16Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err16:Too many braces in while statement!", null);

	}
	public void exitErr17(@NotNull RParser.Err17Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err17:Missing closing braces in while statement!", null);

	}
	public void exitErr18(@NotNull RParser.Err18Context ctx) {

		parser.notifyErrorListeners(ctx.extra, "Err16:Too many braces in for statement!", null);

	}
	public void exitErr19(@NotNull RParser.Err19Context ctx) {

		parser.notifyErrorListeners(ctx.start, "Err17:Missing closing braces in for statement!", null);

	}
	

}