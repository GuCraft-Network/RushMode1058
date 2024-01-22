package cn.mooncookie.rushmode1058.listeners;

import cn.mooncookie.rushmode1058.RushMode;
import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.InvocationTargetException;

public class PlayerListener implements Listener {

    public void sendActionBar(Player player, String message) {
        PacketContainer container = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CHAT);
        container.getChatComponents().write(0, WrappedChatComponent.fromText(message));
        container.getBytes().write(0, (byte) 2);
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        BedWars bedwars = RushMode.getInstance().bedWars;
        final IArena arena = bedwars.getArenaUtil().getArenaByPlayer(player);
        if (arena != null) {
            // 模式切换逻辑
            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                if (event.getMaterial() == Material.WOOL) {
                    if (RushMode.getInstance().getBridgingMode().get(event.getPlayer().getUniqueId())) {
                        RushMode.getInstance().getBridgingMode().replace(event.getPlayer().getUniqueId(), false);
                        sendActionBar(event.getPlayer(), RushMode.getInstance().bridgeModeDisable);
                    } else {
                        RushMode.getInstance().getBridgingMode().replace(event.getPlayer().getUniqueId(), true);
                        sendActionBar(event.getPlayer(), RushMode.getInstance().bridgeModeEnabled);
                    }
                }
            }
        }
    }
}
