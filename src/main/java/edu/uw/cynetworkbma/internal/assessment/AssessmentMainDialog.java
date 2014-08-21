package edu.uw.cynetworkbma.internal.assessment;

import javax.swing.*;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.*;
import org.cytoscape.work.TaskManager;

import edu.uw.cynetworkbma.internal.*;

public class AssessmentMainDialog extends JDialog {
	
	private final CySwingApplication swingApplication;
	private final TaskManager taskManager;
	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	
	private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox edgeProbCombo;
    private javax.swing.JLabel edgeProbLabel;
    private javax.swing.JTextField hostField;
    private javax.swing.JLabel hostLabel;
    private javax.swing.JComboBox networkCombo;
    private javax.swing.JPanel networkPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JFormattedTextField portField;
    private javax.swing.JLabel portLabel;
    private javax.swing.JComboBox referenceNetworkCombo;
    private javax.swing.JPanel referencePanel;
    private javax.swing.JPanel serverPanel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JCheckBox usernamePasswordCheckbox;
    
    public AssessmentMainDialog(
    		CySwingApplication swingApplication,
    		TaskManager taskManager,
    		CyTableFactory tableFactory,
    		CyTableManager tableManager,
    		CyNetworkManager networkManager) {
    	
    	super(swingApplication.getJFrame(), false);
    	
    	this.swingApplication = swingApplication;
    	this.taskManager = taskManager;
    	this.tableFactory = tableFactory;
    	this.tableManager = tableManager;
    	
    	initComponents();
    	setLocationRelativeTo(swingApplication.getJFrame());
    	
		ConnectionParameters connectionParameters = ConnectionParameters.getDefault();
		hostField.setText(connectionParameters.getHost());
		portField.setText("" + connectionParameters.getPort());
		
    	for (CyNetwork network : networkManager.getNetworkSet()) {
			networkCombo.addItem(network);
			referenceNetworkCombo.addItem(network);
		}
    	
    	okButton.setEnabled(
    			networkCombo.getSelectedIndex() >= 0 && referenceNetworkCombo.getSelectedIndex() >= 0);
    }
    
    private void usernamePasswordCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	usernameLabel.setEnabled(usernamePasswordCheckbox.isSelected());
        usernameField.setEnabled(usernamePasswordCheckbox.isSelected());
        passwordLabel.setEnabled(usernamePasswordCheckbox.isSelected());
        passwordField.setEnabled(usernamePasswordCheckbox.isSelected());
    }
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	ConnectionParameters connectionParameters = new ConnectionParameters();
    	connectionParameters.setHost(hostField.getText());
        connectionParameters.setPort(Integer.parseInt(portField.getText()));
        
        if (usernamePasswordCheckbox.isSelected()) {
        	connectionParameters.setLoginRequired(true);
        	connectionParameters.setUsername(usernameField.getText());
        	connectionParameters.setPassword(new String(passwordField.getPassword()));
        }
        
        try {
	        RserveAssessmentTaskFactory taskFactory = new RserveAssessmentTaskFactory(
	        		swingApplication,
	        		taskManager,
	        		tableFactory,
	        		tableManager,
	        		connectionParameters,
	        		(CyNetwork)networkCombo.getSelectedItem(),
	        		(String)edgeProbCombo.getSelectedItem(),
	        		(CyNetwork)referenceNetworkCombo.getSelectedItem());
	        taskManager.execute(taskFactory.createTaskIterator());
	        setVisible(false);
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(
        			this,
        			"Error: " + e.getMessage(),
        			Constants.APP_NAME,
        			JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
    	setVisible(false);
    }
    
    private void networkComboActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	edgeProbCombo.removeAllItems();
        CyNetwork network = (CyNetwork)networkCombo.getSelectedItem();
        CyTable edgeTable = network.getDefaultEdgeTable();
        
        for(CyColumn column : edgeTable.getColumns()) {
        	if (!column.isPrimaryKey() && !column.isImmutable()) {
        		edgeProbCombo.addItem(column.getName());
        	}
        }
    }

    private void initComponents() {

        networkPanel = new javax.swing.JPanel();
        networkCombo = new javax.swing.JComboBox();
        edgeProbLabel = new javax.swing.JLabel();
        edgeProbCombo = new javax.swing.JComboBox();
        referencePanel = new javax.swing.JPanel();
        referenceNetworkCombo = new javax.swing.JComboBox();
        serverPanel = new javax.swing.JPanel();
        hostLabel = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        portLabel = new javax.swing.JLabel();
        usernamePasswordCheckbox = new javax.swing.JCheckBox();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        portField = new javax.swing.JFormattedTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Assess Network");
        setResizable(false);

        networkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Network to assess"));

        networkCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                networkComboActionPerformed(evt);
            }
        });

        edgeProbLabel.setText("Edge probability:");

        javax.swing.GroupLayout networkPanelLayout = new javax.swing.GroupLayout(networkPanel);
        networkPanel.setLayout(networkPanelLayout);
        networkPanelLayout.setHorizontalGroup(
            networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(networkCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(networkPanelLayout.createSequentialGroup()
                        .addComponent(edgeProbLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edgeProbCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        networkPanelLayout.setVerticalGroup(
            networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(networkCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edgeProbLabel)
                    .addComponent(edgeProbCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        referencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Reference network"));

        javax.swing.GroupLayout referencePanelLayout = new javax.swing.GroupLayout(referencePanel);
        referencePanel.setLayout(referencePanelLayout);
        referencePanelLayout.setHorizontalGroup(
            referencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(referencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(referenceNetworkCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        referencePanelLayout.setVerticalGroup(
            referencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(referencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(referenceNetworkCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serverPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "R server"));

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

        javax.swing.GroupLayout serverPanelLayout = new javax.swing.GroupLayout(serverPanel);
        serverPanel.setLayout(serverPanelLayout);
        serverPanelLayout.setHorizontalGroup(
            serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(serverPanelLayout.createSequentialGroup()
                        .addComponent(usernamePasswordCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(passwordLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addGroup(serverPanelLayout.createSequentialGroup()
                        .addComponent(hostLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hostField)
                        .addGap(18, 18, 18)
                        .addComponent(portLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        serverPanelLayout.setVerticalGroup(
            serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverPanelLayout.createSequentialGroup()
                .addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostLabel)
                    .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portLabel)
                    .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernamePasswordCheckbox)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(serverPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(networkPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(referencePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serverPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(networkPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(referencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
}
