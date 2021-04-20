package com.eco.bio7.image;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

import com.eco.bio7.batch.Bio7Dialog;
import com.eco.bio7.rbridge.RServe;
import com.eco.bio7.rbridge.RServeUtil;
import com.eco.bio7.rbridge.RState;
import org.eclipse.swt.widgets.Label;

public class TransferGeometryView extends ViewPart {

	public static final String ID = "com.eco.bio7.image.TransferGeometryView"; //$NON-NLS-1$
	private Combo combo;
	protected boolean transferAsList = true;
	protected String geometrySelection;
	protected int geometrySelectionSelection = 0;
	private Combo combo_1;
	private Button btnAddDataframe;
	protected String selDataframe;
	protected String crsText;
	protected boolean doSetCrs = false;
	protected boolean doSetDataframe = false;
	private Text text;
	private Button btnNewButton;
	private Button btnAddProjectionFrom;
	private Button btnTransfer;
	private Button btnTransferAsCentroid;
	protected boolean transferCentroid;
	private ScrolledComposite scrolledComposite;

	public TransferGeometryView() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);	
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(false);
		
		Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setSize(300,350);
		scrolledComposite.setContent(container);
		createActions();
		//initializeToolBar();
		initializeMenu();
		GridLayout gl_container = new GridLayout(1, true);
		gl_container.marginHeight = 0;
		gl_container.verticalSpacing = 0;
		container.setLayout(gl_container);

		Button btnSelectionCoordinatesAs = new Button(container, SWT.RADIO);
		GridData gd_btnSelectionCoordinatesAs = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnSelectionCoordinatesAs.heightHint = 30;
		btnSelectionCoordinatesAs.setLayoutData(gd_btnSelectionCoordinatesAs);
		btnSelectionCoordinatesAs.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				combo.setEnabled(false);
				combo_1.setEnabled(false);
				btnAddDataframe.setEnabled(false);
				btnNewButton.setEnabled(false);
				text.setEnabled(false);
				btnAddProjectionFrom.setEnabled(false);
				btnTransferAsCentroid.setEnabled(false);
				btnAddDataframe.setSelection(false);
				btnAddProjectionFrom.setSelection(false);
				btnTransferAsCentroid.setSelection(false);
				/*
				 * text.setEnabled(false); btnAddCrs.setEnabled(false);
				 * btnLoadFromFile.setEnabled(false);
				 */
				transferAsList = true;
			}
		});
		btnSelectionCoordinatesAs.setSelection(true);
		btnSelectionCoordinatesAs.setText("Selection coordinates as a list");

		Button btnRadioButton = new Button(container, SWT.RADIO);
		GridData gd_btnRadioButton = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnRadioButton.heightHint = 30;
		btnRadioButton.setLayoutData(gd_btnRadioButton);
		btnRadioButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				combo.setEnabled(true);
				combo_1.setEnabled(true);
				btnAddDataframe.setEnabled(true);
				btnAddProjectionFrom.setEnabled(true);
				btnTransferAsCentroid.setEnabled(true);
				/*
				 * text.setEnabled(true); btnAddCrs.setEnabled(true);
				 * btnLoadFromFile.setEnabled(true);
				 */
				transferAsList = false;
			}
		});
		btnRadioButton.setText("Spatial Data (R package 'sp' required!)");

		combo = new Combo(container, SWT.NONE);
		GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_combo.heightHint = 40;
		combo.setLayoutData(gd_combo);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				geometrySelection = combo.getText();
				geometrySelectionSelection = combo.getSelectionIndex();

			}
		});
		combo.setItems(new String[] { "SpatialPolygons", "SpatialLines", "SpatialPoints" });
		combo.setText("Geometries");
		combo.select(0);
		combo.setEnabled(false);

		btnAddDataframe = new Button(container, SWT.CHECK);
		GridData gd_btnAddDataframe = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnAddDataframe.heightHint = 30;
		btnAddDataframe.setLayoutData(gd_btnAddDataframe);
		btnAddDataframe.setEnabled(false);
		btnAddDataframe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (doSetDataframe)
					doSetDataframe = false;
				else {
					doSetDataframe = true;
				}

				REXP x = null;
				combo_1.removeAll();
				combo_1.add("-Select-", 0);
				combo_1.select(0);
				if (RServe.isAliveDialog()) {
					if (RState.isBusy() == false) {
						String[] v = null;
						// List all variables in the R workspace!

						try {
							RServe.getConnection()
									.eval("try(varWorkspaceType<-NULL);try(for(i in 1:length(ls())){if(is.data.frame(get(ls()[i]))==TRUE){varWorkspaceType<-append(varWorkspaceType,ls()[i])}})");
							x = RServe.getConnection().eval("try(varWorkspaceType)");
							if (x.isNull() == false) {
								try {
									v = x.asStrings();
								} catch (REXPMismatchException e1) {

									e1.printStackTrace();
								}
							}
							RServe.getConnection().eval("try(remove(varWorkspaceType))");
						} catch (RserveException e1) {

							e1.printStackTrace();
						}
						if (v != null) {
							for (int i = 0; i < v.length; i++) {

								combo_1.add(v[i], i + 1);
								combo_1.update();
							}
						}
					} else {
						Bio7Dialog.message("Rserve is busy!");

					}

				}
				combo_1.select(0);
			}
		});
		btnAddDataframe.setText("Add selected dataframe (reselect for a refresh!)");

		combo_1 = new Combo(container, SWT.NONE);
		GridData gd_combo_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_combo_1.heightHint = 40;
		combo_1.setLayoutData(gd_combo_1);
		combo_1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				selDataframe = combo_1.getText();
			}
		});
		combo_1.setItems(new String[] { "-Select-" });
		combo_1.select(0);
		combo_1.setEnabled(false);

		btnTransferAsCentroid = new Button(container, SWT.CHECK);
		GridData gd_btnTransferAsCentroid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnTransferAsCentroid.heightHint = 30;
		btnTransferAsCentroid.setLayoutData(gd_btnTransferAsCentroid);
		btnTransferAsCentroid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnTransferAsCentroid.getSelection()) {
					transferCentroid = true;
				} else {
					transferCentroid = false;
				}
			}
		});
		btnTransferAsCentroid.setEnabled(false);
		btnTransferAsCentroid.setText("Transfer as centroid coordinates");

		btnAddProjectionFrom = new Button(container, SWT.CHECK);
		GridData gd_btnAddProjectionFrom = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnAddProjectionFrom.heightHint = 30;
		btnAddProjectionFrom.setLayoutData(gd_btnAddProjectionFrom);
		btnAddProjectionFrom.setEnabled(false);
		btnAddProjectionFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (btnAddProjectionFrom.getSelection()) {

					doSetCrs = true;
					text.setEnabled(true);
					btnNewButton.setEnabled(true);
				}

				else {
					text.setEnabled(false);
					btnNewButton.setEnabled(false);
					doSetCrs = false;
				}

			}
		});
		btnAddProjectionFrom.setText("Add projection from georeferenced file (Projected Coordinate System!)");

		text = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_text.heightHint = 30;
		text.setLayoutData(gd_text);
		text.setEnabled(false);

		btnNewButton = new Button(container, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnNewButton.heightHint = 30;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				crsText = Bio7Dialog.openFile();
				if (crsText != null) {
					text.setText(crsText);
				}
			}
		});
		btnNewButton.setText("Load File");
		new Label(container, SWT.NONE);

		btnTransfer = new Button(container, SWT.NONE);
		GridData gd_btnTransfer = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_btnTransfer.heightHint = 30;
		btnTransfer.setLayoutData(gd_btnTransfer);
		btnTransfer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// if (dialog.open() == Window.OK) {
				if (RServe.isAliveDialog()) {
					int selection = getGeometrySelectionSelection();
					boolean transferAsList = transferAsList();

					boolean doSetCRS = isDoSetCrs();
					boolean doSetDf = isDoSetDataframe();
					String crs = getCrsText();
					String selectedDf = getSelDataframe();
					boolean asCentroid = isTransferCentroid();
					if (RState.isBusy() == false) {
						RState.setBusy(true);
						TransferSelectionCoordsJob job = new TransferSelectionCoordsJob(transferAsList, selection, doSetCRS, doSetDf, crs, selectedDf, asCentroid);
						// job.setSystem(true);
						job.addJobChangeListener(new JobChangeAdapter() {
							public void done(IJobChangeEvent event) {
								if (event.getResult().isOK()) {

									RState.setBusy(false);
									RServeUtil.listRObjects();
								} else {
									RState.setBusy(false);
								}
							}
						});
						job.schedule();
					} else {

						Bio7Dialog.message("Rserve is busy!");

					}
					/*
					 * MatchingDialoge m=new MatchingDialoge(new Shell()); m.open()
					 */;
					// }
				}
			}
		});
		btnTransfer.setText("Transfer to R!");

	}

	public boolean transferAsList() {
		return transferAsList;
	}

	public String getGeometrySelection() {
		return geometrySelection;
	}

	public int getGeometrySelectionSelection() {
		return geometrySelectionSelection;
	}

	public String getSelDataframe() {
		return selDataframe;
	}

	public void setSelDataframe(String selDataframe) {
		this.selDataframe = selDataframe;
	}

	public String getCrsText() {
		return crsText;
	}

	public boolean isDoSetCrs() {
		return doSetCrs;
	}

	public void setDoSetCrs(boolean doSetCrs) {
		this.doSetCrs = doSetCrs;
	}

	public boolean isDoSetDataframe() {
		return doSetDataframe;
	}

	public void setDoSetDataframe(boolean doSetDataframe) {
		this.doSetDataframe = doSetDataframe;
	}

	public boolean isTransferCentroid() {
		return transferCentroid;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	/*private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}*/

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
