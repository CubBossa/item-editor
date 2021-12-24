package de.bossascrew.itemeditor;

import de.bossascrew.itemeditor.commands.ItemEditorCommand;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemEditor extends JavaPlugin {

	@Getter
	private static ItemEditor instance;

	private BukkitAudiences bukkitAudiences;
	@Getter
	private MiniMessage miniMessage;

	private Audience consoleAudience;
	private Map<UUID, Audience> playerAudiences;

	@Override
	public void onEnable() {
		instance = this;

		bukkitAudiences = BukkitAudiences.create(this);
		miniMessage = MiniMessage.get();

		consoleAudience = bukkitAudiences.console();
		playerAudiences = new HashMap<>();

		new TranslationHandler(this, "en_US");

		Bukkit.getPluginCommand("itemeditor").setExecutor(new ItemEditorCommand());
	}

	public void sendMessage(CommandSender sender, Message message) {
		sendMessage(sender, message.getTranslation());
	}

	public void sendMessage(CommandSender sender, Component message) {
		Audience audience;
		if (sender instanceof Player player) {
			audience = playerAudiences.get(player.getUniqueId());
			if (audience == null) {
				audience = bukkitAudiences.player(player);
				playerAudiences.put(player.getUniqueId(), audience);
			}
		} else {
			audience = consoleAudience;
		}
		audience.sendMessage(message);
	}
}
