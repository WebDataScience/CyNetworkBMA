package edu.uw.cynetworkbma.internal.inference;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="parameters")
@XmlAccessorType(XmlAccessType.NONE)
public class InferenceParameters {
	
	public static final String ALGORITHM_SCANBMA = "ScanBMA";
	public static final String ALGORITHM_IBMA = "iBMA";
	
	@XmlElement(name="timeSeries")
	private boolean timeSeries;
	
	@XmlElement(name="numberOfTimePoints")
	private int numberOfTimePoints;
	
	@XmlElement(name="algorithm")
	private String algorithm;
	
	@XmlElement(name="ScanBMA.OR")
	private int scanBmaOR;
	
	@XmlElement(name="ScanBMA.useg")
	private boolean scanBmaUseg;
	
	@XmlElement(name="ScanBMA.optimize")
	private boolean scanBmaOptimize;
	
	@XmlElement(name="ScanBMA.g0")
	private Integer scanBmaG0;
	
	@XmlElement(name="ScanBMA.iterlim")
	private int scanBmaIterlim;
	
	@XmlElement(name="ScanBMA.epsilon")
	private double scanBmaEpsilon;
	
	@XmlElement(name="ScanBMA.thresProbne0")
	private double scanBmaThresProbne0;
	
	@XmlElement(name="iBMA.OR")
	private int iterBmaOR;
	
	@XmlElement(name="iBMA.nbest")
	private int iterBmaNbest;
	
	@XmlElement(name="iBMA.maxNvar")
	private int iterBmaMaxNvar;
	
	@XmlElement(name="iBMA.thresProbne0")
	private double iterBmaThresProbne0;
	
	@XmlElement(name="iBMA.keepModels")
	private boolean iterBmaKeepModels;
	
	@XmlElement(name="iBMA.maxIter")
	private int iterBmaMaxIter;
	
	@XmlElement(name="nvar")
	private Integer nvar;
	
	@XmlElement(name="ordering")
	private String ordering;
	
	@XmlElement(name="constantPrior")
	private boolean constantPrior;
	
	@XmlElement(name="priorProbConst")
	private Double priorProb;
	
	private boolean tablePrior;
	
	private String[] regulatorNames;
	private String[] regulatedNames;
	private double[][] priorProbMatrix;
	
	@XmlElement(name="postProbThreshold")
	private double postProbThreshold;
	
	private String[] geneNames;
	private double[][] data;
	
	public boolean isTimeSeries() {
		return timeSeries;
	}
	
	public void setTimeSeries(boolean timeSeries) {
		this.timeSeries = timeSeries;
	}
	
	public int getNumberOfTimePoints() {
		return numberOfTimePoints;
	}
	
	public void setNumberOfTimePoints(int numberOfTimePoints) {
		this.numberOfTimePoints = numberOfTimePoints;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public int getScanBmaOR() {
		return scanBmaOR;
	}

	public void setScanBmaOR(int scanBmaOR) {
		this.scanBmaOR = scanBmaOR;
	}

	public boolean isScanBmaUseg() {
		return scanBmaUseg;
	}

	public void setScanBmaUseg(boolean scanBmaUseg) {
		this.scanBmaUseg = scanBmaUseg;
	}

	public boolean isScanBmaOptimize() {
		return scanBmaOptimize;
	}

	public void setScanBmaOptimize(boolean scanBmaOptimize) {
		this.scanBmaOptimize = scanBmaOptimize;
	}

	public Integer getScanBmaG0() {
		return scanBmaG0;
	}

	public void setScanBmaG0(Integer scanBmaG0) {
		this.scanBmaG0 = scanBmaG0;
	}

	public int getScanBmaIterlim() {
		return scanBmaIterlim;
	}

	public void setScanBmaIterlim(int scanBmaIterlim) {
		this.scanBmaIterlim = scanBmaIterlim;
	}

	public double getScanBmaEpsilon() {
		return scanBmaEpsilon;
	}

	public void setScanBmaEpsilon(double scanBmaEpsilon) {
		this.scanBmaEpsilon = scanBmaEpsilon;
	}

	public double getScanBmaThresProbne0() {
		return scanBmaThresProbne0;
	}

	public void setScanBmaThresProbne0(double scanBmaThresProbne0) {
		this.scanBmaThresProbne0 = scanBmaThresProbne0;
	}

	public int getIterBmaOR() {
		return iterBmaOR;
	}

	public void setIterBmaOR(int iterBmaOR) {
		this.iterBmaOR = iterBmaOR;
	}

	public int getIterBmaNbest() {
		return iterBmaNbest;
	}

	public void setIterBmaNbest(int iterBmaNbest) {
		this.iterBmaNbest = iterBmaNbest;
	}

	public int getIterBmaMaxNvar() {
		return iterBmaMaxNvar;
	}

	public void setIterBmaMaxNvar(int iterBmaMaxNvar) {
		this.iterBmaMaxNvar = iterBmaMaxNvar;
	}

	public double getIterBmaThresProbne0() {
		return iterBmaThresProbne0;
	}

	public void setIterBmaThresProbne0(double iterBmaThresProbne0) {
		this.iterBmaThresProbne0 = iterBmaThresProbne0;
	}

	public boolean isIterBmaKeepModels() {
		return iterBmaKeepModels;
	}

	public void setIterBmaKeepModels(boolean iterBmaKeepModels) {
		this.iterBmaKeepModels = iterBmaKeepModels;
	}

	public int getIterBmaMaxIter() {
		return iterBmaMaxIter;
	}

	public void setIterBmaMaxIter(int iterBmaMaxIter) {
		this.iterBmaMaxIter = iterBmaMaxIter;
	}

	public Integer getNvar() {
		return nvar;
	}

	public void setNvar(Integer nvar) {
		this.nvar = nvar;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	public boolean isConstantPrior() {
		return constantPrior;
	}

	public void setConstantPrior(boolean constantPrior) {
		this.constantPrior = constantPrior;
	}

	public Double getPriorProb() {
		return priorProb;
	}

	public void setPriorProb(Double priorProb) {
		this.priorProb = priorProb;
	}

	public boolean isTablePrior() {
		return tablePrior;
	}

	public void setTablePrior(boolean tablePrior) {
		this.tablePrior = tablePrior;
	}

	public String[] getRegulatorNames() {
		return regulatorNames;
	}

	public void setRegulatorNames(String[] regulatorNames) {
		this.regulatorNames = regulatorNames;
	}

	public String[] getRegulatedNames() {
		return regulatedNames;
	}

	public void setRegulatedNames(String[] regulatedNames) {
		this.regulatedNames = regulatedNames;
	}

	public double[][] getPriorProbMatrix() {
		return priorProbMatrix;
	}

	public void setPriorProbMatrix(double[][] priorProbMatrix) {
		this.priorProbMatrix = priorProbMatrix;
	}

	public double getPostProbThreshold() {
		return postProbThreshold;
	}

	public void setPostProbThreshold(double postProbThreshold) {
		this.postProbThreshold = postProbThreshold;
	}

	public String[] getGeneNames() {
		return geneNames;
	}

	public void setGeneNames(String[] geneNames) {
		this.geneNames = geneNames;
	}

	public double[][] getData() {
		return data;
	}

	public void setData(double[][] data) {
		this.data = data;
	}
	
	public static InferenceParameters getDefault() {
		try {
			JAXBContext context = JAXBContext.newInstance(InferenceParameters.class);
			Unmarshaller um = context.createUnmarshaller();
			return (InferenceParameters)um.unmarshal(
					InferenceParameters.class.getResourceAsStream("/inferenceParameters.xml"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return new InferenceParameters();
	}
}
