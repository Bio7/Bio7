package com.eco.bio7.rcp;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.rosuda.REngine.Rserve.RConnection;

import com.eco.bio7.Bio7Plugin;
import com.eco.bio7.rbridge.RConnectionJob;
import com.eco.bio7.rbridge.RServe;
import com.eco.bio7.rbridge.RServeUtil;
import com.eco.bio7.rbridge.TerminateRserve;
import com.eco.bio7.util.Util;
import com.eco.bio7.worldwind.WorldWindView;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	//private static final String PERSPECTIVE_ID = "com.eco.bio7.perspective_2d";
	private static final String PERSPECTIVE_ID = "com.eco.bio7.rbridge.RPerspective";
	
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/* Save the configuration! */
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(false);
	}

	/* Refresh e.g. the Project Explorer when opening! */
	public IAdaptable getDefaultPageInput() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	public String getMainPreferencePageId() {

		return "com.eco.bio7.preferences.Bio7PreferencePage";
	}

	public boolean preShutdown() {
		boolean close = super.preShutdown();
		if (close) {
			close = true;
			/* Save all editors before shutdown! */
			// SAVE_ALL_EDITORS
			IPreferenceStore store = Bio7Plugin.getDefault().getPreferenceStore();
			boolean storeEditors = store.getBoolean("SAVE_ALL_EDITORS");
			boolean storeRWorkspace = store.getBoolean("SAVE_R_WORKSPACE_ON_QUIT");

			// Save the workspace and the editors!
			if (storeEditors) {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {

						monitor.beginTask("Saving editors...", IProgressMonitor.UNKNOWN);
						IWorkspace ws = ResourcesPlugin.getWorkspace();
						// NullProgressMonitor monitor = new NullProgressMonitor();
						IEditorPart[] dirtyEditors = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().getDirtyEditors();
						for (IEditorPart iEditorPart : dirtyEditors) {
							iEditorPart.doSave(monitor);
						}
						/* Here we store the workspace once more, mainly for MacOSX! */
						try {
							// status.merge(ws.save(true, monitor));
							ws.save(true, monitor);
						} catch (CoreException e) {
							e.printStackTrace();
						}

					}

				};
				try {
					new ProgressMonitorDialog(null).run(false, false, runnable);
				} catch (InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (storeRWorkspace) {
				IRunnableWithProgress runnableR = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {

						monitor.beginTask("Saving R workspace...", IProgressMonitor.UNKNOWN);
						/*
						 * We have to save the R editors here because else the Rserve connection will be
						 * lost because of the save file dialog of the editors!
						 */
						IEditorPart[] dirtyEditors = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().getDirtyEditors();
						for (IEditorPart iEditorPart : dirtyEditors) {
							iEditorPart.doSave(monitor);
						}
						/* Save the R workspace for the next session if enabled! */
						String command = store.getString("ON_QUIT_COMMAND");
						if (RServe.isAlive()) {
							RServeUtil.evalR(command, null);
						}
					}

				};

				try {
					new ProgressMonitorDialog(null).run(false, false, runnableR);
				} catch (InvocationTargetException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (RConnectionJob.getProc() != null) {
				RConnectionJob.getProc().destroy();// Kills the Rserve
				// application!"
			}

			terminate_rserve();

			/* Save the ImageJ preferences! */
			try {
				ij.Prefs.savePreferences();
			} catch (RuntimeException e) {

				e.printStackTrace();
			}

			saveWorkspace();

			/*
			 * Location instanceLoc = Platform.getInstanceLocation(); if (instanceLoc !=
			 * null) instanceLoc.release();
			 */

			/*
			 * IWorkbench workbench = PlatformUI.getWorkbench(); final IWorkbenchWindow
			 * window = workbench.getActiveWorkbenchWindow();
			 * 
			 * IPerspectiveRegistry perspectiveRegistry = window.getWorkbench()
			 * .getPerspectiveRegistry(); IPerspectiveDescriptor
			 * personalPerspectiveDescriptor = perspectiveRegistry
			 * .findPerspectiveWithId("com.eco.bio7.WorldWind.3dglobe");
			 * IPerspectiveDescriptor personalPerspectiveDescriptor2 = perspectiveRegistry
			 * .findPerspectiveWithId("com.eco.bio7.perspective_3d");
			 * 
			 * IWorkbenchPage wbp =
			 * PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 * 
			 * //wbp.hideView(wbp.findView("com.eco.bio7.worldwind.WorldWindOptionsView"));
			 * 
			 * wbp.closePerspective(personalPerspectiveDescriptor, false, true);
			 * 
			 * wbp.closePerspective(personalPerspectiveDescriptor2, false, true);
			 */
			/*
			 * if (Util.getOS().equals("Mac")) { IWorkbenchPage wbp =
			 * PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 * 
			 * wbp.hideView(wbp.findView("com.eco.bio7.image"));
			 * wbp.hideView(wbp.findView("com.eco.bio7.WorldWind"));
			 * wbp.hideView(wbp.findView("com.eco.bio7.3d"));
			 * 
			 * }
			 */

		}
		return close;
	}

	public void saveWorkspace() {

		// final MultiStatus status = new MultiStatus();
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				try {
					IWorkspace ws = ResourcesPlugin.getWorkspace();
					ws.save(true, monitor);
				} catch (CoreException e) {
					System.out.println(e.getMessage());
					// status.merge(e.getStatus());
				}
			}
		};

		try {
			new ProgressMonitorDialog(null).run(false, false, runnable);
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		// if (!status.isOK()){ ErrorDialog.openError(...); }

	}

	private void terminate_rserve() {
		IPreferenceStore store = Bio7Plugin.getDefault().getPreferenceStore();
		if (ApplicationWorkbenchWindowAdvisor.getOS().equals("Windows")) {

			TerminateRserve.killProcessWindows();

		} else if (ApplicationWorkbenchWindowAdvisor.getOS().equals("Linux")) {
			RConnection con = RServe.getConnection();
			if (con != null) {
				con.close();
				RServe.setConnection(null);
				WorldWindView.setRConnection(null);
			}
			/*
			 * Only killall Rserve instances if using the deprecated Rserve start function!
			 */
			/*
			 * if (store.getBoolean("RSERVE_NATIVE_START") == false) {
			 * TerminateRserve.killProcessLinux(); }
			 */

		} else if (ApplicationWorkbenchWindowAdvisor.getOS().equals("Mac")) {
			RConnection con = RServe.getConnection();
			if (con != null) {
				con.close();
				RServe.setConnection(null);
				WorldWindView.setRConnection(null);
			}
			/*
			 * Only killall Rserve instances if using the deprecated Rserve start function!
			 */
			/*
			 * if (store.getBoolean("RSERVE_NATIVE_START") == false) {
			 * TerminateRserve.killProcessMac(); }
			 */

		}
	}

}
