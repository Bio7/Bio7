/*******************************************************************************
 * Copyright (c) 2007-2012 M. Austenfeld
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     M. Austenfeld
 *******************************************************************************/

package com.eco.bio7.rcp;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.EditorAreaDropAdapter;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipse.ui.part.MarkerTransfer;
import org.eclipse.ui.part.ResourceTransfer;
import org.osgi.framework.Bundle;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.eco.bio7.Bio7Plugin;
import com.eco.bio7.actions.Bio7Action;
import com.eco.bio7.batch.BatchModel;
import com.eco.bio7.batch.Bio7Dialog;
import com.eco.bio7.compile.BeanShellInterpreter;
import com.eco.bio7.compile.CompileClassAndMultipleClasses;
import com.eco.bio7.compile.GroovyInterpreter;
import com.eco.bio7.compile.PythonInterpreter;
import com.eco.bio7.compile.RInterpreterJob;
import com.eco.bio7.compile.utils.ScanClassPath;
import com.eco.bio7.console.ConsolePageParticipant;
import com.eco.bio7.discrete.Quad2d;
import com.eco.bio7.javaeditor.Bio7EditorPlugin;
import com.eco.bio7.jobs.LoadData;
import com.eco.bio7.popup.actions.RecalculateClasspath;
import com.eco.bio7.preferences.PreferenceConstants;
import com.eco.bio7.preferences.RServePlotPrefs;
import com.eco.bio7.preferences.Reg;
import com.eco.bio7.rbridge.RServe;
import com.eco.bio7.rbridge.RState;
import com.eco.bio7.rbridge.actions.StartRServe;
import com.eco.bio7.rbridge.debug.REditorListener;
import com.eco.bio7.time.CalculationThread;
import com.eco.bio7.util.Util;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static String OS;
	// protected String[] fileList;
	private boolean x11ErrorHandlerFixInstalled = false;
	private IExecutionListener executionListener;
	private IPartListener2 partListener;

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);

		// WorkbenchAdapterBuilder.registerAdapters();
		declareWorkbenchImages();
		IDE.registerAdapters();
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResourceChangeListener listener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {

				if (event == null || event.getDelta() == null) {

					return;
				}
				if (event.getType() != IResourceChangeEvent.POST_CHANGE)
					return;

				else {
					try {
						event.getDelta().accept(new IResourceDeltaVisitor() {
							boolean decision = false;

							public boolean visit(IResourceDelta delta) throws CoreException {
								if (delta.getKind() == IResourceDelta.ADDED) {

									final IResource resource = delta.getResource();
									if (resource.getType() == 4) {

										IProject project = resource.getProject();
										if (project.isOpen()) {

											if (project.hasNature(JavaCore.NATURE_ID)) {
												Display display = PlatformUI.getWorkbench().getDisplay();
												display.asyncExec(new Runnable() {
													public void run() {

														MessageBox message = new MessageBox(new Shell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
														message.setMessage("Recalculate the classpath?\n\n" + "Info: This will set the imported Bio7 Java project classpath\n"
																+ "to your local installation.");
														message.setText("Bio7");
														int response = message.open();
														if (response == SWT.YES) {

															IJavaProject javaProject = JavaCore.create(project);

															IFolder sourceFolder = project.getFolder("src");
															IPackageFragmentRoot fragRoot = javaProject.getPackageFragmentRoot(sourceFolder);

															List<IClasspathEntry> entriesJre = new ArrayList<IClasspathEntry>();

															IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();

															LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
															for (LibraryLocation element : locations) {
																
																entriesJre.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
															}
															IClasspathEntry[] newEntries = new ScanClassPath().scanForJDT();

															IClasspathEntry[] oldEntries = entriesJre.toArray(new IClasspathEntry[entriesJre.size()]);

															System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
															newEntries[oldEntries.length] = JavaCore.newSourceEntry(fragRoot.getPath());

															try {
																javaProject.setRawClasspath(newEntries, null);
															} catch (JavaModelException e) {
																// Auto-generated
																// catch block
																// e.printStackTrace();
																System.out.println("Minor error! Please check the classpath of the project and if necessary calculate again!");
															}
															System.out.println("Java Bio7 Project Libraries Recalculated!");
															//Bio7Dialog.message("Java Bio7 Project Libraries Recalculated!");
														} else {

														}
														
													}
												});

											}
										}
									}

									
								}
								return true;
							}
						});
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		};

		workspace.addResourceChangeListener(listener);

		// ... some time later one ...
		// workspace.removeResourceChangeListener(listener);

		IWorkbenchActivitySupport workbenchActivitySupport = PlatformUI.getWorkbench().getActivitySupport();
		IActivityManager activityManager = workbenchActivitySupport.getActivityManager();

		String osname = System.getProperty("os.name");
		if (osname.startsWith("Windows")) {
			OS = "Windows";
		} else if (osname.equals("Linux")) {
			OS = "Linux";
		} else if (osname.startsWith("Mac")) {
			OS = "Mac";
		}

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		// configurer.setSaveAndRestore( false );
		configurer.setInitialSize(new Point(1024, 768));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(true);
		configurer.setShowPerspectiveBar(true);
		configurer.setShowProgressIndicator(true);
		// Drag and drop support for the editor area!
		configurer.addEditorAreaTransfer(EditorInputTransfer.getInstance());
		configurer.addEditorAreaTransfer(ResourceTransfer.getInstance());
		configurer.addEditorAreaTransfer(FileTransfer.getInstance());
		configurer.addEditorAreaTransfer(MarkerTransfer.getInstance());
		configurer.configureEditorAreaDropListener(new EditorAreaDropAdapter(configurer.getWindow()));

		// PreferenceManager preferenceManager =
		// configurer.getWorkbenchConfigurer().getWorkbench().getPreferenceManager
		// ();

		// Remove unused preference pages by ID:
		/*
		 * preferenceManager.remove("org.eclipse.help.ui.browsersPreferencePage")
		 * ;preferenceManager.remove(
		 * "org.eclipse.update.internal.ui.preferences.MainPreferencePage");
		 */

		// Listen to changed perspective !!!!
		configurer.getWindow().addPerspectiveListener(new PerspectiveAdapter() {
			public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {

				if (perspective.getId().equals("com.eco.bio7.perspective_image")) {

				}

			}

			public void perspectiveSavedAs(IWorkbenchPage page, IPerspectiveDescriptor oldPerspective, IPerspectiveDescriptor newPerspective) {

			}

			public void perspectiveDeactivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
				if (perspective.getId().equals("com.eco.bio7.perspective_image")) {

				}
			}
		});

		/*
		 * Listen to the R editor if debugging actions should be added to the
		 * console toolbar!
		 */

		configurer.getWindow().getPartService().addPartListener(new REditorListener().listen());

		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_SYSTEM_JOBS, false);
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR, IWorkbenchPreferenceConstants.TOP_LEFT);
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_INTRO, true);
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_MEMORY_MONITOR, true);
		// Option for Bio7 1.6!
		// WorkbenchPlugin.getDefault().getPreferenceStore().setValue("RUN_IN_BACKGROUND",
		// true);

		IPreferenceStore store = Bio7Plugin.getDefault().getPreferenceStore();

		IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
		registry.setDefaultEditor("*.Rnw", "org.eclipse.ui.DefaultTextEditor");

		// Important to set the path for the database.
		// **********************************************************************
		// ****

		Bundle bundle = Platform.getBundle("com.eco.bio7");

		// String path = bundle.getLocation().split(":")[2];
		URL locationUrl = FileLocator.find(bundle, new Path("/"), null);
		URL fileUrl = null;
		try {
			fileUrl = FileLocator.toFileURL(locationUrl);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String path = fileUrl.getFile();

		// System.out.println(path);
		File fileStartupScripts = new File(path + "/startup_scripts");
		File fileImportScripts = new File(path + "/importscripts");
		File fileExportScripts = new File(path + "/export_scripts");
		File fileGeneralScripts = new File(path + "/scripts");
		File fileSpatialScripts = new File(path + "/spatial_scripts");
		File fileImageScripts = new File(path + "/image_scripts");
		File fileRScripts = new File(path + "/r_scripts");
		File fileRShellScripts = new File(path + "/r_shell_scripts");
		File fileGridScripts = new File(path + "/grid_scripts");

		/* Folder for the temp *.RData file! */
		File fileTempR = new File(fileUrl.getPath());
		String pathTempR = fileTempR.toString();

		String reg1 = "";
		String reg2 = "";

		if (getOS().equals("Windows")) {
			reg1 = Reg.setPrefReg(PreferenceConstants.PATH_R);
			reg2 = Reg.setPrefReg(PreferenceConstants.PATH_LIBREOFFICE);
			if (reg1 != null) {
				store.setDefault(PreferenceConstants.PATH_R, reg1);
				/* Default install location for the packages! */
				store.setDefault("InstallLocation", reg1 + "\\site-library");
				store.setDefault("SweaveScriptLocation", reg1 + "/share/texmf/tex/latex");
				store.setDefault("pdfLatex", "C:/");
				store.setDefault("RSERVE_ARGS", "");

				if (is64BitVM()) {
					store.setDefault("r_pipe_path", reg1 + "\\bin\\x64");
				} else {
					store.setDefault("r_pipe_path", reg1 + "\\bin\\i386");
				}

			}
			if (reg2 != null) {
				store.setDefault(PreferenceConstants.PATH_LIBREOFFICE, reg2);
			} else {
				/*
				 * If the path cannot be found in the reg. it will be set to C:\
				 * -> see com.eco.bio7.preferences.Reg.java!
				 */
				store.setDefault(PreferenceConstants.PATH_LIBREOFFICE, "C:\\");
			}
		} else if (getOS().equals("Linux")) {
			store.setDefault(PreferenceConstants.PATH_LIBREOFFICE, reg2);
			reg1 = "/usr/lib/R";
			reg2 = "/usr/lib/libreoffice/program";
			store.setDefault(PreferenceConstants.PATH_R, reg1);
			store.setDefault(PreferenceConstants.PATH_LIBREOFFICE, reg2);
			store.setDefault("InstallLocation", "/usr/lib/R/site-library");
			store.setDefault("SweaveScriptLocation", "/usr/share/R/share/texmf/tex/latex");
			store.setDefault("pdfLatex", "/usr/bin");
			store.setDefault("RSERVE_ARGS", "");

			if (is64BitVM()) {
				store.setDefault("r_pipe_path", reg1 + "/bin");
			} else {
				store.setDefault("r_pipe_path", reg1 + "/bin");
			}

		} else if (getOS().equals("Mac")) {
			reg2 = "/usr/lib/openoffice/program";
			store.setDefault(PreferenceConstants.PATH_LIBREOFFICE, reg2);

		}
		
		//store.setDefault("R_STARTUP_ARGS","try(options(max.print=5000)\n)");
		store.setDefault("SHOW_JDT_GUI",false);
		store.setDefault("datatablesize", 100);
		store.setDefault(PreferenceConstants.D_STRING, fileStartupScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_IMPORT, fileImportScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_EXPORT, fileExportScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_SCRIPT_GENERAL, fileGeneralScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_SCRIPT_SPATIAL, fileSpatialScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_SCRIPT_IMAGE, fileImageScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_SCRIPT_R, fileRScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_RSHELL_SCRIPTS, fileRShellScripts.getAbsolutePath());
		store.setDefault(PreferenceConstants.D_GRID_SCRIPTS, fileGridScripts.getAbsolutePath());

		if (getOS().equals("Windows")) {
			String pathTempR2 = pathTempR + "\\bio7temp\\";
			// String pathTempR3 = pathTempR2.replace("\\", "\\\\");
			store.setDefault(PreferenceConstants.P_TEMP_R, pathTempR2);
			store.setDefault("Console_Encoding", "CP850");
			store.setDefault("DEVICE_DEFINITION", "bio7Device <- function(filename = \"" + pathTempR2 + "tempDevicePlot%05d.tiff"
					+ "\") { tiff(filename,width = 480, height = 480, units = \"px\")}; options(device=\"bio7Device\")");
			store.setDefault("DEVICE_FILENAME", "");
			store.setDefault("PLOT_DEVICE_SELECTION", "PLOT_IMAGE");
		} else if (getOS().equals("Linux")) {
			pathTempR = pathTempR + "/bio7temp/";
			store.setDefault(PreferenceConstants.P_TEMP_R, pathTempR);
			store.setDefault("Console_Encoding", "UTF-8");
			store.setDefault("shell_arguments", "");
			store.setDefault("DEVICE_DEFINITION", "bio7Device <- function(filename = \"" + pathTempR + "tempDevicePlot%05d.tiff"
					+ "\") { tiff(filename,width = 480, height = 480, units = \"px\")}; options(device=\"bio7Device\")");
			store.setDefault("DEVICE_FILENAME", "");
			store.setDefault("PLOT_DEVICE_SELECTION", "PLOT_IMAGE");
		} else if (getOS().equals("Mac")) {
			pathTempR = pathTempR + "/bio7temp/";
			store.setDefault(PreferenceConstants.P_TEMP_R, pathTempR);
			store.setDefault("Console_Encoding", "UTF-8");
			store.setDefault("shell_arguments", "export TERM=xterm");
			store.setDefault("DEVICE_DEFINITION", "bio7Device <- function(filename = \"" + pathTempR + "tempDevicePlot%05d.tiff"
					+ "\") { tiff(filename,width = 480, height = 480, type=\"cairo\")}; options(device=\"bio7Device\")");
			store.setDefault("DEVICE_FILENAME", "");
			store.setDefault("PLOT_DEVICE_SELECTION", "PLOT_IMAGE");
		}
		store.setDefault("RSERVE_AUTOSTART", false);
		store.setDefault(PreferenceConstants.PACKAGE_R_SERVER, "http://cran.r-project.org");
		if (getOS().equals("Linux")) {
			store.setDefault(PreferenceConstants.D_OPENOFFICE_HEAD, "Ä, ,ä,Ö,ö,Ü,ü,+,!,§,$,%,&,/,(,),=,?,[,],°,^,;,:,>,<,|,*,µ,\\,@,\",“,¸,`,~,#,},{,¹,²,³,_,-");
		} else if (getOS().equals("Mac")) {
			store.setDefault(PreferenceConstants.D_OPENOFFICE_HEAD, "Ä, ,ä,Ö,ö,Ü,ü,+,!,§,$,%,&,/,(,),=,?,[,],°,^,;,:,>,<,|,*,µ,\\,@,\",“,¸,`,~,#,},{,¹,²,³,_,-");
		} else {
			store.setDefault(PreferenceConstants.D_OPENOFFICE_HEAD, "�, ,�,�,�,�,�,+,!,�,�,$,%,&,/,(,),=,?,[,],�,^,;,:,>,<,|,*,�,\\,�,@,\",�,�,�,`,~,#,},{,�,�,_,-");
		}

		store.setDefault("RSERVE_NATIVE_START", true);
		store.setDefault("R_DEBUG_PORT", 21555);

		store.setDefault("LINUX_SHELL", "GNOME");
		store.setDefault("PDF_READER", "ACROBAT");

		store.setDefault("USE_CUSTOM_DEVICE", true);

		store.setDefault("CLOSE_DEVICE", "if(length(dev.list())>0) dev.off()");

		store.setDefault("blender_args", "--window-geometry 0 0 600 600");
		store.setDefault("rcmdinstall", "--build");
		store.setDefault("REMOTE", false);
		store.setDefault("HOST", "127.0.0.1");
		store.setDefault("TCP", 6311);
		store.setDefault("USERNAME", "bio7");
		store.setDefault("PASSWORD", "admin");

		store.setDefault("DEFAULT_DIGITS", 15);
		store.setDefault("TRANSFER_METHOD", false);

		store.setDefault("REPAINT_QUAD", true);
		store.setDefault("REPAINT_HEX", true);
		store.setDefault("RECORD_VALUES", true);
		store.setDefault("STARTUP_SCRIPTS", false);
		store.setDefault("blender_options", "interactive");
		store.setDefault("before_script_blender", "import bpy;bpy.ops.object.select_all(action='SELECT');bpy.ops.object.delete()");
		store.setDefault("after_script_blender", "bpy.ops.render.render();bpy.ops.wm.redraw_timer(type='DRAW_WIN_SWAP', iterations=1)");

		/* A default FPS setting for the 3d view! */
		store.setDefault("fixedFps", 60);

		/*
		 * Default Colours for the Bio7 editors!
		 */
		String font = null;
		int fsize = 10;

		if (getOS().equals("Windows")) {
			PreferenceConverter.setDefault(store, "RShellFonts", new FontData("Courier New", 9, SWT.NONE));
			PreferenceConverter.setDefault(store, "Bio7ShellFonts", new FontData("Courier New", 10, SWT.NONE));
			font = "Courier New";
			fsize = 10;
		} else if (getOS().equals("Linux")) {
			PreferenceConverter.setDefault(store, "RShellFonts", new FontData("Courier New", 9, SWT.NONE));
			PreferenceConverter.setDefault(store, "Bio7ShellFonts", new FontData("Courier New", 10, SWT.NONE));
			font = "Courier New";
			fsize = 10;

		} else if (getOS().equals("Mac")) {
			PreferenceConverter.setDefault(store, "RShellFonts", new FontData("Monaco", 11, SWT.NONE));
			PreferenceConverter.setDefault(store, "Bio7ShellFonts", new FontData("Monaco", 11, SWT.NONE));
			font = "Monaco";
			fsize = 11;

		}

		/*
		 * IPreferenceStore storeBsh =
		 * BeanshellEditorPlugin.getDefault().getPreferenceStore();
		 * PreferenceConverter.setDefault(storeBsh, "colourkey", new RGB(127, 0,
		 * 85)); PreferenceConverter.setDefault(storeBsh, "colourkey1", new
		 * RGB(127, 0, 85)); PreferenceConverter.setDefault(storeBsh,
		 * "colourkey2", new RGB(42, 0, 255));
		 * PreferenceConverter.setDefault(storeBsh, "colourkey3", new RGB(128,
		 * 128, 128)); PreferenceConverter.setDefault(storeBsh, "colourkey4",
		 * new RGB(0, 0, 0)); PreferenceConverter.setDefault(storeBsh,
		 * "colourkey5", new RGB(0, 0, 0));
		 * PreferenceConverter.setDefault(storeBsh, "colourkey6", new RGB(0, 0,
		 * 0)); PreferenceConverter.setDefault(storeBsh, "colourkey7", new
		 * RGB(0, 0, 0)); // PreferenceConverter.setDefault(storeBsh,
		 * "colourkey8", new RGB(50, // 150, 150));
		 * 
		 * PreferenceConverter.setDefault(storeBsh, "colourkeyfont", new
		 * FontData(font, fsize, 1)); PreferenceConverter.setDefault(storeBsh,
		 * "colourkeyfont1", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeBsh, "colourkeyfont2", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeBsh,
		 * "colourkeyfont3", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeBsh, "colourkeyfont4", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeBsh,
		 * "colourkeyfont5", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeBsh, "colourkeyfont6", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeBsh,
		 * "colourkeyfont7", new FontData(font, fsize, 0));
		 */
		// PreferenceConverter.setDefault(storeBsh, "colourkeyfont8", new
		// FontData("Courier New", 10, 0));

		/*
		 * IPreferenceStore storePython =
		 * PythonEditorPlugin.getDefault().getPreferenceStore();
		 * PreferenceConverter.setDefault(storePython, "colourkey", new RGB(127,
		 * 0, 85)); PreferenceConverter.setDefault(storePython, "colourkey1",
		 * new RGB(127, 0, 85)); PreferenceConverter.setDefault(storePython,
		 * "colourkey2", new RGB(42, 0, 255));
		 * PreferenceConverter.setDefault(storePython, "colourkey3", new
		 * RGB(128, 128, 128)); PreferenceConverter.setDefault(storePython,
		 * "colourkey4", new RGB(0, 0, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkey5", new RGB(0,
		 * 0, 0)); // PreferenceConverter.setDefault(storePython, "colourkey6",
		 * new RGB(0, // 0, 0)); PreferenceConverter.setDefault(storePython,
		 * "colourkey7", new RGB(0, 0, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkey8", new RGB(0,
		 * 0, 0)); PreferenceConverter.setDefault(storePython, "colourkey9", new
		 * RGB(0, 0, 0)); PreferenceConverter.setDefault(storePython,
		 * "colourkey10", new RGB(0, 0, 0)); //
		 * PreferenceConverter.setDefault(storePython, "colourkey11", new RGB(0,
		 * // 0, 0)); PreferenceConverter.setDefault(storePython, "colourkey12",
		 * new RGB(0, 0, 0));
		 * 
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont", new
		 * FontData(font, fsize, 1));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont1", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont2", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont3", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont4", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont5", new
		 * FontData(font, fsize, 0)); //
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont6", new //
		 * FontData("Courier New", 10, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont7", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont8", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont9", new
		 * FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont10", new
		 * FontData(font, fsize, 0)); //
		 * PreferenceConverter.setDefault(storePython, "colourkeyfon11", new //
		 * FontData("Courier New", 10, 0));
		 * PreferenceConverter.setDefault(storePython, "colourkeyfont12", new
		 * FontData(font, fsize, 0));
		 */

		/*
		 * IPreferenceStore storeR =
		 * Bio7REditorPlugin.getDefault().getPreferenceStore();
		 * PreferenceConverter.setDefault(storeR, "colourkey", new RGB(127, 0,
		 * 85)); PreferenceConverter.setDefault(storeR, "colourkey1", new
		 * RGB(127, 0, 85)); PreferenceConverter.setDefault(storeR,
		 * "colourkey2", new RGB(42, 0, 255));
		 * PreferenceConverter.setDefault(storeR, "colourkey3", new RGB(128,
		 * 128, 128)); PreferenceConverter.setDefault(storeR, "colourkey4", new
		 * RGB(0, 0, 0)); PreferenceConverter.setDefault(storeR, "colourkey5",
		 * new RGB(0, 0, 0)); PreferenceConverter.setDefault(storeR,
		 * "colourkey6", new RGB(0, 0, 0));
		 * PreferenceConverter.setDefault(storeR, "colourkey7", new RGB(0, 0,
		 * 0)); PreferenceConverter.setDefault(storeR, "colourkey8", new RGB(0,
		 * 0, 0));
		 * 
		 * PreferenceConverter.setDefault(storeR, "colourkeyfont", new
		 * FontData(font, fsize, 1)); PreferenceConverter.setDefault(storeR,
		 * "colourkeyfont1", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeR, "colourkeyfont2", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeR,
		 * "colourkeyfont3", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeR, "colourkeyfont4", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeR,
		 * "colourkeyfont5", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeR, "colourkeyfont6", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeR,
		 * "colourkeyfont7", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeR, "colourkeyfont8", new
		 * FontData(font, fsize, 0));
		 */

		IPreferenceStore storeJava = Bio7EditorPlugin.getDefault().getPreferenceStore();

		storeJava.setDefault("classbody", false);
		storeJava.setDefault("compiler_version", 1.8);
		storeJava.setDefault("compiler_debug", false);
		storeJava.setDefault("compiler_verbose", false);
		storeJava.setDefault("compiler_warnings", false);

		/*
		 * PreferenceConverter.setDefault(storeJava, "colourkey", new RGB(127,
		 * 0, 85)); PreferenceConverter.setDefault(storeJava, "colourkey1", new
		 * RGB(127, 0, 85)); PreferenceConverter.setDefault(storeJava,
		 * "colourkey2", new RGB(42, 0, 255));
		 * PreferenceConverter.setDefault(storeJava, "colourkey3", new RGB(128,
		 * 128, 128)); PreferenceConverter.setDefault(storeJava, "colourkey4",
		 * new RGB(0, 0, 0)); PreferenceConverter.setDefault(storeJava,
		 * "colourkey5", new RGB(0, 0, 0));
		 * PreferenceConverter.setDefault(storeJava, "colourkey6", new RGB(0, 0,
		 * 0)); PreferenceConverter.setDefault(storeJava, "colourkey7", new
		 * RGB(0, 0, 0)); // PreferenceConverter.setDefault(storeJava,
		 * "colourkey8", new RGB(0, // 150, 150));
		 * 
		 * PreferenceConverter.setDefault(storeJava, "colourkeyfont", new
		 * FontData(font, fsize, 1)); PreferenceConverter.setDefault(storeJava,
		 * "colourkeyfont1", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeJava, "colourkeyfont2", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeJava,
		 * "colourkeyfont3", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeJava, "colourkeyfont4", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeJava,
		 * "colourkeyfont5", new FontData(font, fsize, 0));
		 * PreferenceConverter.setDefault(storeJava, "colourkeyfont6", new
		 * FontData(font, fsize, 0)); PreferenceConverter.setDefault(storeJava,
		 * "colourkeyfont7", new FontData(font, fsize, 0));
		 */
		// PreferenceConverter.setDefault(storeJava, "colourkeyfont8", new
		// FontData("Courier New", 10, 0));

		final String pathTo = store.getString(PreferenceConstants.P_TEMP_R);
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == "DEVICE_DEFINITION") {

					String value = event.getNewValue().toString();
					if (getOS().equals("Windows")) {

						value = value.replace("\\", "/");
					}
					if (RState.isBusy() == false) {
						RConnection con = RServe.getConnection();
						if (con != null) {
							try {
								con.eval(value);

							} catch (RserveException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Couldn't change R preferences-> Rserve is busy!");
					}
				} else if (event.getProperty() == "USE_CUSTOM_DEVICE") {
					String val = event.getNewValue().toString();
					if (RState.isBusy() == false) {
						RConnection con = RServe.getConnection();
						if (con != null) {
							try {
								if (val.equals("false")) {
									if (getOS().equals("Windows")) {
										con.eval("options(device=\"windows\")");
									} else if (getOS().equals("Linux")) {
										con.eval("options(device=\"x11\")");
									} else if (getOS().equals("Mac")) {
										con.eval("options(device=\"quartz\")");
									}
								} else {
									con.eval("options(device=\"bio7Device\")");
								}

							} catch (RserveException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Couldn't change R preferences-> Rserve is busy!");
					}

				}

				else if (event.getProperty() == "PLOT_DEVICE_SELECTION") {

					IPreferenceStore store2 = Bio7Plugin.getDefault().getPreferenceStore();

					String sel = store2.getString("PLOT_DEVICE_SELECTION");

					RServePlotPrefs prefsPlotRserve = RServePlotPrefs.getInstance();

					if (sel.equals("PLOT_IMAGE")) {

						prefsPlotRserve.mult.setStringValue("bio7Device <- function(filename = \"" + pathTo + "tempDevicePlot%05d.tiff"
								+ "\") { tiff(filename,width = 480, height = 480, units = \"px\")}; options(device=\"bio7Device\")");
						prefsPlotRserve.deviceFilename.setStringValue("");
						prefsPlotRserve.deviceFilename.setEnabled(false, prefsPlotRserve.getFieldEditorParentControl());
					} else if (sel.equals("PLOT_CAIRO")) {

						prefsPlotRserve.mult.setStringValue("bio7Device <- function(filename = \"" + pathTo + "tempDevicePlot%05d.tiff"
								+ "\") { tiff(filename,width = 480, height = 480, type=\"cairo\")}; options(device=\"bio7Device\")");
						prefsPlotRserve.deviceFilename.setStringValue("");
						prefsPlotRserve.deviceFilename.setEnabled(false, prefsPlotRserve.getFieldEditorParentControl());
					}

					else if (sel.equals("PLOT_PRINT")) {

						prefsPlotRserve.mult.setStringValue("bio7Device <- function(filename = \"" + pathTo + "tempDevicePlot%05d.tiff"
								+ "\") { tiff(filename,width = 6, height = 6, units=\"in\",res=600)}; options(device=\"bio7Device\")");
						prefsPlotRserve.deviceFilename.setStringValue("");
						prefsPlotRserve.deviceFilename.setEnabled(false, prefsPlotRserve.getFieldEditorParentControl());
					} else if (sel.equals("PLOT_PDF")) {

						prefsPlotRserve.mult.setStringValue("bio7Device <- function(filename = \"" + pathTo + "tempDevicePlot.pdf" + "\") { pdf(filename)}; options(device=\"bio7Device\")");
						prefsPlotRserve.deviceFilename.setStringValue("tempDevicePlot.pdf");
						prefsPlotRserve.deviceFilename.setEnabled(true, prefsPlotRserve.getFieldEditorParentControl());
					}

					else if (sel.equals("PLOT_SVG")) {

						prefsPlotRserve.mult.setStringValue("bio7Device <- function(filename = \"" + pathTo + "tempDevicePlot.svg" + "\") { svg(filename)}; options(device=\"bio7Device\")");
						prefsPlotRserve.deviceFilename.setStringValue("tempDevicePlot.svg");
						prefsPlotRserve.deviceFilename.setEnabled(true, prefsPlotRserve.getFieldEditorParentControl());

					} else if (sel.equals("PLOT_POSTSCRIPT")) {

						prefsPlotRserve.mult.setStringValue("bio7Device <- function(filename = \"" + pathTo + "tempDevicePlot.eps" + "\") { postscript(filename)}; options(device=\"bio7Device\")");
						prefsPlotRserve.deviceFilename.setStringValue("tempDevicePlot.eps");
						prefsPlotRserve.deviceFilename.setEnabled(true, prefsPlotRserve.getFieldEditorParentControl());
					}

				}
			}

		});

		// Initialize important classes for Bio7!!!
		initBio7();

	}

	private void setComponentFont() {

		Display dis = Display.getDefault();

		assert EventQueue.isDispatchThread(); // On AWT event thread

		FontData fontData = dis.getSystemFont().getFontData()[0];

		int resolution = Toolkit.getDefaultToolkit().getScreenResolution();

		int awtFontSize = (int) Math.round((double) fontData.getHeight() * resolution / 72.0);
		java.awt.Font awtFont = null;
		/* Font size Linux! */
		if (getOS().equals("Linux")) {
			awtFont = new java.awt.Font(fontData.getName(), fontData.getStyle(), awtFontSize);
		}

		else {
			awtFont = new java.awt.Font(fontData.getName(), fontData.getStyle(), awtFontSize);
		}

		// Update the look and feel defaults to use new font.
		updateLookAndFeel(awtFont);

	}

	private void updateLookAndFeel(java.awt.Font awtFont) {
		assert awtFont != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		FontUIResource fontResource = new FontUIResource(awtFont);

		UIManager.put("Button.font", fontResource); //$NON-NLS-1$
		UIManager.put("CheckBox.font", fontResource); //$NON-NLS-1$
		UIManager.put("ComboBox.font", fontResource); //$NON-NLS-1$
		UIManager.put("EditorPane.font", fontResource); //$NON-NLS-1$
		UIManager.put("Label.font", fontResource); //$NON-NLS-1$
		UIManager.put("List.font", fontResource); //$NON-NLS-1$
		UIManager.put("Panel.font", fontResource); //$NON-NLS-1$
		UIManager.put("ProgressBar.font", fontResource); //$NON-NLS-1$
		UIManager.put("RadioButton.font", fontResource); //$NON-NLS-1$
		UIManager.put("ScrollPane.font", fontResource); //$NON-NLS-1$
		UIManager.put("TabbedPane.font", fontResource); //$NON-NLS-1$
		UIManager.put("Table.font", fontResource); //$NON-NLS-1$
		UIManager.put("TableHeader.font", fontResource); //$NON-NLS-1$
		UIManager.put("TextField.font", fontResource); //$NON-NLS-1$
		UIManager.put("TextPane.font", fontResource); //$NON-NLS-1$
		UIManager.put("TitledBorder.font", fontResource); //$NON-NLS-1$
		UIManager.put("ToggleButton.font", fontResource); //$NON-NLS-1$
		UIManager.put("TreeFont.font", fontResource); //$NON-NLS-1$
		UIManager.put("ViewportFont.font", fontResource); //$NON-NLS-1$
		UIManager.put("MenuItem.font", fontResource); //$NON-NLS-1$
		UIManager.put("CheckboxMenuItem.font", fontResource); //$NON-NLS-1$
		UIManager.put("PopupMenu.font", fontResource); //$NON-NLS-1

		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, fontResource);
		}

	}

	private void initBio7() {
		setComponentFont();
		/* Set the system look and feel! */
		if (getOS().equals("Windows")) {

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		} else if (getOS().equals("Mac")) {

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}
		/* If Linux is the OS! */
		else {
			String lookAndFeel = Messages.getString("Swing.2");
			int lookSelection = Integer.parseInt(lookAndFeel);
			if (lookSelection == 2) {

				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); //$NON-NLS-1$
					// SwingUtilities.updateComponentTreeUI(this);
				} catch (Exception e) {
					System.out.println(e);
				}

			} else {

				MetalTheme theme = new Bio7LinuxTheme();

				MetalLookAndFeel.setCurrentTheme(theme);

				try {
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); //$NON-NLS-1$
					// SwingUtilities.updateComponentTreeUI(this);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}

		/* Create the instance of the discrete grid! */
		if (Quad2d.getQuad2dInstance() == null) {
			new Quad2d();
		}

		/* Start the calculation thread of the Bio7 application! */
		CalculationThread m = new CalculationThread();
		m.start();
		/*
		 * Important to set, see:
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=422258
		 */
		javafx.application.Platform.setImplicitExit(false);

	}

	private void startupScripts() {
		String startupDirectory = null;
		IPreferenceStore store = null;

		try {
			store = Bio7Plugin.getDefault().getPreferenceStore();
		} catch (RuntimeException e) {

			e.printStackTrace();
		}
		if (store != null) {
			startupDirectory = store.getString(PreferenceConstants.D_STRING);
		}

		if (startupDirectory != null && startupDirectory != "") {

			File[] files = new Util().ListFilesDirectory(new File(startupDirectory), new String[] { ".java", ".r", ".R", ".bsh", ".groovy", ".py" });
			System.out.println(files.length);
			if (files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					System.out.println(files[i].getName());
					if (files[i].getName().endsWith(".R") || files[i].getName().endsWith(".r")) {
						if (RServe.isAliveDialog()) {
							if (RState.isBusy() == false) {
								RState.setBusy(true);
								final RInterpreterJob Do = new RInterpreterJob(null, true, files[i].toString());
								Do.addJobChangeListener(new JobChangeAdapter() {
									public void done(IJobChangeEvent event) {
										if (event.getResult().isOK()) {
											int countDev = RServe.getDisplayNumber();
											RState.setBusy(false);
											if (countDev > 0) {
												RServe.closeAndDisplay();
											}
										}
									}
								});
								Do.setUser(true);
								Do.schedule();
							} else {

								Bio7Dialog.message("RServer is busy!");
							}

						}
					}

					else if (files[i].getName().endsWith(".bsh")) {

						BeanShellInterpreter.interpretJob(null, files[i].toString());

					} else if (files[i].getName().endsWith(".groovy")) {

						GroovyInterpreter.interpretJob(null, files[i].toString());

					} else if (files[i].getName().endsWith(".py")) {

						PythonInterpreter.interpretJob(null, files[i].toString());

					}

					else if (files[i].getName().endsWith(".java")) {

						final int count = i;

						Job job = new Job("Compile Java") {
							@Override
							protected IStatus run(IProgressMonitor monitor) {
								monitor.beginTask("Compile Java...", IProgressMonitor.UNKNOWN);
								String name = files[count].getName().replaceFirst("[.][^.]+$", "");
								// IWorkspace workspace =
								// ResourcesPlugin.getWorkspace();
								IPath location = Path.fromOSString(files[count].getAbsolutePath());

								// IFile ifile =
								// workspace.getRoot().getFileForLocation(location);
								CompileClassAndMultipleClasses cp = new CompileClassAndMultipleClasses();
								try {
									cp.compileAndLoad(new File(location.toOSString()), new File(location.toOSString()).getParent(), name, null, true);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									// Bio7Dialog.message(e.getMessage());
								}

								monitor.done();
								return Status.OK_STATUS;
							}

						};
						job.addJobChangeListener(new JobChangeAdapter() {
							public void done(IJobChangeEvent event) {
								if (event.getResult().isOK()) {

								} else {

								}
							}
						});
						// job.setSystem(true);
						job.schedule();

					}

				}

			}
		}

	}

	public File[] ListFileDirectory(File filedirectory) {
		File dir = filedirectory;

		String[] children = dir.list();
		if (children == null) {

		} else {
			for (int i = 0; i < children.length; i++) {

				String filename = children[i];
			}
		}

		// Filter the extension with the specified string.
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".groovy"));
			}
		};

		File[] files = dir.listFiles(filter);

		return files;
	}

	public void postWindowOpen() {

		super.postWindowOpen();
		
		

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		try {

			configurer.getWorkbenchConfigurer().getWorkbench().showPerspective("com.eco.bio7.perspective_image", configurer.getWindow());

			configurer.getWorkbenchConfigurer().getWorkbench().showPerspective("com.eco.bio7.rbridge.RPerspective", configurer.getWindow());

			configurer.getWorkbenchConfigurer().getWorkbench().showPerspective("com.eco.bio7.browser.SceneBuilderPerspective", configurer.getWindow());

			configurer.getWorkbenchConfigurer().getWorkbench().showPerspective("com.eco.bio7.bio7resource", configurer.getWindow());
			// *************************************
			new StartBio7Utils();
			// Start console and output!!
			StartBio7Utils.getConsoleInstance().startutils();
			// *************************************************
			/*
			 * If Bio7 should be customized at startup the startup scripts have
			 * to be enabled! The startup is faster without!
			 */
			IPreferenceStore store = Bio7Plugin.getDefault().getPreferenceStore();
			if (store.getBoolean("STARTUP_SCRIPTS")) {
				startupScripts();
			}

			if (store.getBoolean("RSERVE_AUTOSTART")) {
				Bio7Action.callRserve();
			}

		} catch (WorkbenchException e) {
			e.printStackTrace();
		}

		dragDropR();

		addExecutionListener();

	}

	/* The listener for save events of the Java editor! */
	public void addExecutionListener() {
		executionListener = new IExecutionListener() {
			public void notHandled(String commandId, NotHandledException exception) {

			}

			public void postExecuteFailure(String commandId, ExecutionException exception) {

			}

			public void postExecuteSuccess(String commandId, Object returnValue) {
				if (commandId.equals("org.eclipse.ui.file.save")) {

				}

			}

			public void preExecute(String commandId, ExecutionEvent event) {

			}
		};
		getCommandService().addExecutionListener(executionListener);

	}

	private ICommandService getCommandService() {
		return (ICommandService) PlatformUI.getWorkbench().getAdapter(ICommandService.class);
	}

	private void dragDropR() {
		Shell sh = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		DropTarget dt = new DropTarget(sh.getShell(), DND.DROP_DEFAULT | DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {

			public void drop(DropTargetEvent event) {

				FileTransfer ft = FileTransfer.getInstance();
				if (ft.isSupportedType(event.currentDataType)) {
					String[] fileListR = (String[]) event.data;
					if (fileListR[0].endsWith("RData")) {
						IPreferenceStore store = Bio7Plugin.getDefault().getPreferenceStore();
						boolean rPipe = store.getBoolean("r_pipe");

						if (rPipe == true) {
							String fileR;
							System.out.println(fileListR[0]);
							if (Bio7Dialog.getOS().equals("Windows")) {
								fileR = fileListR[0].replace("\\", "\\\\");
							} else {
								fileR = fileListR[0];
							}

							String selectionConsole = ConsolePageParticipant.getInterpreterSelection();
							if (selectionConsole.equals("R")) {

								ConsolePageParticipant.pipeInputToConsole("load(file =\"" + fileR + "\")", true, true);
							} else {
								Bio7Dialog.message("Please start the \"Native R\" shell in the Bio7 console!");
							}

						}

						else {

							if (RServe.isAlive() == false) {

								StartRServe.setFileList(fileListR);
								StartRServe.setFromDragDrop(true);
								/*
								 * Now we can start the server. Variable
								 * setFromDragDrop will be set to false in the
								 * StartRserve class after job has finished!
								 */
								Bio7Action.callRserve();

							} else {

								loadFile(fileListR);
							}

						}
					}
					/*
					 * Load an xml file for the discrete grid and quick
					 * compilation!
					 */
					else if (fileListR[0].endsWith("exml")) {
						LoadData.load(fileListR[0].toString());
					}

					else {
						MessageBox messageBox = new MessageBox(new Shell(),

						SWT.ICON_WARNING);
						messageBox.setMessage("File is not drag and \ndrop supported file for Bio7!");
						messageBox.open();

					}
				}
			}

		});

	}

	private void loadFile(String[] fileList) {
		String file;

		if (Bio7Dialog.getOS().equals("Windows")) {
			file = fileList[0].replace("\\", "\\\\");
		} else {
			file = fileList[0];
		}

		String load = "try(load(file =BIO7_TEMP))";
		if (RState.isBusy() == false) {
			RState.setBusy(true);
			try {
				RServe.getConnection().assign("BIO7_TEMP", file);
			} catch (RserveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RInterpreterJob Do = new RInterpreterJob(load, false, null);
			Do.addJobChangeListener(new JobChangeAdapter() {
				public void done(IJobChangeEvent event) {
					if (event.getResult().isOK()) {
						RState.setBusy(false);
					} else {
						RState.setBusy(false);
					}
				}
			});
			Do.setUser(true);
			Do.schedule();
		} else {

			Bio7Dialog.message("RServer is busy!");

		}

	}

	public void openIntro() {

	}

	private void declareWorkbenchImages() {

		final String ICONS_PATH = "$nl$/icons/full/";
		final String BIO7_PATH = "/icons/workbench/";
		final String PATH_ELOCALTOOL = ICONS_PATH + "elcl16/";
		// Enabled toolbar icons.
		final String PATH_ETOOL = ICONS_PATH + "etool16/"; // Enabled
		// toolbar icons.
		final String PATH_DTOOL = ICONS_PATH + "dtool16/"; // Disabled
		// toolbar icons.
		final String PATH_OBJECT = ICONS_PATH + "obj16/"; // Model
		// object icons
		final String PATH_WIZBAN = ICONS_PATH + "wizban/"; // Wizard
		final String PATH_EVIEW = ICONS_PATH + "eview16/"; // View icons
		// final String PATH_DLOCALTOOL = ICONS_PATH + "dlcl16/"; // Disabled

		Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);

		// Bio7 custom icons!

		Bundle bio7 = Platform.getBundle("com.eco.bio7");
		/* Image for the projects! */
		declareWorkbenchImage(bio7, IDE.SharedImages.IMG_OBJ_PROJECT, BIO7_PATH + "prj_obj.gif", false);
		declareWorkbenchImage(bio7, IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, BIO7_PATH + "file.gif", false);

		declareWorkbenchImage(bio7, IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFOLDER_WIZ, BIO7_PATH + "resource_persp.gif", false);
		/* Image for the printer! */
		declareWorkbenchImage(bio7, org.eclipse.ui.ISharedImages.IMG_ETOOL_PRINT_EDIT_DISABLED, BIO7_PATH + "print_edit.gif", false);
		declareWorkbenchImage(bio7, org.eclipse.ui.ISharedImages.IMG_ETOOL_PRINT_EDIT, BIO7_PATH + "print_edit.gif", false);
		/* Image for the folders! */
		declareWorkbenchImage(bio7, org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER, BIO7_PATH + "prj_obj.gif", false);

		// declareWorkbenchImage(ideBundle,
		// IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, PATH_OBJECT +
		// "cprj_obj.gif", true);
		declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OPEN_MARKER, PATH_ELOCALTOOL + "gotoobj_tsk.png", true);

		declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJS_TASK_TSK, PATH_OBJECT + "taskmrk_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJS_BKMRK_TSK, PATH_OBJECT + "bkmrk_tsk.png", true);

		String string = IDEInternalWorkbenchImages.IMG_OBJS_COMPLETE_TSK;
		declareWorkbenchImage(ideBundle, string, PATH_OBJECT + "complete_tsk.gif", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_INCOMPLETE_TSK, PATH_OBJECT + "incomplete_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WELCOME_ITEM, PATH_OBJECT + "welcome_item.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WELCOME_BANNER, PATH_OBJECT + "welcome_banner.png", true);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_BUILD_EXEC, PATH_ETOOL + "build_exec.png", false);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_BUILD_EXEC_HOVER, PATH_ETOOL + "build_exec.png", false);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_BUILD_EXEC_DISABLED, PATH_DTOOL + "build_exec.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_SEARCH_SRC, PATH_ETOOL + "search_src.png", false);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_SEARCH_SRC_HOVER, PATH_ETOOL + "search_src.png", false);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_SEARCH_SRC_DISABLED, PATH_DTOOL + "search_src.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_NEXT_NAV, PATH_ETOOL + "next_nav.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PREVIOUS_NAV, PATH_ETOOL + "prev_nav.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_NEWPRJ_WIZ, PATH_WIZBAN + "newprj_wiz.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFILE_WIZ, PATH_WIZBAN + "newfile_wiz.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_IMPORTDIR_WIZ, PATH_WIZBAN + "importdir_wiz.png", false);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_IMPORTZIP_WIZ, PATH_WIZBAN + "importzip_wiz.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_EXPORTDIR_WIZ, PATH_WIZBAN + "exportdir_wiz.png", false);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_EXPORTZIP_WIZ, PATH_WIZBAN + "exportzip_wiz.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_WIZBAN_RESOURCEWORKINGSET_WIZ, PATH_WIZBAN + "workset_wiz.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_DLGBAN_SAVEAS_DLG, PATH_WIZBAN + "saveas_wiz.png", false);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_DLGBAN_QUICKFIX_DLG, PATH_WIZBAN + "quick_fix.png", false);

		declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OPEN_MARKER, PATH_ELOCALTOOL + "gotoobj_tsk.png", true);

		declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJS_TASK_TSK, PATH_OBJECT + "taskmrk_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJS_BKMRK_TSK, PATH_OBJECT + "bkmrk_tsk.png", true);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_COMPLETE_TSK, PATH_OBJECT + "complete_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_INCOMPLETE_TSK, PATH_OBJECT + "incomplete_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WELCOME_ITEM, PATH_OBJECT + "welcome_item.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WELCOME_BANNER, PATH_OBJECT + "welcome_banner.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_ERROR_PATH, PATH_OBJECT + "error_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WARNING_PATH, PATH_OBJECT + "warn_tsk.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_INFO_PATH, PATH_OBJECT + "info_tsk.png", true);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_LCL_FLAT_LAYOUT, PATH_ELOCALTOOL + "flatLayout.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_LCL_HIERARCHICAL_LAYOUT, PATH_ELOCALTOOL + "hierarchicalLayout.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEM_CATEGORY, PATH_ETOOL + "problem_category.png", true);

		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW, PATH_EVIEW + "problems_view.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_ERROR, PATH_EVIEW + "problems_view_error.png", true);
		declareWorkbenchImage(ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_WARNING, PATH_EVIEW + "problems_view_warning.png", true);

		// declareWorkbenchImage(ideBundle,
		// IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFOLDER_WIZ, PATH_WIZBAN +
		// "newfolder_wiz.png", false);

		// declareWorkbenchImage(ideBundle, IDE.SharedImages.IMG_OBJ_PROJECT,
		// PATH_OBJECT + "prj_obj.gif", true);
		// declareWorkbenchImage(ideBundle,
		// IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, PATH_OBJECT +
		// "cprj_obj.gif", true);

		// QuickFix images

		// declareWorkbenchImage(ideBundle,
		// IDEInternalWorkbenchImages.IMG_ELCL_QUICK_FIX_ENABLED,
		// PATH_ELOCALTOOL + "smartmode_co.gif", true);

		// declareWorkbenchImage(ideBundle,
		// IDEInternalWorkbenchImages.IMG_DLCL_QUICK_FIX_DISABLED,
		// PATH_DLOCALTOOL + "smartmode_co.gif", true);

		// declareWorkbenchImage(ideBundle,
		// IDEInternalWorkbenchImages.IMG_OBJS_FIXABLE_WARNING, PATH_OBJECT +
		// "quickfix_warning_obj.gif", true);
		// declareWorkbenchImage(ideBundle,
		// IDEInternalWorkbenchImages.IMG_OBJS_FIXABLE_ERROR, PATH_OBJECT +
		// "quickfix_error_obj.gif", true);

		// Task images
		// declareRegistryImage(IDEInternalWorkbenchImages.IMG_OBJS_HPRIO_TSK,
		// PATH_OBJECT+"hprio_tsk.gif");
		// declareRegistryImage(IDEInternalWorkbenchImages.IMG_OBJS_MPRIO_TSK,
		// PATH_OBJECT+"mprio_tsk.gif");
		// declareRegistryImage(IDEInternalWorkbenchImages.IMG_OBJS_LPRIO_TSK,
		// PATH_OBJECT+"lprio_tsk.gif");

		/*
		 * declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_NEWPRJ_WIZ, PATH_WIZBAN +
		 * "newprj_wiz.gif", false); declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFOLDER_WIZ, BIO7_PATH +
		 * "ordner_zu.gif", false); declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_NEWFILE_WIZ, PATH_WIZBAN +
		 * "newfile_wiz.gif", false);
		 * 
		 * declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_IMPORTDIR_WIZ, PATH_WIZBAN +
		 * "importdir_wiz.gif", false); declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_IMPORTZIP_WIZ, PATH_WIZBAN +
		 * "importzip_wiz.gif", false);
		 * 
		 * declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_EXPORTDIR_WIZ, PATH_WIZBAN +
		 * "exportdir_wiz.gif", false); declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_WIZBAN_EXPORTZIP_WIZ, PATH_WIZBAN +
		 * "exportzip_wiz.gif", false);
		 * 
		 * declareWorkbenchImage(ideBundle,
		 * IDEInternalWorkbenchImages.IMG_DLGBAN_SAVEAS_DLG, PATH_WIZBAN +
		 * "saveas_wiz.gif", false);
		 */

	}

	private void declareWorkbenchImage(Bundle ideBundle, String symbolicName, String path, boolean shared) {
		URL url = Platform.find(ideBundle, new Path(path));
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.getWorkbenchConfigurer().declareImage(symbolicName, desc, shared);
	}

	public static String getOS() {
		return OS;
	}

	/* From: http://www.rgagnon.com/javadetails/java-0565.html */
	public static boolean is64BitVM() {
		String bits = System.getProperty("sun.arch.data.model", "?");
		if (bits.equals("64")) {
			return true;
		}
		if (bits.equals("?")) {
			// probably sun.arch.data.model isn't available
			// maybe not a Sun JVM?
			// try with the vm.name property
			return System.getProperty("java.vm.name").toLowerCase().indexOf("64") >= 0;
		}
		// probably 32bit
		return false;
	}

	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(false);
		declareWorkbenchImages();
	}

}
