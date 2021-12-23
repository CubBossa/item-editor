package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.Message;

public class PermissionException extends ItemEditorException {

	public PermissionException() {
		super(Message.NO_PERM);
	}
}
