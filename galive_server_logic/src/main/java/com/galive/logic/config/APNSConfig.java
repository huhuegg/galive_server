package com.galive.logic.config;


public class APNSConfig {
	
	private String certNameDevelopment;
	
	private String certPasswordDevelopment;
	
	private String certNameDistruction;
	
	private String certPasswordDistruction;
	
	private String pushSound;
	
	private int pushBadge;

	public String getCertNameDevelopment() {
		return certNameDevelopment;
	}

	public void setCertNameDevelopment(String certNameDevelopment) {
		this.certNameDevelopment = certNameDevelopment;
	}

	public String getCertPasswordDevelopment() {
		return certPasswordDevelopment;
	}

	public void setCertPasswordDevelopment(String certPasswordDevelopment) {
		this.certPasswordDevelopment = certPasswordDevelopment;
	}

	public String getCertNameDistruction() {
		return certNameDistruction;
	}

	public void setCertNameDistruction(String certNameDistruction) {
		this.certNameDistruction = certNameDistruction;
	}

	public String getCertPasswordDistruction() {
		return certPasswordDistruction;
	}

	public void setCertPasswordDistruction(String certPasswordDistruction) {
		this.certPasswordDistruction = certPasswordDistruction;
	}

	public String getPushSound() {
		return pushSound;
	}

	public void setPushSound(String pushSound) {
		this.pushSound = pushSound;
	}

	public int getPushBadge() {
		return pushBadge;
	}

	public void setPushBadge(int pushBadge) {
		this.pushBadge = pushBadge;
	}
}
