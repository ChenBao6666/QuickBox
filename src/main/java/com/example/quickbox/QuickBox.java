package com.example.quickbox;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class QuickBox extends JavaPlugin implements CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        registerCommand("enderchest", List.of("ec"));
        registerCommand("workbench", List.of("wb", "craft"));
        getLogger().info("QuickBox enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("QuickBox disabled!");
    }

    private void registerCommand(String name, List<String> aliases) {
        PluginCommand cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
            if (!aliases.isEmpty()) {
                cmd.setAliases(aliases);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("workbench")) {
            return handleWorkbench(sender);
        }

        if (cmd.equals("enderchest")) {
            return handleEnderChest(sender, args);
        }

        return false;
    }

    private boolean handleWorkbench(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by a player.");
            return true;
        }
        if (!sender.hasPermission("quickbox.workbench")) {
            sender.sendMessage("§cYou don't have permission.");
            return true;
        }

        player.getScheduler().run(this, task -> {
            player.openWorkbench(null, true);
        }, null);

        return true;
    }

    private boolean handleEnderChest(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by a player.");
            return true;
        }
        if (!sender.hasPermission("quickbox.enderchest")) {
            sender.sendMessage("§cYou don't have permission.");
            return true;
        }

        // Admin opening another player's ender chest
        if (args.length >= 1 && sender.hasPermission("quickbox.admin")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not online: " + args[0]);
                return true;
            }
            player.getScheduler().run(this, task -> {
                player.openInventory(target.getEnderChest());
            }, null);
            return true;
        }

        // Open own ender chest
        player.getScheduler().run(this, task -> {
            player.openInventory(player.getEnderChest());
        }, null);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("enderchest") && args.length == 1 && sender.hasPermission("quickbox.admin")) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                names.add(p.getName());
            }
            return filter(names, args[0]);
        }

        return Collections.emptyList();
    }

    private List<String> filter(List<String> options, String prefix) {
        List<String> result = new ArrayList<>();
        String lower = prefix.toLowerCase();
        for (String opt : options) {
            if (opt.toLowerCase().startsWith(lower)) {
                result.add(opt);
            }
        }
        return result;
    }
}
