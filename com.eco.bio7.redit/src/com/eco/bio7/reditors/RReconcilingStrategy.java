package com.eco.bio7.reditors;

/*******************************************************************************
 * Copyright (c) 2005 Prashant Deva and Gerd Castan
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License - v 1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import java.util.ArrayList;
import java.util.Vector;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;

import com.eco.bio7.reditor.antlr.RBaseListen;
import com.eco.bio7.reditor.antlr.RErrorStrategy;
import com.eco.bio7.reditor.antlr.RFilter;
import com.eco.bio7.reditor.antlr.RLexer;
import com.eco.bio7.reditor.antlr.RParser;
import com.eco.bio7.reditor.antlr.UnderlineListener;
import com.eco.bio7.reditor.outline.REditorOutlineNode;


public class RReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

	private REditor editor;

	private IDocument fDocument;

	/** holds the calculated positions */
	protected final ArrayList<Position> fPositions = new ArrayList<Position>();

	/** The offset of the next character to be read */
	protected int fOffset;

	/** The end offset of the range to be scanned */
	protected int fRangeEnd;

	/**
	 * @return Returns the editor.
	 */
	public REditor getEditor() {
		return editor;
	}

	public void setEditor(REditor editor) {
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
	 */
	public void setDocument(IDocument document) {
		this.fDocument = document;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.reconciler.DirtyRegion, org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		initialReconcile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(IRegion partition) {
		initialReconcile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setProgressMonitor(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#initialReconcile()
	 */
	public void initialReconcile() {
		fOffset = 0;
		fRangeEnd = fDocument.getLength();
		calculatePositions();

	}

	/**
	 * next character position - used locally and only valid while {@link #calculatePositions()} is in progress.
	 */
	


	
	protected void calculatePositions() {
		
		Vector<REditorOutlineNode>  editorOldNodes=editor.nodes;
		/*Create the category base node for the outline! */
		editor.createNodes();

		if (editor != null) {

			IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);

			if (resource != null) {
				try {
					resource.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		IDocument doc = fDocument;

		ANTLRInputStream input = new ANTLRInputStream(doc.get());
		RLexer lexer = new RLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RFilter filter = new RFilter(tokens);
		filter.stream(); // call start rule: stream
		tokens.reset();

		RParser parser = new RParser(tokens);
       // parser.setErrorHandler(new RErrorStrategy());
		parser.setBuildParseTree(true);
		UnderlineListener li=new UnderlineListener(editor);
		lexer.removeErrorListeners();
		lexer.addErrorListener(li);
		parser.removeErrorListeners();
		// parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
		parser.addErrorListener(li);
       
		// Token to= parser.match(0);

		// System.out.println("Errors: " + parser.getNumberOfSyntaxErrors());

		ParseTreeWalker walker = new ParseTreeWalker();

		RuleContext tree = parser.prog();
		RBaseListen list = new RBaseListen(tokens, editor, parser);

		list.startStop.clear();
		walker.walk(list, tree);
		// System.out.println(tree.toStringTree(parser)); // print LISP-style tree
		/*long startTime = System.currentTimeMillis();
		
		long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);*/

		fPositions.clear();
		//cNextPos = fOffset;

		for (int i = 0; i < list.startStop.size(); i++) {

			String pos = (String) list.startStop.get(i);
			String[] val = pos.split(",");

			fPositions.add(new Position(Integer.parseInt(val[0]), Integer.parseInt(val[1])));

		}
		/* Update the outline! */

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				
				
				editor.updateFoldingStructure(fPositions);

				editor.outlineInputChanged(editorOldNodes, editor.nodes);
			}

		});
		
		

	}

}
