package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.Message;
import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.bossascrew.itemeditor.exception.ItemEditorException;
import de.bossascrew.itemeditor.parser.Parser;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnchantSubCommand extends ItemSubCommand {

	public EnchantSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<Enchantment> [<level>]");
		acceptedFlags.add(ItemEditorCommand.FLAG_UNSAFE);
	}

	@Override
	public @Nullable ItemStack onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
			throw new CommandSyntaxException(this);
		}
		Enchantment enchantment = Parser.ENCHANT_PARSER.parse(args[0]);
		int level = 1;
		if (args.length > 1) {
			level = Parser.INT_PARSER.parse(args[1]);
		}
		if (!flags.containsKey(ItemEditorCommand.FLAG_UNSAFE) && !enchantment.canEnchantItem(stack)) {
			throw new ItemEditorException(Message.UNSAFE_REQUIRED);
		}

		ItemMeta itemMeta = stack.getItemMeta();
		itemMeta.addEnchant(enchantment, level, true);
		stack.setItemMeta(itemMeta);
		return stack;
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
		List<String> completions = super.getCompletions(sender, args, foundFlags);
		Player player = (Player) sender;
		ItemStack hand = player.getInventory().getItemInMainHand();
		if (args.length == 1) {
			completions.addAll(Parser.ENCHANT_PARSER.getCompletions(args[0],
					foundFlags.containsKey(ItemEditorCommand.FLAG_UNSAFE) ?
							List.of(Enchantment.values()) :
							Arrays.stream(Enchantment.values()).filter(enchantment -> enchantment.canEnchantItem(hand)).collect(Collectors.toList())));
		}
		return completions;
	}
}
