package de.bossascrew.itemeditor.commands.flags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CommandFlag flag = (CommandFlag) o;
		return Objects.equals(name, flag.name);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
