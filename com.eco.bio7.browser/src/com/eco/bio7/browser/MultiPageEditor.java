package com.eco.bio7.browser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Timer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swt.FXCanvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.Source;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.eco.bio7.browser.editor.XMLEditor;
import com.eco.bio7.util.Util;

/**
 * An example showing how to create a multi-page editor. This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener {

	/** The text editor used in page 0. */
	private XMLEditor editor;
	/** The font chosen in page 1. */
	private Font font;
	/** The text widget used in page 2. */
	private StyledText text;
	private Text txt;
	protected String content;
	protected String path;
	public static final String ID = "com.eco.bio7.browser.BrowserEditorView"; //$NON-NLS-1$
	public String basePath = ".";
	protected String loadContent;
	protected boolean hasToComplete;
	private Scene scene;
	private FXCanvas canvas;
	private HTMLEditor htmlEditor;
	protected IDocumentProvider documentProvider;
	protected boolean dirty = false;
	private IFile ifile;
	private boolean isDirty;
	private FileEditorInput fileInputEditor;
	protected Timer timer;
	protected Job job;

	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		/*Disable logging in Jericho parser!*/
		Config.LoggerProvider=LoggerProvider.DISABLED;
	}

	void createPage1() {
		try {
			editor = new XMLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}

	}

	public void createPage0() {

		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);

		canvas = new FXCanvas(composite, SWT.NONE);
		canvas.setLayout(new FillLayout());
		Display display = PlatformUI.getWorkbench().getDisplay();
		display.syncExec(new Runnable() {

			public void run() {

				htmlEditor = new HTMLEditor();
			}
		});
		WebView webview = (WebView) htmlEditor.lookup("WebView");
		GridPane.setHgrow(webview, Priority.ALWAYS);
		GridPane.setVgrow(webview, Priority.ALWAYS);
		// htmlEditor.setMaxHeight(2000);
		setCustomActions(htmlEditor);

		EventHandler<MouseEvent> onMouseExitedHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// System.out.println("changed toolbar");
			}
		};

		for (Node node : htmlEditor.lookupAll("ToolBar")) {
			node.setOnMouseExited(onMouseExitedHandler);
		}
		htmlEditor.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent evt) -> {

			

			final KeyCombination combo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
			final KeyCombination combo2 = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
			final KeyCombination combo3 = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

			if (combo.match(evt)) {

				IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(getEditor(1).getEditorInput());

				htmlEditor.setHtmlText(doc.get());
			}

			else if (combo2.match(evt)) {
				IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(getEditor(1).getEditorInput());

				htmlEditor.setHtmlText(doc.get());
			}
			/* Save! */
			else if (evt.isControlDown()) {
				if (combo3.match(evt)) {

				}
				return;
			}
			/* Select All! */
			else if (evt.isShortcutDown() && evt.getCode() == KeyCode.A) {

			}
			/* Paste Event! */
			else if (evt.isShortcutDown() && evt.getCode() == KeyCode.V) {

			}

			else {
				/*
				 * A transfer to the source editor is triggered after the last text change event occurs to avoid time intensive transfers for longer documents!
				 */
				if (job != null) {
					job.cancel();
				}

				job = new Job("Update Text") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("Update Text...", IProgressMonitor.UNKNOWN);

						Display display = PlatformUI.getWorkbench().getDisplay();
						display.syncExec(new Runnable() {

							public void run() {

								String sf = formatHtml();

								try {
									IEditorInput ed = getEditor(1).getEditorInput();

									IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(ed);

									doc.set(sf);
								} catch (Exception e) {
									/*
									 * Listener won't work (if two editors are open and one is disposed)so we simply catch the error (without a message) if the editor is disposed!
									 */
								}

							}
						});

						monitor.done();
						return Status.OK_STATUS;
					}

				};

				job.schedule(500);

				firePropertyChange(IEditorPart.PROP_DIRTY);
			}

		});

		htmlEditor.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {

			}

		});
		/* JavaFX Thread! */
		Platform.runLater(new Runnable() {

			public void run() {
				scene = new Scene(htmlEditor);
				canvas.setScene(scene);
			}
		});

		fileInputEditor = (FileEditorInput) this.getEditorInput();

		ifile = fileInputEditor.getFile();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(ifile.getContents(), Charset.defaultCharset()));
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String content = null;
		StringBuffer buff = new StringBuffer();

		try {
			while ((content = reader.readLine()) != null) {

				// System.out.println(content);
				buff.append(content);

			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		htmlEditor.setHtmlText(buff.toString());

		buff = null;

		int index = addPage(composite);
		setPageText(index, "GUI Editor");

	}

	public void setCustomActions(final HTMLEditor htmlEditor) {
		// add a custom button to the top toolbar.
		Node node = htmlEditor.lookup(".top-toolbar");
		if (node instanceof ToolBar) {
			ToolBar bar = (ToolBar) node;

			Button browserButton = new Button("Open In Browser");

			Button linkButton = new Button("Link");

			Button imageButton = new Button("Image");

			Button knitrButton = new Button("Knitr");

			bar.getItems().add(browserButton);
			bar.getItems().add(linkButton);
			bar.getItems().add(imageButton);
			bar.getItems().add(knitrButton);

			browserButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					String docTemp = htmlEditor.getHtmlText();
					Document docHtml = Jsoup.parse(docTemp);
					/* Remove the content editable attribute! */
					Elements elements = docHtml.select("body");
					elements.removeAttr("contenteditable");
					/*
					 * Write the changes to the file with the help of the ApacheIO lib!
					 */
					String fileLocation = null;
					try {

						fileLocation = ifile.getLocationURI().toURL().toString();
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						/* Convert IFile location to file! */
						File fi = ifile.getRawLocation().makeAbsolute().toFile();
						FileUtils.writeStringToFile(fi, docHtml.html());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Display display = PlatformUI.getWorkbench().getDisplay();
					display.syncExec(new Runnable() {
						public void run() {
							try {
								IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
								page.showView("com.eco.bio7.browser.Browser");
							} catch (PartInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

					BrowserView bv = BrowserView.getBrowserInstance();

					bv.browser.setUrl(fileLocation);
					bv.txt.setText(fileLocation);

				}
			});

			linkButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					Display display = PlatformUI.getWorkbench().getDisplay();
					display.syncExec(new Runnable() {
						public void run() {
							String url = "";
							InputDialog inp = new InputDialog(Util.getShell(), "Link", "Select", "Link", null);

							if (inp.open() == Dialog.OK) {

								url = inp.getValue();
								WebView webView = (WebView) htmlEditor.lookup("WebView");
								String selected = (String) webView.getEngine().executeScript("window.getSelection().toString();");
								String hyperlinkHtml = "<a href=\"" + url.trim() + "\" title=\"" + selected + "\" target=\"_blank\">" + selected + "</a>";
								try {
									webView.getEngine().executeScript(getInsertHtmlAtCurstorJS(hyperlinkHtml));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									// e.printStackTrace();
								}

							}

						}
					});

				}
			});
			imageButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					Display display = PlatformUI.getWorkbench().getDisplay();
					display.syncExec(new Runnable() {
						public void run() {
							String imageLocation = "";
							InputDialog inp = new InputDialog(Util.getShell(), "Image path", "Enter image path", "image/yourImage.jpg", null);

							if (inp.open() == Dialog.OK) {

								imageLocation = inp.getValue();
								WebView webView = (WebView) htmlEditor.lookup("WebView");

								try {
									webView.getEngine().executeScript(getInsertHtmlAtCurstorJS("<img alt=\"Image\" src=\"" + imageLocation + "\"/>"));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						}
					});

				}
			});

			knitrButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					Display display = PlatformUI.getWorkbench().getDisplay();
					display.syncExec(new Runnable() {
						public void run() {

							/* Insert HTML layer with JavaScript at selected cursor location! */
							String knitrCode = "<p>";

							WebView webView = (WebView) htmlEditor.lookup("WebView");
							try {
								// webView.getEngine().executeScript(getInsertHtmlAtCurstorJS("<!--begin.rcode "+knitrCode+" end.rcode-->"));
								webView.getEngine().executeScript(getInsertHtmlAtCurstorJS("<br><div id=\"knitrcode\" style=\"color: black; background-color: lightgrey; border: 1px solid grey;\">" + knitrCode + "<br></div></br>"));

							} catch (Exception e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();
							}

						}
					});

				}
			});

		}
	}

	/*
	 * Source from: http://rajeshkumarsahanee.wordpress.com/author/rajeshsahanee/
	 */
	private String getInsertHtmlAtCurstorJS(String html) {
		return "insertHtmlAtCursor('" + html + "');" + "function insertHtmlAtCursor(html) {\n" + " var range, node;\n" + " if (window.getSelection && window.getSelection().getRangeAt) {\n" + " window.getSelection().deleteFromDocument();\n" + " range = window.getSelection().getRangeAt(0);\n"
				+ " node = range.createContextualFragment(html);\n" + " range.insertNode(node);\n" + " } else if (document.selection && document.selection.createRange) {\n" + " document.selection.createRange().pasteHTML(html);\n" + " document.selection.clear();" + " }\n" + "}";
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();

		setActivePage(1);

	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code> method disposes all nested editors. Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document. Also changes the default HTML code (removes contenteditable arg!).
	 */
	public void doSave(IProgressMonitor monitor) {
		IEditorInput ed = getEditor(1).getEditorInput();

		/*
		 * IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(ed); SourceFormatter sf = formatHtml(); String
		 * docc=sf.toString().replace("<body contenteditable=\"true\">","<body>"); doc.set(docc);
		 */
		getEditor(1).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this multi-page editor's input to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(1);
		editor.doSaveAs();
		setPageText(1, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(1);
		IDE.gotoMarker(getEditor(1), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * Workaround to close and reopen Outline for multipage editor is cited here:
	 * http://stackoverflow.com/questions/24694269/how-to-keep-off-multipageeditor-with-structuredtexteditor-to-show-outline-view-f I added setActivePage() method so that the Outline is available if
	 * the multipage editor has been opened.
	 */

	public void refreshOutlineView() {
		// get the activePage
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		// Find desired view by its visual ID
		IViewPart myView = wp.findView("org.eclipse.ui.views.ContentOutline");

		// Hide the view :
		wp.hideView(myView);
		try {
			// show the view again
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ContentOutline");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 0) {
			IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(getEditor(1).getEditorInput());

			htmlEditor.setHtmlText(doc.get());
		} else if (newPageIndex == 1) {

			String parsed = formatHtml();

			IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(getEditor(1).getEditorInput());

			doc.set(parsed);

		}
		refreshOutlineView();
	}

	private String formatHtml() {
		String t = htmlEditor.getHtmlText();
		Document sf = Jsoup.parse(t);
		Source s = new Source(t);
		// SourceFormatter sf = new SourceFormatter(s);
		return sf.html();
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

}
