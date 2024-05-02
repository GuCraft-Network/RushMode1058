package cn.mooncookie.rushmode1058.listeners;

import cn.mooncookie.rushmode1058.RushMode;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BridgeModeListener implements Listener {
    private final HashMap<Player, Boolean> bridgingMode = new HashMap<>();

    @EventHandler
    public void onQuit(PlayerLeaveArenaEvent event) {
        bridgingMode.remove(event.getPlayer());
    }

    @EventHandler
    public void onEnd(GameEndEvent event) {
        IArena arena = event.getArena();
        for (Player p : arena.getPlayers()) {
            if (p == null) return;
            bridgingMode.remove(p);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        IArena arena = Arena.getArenaByPlayer(player);
        if (arena == null) return;
        if (arena.getStatus() != GameState.playing) return;
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            if (event.getMaterial() == Material.WOOL) {
                if (bridgingMode.getOrDefault(player, false)) {
                    bridgingMode.replace(event.getPlayer(), false);
                    sendActionBar(event.getPlayer(), "§c§l搭桥模式已关闭");
                } else {
                    bridgingMode.replace(event.getPlayer(), true);
                    sendActionBar(event.getPlayer(), "§a§l搭桥模式已开启");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.WOOL) return;
        Player placePlayer = event.getPlayer();
        if (!bridgingMode.getOrDefault(placePlayer, false)) return;
        IArena arena = Arena.getArenaByPlayer(placePlayer);
        if (arena == null) return;
        BlockFace face = event.getBlockPlaced().getFace(event.getBlockAgainst());
        AtomicInteger distance = new AtomicInteger(1);
        Location playerLocation = placePlayer.getLocation();
        AtomicBoolean isTaskEnd = new AtomicBoolean(false);
        arena.getRegionsList().forEach((region) -> region.isInRegion(event.getBlockPlaced().getLocation()));
        World nextBlockWorld = event.getBlock().getWorld();

        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            if (event.getBlockPlaced().getLocation().getBlock().getType() == Material.AIR) {
                isTaskEnd.set(true);
                return;
            }

            Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
            for (Player player : nextBlockWorld.getPlayers()) {
                if (arena.isSpectator(player) || arena.isReSpawning(player)) return;
                Location feetLoc = player.getLocation();
                Location eyeLoc = player.getEyeLocation();
                Location checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                double feetDistance = feetLoc.distance(checkLoc);
                double eyeDistance = eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0));
                if (feetDistance <= 1.0 || eyeDistance <= 1.0) {
                    isTaskEnd.set(true);
                    break;
                }
            }
            if (!isTaskEnd.get()) {
                if (Misc.isBuildProtected(nextBlock, arena) || nextBlock.getBlock().getType() != Material.AIR) {
                    isTaskEnd.set(true);
                    return;
                }

                for (Player player : playerLocation.getWorld().getPlayers()) {
                    if (player.getLocation().distance(playerLocation) <= 12.0) {
                        this.playSound(player, playerLocation);
                    }
                }

                nextBlock.getBlock().setType(Material.WOOL);
                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                distance.addAndGet(1);
                arena.addPlacedBlock(nextBlock.getBlock());
            }
        }, 3L);
        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
            for (Player player : nextBlockWorld.getPlayers()) {
                if (arena.isSpectator(player) || arena.isReSpawning(player)) return;
                Location feetLoc = player.getLocation();
                Location eyeLoc = player.getEyeLocation();
                Location checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                double feetDistance = feetLoc.distance(checkLoc);
                double eyeDistance = eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0));
                if (feetDistance <= 1.0 || eyeDistance <= 1.0) {
                    isTaskEnd.set(true);
                    break;
                }
            }
            if (!isTaskEnd.get()) {
                if (Misc.isBuildProtected(nextBlock, arena) || nextBlock.getBlock().getType() != Material.AIR) {
                    isTaskEnd.set(true);
                    return;
                }

                for (Player player : playerLocation.getWorld().getPlayers()) {
                    if (player.getLocation().distance(playerLocation) <= 12.0) {
                        this.playSound(player, playerLocation);
                    }
                }

                nextBlock.getBlock().setType(Material.WOOL);
                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                distance.addAndGet(1);
                arena.addPlacedBlock(nextBlock.getBlock());
            }
        }, 5L);
        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
            for (Player player : nextBlockWorld.getPlayers()) {
                if (arena.isSpectator(player) || arena.isReSpawning(player)) return;
                Location feetLoc = player.getLocation();
                Location eyeLoc = player.getEyeLocation();
                Location checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                double feetDistance = feetLoc.distance(checkLoc);
                double eyeDistance = eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0));
                if (feetDistance <= 1.0 || eyeDistance <= 1.0) {
                    isTaskEnd.set(true);
                    break;
                }
            }
            if (!isTaskEnd.get()) {
                if (Misc.isBuildProtected(nextBlock, arena) || nextBlock.getBlock().getType() != Material.AIR) {
                    isTaskEnd.set(true);
                    return;
                }

                for (Player player : playerLocation.getWorld().getPlayers()) {
                    if (player.getLocation().distance(playerLocation) <= 12.0) {
                        this.playSound(player, playerLocation);
                    }
                }

                nextBlock.getBlock().setType(Material.WOOL);
                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                distance.addAndGet(1);
                arena.addPlacedBlock(nextBlock.getBlock());
            }
        }, 7L);
        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
            for (Player player : nextBlockWorld.getPlayers()) {
                if (arena.isSpectator(player) || arena.isReSpawning(player)) return;
                Location feetLoc = player.getLocation();
                Location eyeLoc = player.getEyeLocation();
                Location checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                double feetDistance = feetLoc.distance(checkLoc);
                double eyeDistance = eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0));
                if (feetDistance <= 1.0 || eyeDistance <= 1.0) {
                    isTaskEnd.set(true);
                    break;
                }
            }
            if (!isTaskEnd.get()) {
                if (Misc.isBuildProtected(nextBlock, arena) || nextBlock.getBlock().getType() != Material.AIR) {
                    isTaskEnd.set(true);
                    return;
                }

                for (Player player : playerLocation.getWorld().getPlayers()) {
                    if (player.getLocation().distance(playerLocation) <= 12.0) {
                        this.playSound(player, playerLocation);
                    }
                }

                nextBlock.getBlock().setType(Material.WOOL);
                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                distance.addAndGet(1);
                arena.addPlacedBlock(nextBlock.getBlock());
            }
        }, 9L);
        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
            for (Player player : nextBlockWorld.getPlayers()) {
                if (arena.isSpectator(player) || arena.isReSpawning(player)) return;
                Location feetLoc = player.getLocation();
                Location eyeLoc = player.getEyeLocation();
                Location checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                double feetDistance = feetLoc.distance(checkLoc);
                double eyeDistance = eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0));
                if (feetDistance <= 1.0 || eyeDistance <= 1.0) {
                    isTaskEnd.set(true);
                    break;
                }
            }
            if (!isTaskEnd.get()) {
                if (Misc.isBuildProtected(nextBlock, arena) || nextBlock.getBlock().getType() != Material.AIR) {
                    isTaskEnd.set(true);
                    return;
                }

                for (Player player : playerLocation.getWorld().getPlayers()) {
                    if (player.getLocation().distance(playerLocation) <= 12.0) {
                        this.playSound(player, playerLocation);
                    }
                }

                nextBlock.getBlock().setType(Material.WOOL);
                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                distance.addAndGet(1);
                arena.addPlacedBlock(nextBlock.getBlock());
            }
        }, 11L);
    }

    public void playSound(Player player, Location location) {
        PacketContainer container = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);
        container.getStrings().write(0, "dig.cloth");
        container.getIntegers().write(0, (int) (location.getX() * 8.0));
        container.getIntegers().write(1, (int) (location.getY() * 8.0));
        container.getIntegers().write(2, (int) (location.getZ() * 8.0));
        container.getFloat().write(0, 4.0F);
        container.getIntegers().write(3, 63);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
        }
    }

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

}

