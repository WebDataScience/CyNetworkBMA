package edu.uw.cynetworkbma.internal.assessment;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.*;
import org.cytoscape.model.*;
import org.cytoscape.work.TaskManager;

import edu.uw.cynetworkbma.internal.Constants;

public class AssessmentMenuAction extends AbstractCyAction {

	private static final long serialVersionUID = 7565919851019256373L;

	private final CySwingApplication swingApplication;
	private final TaskManager taskManager;
	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	private final CyNetworkManager networkManager;
	
	public AssessmentMenuAction(
			CySwingApplication swingApplication,
			TaskManager taskManager,
			CyTableFactory tableFactory,
			CyTableManager tableManager,
			CyNetworkManager networkManager) {
		
		super("Assess Network...");
        setPreferredMenu(Constants.MENU + "." + Constants.APP_NAME);
        
        this.swingApplication = swingApplication;
        this.taskManager = taskManager;
        this.tableFactory = tableFactory;
        this.tableManager = tableManager;
        this.networkManager = networkManager;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new AssessmentMainDialog(
				swingApplication,
				taskManager,
				tableFactory,
				tableManager,
				networkManager).setVisible(true);
	}
}
