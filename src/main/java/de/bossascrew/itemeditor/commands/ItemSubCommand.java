package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.exception.ItemRequiredException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemSubCommand extends SubCommand {

	public ItemSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission, true);
	}

	public ItemSubCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission) {
		super(parent, names, permission, true);
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (!super.onCommand(sender, args)) {
			return false;
		}
		Player player = (Player) sender;
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if (mainHand.getType() == Material.AIR) {
			throw new ItemRequiredException();
		}
		if (!mainHand.hasItemMeta()) {
			mainHand.setItemMeta(Bukkit.getItemFactory().getItemMeta(mainHand.getType()));
		}
		return true;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args) {
		return null;
	}
}
