package edu.uw.cynetworkbma.internal.jobtracking;

import java.util.*;

public class JobInfoComparator implements Comparator<JobInfo> {

	@Override
	public int compare(JobInfo arg0, JobInfo arg1) {
		return arg0.getStartTime().compareTo(arg1.getStartTime());
	}
}
