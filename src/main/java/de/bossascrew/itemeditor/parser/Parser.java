package de.bossascrew.itemeditor.parser;

import java.util.List;

public interface Parser<T> {

	public T parse(String input);

	public List<String> getCompletions(T input);
}
