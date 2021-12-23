package de.bossascrew.itemeditor.commands.flags;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class TargetCommandFlag extends CommandFlag {

	public @Nullable String target = null;

	public TargetCommandFlag(char letter, String name, String description, @Nullable String target) {
		super(letter, name, description);
		this.target = target;
	}
}
