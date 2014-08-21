package edu.uw.cynetworkbma.internal.inference;

public interface InferenceJob {

	void execute();
	
	String getNetworkName();
}
