package com.eco.bio7.preferences;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Bundle;

import com.eco.bio7.Bio7Plugin;
import com.eco.bio7.rcp.ApplicationWorkbenchWindowAdvisor;

public class Bio7PaintPreferences extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private MultiLineTextFieldEditor mult;

	public Bio7PaintPreferences() {
		super(GRID);
		setPreferenceStore(Bio7Plugin.getDefault().getPreferenceStore());
		setDescription("Preferences Bio7 Panels, Values, Scripts:");

	}

	public void createFieldEditors() {
		addField(new BooleanFieldEditor("REPAINT_QUAD", "Repaint the Quadgrid after each iteration step", getFieldEditorParent()));
		addField(new BooleanFieldEditor("REPAINT_HEX", "Repaint the Hexgrid after each iteration step", getFieldEditorParent()));
		addField(new BooleanFieldEditor("RECORD_VALUES", "Record at each iteration step the amount of states (Quadgrid, Hexgrid)", getFieldEditorParent()));
		addField(new BooleanFieldEditor("QUAD_PANEL_SCROLLBAR", "Show the scrollbar in the Quadgrid (Hexgrid) panel", getFieldEditorParent()));
		addField(new BooleanFieldEditor("POINTS_PANEL_SCROLLBAR", "Show the scrollbar in the Points panel", getFieldEditorParent()));
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor("STARTUP_SCRIPTS", "Enable startup scripts", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		mult=new MultiLineTextFieldEditor("BIO7_STARTUP_COMMANDS", "Startup Commands (Groovy Script)", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
		addField(mult);
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("JDT:", getFieldEditorParent()));
		addField(new BooleanFieldEditor("SHOW_JDT_GUI", "Enable JDT Gui elements", getFieldEditorParent()));
		//addField(new SpacerFieldEditor(getFieldEditorParent()));
		/*addField(new LabelFieldEditor("Additional Default Perspectives:", getFieldEditorParent()));
		addField(new StringFieldEditor("DEFAULT_CUSTOM_PERSPECTIVE1", "Additional perspective at startup (ID)", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor("DEFAULT_CUSTOM_PERSPECTIVE2", "Additional perspective at startup (ID)", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new StringFieldEditor("DEFAULT_CUSTOM_PERSPECTIVE3", "Additional perspective at startup (ID)", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		*/
		
	}

	public void init(IWorkbench workbench) {
		

		

	}

}
