package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.parser.Parser;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RgbColorSubCommand extends ItemSubCommand {

	public RgbColorSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<HexColor>");
		displayPredicate = itemStack -> {
			ItemMeta m = itemStack.getItemMeta();
			return m instanceof LeatherArmorMeta || m instanceof MapMeta || m instanceof PotionMeta;
		};
	}

	@Override
	public boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		Color color = Parser.COLOR_PARSER.parse(args[0]);

		ItemMeta meta = stack.getItemMeta();
		if (meta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) meta).setColor(color);
		} else if (meta instanceof MapMeta) {
			((MapMeta) meta).setColor(color);
		} else if (meta instanceof PotionMeta) {
			((PotionMeta) meta).setColor(color);
		}
		stack.setItemMeta(meta);
		return true;
	}
}
