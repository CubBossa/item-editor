package de.bossascrew.itemeditor.commands.flags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandFlag implements Cloneable {

	private final char letter;
	private final String name;
	private final String description;

	@Override
	public CommandFlag clone() {
		return new CommandFlag(letter, name, description);
	}
}
