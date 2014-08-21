package edu.uw.cynetworkbma.internal.assessment;

import java.util.List;

import javax.swing.*;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.*;
import org.cytoscape.work.TaskManager;

public class AssessmentResultsDialog extends JDialog {

	private final int INDEX_SCORES = 0;
	private final int INDEX_ROC = 1;
	private final int INDEX_PRC = 2;
	
	private final TaskManager taskManager;
	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	
	private final List<AssessmentScores> scores;
	private final List<AssessmentScores> scores100;
	private final byte[] rocImage;
	private final byte[] prcImage;
	
	private javax.swing.JLabel accLabel;
    private javax.swing.JTextField accText;
    private javax.swing.JLabel expectedLabel;
    private javax.swing.JTextField expectedText;
    private javax.swing.JLabel f1Label;
    private javax.swing.JTextField f1Text;
    private javax.swing.JLabel fdrLabel;
    private javax.swing.JTextField fdrText;
    private javax.swing.JLabel fnLabel;
    private javax.swing.JTextField fnText;
    private javax.swing.JLabel fpLabel;
    private javax.swing.JTextField fpText;
    private javax.swing.JLabel fprLabel;
    private javax.swing.JTextField fprText;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTabbedPane mainPanel;
    private javax.swing.JLabel mccLabel;
    private javax.swing.JTextField mccText;
    private javax.swing.JLabel npvLabel;
    private javax.swing.JTextField npvText;
    private javax.swing.JLabel oeLabel;
    private javax.swing.JTextField oeText;
    private javax.swing.JLabel ppvLabel;
    private javax.swing.JTextField ppvText;
    private javax.swing.JLabel prcLabel;
    private javax.swing.JPanel prcPanel;
    private javax.swing.JLabel rocLabel;
    private javax.swing.JPanel rocPanel;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPanel scoresPanel;
    private javax.swing.JLabel thresholdLabel;
    private javax.swing.JSlider thresholdSlider;
    private javax.swing.JLabel thresholdSliderLabel;
    private javax.swing.JLabel tnLabel;
    private javax.swing.JTextField tnText;
    private javax.swing.JLabel tnrLabel;
    private javax.swing.JTextField tnrText;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JLabel tpLabel;
    private javax.swing.JTextField tpText;
    private javax.swing.JLabel tprLabel;
    private javax.swing.JTextField tprText;
    
    public AssessmentResultsDialog(
    		CySwingApplication swingApplication,
    		TaskManager taskManager,
    		CyTableFactory tableFactory,
    		CyTableManager tableManager,
    		List<AssessmentScores> scores,
    		List<AssessmentScores> scores100,
    		byte[] rocImage,
    		byte[] prcImage) {
    	
    	super(swingApplication.getJFrame(), false);
    	
    	this.taskManager = taskManager;
    	this.tableFactory = tableFactory;
    	this.tableManager = tableManager;
    	this.scores = scores;
    	this.scores100 = scores100;
    	this.rocImage = rocImage;
    	this.prcImage = prcImage;
    	
    	initComponents();
    	setLocationRelativeTo(swingApplication.getJFrame());
    	updateScores();
    	rocLabel.setIcon(new ImageIcon(rocImage));
    	prcLabel.setIcon(new ImageIcon(prcImage));
    }
    
    private void thresholdSliderStateChanged(javax.swing.event.ChangeEvent evt) {                                             
    	updateScores();
    }
    
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if (mainPanel.getSelectedIndex() == INDEX_SCORES) {
        	SaveScoresTaskFactory taskFactory = new SaveScoresTaskFactory(
        			tableFactory,
        			tableManager,
        			scores);
        	taskManager.execute(taskFactory.createTaskIterator());
        } else {
        	byte[] image = mainPanel.getSelectedIndex() == INDEX_ROC? rocImage : prcImage;
        	SaveGraphTaskFactory taskFactory = new SaveGraphTaskFactory(image);
        	taskManager.execute(taskFactory.createTaskIterator());
        }
    }
    
    private void updateScores() {
    	
    	int threshold = thresholdSlider.getValue();
    	thresholdLabel.setText(toString(threshold));
    	AssessmentScores s = scores100.get(threshold);
        tpText.setText(toString(s.getTP()));
        fnText.setText(toString(s.getFN()));
        fpText.setText(toString(s.getFP()));
        tnText.setText(toString(s.getTN()));
        tprText.setText(toString(trimDecimals(s.getTPR())));
        tnrText.setText(toString(trimDecimals(s.getTNR())));
        fprText.setText(toString(trimDecimals(s.getFPR())));
        fdrText.setText(toString(trimDecimals(s.getFDR())));
        ppvText.setText(toString(trimDecimals(s.getPPV())));
        npvText.setText(toString(trimDecimals(s.getNPV())));
        f1Text.setText(toString(trimDecimals(s.getF1())));
        mccText.setText(toString(trimDecimals(s.getMCC())));
        accText.setText(toString(trimDecimals(s.getACC())));
        expectedText.setText(toString(trimDecimals(s.getExpected())));
        oeText.setText(toString(trimDecimals(s.getOE())));
    }
    
    private static String toString(int value) {
    	return "" + value;
    }
    
    private static String toString(double value) {
    	return "" + value;
    }
    
    private static double trimDecimals(double value) {
    	return Double.isNaN(value)? value : Math.round(10000.0 * value) / 10000.0;
    }
    
    private void initComponents() {

        mainPanel = new javax.swing.JTabbedPane();
        scoresPanel = new javax.swing.JPanel();
        thresholdSlider = new javax.swing.JSlider();
        tpLabel = new javax.swing.JLabel();
        tpText = new javax.swing.JTextField();
        fnLabel = new javax.swing.JLabel();
        fnText = new javax.swing.JTextField();
        fpLabel = new javax.swing.JLabel();
        fpText = new javax.swing.JTextField();
        tnLabel = new javax.swing.JLabel();
        tnText = new javax.swing.JTextField();
        tprLabel = new javax.swing.JLabel();
        tprText = new javax.swing.JTextField();
        tnrLabel = new javax.swing.JLabel();
        tnrText = new javax.swing.JTextField();
        fprLabel = new javax.swing.JLabel();
        fprText = new javax.swing.JTextField();
        fdrLabel = new javax.swing.JLabel();
        fdrText = new javax.swing.JTextField();
        ppvLabel = new javax.swing.JLabel();
        ppvText = new javax.swing.JTextField();
        npvLabel = new javax.swing.JLabel();
        npvText = new javax.swing.JTextField();
        f1Label = new javax.swing.JLabel();
        f1Text = new javax.swing.JTextField();
        mccLabel = new javax.swing.JLabel();
        mccText = new javax.swing.JTextField();
        accLabel = new javax.swing.JLabel();
        accText = new javax.swing.JTextField();
        expectedLabel = new javax.swing.JLabel();
        expectedText = new javax.swing.JTextField();
        oeLabel = new javax.swing.JLabel();
        oeText = new javax.swing.JTextField();
        thresholdSliderLabel = new javax.swing.JLabel();
        thresholdLabel = new javax.swing.JLabel();
        rocPanel = new javax.swing.JPanel();
        rocLabel = new javax.swing.JLabel();
        prcPanel = new javax.swing.JPanel();
        prcLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        toolsMenu = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Assessment Results");
        setResizable(false);

        thresholdSlider.setMajorTickSpacing(10);
        thresholdSlider.setMinorTickSpacing(1);
        thresholdSlider.setPaintLabels(true);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setSnapToTicks(true);
        thresholdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                thresholdSliderStateChanged(evt);
            }
        });

        tpLabel.setText("TP");

        tpText.setEditable(false);

        fnLabel.setText("FN");

        fnText.setEditable(false);

        fpLabel.setText("FP");

        fpText.setEditable(false);

        tnLabel.setText("TN");

        tnText.setEditable(false);

        tprLabel.setText("TPR");

        tprText.setEditable(false);

        tnrLabel.setText("TNR");

        tnrText.setEditable(false);

        fprLabel.setText("FPR");

        fprText.setEditable(false);

        fdrLabel.setText("FDR");

        fdrText.setEditable(false);

        ppvLabel.setText("PPV");

        ppvText.setEditable(false);

        npvLabel.setText("NPV");

        npvText.setEditable(false);

        f1Label.setText("F1");

        f1Text.setEditable(false);

        mccLabel.setText("MCC");

        mccText.setEditable(false);

        accLabel.setText("ACC");

        accText.setEditable(false);

        expectedLabel.setText("expected");

        expectedText.setEditable(false);

        oeLabel.setText("O/E");

        oeText.setEditable(false);

        thresholdSliderLabel.setText("Edge posterior probability threshold:");

        thresholdLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        thresholdLabel.setText("100");

        javax.swing.GroupLayout scoresPanelLayout = new javax.swing.GroupLayout(scoresPanel);
        scoresPanel.setLayout(scoresPanelLayout);
        scoresPanelLayout.setHorizontalGroup(
            scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(thresholdSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addComponent(thresholdSliderLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(thresholdLabel))
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(fnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fnText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(fpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fpText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(tnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tnText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(tprLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tprText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(tpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tpText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(62, 62, 62)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(fdrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fdrText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(ppvLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ppvText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(npvLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(npvText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(fprLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fprText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(tnrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tnrText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(oeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oeText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(expectedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(expectedText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(f1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(f1Text, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(mccLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mccText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(scoresPanelLayout.createSequentialGroup()
                                .addComponent(accLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(accText, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        scoresPanelLayout.setVerticalGroup(
            scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(thresholdSliderLabel)
                    .addComponent(thresholdLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(thresholdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tnrLabel)
                            .addComponent(tnrText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fprLabel)
                            .addComponent(fprText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fdrLabel)
                            .addComponent(fdrText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ppvLabel)
                            .addComponent(ppvText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(npvLabel)
                            .addComponent(npvText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tpLabel)
                            .addComponent(tpText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fnLabel)
                            .addComponent(fnText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fpLabel)
                            .addComponent(fpText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tnLabel)
                            .addComponent(tnText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tprLabel)
                            .addComponent(tprText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(f1Label)
                            .addComponent(f1Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mccLabel)
                            .addComponent(mccText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(accLabel)
                            .addComponent(accText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(expectedLabel)
                            .addComponent(expectedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oeLabel)
                            .addComponent(oeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(140, Short.MAX_VALUE))
        );

        mainPanel.addTab("Scores", scoresPanel);

        rocLabel.setMinimumSize(new java.awt.Dimension(482, 362));

        javax.swing.GroupLayout rocPanelLayout = new javax.swing.GroupLayout(rocPanel);
        rocPanel.setLayout(rocPanelLayout);
        rocPanelLayout.setHorizontalGroup(
            rocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rocLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rocPanelLayout.setVerticalGroup(
            rocPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rocLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainPanel.addTab("ROC", rocPanel);

        prcLabel.setMinimumSize(new java.awt.Dimension(482, 362));

        javax.swing.GroupLayout prcPanelLayout = new javax.swing.GroupLayout(prcPanel);
        prcPanel.setLayout(prcPanelLayout);
        prcPanelLayout.setHorizontalGroup(
            prcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prcLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        prcPanelLayout.setVerticalGroup(
            prcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prcLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainPanel.addTab("Precision-Recall", prcPanel);

        toolsMenu.setText("Tools");

        saveMenuItem.setText("Save Current Tab...");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(saveMenuItem);

        jMenuBar1.add(toolsMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel)
        );

        pack();
    }
}
