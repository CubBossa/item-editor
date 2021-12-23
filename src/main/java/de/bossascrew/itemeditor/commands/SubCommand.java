package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.exception.PermissionException;
import de.bossascrew.itemeditor.exception.PlayerRequiredException;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public abstract class SubCommand {

	private final @Nullable SubCommand parent;
	private final String[] names;
	private final @Nullable String permission;
	private final boolean playerRequired;

	public SubCommand(@Nullable SubCommand parent, String name, @Nullable String permission, boolean playerRequired) {
		this(parent, new String[]{name}, permission, playerRequired);
	}

	public SubCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission, boolean playerRequired) {
		this.parent = parent;
		this.names = names;
		this.permission = permission;
		this.playerRequired = playerRequired;

		if (parent instanceof BridgeCommand bc) {
			bc.registerSubCommand(this);
		}
	}

	public boolean onCommand(CommandSender sender, String[] args) {
		if (playerRequired && !(sender instanceof Player)) {
			throw new PlayerRequiredException();
		}
		if (permission != null && !sender.hasPermission(permission)) {
			throw new PermissionException();
		}
		return true;
	}

	public abstract List<String> getCompletions(CommandSender sender, String[] args);

	public boolean isParent(SubCommand subCommand) {
		SubCommand p = parent;
		while (p != null) {
			if (p.equals(subCommand)) {
				return true;
			}
			p = p.parent;
		}
		return false;
	}
}
