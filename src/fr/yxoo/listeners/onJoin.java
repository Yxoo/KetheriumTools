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
            player.performCommand("jobs join miner");
            player.performCommand("jobs join hunter");
            player.performCommand("jobs join farmer");
            player.performCommand("jobs join fisherman");
            player.performCommand("jobs join woodcutter");

            if (!plugin.getConfig().getString(config.jobsJoinMessage).isEmpty())
                player.sendMessage(plugin.getConfig().getString(config.jobsJoinMessage).replace("&", "§"));
        }
    }
}
