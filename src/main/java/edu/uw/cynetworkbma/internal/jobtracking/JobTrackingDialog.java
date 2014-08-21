package edu.uw.cynetworkbma.internal.jobtracking;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JDialog;

import org.cytoscape.application.swing.CySwingApplication;

import edu.uw.cynetworkbma.internal.ConnectionParameters;
import edu.uw.cynetworkbma.internal.inference.InferenceJob;

public class JobTrackingDialog extends JDialog implements InferenceJobUpdateEventListener {

	private static final long serialVersionUID = -3228563206333431998L;
	
	private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jobTable;
    private javax.swing.JButton okButton;
    
    public JobTrackingDialog(CySwingApplication swingApplication) {
    	
    	super(swingApplication.getJFrame(), false);
    	initComponents();
    	setLocationRelativeTo(swingApplication.getJFrame());
    	refreshJobList();
    	InferenceJobTracker.getInstance().addUpdateEventListener(this);
    }
    
    public void jobUpdated(InferenceJob job, JobInfo jobInfo) {
    	refreshJobList();
    }
    
    private void refreshJobList() {
    	
    	TreeSet<JobInfo> jobs = new TreeSet<JobInfo>(new JobInfoComparator());
    	jobs.addAll(InferenceJobTracker.getInstance().getJobInfo());
    	Object[][] jobsToShow = new Object[jobs.size()][];
    	int i = 0;
    	for (JobInfo ji : jobs) {
    		jobsToShow[i++] = formatJobInfo(ji); 
    	}
    	
    	jobTable.setModel(new javax.swing.table.DefaultTableModel(
                jobsToShow,
                new String [] {
                    "Name", "Status", "Started", "Finished"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
    }
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	InferenceJobTracker.getInstance().removeUpdateEventListener(this);
        setVisible(false);
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
    	InferenceJobTracker.getInstance().removeUpdateEventListener(this);
    }
    
    private Object[] formatJobInfo(JobInfo ji) {
    	Object[] jiArr = new Object[4];
    	jiArr[0] = ji.getNetworkName();
    	jiArr[1] = ji.getStatus();
    	SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    	jiArr[2] = format.format(ji.getStartTime());
    	if (ji.getFinishTime() != null) {
    		jiArr[3] = format.format(ji.getFinishTime());
    	} else {
    		jiArr[3] = "";
    	}
    	
    	return jiArr;
    }
    
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jobTable = new javax.swing.JTable();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CyNetworkBMA Jobs");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setViewportView(jobTable);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
}
