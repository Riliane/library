package com.example.library.models;

public enum BookOrderColumns {
	NAME("bookName"), GENRE("genre");
	// how to sort by authors if we have multiple?

	private String name;

	BookOrderColumns(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
