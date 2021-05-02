package com.bm.financial.model.beans;

import java.time.LocalDate;

public class SearchParametersBean {

	private String accountNumber;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Double fromAmount;
	private Double toAmount;

	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public Double getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(Double fromAmount) {
		this.fromAmount = fromAmount;
	}
	public Double getToAmount() {
		return toAmount;
	}
	public void setToAmount(Double toAmount) {
		this.toAmount = toAmount;
	}
}
