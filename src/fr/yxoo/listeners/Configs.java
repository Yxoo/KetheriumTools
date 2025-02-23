package fr.yxoo.listeners;

import fr.yxoo.KeteriumTools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Configs implements Listener {
    private final KeteriumTools plugin;

    public Configs(KeteriumTools plugin) {
        this.plugin = plugin;
    }

    public String reloadPath = "reloadMessage.enable";
    public String reloadMessagePath = "reloadMessage.message";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.getConfig();
    }

}
