package edu.uw.cynetworkbma.internal.assessment;

import java.util.*;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.*;
import org.cytoscape.work.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;

import edu.uw.cynetworkbma.internal.ScriptReader;
import edu.uw.cynetworkbma.internal.inference.RserveInferenceJob;

public class RserveAssessmentTask extends AbstractTask {

	private static final String VAR_EDGE_LIST = "edgeList";
	private static final String VAR_REFERENCE_PAIRS = "referencePairs";
	private static final String VAR_SCORES = "scores";
	private static final String VAR_SCORES_100 = "scores.100";
	private static final String VAR_ROC = "roc";
	private static final String VAR_PRC = "prc";
	
	private final CySwingApplication swingApplication;
	private final TaskManager taskManager;
	private final CyTableFactory tableFactory;
	private final CyTableManager tableManager;
	
	private final RConnection connection;
	private final REXP networkDataFrame;
	private final REXP referenceNetworkDataFrame;
	
	public RserveAssessmentTask(
			CySwingApplication swingApplication,
			TaskManager taskManager,
			CyTableFactory tableFactory,
			CyTableManager tableManager,
			RConnection connection,
			REXP networkDataFrame,
			REXP referenceNetworkDataFrame) {
		
		this.swingApplication = swingApplication;
		this.taskManager = taskManager;
		this.tableFactory = tableFactory;
		this.tableManager = tableManager;
		this.connection = connection;
		this.networkDataFrame = networkDataFrame;
		this.referenceNetworkDataFrame = referenceNetworkDataFrame;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		taskMonitor.setTitle("Network assessment");
		taskMonitor.setStatusMessage("Assessing network");
    	
    	connection.assign(VAR_EDGE_LIST, networkDataFrame);
    	connection.assign(VAR_REFERENCE_PAIRS, referenceNetworkDataFrame);
    	
    	String script = new ScriptReader().readScript(
    			RserveInferenceJob.class.getResourceAsStream("/assessment.r"));
    	connection.voidEval(script);
    	
    	taskMonitor.setStatusMessage("Downloading results");
    	
    	List<AssessmentScores> scores = downloadScores(VAR_SCORES);
    	List<AssessmentScores> scores100 = downloadScores(VAR_SCORES_100);
    	
    	byte[] roc = connection.eval(VAR_ROC).asBytes();
    	byte[] prc = connection.eval(VAR_PRC).asBytes();
    	
    	connection.close();
		
		new AssessmentResultsDialog(
				swingApplication,
				taskManager,
				tableFactory,
				tableManager,
				scores,
				scores100,
				roc,
				prc).setVisible(true);
	}
	
	private List<AssessmentScores> downloadScores(String variableName) throws Exception {
		
		double[] threshold = connection.eval("as.numeric(rownames(" + variableName + "))").asDoubles();
    	int[] tp = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_TP + "\"").asIntegers();
    	int[] fn = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_FN + "\"").asIntegers();
    	int[] fp = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_FP + "\"").asIntegers();
    	int[] tn = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_TN + "\"").asIntegers();
    	double[] tpr = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_TPR + "\"").asDoubles();
    	double[] tnr = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_TNR + "\"").asDoubles();
    	double[] fpr = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_FPR + "\"").asDoubles();
    	double[] fdr = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_FDR + "\"").asDoubles();
    	double[] ppv = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_PPV + "\"").asDoubles();
    	double[] npv = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_NPV + "\"").asDoubles();
    	double[] f1 = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_F1 + "\"").asDoubles();
    	double[] mcc = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_MCC + "\"").asDoubles();
    	double[] acc = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_ACC + "\"").asDoubles();
    	double[] expected = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_EXPECTED + "\"").asDoubles();
    	double[] oe = connection.eval(variableName + "$\"" + AssessmentScores.ATTR_OE + "\"").asDoubles();
    	
		ArrayList<AssessmentScores> scores = new ArrayList<AssessmentScores>();
		for(int i = 0; i < threshold.length; i++) {
    		AssessmentScores as = new AssessmentScores();
    		as.setThreshold(threshold[i]);
    		as.setTP(tp[i]);
    		as.setFN(fn[i]);
    		as.setFP(fp[i]);
    		as.setTN(tn[i]);
    		as.setTPR(tpr[i]);
    		as.setTNR(tnr[i]);
    		as.setFPR(fpr[i]);
    		as.setFDR(fdr[i]);
    		as.setPPV(ppv[i]);
    		as.setNPV(npv[i]);
    		as.setF1(f1[i]);
    		as.setMCC(mcc[i]);
    		as.setACC(acc[i]);
    		as.setExpected(expected[i]);
    		as.setOE(oe[i]);
    		scores.add(as);
    	}
		
		return scores;
	}
}
