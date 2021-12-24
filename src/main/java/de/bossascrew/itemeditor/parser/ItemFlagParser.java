package de.bossascrew.itemeditor.parser;

import org.bukkit.inventory.ItemFlag;

import java.util.List;

public class ItemFlagParser extends BasicParser<ItemFlag> {

	@Override
	public ItemFlag parse(String input) {
		return parse(input, List.of(ItemFlag.values()));
	}

	@Override
	public List<ItemFlag> parseConcat(String input) {
		return super.parseConcat(input, List.of(ItemFlag.values()));
	}
}
