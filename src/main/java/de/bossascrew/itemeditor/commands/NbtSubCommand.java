package de.bossascrew.itemeditor.commands;

import de.bossascrew.itemeditor.ItemEditor;
import de.bossascrew.itemeditor.Message;
import de.bossascrew.itemeditor.commands.flags.CommandFlag;
import de.bossascrew.itemeditor.exception.ItemEditorException;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NbtSubCommand extends ItemSubCommand {

	public NbtSubCommand(@Nullable SubCommand parent, String name, @Nullable String permission) {
		super(parent, name, permission);
		setSyntax("<path.to.Tag> [<Value>]");
	}

	@Override
	public @Nullable ItemStack onCommand(Player player, String[] args, Map<CommandFlag, String> flags, ItemStack stack) {
		NBTItem item = new NBTItem(stack);

		String[] tags = new String[0];
		if (args.length > 0) {
			tags = args[0].split("\\.");
		}

		NBTCompound parent = item;
		Object value = item;
		for (String tag : tags) {
			if (value instanceof NBTCompound compound) {
				parent = compound;
				value = compound.getCompound(tag);
			}
			if (value == null) {
				value = getFromKey(parent, tag);
				if (value == null) {
					throw new ItemEditorException(Message.TAG_NOT_FOUND);
				}
			}
		}
		if (args.length >= 2) {
			String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			//parent.setObject(tags[tags.length - 1], input);
			//TODO
		} else {
			ItemEditor.getInstance().sendMessage(player, Component.text((args.length == 0 ? "item" : args[0]) + ": ", NamedTextColor.YELLOW)
					.append(Component.text(value.toString(), NamedTextColor.WHITE)));
		}
		return item.getItem();
	}

	@Override
	public List<String> getCompletions(CommandSender sender, String[] args, Map<CommandFlag, String> foundFlags) {
		List<String> completions = super.getCompletions(sender, args, foundFlags);

		if (args.length == 1) {
			NBTItem item = new NBTItem(((Player) sender).getInventory().getItemInMainHand());
			String[] tags = args[0].split("\\.");
			NBTCompound compound = item;
			for (String tag : tags) {
				NBTCompound next = compound.getCompound(tag);
				if (next == null) {
					break;
				}
				compound = next;
			}
			//TODO parsed noch nicht ganz richtig
			String joined = String.join(".", Arrays.copyOfRange(tags, 0, tags.length - 1));
			completions.addAll(compound.getKeys().stream()
					.map(string -> (joined.isEmpty() ? "" : joined + ".") + string)
					.filter(string -> string.toLowerCase().startsWith(args[0].toLowerCase()))
					.collect(Collectors.toList()));
		}
		return completions;
	}

	public @Nullable
	static Object getFromKey(NBTCompound compound, String key) {
		NBTType t = compound.getType(key);
		return switch (t) {
			case NBTTagCompound -> compound.getCompound(key);
			case NBTTagInt -> compound.getInteger(key);
			case NBTTagString -> compound.getString(key);
			case NBTTagDouble -> compound.getDouble(key);
			case NBTTagByte -> compound.getByte(key);
			case NBTTagByteArray -> compound.getByteArray(key);
			case NBTTagFloat -> compound.getFloat(key);
			case NBTTagIntArray -> compound.getIntArray(key);
			case NBTTagList -> compound.getStringList(key);
			case NBTTagLong -> compound.getLong(key);
			case NBTTagShort -> compound.getShort(key);
			default -> null;
		};
	}
}
