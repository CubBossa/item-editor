package de.bossascrew.itemeditor.parser;

import org.bukkit.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class DyeColorParser extends BasicParser<DyeColor> {

	@Override
	public DyeColor parse(String input) {
		List<DyeColor> values = new ArrayList<>(List.of(DyeColor.values()));
		return super.parse(input, values);
	}

	@Override
	public List<DyeColor> parseConcat(String input) {
		List<DyeColor> values = new ArrayList<>(List.of(DyeColor.values()));
		return super.parseConcat(input, values);
	}

	@Override
	public List<String> getCompletions(String input, List<DyeColor> allowedValues) {
		return super.getCompletions(input, allowedValues);
	}

	@Override
	public List<String> getCompletionsConcat(String input, List<DyeColor> allowedValues) {
		return super.getCompletionsConcat(input, allowedValues);
	}
}
