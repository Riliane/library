package com.example.library.controllers.errorhandling;

public class QuantityExceededException extends RuntimeException {
	public QuantityExceededException(Integer deleted, Integer existing) {
		super("Quantity of deleted items " + deleted.toString() + " exceeds existing quantity of items "
				+ existing.toString());
	}

	public QuantityExceededException() {
		super("Limit of books borrowed at once exceeded");
	}

}
