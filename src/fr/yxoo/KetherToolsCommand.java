package fr.yxoo;

import fr.yxoo.listeners.Configs;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.libs.kyori.adventure.text.Component;
import me.clip.placeholderapi.libs.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class KetherToolsCommand implements CommandExecutor {
    private final KetheriumTools plugin;
    private final Configs config;
    private final DatabaseManager bdd;

    public KetherToolsCommand(KetheriumTools plugin, Configs config, DatabaseManager bdd) {
        this.plugin = plugin;
        this.config = config;
        this.bdd = bdd;
    }

    private static final String[] [] placeholders =
            {
                    {"jexp_(job)%", "Affichage simple de l'exp actuel dans le job (sans virgules)."},
                    {"jfexp_(job)%", "Affichage simple de l'exp actuel dans le job (formatté)."},
                    {"jlevel_max%", "Affichage simple du niveau le plus haut atteint dans les jobs."},
                    {"jfmaxexp_(job)%", "Affichage simple de l'exp requis dans le job (formatté)."},
                    {"progress_(job)%", "Affichage en pourcentage de la progression actuel dans le job."},
                    {"progressbar_(job)%", "Affichage en barre de progression l'exp du job."},
                    {"jtop_(job)_(number[1-15])%", "Renvois l'UUID du joueur"},
                    {"baltop_uuid_(number)%", "Renvois l'UUID du joueur a cette place dans le baltop."},
                    {"baltop_displayname_(number)%", "Renvois le displayname du joueur a cette place dans le baltop."},
                    {"hasreward_(job)_(reward number)%", "Renvois 'Yes' si le joueur a deja recuperer la récompense, sinon 'No'."},
                    {"dimensiontime_{world}_{target_name(placeholder)}%", "Temps restant dans le world."}

            };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kethertools.admin")) {
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
                handlePlaceholders(sender);
                break;
            case "reward":
                handleReward(sender, args);
                break;
            default:
                sender.sendMessage("§cCommande inconnue. Utilisation: /kethertools <reload>");
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
            if (plugin.getConfig().getString(config.reloadMessagePath).length() > 0)
                sender.sendMessage(plugin.getConfig().getString(config.reloadMessagePath).replace("&", "§"));
        } catch (Exception e) {
            sender.sendMessage("§cUne erreur est survenue lors du rechargement du plugin.");
            e.printStackTrace();
        }
    }

    private void handlePlaceholders(CommandSender sender) {
        String key = "%kether_";

        ComponentBuilder message = new ComponentBuilder("§a=== KetherTools Placeholders ===\n");

        for (int i = 0; i < placeholders.length; i++) {
            message.append(key + placeholders[i][0])
                    .color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, key + placeholders[i][0]))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Cliquez pour copier !").color(ChatColor.GRAY).create()))
                    .append(" ➜ ").color(ChatColor.GOLD)
                    .append(placeholders[i][1]).color(ChatColor.WHITE)
                    .append("\n").reset();
        }

        message.append("§a===============================");

        sender.spigot().sendMessage(message.create());
    }

    private void handleReward(CommandSender sender, String[] args) {
            String PlayerName = args[3];
            if (PlayerName.contains("%"))
                PlayerName = PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, args[3]);
            Player player = Bukkit.getPlayer(PlayerName);

            if (player == null) {
                KetheriumTools.logInfo("Invalid player.");
                return;
            }

            UUID playerUUID = player.getUniqueId();
            String job = args[1];
            int number = args.length > 2 ? Integer.parseInt(args[2]) : 1;
            FileConfiguration configuration = plugin.getConfig();

            List<String> jobs = plugin.getConfig().getStringList("jobs");
            if (jobs.contains(job)) {
                if (bdd.hasReward(playerUUID, job, number)) {
                    if (!configuration.getString("rewardFailed").isEmpty())
                        player.sendMessage(configuration.getString("rewardFailed"));
                } else {
                    bdd.addReward(playerUUID, job, number);
                    if (!configuration.getString("rewardSuccess").isEmpty())
                        player.sendMessage(configuration.getString("rewardSuccess"));
                }
            } else {
                player.sendMessage("Job invalide.");
            }
    }

}