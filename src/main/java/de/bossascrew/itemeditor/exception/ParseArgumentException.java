package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.Message;

public class ParseArgumentException extends ItemEditorException {

	public ParseArgumentException(String message) {
		super(Message.PARSE_ERROR); //TODO translation with message;
	}
}
