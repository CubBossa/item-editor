package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.ItemRequiredException;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ItemSubCommand extends SubCommand {

	@Getter
	protected Predicate<ItemStack> displayPredicate;

	public ItemSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		this(parent, new String[]{name}, permission);
	}

	public ItemSubCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission) {
		super(parent, names, permission, true);
		this.acceptedFlags.add(ItemEditorCommand.FLAG_COPY);
		this.displayPredicate = itemStack -> true;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args, Map<CommandFlag, String> flags) {
		if (!super.onCommand(sender, args, flags)) {
			return false;
		}
		Player player = (Player) sender;
		ItemStack mainHand = player.getInventory().getItemInMainHand().clone();
		if (mainHand.getType() == Material.AIR) {
			throw new ItemRequiredException();
		}
		if (!mainHand.hasItemMeta()) {
			mainHand.setItemMeta(Bukkit.getItemFactory().getItemMeta(mainHand.getType()));
		}
		boolean success = onCommand(player, args, flags, mainHand);

		if (flags.containsKey(ItemEditorCommand.FLAG_COPY)) {
			player.getInventory().addItem(mainHand);
		} else {
			player.getInventory().setItemInMainHand(mainHand);
		}
		return success;
	}

	public abstract boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack);

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
		return super.getCompletions(sender, args, foundFlags);
	}
}
