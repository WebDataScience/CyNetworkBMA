package edu.uw.cynetworkbma.internal.inference;

import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.*;
import org.cytoscape.work.TaskManager;

import edu.uw.cynetworkbma.internal.*;
import edu.uw.cynetworkbma.internal.jobtracking.InferenceJobTracker;

public class InferenceMainDialog extends JDialog {

	private static final long serialVersionUID = 1176557431565741178L;
	
	private static final int STEADY_STATE_LIST_INDEX = 0;
	private static final int TIME_SERIES_LIST_INDEX = 1;
	
	private final InferenceParameters inferenceParameters;
	private CyTable priorProbTable;
	
	private final CyTableManager tableManager;
	private final TaskManager taskManager;
	private final NetworkServiceReferences serviceReferences;
	
	private javax.swing.JButton advancedButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel columnsLabel;
    private javax.swing.JList columnsList;
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JPanel dataSourcePanel;
    private javax.swing.JComboBox dataTypeComboBox;
    private javax.swing.JLabel dataTypeLabel;
    private javax.swing.JTextField hostField;
    private javax.swing.JLabel hostLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField networkNameField;
    private javax.swing.JPanel networkNamePanel;
    private javax.swing.JLabel noOfTimePointsLabel;
    private javax.swing.JSpinner noOfTimePointsSpinner;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JFormattedTextField portField;
    private javax.swing.JLabel portLabel;
    private javax.swing.JRadioButton rowsAreExperimentsRadioButton;
    private javax.swing.JRadioButton rowsAreGenesRadioButton;
    private javax.swing.JPanel sourceFormatPanel;
    private javax.swing.JComboBox sourceTableCombo;
    private javax.swing.JLabel tableKeyLabel;
    private javax.swing.JLabel tableKeyNameLabel;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JCheckBox usernamePasswordCheckbox;
	
	public InferenceMainDialog(
			CySwingApplication swingApplication,
			TaskManager taskManager,
			CyTableManager tableManager,
			NetworkServiceReferences serviceReferences) {
		
		super(swingApplication.getJFrame(), false);
		
		inferenceParameters = InferenceParameters.getDefault();
		
		this.taskManager = taskManager;
		this.tableManager = tableManager;
		this.serviceReferences = serviceReferences;
		
		initComponents();
		setLocationRelativeTo(swingApplication.getJFrame());
		
		ConnectionParameters connectionParameters = ConnectionParameters.getDefault();
		hostField.setText(connectionParameters.getHost());
		portField.setText("" + connectionParameters.getPort());
		
		dataTypeComboBox.setSelectedIndex(
				inferenceParameters.isTimeSeries()? TIME_SERIES_LIST_INDEX : STEADY_STATE_LIST_INDEX);
		noOfTimePointsSpinner.setValue(inferenceParameters.getNumberOfTimePoints());
		
		for (CyTable table : tableManager.getGlobalTables()) {
			if (table.isPublic()) {
				sourceTableCombo.addItem(table);
			}
		}
		
		int jobCount = InferenceJobTracker.getInstance().getTotalJobCount();
		networkNameField.setText(Constants.NETWORK_NAME + (jobCount > 0? " " + (jobCount + 1) : ""));
		
		enableOrDisableOKButton();
	}
	
    private void usernamePasswordCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	usernameLabel.setEnabled(usernamePasswordCheckbox.isSelected());
        usernameField.setEnabled(usernamePasswordCheckbox.isSelected());
        passwordLabel.setEnabled(usernamePasswordCheckbox.isSelected());
        passwordField.setEnabled(usernamePasswordCheckbox.isSelected());
    }

    private void sourceTableComboActionPerformed(ActionEvent evt) {
    	
        CyTable table = (CyTable)sourceTableCombo.getSelectedItem();
        
        tableKeyNameLabel.setText(table.getPrimaryKey().getName());
        
        DefaultListModel listModel = new DefaultListModel();
        for (CyColumn column : table.getColumns()) {
        	if (!column.isPrimaryKey()) {
        		listModel.addElement(new ColumnWrapper(column));
        	}
        }
        
        columnsList.setModel(listModel);
        
        int[] indicesToSelect = new int[listModel.size()];
        for (int i = 0; i < listModel.size(); i++) {
        	indicesToSelect[i] = i;
        }
        
        columnsList.setSelectedIndices(indicesToSelect);
        enableOrDisableOKButton();
    }
    
    private class ColumnWrapper {
    	
    	public final CyColumn column;
    	
    	public ColumnWrapper(CyColumn column) {
    		this.column = column;
    	}
    	
    	public String toString() {
    		return column.getName();
    	}
    }
    
    private void columnsListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        enableOrDisableOKButton();
    }
    
    private void dataTypeComboBoxActionPerformed(ActionEvent evt) {   
    	
        boolean showTimeSeriesControls = dataTypeComboBox.getSelectedIndex() == TIME_SERIES_LIST_INDEX;
        noOfTimePointsLabel.setEnabled(showTimeSeriesControls);
        noOfTimePointsSpinner.setEnabled(showTimeSeriesControls);
    }
    
    private void enableOrDisableOKButton() {
    	okButton.setEnabled(columnsList.getSelectedValues().length > 0);
    }
    
    private void okButtonActionPerformed(ActionEvent evt) {
        
    	List<CyColumn> selectedColumns = new ArrayList<CyColumn>();
    	// using a deprecated method for compatibility with JRE 1.6
    	for (Object wrapper : columnsList.getSelectedValues()) {
    		selectedColumns.add(((ColumnWrapper)wrapper).column);
    	}
    	
    	try {
	    	ConnectionParameters connectionParameters = new ConnectionParameters();
	    	connectionParameters.setHost(hostField.getText());
	        connectionParameters.setPort(Integer.parseInt(portField.getText()));
	        
	        if (usernamePasswordCheckbox.isSelected()) {
	        	connectionParameters.setLoginRequired(true);
	        	connectionParameters.setUsername(usernameField.getText());
	        	connectionParameters.setPassword(new String(passwordField.getPassword()));
	        }
	        
	    	TableReader tableReader = new TableReader();
	    	TableReader.NamedExpressionMatrix dataMatrix = tableReader.getExpressionMatrix(
	    			(CyTable)sourceTableCombo.getSelectedItem(),
	    			selectedColumns,
	    			rowsAreGenesRadioButton.isSelected());
	    	
	        inferenceParameters.setGeneNames(dataMatrix.geneNames);
	        inferenceParameters.setData(dataMatrix.data);
	        inferenceParameters.setTimeSeries(dataTypeComboBox.getSelectedIndex() == TIME_SERIES_LIST_INDEX);
	        if (inferenceParameters.isTimeSeries()) {
	        	inferenceParameters.setNumberOfTimePoints((Integer)noOfTimePointsSpinner.getValue());
	        }
	        
	    	if (inferenceParameters.isTablePrior()) {
		    	TableReader.PriorProbabilityMatrix priorProbMatrix =
		    			tableReader.getPriorProbabilityMatrix(priorProbTable, dataMatrix.geneNames);
		    	inferenceParameters.setPriorProbMatrix(priorProbMatrix.data);
		    	inferenceParameters.setRegulatorNames(priorProbMatrix.regulatorGeneNames);
		    	inferenceParameters.setRegulatedNames(priorProbMatrix.regulatedGeneNames);
	    	}
	    	
    		InferenceJob job = new RserveInferenceJob(
    				networkNameField.getText(),
    				serviceReferences,
    				connectionParameters,
    				inferenceParameters,
    				taskManager);
    		job.execute();
    		setVisible(false);
    		JOptionPane.showMessageDialog(
    				this,
    				"Your job has been submitted. You will be notified when the work is complete.\n"
    						+ "You can track job progress by going to " + Constants.MENU + " \u2192 " + Constants.APP_NAME + " \u2192 Show Jobs...\n"
    						+ "If you close Cytoscape before the job finishes, the work will be lost and you will need to re-submit your job.",
    				Constants.APP_NAME,
    				JOptionPane.INFORMATION_MESSAGE);
    	} catch (Exception e) {
    		JOptionPane.showMessageDialog(
    				this, "Error: " + e.getMessage(), Constants.APP_NAME, JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    private void cancelButtonActionPerformed(ActionEvent evt) {
        setVisible(false);
    }
    
    private void advancedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        InferenceAdvancedDialog advancedDialog = new InferenceAdvancedDialog(
        		this, inferenceParameters, priorProbTable, tableManager);
        advancedDialog.setVisible(true);
        priorProbTable = advancedDialog.getPriorProbTable();
    }
    
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        connectionPanel = new javax.swing.JPanel();
        hostLabel = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        portLabel = new javax.swing.JLabel();
        usernamePasswordCheckbox = new javax.swing.JCheckBox();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        portField = new javax.swing.JFormattedTextField();
        dataSourcePanel = new javax.swing.JPanel();
        tableLabel = new javax.swing.JLabel();
        sourceTableCombo = new javax.swing.JComboBox();
        tableKeyLabel = new javax.swing.JLabel();
        tableKeyNameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        columnsList = new javax.swing.JList();
        columnsLabel = new javax.swing.JLabel();
        sourceFormatPanel = new javax.swing.JPanel();
        rowsAreGenesRadioButton = new javax.swing.JRadioButton();
        rowsAreExperimentsRadioButton = new javax.swing.JRadioButton();
        dataTypeLabel = new javax.swing.JLabel();
        dataTypeComboBox = new javax.swing.JComboBox();
        noOfTimePointsLabel = new javax.swing.JLabel();
        noOfTimePointsSpinner = new javax.swing.JSpinner();
        networkNamePanel = new javax.swing.JPanel();
        networkNameField = new javax.swing.JTextField();
        advancedButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Infer Network");
        setResizable(false);

        connectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "R server"));

        hostLabel.setText("Address:");

        portLabel.setText("Port:");

        usernamePasswordCheckbox.setToolTipText("");
        usernamePasswordCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernamePasswordCheckboxActionPerformed(evt);
            }
        });

        usernameLabel.setText("Username:");
        usernameLabel.setEnabled(false);

        usernameField.setEnabled(false);

        passwordLabel.setText("Password:");
        passwordLabel.setEnabled(false);

        passwordField.setToolTipText("");
        passwordField.setEnabled(false);

        portField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        javax.swing.GroupLayout connectionPanelLayout = new javax.swing.GroupLayout(connectionPanel);
        connectionPanel.setLayout(connectionPanelLayout);
        connectionPanelLayout.setHorizontalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(connectionPanelLayout.createSequentialGroup()
                        .addComponent(usernamePasswordCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(passwordLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 37, Short.MAX_VALUE))
                    .addGroup(connectionPanelLayout.createSequentialGroup()
                        .addComponent(hostLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hostField)
                        .addGap(18, 18, 18)
                        .addComponent(portLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        connectionPanelLayout.setVerticalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostLabel)
                    .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portLabel)
                    .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernamePasswordCheckbox)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        dataSourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Data source"));

        tableLabel.setText("Select table:");

        sourceTableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceTableComboActionPerformed(evt);
            }
        });

        tableKeyLabel.setText("Table key: ");

        columnsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                columnsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(columnsList);

        columnsLabel.setText("Columns to include:");
        columnsLabel.setToolTipText("");

        javax.swing.GroupLayout dataSourcePanelLayout = new javax.swing.GroupLayout(dataSourcePanel);
        dataSourcePanel.setLayout(dataSourcePanelLayout);
        dataSourcePanelLayout.setHorizontalGroup(
            dataSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataSourcePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                    .addGroup(dataSourcePanelLayout.createSequentialGroup()
                        .addComponent(tableLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sourceTableCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(dataSourcePanelLayout.createSequentialGroup()
                        .addGroup(dataSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dataSourcePanelLayout.createSequentialGroup()
                                .addComponent(tableKeyLabel)
                                .addGap(18, 18, 18)
                                .addComponent(tableKeyNameLabel))
                            .addComponent(columnsLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dataSourcePanelLayout.setVerticalGroup(
            dataSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataSourcePanelLayout.createSequentialGroup()
                .addGroup(dataSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableLabel)
                    .addComponent(sourceTableCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableKeyLabel)
                    .addComponent(tableKeyNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(columnsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addContainerGap())
        );

        sourceFormatPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Source format"));

        buttonGroup1.add(rowsAreGenesRadioButton);
        rowsAreGenesRadioButton.setSelected(true);
        rowsAreGenesRadioButton.setText("Genes as rows, experiments as columns");

        buttonGroup1.add(rowsAreExperimentsRadioButton);
        rowsAreExperimentsRadioButton.setText("Genes as columns, experiments as rows");

        dataTypeLabel.setText("Data type:");

        dataTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Steady state", "Time series" }));
        dataTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataTypeComboBoxActionPerformed(evt);
            }
        });

        noOfTimePointsLabel.setText("Number of time points:");
        noOfTimePointsLabel.setEnabled(false);

        noOfTimePointsSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(2), null, Integer.valueOf(1)));
        noOfTimePointsSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(noOfTimePointsSpinner, ""));
        noOfTimePointsSpinner.setEnabled(false);
        noOfTimePointsSpinner.setValue(1);

        javax.swing.GroupLayout sourceFormatPanelLayout = new javax.swing.GroupLayout(sourceFormatPanel);
        sourceFormatPanel.setLayout(sourceFormatPanelLayout);
        sourceFormatPanelLayout.setHorizontalGroup(
            sourceFormatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourceFormatPanelLayout.createSequentialGroup()
                .addGroup(sourceFormatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rowsAreGenesRadioButton)
                    .addComponent(rowsAreExperimentsRadioButton))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(sourceFormatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sourceFormatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sourceFormatPanelLayout.createSequentialGroup()
                        .addComponent(noOfTimePointsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noOfTimePointsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(sourceFormatPanelLayout.createSequentialGroup()
                        .addComponent(dataTypeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sourceFormatPanelLayout.setVerticalGroup(
            sourceFormatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourceFormatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rowsAreGenesRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rowsAreExperimentsRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sourceFormatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataTypeLabel)
                    .addComponent(dataTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sourceFormatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noOfTimePointsLabel)
                    .addComponent(noOfTimePointsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        networkNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Network name"));

        javax.swing.GroupLayout networkNamePanelLayout = new javax.swing.GroupLayout(networkNamePanel);
        networkNamePanel.setLayout(networkNamePanelLayout);
        networkNamePanelLayout.setHorizontalGroup(
            networkNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(networkNameField)
                .addContainerGap())
        );
        networkNamePanelLayout.setVerticalGroup(
            networkNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(networkNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        advancedButton.setText("Advanced...");
        advancedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedButtonActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(advancedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(connectionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dataSourcePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sourceFormatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(networkNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataSourcePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceFormatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(networkNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(advancedButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
}
