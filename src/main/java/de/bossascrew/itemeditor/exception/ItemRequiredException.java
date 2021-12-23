package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.Message;

public class ItemRequiredException extends ItemEditorException {

	public ItemRequiredException() {
		super(Message.ITEM_REQUIRED);
	}
}
