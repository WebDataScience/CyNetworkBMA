package edu.uw.cynetworkbma.internal.assessment;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.*;
import org.cytoscape.work.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;

import edu.uw.cynetworkbma.internal.ConnectionParameters;

public class RserveAssessmentTaskFactory extends AbstractTaskFactory {

	private final CySwingApplication swingApplication;
	private final TaskManager taskManager;
	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	
	private final RConnection connection;
	private final REXP networkDataFrame;
	private final REXP referenceNetworkDataFrame;
	
	public RserveAssessmentTaskFactory (
			CySwingApplication swingApplication,
			TaskManager taskManager,
			CyTableFactory tableFactory,
			CyTableManager tableManager,
			ConnectionParameters connectionParameters,
			CyNetwork network,
			String edgeProbColumn,
			CyNetwork referenceNetwork) throws Exception {
		
		this.swingApplication = swingApplication;
		this.taskManager = taskManager;
		this.tableFactory = tableFactory;
		this.tableManager = tableManager;
		
		NetworkToDataFrameConverter converter = new NetworkToDataFrameConverter();
		networkDataFrame = converter.createDataFrame(network, edgeProbColumn);
		referenceNetworkDataFrame = converter.createDataFrame(referenceNetwork);
		
		connection = new RConnection(connectionParameters.getHost(), connectionParameters.getPort());
		if (connectionParameters.isLoginRequired()) {
			connection.login(connectionParameters.getUsername(), connectionParameters.getPassword());
		}
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(
				new RserveAssessmentTask(
						swingApplication,
						taskManager,
						tableFactory,
						tableManager,
						connection,
						networkDataFrame,
						referenceNetworkDataFrame));
	}
}
