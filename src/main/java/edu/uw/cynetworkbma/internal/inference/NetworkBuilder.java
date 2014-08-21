package edu.uw.cynetworkbma.internal.inference;

import java.util.*;

import javax.swing.JOptionPane;

import org.cytoscape.model.*;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskManager;

import edu.uw.cynetworkbma.internal.*;

public class NetworkBuilder {

	private final NetworkServiceReferences sr;
	private final TaskManager taskManager;
	
	public NetworkBuilder(NetworkServiceReferences references, TaskManager taskManager) {
		
		this.sr = references;
		this.taskManager = taskManager;
	}
	
	public void buildNetwork(List<Edge> edgeList, String name) {
		
    	CyNetwork network = sr.networkFactory.createNetwork();
    	network.getRow(network).set(CyNetwork.NAME, sr.networkNaming.getSuggestedNetworkTitle(name));
    	
    	CyTable nodeTable = network.getDefaultNodeTable();
    	nodeTable.createColumn(Constants.INDEGREE_COLUMN_NAME, Integer.class, false);
    	nodeTable.createColumn(Constants.OUTDEGREE_COLUMN_NAME, Integer.class, false);
    	
    	CyTable edgeTable = network.getDefaultEdgeTable();
    	edgeTable.createColumn(Constants.POST_PROB_COLUMN_NAME, Double.class, false);
    	
    	HashMap<String, NodeInfo> nodeCache = new HashMap<String, NodeInfo>();
    	
    	for (Edge edge : edgeList) {
        	NodeInfo fromNode;
        	if (nodeCache.containsKey(edge.getSource())) {
        		fromNode = nodeCache.get(edge.getSource());
        	} else {
        		fromNode = new NodeInfo();
        		fromNode.node = network.addNode();
        		network.getRow(fromNode.node).set(CyNetwork.NAME, edge.getSource());
        		nodeCache.put(edge.getSource(), fromNode);
        	}
        	
        	NodeInfo toNode;
        	if (nodeCache.containsKey(edge.getTarget())) {
        		toNode = nodeCache.get(edge.getTarget());
        	} else {
        		toNode = new NodeInfo();
        		toNode.node = network.addNode();
        		network.getRow(toNode.node).set(CyNetwork.NAME, edge.getTarget());
        		nodeCache.put(edge.getTarget(), toNode);
        	}

        	CyEdge cyEdge = network.addEdge(fromNode.node, toNode.node, true);
        	network.getRow(cyEdge).set(CyEdge.INTERACTION, Constants.INTERACTION_TYPE);
        	network.getRow(cyEdge).set(CyNetwork.NAME, edge.getSource()
        			+ " (" + Constants.INTERACTION_TYPE + ") " + edge.getTarget());
        	network.getRow(cyEdge).set(Constants.POST_PROB_COLUMN_NAME, edge.getProbability());
        	fromNode.outdegree++;
        	toNode.indegree++;
    	}
    	
    	for (CyRow row : nodeTable.getAllRows()) {
    		String nodeName = row.get(CyNetwork.NAME, String.class);
    		NodeInfo info = nodeCache.get(nodeName);
    		row.set(Constants.INDEGREE_COLUMN_NAME, info.indegree);
    		row.set(Constants.OUTDEGREE_COLUMN_NAME, info.outdegree);
    	}
    	
    	CyNetworkView networkView = sr.networkViewFactory.createNetworkView(network);
    	
    	JOptionPane.showMessageDialog(
    			null,
    			"Network inference job completed. Consider using a visual style that\n"
    				+ "shows directionality of edges (gene networks are directed graphs).",
    			Constants.APP_NAME,
    			JOptionPane.INFORMATION_MESSAGE);
    	
    	sr.networkManager.addNetwork(network);
    	sr.networkViewManager.addNetworkView(networkView);
    	
    	CyLayoutAlgorithm layout = sr.layoutAlgorithmManager.getDefaultLayout();
    	Object layoutContext = layout.getDefaultLayoutContext();
    	taskManager.execute(layout.createTaskIterator(
    			networkView, layoutContext, CyLayoutAlgorithm.ALL_NODE_VIEWS, ""));
	}
	
	private class NodeInfo {
		public CyNode node;
		public int indegree;
		public int outdegree;
	}
}
