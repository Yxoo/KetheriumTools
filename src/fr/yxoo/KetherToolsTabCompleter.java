package fr.yxoo;

import fr.yxoo.listeners.Configs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KetherToolsTabCompleter implements TabCompleter {
    private static KetheriumTools plugin = null;

    public KetherToolsTabCompleter(KetheriumTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("reload", "placeholders", "reward");
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reward")) {
            List<String> jobs = plugin.getConfig().getStringList("jobs");
            StringUtil.copyPartialMatches(args[1], jobs, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
