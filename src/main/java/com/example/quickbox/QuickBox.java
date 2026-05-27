package com.example.quickbox;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class QuickBox extends JavaPlugin implements CommandExecutor, TabCompleter, Listener {

    private final Map<Inventory, Inventory> enderMirrors = new HashMap<>();

    @Override
    public void onEnable() {
        registerCommand("enderchest", List.of("ec"));
        registerCommand("workbench", List.of("wb", "craft"));
        getServer().getPluginManager().registerEvents(this, this);
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
            if (!aliases.isEmpty()) {
                cmd.setAliases(aliases);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by a player.");
            return true;
        }

        String cmd = command.getName().toLowerCase();

        if (cmd.equals("workbench")) {
            return handleWorkbench(player);
        } else if (cmd.equals("enderchest")) {
            return handleEnderChest(player);
        }

        return false;
    }

    private boolean handleWorkbench(Player player) {
        if (!player.hasPermission("quickbox.workbench")) {
            player.sendMessage("§cYou don't have permission.");
            return true;
        }
        player.getScheduler().run(this, task -> player.openWorkbench(null, true), null);
        return true;
    }

    private boolean handleEnderChest(Player player) {
        if (!player.hasPermission("quickbox.enderchest")) {
            player.sendMessage("§cYou don't have permission.");
            return true;
        }
        player.getScheduler().run(this, task -> {
            Inventory source = player.getEnderChest();
            Component title = Component.text(player.getName() + " 的末影箱")
                    .color(NamedTextColor.DARK_PURPLE)
                    .decoration(TextDecoration.ITALIC, false);
            Inventory mirror = Bukkit.createInventory(null, 27, title);
            mirror.setContents(source.getContents());
            enderMirrors.put(mirror, source);
            player.openInventory(mirror);
        }, null);
        return true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Inventory source = enderMirrors.remove(inv);
        if (source != null) {
            source.setContents(inv.getContents());
        }
    }
}
