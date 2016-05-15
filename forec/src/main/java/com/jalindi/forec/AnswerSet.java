package com.jalindi.forec;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AnswerSet implements Iterable<Answer> {
	private Set<Answer> answers = new HashSet<>();

	@Override
	public Iterator<Answer> iterator() {
		return answers.iterator();
	}

	@Override
	public String toString() {
		return answers.toString();
	}

	public void add(String name, String... values) {
		answers.add(new Answer(name, values));
	}

	public void add(String name, Value... values) {
		answers.add(new Answer(name, values));
	}

}
