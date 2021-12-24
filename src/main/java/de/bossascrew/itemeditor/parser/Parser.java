package de.bossascrew.itemeditor.parser;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Parser<T> {

	Parser<Integer> INT_PARSER = new IntegerParser();
	Parser<DyeColor> DYE_PARSER = new DyeColorParser();
	Parser<Color> COLOR_PARSER = new ColorParser();
	Parser<Enchantment> ENCHANT_PARSER = new EnchantmentParser();
	Parser<ItemFlag> ITEMFLAG_PARSER = new ItemFlagParser();

	T parse(String input);

	T parse(String input, @Nullable List<T> allowedValues);

	List<T> parseConcat(String input);

	List<T> parseConcat(String input, @Nullable List<T> allowedValues);

	List<String> getCompletions(String input, List<T> allowedValues);

	List<String> getCompletionsConcat(String input, List<T> allowedValues);
}
