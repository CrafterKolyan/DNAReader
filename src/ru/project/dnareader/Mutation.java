package ru.project.dnareader;

public class Mutation {

	int begin = 0;
	int end = 0;
	int val = 0;
	String info = "";

	public Mutation(int val, String info) {
		this.val = val;
		this.info = info;
	}
}
