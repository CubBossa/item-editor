package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.Parser;
import de.bossascrew.itemeditor.commands.flags.CommandFlag;
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
	public boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack itemStack) {
		ItemFlag[] itemFlags = Parser.parseFlags(args[0]);

		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(itemFlags);
		itemStack.setItemMeta(meta);

		return true;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args) {
		List<String> completions = super.getCompletions(sender, args);
		completions.addAll(Parser.printItemFlags(args[0]));
		return completions;
	}
}
