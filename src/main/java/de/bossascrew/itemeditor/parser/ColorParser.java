package de.bossascrew.itemeditor.parser;


import de.bossascrew.itemeditor.exception.ParseArgumentException;
import org.bukkit.Color;

import java.util.List;

public class ColorParser extends BasicParser<Color> {

	@Override
	public Color parse(String input) {
		return parse(input, null);
	}

	@Override
	public Color parse(String input, List<Color> allowedValues) {
		if (input.startsWith("#")) {
			input = input.substring(1);
		}
		int hex;
		try {
			hex = Integer.parseInt(input, 16);
		} catch (NumberFormatException e) {
			throw new ParseArgumentException(input);
		}
		return Color.fromRGB(hex);
	}

	@Override
	public List<Color> parseConcat(String input) {
		return super.parseConcat(input, null);
	}
}
