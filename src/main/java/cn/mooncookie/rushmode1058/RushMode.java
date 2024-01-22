package cn.mooncookie.rushmode1058;

import cn.mooncookie.rushmode1058.listeners.BridgeModeListener;
import cn.mooncookie.rushmode1058.listeners.BuyWoolListener;
import cn.mooncookie.rushmode1058.listeners.GameStartListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class RushMode extends JavaPlugin {
    private static RushMode instance;

    public static RushMode getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;


        this.getServer().getPluginManager().registerEvents(new GameStartListener(), this);
        this.getServer().getPluginManager().registerEvents(new BridgeModeListener(), this);
        this.getServer().getPluginManager().registerEvents(new BuyWoolListener(), this);
        getLogger().info(ChatColor.WHITE + "▬▬▬▬▬M0onCo0kie▬▬▬▬▬");
        getLogger().info(ChatColor.GREEN + "By YukiEnd Aka.羽末");
        getLogger().info(ChatColor.GREEN + "RushMode1058 插件已启用。");
        getLogger().info(ChatColor.WHITE + "▬▬▬▬▬M0onCo0kie▬▬▬▬▬");
    }

    public void onDisable() {
    }


}
