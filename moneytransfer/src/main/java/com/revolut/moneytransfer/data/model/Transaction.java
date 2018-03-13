package com.revolut.moneytransfer.data.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Transaction {	
	
	private LocalDateTime time;
    private UUID sourceAccountId;
    private UUID destAccountId;
    private double sourceAmount;
    private double targetAmount;
    private boolean success;
    private String reference;
    
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public UUID getSourceAccountId() {
		return sourceAccountId;
	}
	public void setSourceAccountId(UUID sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}
	public UUID getDestAccountId() {
		return destAccountId;
	}
	public void setDestAccountId(UUID destAccountId) {
		this.destAccountId = destAccountId;
	}
	public double getSourceAmount() {
		return sourceAmount;
	}
	public void setSourceAmount(double sourceAmount) {
		this.sourceAmount = sourceAmount;
	}
	public double getTargetAmount() {
		return targetAmount;
	}
	public void setTargetAmount(double targetAmount) {
		this.targetAmount = targetAmount;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	

}
