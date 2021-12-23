package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.Message;
import de.bossascrew.itemeditor.Parser;
import de.bossascrew.itemeditor.exception.ItemEditorException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFlagsSubCommand extends ItemSubCommand {

	public ItemFlagsSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
	}

	public ItemFlagsSubCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission) {
		super(parent, names, permission);
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (!super.onCommand(sender, args)) {
			return false;
		}
		ItemFlag[] itemFlags = Parser.parseFlags(args[0]);

		Player player = (Player) sender;
		ItemStack itemStack = player.getInventory().getItemInMainHand();
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(itemFlags);
		itemStack.setItemMeta(meta);

		//TODO success sound
		return true;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args) {
		return Parser.printItemFlags(args[0]);
	}
}
