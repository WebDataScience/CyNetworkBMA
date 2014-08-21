package edu.uw.cynetworkbma.internal.inference;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.*;
import org.cytoscape.model.*;
import org.cytoscape.work.swing.DialogTaskManager;

import edu.uw.cynetworkbma.internal.*;

public class InferenceMenuAction extends AbstractCyAction {
	
	private static final long serialVersionUID = -3753865041840170976L;
	
	private final CySwingApplication swingApplication;
	private final DialogTaskManager taskManager;
	private final CyTableManager tableManager;
	private final NetworkServiceReferences serviceReferences;

    public InferenceMenuAction(
    		CySwingApplication swingApplication,
    		DialogTaskManager taskManager,
    		CyTableManager tableManager,
			NetworkServiceReferences serviceReferences) {
    	
        super("Infer Network...");
        setPreferredMenu(Constants.MENU + "." + Constants.APP_NAME);
        
        this.swingApplication = swingApplication;
        this.taskManager = taskManager;
        this.tableManager = tableManager;
		this.serviceReferences = serviceReferences;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		new InferenceMainDialog(
				swingApplication,
				taskManager,
				tableManager,
				serviceReferences).setVisible(true);
	}
}
