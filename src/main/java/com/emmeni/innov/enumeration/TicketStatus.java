package com.emmeni.innov.enumeration;

public enum TicketStatus {
	
	IN_PROGRESS,
	FINISHED,
	CANCELLED;
	
	public boolean isInProgress() {
		return this == IN_PROGRESS;
	}
	
	public boolean isFinished() {
		return this == FINISHED;
	}
	
	public boolean isCancelled() {
		return this == CANCELLED;
	}
}
