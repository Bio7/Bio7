package com.eco.bio7.r.packages;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.eco.bio7.batch.Bio7Dialog;
import com.eco.bio7.console.ConsolePageParticipant;
import com.eco.bio7.rbridge.RServe;
import com.eco.bio7.rbridge.RState;
import com.eco.bio7.rcp.ApplicationWorkbenchWindowAdvisor;

public class ExecuteDevtoolsCommand {

	public String packageName;
	public String[] includeRVariables;
	public boolean builtFromVariables;
	public boolean cancelCreation = false;
	private String loc;

	public ExecuteDevtoolsCommand(String command) {

		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		IStructuredSelection strucSelection = null;
		if (selection instanceof IStructuredSelection) {
			strucSelection = (IStructuredSelection) selection;

			Object selectedObj = strucSelection.getFirstElement();
			IResource resource = (IResource) strucSelection.getFirstElement();
			final IProject activeProject = resource.getParent().getProject();
			if (selectedObj instanceof IFolder || selectedObj instanceof IProject) {
				IFolder selectedFolder = null;

				if (selectedObj instanceof IProject) {
					IProject proj = (IProject) selectedObj;
					loc = proj.getLocation().toOSString();

				} else {
					selectedFolder = (IFolder) selectedObj;
					loc = selectedFolder.getLocation().toString();
				}
				if (ApplicationWorkbenchWindowAdvisor.getOS().equals("Windows")) {
					loc = loc.replace("\\", "/");
				}
				// System.out.println(loc);
				
				String rCommand = "library(devtools);setwd(\"" + loc + "\");" + command + "";
				if (RServe.isAlive()) {

					if (cancelCreation == false) {

						if (RState.isBusy() == false) {
							RState.setBusy(true);

							Job job = new Job("Roxygen") {
								REXPLogical bol = null;

								@Override
								protected IStatus run(IProgressMonitor monitor) {
									monitor.beginTask("Create Roxygen ...", IProgressMonitor.UNKNOWN);

									RConnection c = RServe.getConnection();

									try {
										bol = (REXPLogical) c.eval("require(devtools)");
									} catch (RserveException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									if (bol.isTRUE()[0] == false) {
										Bio7Dialog.message("Library 'devtools' required!\nPlease install the 'devtools' package!");
										return Status.OK_STATUS;
									}

									try {
										/*Store the current workspace!*/
										c.eval("try(.tempCurrentWd<-getwd());");
										/* Remove the temporary variable! */
										c.eval(rCommand);
										/*Restore the current workspace!*/
										c.eval("try(setwd(.tempCurrentWd));");

									} catch (RserveException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

									if ((selectedObj instanceof IProject) == false) {
										IProject proj = root.getProject(activeProject.getName());
										try {
											proj.refreshLocal(IResource.DEPTH_INFINITE, null);
										} catch (CoreException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} else {
										try {
											IProject proj = (IProject) selectedObj;
											proj.refreshLocal(IResource.DEPTH_INFINITE, null);
										} catch (CoreException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									monitor.done();
									return Status.OK_STATUS;
								}

							};
							job.addJobChangeListener(new JobChangeAdapter() {
								public void done(IJobChangeEvent event) {
									if (event.getResult().isOK()) {

										RState.setBusy(false);
									} else {

										RState.setBusy(false);
									}
								}
							});
							// job.setSystem(true);
							job.schedule();
						} else {
							Bio7Dialog.message("Rserve is busy!");
						}
					}
				} else if (RServe.isAlive() == false) {
					String selectionConsole = ConsolePageParticipant.getInterpreterSelection();
					if (selectionConsole.equals("R")) {

						ConsolePageParticipant.pipeInputToConsole(rCommand, true, true);
						System.out.println(rCommand);
					}

				} else {
					Bio7Dialog.message("Please start Rserve or the R console.");
				}

			}
		}

	}

}
