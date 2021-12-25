package de.bossascrew.itemeditor;

import de.tr7zw.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class TextUtils {

	private static final GsonComponentSerializer GSON_SERIALZIER = GsonComponentSerializer.builder().build();

	private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
			.character('&')
			.hexColors()
			.hexCharacter('#')
			.build();

	public Component fromLegacy(String legacy) {
		return LEGACY_SERIALIZER.deserialize(legacy);
	}

	public String toLegacy(Component component) {
		return LEGACY_SERIALIZER.serialize(component);
	}

	public String toGson(Component component) {
		return GSON_SERIALZIER.serialize(component);
	}

	public String toLegacyFromMiniMessage(String minimessage) {
		return toLegacy(ItemEditor.getInstance().getMiniMessage().parse(minimessage));
	}

	/**
	 * @param itemStack the itemstack to display as a text component with hover text.
	 * @return the displayname of the itemstack with the nbt data as hover text.
	 */
	public Component toComponent(ItemStack itemStack, boolean hover) {
		if (!hover) {
			return itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() ?
					fromLegacy(itemStack.getItemMeta().getDisplayName()) :
					toTranslatable(itemStack.getType());
		}
		return (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() ?
				fromLegacy(itemStack.getItemMeta().getDisplayName()) :
				toTranslatable(itemStack.getType()))
				.hoverEvent(HoverEvent.showItem(HoverEvent.ShowItem.of(Key.key(itemStack.getType().getKey().toString()),
						1, BinaryTagHolder.of(new NBTItem(itemStack).toString()))));
	}

	public Component toComponent(ItemStack itemStack) {
		return toComponent(itemStack, true);
	}

	public Component toTranslatable(Material material) {
		if (material.isBlock()) {
			return Component.translatable("block.minecraft." + String.valueOf(material).toLowerCase());
		}
		return Component.translatable("item.minecraft." + String.valueOf(material).toLowerCase());
	}
}
