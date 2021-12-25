package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.commands.flags.CommandFlagParser;
import de.bossascrew.itemeditor.commands.flags.TargetCommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.exception.ItemEditorException;
import de.bossascrew.itemeditor.parser.Parser;
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
	public static CommandFlag FLAG_UNSAFE = new CommandFlag('u', "unsafe", "allows applying unsafe enchantments/attributes");
	public static CommandFlag FLAG_GSON = new CommandFlag('g', "gson", "parses the input as gson format");
	public static CommandFlag FLAG_LEGACY = new CommandFlag('l', "legacy", "parses the input as legacy format (&...)");
	public static TargetCommandFlag<Integer> FLAG_INSERT = new TargetCommandFlag<>('i', "insert", "inserts at provided index", Parser.INT_PARSER, false);

	private final SubCommand startCommand;
	private final CommandFlagParser parser;

	public ItemEditorCommand() {
		startCommand = new BridgeCommand(null, "itemeditor", null, false);
		parser = new CommandFlagParser();

		new ItemFlagsSubCommand(startCommand, "itemflags", null);
		new DisplayNameCommand(startCommand, "displayname", null);
		new CustomModelDataSubCommand(startCommand, "custommodeldata", null);
		new DamageSubCommand(startCommand, "damage", null);
		new ColorSubCommand(startCommand, "dyecolor", null);
		new RgbColorSubCommand(startCommand, "color", null);
		new EnchantSubCommand(startCommand, "enchant", null);
		new NbtSubCommand(startCommand, "nbttag", null);

		SubCommand lore = new BridgeCommand(startCommand, "lore", null, true);
		new LoreClearSubCommand(lore, "clear", null);
		new LoreAddSubCommand(lore, "add", null);
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
			e.sendMessage(commandSender);
		}
		return success;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		try {
			Map<CommandFlag, String> foundFlags = new HashMap<>();
			args = parser.reduce(args, startCommand.getAcceptedFlags(), foundFlags, true);
			return startCommand.getCompletions(commandSender, args, foundFlags);
		} catch (ItemEditorException e) {
			System.out.println(e.getClass().getName());
			//TODO messsage senden
		}
		return null;
	}
}
