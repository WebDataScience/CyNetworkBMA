package edu.uw.cynetworkbma.internal.assessment;

public class AssessmentScores {

	public static final String ATTR_THRESHOLD = "threshold";
	public static final String ATTR_TP = "TP";
	public static final String ATTR_FN = "FN";
	public static final String ATTR_FP = "FP";
	public static final String ATTR_TN = "TN";
	public static final String ATTR_TPR = "TPR";
	public static final String ATTR_TNR = "TNR";
	public static final String ATTR_FPR = "FPR";
	public static final String ATTR_FDR = "FDR";
	public static final String ATTR_PPV = "PPV";
	public static final String ATTR_NPV = "NPV";
	public static final String ATTR_F1 = "F1";
	public static final String ATTR_MCC = "MCC";
	public static final String ATTR_ACC = "ACC";
	public static final String ATTR_EXPECTED = "expected";
	public static final String ATTR_OE = "O/E";
	
	private double threshold;
	private int TP;
	private int FN;
	private int FP;
	private int TN;
	private double TPR;
	private double TNR;
	private double FPR;
	private double FDR;
	private double PPV;
	private double NPV;
	private double F1;
	private double MCC;
	private double ACC;
	private double expected;
	private double OE;
	
	public double getThreshold() {
		return threshold;
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public int getTP() {
		return TP;
	}
	
	public void setTP(int tP) {
		TP = tP;
	}
	
	public int getFN() {
		return FN;
	}
	public void setFN(int fN) {
		FN = fN;
	}
	
	public int getFP() {
		return FP;
	}
	
	public void setFP(int fP) {
		FP = fP;
	}
	
	public int getTN() {
		return TN;
	}
	
	public void setTN(int tN) {
		TN = tN;
	}
	
	public double getTPR() {
		return TPR;
	}
	
	public void setTPR(double tPR) {
		TPR = tPR;
	}
	
	public double getTNR() {
		return TNR;
	}
	
	public void setTNR(double tNR) {
		TNR = tNR;
	}
	
	public double getFPR() {
		return FPR;
	}
	
	public void setFPR(double fPR) {
		FPR = fPR;
	}
	
	public double getFDR() {
		return FDR;
	}
	
	public void setFDR(double fDR) {
		FDR = fDR;
	}
	
	public double getPPV() {
		return PPV;
	}
	
	public void setPPV(double pPV) {
		PPV = pPV;
	}
	
	public double getNPV() {
		return NPV;
	}
	
	public void setNPV(double nPV) {
		NPV = nPV;
	}
	
	public double getF1() {
		return F1;
	}
	
	public void setF1(double f1) {
		F1 = f1;
	}
	
	public double getMCC() {
		return MCC;
	}
	
	public void setMCC(double mCC) {
		MCC = mCC;
	}
	
	public double getACC() {
		return ACC;
	}
	
	public void setACC(double aCC) {
		ACC = aCC;
	}
	
	public double getExpected() {
		return expected;
	}
	
	public void setExpected(double expected) {
		this.expected = expected;
	}
	
	public double getOE() {
		return OE;
	}
	
	public void setOE(double oE) {
		OE = oE;
	}
}
