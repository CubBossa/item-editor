package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.Message;

public class UnexpectedFlagException extends ItemEditorException {

	public UnexpectedFlagException(String flag) {
		super(Message.UNEXPECTED_FLAG);
	}

}
