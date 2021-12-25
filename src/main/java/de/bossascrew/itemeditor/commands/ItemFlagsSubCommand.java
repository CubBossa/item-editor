package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.parser.Parser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemFlagsSubCommand extends ItemSubCommand {

	public ItemFlagsSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		this(parent, new String[]{name}, permission);
	}

	public ItemFlagsSubCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission) {
		super(parent, names, permission);
		setSyntax("*|<ItemFlag>{,<ItemFlag>}");
	}

	@Override
	public @Nullable ItemStack onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack itemStack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		List<ItemFlag> itemFlags = Parser.ITEMFLAG_PARSER.parseConcat(args[0]);

		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(itemFlags.toArray(ItemFlag[]::new));
		itemStack.setItemMeta(meta);

		return itemStack;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
		List<String> completions = super.getCompletions(sender, args, foundFlags);
		if (args.length == 1) {
			completions.addAll(Parser.ITEMFLAG_PARSER.getCompletionsConcat(args[0], List.of(ItemFlag.values())));
		}
		return completions;
	}
}
