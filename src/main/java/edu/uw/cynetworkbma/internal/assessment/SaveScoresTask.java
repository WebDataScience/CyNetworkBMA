package edu.uw.cynetworkbma.internal.assessment;

import java.util.List;

import org.cytoscape.model.*;
import org.cytoscape.work.*;

public class SaveScoresTask extends AbstractTask {
	
	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	private final List<AssessmentScores> scores;
	
	public SaveScoresTask(
			CyTableFactory tableFactory,
			CyTableManager tableManager,
			List<AssessmentScores> scores) {
		
		this.tableFactory = tableFactory;
		this.tableManager = tableManager;
		this.scores = scores;
	}
	
	@Tunable(description="Table Name")
	public String tableName;
	
	@Override
	public void run(final TaskMonitor taskMonitor) throws Exception {
		
		CyTable table = tableFactory.createTable(
				tableName, AssessmentScores.ATTR_THRESHOLD, Double.class, true, true);
		
		table.createColumn(AssessmentScores.ATTR_TP, Integer.class, false);
		table.createColumn(AssessmentScores.ATTR_FN, Integer.class, false);
		table.createColumn(AssessmentScores.ATTR_FP, Integer.class, false);
		table.createColumn(AssessmentScores.ATTR_TN, Integer.class, false);
		table.createColumn(AssessmentScores.ATTR_TPR, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_TNR, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_FPR, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_FDR, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_PPV, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_NPV, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_F1, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_MCC, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_ACC, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_EXPECTED, Double.class, false);
		table.createColumn(AssessmentScores.ATTR_OE, Double.class, false);
		
		for (AssessmentScores score : scores) {
			CyRow row = table.getRow(new Double(score.getThreshold()));
			row.set(AssessmentScores.ATTR_TP, new Integer(score.getTP()));
			row.set(AssessmentScores.ATTR_FN, new Integer(score.getFN()));
			row.set(AssessmentScores.ATTR_FP, new Integer(score.getFP()));
			row.set(AssessmentScores.ATTR_TN, new Integer(score.getTN()));
			row.set(AssessmentScores.ATTR_TPR, new Double(score.getTPR()));
			row.set(AssessmentScores.ATTR_TNR, new Double(score.getTNR()));
			row.set(AssessmentScores.ATTR_FPR, new Double(score.getFPR()));
			row.set(AssessmentScores.ATTR_FDR, new Double(score.getFDR()));
			row.set(AssessmentScores.ATTR_PPV, new Double(score.getPPV()));
			row.set(AssessmentScores.ATTR_NPV, new Double(score.getNPV()));
			row.set(AssessmentScores.ATTR_F1, new Double(score.getF1()));
			row.set(AssessmentScores.ATTR_MCC, new Double(score.getMCC()));
			row.set(AssessmentScores.ATTR_ACC, new Double(score.getACC()));
			row.set(AssessmentScores.ATTR_EXPECTED, new Double(score.getExpected()));
			row.set(AssessmentScores.ATTR_OE, new Double(score.getOE()));
		}
		
		tableManager.addTable(table);
	}
}
