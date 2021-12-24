package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.parser.Parser;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * dye color not rgb color
 */
public class ColorSubCommand extends ItemSubCommand {

	public ColorSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<Dye Color>");
		displayPredicate = itemStack -> itemStack.getItemMeta() instanceof Colorable;
	}

	@Override
	public boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		DyeColor d = Parser.DYE_PARSER.parse(args[0]);

		if (stack instanceof Colorable colorable) {
			colorable.setColor(d);
			return true;
		}
		return false;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
		List<String> completions = super.getCompletions(sender, args, foundFlags);
		if (args.length == 1) {
			completions.addAll(Parser.DYE_PARSER.getCompletions(args[0], List.of(DyeColor.values())));
		}
		return completions;
	}
}
