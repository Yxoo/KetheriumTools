package fr.yxoo.placeholders;

import fr.yxoo.KetheriumTools;
import fr.yxoo.listeners.Configs;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class jobsPlaceholder extends PlaceholderExpansion {
    private static KetheriumTools plugin = null;
    private static Configs config = null;

    public jobsPlaceholder(KetheriumTools plugin, Configs config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public String getIdentifier() {
        return "kether";
    }

    @Override
    public String getAuthor() {
        return "Yxoo";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        try {
            String newIdentifier = identifier;
            Pattern pattern = Pattern.compile("^(\\w+)_\\{([^}]+)\\}_\\{([^}]+)\\}$");
            Matcher matcher = pattern.matcher(newIdentifier);

            if (matcher.matches()) {
                String command = matcher.group(1);
                String arg1 = matcher.group(2);
                String arg2 = matcher.group(3);

                switch (command) {
                    case "dimensiontime":

                        String key2Value = PlaceholderAPI.setPlaceholders(player, "%" + arg2 + "%");
                        String finalPlaceholder = "%dimensions_allowedTime_" + arg1 + "%";
                        Player targetPlayer = Bukkit.getPlayer(key2Value);
                        KetheriumTools.logInfo("%dimensions_allowedTime_" + arg1 + "% = " + finalPlaceholder + " || " + key2Value + " = " + targetPlayer);
                        if (targetPlayer != null) {
                            return PlaceholderAPI.setPlaceholders(targetPlayer, finalPlaceholder);
                        } else {
                            return "Player not found";
                        }
                    default:
                        KetheriumTools.logWarning("KetheriumTools : placeholder inconnu : " + newIdentifier);
                        return "%kether_" + newIdentifier + "%";
                }
            } else {
                String[] keys = identifier.split("_");
                if (keys.length == 2) {
                    switch (keys[0].toString()) {
                        case "jexp":
                            String ph = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_" + keys[0] + "_" + keys[1] + "%");
                            return ph.replace(",", "");
                        case "jfexp":
                            String exp = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jexp_" + keys[1] + "%");
                            exp = exp.replace(",", "");
                            double newexp = Double.parseDouble(exp);
                            return formatNumber(newexp);
                        case "jlevel":
                            if (keys[1].equals("max")){
                                List<String> jobs = plugin.getConfig().getStringList("jobs");

                                int size = jobs.size();
                                int[] levels = new int[size];

                                for (int i = 0; i < size; i++) {
                                    String job = jobs.get(i);
                                    String jlevel = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jlevel_" + job + "%");
                                    levels[i] = Integer.parseInt(jlevel);
                                }

                                Arrays.sort(levels);

                                return Integer.toString(levels[levels.length - 1]);
                            }
                        case "jfmaxexp":
                            String exp2 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jmaxexp_" + keys[1] + "%");
                            exp2 = exp2.replace(",", "");
                            double maxexp = Double.parseDouble(exp2);
                            return formatNumber(maxexp);
                        case "progress":
                            String ph1 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jexp_" + keys[1] + "%");
                            String ph2 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jmaxexp_" + keys[1] + "%");
                            ph1 = ph1.replace(",", "");
                            ph2 = ph2.replace(",", "");
                            Float pourcent = Float.parseFloat(ph1) / Float.parseFloat(ph2) * 100;
                            return String.format("%1$.1f%2$s", pourcent, "%");
                        case "progressbar":
                            String ph3 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jexp_" + keys[1] + "%");
                            String ph4 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jmaxexp_" + keys[1] + "%");
                            ph3 = ph3.replace(",", "");
                            ph4 = ph4.replace(",", "");
                            return createProgressBar(Double.parseDouble(ph3), Double.parseDouble(ph4));
                        default:
                            KetheriumTools.logWarning("KetheriumTools : placeholder inconnu : " + identifier);
                            return "%kether_" + identifier + "%";
                    }
                }
                if (keys.length == 3) {
                    switch (keys[0].toString()) {
                        case "jtop":
                            String playerName = PlaceholderAPI.setPlaceholders(player, "%jobsr_" + keys[0] + "_name_" + keys[1] + "_" + keys[2] + "%");
                            player = Bukkit.getPlayer(playerName);
                            if (player != null) {
                                return player.getUniqueId().toString();
                            } else {
                                return getUUIDFromNameAPI(playerName).toString();
                            }
                        case "baltop":
                            if (keys[1].equals("uuid"))
                            {
                                String playerName2 = PlaceholderAPI.setPlaceholders(player, "%essentials_" + keys[0] + "_player_" + keys[2] + "%");
                                player = Bukkit.getPlayer(playerName2);
                                if (player != null) {
                                    return player.getUniqueId().toString();
                                } else {
                                    return getUUIDFromNameAPI(playerName2).toString();
                                }
                            }
                            if (keys[1].equals("displayname")) {
                                // %kether_baltop_displayname_[nb]%
                                String baseDisplayname = PlaceholderAPI.setPlaceholders(player, "%ajlb_lb_vault_eco_balance_" + keys[2] + "_alltime_" + keys[1] + "%");
                                String[] splitted = baseDisplayname.split(":");

                                if (splitted.length > 1) {
                                    String rank = splitted[1];
                                    String pseudo = splitted[2];
                                    String imgRank = PlaceholderAPI.setPlaceholders(player, "%img_" + rank + "%");
                                    return imgRank + " " + pseudo;
                                } else {
                                    return handleSpecialRanks(player, baseDisplayname);
                                }

                            }
                        default:
                            KetheriumTools.logWarning("KetheriumTools : placeholder inconnu : " + identifier);
                            return "%kether_" + identifier + "%";
                    }
                }
            }
        } catch (Exception e) {
            KetheriumTools.logWarning("KetheriumTools : erreur avec le placeholder : " + identifier + " :: " + e.toString());
            return "";
        }
        return "";
    }

    public static String formatNumber(double number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000);
        }
        if (number >= 1000) {
            return String.format("%.1fK", number / 1000);
        }
        return String.valueOf(number);
    }
    private String handleSpecialRanks(Player player, String baseDisplayname) {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection ranksSection = config.getConfigurationSection("Ranks");

        if (ranksSection != null) {
            for (String rankKey : ranksSection.getKeys(false)) {
                ConfigurationSection rankConfig = ranksSection.getConfigurationSection(rankKey);
                String charValue = rankConfig.getString("char");
                String nameValue = rankConfig.getString("name");

                if (baseDisplayname.contains(charValue)) {
                    int index = baseDisplayname.indexOf(charValue);
                    String pseudo = baseDisplayname.substring(index + charValue.length()).trim();
                    String imgRank = PlaceholderAPI.setPlaceholders(player, "%img_" + nameValue + "%");
                    return imgRank + " " + pseudo;
                }
            }
        }
        return baseDisplayname;
    }

    public static UUID getUUIDFromNameAPI(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            reader.close();

            String uuidString = response.split("\"")[3];
            return UUID.fromString(uuidString.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createProgressBar(double current, double max) {
        int barLength = plugin.getConfig().getInt(config.progressBarSize);
        double progress = (double) current / max;
        int filledLength = (int) (progress * barLength);

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                bar.append(plugin.getConfig().getString(config.progressBarvalid));
            } else {
                bar.append(plugin.getConfig().getString(config.progressBarinvalid));
            }
        }
        return bar.toString();
    }
}
