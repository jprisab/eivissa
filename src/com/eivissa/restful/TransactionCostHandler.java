package com.eivissa.restful;

public class TransactionCostHandler {

	public int getTransactionCost(String strTransactionType) {
		int iResult = 0;

		switch (strTransactionType) {
		case "EntryWithoutAttachment":
			iResult = 5;
			break;
		case "EntryWithSmallAttachment":
			iResult = 10;
			break;
		case "EntryWithMediumAttachment":
			iResult = 20;
			break;
		case "EntryWithLargeAttachment":
			iResult = 50;
			break;

		default:
			iResult = 0;
			break;
		}

		return iResult;
	}
	
}
