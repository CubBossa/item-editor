package de.bossascrew.itemeditor;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Message {

	public static final Message PREFIX = new Message("prefix");
	public static final Message NO_PERM = new Message("no_permission");
	public static final Message PLAYER_REQUIRED = new Message("command.requires_player");
	public static final Message PARSE_ERROR = new Message("command.parse_error");
	public static final Message ITEM_REQUIRED = new Message("command.requires_item");
	public static final Message UNEXPECTED_FLAG = new Message("command.unexpected_flag");
	public static final Message SYNTAX_HEADER = new Message("command.syntax_help_header");
	public static final Message SYNTAX_LINE = new Message("command.syntax_help_line");
	public static final Message SYNTAX_FLAG_LINE = new Message("command.syntax_flags_line");
	public static final Message UNSAFE_REQUIRED = new Message("command.unsafe_flag_required");
	public static final Message TAG_NOT_FOUND = new Message("command.nbt.tag_not_found");

	@Getter
	private final String key;
	@Getter
	private final String comment;
	@Getter
	private final Pair<String, String>[] examplePlaceholders;

	@SafeVarargs
	public Message(String key, Pair<String, String>... examplePlaceholders) {
		this(key, "", examplePlaceholders);
	}

	@SafeVarargs
	public Message(String key, String comment, Pair<String, String>... examplePlaceholders) {
		this.key = key;
		this.comment = comment;
		this.examplePlaceholders = examplePlaceholders;
	}

	public Component getTranslation(Template... templates) {
		List<Template> t = new ArrayList<>(List.of(templates));
		if (!this.equals(Message.PREFIX)) {
			t.add(Template.of("prefix", Message.PREFIX.getTranslation()));
		}
		String format = TranslationHandler.getInstance().getMessage(key);
		return ItemEditor.getInstance().getMiniMessage().parse(format, t);
	}

	public List<Component> getTranslations(Template... templates) {
		String[] toFormat = TranslationHandler.getInstance().getMessage(key).split("\n");
		List<Component> result = new ArrayList<>();
		MiniMessage miniMessage = ItemEditor.getInstance().getMiniMessage();
		for (String string : toFormat) {
			result.add(miniMessage.parse(string, templates));
		}
		if (result.stream().allMatch(component -> component.equals(Component.text("")))) {
			return new ArrayList<>();
		}
		return result;
	}

	public String getLegacyTranslation(Template... templates) {
		return TextUtils.toLegacy(getTranslation(templates));
	}

	public List<String> getLegacyTranslations(Template... templates) {
		return getTranslations(templates).stream().map(TextUtils::toLegacy).collect(Collectors.toList());
	}

	public static List<Message> values() {
		List<Message> messages = new ArrayList<>();
		Field[] fields = Message.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.getType().equals(Message.class) && Modifier.isStatic(field.getModifiers())) {
					messages.add((Message) field.get(null));
				}
			} catch (IllegalAccessException ignored) {
			}
		}
		return messages;
	}
}
