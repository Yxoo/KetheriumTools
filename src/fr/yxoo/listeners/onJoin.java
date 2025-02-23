package fr.yxoo.listeners;

import fr.yxoo.KetheriumTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {
    private final KetheriumTools plugin;
    private final Configs config;

    // Constructeur pour avoir accès au plugin principal
    public onJoin(KetheriumTools plugin, Configs config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore() && plugin.getConfig().getBoolean(config.jobsAutojoin))
        {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jobs join miner");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jobs join hunter");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jobs join farmer");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jobs join fisherman");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "jobs join woodcutter");
        }
    }
}
