package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.commands.flags.CommandFlagParser;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.exception.ItemEditorException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemEditorCommand implements CommandExecutor, TabCompleter {

	public static CommandFlag FLAG_COPY = new CommandFlag('c', "copy", "applies modification to a copy of the held itemstack");

	private final SubCommand startCommand;
	private final CommandFlagParser parser;

	public ItemEditorCommand() {
		startCommand = new BridgeCommand(null, "itemeditor", null, false);
		parser = new CommandFlagParser();

		new ItemFlagsSubCommand(startCommand, "itemflags", null);

		new DisplayNameCommand(startCommand, "displayname", null);

		SubCommand lore = new BridgeCommand(startCommand, "lore", null, true);


	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		boolean success = false;
		try {
			Map<CommandFlag, String> foundFlags = new HashMap<>();
			args = parser.reduce(args, startCommand.getAcceptedFlags(), foundFlags, false);
			success = startCommand.onCommand(commandSender, args, foundFlags);
		} catch (CommandSyntaxException e) {
			e.getCommand().sendHelp(commandSender);
		} catch (ItemEditorException e) {
			System.out.println(e.getClass().getName());
			e.getMessage(); //TODO an sender senden;
		}
		return success;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		try {
			Map<CommandFlag, String> foundFlags = new HashMap<>();
			args = parser.reduce(args, startCommand.getAcceptedFlags(), foundFlags, true);
			return startCommand.getCompletions(commandSender, args);
		} catch (ItemEditorException e) {
			System.out.println(e.getClass().getName());
			//TODO messsage senden
		}
		return null;
	}
}
