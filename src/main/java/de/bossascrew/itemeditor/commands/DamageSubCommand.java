package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.parser.Parser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DamageSubCommand extends ItemSubCommand {

	public DamageSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<Damage>");
		this.displayPredicate = itemStack -> itemStack.getItemMeta() instanceof Damageable;
	}

	@Override
	public boolean onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		int damage = Parser.INT_PARSER.parse(args[0]);

		if (stack.getItemMeta() instanceof Damageable damageable) {
			damageable.setDamage(damage);
			stack.setItemMeta(damageable);
			return true;
		}
		return false;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
		List<String> completions = super.getCompletions(sender, args, foundFlags);
		if (args.length == 1) {
			completions.add("1");
			completions.add("255");
		}
		return completions;
	}
}
