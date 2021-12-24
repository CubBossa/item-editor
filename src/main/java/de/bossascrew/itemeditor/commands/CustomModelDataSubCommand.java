package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.parser.Parser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CustomModelDataSubCommand extends ItemSubCommand {

	public CustomModelDataSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<CustomModelData>");
	}

	@Override
	public boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		int customModelData = Parser.INT_PARSER.parse(args[0]);

		ItemMeta meta = stack.getItemMeta();
		meta.setCustomModelData(customModelData);
		stack.setItemMeta(meta);

		return true;
	}
}
