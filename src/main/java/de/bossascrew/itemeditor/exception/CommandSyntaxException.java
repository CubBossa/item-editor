package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.commands.SubCommand;
import lombok.Getter;

public class CommandSyntaxException extends ItemEditorException {

	@Getter
	private final SubCommand command;

	public CommandSyntaxException(SubCommand subCommand) {
		super(null); //TODO
		this.command = subCommand;
	}
}
