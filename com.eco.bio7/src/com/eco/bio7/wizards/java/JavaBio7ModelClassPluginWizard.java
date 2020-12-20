package com.eco.bio7.wizards.java;



import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

import java.io.*;

import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import com.eco.bio7.wizards.java.CreateBio7JdtProject;

public class JavaBio7ModelClassPluginWizard extends Wizard implements INewWizard {
	private JavaBio7ModelClassWizardPage page;
	private ISelection selection;

	public JavaBio7ModelClassPluginWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page = new JavaBio7ModelClassWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
		
		monitor.beginTask("Creating and Configure Bio7 JDT Project", 3);
		CreateBio7JdtProject jdt = new CreateBio7JdtProject(containerName, monitor);
		monitor.worked(1);
		IContainer container = jdt.getSourceFolder();
		
		final IFile file = container.getFile(new Path(fileName));
		String an = "My_Model";
		int index = file.getName().lastIndexOf('.');
		if (index > 0 && index <= file.getName().length() - 2) {
			an = file.getName().substring(0, index);
		}

		try {
			InputStream stream = openContentStream(an);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

	private InputStream openContentStream(String filename) {
		String linefeed = "\r\n";
		String setup = "\tpublic void setup(){" 
		+ linefeed + "\t// Please enter your setup code here!" 
				+ linefeed + "\t}" + linefeed 
		+ "\tpublic void run(){" + linefeed
				+ "\t// Please enter your main code here!" + linefeed 
		+ "\t}";

		String contents = "public class " + filename + " extends com.eco.bio7.compile.Model {" + linefeed + linefeed + setup + linefeed + "}";

		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "com.eco.bio7", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}