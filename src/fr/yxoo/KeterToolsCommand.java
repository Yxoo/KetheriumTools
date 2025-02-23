package fr.yxoo;

import fr.yxoo.listeners.Configs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeterToolsCommand implements CommandExecutor {
    private final KeteriumTools plugin;
    private final Configs config;

    public KeterToolsCommand(KeteriumTools plugin, Configs config) {
        this.plugin = plugin;
        this.config = config;
    }

    private static final String[] [] placeholders =
            {
                    {"jexp_(job)%", "Affichage simple de l'exp actuel dans le job (sans virgules)."},
                    {"progress_(job)%", "Affichage en pourcentage de la progression actuel dans le job."}
            };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ketertools.admin")) {
            sender.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cInvalid argument.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "placeholders":
                handelPlaceholders(sender);
                break;
            default:
                sender.sendMessage("§cCommande inconnue. Utilisation: /ketertools <reload|update>");
                break;
        }

        return true;
    }

    private void handleReload(CommandSender sender) {
        try {
            plugin.onDisable();
            plugin.reloadConfig();
            plugin.reloadConfig();
            plugin.onEnable();
            if (plugin.getConfig().getBoolean(config.reloadPath))
                sender.sendMessage(plugin.getConfig().getString(config.reloadMessagePath));
        } catch (Exception e) {
            sender.sendMessage("§cUne erreur est survenue lors du rechargement du plugin.");
            e.printStackTrace();
        }
    }

    private void handelPlaceholders(CommandSender sender) {
        String key = "%keter_";

        StringBuilder message = new StringBuilder()
                .append("§a=== KeterTools Placeholders ===\n");
        for (int i = 0; i < placeholders.length; i++)
        {
            message.append("§a" + key + placeholders[i] [0].toString() + " §6➜ §f" + placeholders[i] [1].toString() + "\n");
        }
        message.append("§a===============================\n");;

        sender.sendMessage(message.toString());
    }

}