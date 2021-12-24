package de.bossascrew.itemeditor.parser;

import com.google.common.collect.Lists;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.List;

public class EnchantmentParser extends BasicParser<Enchantment> {

	public EnchantmentParser() {
		super(enchantment -> enchantment.getKey().getKey(), (string, enchantments) ->
				Arrays.stream(Enchantment.values()).filter(enchantment -> enchantment.getKey().getKey().equalsIgnoreCase(string)).findFirst().orElse(null));
	}

	@Override
	public Enchantment parse(String input) {
		return super.parse(input, Lists.newArrayList(Enchantment.values()));
	}

	@Override
	public List<Enchantment> parseConcat(String input) {
		return super.parseConcat(input, Lists.newArrayList(Enchantment.values()));
	}
}
