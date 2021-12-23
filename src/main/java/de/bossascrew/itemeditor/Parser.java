package de.bossascrew.itemeditor;

import de.bossascrew.itemeditor.exception.ParseArgumentException;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Parser {

	public ItemFlag[] parseFlags(String input) {
		List<ItemFlag> flags = new ArrayList<>();
		if (input.equalsIgnoreCase("*")) {
			return ItemFlag.values();
		}
		String[] splits = input.split(",");
		for (String split : splits) {
			try {
				flags.add(ItemFlag.valueOf(split.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new ParseArgumentException("\"" + split + "\" is not a valid command flag.");
			}
		}
		return flags.toArray(new ItemFlag[0]);
	}

	public List<String> printItemFlags(String input) {
		String beforeLastComma = input.substring(0, input.lastIndexOf(',') + 1);
		List<String> completions = Arrays.stream(ItemFlag.values())
				.map(itemFlag -> itemFlag.toString().toLowerCase())
				.filter(itemFlag -> !beforeLastComma.toLowerCase().contains(itemFlag))
				.map(itemFlag -> beforeLastComma + itemFlag)
				.collect(Collectors.toList());
		completions.add("*");
		return completions;
	}

	public @Nullable Enchantment parseEnchant(String input) {
		Enchantment e = Enchantment.getByKey(NamespacedKey.minecraft(input));
		if (e == null) {
			throw new ParseArgumentException("There is no enchantment with the name \"" + input + "\".");
		}
		return e;
	}

	public List<String> printEnchants() {
		return Arrays.stream(Enchantment.values()).map(enchantment -> enchantment.getKey().getKey()).collect(Collectors.toList());
	}

	public List<String> printContainedEnchants(Player player) {
		ItemStack hand = player.getInventory().getItemInMainHand();
		if (hand.hasItemMeta()) {
			return hand.getItemMeta().getEnchants().keySet().stream()
					.map(enchantment -> enchantment.getKey().getKey().toLowerCase())
					.collect(Collectors.toList());
		}
		return null;
	}
}
