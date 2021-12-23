package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.PermissionException;
import de.bossascrew.itemeditor.exception.PlayerRequiredException;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class SubCommand {

	private final @Nullable SubCommand parent;
	private final String[] names;
	private final @Nullable String permission;
	private final boolean playerRequired;
	protected final List<CommandFlag> acceptedFlags;
	private String syntax = "{<args>}";

	public SubCommand(@Nullable SubCommand parent, String name, @Nullable String permission, boolean playerRequired) {
		this(parent, new String[]{name}, permission, playerRequired);
	}

	public SubCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission, boolean playerRequired) {
		this.parent = parent;
		this.names = names;
		this.permission = permission;
		this.playerRequired = playerRequired;
		this.acceptedFlags = new ArrayList<>();

		if (parent instanceof BridgeCommand bc) {
			bc.registerSubCommand(this);
		}
	}

	public boolean onCommand(CommandSender sender, String[] args, Map<CommandFlag, String> flags) {
		if (playerRequired && !(sender instanceof Player)) {
			throw new PlayerRequiredException();
		}
		if (permission != null && !sender.hasPermission(permission)) {
			throw new PermissionException();
		}
		return true;
	}

	public void sendHelp(CommandSender sender) {

		List<SubCommand> toPrint = new ArrayList<>();
		if (this instanceof BridgeCommand bridgeCommand) {
			toPrint.addAll(bridgeCommand.getSubCommands());
		} else {
			toPrint.add(this);
		}
		for (SubCommand subCommand : toPrint) {
			StringBuilder commandbase = new StringBuilder(subCommand.names[0] + " " + subCommand.getSyntax());
			SubCommand parent = subCommand.getParent();
			while (parent != null) {
				commandbase.insert(0, parent.getNames()[0] + " ");
				parent = parent.getParent();
			}
			sender.sendMessage(" - /" + commandbase);
		}
	}

	public List<String> getCompletions(CommandSender sender, String[] args) {
		if (args[0].startsWith("--")) {
			return getAcceptedFlags().stream().map(flag -> "--" + flag.getName()).collect(Collectors.toList());
		} else if (args[0].startsWith("-")) {
			return getAcceptedFlags().stream()
					.filter(flag -> !args[0].contains("" + flag.getLetter()))
					.map(flag -> args[0] + flag.getLetter()).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

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

	public List<CommandFlag> getAcceptedFlags() {
		List<CommandFlag> list = new ArrayList<>(acceptedFlags);
		if (this instanceof BridgeCommand bridge) {
			for (SubCommand subCommand : bridge.getSubCommands()) {
				list.addAll(subCommand.acceptedFlags);
			}
		}
		return list;
	}
}
