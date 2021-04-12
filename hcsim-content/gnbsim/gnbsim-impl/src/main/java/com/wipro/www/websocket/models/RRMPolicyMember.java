package com.wipro.www.websocket.models;

public class RRMPolicyMember {
	private String pLMNId;
	private String sNSSAI;

	public RRMPolicyMember() {

	}

	public RRMPolicyMember(String pLMNId, String sNSSAI) {
		super();
		this.pLMNId = pLMNId;
		this.sNSSAI = sNSSAI;
	}

	public String getpLMNId() {
		return pLMNId;
	}

	public void setpLMNId(String pLMNId) {
		this.pLMNId = pLMNId;
	}

	public String getsNSSAI() {
		return sNSSAI;
	}

	public void setsNSSAI(String sNSSAI) {
		this.sNSSAI = sNSSAI;
	}

}

