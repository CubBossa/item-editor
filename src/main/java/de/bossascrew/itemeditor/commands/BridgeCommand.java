package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.exception.ItemEditorException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BridgeCommand extends SubCommand {

	private final List<SubCommand> subCommands;

	public BridgeCommand(@Nullable SubCommand parent, String name, @Nullable String permission, boolean requiresPlayer) {
		super(parent, name, permission, requiresPlayer);
		this.subCommands = new ArrayList<>();
	}

	public void registerSubCommand(SubCommand subCommand) throws IllegalArgumentException {
		if (isParent(subCommand)) {
			throw new IllegalArgumentException("Registered SubCommand is a parent of subcommand.");
		}
		this.subCommands.add(subCommand);
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args)  {
		if (!super.onCommand(sender, args)) {
			return false;
		}
		for (SubCommand subCommand : subCommands) {
			if (Arrays.stream(subCommand.getNames()).noneMatch(s -> s.equalsIgnoreCase(args[0]))) {
				continue;
			}
			subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
			return true;
		}
		//TODO message unknown SubCommand
		return false;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args) {
		boolean isSenderPlayer = sender instanceof Player;
		if (isPlayerRequired() && !isSenderPlayer) {
			return null;
		}
		return subCommands.stream()
				.filter(sub -> !sub.isPlayerRequired() || isSenderPlayer)
				.filter(sub -> sub.getPermission() == null || sender.hasPermission(sub.getPermission()))
				.map(SubCommand::getNames)
				.flatMap(Stream::of)
				.filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
				.collect(Collectors.toList());
	}
}
