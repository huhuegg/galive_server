package com.galive.logic.model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class MeetingLiveState {

	private boolean allMuted = true;
	
	private boolean useSpeaker = true;
	
	private boolean voiceMuted = true;
	
	private boolean videoClosed = true;

	public boolean isAllMuted() {
		return allMuted;
	}

	public void setAllMuted(boolean allMuted) {
		this.allMuted = allMuted;
	}

	public boolean isUseSpeaker() {
		return useSpeaker;
	}

	public void setUseSpeaker(boolean useSpeaker) {
		this.useSpeaker = useSpeaker;
	}

	public boolean isVoiceMuted() {
		return voiceMuted;
	}

	public void setVoiceMuted(boolean voiceMuted) {
		this.voiceMuted = voiceMuted;
	}

	public boolean isVideoClosed() {
		return videoClosed;
	}

	public void setVideoClosed(boolean videoClosed) {
		this.videoClosed = videoClosed;
	}
	
	
	
	
	
}
