package cn.mooncookie.rushmode1058.listeners;

import cn.mooncookie.rushmode1058.RushMode;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.shop.ShopBuyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class BuyWoolListener implements Listener {

    private final ArrayList<Player> boughtWoolPlayers = new ArrayList<>();

    @EventHandler
    public void onBuy(ShopBuyEvent e) {
        if (e.isCancelled()) return;
        Player p = e.getBuyer();
        if (boughtWoolPlayers.contains(p)) return;
        if (p.getInventory().contains(Material.WOOL)) {
            boughtWoolPlayers.add(p);
            p.sendMessage("§e§l你可以在手中拿着羊毛时左键点击，开启搭桥模式！");
        }
    }

    @EventHandler
    public void onGameStart(GameStateChangeEvent event) {
        if (event.getNewState() != GameState.playing) return;
        IArena arena = event.getArena();
        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            for (Player p : arena.getPlayers()) {
                if (boughtWoolPlayers.contains(p)) return;
                boughtWoolPlayers.add(p);
                p.sendMessage("§e§l你可以在手中拿着羊毛时左键点击，开启搭桥模式！");
            }
        }, 1200L);
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        IArena arena = event.getArena();
        for (Player p : arena.getPlayers()) {
            if (p == null) return;
            boughtWoolPlayers.remove(p);
        }
    }
}
