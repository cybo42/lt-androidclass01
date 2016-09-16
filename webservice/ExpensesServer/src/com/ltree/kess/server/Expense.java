package com.ltree.kess.server;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class Expense implements java.io.Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String description;
	public double amount;
	public long expenseDate;	

	
	public Expense(){}
	
	public Expense(String description, double amount, long date ){
		this.description = description;
		this.amount = amount;
		this.expenseDate = date;

	}	
	
}

