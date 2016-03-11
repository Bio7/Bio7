package com.eco.bio7.reditor.code;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;

import com.eco.bio7.reditor.antlr.RBaseListen;
import com.eco.bio7.reditor.antlr.RErrorStrategy;
import com.eco.bio7.reditor.antlr.RFilter;
import com.eco.bio7.reditor.antlr.RLexer;
import com.eco.bio7.reditor.antlr.RParser;
import com.eco.bio7.reditor.antlr.UnderlineListener;
import com.eco.bio7.reditor.antlr.ref.RRefPhaseListen;
import com.eco.bio7.reditors.REditor;

public class RQuickFixSolutions {
	private ISourceViewer viewer;
	private REditor editor;

	public RQuickFixSolutions(ISourceViewer viewer, REditor rEditor) {
		this.viewer = viewer;
		this.editor = rEditor;
	}

	/*
	 * Hardcoded. We could also refactor out the solutions to a properties file!
	 */
	public ICompletionProposal[] getProposals(String errorCode, int offset, int endChar, ICompletionProposal[] prop, String tokenText) {
		if (errorCode != null) {

			switch (errorCode) {
			case "Err1":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ')'", offset, endChar, "", 1) };
				break;
			case "Err2":

				break;
			case "Err3":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ')'", offset, endChar, "", 1) };
				break;
			case "Err4":

				break;
			case "Err5":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ')'", offset, endChar, "", 1) };
				break;
			case "Err6":

				break;
			case "Err7":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ')'", offset, endChar, "", 1) };
				break;
			case "Err8":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ']'", offset, endChar, "", 1) };

				break;
			case "Err9":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ']'", offset, endChar, "", 1) };
				break;
			case "Err10":

				break;
			case "Err11":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove '}'", offset, endChar, "", 1) };
				break;
			case "Warn12":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Replace 'true' with 'TRUE'", offset, endChar, "TRUE", 4) };

				break;
			case "Warn13":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Replace 'false' with 'FALSE'", offset, endChar, "FALSE", 5) };

				break;
			case "Warn14":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Replace 'null' with 'NULL'", offset, endChar, "NULL", 4) };

				break;
			case "Warn15":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Replace 'na' with 'NA'", offset, endChar, "NA", 2) };

				break;
			case "Warn16":

				IDocument doc = viewer.getDocument();

				ANTLRInputStream input = new ANTLRInputStream(doc.get());
				RLexer lexer = new RLexer(input);

				CommonTokenStream tokens = new CommonTokenStream(lexer);

				UnderlineListener li = new UnderlineListener(editor);
				RFilter filter = new RFilter(tokens);
				/*
				 * We have to remove the filter, too! Else we get error messages
				 * on the console!
				 */
				filter.removeErrorListeners();
				// filter.addErrorListener(li);

				filter.stream(); // call start rule: stream
				tokens.reset();

				RParser parser = new RParser(tokens);
				parser.removeErrorListeners();
				/*
				 * Add some modified error messages by implementing a custom
				 * error strategy!
				 */
				parser.setErrorHandler(new RErrorStrategy());
				parser.setBuildParseTree(true);

				lexer.removeErrorListeners();
				// lexer.addErrorListener(li);
				parser.removeErrorListeners();
				// parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
				parser.addErrorListener(li);

				ParseTreeWalker walker = new ParseTreeWalker();

				RuleContext tree = parser.prog();
				/* Create the listener to create the outline, etc. */
				RBaseListen list = new RBaseListen(tokens, editor, parser);

				list.startStop.clear();
				walker.walk(list, tree);

				RRefPhaseListen ref = new RRefPhaseListen(tokens, list, parser, offset);
				walker.walk(ref, tree);

				StringBuffer buffScopedFunctions = ref.getBuffScopeFunctions();
				String[] splitBuffScopedFun = buffScopedFunctions.toString().split(",");
				
				String[]pcPro=new String[splitBuffScopedFun.length];
				
				for (int i = 0; i < splitBuffScopedFun.length; i++) {
					double dist=Levenshtein.getLevenshteinDistance(tokenText, splitBuffScopedFun[i]);
					
					double max= Math.max(tokenText.length(),splitBuffScopedFun[i].length());
					double perct = round(1.0 - (dist / max),2);
					System.out.println(dist+" : "+perct);
					pcPro[i]= String.valueOf(perct);
				}			
				
				prop = new ICompletionProposal[splitBuffScopedFun.length + 1];
				
				prop[0] = new RQuickFixCompletionProposal("Create function", offset, endChar, System.lineSeparator() + tokenText + "<-function(){}" + System.lineSeparator(), 0);
				
				for (int i = 0; i < splitBuffScopedFun.length; i++) {
					if(splitBuffScopedFun[i]!=null){
					prop[i+1] = new RQuickFixCompletionProposal("Functions available:"+" '"+splitBuffScopedFun[i] +"' Similarity:"+" "+pcPro[i], offset, endChar, splitBuffScopedFun[i], endChar-offset);
					}
					else{
						prop[i+1] = new RQuickFixCompletionProposal("Functions available: NA", offset, endChar, "NA", 2);	
					}
				}

				

			case "Warn17":
				/*
				 * prop = new ICompletionProposal[] {
				 * 
				 * new RQuickFixCompletionProposal("Create function", offset,
				 * endChar, System.lineSeparator()+tokenText+"<-function(){}"+
				 * System.lineSeparator(), 0) };
				 */

				break;
			case "Err16":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ')'", offset, endChar, "", 1) };

				break;
			case "Err17":

				break;
			case "Err18":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove ')'", offset, endChar, "", 1) };
				break;
			case "Err19":

				break;
			case "Err20":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Replace '=>' with '>='", offset, endChar, ">=", 2) };
				break;
			case "Err21":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Replace '=<' with '<='", offset, endChar, "<=", 2) };
				break;
			case "Err22":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove unknown token", offset, endChar, "", 1) };
				break;
			case "Err23":
				prop = new ICompletionProposal[] {

						new RQuickFixCompletionProposal("Remove function variable", offset, endChar, "", 1) };

			default:
				break;
			}

		}
		return prop;
	}
	/*From: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places?lq=1*/
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}