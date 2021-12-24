package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.Message;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.command.CommandSender;

public class ParseArgumentException extends ItemEditorException {

	private final String argument;

	public ParseArgumentException(String argument) {
		super(Message.PARSE_ERROR);
		this.argument = argument;
	}

	@Override
	public void sendMessage(CommandSender sender) {
		ItemEditor.getInstance().sendMessage(sender, getMessageObject().getTranslation(Template.of("argument", argument)));
	}
}
