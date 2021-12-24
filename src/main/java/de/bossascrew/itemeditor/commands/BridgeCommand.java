package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BridgeCommand extends SubCommand {

	@Getter
	private final List<SubCommand> subCommands;

	public BridgeCommand(@Nullable SubCommand parent, String name, @Nullable String permission, boolean requiresPlayer) {
		super(parent, name, permission, requiresPlayer);
		this.subCommands = new ArrayList<>();
		this.setSyntax("[...]");
	}

	public void registerSubCommand(SubCommand subCommand) throws IllegalArgumentException {
		if (isParent(subCommand)) {
			throw new IllegalArgumentException("Registered SubCommand is a parent of subcommand.");
		}
		this.subCommands.add(subCommand);
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args, Map<CommandFlag, String> flags) {
		if (!super.onCommand(sender, args, flags)) {
			return false;
		}
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		for (SubCommand subCommand : subCommands) {
			if (Arrays.stream(subCommand.getNames()).noneMatch(s -> s.equalsIgnoreCase(args[0]))) {
				continue;
			}
			subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.length), flags);
			return true;
		}
		throw new CommandSyntaxException(this);
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {

		if (args.length > 1) {
			SubCommand called = subCommands.stream()
					.filter(subCommand -> Stream.of(subCommand.getNames()).anyMatch(name -> name.equalsIgnoreCase(args[0])))
					.findFirst().orElse(null);
			if (called == null) {
				return null;
			}
			return called.getCompletions(sender, Arrays.copyOfRange(args, 1, args.length), foundFlags);
		}

		List<String> completions = super.getCompletions(sender, args, foundFlags);
		boolean isSenderPlayer = sender instanceof Player;
		if (isPlayerRequired() && !isSenderPlayer) {
			return null;
		}
		completions.addAll(subCommands.stream()
				.filter(sub -> !sub.isPlayerRequired() || isSenderPlayer)
				.filter(sub -> sub.getPermission() == null || sender.hasPermission(sub.getPermission()))
				.filter(sub -> !(sub instanceof ItemSubCommand isub) ||
						(isSenderPlayer && ((Player) sender).getInventory().getItemInMainHand().getType() != Material.AIR
								&& isub.getDisplayPredicate().test(((Player) sender).getInventory().getItemInMainHand())))
				.map(SubCommand::getNames)
				.flatMap(Stream::of)
				.filter(s -> s.toLowerCase().startsWith(args[0]))
				.collect(Collectors.toList()));
		return completions;
	}
}
