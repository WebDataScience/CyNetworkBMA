package edu.uw.cynetworkbma.internal.jobtracking;

import java.util.Date;

public class JobInfo {

	public static enum JobStatus {

		RUNNING("Running"),
		ERROR("Error"),
		COMPLETED("Completed");
		
		private final String status;
		
		private JobStatus(String status) {
			this.status = status;
		}
		
		public String toString() {
			return status;
		}
	};
	
	private final String networkName;
	private final JobStatus status;
	private final Date startTime;
	private final Date finishTime;
	
	public JobInfo(
			String networkName,
			JobStatus status,
			Date startTime,
			Date finishTime) {
		
		this.networkName = networkName;
		this.status = status;
		this.startTime = startTime;
		this.finishTime = finishTime;
	}
	
	public String getNetworkName() {
		return networkName;
	}
	
	public JobStatus getStatus() {
		return status;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getFinishTime() {
		return finishTime;
	}
}
