package fr.yxoo.placeholders;

import fr.yxoo.KetheriumTools;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class jobsPlaceholder extends PlaceholderExpansion {


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
    public String onPlaceholderRequest(Player player, String identifier)
    {
        String[] keys = identifier.split("_");

        try {

            if (keys.length == 2){

                //demande d'un placeholder du type :: jbp_exp_Chasseur

                switch(keys[0].toString()){

                    case "jexp":

                        String ph = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_" + keys[0] + "_"+ keys[1]+"%");
                        return ph.replace(",", "");
                    case "jfexp":

                        String exp = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jexp_"+ keys[1]+"%");
                        exp = exp.replace(",", "");
                        double newexp = Double.parseDouble(exp);
                        return formatNumber(newexp);

                    case "jfmaxexp":

                        String exp2 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jmaxexp_"+ keys[1]+"%");
                        exp2 = exp2.replace(",", "");
                        double maxexp = Double.parseDouble(exp2);
                        return formatNumber(maxexp);

                    case "progress":

                        String ph1 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jexp_"+ keys[1]+"%");
                        String ph2 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jmaxexp_"+ keys[1]+"%");

                        ph1 = ph1.replace(",", "");
                        ph2 = ph2.replace(",", "");
                        Float pourcent = Float.parseFloat(ph1) / Float.parseFloat(ph2) * 100;

                        return String.format("%1$.1f%2$s", pourcent, "%");

                    default:

                        KetheriumTools.logWarning(
                                "KetheriumTools : placeholder inconnu : " + identifier
                        );
                        return "%kether_" + identifier + "%";

                }

            }
            if (keys.length == 3){
                //demande d'un placeholder du type :: kether_jtop_Chasseur_1

                if (keys[0].toString().equals("jtop")) {
                    String playerName = PlaceholderAPI.setPlaceholders(player, "%jobsr_" + keys[0] + "_name_" + keys[1] + "_" + keys[2] + "%");
                    player = Bukkit.getPlayer(playerName);
                    if (player != null) {
                        return player.getUniqueId().toString();
                    }
                    else {
                        return getUUIDFromNameAPI(playerName).toString();
                    }
                }
                else {
                    KetheriumTools.logWarning(
                            "KetheriumTools : placeholder inconnu : " + identifier
                    );
                    return "%kether_" + identifier + "%";
                }

            }

        } catch (Exception e){

            KetheriumTools.logWarning(
                    "KetheriumTools : erreur avec le placeholder : " + identifier +
                            " :: " + e.toString()
            );
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

    public static UUID getUUIDFromNameAPI(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            reader.close();

            // Exemple de réponse : {"id":"d2a1b5c3e4f5a6b7c8d9e0f1a2b3c4d5","name":"PlayerName"}
            String uuidString = response.split("\"")[3]; // Extrait l'UUID de la réponse JSON
            return UUID.fromString(uuidString.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
