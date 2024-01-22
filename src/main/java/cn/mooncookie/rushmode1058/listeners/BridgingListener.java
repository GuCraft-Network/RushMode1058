package cn.mooncookie.rushmode1058.listeners;

import cn.mooncookie.rushmode1058.RushMode;
import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BridgingListener implements Listener {
    public BridgingListener() {
    }

    public void playSound(Player player, Location location) {
        PacketContainer container = ProtocolLibrary.getProtocolManager().createPacket(Server.NAMED_SOUND_EFFECT);
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        BedWars bedwarsAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        if (event.getBlockPlaced().getType() == Material.WOOL && bedwarsAPI.getArenaUtil().isPlaying(event.getPlayer()) && RushMode.getInstance().getBridgingMode().get(event.getPlayer().getUniqueId())) {
            IArena arena = bedwarsAPI.getArenaUtil().getArenaByPlayer(event.getPlayer());
            BlockFace face = event.getBlockPlaced().getFace(event.getBlockAgainst());
            AtomicInteger distance = new AtomicInteger(1);
            Location playerLocation = event.getPlayer().getLocation();
            AtomicBoolean isTaskEnd = new AtomicBoolean(false);
            arena.getRegionsList().forEach((region) -> {
                region.isInRegion(event.getBlockPlaced().getLocation());
            });
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                if (event.getBlockPlaced().getLocation().getBlock().getType() == Material.AIR) {
                    isTaskEnd.set(true);
                }

                Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                Iterator var8 = nextBlock.getWorld().getEntitiesByClass(Player.class).iterator();

                while (true) {
                    Location feetLoc;
                    Location eyeLoc;
                    Location checkLoc;
                    do {
                        Player player;
                        if (!var8.hasNext()) {
                            if (nextBlock.getBlock().getType() != Material.AIR || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < (double) arena.getIslandRadius()) {
                                isTaskEnd.set(true);
                            }

                            arena.getTeams().forEach((team) -> {
                                if (nextBlock.distance(team.getSpawn()) < (double) arena.getIslandRadius()) {
                                    isTaskEnd.set(true);
                                }

                            });
                            if (!isTaskEnd.get()) {
                                var8 = playerLocation.getWorld().getEntitiesByClass(Player.class).iterator();

                                while (var8.hasNext()) {
                                    player = (Player) var8.next();
                                    if (player.getLocation().distance(playerLocation) <= 4.0) {
                                        this.playSound(player, playerLocation);
                                    }
                                }

                                nextBlock.getBlock().setType(Material.WOOL);
                                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                distance.addAndGet(1);
                                arena.addPlacedBlock(nextBlock.getBlock());
                            }

                            return;
                        }

                        player = (Player) var8.next();
                        feetLoc = player.getLocation();
                        eyeLoc = player.getEyeLocation();
                        checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                    } while (!(feetLoc.distance(checkLoc) <= 1.0) && !(eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0)) <= 1.0));

                    isTaskEnd.set(true);
                }
            }, 3L);
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                Iterator var8 = nextBlock.getWorld().getEntitiesByClass(Player.class).iterator();

                while (true) {
                    Location feetLoc;
                    Location eyeLoc;
                    Location checkLoc;
                    do {
                        Player player;
                        if (!var8.hasNext()) {
                            if (nextBlock.getBlock().getType() != Material.AIR || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < (double) arena.getIslandRadius()) {
                                isTaskEnd.set(true);
                            }

                            if (!isTaskEnd.get()) {
                                var8 = playerLocation.getWorld().getEntitiesByClass(Player.class).iterator();

                                while (var8.hasNext()) {
                                    player = (Player) var8.next();
                                    if (player.getLocation().distance(playerLocation) <= 4.0) {
                                        this.playSound(player, playerLocation);
                                    }
                                }

                                nextBlock.getBlock().setType(Material.WOOL);
                                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                distance.addAndGet(1);
                                arena.addPlacedBlock(nextBlock.getBlock());
                            }

                            return;
                        }

                        player = (Player) var8.next();
                        feetLoc = player.getLocation();
                        eyeLoc = player.getEyeLocation();
                        checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                    } while (!(feetLoc.distance(checkLoc) <= 1.0) && !(eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0)) <= 1.0));

                    isTaskEnd.set(true);
                }
            }, 5L);
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                Iterator var8 = nextBlock.getWorld().getEntitiesByClass(Player.class).iterator();

                while (true) {
                    Location feetLoc;
                    Location eyeLoc;
                    Location checkLoc;
                    do {
                        Player player;
                        if (!var8.hasNext()) {
                            if (nextBlock.getBlock().getType() != Material.AIR || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < (double) arena.getIslandRadius()) {
                                isTaskEnd.set(true);
                            }

                            if (!isTaskEnd.get()) {
                                var8 = playerLocation.getWorld().getEntitiesByClass(Player.class).iterator();

                                while (var8.hasNext()) {
                                    player = (Player) var8.next();
                                    if (player.getLocation().distance(playerLocation) <= 4.0) {
                                        this.playSound(player, playerLocation);
                                    }
                                }

                                nextBlock.getBlock().setType(Material.WOOL);
                                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                distance.addAndGet(1);
                                arena.addPlacedBlock(nextBlock.getBlock());
                            }

                            return;
                        }

                        player = (Player) var8.next();
                        feetLoc = player.getLocation();
                        eyeLoc = player.getEyeLocation();
                        checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                    } while (!(feetLoc.distance(checkLoc) <= 1.0) && !(eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0)) <= 1.0));

                    isTaskEnd.set(true);
                }
            }, 7L);
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                Iterator var8 = nextBlock.getWorld().getEntitiesByClass(Player.class).iterator();

                while (true) {
                    Location feetLoc;
                    Location eyeLoc;
                    Location checkLoc;
                    do {
                        Player player;
                        if (!var8.hasNext()) {
                            if (nextBlock.getBlock().getType() != Material.AIR || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < (double) arena.getIslandRadius()) {
                                isTaskEnd.set(true);
                            }

                            if (!isTaskEnd.get()) {
                                var8 = playerLocation.getWorld().getEntitiesByClass(Player.class).iterator();

                                while (var8.hasNext()) {
                                    player = (Player) var8.next();
                                    if (player.getLocation().distance(playerLocation) <= 4.0) {
                                        this.playSound(player, playerLocation);
                                    }
                                }

                                nextBlock.getBlock().setType(Material.WOOL);
                                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                distance.addAndGet(1);
                                arena.addPlacedBlock(nextBlock.getBlock());
                            }

                            return;
                        }

                        player = (Player) var8.next();
                        feetLoc = player.getLocation();
                        eyeLoc = player.getEyeLocation();
                        checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                    } while (!(feetLoc.distance(checkLoc) <= 1.0) && !(eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0)) <= 1.0));

                    isTaskEnd.set(true);
                }
            }, 9L);
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                Location nextBlock = event.getBlockPlaced().getLocation().add(-face.getModX() * distance.get(), -face.getModY() * distance.get(), -face.getModZ() * distance.get());
                Iterator var8 = nextBlock.getWorld().getEntitiesByClass(Player.class).iterator();

                while (true) {
                    Location feetLoc;
                    Location eyeLoc;
                    Location checkLoc;
                    do {
                        Player player;
                        if (!var8.hasNext()) {
                            if (nextBlock.getBlock().getType() != Material.AIR || nextBlock.distance(arena.getTeam(event.getPlayer()).getSpawn()) < (double) arena.getIslandRadius()) {
                                isTaskEnd.set(true);
                            }

                            if (!isTaskEnd.get()) {
                                var8 = playerLocation.getWorld().getEntitiesByClass(Player.class).iterator();

                                while (var8.hasNext()) {
                                    player = (Player) var8.next();
                                    if (player.getLocation().distance(playerLocation) <= 4.0) {
                                        this.playSound(player, playerLocation);
                                    }
                                }

                                nextBlock.getBlock().setType(Material.WOOL);
                                nextBlock.getBlock().setData(event.getBlockPlaced().getData());
                                distance.addAndGet(1);
                                arena.addPlacedBlock(nextBlock.getBlock());
                            }

                            return;
                        }

                        player = (Player) var8.next();
                        feetLoc = player.getLocation();
                        eyeLoc = player.getEyeLocation();
                        checkLoc = nextBlock.clone().add(0.5, 0.0, 0.5);
                    } while (!(feetLoc.distance(checkLoc) <= 1.0) && !(eyeLoc.distance(checkLoc.add(0.0, 1.0, 0.0)) <= 1.0));

                    isTaskEnd.set(true);
                }
            }, 11L);
        }
    }
}