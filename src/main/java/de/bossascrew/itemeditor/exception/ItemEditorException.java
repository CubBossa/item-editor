package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.Message;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ItemEditorException extends RuntimeException {

	private final Message message;

	public Message getMessageObject() {
		return message;
	}

	public void sendMessage(CommandSender sender) {
		ItemEditor.getInstance().sendMessage(sender, message);
	}
}
