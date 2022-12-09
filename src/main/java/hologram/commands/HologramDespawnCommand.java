package hologram.commands;

import hologram.Hologram;
import hologram.data.ObjectLoader;
import hologram.spawners.ObjectSpawner;
import hologram.utils.Vector3;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class HologramDespawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player");
            return false;
        }

        Player senderPlayer = (Player) sender;

        if (args.length == 0) {
            senderPlayer.sendMessage(ChatColor.RED+"Usage: /despawnobject id");
            return false;
        }

        String id = args[0].toLowerCase();

        if (!Hologram.getObjectSpawner().hasObjectById(id)) {
            senderPlayer.sendMessage(ChatColor.RED+"There is not such object loaded");
            return false;
        }

        Hologram.getObjectSpawner().despawnObject(id);
        senderPlayer.sendMessage("Despawned object with id: "+id);
        return false;
    }
}
