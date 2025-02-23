package fr.yxoo.listeners;

import fr.yxoo.KetheriumTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Configs implements Listener {
    private final KetheriumTools plugin;

    public Configs(KetheriumTools plugin) {
        this.plugin = plugin;
    }

    public String reloadMessagePath = "reloadMessage";
    public String jobsAutojoin = "jobsAutojoin";
    public String jobsJoinMessage = "jobsJoinMessage";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.getConfig();
    }

}
