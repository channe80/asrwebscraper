package com.as.app.trademe.beans;

public enum PaymentMethod {
	None(0),
	BankDeposit(1),
	CreditCard(2),
	Cash(4),
	SafeTrader(8),
	Other(16);
	
	private int method;
	PaymentMethod(int method) {
		this.method = method;
	}
	
	public int method() {
		return this.method;
	}
}
