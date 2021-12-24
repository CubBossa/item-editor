package de.bossascrew.itemeditor;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class TranslationHandler {

	@Getter
	private static TranslationHandler instance;

	private final JavaPlugin plugin;
	private String activeLanguage = "none";
	private final Map<String, String> messageFormats;
	private final Map<String, String> fallbackLanguage;

	public TranslationHandler(JavaPlugin plugin, String startlanguage) {
		instance = this;
		this.plugin = plugin;
		messageFormats = new HashMap<>();
		loadLanguage("en_US");
		fallbackLanguage = new HashMap<>(messageFormats);
		loadLanguage(startlanguage);
	}

	public CompletableFuture<Boolean> loadLanguage(String languageKey) {
		activeLanguage = languageKey;
		messageFormats.clear();

		return CompletableFuture.supplyAsync(() -> {

			File file = new File(plugin.getDataFolder(), "lang/" + languageKey + ".yml");
			if (!file.exists()) {
				plugin.saveResource("lang/" + languageKey + ".yml", false);
				file = new File(plugin.getDataFolder(), "lang/" + languageKey + ".yml");
				if (!file.exists()) {

					plugin.getLogger().log(Level.SEVERE, "Error while creating language.yml for " + languageKey);
					return false;
				}
			}
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			Map<String, Object> map = cfg.getValues(true);
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() instanceof String s) {
					messageFormats.put(entry.getKey(), s);
				}
			}
			return true;
		});
	}

	public String getMessage(String key) {
		return messageFormats.getOrDefault(key, fallbackLanguage.getOrDefault(key, activeLanguage + "-missing:" + key));
	}
}
