package cn.mooncookie.rushmode1058;

import cn.mooncookie.rushmode1058.listeners.BedWarsListener;
import cn.mooncookie.rushmode1058.listeners.BridgingListener;
import cn.mooncookie.rushmode1058.listeners.PlayerListener;
import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RushMode extends JavaPlugin {
    private static RushMode instance;
    private final Map<UUID, Boolean> bridgingMode = new HashMap();
    public BedWars BedWars;
    public BedWars bedWars;
    public List<String> groups;
    public String bridgeModeEnabled;
    public String bridgeModeDisable;
    public boolean sendMessageToChatList;
    public int ironAmount;
    public int ironDelay;
    public int ironSpawnLimit;
    public int goldAmount;
    public int goldDelay;
    public int goldSpawnLimit;
    public int emeraldAmount;
    public int emeraldDelay;
    public int emeraldSpawnLimit;

    public RushMode() {
    }

    public static RushMode getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.bedWars = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            this.bridgingMode.put(player.getUniqueId(), false);
        });
        this.BedWars = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        File folder = new File("plugins/BedWars1058/Addons/RushMode1058");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File configFile = new File(folder, "config.yml");
        if (!configFile.exists()) {
            try {
                Files.copy(this.getResource("config.yml"), configFile.toPath());
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        this.initConfig(configFile);
        this.getServer().getPluginManager().registerEvents(new BedWarsListener(), this);
        this.getServer().getPluginManager().registerEvents(new BridgingListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info(ChatColor.WHITE + "▬▬▬▬▬M0onCo0kie▬▬▬▬▬");
        getLogger().info(ChatColor.GREEN + "By YukiEnd Aka.羽末");
        getLogger().info(ChatColor.GREEN + "RushMode1058 插件已启用。");
        getLogger().info(ChatColor.WHITE + "▬▬▬▬▬M0onCo0kie▬▬▬▬▬");
    }

    public void onDisable() {
    }

    public void initConfig(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
        config.addDefault("generator.iron.amount", 5);
        config.addDefault("generator.iron.delay", 1);
        config.addDefault("generator.iron.spawnlimit", 64);
        config.addDefault("generator.gold.amount", 2);
        config.addDefault("generator.gold.delay", 3);
        config.addDefault("generator.gold.spawnlimit", 32);
        config.addDefault("generator.emerald.amount", 1);
        config.addDefault("generator.emerald.delay", 45);
        config.addDefault("generator.emerald.spawnlimit", 10);
        config.addDefault("messages.send-bridge-mode-message-to-chat-list", false);
        config.addDefault("messages.bridge-mode-enabled", "§a§l搭桥模式已开启");
        config.addDefault("messages.bridge-mode-disable", "§c§l搭桥模式已关闭");

        try {
            config.save(file);
        } catch (IOException var4) {
        }

        this.bridgeModeEnabled = config.getString("messages.bridge-mode-enabled");
        this.bridgeModeDisable = config.getString("messages.bridge-mode-disable");
        this.sendMessageToChatList = config.getBoolean("messages.send-bridge-mode-message-to-chat-list", false);
        this.ironAmount = config.getInt("generator.iron.amount", 3);
        this.ironDelay = config.getInt("generator.iron.delay", 1);
        this.ironSpawnLimit = config.getInt("generator.iron.spawnlimit", 48);
        this.goldAmount = config.getInt("generator.gold.amount", 1);
        this.goldDelay = config.getInt("generator.gold.delay", 3);
        this.goldSpawnLimit = config.getInt("generator.gold.spawnlimit", 8);
        this.emeraldAmount = config.getInt("generator.emerald.amount", 1);
        this.emeraldDelay = config.getInt("generator.emerald.delay", 45);
        this.emeraldSpawnLimit = config.getInt("generator.emerald.spawnlimit", 2);
    }

    public Map<UUID, Boolean> getBridgingMode() {
        return this.bridgingMode;
    }

    public boolean isMode(IArena arena) {
        return this.groups.contains(arena.getGroup().toLowerCase());
    }
}
