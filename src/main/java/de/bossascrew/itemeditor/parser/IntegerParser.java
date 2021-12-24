package de.bossascrew.itemeditor.parser;

import de.bossascrew.itemeditor.exception.ParseArgumentException;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IntegerParser extends BasicParser<Integer> {

	@Override
	public Integer parse(String input) {
		return parse(input, null);
	}

	@Override
	public Integer parse(String input, @Nullable List<Integer> allowedValues) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			throw new ParseArgumentException(input);
		}
	}

	@Override
	public List<Integer> parseConcat(String input) {
		return super.parseConcat(input, null);
	}
}
