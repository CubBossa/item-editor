package de.bossascrew.itemeditor.exception;

import de.bossascrew.itemeditor.Message;

public class PlayerRequiredException extends ItemEditorException {

	public PlayerRequiredException() {
		super(Message.PLAYER_REQUIRED);
	}
}
