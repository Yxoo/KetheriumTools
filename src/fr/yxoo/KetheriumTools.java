package fr.yxoo;

import fr.yxoo.listeners.Configs;
import fr.yxoo.placeholders.jobsPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class KetheriumTools extends JavaPlugin implements Listener {
    private static JavaPlugin instance;
    private Configs config;

    public String enablePath = "onEnableMessage.enable";
    public String enableMessagePath = "onEnableMessage.message";
    public String disablePath = "onDisableMessage.enable";
    public String disableMessagePath = "onDisableMessage.message";

    @Override
    public void onEnable() {

        instance = this;
        this.config = new Configs(this);

        saveDefaultConfig();

        PluginCommand command = getCommand("kethertools");
        if (command != null) {
            KeterToolsCommand executor = new KeterToolsCommand(this, config);
            command.setExecutor(executor);
            command.setTabCompleter(new KetherToolsTabCompleter());
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Configs(this), this);
        new jobsPlaceholder().register();

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            logSevere("KetheriumTools : PlaceholderAPI manquant !");
            return;
        }

        if (this.getConfig().getBoolean(enablePath))
            System.out.println("\u001b[32m" + this.getConfig().getString(enableMessagePath) + "\u001b[37m");

    }

    @Override
    public void onDisable() {
        if (this.getConfig().getBoolean(disablePath))
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
