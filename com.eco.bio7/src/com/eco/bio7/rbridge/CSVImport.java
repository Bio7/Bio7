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

package com.eco.bio7.rbridge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import com.opencsv.CSVReader;

public class CSVImport {

	public Shell sShell = null;
	private Grid table = null;
	private Text textSeperator = null;
	private Text text3 = null;
	private Button button = null;
	private Button button2 = null;
	private Button button3 = null;
	protected static String file;
	private Label seperatorLabel = null;
	private Label label1 = null;
	private Label label2 = null;
	private Spinner spinner = null;
	protected Grid grid;
	private Text textQuotedChar;
	private Button kommaButton;
	private Button semicolonButton;
	private Button otherButton;
	private int count;
	private int sizey;
	private int sizex;
	private FileReader fi;
	private CSVReader reader;
	private String fileToRead;
	protected char c;
	protected char c2;
	protected int sel;
	protected String name;
	private Label lblSeperator;
	private Label label_1;
	private Button strictQuotes;
	private Label lblExtra;
	private Button ignoreWs;
	private Button keepCr;
	private Text textEscape;
	private Label lblNewLabel;
	private Label label;
	private Label labelError;
	private Label lblMessage;

	/**
	 * This method initializes combo
	 * 
	 */
	private void createCombo() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = Display.getDefault();
		CSVImport thisClass = new CSVImport();
		thisClass.createSShell();
		thisClass.sShell.open();

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static String openFile(Display display) {

		display.syncExec(new Runnable() {

			public void run() {
				Shell s = new Shell(SWT.ON_TOP);
				FileDialog fd = new FileDialog(s, SWT.OPEN);
				fd.setText("Load");

				String[] filterExt = { "*.*" };
				fd.setFilterExtensions(filterExt);
				file = fd.open();
			}
		});
		return file;
	}

	/**
	 * This method initializes sShell
	 */
	void createSShell() {
		sShell = new Shell(SWT.CLOSE | SWT.RESIZE | SWT.TITLE);
		sShell.setText("CSV Reader");
		sShell.setSize(new Point(518, 717));
		sShell.setLayout(new GridLayout(3, true));

		text3 = new Text(sShell, SWT.BORDER);
		GridData gd_text3 = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_text3.heightHint = 25;
		text3.setLayoutData(gd_text3);
		button = new Button(sShell, SWT.NONE);
		GridData gd_button = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_button.heightHint = 35;
		button.setLayoutData(gd_button);
		button.setText("File");
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				String f = openFile(sShell.getDisplay());
				if (f != null) {
					text3.setText(f);
				}
				modified();

			}
		});
		table = new Grid(sShell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 5);
		gd_table.widthHint = 411;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setRowHeaderVisible(true);
		table.setLinesVisible(true);
		for (int i = 0; i < 20; i++) {
			GridColumn column = new GridColumn(table, SWT.NONE);

			column.setText("C " + (i + 1));
			column.setWidth(50);

		}
		for (int i = 0; i < 20; i++) {
			new GridItem(table, SWT.NONE);

		}

		lblSeperator = new Label(sShell, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeperator.setText("Seperator");
		lblSeperator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		seperatorLabel = new Label(sShell, SWT.NONE);
		GridData gd_seperatorLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_seperatorLabel.heightHint = 25;
		seperatorLabel.setLayoutData(gd_seperatorLabel);
		seperatorLabel.setText("Seperator character:");
		new Label(sShell, SWT.NONE);
		new Label(sShell, SWT.NONE);

		final Button tabButton = new Button(sShell, SWT.CHECK);
		GridData gd_tabButton = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_tabButton.heightHint = 30;
		tabButton.setLayoutData(gd_tabButton);
		tabButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (tabButton.getSelection()) {
					textSeperator.setEnabled(false);
					textSeperator.setText("\t");
					kommaButton.setSelection(false);
					semicolonButton.setSelection(false);
					otherButton.setSelection(false);
					// tabButton.setSelection(false);
				} else {
					textSeperator.setText("");
				}
				modified();
			}
		});
		tabButton.setText("Tab");

		semicolonButton = new Button(sShell, SWT.CHECK);
		GridData gd_semicolonButton = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_semicolonButton.heightHint = 30;
		semicolonButton.setLayoutData(gd_semicolonButton);
		semicolonButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (semicolonButton.getSelection()) {
					textSeperator.setEnabled(false);
					textSeperator.setText(";");
					kommaButton.setSelection(false);
					otherButton.setSelection(false);
					tabButton.setSelection(false);
				} else {
					textSeperator.setText("");
				}
				modified();
			}
		});
		semicolonButton.setText("Semicolon");
		new Label(sShell, SWT.NONE);

		kommaButton = new Button(sShell, SWT.CHECK);
		kommaButton.setSelection(true);
		GridData gd_kommaButton = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_kommaButton.heightHint = 30;
		kommaButton.setLayoutData(gd_kommaButton);
		kommaButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (kommaButton.getSelection()) {
					textSeperator.setEnabled(false);
					textSeperator.setText(",");
					semicolonButton.setSelection(false);
					otherButton.setSelection(false);
					tabButton.setSelection(false);
				} else {
					textSeperator.setText("");
				}
				modified();
			}
		});
		kommaButton.setText("Komma");

		otherButton = new Button(sShell, SWT.CHECK);
		GridData gd_otherButton = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_otherButton.heightHint = 30;
		otherButton.setLayoutData(gd_otherButton);
		otherButton.setToolTipText("Default = Whitespace");
		otherButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				if (otherButton.getSelection()) {
					textSeperator.setEnabled(true);
					textSeperator.setText(" ");
					kommaButton.setSelection(false);
					semicolonButton.setSelection(false);
					tabButton.setSelection(false);
				} else {
					textSeperator.setEnabled(false);
				}
				modified();
			}
		});
		otherButton.setText("Other");
		new Label(sShell, SWT.NONE);

		textSeperator = new Text(sShell, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_text.heightHint = 20;
		textSeperator.setLayoutData(gd_text);
		textSeperator.setEnabled(false);
		textSeperator.setTextLimit(1);
		textSeperator.setText(",");
		textSeperator.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				modified();
			}
		});
		new Label(sShell, SWT.NONE);
		new Label(sShell, SWT.NONE);

		lblExtra = new Label(sShell, SWT.NONE);
		lblExtra.setText("Extra:");
		new Label(sShell, SWT.NONE);
		new Label(sShell, SWT.NONE);

		strictQuotes = new Button(sShell, SWT.CHECK);
		GridData gd_strictQuotes = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_strictQuotes.heightHint = 25;
		strictQuotes.setLayoutData(gd_strictQuotes);
		strictQuotes.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modified();
			}
		});
		strictQuotes.setText("Strict Quotes");

		ignoreWs = new Button(sShell, SWT.CHECK);
		GridData gd_ignoreWs = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_ignoreWs.heightHint = 25;
		ignoreWs.setLayoutData(gd_ignoreWs);
		ignoreWs.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modified();
			}
		});
		ignoreWs.setToolTipText("Ignore Leading Whitespace");
		ignoreWs.setText("Ignore Leading WS");

		keepCr = new Button(sShell, SWT.CHECK);
		GridData gd_keepCr = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_keepCr.heightHint = 25;
		keepCr.setLayoutData(gd_keepCr);
		keepCr.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modified();
			}
		});
		keepCr.setToolTipText("Keep Carriage Returns");
		keepCr.setText("Keep CR");

		label_1 = new Label(sShell, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gd_label_1.heightHint = 25;
		label_1.setLayoutData(gd_label_1);

		lblNewLabel = new Label(sShell, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblNewLabel.heightHint = 35;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Escape character:");
		label1 = new Label(sShell, SWT.NONE);
		GridData gd_label1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_label1.heightHint = 35;
		label1.setLayoutData(gd_label1);
		label1.setText("Quote character:");
		label2 = new Label(sShell, SWT.NONE);
		GridData gd_label2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_label2.heightHint = 35;
		label2.setLayoutData(gd_label2);
		label2.setText("Start at line:");

		textEscape = new Text(sShell, SWT.BORDER);
		textEscape.setText("\\");
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_1.heightHint = 20;
		textEscape.setTextLimit(1);
		textEscape.setLayoutData(gd_text_1);
		textEscape.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				modified();
			}
		});
		textQuotedChar = new Text(sShell, SWT.NONE);
		textQuotedChar.setText("\"");
		textQuotedChar.setTextLimit(1);
		textQuotedChar.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				modified();
			}
		});
		GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_combo.heightHint = 30;
		textQuotedChar.setLayoutData(gd_combo);
		spinner = new Spinner(sShell, SWT.NONE);
		spinner.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modified();
			}
		});
		GridData gd_spinner = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_spinner.heightHint = 25;
		spinner.setLayoutData(gd_spinner);
		spinner.setMaximum(1000000);

		label = new Label(sShell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));

		lblMessage = new Label(sShell, SWT.NONE);
		lblMessage.setText("Help message:");
		new Label(sShell, SWT.NONE);
		new Label(sShell, SWT.NONE);

		labelError = new Label(sShell, SWT.NONE);
		GridData gd_labelError = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gd_labelError.heightHint = 25;
		labelError.setLayoutData(gd_labelError);
		labelError.setText("No parsing  problems detected!");

		button2 = new Button(sShell, SWT.NONE);
		GridData gd_button2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_button2.heightHint = 35;
		button2.setLayoutData(gd_button2);
		button2.setText("OK");
		button2.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				String fileToRead = text3.getText();
				String name = new File(fileToRead).getName();
				char sep = textSeperator.getText().charAt(0);
				char quot = textQuotedChar.getText().charAt(0);
				char esc=textEscape.getText().charAt(0);
				int sel = spinner.getSelection();
				LoadCsvJob job = new LoadCsvJob(fileToRead, sep, quot,esc, sel,strictQuotes.getSelection(), ignoreWs.getSelection(), keepCr.getSelection(), name);
				// job.setSystem(true);
				job.schedule();

				sShell.dispose();

			}
		});
		new Label(sShell, SWT.NONE);
		button3 = new Button(sShell, SWT.NONE);
		GridData gd_button3 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_button3.heightHint = 35;
		button3.setLayoutData(gd_button3);
		button3.setText("Cancel");
		button3.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sShell.dispose();

			}
		});
		createCombo();

	}

	private void modified() {
		table.setRedraw(false);
		for (int i = 0; i < table.getItemCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.getItem(i).setText(j, "");
			}
		}
		table.setRedraw(true);
		CSVReader reader = null;
		/* Detect if char is given! */

		if (textSeperator.getText().length() <= 0 || textQuotedChar.getText().length()<= 0 || textEscape.getText().length()<= 0) {
			labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			labelError.setText("The separator, quote, and escape characters could'nt be empty!");

			return;
		}

		char seperator = textSeperator.getText().charAt(0);
		char quoteChar = textQuotedChar.getText().charAt(0);
		char escape = textEscape.getText().charAt(0);

		if (anyCharactersAreTheSame(seperator, quoteChar, escape)) {
			labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			labelError.setText("The separator, quote, and escape characters must be different!");

			// Bio7Dialog.message("The separator, quote, and escape
			// characters must be different!");
			return;
		} else {
			labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			labelError.setText("No problems detected!");
		}

		FileReader fi = null;
		try {
			fi = new FileReader(text3.getText());

			reader = new CSVReader(fi, seperator, quoteChar, escape, spinner.getSelection(), strictQuotes.getSelection(), ignoreWs.getSelection(), keepCr.getSelection());

			String[] nextLine;
			int count = 0;
			try {
				while ((nextLine = reader.readNext()) != null) {
					if (count < table.getItemCount()) {

						for (int i = 0; i < 20; i++) {
							if (i < nextLine.length) {
								table.getItem(count).setText(i, nextLine[i]);
							}
						}

						count++;

					}
				}
			} catch (IOException e1) {

				e1.printStackTrace();
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	private void readTable() {

		try {
			fi = new FileReader(fileToRead);
		} catch (FileNotFoundException e2) {

			e2.printStackTrace();
		}

		reader = new CSVReader(fi, c, c2, sel);

		sizey = 0;
		sizex = 0;
		String[] nextLine;

		try {
			while ((nextLine = reader.readNext()) != null) {

				for (int i = 0; i < nextLine.length; i++) {
					if (nextLine.length > sizex) {
						sizex = nextLine.length;
					}

				}

				sizey++;

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		/* Now Load the File definitely ! */
		try {
			fi = new FileReader(fileToRead);
		} catch (FileNotFoundException e2) {

			e2.printStackTrace();
		}

	}
	/*
	 * The following methods were copied from the CSV source:
	 * 
	 * Copyright 2005 Bytecode Pty Ltd.
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License. You may obtain a
	 * copy of the License at
	 * 
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations
	 * under the License.
	 */

	/**
	 * Checks to see if any two of the three characters are the same. This is
	 * because in opencsv the separator, quote, and escape characters must the
	 * different.
	 *
	 * @param separator
	 *            The defined separator character
	 * @param quotechar
	 *            The defined quotation cahracter
	 * @param escape
	 *            The defined escape character
	 * @return True if any two of the three are the same.
	 */
	private boolean anyCharactersAreTheSame(char separator, char quotechar, char escape) {
		return isSameCharacter(separator, quotechar) || isSameCharacter(separator, escape) || isSameCharacter(quotechar, escape);
	}

	/**
	 * Checks that the two characters are the same and are not the defined
	 * NULL_CHARACTER.
	 * 
	 * @param c1
	 *            First character
	 * @param c2
	 *            Second character
	 * @return True if both characters are the same and are not the defined
	 *         NULL_CHARACTER
	 */
	private boolean isSameCharacter(char c1, char c2) {
		return c1 != '\0' && c1 == c2;
	}

}
