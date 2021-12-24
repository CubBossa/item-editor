package de.bossascrew.itemeditor.parser;

import de.bossascrew.itemeditor.exception.ParseArgumentException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BasicParser<T> implements Parser<T> {

	private Function<T, String> toString = Object::toString;
	private BiFunction<String, List<T>, T> fromString = (string, ts) -> ts.stream()
			.filter(t -> toString.apply(t).equalsIgnoreCase(string)).findFirst().orElse(null);

	public BasicParser() {

	}

	public BasicParser(Function<T, String> toString, BiFunction<String, List<T>, T> fromString) {
		this.toString = toString;
		this.fromString = fromString;
	}


	public T parse(String input, @Nullable List<T> allowedValues) {
		T found = null;
		if (allowedValues != null) {
			found = fromString.apply(input, allowedValues);
		}
		if (found == null) {
			throw new ParseArgumentException(input);
		}
		return found;
	}

	public List<T> parseConcat(String input, @Nullable List<T> allowedValues) {
		List<T> found = new ArrayList<>();
		if (input.equalsIgnoreCase("*") && allowedValues != null) {
			return allowedValues;
		}
		String[] splits = input.split(",");
		for (String split : splits) {
			T t = parse(split, allowedValues);
			if (t == null) {
				continue;
			}
			found.add(t);
		}
		return found;
	}

	public List<String> getCompletions(String input, List<T> allowedValues) {
		return allowedValues.stream().map(t -> toString.apply(t))
				.filter(s -> s.startsWith(input.toLowerCase()))
				.collect(Collectors.toList());
	}

	public List<String> getCompletionsConcat(String input, List<T> allowedValues) {
		String beforeLastComma = input.substring(0, input.lastIndexOf(',') + 1);
		List<String> completions = allowedValues.stream()
				.map(t -> toString.apply(t))
				.filter(v -> !beforeLastComma.toLowerCase().contains(v))
				.map(v -> beforeLastComma + v)
				.filter(s -> s.startsWith(input.toLowerCase()))
				.collect(Collectors.toList());
		completions.add("*");
		return completions;
	}
}
