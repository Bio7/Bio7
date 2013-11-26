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

package com.eco.bio7.console;

import java.util.prefs.Preferences;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.eco.bio7.batch.Bio7Dialog;

public class DebugStopAction extends Action {

	public DebugStopAction() {
		super("Stop");

		setId("Stop");
		setText("Stop");

		ImageDescriptor desc = ImageDescriptor.createFromImage(new Image(
				Display.getCurrent(), getClass().getResourceAsStream(
						"/pics/terminatedlaunch_obj.gif")));

		this.setImageDescriptor(desc);
	}

	public void run() {
		
		String selectionConsole = ConsolePageParticipant.getInterpreterSelection();

		if (selectionConsole.equals("R")) {
		
		ConsolePageParticipant.pipeInputToConsole("Q", true, false);
		System.out.println("Q");
          ConsolePageParticipant inst=ConsolePageParticipant.getConsolePageParticipantInstance();
        
		//IContributionItem []its=inst.toolBarManager.getItems();
		
				
					inst.toolBarManager.remove("Stop");
					inst.toolBarManager.remove("Next");
					inst.toolBarManager.remove("Continue");
					inst.actionBars.updateActionBars();
					
		
			
			
			
		
		
		
		//inst.toolBarManager.update(true);	
		
		}
		else {
			Bio7Dialog.message("Please start the \"Native R\" shell in the Bio7 console!");
		}
		
	}

}