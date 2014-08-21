package edu.uw.cynetworkbma.internal.jobtracking;

import java.util.*;

import edu.uw.cynetworkbma.internal.inference.InferenceJob;

public class InferenceJobTracker {
	
	private static final InferenceJobTracker instance;
	
	private final Map<InferenceJob, JobInfo> jobs = new HashMap<InferenceJob, JobInfo>();
	private final HashSet<InferenceJobUpdateEventListener> listeners = new HashSet<InferenceJobUpdateEventListener>();
	
	private int jobCount;
	
	static {
		instance = new InferenceJobTracker();
	}

	private InferenceJobTracker() {}
	
	public static InferenceJobTracker getInstance() {
		return instance;
	}
	
	public void registerJob(InferenceJob job) {
		JobInfo ji = new JobInfo(
				job.getNetworkName(),
				JobInfo.JobStatus.RUNNING,
				new Date(),
				null);
		
		synchronized(jobs) {
			jobs.put(job, ji);
			for (InferenceJobUpdateEventListener listener : listeners) {
				listener.jobUpdated(job, ji);
			}
			
			jobCount++;
		}
	}
	
	public void notifyJobFailure(InferenceJob job) {
		JobInfo oldJI = jobs.get(job);
		JobInfo newJI = new JobInfo(
				oldJI.getNetworkName(),
				JobInfo.JobStatus.ERROR,
				oldJI.getStartTime(),
				new Date());
		
		synchronized(jobs) {
			jobs.put(job,  newJI);
			for (InferenceJobUpdateEventListener listener : listeners) {
				listener.jobUpdated(job, newJI);
			}
		}
	}
	
	public void notifyJobCompletion(InferenceJob job) {
		JobInfo oldJI = jobs.get(job);
		JobInfo newJI = new JobInfo(
				oldJI.getNetworkName(),
				JobInfo.JobStatus.COMPLETED,
				oldJI.getStartTime(),
				new Date());
		
		synchronized(jobs) {
			jobs.put(job,  newJI);
			for (InferenceJobUpdateEventListener listener : listeners) {
				listener.jobUpdated(job, newJI);
			}
		}
	}
	
	public Collection<JobInfo> getJobInfo() {
		return jobs.values();
	}
	
	public void addUpdateEventListener(InferenceJobUpdateEventListener listener) {
		synchronized (jobs) {
			listeners.add(listener);
		}
	}
	
	public void removeUpdateEventListener(InferenceJobUpdateEventListener listener) {
		synchronized (jobs) {
			listeners.remove(listener);
		}
	}
	
	public int getTotalJobCount() {
		return jobCount;
	}
}
