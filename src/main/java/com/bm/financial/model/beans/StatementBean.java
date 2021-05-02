package com.bm.financial.model.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatementBean implements Comparable<StatementBean>{

	private int id;
	private int accountId;
	private LocalDate dateField;
	private String amount;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public LocalDate getDateField() {
		return dateField;
	}

	public void setDateField(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		date = date.replace('.', '-');
		LocalDate dateField = LocalDate.parse(date, formatter);

		this.dateField = dateField;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public int compareTo(StatementBean obj) {
		if(obj != null && obj.dateField != null){
         
            return this.dateField.compareTo(obj.dateField);
   
        }else{
            return -1;
        }
	}
}
