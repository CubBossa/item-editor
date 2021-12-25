package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.Message;
import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.PermissionException;
import de.bossascrew.itemeditor.exception.PlayerRequiredException;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
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
		if(parent != null && parent.equals(this)) {
			throw new IllegalArgumentException("Subcommand cannot set self as parent");
		}

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

		String commandBase = getNames()[0] + " ";
		SubCommand extractName = this;
		while (extractName.getParent() != null) {
			extractName = extractName.getParent();
			commandBase = extractName.getNames()[0] + " " + commandBase;
		}
		List<Component> components = new ArrayList<>();
		for (SubCommand subCommand : toPrint) {
			Component flagList = Component.text("Flags:");
			for (CommandFlag flag : subCommand.getAcceptedFlags()) {
				flagList = flagList.append(Component.newline()).append(Message.SYNTAX_FLAG_LINE.getTranslation(
						Template.of("name", flag.getName()),
						Template.of("letter", flag.getLetter() + ""),
						Template.of("description", flag.getDescription())));
			}
			components.add(Message.SYNTAX_LINE.getTranslation(
					Template.of("cmd_syntax", commandBase + subCommand.getNames()[0] + " " + subCommand.getSyntax()),
					Template.of("cmd", "/" + commandBase + subCommand.getNames()[0]),
					Template.of("flags", "/" + ItemEditor.getInstance().getMiniMessage().serialize(flagList))));
		}
		if (components.size() > 0) {
			ItemEditor.getInstance().sendMessage(sender, Message.SYNTAX_HEADER);
			components.forEach(component -> ItemEditor.getInstance().sendMessage(sender, component));
		}
	}

	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
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
