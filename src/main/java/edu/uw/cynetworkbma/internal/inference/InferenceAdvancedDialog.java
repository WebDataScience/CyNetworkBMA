package edu.uw.cynetworkbma.internal.inference;

import java.awt.Dialog;

import org.cytoscape.model.*;

public class InferenceAdvancedDialog extends javax.swing.JDialog {
	
	private static final long serialVersionUID = -3738444046289316675L;

	private final InferenceParameters taskParameters;
	private CyTable priorProbTable;
	
	private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton constantPriorRadioButton;
    private javax.swing.JPanel iBmaPanel;
    private javax.swing.JRadioButton iBmaRadioButton;
    private javax.swing.JCheckBox iKeepModelsCheckbox;
    private javax.swing.JLabel iMaxIterLabel;
    private javax.swing.JSpinner iMaxIterSpinner;
    private javax.swing.JLabel iMaxNvarLabel;
    private javax.swing.JSpinner iMaxNvarSpinner;
    private javax.swing.JLabel iNbestLabel;
    private javax.swing.JSpinner iNbestSpinner;
    private javax.swing.JLabel iORLabel;
    private javax.swing.JSpinner iORSpinner;
    private javax.swing.JLabel iThresProbne0Label;
    private javax.swing.JSpinner iThresProbne0Spinner;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton noPriorRadioButton;
    private javax.swing.JCheckBox nvarCheckBox;
    private javax.swing.JSpinner nvarSpinner;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox orderingComboBox;
    private javax.swing.JLabel orderingLabel;
    private javax.swing.JLabel posteriorProbThresLabel;
    private javax.swing.JSpinner posteriorProbThresSpinner;
    private javax.swing.JComboBox priorProbComboBox;
    private javax.swing.JSpinner priorProbSpinner;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel sEpsilonLabel;
    private javax.swing.JSpinner sEpsilonSpinner;
    private javax.swing.JCheckBox sG0Checkbox;
    private javax.swing.JSpinner sG0Spinner;
    private javax.swing.JLabel sIterlimLabel;
    private javax.swing.JSpinner sIterlimSpinner;
    private javax.swing.JLabel sORLabel;
    private javax.swing.JSpinner sORSpinner;
    private javax.swing.JCheckBox sOptimizeCheckbox;
    private javax.swing.JLabel sThresProbne0Label;
    private javax.swing.JSpinner sThresProbne0Spinner;
    private javax.swing.JCheckBox sUsegCheckbox;
    private javax.swing.JPanel scanBmaPanel;
    private javax.swing.JRadioButton scanBmaRadioButton;
    private javax.swing.JRadioButton tablePriorRadioButton;

	public InferenceAdvancedDialog(
			Dialog owner,
			InferenceParameters taskParameters,
			CyTable priorProbTable,
			CyTableManager tableManager) {
		
		super(owner, true);
		
		initComponents();
		setLocationRelativeTo(owner);
		
		for (CyTable table : tableManager.getGlobalTables()) {
			if (table.isPublic()) {
				priorProbComboBox.addItem(table);
			}
		}
		
		this.taskParameters = taskParameters;
		this.priorProbTable = priorProbTable;
		populateControls(taskParameters);
	}
	
    private void scanBmaRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                   
    	enableOrDisableAlgorithmSpecificControls();
    }                                                  

    private void iBmaRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                
    	enableOrDisableAlgorithmSpecificControls();
    }                                               

    private void sOptimizeCheckboxActionPerformed(java.awt.event.ActionEvent evt) {                                                  
    	enableOrDisableAlgorithmSpecificControls();
    }                                                 

    private void sG0CheckboxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        enableOrDisableAlgorithmSpecificControls();
    }                                           

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        populateControls(InferenceParameters.getDefault());
    }                                           

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	
        if (scanBmaRadioButton.isSelected()) {
        	taskParameters.setAlgorithm(InferenceParameters.ALGORITHM_SCANBMA);
        } else if (iBmaRadioButton.isSelected()) {
        	taskParameters.setAlgorithm(InferenceParameters.ALGORITHM_IBMA);
        }
        
        taskParameters.setScanBmaOR((Integer)sORSpinner.getValue());
        taskParameters.setScanBmaUseg(sUsegCheckbox.isSelected());
        taskParameters.setScanBmaThresProbne0((Double)sThresProbne0Spinner.getValue());
        taskParameters.setScanBmaOptimize(sOptimizeCheckbox.isSelected());
        taskParameters.setScanBmaG0(sG0Checkbox.isSelected()? (Integer)sG0Spinner.getValue() : null);
        taskParameters.setScanBmaIterlim((Integer)sIterlimSpinner.getValue());
        taskParameters.setScanBmaEpsilon(Double.parseDouble(sEpsilonSpinner.getValue().toString()));
        taskParameters.setIterBmaOR((Integer)iORSpinner.getValue());
        taskParameters.setIterBmaNbest((Integer)iNbestSpinner.getValue());
        taskParameters.setIterBmaMaxNvar((Integer)iMaxNvarSpinner.getValue());
        taskParameters.setIterBmaThresProbne0((Double)iThresProbne0Spinner.getValue());
        taskParameters.setIterBmaKeepModels(iKeepModelsCheckbox.isSelected());
        taskParameters.setIterBmaMaxIter((Integer)iMaxIterSpinner.getValue());
        taskParameters.setNvar(nvarCheckBox.isSelected()? (Integer)nvarSpinner.getValue() : null);
        taskParameters.setOrdering((String)orderingComboBox.getSelectedItem());
        taskParameters.setConstantPrior(constantPriorRadioButton.isSelected());
        taskParameters.setPriorProb(constantPriorRadioButton.isSelected()? Double.parseDouble(priorProbSpinner.getValue().toString()) : null);
        taskParameters.setTablePrior(tablePriorRadioButton.isSelected());
        taskParameters.setPostProbThreshold((Double)posteriorProbThresSpinner.getValue());
        
        priorProbTable = (CyTable)priorProbComboBox.getSelectedItem();

        setVisible(false);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        setVisible(false);
    }

    private void noPriorRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                   
    	enableOrDisableVariableOrderingControls();
    }

    private void constantPriorRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                         
    	enableOrDisableVariableOrderingControls();
    }

    private void tablePriorRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                      
    	enableOrDisableVariableOrderingControls();
    }
    
    private void nvarCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {                                             
    	enableOrDisableVariableOrderingControls();
    }
    
    private void enableOrDisableAlgorithmSpecificControls() {
    	
    	sORLabel.setEnabled(scanBmaRadioButton.isSelected());
    	sORSpinner.setEnabled(scanBmaRadioButton.isSelected());
    	sUsegCheckbox.setEnabled(scanBmaRadioButton.isSelected());
    	sThresProbne0Label.setEnabled(scanBmaRadioButton.isSelected());
    	sThresProbne0Spinner.setEnabled(scanBmaRadioButton.isSelected());
    	sOptimizeCheckbox.setEnabled(scanBmaRadioButton.isSelected());
    	sG0Checkbox.setEnabled(scanBmaRadioButton.isSelected());
    	sG0Spinner.setEnabled(scanBmaRadioButton.isSelected() && sG0Checkbox.isSelected());
    	sIterlimLabel.setEnabled(scanBmaRadioButton.isSelected() && sOptimizeCheckbox.isSelected());
    	sIterlimSpinner.setEnabled(scanBmaRadioButton.isSelected() && sOptimizeCheckbox.isSelected());
    	sEpsilonLabel.setEnabled(scanBmaRadioButton.isSelected() && sOptimizeCheckbox.isSelected());
    	sEpsilonSpinner.setEnabled(scanBmaRadioButton.isSelected() && sOptimizeCheckbox.isSelected());
    	
    	iORLabel.setEnabled(iBmaRadioButton.isSelected());
    	iORSpinner.setEnabled(iBmaRadioButton.isSelected());
    	iNbestLabel.setEnabled(iBmaRadioButton.isSelected());
    	iNbestSpinner.setEnabled(iBmaRadioButton.isSelected());
    	iMaxNvarLabel.setEnabled(iBmaRadioButton.isSelected());
    	iMaxNvarSpinner.setEnabled(iBmaRadioButton.isSelected());
    	iThresProbne0Label.setEnabled(iBmaRadioButton.isSelected());
    	iThresProbne0Spinner.setEnabled(iBmaRadioButton.isSelected());
    	iKeepModelsCheckbox.setEnabled(iBmaRadioButton.isSelected());
    	iMaxIterLabel.setEnabled(iBmaRadioButton.isSelected());
    	iMaxIterSpinner.setEnabled(iBmaRadioButton.isSelected());
    }
    
    private void enableOrDisableVariableOrderingControls() {
    	
    	nvarSpinner.setEnabled(nvarCheckBox.isSelected());
    	priorProbSpinner.setEnabled(constantPriorRadioButton.isSelected());
    	priorProbComboBox.setEnabled(tablePriorRadioButton.isSelected());
    }
    
    private void populateControls(InferenceParameters taskParameters) {
    	
    	scanBmaRadioButton.setSelected(InferenceParameters.ALGORITHM_SCANBMA.equals(taskParameters.getAlgorithm()));
    	sORSpinner.setValue(taskParameters.getScanBmaOR());
    	sUsegCheckbox.setSelected(taskParameters.isScanBmaUseg());
    	sThresProbne0Spinner.setValue(taskParameters.getScanBmaThresProbne0());
    	sOptimizeCheckbox.setSelected(taskParameters.isScanBmaOptimize());
    	sG0Checkbox.setSelected(taskParameters.getScanBmaG0() != null);
    	sG0Spinner.setValue(taskParameters.getScanBmaG0() != null? taskParameters.getScanBmaG0() : 0);
    	sIterlimSpinner.setValue(taskParameters.getScanBmaIterlim());
    	sEpsilonSpinner.setValue(taskParameters.getScanBmaEpsilon());
    	
    	iBmaRadioButton.setSelected(InferenceParameters.ALGORITHM_IBMA.equals(taskParameters.getAlgorithm()));
    	iORSpinner.setValue(taskParameters.getIterBmaOR());
    	iNbestSpinner.setValue(taskParameters.getIterBmaNbest());
    	iMaxNvarSpinner.setValue(taskParameters.getIterBmaMaxNvar());
    	iThresProbne0Spinner.setValue(taskParameters.getIterBmaThresProbne0());
    	iKeepModelsCheckbox.setSelected(taskParameters.isIterBmaKeepModels());
    	iMaxIterSpinner.setValue(taskParameters.getIterBmaMaxIter());
    	
    	nvarCheckBox.setSelected(taskParameters.getNvar() != null);
    	nvarSpinner.setValue(taskParameters.getNvar() != null? taskParameters.getNvar() : 0);
    	orderingComboBox.setSelectedItem(taskParameters.getOrdering());
    	noPriorRadioButton.setSelected(!taskParameters.isConstantPrior() && !taskParameters.isTablePrior());
    	constantPriorRadioButton.setSelected(taskParameters.isConstantPrior());
    	priorProbSpinner.setValue(taskParameters.isConstantPrior()? taskParameters.getPriorProb() : 0);
    	tablePriorRadioButton.setSelected(taskParameters.isTablePrior());
    	
    	posteriorProbThresSpinner.setValue(taskParameters.getPostProbThreshold());
    	
    	if (priorProbTable != null) {
    		priorProbComboBox.setSelectedItem(priorProbTable);
    	}
    	
    	enableOrDisableAlgorithmSpecificControls();
    	enableOrDisableVariableOrderingControls();
    }
    
    public CyTable getPriorProbTable() {
    	return priorProbTable;
    }

    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        scanBmaPanel = new javax.swing.JPanel();
        sORLabel = new javax.swing.JLabel();
        sUsegCheckbox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        sOptimizeCheckbox = new javax.swing.JCheckBox();
        sG0Checkbox = new javax.swing.JCheckBox();
        sG0Spinner = new javax.swing.JSpinner();
        sIterlimLabel = new javax.swing.JLabel();
        sIterlimSpinner = new javax.swing.JSpinner();
        sEpsilonSpinner = new javax.swing.JSpinner();
        sEpsilonLabel = new javax.swing.JLabel();
        sORSpinner = new javax.swing.JSpinner();
        sThresProbne0Label = new javax.swing.JLabel();
        sThresProbne0Spinner = new javax.swing.JSpinner();
        scanBmaRadioButton = new javax.swing.JRadioButton();
        iBmaRadioButton = new javax.swing.JRadioButton();
        iBmaPanel = new javax.swing.JPanel();
        iORLabel = new javax.swing.JLabel();
        iORSpinner = new javax.swing.JSpinner();
        iNbestLabel = new javax.swing.JLabel();
        iNbestSpinner = new javax.swing.JSpinner();
        iMaxNvarLabel = new javax.swing.JLabel();
        iMaxNvarSpinner = new javax.swing.JSpinner();
        iThresProbne0Label = new javax.swing.JLabel();
        iThresProbne0Spinner = new javax.swing.JSpinner();
        iKeepModelsCheckbox = new javax.swing.JCheckBox();
        iMaxIterLabel = new javax.swing.JLabel();
        iMaxIterSpinner = new javax.swing.JSpinner();
        resetButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        nvarSpinner = new javax.swing.JSpinner();
        orderingLabel = new javax.swing.JLabel();
        orderingComboBox = new javax.swing.JComboBox();
        nvarCheckBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        noPriorRadioButton = new javax.swing.JRadioButton();
        constantPriorRadioButton = new javax.swing.JRadioButton();
        priorProbSpinner = new javax.swing.JSpinner();
        tablePriorRadioButton = new javax.swing.JRadioButton();
        priorProbComboBox = new javax.swing.JComboBox();
        posteriorProbThresLabel = new javax.swing.JLabel();
        posteriorProbThresSpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Advanced Parameters");
        setResizable(false);

        scanBmaPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        sORLabel.setText("OR");

        sUsegCheckbox.setText("useg");
        sUsegCheckbox.setToolTipText("<html>A logical value indicating whether to use Zellner's g-prior in model likelihood evaluation.<br>If set to FALSE, ScanBMA will use BIC to approximate the likelihood.</html>");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "gCtrl"));

        sOptimizeCheckbox.setText("optimize");
        sOptimizeCheckbox.setToolTipText("<html>A logical value indicating whether to optimize g<br>using an iterative EM algorithm or use a fixed value of g.</html>");
        sOptimizeCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sOptimizeCheckboxActionPerformed(evt);
            }
        });

        sG0Checkbox.setText("g0");
        sG0Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sG0CheckboxActionPerformed(evt);
            }
        });

        sG0Spinner.setModel(new javax.swing.SpinnerNumberModel());
        sG0Spinner.setToolTipText("An initial value of g to use if optimize is TRUE, or the fixed value to use without optimization.");
        sG0Spinner.setEditor(new javax.swing.JSpinner.NumberEditor(sG0Spinner, ""));

        sIterlimLabel.setText("iterlim");

        sIterlimSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        sIterlimSpinner.setToolTipText("If optimize is TRUE, the maximum number of iterations of the EM algorithm to use.");
        sIterlimSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(sIterlimSpinner, ""));

        sEpsilonSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(0.1d)));
        sEpsilonSpinner.setToolTipText("If optimize is TRUE, the precision with which to find g using the EM algorithm.");
        sEpsilonSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(sEpsilonSpinner, ""));

        sEpsilonLabel.setText("epsilon");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sOptimizeCheckbox)
                .addGap(18, 18, 18)
                .addComponent(sG0Checkbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sG0Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sIterlimLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sIterlimSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sEpsilonLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sEpsilonSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sOptimizeCheckbox)
                    .addComponent(sG0Checkbox)
                    .addComponent(sG0Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sIterlimLabel)
                    .addComponent(sIterlimSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sEpsilonLabel)
                    .addComponent(sEpsilonSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        sORSpinner.setModel(new javax.swing.SpinnerNumberModel());
        sORSpinner.setToolTipText("A number specifying the maximum ratio for excluding models in Occam's window.");
        sORSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(sORSpinner, ""));

        sThresProbne0Label.setText("thresProbne0");

        sThresProbne0Spinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
        sThresProbne0Spinner.setToolTipText("<html>Threshold (in percent) for the posterior probability that each variable<br>has a non-zero coefficient (in percent). Variables with posterior probability<br>less than thresProbne0 are removed in future BMA iterations.</html>");
        sThresProbne0Spinner.setEditor(new javax.swing.JSpinner.NumberEditor(sThresProbne0Spinner, ""));

        javax.swing.GroupLayout scanBmaPanelLayout = new javax.swing.GroupLayout(scanBmaPanel);
        scanBmaPanel.setLayout(scanBmaPanelLayout);
        scanBmaPanelLayout.setHorizontalGroup(
            scanBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scanBmaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scanBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, Short.MAX_VALUE)
                    .addGroup(scanBmaPanelLayout.createSequentialGroup()
                        .addComponent(sORLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sORSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sUsegCheckbox)
                        .addGap(18, 18, 18)
                        .addComponent(sThresProbne0Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sThresProbne0Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        scanBmaPanelLayout.setVerticalGroup(
            scanBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scanBmaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scanBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sORLabel)
                    .addComponent(sUsegCheckbox)
                    .addComponent(sORSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sThresProbne0Label)
                    .addComponent(sThresProbne0Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonGroup1.add(scanBmaRadioButton);
        scanBmaRadioButton.setText("Use ScanBMA");
        scanBmaRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanBmaRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(iBmaRadioButton);
        iBmaRadioButton.setText("Use iBMA");
        iBmaRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iBmaRadioButtonActionPerformed(evt);
            }
        });

        iBmaPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        iORLabel.setText("OR");

        iORSpinner.setModel(new javax.swing.SpinnerNumberModel());
        iORSpinner.setToolTipText("A number specifying the maximum ratio for excluding models in Occam's window.");
        iORSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(iORSpinner, ""));

        iNbestLabel.setText("nbest");

        iNbestSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        iNbestSpinner.setToolTipText("<html>A positive integer specifying the number of models of each size to be considered<br>by leaps-and-bounds in determining the model space for Bayesian Model Averaging.</html>");
        iNbestSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(iNbestSpinner, ""));

        iMaxNvarLabel.setText("maxNvar");

        iMaxNvarSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        iMaxNvarSpinner.setToolTipText("<html>A positive integer specifying the maximum number of variables<br>(excluding the intercept) used in each iteration of BMA.</html>");
        iMaxNvarSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(iMaxNvarSpinner, ""));

        iThresProbne0Label.setText("thresProbne0");

        iThresProbne0Spinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
        iThresProbne0Spinner.setToolTipText("<html>Threshold (in percent) for the posterior probability that each variable<br>has a non-zero coefficient (in percent). Variables with posterior probability<br>less than thresProbne0 are removed in future BMA iterations.</html>");
        iThresProbne0Spinner.setEditor(new javax.swing.JSpinner.NumberEditor(iThresProbne0Spinner, ""));

        iKeepModelsCheckbox.setText("keepModels");
        iKeepModelsCheckbox.setToolTipText("<html>A logical value indicating whether or not to keep the BMA models<br>from all of the iterations and apply Occam's window using OR at the end,<br>or to apply Occam's window in all BMA iterations and return the final model.</html>");

        iMaxIterLabel.setText("maxIter");

        iMaxIterSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        iMaxIterSpinner.setToolTipText("A positive integer giving a limit on the number of iterations of iterateBMAlm.");
        iMaxIterSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(iMaxIterSpinner, ""));

        javax.swing.GroupLayout iBmaPanelLayout = new javax.swing.GroupLayout(iBmaPanel);
        iBmaPanel.setLayout(iBmaPanelLayout);
        iBmaPanelLayout.setHorizontalGroup(
            iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(iBmaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(iBmaPanelLayout.createSequentialGroup()
                        .addComponent(iThresProbne0Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iThresProbne0Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(iKeepModelsCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(iMaxIterLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iMaxIterSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(iBmaPanelLayout.createSequentialGroup()
                        .addComponent(iORLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iORSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(iNbestLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iNbestSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(iMaxNvarLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iMaxNvarSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        iBmaPanelLayout.setVerticalGroup(
            iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(iBmaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iORLabel)
                    .addComponent(iORSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iNbestLabel)
                    .addComponent(iNbestSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iMaxNvarLabel)
                    .addComponent(iMaxNvarSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(iMaxIterLabel)
                        .addComponent(iMaxIterSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(iKeepModelsCheckbox))
                    .addGroup(iBmaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(iThresProbne0Label)
                        .addComponent(iThresProbne0Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        resetButton.setText("Reset to defaults");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        nvarSpinner.setModel(new javax.swing.SpinnerNumberModel());
        nvarSpinner.setToolTipText("<html>The number of top-ranked genes to be considered in the modeling.<br>Large values of nvar (above 1000) may slow down the execution.</html>");
        nvarSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(nvarSpinner, ""));

        orderingLabel.setText("ordering");

        orderingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "bic1", "prior", "bic1+prior" }));
        orderingComboBox.setToolTipText("The ordering to be used for the genes or variables.");

        nvarCheckBox.setText("nvar");
        nvarCheckBox.setToolTipText("<html>Enables manual override of nvar value if checked.<br>If unchecked, nvar is equal to the number of genes.</html>");
        nvarCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nvarCheckBoxActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Prior probabilities"));

        buttonGroup2.add(noPriorRadioButton);
        noPriorRadioButton.setText("None");
        noPriorRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noPriorRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(constantPriorRadioButton);
        constantPriorRadioButton.setText("Constant");
        constantPriorRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                constantPriorRadioButtonActionPerformed(evt);
            }
        });

        priorProbSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 1.0d, 0.01d));
        priorProbSpinner.setToolTipText("The probability of a regulator-gene pair in the network.");
        priorProbSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(priorProbSpinner, ""));

        buttonGroup2.add(tablePriorRadioButton);
        tablePriorRadioButton.setText("From table");
        tablePriorRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tablePriorRadioButtonActionPerformed(evt);
            }
        });

        priorProbComboBox.setToolTipText("A matrix in which the (i,j) entry is the estimated prior probability that gene i regulates gene j.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noPriorRadioButton)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(constantPriorRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(priorProbSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tablePriorRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(priorProbComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(noPriorRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(constantPriorRadioButton)
                    .addComponent(priorProbSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tablePriorRadioButton)
                    .addComponent(priorProbComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        posteriorProbThresLabel.setText("Edge probability threshold");

        posteriorProbThresSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
        posteriorProbThresSpinner.setToolTipText("<html>Threshold (in percent) for the posterior edge<br>probability to be included in the final network.</html>");
        posteriorProbThresSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(posteriorProbThresSpinner, ""));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(nvarCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nvarSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(orderingLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orderingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(143, 150, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(scanBmaRadioButton)
                                    .addComponent(iBmaRadioButton))
                                .addGap(0, 301, Short.MAX_VALUE))
                            .addComponent(iBmaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scanBmaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(resetButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(posteriorProbThresLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(posteriorProbThresSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scanBmaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scanBmaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(iBmaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iBmaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nvarSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderingLabel)
                    .addComponent(orderingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nvarCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(posteriorProbThresLabel)
                    .addComponent(posteriorProbThresSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetButton)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }
}
