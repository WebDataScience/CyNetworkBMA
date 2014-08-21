package edu.uw.cynetworkbma.internal.assessment;

import java.util.*;

import org.cytoscape.model.*;
import org.rosuda.REngine.*;

public class NetworkToDataFrameConverter {

	private final String SOURCE_COL_NAME = "Regulator";
	private final String TARGET_COL_NAME = "Target";
	private final String PROB_COL_NAME = "PostProb";
	
	public REXP createDataFrame(CyNetwork network, String postProbColumn) throws Exception {
		List<CyEdge> edges = network.getEdgeList();
		CyTable edgeTable = network.getDefaultEdgeTable();
		
		String[] sourceNodes = new String[edges.size()];
		String[] targetNodes = new String[edges.size()];
		double[] edgeProb = null;
		if (postProbColumn != null) {
			edgeProb = new double[edges.size()];
		}
		
		for(int i = 0; i < edges.size(); i++) {
			CyEdge edge = edges.get(i);
			sourceNodes[i] = network.getRow(edge.getSource()).get(CyNetwork.NAME, String.class);
			targetNodes[i] = network.getRow(edge.getTarget()).get(CyNetwork.NAME, String.class);
			if (postProbColumn != null) {
				Double postProb = edgeTable.getRow(edge.getSUID()).get(postProbColumn, Double.class);
				if (postProb == null) {
					throw new Exception(
							"Missing edge probability (" + sourceNodes[i] + " \u2192 " + targetNodes[i] + ").");
				}
				
				edgeProb[i] = postProb;
			}
		}
		
		ArrayList<Object> columns = new ArrayList<Object>();
		ArrayList<String> columnNames = new ArrayList<String>();
		columns.add(new REXPString(sourceNodes));
		columnNames.add(SOURCE_COL_NAME);
		columns.add(new REXPString(targetNodes));
		columnNames.add(TARGET_COL_NAME);
		if (postProbColumn != null) {
			columns.add(new REXPDouble(edgeProb));
			columnNames.add(PROB_COL_NAME);
		}
		
		return REXP.createDataFrame(new RList(columns, columnNames));
	}
	
	public REXP createDataFrame(CyNetwork network) throws Exception {
		return createDataFrame(network, null);
	}
}
