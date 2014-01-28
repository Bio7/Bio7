package com.eco.bio7.reditor.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
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
	Stack<REditorOutlineNode> methods;// A stack for nested nodes!

	public RBaseListen(CommonTokenStream tokens, REditor editor, Parser parser) {
		this.tokens = tokens;
		this.editor = editor;
		this.parser = parser;
		methods = new Stack<REditorOutlineNode>();

	}

	@Override
	public void exitExprError(@NotNull RParser.ExprErrorContext ctx) {
		Interval sourceInterval = ctx.getSourceInterval();
		int count = -1;
		int start = sourceInterval.a;
		/* We calculate the token position from the expression! */
		List<Token> firstToken = tokens.get(sourceInterval.a, sourceInterval.b);
		for (int i = 0; i < firstToken.size(); i++) {
			System.out.println(firstToken.get(i).getText());
			if (firstToken.get(i).getText().equals(")")) {
				count = i + 1;
				break;
			}
		}
		// System.out.println(count);

		/* Notify the parser! */
		parser.notifyErrorListeners(tokens.get(start + count), "One Parentheses to much!", null);

	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * The default implementation does nothing.
	 */
	@Override
	public void exitExprError2(@NotNull RParser.ExprError2Context ctx) {

		Interval sourceInterval = ctx.getSourceInterval();
		int count = -1;
		int start = sourceInterval.a;
		/* We calculate the token position from the expression! */
		List<Token> firstToken = tokens.get(sourceInterval.a, sourceInterval.b);
		for (int i = 0; i < firstToken.size(); i++) {
			System.out.println(firstToken.get(i).getText());
			if (firstToken.get(i).getText().equals("(")) {
				count = i;
				break;
			}
		}
		// System.out.println(count);

		/* Notify the parser! */
		parser.notifyErrorListeners(tokens.get(start + count), "One Parentheses to much!", null);
	}

	public void exitDefFunction(@NotNull RParser.DefFunctionContext ctx) {
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
	public void enterDefFunction(@NotNull RParser.DefFunctionContext ctx) {

		Interval sourceInterval = ctx.getSourceInterval();

		Token firstToken = tokens.get(sourceInterval.a);
		// System.out.println(ctx.getParent().getChild(0).getText());
		int lineStart = firstToken.getStartIndex();
		// String ct=ctx.getText();

		// System.out.println("function start at line:"+lineStart);
		Token lastToken = tokens.get(sourceInterval.b);
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
	public void enterVariableDeclaration(@NotNull RParser.VariableDeclarationContext ctx) {

		Interval sourceInterval = ctx.getSourceInterval();
		int start = sourceInterval.a;
		Token assign = tokens.get(start + 2);

		String subExpr = assign.getText();

		if (subExpr.equals("function") == false) {
			Token firstToken = tokens.get(start);

			int lineStart = firstToken.getStartIndex();

			int line = calculateLine(lineStart);

			if (ctx.getParent().getChild(1) != null) {

				String op = tokens.get(start + 1).getText();

				if (op.equals("<-") || op.equals("<<-") || op.equals("=")) {
					String name = tokens.get(start).getText();
					if (methods.size() == 0) {

						new REditorOutlineNode(name, line, "variable", editor.baseNode);

					} else {

						new REditorOutlineNode(name, line, "variable", methods.peek());

					}

				}

				else if (op.equals("->") || op.equals("->>")) {
					String name = tokens.get(start + 2).getText();
					if (methods.size() == 0) {

						new REditorOutlineNode(name, line, "variable", editor.baseNode);

					} else {

						new REditorOutlineNode(name, line, "variable", methods.peek());

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
	public void exitVariableDeclaration(@NotNull RParser.VariableDeclarationContext ctx) {

	}

	@Override
	public void enterCallFunction(@NotNull RParser.CallFunctionContext ctx) {
		Interval sourceInterval = ctx.getSourceInterval();
		int start = sourceInterval.a;
		Token assign = tokens.get(start);

		String subExpr = assign.getText();
		/* Detect libraries and add them to the outline! */
		if (subExpr.equals("library")) {
			Token firstToken = tokens.get(start);

			int lineStart = firstToken.getStartIndex();

			int line = calculateLine(lineStart);

			if (ctx.getParent().getChild(1) != null) {
				String name = ctx.getChild(2).getText();
				String parenthesis = tokens.get(start + 3).getText();//The third token should be a parenthesis!
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
	public void exitCallFunction(@NotNull RParser.CallFunctionContext ctx) {

	}

}