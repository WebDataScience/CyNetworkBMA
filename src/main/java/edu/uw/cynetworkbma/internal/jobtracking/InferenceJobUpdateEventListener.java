package edu.uw.cynetworkbma.internal.jobtracking;

import edu.uw.cynetworkbma.internal.inference.InferenceJob;

public interface InferenceJobUpdateEventListener {
	
	void jobUpdated(InferenceJob job, JobInfo jobInfo);
}