package edu.uw.cynetworkbma.internal.jobtracking;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.*;

import edu.uw.cynetworkbma.internal.Constants;

public class JobTrackingMenuAction extends AbstractCyAction {
	
	private static final long serialVersionUID = -1709943761401870000L;
	
	private final CySwingApplication swingApplication;

	public JobTrackingMenuAction(
			CySwingApplication swingApplication) {
		
		super("Show Jobs...");
        setPreferredMenu(Constants.MENU + "." + Constants.APP_NAME);
        
        this.swingApplication = swingApplication;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new JobTrackingDialog(swingApplication).setVisible(true);
	}
}
