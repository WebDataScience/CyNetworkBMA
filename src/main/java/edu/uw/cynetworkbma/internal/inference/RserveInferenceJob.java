package edu.uw.cynetworkbma.internal.inference;

import java.util.*;

import javax.swing.JOptionPane;

import org.cytoscape.work.*;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

import edu.uw.cynetworkbma.internal.*;
import edu.uw.cynetworkbma.internal.jobtracking.InferenceJobTracker;

public class RserveInferenceJob extends Thread implements InferenceJob {

	private static final String VAR_DATA = "data";
	private static final String VAR_GENE_NAMES = "genes";
	private static final String VAR_IS_TIME_SERIES = "isTimeSeries";
	private static final String VAR_NO_OF_TIME_POINTS = "nTimePoints";
	private static final String VAR_ALGORITHM = "algorithm";
	private static final String VAR_OR = "OR";
	private static final String VAR_THRES_PROBNE0 = "thresProbne0";
	private static final String VAR_USEG = "useg";
	private static final String VAR_OPTIMIZE = "optimize";
	private static final String VAR_G0 = "g0";
	private static final String VAR_ITERLIM = "iterlim";
	private static final String VAR_EPSILON = "epsilon";
	private static final String VAR_NBEST = "nbest";
	private static final String VAR_MAX_NVAR = "maxNvar";
	private static final String VAR_KEEP_MODELS = "keepModels";
	private static final String VAR_MAX_ITER = "maxIter";
	private static final String VAR_NVAR = "nvar";
	private static final String VAR_ORDERING = "ordering";
	private static final String VAR_PRIOR_PROB_CONST = "prior.prob.const";
	private static final String VAR_PRIOR_PROB_MATRIX = "prior.prob.matrix";
	private static final String VAR_PRIOR_PROB_REGULATOR_LABELS = "prior.prob.regulator.labels";
	private static final String VAR_PRIOR_PROB_REGULATED_LABELS = "prior.prob.regulated.labels";
	private static final String VAR_POSTERIOR_PROB_THRESHOLD = "postProbThreshold";
	private static final String VAR_RESULT = "out";
	
	private final String networkName;
	private final NetworkServiceReferences sr;
	private final InferenceParameters ip;
	private final ConnectionParameters cp;
	private final RConnection connection;
	private final TaskManager taskManager;
	
	public RserveInferenceJob(
			String networkName,
			NetworkServiceReferences references,
			ConnectionParameters connectionParameters,
			InferenceParameters parameters,
			TaskManager taskManager) throws Exception {
		
		this.networkName = networkName;
		this.sr = references;
		this.ip = parameters;
		this.cp = connectionParameters;
		this.taskManager = taskManager;
		
		connection = new RConnection(
				connectionParameters.getHost(), connectionParameters.getPort());
		
		if (connectionParameters.isLoginRequired()) {
			connection.login(connectionParameters.getUsername(), connectionParameters.getPassword());
		}
	}
	
	public void execute() {
		start();
	}
	
	public void run() {
		try {
			
			InferenceJobTracker.getInstance().registerJob(this);
			
	    	assignVariables();
	    	
	    	String script = new ScriptReader().readScript(
	    			RserveInferenceJob.class.getResourceAsStream("/inference.r"));
	    	REXP r = connection.parseAndEval(script);
	    	if (r.inherits("try-error")) {
	    		throw new Exception(r.asString());
	    	}
	    	
	    	String[] fromNodes = connection.eval(VAR_RESULT + "[,1]").asStrings();
	    	String[] toNodes = connection.eval(VAR_RESULT + "[,2]").asStrings();
	    	double[] probabilities = connection.eval(VAR_RESULT + "[,3]").asDoubles();
	    	
	    	connection.close();
	    	
	    	List<Edge> edges = new ArrayList<Edge>(fromNodes.length);
	    	for (int i = 0; i < fromNodes.length; i++) {
	    		Edge edge = new Edge();
	    		edge.setSource(fromNodes[i]);
	    		edge.setTarget(toNodes[i]);
	    		edge.setProbability(probabilities[i]);
	    		edges.add(edge);
	    	}
	    	
	    	NetworkBuilder networkBuilder = new NetworkBuilder(sr, taskManager);
	    	networkBuilder.buildNetwork(edges, networkName);
	    	
	    	InferenceJobTracker.getInstance().notifyJobCompletion(this);
	    	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null, "Error: " + e.getMessage(), Constants.APP_NAME, JOptionPane.ERROR_MESSAGE);
			InferenceJobTracker.getInstance().notifyJobFailure(this);
		}
	}
	
	public String getNetworkName() {
		return networkName;
	}
	
	private void assignVariables() throws Exception {
		
		connection.assign(VAR_DATA, REXP.createDoubleMatrix(ip.getData()));
		connection.assign(VAR_GENE_NAMES, new REXPString(ip.getGeneNames()));
		connection.assign(VAR_IS_TIME_SERIES, new REXPLogical(ip.isTimeSeries()));
		
		if(ip.isTimeSeries()) {
			connection.assign(VAR_NO_OF_TIME_POINTS, new REXPInteger(ip.getNumberOfTimePoints()));
		}
		
		connection.assign(VAR_ALGORITHM, new REXPString(ip.getAlgorithm()));
		if (InferenceParameters.ALGORITHM_SCANBMA.equals(ip.getAlgorithm())) {
			connection.assign(VAR_OR, new REXPInteger(ip.getScanBmaOR()));
			connection.assign(VAR_USEG, new REXPLogical(ip.isScanBmaUseg()));
			connection.assign(VAR_THRES_PROBNE0, new REXPDouble(ip.getScanBmaThresProbne0()));
			connection.assign(VAR_OPTIMIZE, new REXPLogical(ip.isScanBmaOptimize()));
			connection.assign(VAR_G0, ip.getScanBmaG0() != null? new REXPInteger(ip.getScanBmaG0()) : new REXPNull());
			connection.assign(VAR_ITERLIM, new REXPInteger(ip.getScanBmaIterlim()));
			connection.assign(VAR_EPSILON, new REXPDouble(ip.getScanBmaEpsilon()));
		} else if (InferenceParameters.ALGORITHM_IBMA.equals(ip.getAlgorithm())) {
			connection.assign(VAR_OR, new REXPInteger(ip.getIterBmaOR()));
			connection.assign(VAR_NBEST, new REXPInteger(ip.getIterBmaNbest()));
			connection.assign(VAR_MAX_NVAR, new REXPInteger(ip.getIterBmaMaxNvar()));
			connection.assign(VAR_THRES_PROBNE0, new REXPDouble(ip.getIterBmaThresProbne0()));
			connection.assign(VAR_KEEP_MODELS, new REXPLogical(ip.isIterBmaKeepModels()));
			connection.assign(VAR_MAX_ITER, new REXPInteger(ip.getIterBmaMaxIter()));
		}
		
		connection.assign(VAR_NVAR, ip.getNvar() != null? new REXPInteger(ip.getNvar()) : new REXPNull());
		connection.assign(VAR_ORDERING, new REXPString(ip.getOrdering()));
		connection.assign(VAR_PRIOR_PROB_CONST, ip.isConstantPrior()? new REXPDouble(ip.getPriorProb()) : new REXPNull());
		if (ip.isTablePrior()) {
			connection.assign(VAR_PRIOR_PROB_MATRIX, REXP.createDoubleMatrix(ip.getPriorProbMatrix()));
			connection.assign(VAR_PRIOR_PROB_REGULATOR_LABELS, new REXPString(ip.getRegulatorNames()));
			connection.assign(VAR_PRIOR_PROB_REGULATED_LABELS, new REXPString(ip.getRegulatedNames()));
		} else {
			connection.assign(VAR_PRIOR_PROB_MATRIX, new REXPNull());
		}
		
		connection.assign(VAR_POSTERIOR_PROB_THRESHOLD, new REXPDouble(ip.getPostProbThreshold()));
	}
}
