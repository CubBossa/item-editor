package de.bossascrew.itemeditor.commands.flags;

import com.google.common.collect.Lists;
import de.bossascrew.itemeditor.parser.Parser;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TargetCommandFlag<T> extends CommandFlag {

	private final Parser<T> parser;
	private final boolean concat;
	private List<T> allowedValues = new ArrayList<>();

	public TargetCommandFlag(TargetCommandFlag<T> flag) {
		this(flag.getLetter(), flag.getName(), flag.getDescription(), flag.getParser(), flag.isConcat());
		this.allowedValues = flag.getAllowedValues();
	}

	public TargetCommandFlag(char letter, String name, String description, Parser<T> parser, boolean concat) {
		super(letter, name, description);
		this.parser = parser;
		this.concat = concat;
	}

	public List<T> parse(String input) {
		return concat ? parser.parseConcat(input, allowedValues) : Lists.newArrayList(parser.parse(input, allowedValues));
	}

	public List<String> getCompletions(String input) {
		return concat ? parser.getCompletionsConcat(input, allowedValues) : parser.getCompletions(input, allowedValues);
	}
}
