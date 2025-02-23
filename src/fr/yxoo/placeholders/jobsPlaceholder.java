package fr.yxoo.placeholders;

import fr.yxoo.KeteriumTools;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class jobsPlaceholder extends PlaceholderExpansion {


    @Override
    public String getIdentifier() {
        return "keter";
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

                    case "progress":

                        String ph1 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jexp_"+ keys[1]+"%");
                        String ph2 = PlaceholderAPI.setPlaceholders(player, "%jobsr_user_jmaxexp_"+ keys[1]+"%");

                        ph1 = ph1.replace(",", "");
                        ph2 = ph2.replace(",", "");
                        Float pourcent = Float.parseFloat(ph1) / Float.parseFloat(ph2) * 100;

                        return String.format("%1$.1f%2$s", pourcent, "%");

                    default:

                        KeteriumTools.logWarning(
                                "KeteriumTools : placeholder inconnu : " + identifier
                        );
                        return "%keter_" + identifier + "%";

                }

            }

        } catch (Exception e){

            KeteriumTools.logWarning(
                    "KeteriuemTools : erreur avec le placeholder : " + identifier +
                            " :: " + e.toString()
            );
            return "";

        }

        return "";

    }

}
