package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.Message;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.command.CommandSender;

public class UnexpectedFlagException extends ItemEditorException {

	private final String flag;

	public UnexpectedFlagException(String flag) {
		super(Message.UNEXPECTED_FLAG);
		this.flag = flag;
	}

	@Override
	public void sendMessage(CommandSender sender) {
		ItemEditor.getInstance().sendMessage(sender, getMessageObject().getTranslation(Template.of("flag", flag)));
	}
}
