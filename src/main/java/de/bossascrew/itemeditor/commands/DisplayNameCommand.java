package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DisplayNameCommand extends ItemSubCommand {

	public DisplayNameCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		this(parent, new String[]{name}, permission);
	}

	public DisplayNameCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission) {
		super(parent, names, permission);
		setSyntax("<Displayname>");
	}

	@Override
	public boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(args[0]); //TODO minimessage natürlich
		stack.setItemMeta(meta);

		return true;
	}
}
