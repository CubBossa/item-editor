package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.exception.ItemEditorException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemEditorCommand implements CommandExecutor, TabCompleter {

	private final SubCommand startCommand;

	public ItemEditorCommand() {
		startCommand = new BridgeCommand(null, "itemeditor", null, false);

		new ItemFlagsSubCommand(startCommand, "itemflags", null);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		//TODO command flags rausfiltern
		boolean success = false;
		try {
			success = startCommand.onCommand(commandSender, args);
		} catch (ItemEditorException e) {
			e.getMessage(); //TODO an sender senden;
		}
		return success;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		//TODO command flags rausfiltern
		try {
			return startCommand.getCompletions(commandSender, args);
		} catch (ItemEditorException e) {
			//TODO messsage senden
		}
		return null;
	}
}
