package com.eco.bio7.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.eco.bio7.Bio7Plugin;

public class Python extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Create the preference page.
	 */
	public Python() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(Bio7Plugin.getDefault().getPreferenceStore());
		setDescription("");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		SpacerFieldEditor spacer1 = new SpacerFieldEditor(getFieldEditorParent());
		addField(spacer1);
		
		{
			LabelFieldEditor labelFieldEditor = new LabelFieldEditor("Python/Blender:", getFieldEditorParent());
			labelFieldEditor.setLabelText("Python/Blender:");
			addField(labelFieldEditor);
		}
		
		addField(new BooleanFieldEditor("python_pipe", "Execute Editor (Floweditor) Source in Python/Blender", BooleanFieldEditor.DEFAULT, getFieldEditorParent()));
		addField(new BooleanFieldEditor("python_process_extra", "Execute Editor Source in Seperate Python Process (not sent to Bio7 Python Shell)", BooleanFieldEditor.DEFAULT, getFieldEditorParent()));
		addField(new BooleanFieldEditor("python_3x", "Python 3 is used to evaluate scripts (only important if sent to Bio7 Python Shell)", BooleanFieldEditor.DEFAULT, getFieldEditorParent()));
		addField(new RadioGroupFieldEditor("python_select", "Select Interpreter", 1, new String[][] { { "Python", "Python" }, { "Blender", "Blender" } }, getFieldEditorParent(), false));
		addField(new DirectoryFieldEditor("python_pipe_path", "Path Python", getFieldEditorParent()));
		addField(new LabelFieldEditor("Blender:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("path_blender", "Path Blender", getFieldEditorParent()));

		RadioGroupFieldEditor radioGroupFieldEditor = new RadioGroupFieldEditor("blender_options", "Blender Default Options", 1, new String[][] { { "None (-)", "pnone" },{ "Interactive Shell (--python-console)", "interactive" },
				{ "Python Script (-P)", "pscript" } }, getFieldEditorParent(), false);
		
		addField(radioGroupFieldEditor);
		addField(new StringFieldEditor("blender_args", "Blender Arguments", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));

		addField(new MultiLineTextFieldEditor("before_script_blender", "Before Script Execution (Interactive Shell)", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		addField(new MultiLineTextFieldEditor("after_script_blender", "After Script Execution (Interactive Shell)", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent()));
		SpacerFieldEditor spacer2 = new SpacerFieldEditor(getFieldEditorParent());
		addField(spacer2);
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
