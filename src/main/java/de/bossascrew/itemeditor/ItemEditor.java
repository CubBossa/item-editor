package de.bossascrew.itemeditor;

import de.bossascrew.itemeditor.commands.ItemEditorCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemEditor extends JavaPlugin {


	@Override
	public void onEnable() {

		Bukkit.getPluginCommand("itemEditor").setExecutor(new ItemEditorCommand());
	}
}
