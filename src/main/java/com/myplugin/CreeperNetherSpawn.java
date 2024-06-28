package com.myplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CreeperNetherSpawn extends JavaPlugin {

    private FileConfiguration config;
    private int spawnRadiusPerPlayer;
    private int spawnRadiusPerPlayerMin;
    private int spawnRadiusPerPlayerY;
    private Random random;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();

        spawnRadiusPerPlayer = config.getInt("creeper-spawn.spawn-radius-per-player");
        spawnRadiusPerPlayerMin = config.getInt("creeper-spawn.spawn-radius-per-player-min");
        spawnRadiusPerPlayerY = config.getInt("creeper-spawn.spawn-radius-per-player-y");

        random = new Random();

        Bukkit.getLogger().info("CreeperNetherSpawn plugin has been enabled.");
        startCreeperSpawnTask();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("CreeperNetherSpawn plugin has been disabled.");
    }

    public void startCreeperSpawnTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
                        Location loc = getRandomLocationNear(player.getLocation());
                        if (loc.getBlock().getType() == Material.AIR) {
                            player.getWorld().spawnEntity(loc, EntityType.CREEPER);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 600L);
    }

    public Location getRandomLocationNear(Location loc) {
        int x = loc.getBlockX() + random.nextInt(spawnRadiusPerPlayer) - spawnRadiusPerPlayerMin;
        int y = loc.getBlockY() + random.nextInt(spawnRadiusPerPlayerY) - (spawnRadiusPerPlayerY / 2);
        int z = loc.getBlockZ() + random.nextInt(spawnRadiusPerPlayer) - spawnRadiusPerPlayerMin;
        return new Location(loc.getWorld(), x, y, z);
    }
}
