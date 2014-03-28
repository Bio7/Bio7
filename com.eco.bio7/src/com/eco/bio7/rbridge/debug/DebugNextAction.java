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

package com.eco.bio7.rbridge.debug;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.eco.bio7.batch.Bio7Dialog;
import com.eco.bio7.console.ConsolePageParticipant;

public class DebugNextAction extends Action {

	public DebugNextAction() {
		super("Next");

		setId("Next");
		setText("Next");

		ImageDescriptor desc = ImageDescriptor.createFromImage(new Image(
				Display.getCurrent(), getClass().getResourceAsStream(
						"/pics/stepover_co.gif")));

		this.setImageDescriptor(desc);
	}

	public void run() {
		
		String selectionConsole = ConsolePageParticipant.getInterpreterSelection();

		if (selectionConsole.equals("R")) {
		
		ConsolePageParticipant.pipeInputToConsole("n", true, false);
		System.out.println("n");
        final ConsolePageParticipant inst=ConsolePageParticipant.getConsolePageParticipantInstance();
        
        final StringBuilder sb = new StringBuilder();
       
        /*new Thread() {

			public void run() {
				 
				setPriority(Thread.MAX_PRIORITY);
				final InputStream inp = inst.RProcess.getInputStream();
				InputStreamReader inr = new InputStreamReader(inp);
				int ch;
				try {
					while ((ch = inr.read()) != -1) {
						sb.append((char)ch);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
			
			System.out.println("Result is::: "+sb.toString());*/
			
		
		
		
		//inst.toolBarManager.update(true);	
		
		}
		else {
			Bio7Dialog.message("Please start the \"Native R\" shell in the Bio7 console!");
		}
		
	}

}