package cn.mooncookie.rushmode1058.listeners;

import cn.mooncookie.rushmode1058.RushMode;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.NextEvent;
import com.andrei1058.bedwars.api.arena.generator.GeneratorType;
import com.andrei1058.bedwars.api.arena.generator.IGenerator;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.OreGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BedWarsListener implements Listener {
    public BedWarsListener() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        RushMode.getInstance().getBridgingMode().put(event.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        RushMode.getInstance().getBridgingMode().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onGameStart(GameStateChangeEvent event) {
        IArena arena = event.getArena();
        if (event.getNewState() == GameState.playing) {
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                arena.setNextEvent(NextEvent.BEDS_DESTROY);
                Iterator var2 = arena.getTeams().iterator();

                while (var2.hasNext()) {
                    ITeam team = (ITeam) var2.next();
                    team.getTeamUpgradeTiers().put("upgrade-forge", 3);
                    team.getTeamUpgradeTiers().put("upgrade-miner", 0);
                    team.addTeamEffect(PotionEffectType.getByName("SPEED"), 0, Integer.MAX_VALUE);
                    team.addTeamEffect(PotionEffectType.getByName("FAST_DIGGING"), 0, Integer.MAX_VALUE);
                    Iterator var4 = ((List) team.getGenerators().stream().filter((gx) -> {
                        return gx.getType() == GeneratorType.IRON;
                    }).collect(Collectors.toList())).iterator();

                    IGenerator g;
                    while (var4.hasNext()) {
                        g = (IGenerator) var4.next();
                        g.setAmount(RushMode.getInstance().ironAmount);
                        g.setDelay(RushMode.getInstance().ironDelay);
                        g.setSpawnLimit(RushMode.getInstance().ironSpawnLimit);
                    }

                    var4 = ((List) team.getGenerators().stream().filter((gx) -> {
                        return gx.getType() == GeneratorType.GOLD;
                    }).collect(Collectors.toList())).iterator();

                    while (var4.hasNext()) {
                        g = (IGenerator) var4.next();
                        g.setAmount(RushMode.getInstance().goldAmount);
                        g.setDelay(RushMode.getInstance().goldDelay);
                        g.setSpawnLimit(RushMode.getInstance().goldSpawnLimit);
                    }

                    var4 = arena.getConfig().getArenaLocations("Team." + team.getName() + ".Emerald").iterator();

                    while (var4.hasNext()) {
                        Location l = (Location) var4.next();
                        IGenerator gen = new OreGenerator(l, arena, GeneratorType.CUSTOM, team);
                        gen.setOre(new ItemStack(Material.EMERALD));
                        gen.setType(GeneratorType.EMERALD);
                        gen.setAmount(RushMode.getInstance().emeraldAmount);
                        gen.setDelay(RushMode.getInstance().emeraldDelay);
                        gen.setSpawnLimit(RushMode.getInstance().emeraldSpawnLimit);
                        team.getGenerators().add(gen);
                    }

                    if (!team.isBedDestroyed()) {
                        this.setProtectBlocks(arena, team, (Bed) team.getBed().getBlock().getState().getData(), team.getBed());
                    }
                }

            }, 5L);
            Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
                arena.getOreGenerators().forEach((iGenerator) -> {
                    if (iGenerator.getType() == GeneratorType.DIAMOND || iGenerator.getType() == GeneratorType.EMERALD) {
                        iGenerator.upgrade();
                        iGenerator.upgrade();
                    }

                });
            }, 100L);
        }

    }

    public void setProtectBlocks(IArena arena, ITeam team, Bed bed, Location bedLoc) {
        int deltaX = bed.getFacing().getModX();
        int deltaZ = bed.getFacing().getModZ();
        if (bed.isHeadOfBed()) {
            bedLoc.subtract(deltaX, 0.0, deltaZ);
        }

        ArrayList<Location> woods = new ArrayList();
        ArrayList<Location> wools = new ArrayList();
        ArrayList<Location> glasses = new ArrayList();
        if (deltaX != 0) {
            woods = new ArrayList(Arrays.asList(bedLoc.clone().add(-deltaX, 0.0, 0.0), bedLoc.clone().add(0.0, 0.0, 1.0), bedLoc.clone().add(0.0, 0.0, -1.0), bedLoc.clone().add(0.0, 1.0, 0.0), bedLoc.clone().add(deltaX + deltaX, 0.0, 0.0), bedLoc.clone().add(deltaX, 0.0, 1.0), bedLoc.clone().add(deltaX, 0.0, -1.0), bedLoc.clone().add(deltaX, 1.0, 0.0)));
            wools = new ArrayList(Arrays.asList(bedLoc.clone().add(-deltaX * 2, 0.0, 0.0), bedLoc.clone().add(-deltaX, 1.0, 0.0), bedLoc.clone().add(-deltaX, 0.0, 1.0), bedLoc.clone().add(-deltaX, 0.0, -1.0), bedLoc.clone().add(0.0, 2.0, 0.0), bedLoc.clone().add(0.0, 1.0, 1.0), bedLoc.clone().add(0.0, 1.0, -1.0), bedLoc.clone().add(0.0, 0.0, 2.0), bedLoc.clone().add(0.0, 0.0, -2.0), bedLoc.clone().add(deltaX * 2 + deltaX, 0.0, 0.0), bedLoc.clone().add(deltaX + deltaX, 1.0, 0.0), bedLoc.clone().add(deltaX + deltaX, 0.0, 1.0), bedLoc.clone().add(deltaX + deltaX, 0.0, -1.0), bedLoc.clone().add(deltaX, 2.0, 0.0), bedLoc.clone().add(deltaX, 1.0, 1.0), bedLoc.clone().add(deltaX, 1.0, -1.0), bedLoc.clone().add(deltaX, 0.0, 2.0), bedLoc.clone().add(deltaX, 0.0, -2.0)));
            glasses = new ArrayList(Arrays.asList(bedLoc.clone().add(-deltaX * 3, 0.0, 0.0), bedLoc.clone().add(-deltaX * 2, 1.0, 0.0), bedLoc.clone().add(-deltaX * 2, 0.0, -1.0), bedLoc.clone().add(-deltaX * 2, 0.0, 1.0), bedLoc.clone().add(-deltaX, 1.0, 1.0), bedLoc.clone().add(-deltaX, 1.0, -1.0), bedLoc.clone().add(-deltaX, 0.0, 2.0), bedLoc.clone().add(-deltaX, 0.0, -2.0), bedLoc.clone().add(-deltaX, 2.0, 0.0), bedLoc.clone().add(0.0, 0.0, -3.0), bedLoc.clone().add(0.0, 0.0, 3.0), bedLoc.clone().add(0.0, 1.0, 2.0), bedLoc.clone().add(0.0, 1.0, -2.0), bedLoc.clone().add(0.0, 2.0, 1.0), bedLoc.clone().add(0.0, 2.0, -1.0), bedLoc.clone().add(0.0, 3.0, 0.0), bedLoc.clone().add(deltaX * 3 + deltaX, 0.0, 0.0), bedLoc.clone().add(deltaX * 2 + deltaX, 1.0, 0.0), bedLoc.clone().add(deltaX * 2 + deltaX, 0.0, -1.0), bedLoc.clone().add(deltaX * 2 + deltaX, 0.0, 1.0), bedLoc.clone().add(deltaX + deltaX, 1.0, 1.0), bedLoc.clone().add(deltaX + deltaX, 1.0, -1.0), bedLoc.clone().add(deltaX + deltaX, 0.0, 2.0), bedLoc.clone().add(deltaX + deltaX, 0.0, -2.0), bedLoc.clone().add(deltaX + deltaX, 2.0, 0.0), bedLoc.clone().add(deltaX, 0.0, -3.0), bedLoc.clone().add(deltaX, 0.0, 3.0), bedLoc.clone().add(deltaX, 1.0, 2.0), bedLoc.clone().add(deltaX, 1.0, -2.0), bedLoc.clone().add(deltaX, 2.0, 1.0), bedLoc.clone().add(deltaX, 2.0, -1.0), bedLoc.clone().add(deltaX, 3.0, 0.0)));
        } else if (deltaZ != 0) {
            woods = new ArrayList(Arrays.asList(bedLoc.clone().add(0.0, 0.0, -deltaZ), bedLoc.clone().add(1.0, 0.0, 0.0), bedLoc.clone().add(-1.0, 0.0, 0.0), bedLoc.clone().add(0.0, 1.0, 0.0), bedLoc.clone().add(0.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(1.0, 0.0, deltaZ), bedLoc.clone().add(-1.0, 0.0, deltaZ), bedLoc.clone().add(0.0, 1.0, deltaZ)));
            wools = new ArrayList(Arrays.asList(bedLoc.clone().add(0.0, 0.0, -deltaZ * 2), bedLoc.clone().add(0.0, 1.0, -deltaZ), bedLoc.clone().add(1.0, 0.0, -deltaZ), bedLoc.clone().add(-1.0, 0.0, -deltaZ), bedLoc.clone().add(0.0, 2.0, 0.0), bedLoc.clone().add(1.0, 1.0, 0.0), bedLoc.clone().add(-1.0, 1.0, 0.0), bedLoc.clone().add(2.0, 0.0, 0.0), bedLoc.clone().add(-2.0, 0.0, 0.0), bedLoc.clone().add(0.0, 0.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(0.0, 1.0, deltaZ + deltaZ), bedLoc.clone().add(1.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(-1.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(0.0, 2.0, deltaZ), bedLoc.clone().add(1.0, 1.0, deltaZ), bedLoc.clone().add(-1.0, 1.0, deltaZ), bedLoc.clone().add(2.0, 0.0, deltaZ), bedLoc.clone().add(-2.0, 0.0, deltaZ)));
            glasses = new ArrayList(Arrays.asList(bedLoc.clone().add(0.0, 0.0, -deltaZ * 3), bedLoc.clone().add(0.0, 1.0, -deltaZ * 2), bedLoc.clone().add(-1.0, 0.0, -deltaZ * 2), bedLoc.clone().add(1.0, 0.0, -deltaZ * 2), bedLoc.clone().add(1.0, 1.0, -deltaZ), bedLoc.clone().add(-1.0, 1.0, -deltaZ), bedLoc.clone().add(2.0, 0.0, -deltaZ), bedLoc.clone().add(-2.0, 0.0, -deltaZ), bedLoc.clone().add(0.0, 2.0, -deltaZ), bedLoc.clone().add(-3.0, 0.0, 0.0), bedLoc.clone().add(3.0, 0.0, 0.0), bedLoc.clone().add(2.0, 1.0, 0.0), bedLoc.clone().add(-2.0, 1.0, 0.0), bedLoc.clone().add(1.0, 2.0, 0.0), bedLoc.clone().add(-1.0, 2.0, 0.0), bedLoc.clone().add(0.0, 3.0, 0.0), bedLoc.clone().add(0.0, 0.0, deltaZ * 3 + deltaZ), bedLoc.clone().add(0.0, 1.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(-1.0, 0.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(1.0, 0.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(1.0, 1.0, deltaZ + deltaZ), bedLoc.clone().add(-1.0, 1.0, deltaZ + deltaZ), bedLoc.clone().add(2.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(-2.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(0.0, 2.0, deltaZ + deltaZ), bedLoc.clone().add(-3.0, 0.0, deltaZ), bedLoc.clone().add(3.0, 0.0, deltaZ), bedLoc.clone().add(2.0, 1.0, deltaZ), bedLoc.clone().add(-2.0, 1.0, deltaZ), bedLoc.clone().add(1.0, 2.0, deltaZ), bedLoc.clone().add(-1.0, 2.0, deltaZ), bedLoc.clone().add(0.0, 3.0, deltaZ)));
        }

        woods.forEach((location) -> {
            if (location.getBlock().getType() == Material.AIR && !Misc.isBuildProtected(location, arena)) {
                location.getBlock().setType(Material.WOOD);
                arena.addPlacedBlock(location.getBlock());
            }

        });
        wools.forEach((location) -> {
            if (location.getBlock().getType() == Material.AIR && !Misc.isBuildProtected(location, arena)) {
                location.getBlock().setType(Material.WOOL);
                location.getBlock().setData(team.getColor().itemByte());
                arena.addPlacedBlock(location.getBlock());
            }

        });
        glasses.forEach((location) -> {
            if (location.getBlock().getType() == Material.AIR && !Misc.isBuildProtected(location, arena)) {
                location.getBlock().setType(Material.STAINED_GLASS);
                location.getBlock().setData(team.getColor().itemByte());
                arena.addPlacedBlock(location.getBlock());
            }

        });
    }
}
