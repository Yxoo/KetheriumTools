package fr.yxoo;

import fr.yxoo.listeners.Configs;
import fr.yxoo.listeners.onJoin;
import fr.yxoo.placeholders.jobsPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class KetheriumTools extends JavaPlugin implements Listener {
    private static JavaPlugin instance;
    private Configs config;
    private DatabaseManager databaseManager;

    public String enableMessagePath = "onEnableMessage";
    public String disableMessagePath = "onDisableMessage";

    @Override
    public void onEnable() {

        instance = this;
        this.config = new Configs(this);
        databaseManager = new DatabaseManager(this, getDataFolder() + "/jobs_rewards.db");

        saveDefaultConfig();
        File pluginDirectory = getDataFolder();
        if (!pluginDirectory.exists()) {
            pluginDirectory.mkdirs();
        }

        PluginCommand command = getCommand("kethertools");
        if (command != null) {
            KetherToolsCommand executor = new KetherToolsCommand(this, config, databaseManager);
            command.setExecutor(executor);
            command.setTabCompleter(new KetherToolsTabCompleter(this));
        }

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Configs(this), this);
        getServer().getPluginManager().registerEvents(new onJoin(this, config), this);

        new jobsPlaceholder(this, config, databaseManager).register();

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            logSevere("KetheriumTools : PlaceholderAPI manquant !");
            return;
        }

        if (!this.getConfig().getString(enableMessagePath).isEmpty())
            System.out.println("\u001b[32m" + this.getConfig().getString(enableMessagePath) + "\u001b[37m");

    }

    @Override
    public void onDisable() {
        saveDefaultConfig();

        if (!this.getConfig().getString(disableMessagePath).isEmpty())
            System.out.println("\u001b[31m" + this.getConfig().getString(disableMessagePath) + "\u001b[37m");
    }

    public static JavaPlugin getInstance(){
        return instance;
    }

    public static void logInfo(String message){
        instance.getLogger().info(message);
    }

    public static void logWarning(String message){
        instance.getLogger().warning(message);
    }

    public static void logSevere(String message){
        instance.getLogger().severe(message);
    }

}
