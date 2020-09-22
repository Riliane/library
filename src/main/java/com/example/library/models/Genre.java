package com.example.library.models;

public enum Genre {
	CLASSICS("Classics"), POETRY("Poetry"), FANTASY("Fantasy"), SCI_FI("Sci-fi");

	private String name;

	Genre(String name) {
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
