package edu.uw.cynetworkbma.internal;

import java.util.Properties;

import org.cytoscape.application.swing.*;
import org.cytoscape.model.*;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.*;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

import edu.uw.cynetworkbma.internal.assessment.AssessmentMenuAction;
import edu.uw.cynetworkbma.internal.inference.InferenceMenuAction;
import edu.uw.cynetworkbma.internal.jobtracking.JobTrackingMenuAction;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		
		DialogTaskManager taskManager = getService(context, DialogTaskManager.class);
		CySwingApplication swingApplication = getService(context, CySwingApplication.class);
		CyTableFactory tableFactory = getService(context, CyTableFactory.class);
		CyTableManager tableManager = getService(context, CyTableManager.class);
		
		NetworkServiceReferences sr = new NetworkServiceReferences();
		sr.networkManager = getService(context, CyNetworkManager.class);
		sr.networkFactory = getService(context, CyNetworkFactory.class);
		sr.networkViewManager = getService(context, CyNetworkViewManager.class);
		sr.networkViewFactory = getService(context, CyNetworkViewFactory.class);
		sr.networkNaming = getService(context, CyNetworkNaming.class);
		sr.layoutAlgorithmManager = getService(context, CyLayoutAlgorithmManager.class);

		InferenceMenuAction inferenceAction = new InferenceMenuAction(
				swingApplication,
				taskManager,
				tableManager,
				sr);
		
		registerService(context, inferenceAction, CyAction.class, new Properties());
		
		AssessmentMenuAction assessmentAction = new AssessmentMenuAction(
				swingApplication,
				taskManager,
				tableFactory,
				tableManager,
				sr.networkManager);
		
		registerService(context, assessmentAction, CyAction.class, new Properties());
		
		JobTrackingMenuAction jobTrackingAction = new JobTrackingMenuAction(
				swingApplication);
		
		registerService(context, jobTrackingAction, CyAction.class, new Properties());
	}
}
