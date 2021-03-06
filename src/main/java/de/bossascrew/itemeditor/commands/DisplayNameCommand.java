package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.TextUtils;
import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.CommandSyntaxException;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DisplayNameCommand extends ItemSubCommand {

	public DisplayNameCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		this(parent, new String[]{name}, permission);
	}

	public DisplayNameCommand(@Nullable SubCommand parent, String[] names, @Nullable String permission) {
		super(parent, names, permission);
		setSyntax("<Displayname>");
		this.acceptedFlags.add(ItemEditorCommand.FLAG_GSON);
		this.acceptedFlags.add(ItemEditorCommand.FLAG_LEGACY);
	}

	@Override
	public @Nullable ItemStack onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		if (args.length == 0) {
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
		display.setString("Name", gsonText);

		return item.getItem();
	}
}
