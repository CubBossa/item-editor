package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.Message;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemEditorException extends RuntimeException {

	private final Message message;

	public Message getMessageObject() {
		return message;
	}
}
