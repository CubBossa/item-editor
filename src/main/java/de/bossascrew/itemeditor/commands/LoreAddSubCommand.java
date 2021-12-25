package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.TextUtils;
import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.commands.flags.TargetCommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoreAddSubCommand extends ItemSubCommand {

	private final TargetCommandFlag<Integer> insert;

	public LoreAddSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<>");
		insert = new TargetCommandFlag<>(ItemEditorCommand.FLAG_INSERT);
		this.acceptedFlags.add(insert);
	}

	@Override
	public @Nullable ItemStack onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length < 1) {
			throw new CommandSyntaxException(this);
		}
		String gsonText = String.join(" ", args);
		if (!flags.containsKey(ItemEditorCommand.FLAG_GSON)) {
			if (flags.containsKey(ItemEditorCommand.FLAG_LEGACY)) {
				gsonText = TextUtils.toGson(TextUtils.fromLegacy(gsonText));
			} else {
				gsonText = TextUtils.toGson(ItemEditor.getInstance().getMiniMessage().parse(gsonText));
			}
		}

		NBTItem item = new NBTItem(stack);
		NBTCompound display = item.getOrCreateCompound("display");
		List<String> lore = display.getStringList("Lore");
		if (lore == null) {
			lore = new ArrayList<>();
		}
		int index = lore.size();
		if (flags.containsKey(insert)) {
			index = insert.parse(flags.get(insert)).get(0);
		}
		lore.add(index, gsonText);
		return item.getItem();
	}
}
