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
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameStartListener implements Listener {

    @EventHandler
    public void onGameStart(GameStateChangeEvent event) {
        IArena arena = event.getArena();
        if (event.getNewState() != GameState.playing) return;
        Bukkit.getScheduler().runTaskLater(RushMode.getInstance(), () -> {
            arena.setNextEvent(NextEvent.BEDS_DESTROY);
            arena.setNoRecordMap();
            arena.setDreamMode();

            List<ITeam> teams = arena.getTeams();
            for (ITeam team : teams) {
                team.getTeamUpgradeTiers().put("upgrade-forge-fourteams", 3);
                team.getTeamUpgradeTiers().put("upgrade-miner-fourteams", 0);
                team.getTeamUpgradeTiers().put("upgrade-forge-eightteams", 3);
                team.getTeamUpgradeTiers().put("upgrade-miner-eightteams", 0);
                team.addTeamEffect(PotionEffectType.SPEED, 0, Integer.MAX_VALUE);
                team.addTeamEffect(PotionEffectType.FAST_DIGGING, 0, Integer.MAX_VALUE);

                IGenerator gen = new OreGenerator(team.getGenerators().get(0).getLocation(), arena, GeneratorType.CUSTOM, team);
                gen.setOre(new ItemStack(Material.EMERALD));
                gen.setType(GeneratorType.EMERALD);
                gen.setAmount(Integer.parseInt("1"));
                gen.setDelay(Integer.parseInt("30"));
                gen.setSpawnLimit(Integer.parseInt("2"));
                team.getGenerators().add(gen);
                if (!team.isBedDestroyed()) {
                    this.setProtectBlocks(arena, team, (Bed) team.getBed().getBlock().getState().getData(), team.getBed());
                }
            }
            arena.getOreGenerators().forEach((iGenerator) -> {
                if (iGenerator.getType() == GeneratorType.DIAMOND || iGenerator.getType() == GeneratorType.EMERALD) {
                    iGenerator.upgrade();
                    iGenerator.upgrade();
                }
            });
        }, 5L);

    }

    public void setProtectBlocks(IArena arena, ITeam team, Bed bed, Location bedLoc) {
        int deltaX = bed.getFacing().getModX();
        int deltaZ = bed.getFacing().getModZ();
        if (bed.isHeadOfBed()) {
            bedLoc.subtract(deltaX, 0.0, deltaZ);
        }

        ArrayList<Location> woods = new ArrayList<>();
        ArrayList<Location> wools = new ArrayList<>();
        ArrayList<Location> glasses = new ArrayList<>();
        if (deltaX != 0) {
            woods = new ArrayList<>(Arrays.asList(bedLoc.clone().add(-deltaX, 0.0, 0.0), bedLoc.clone().add(0.0, 0.0, 1.0), bedLoc.clone().add(0.0, 0.0, -1.0), bedLoc.clone().add(0.0, 1.0, 0.0), bedLoc.clone().add(deltaX + deltaX, 0.0, 0.0), bedLoc.clone().add(deltaX, 0.0, 1.0), bedLoc.clone().add(deltaX, 0.0, -1.0), bedLoc.clone().add(deltaX, 1.0, 0.0)));
            wools = new ArrayList<>(Arrays.asList(bedLoc.clone().add(-deltaX * 2, 0.0, 0.0), bedLoc.clone().add(-deltaX, 1.0, 0.0), bedLoc.clone().add(-deltaX, 0.0, 1.0), bedLoc.clone().add(-deltaX, 0.0, -1.0), bedLoc.clone().add(0.0, 2.0, 0.0), bedLoc.clone().add(0.0, 1.0, 1.0), bedLoc.clone().add(0.0, 1.0, -1.0), bedLoc.clone().add(0.0, 0.0, 2.0), bedLoc.clone().add(0.0, 0.0, -2.0), bedLoc.clone().add(deltaX * 2 + deltaX, 0.0, 0.0), bedLoc.clone().add(deltaX + deltaX, 1.0, 0.0), bedLoc.clone().add(deltaX + deltaX, 0.0, 1.0), bedLoc.clone().add(deltaX + deltaX, 0.0, -1.0), bedLoc.clone().add(deltaX, 2.0, 0.0), bedLoc.clone().add(deltaX, 1.0, 1.0), bedLoc.clone().add(deltaX, 1.0, -1.0), bedLoc.clone().add(deltaX, 0.0, 2.0), bedLoc.clone().add(deltaX, 0.0, -2.0)));
            glasses = new ArrayList<>(Arrays.asList(bedLoc.clone().add(-deltaX * 3, 0.0, 0.0), bedLoc.clone().add(-deltaX * 2, 1.0, 0.0), bedLoc.clone().add(-deltaX * 2, 0.0, -1.0), bedLoc.clone().add(-deltaX * 2, 0.0, 1.0), bedLoc.clone().add(-deltaX, 1.0, 1.0), bedLoc.clone().add(-deltaX, 1.0, -1.0), bedLoc.clone().add(-deltaX, 0.0, 2.0), bedLoc.clone().add(-deltaX, 0.0, -2.0), bedLoc.clone().add(-deltaX, 2.0, 0.0), bedLoc.clone().add(0.0, 0.0, -3.0), bedLoc.clone().add(0.0, 0.0, 3.0), bedLoc.clone().add(0.0, 1.0, 2.0), bedLoc.clone().add(0.0, 1.0, -2.0), bedLoc.clone().add(0.0, 2.0, 1.0), bedLoc.clone().add(0.0, 2.0, -1.0), bedLoc.clone().add(0.0, 3.0, 0.0), bedLoc.clone().add(deltaX * 3 + deltaX, 0.0, 0.0), bedLoc.clone().add(deltaX * 2 + deltaX, 1.0, 0.0), bedLoc.clone().add(deltaX * 2 + deltaX, 0.0, -1.0), bedLoc.clone().add(deltaX * 2 + deltaX, 0.0, 1.0), bedLoc.clone().add(deltaX + deltaX, 1.0, 1.0), bedLoc.clone().add(deltaX + deltaX, 1.0, -1.0), bedLoc.clone().add(deltaX + deltaX, 0.0, 2.0), bedLoc.clone().add(deltaX + deltaX, 0.0, -2.0), bedLoc.clone().add(deltaX + deltaX, 2.0, 0.0), bedLoc.clone().add(deltaX, 0.0, -3.0), bedLoc.clone().add(deltaX, 0.0, 3.0), bedLoc.clone().add(deltaX, 1.0, 2.0), bedLoc.clone().add(deltaX, 1.0, -2.0), bedLoc.clone().add(deltaX, 2.0, 1.0), bedLoc.clone().add(deltaX, 2.0, -1.0), bedLoc.clone().add(deltaX, 3.0, 0.0)));
        } else if (deltaZ != 0) {
            woods = new ArrayList<>(Arrays.asList(bedLoc.clone().add(0.0, 0.0, -deltaZ), bedLoc.clone().add(1.0, 0.0, 0.0), bedLoc.clone().add(-1.0, 0.0, 0.0), bedLoc.clone().add(0.0, 1.0, 0.0), bedLoc.clone().add(0.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(1.0, 0.0, deltaZ), bedLoc.clone().add(-1.0, 0.0, deltaZ), bedLoc.clone().add(0.0, 1.0, deltaZ)));
            wools = new ArrayList<>(Arrays.asList(bedLoc.clone().add(0.0, 0.0, -deltaZ * 2), bedLoc.clone().add(0.0, 1.0, -deltaZ), bedLoc.clone().add(1.0, 0.0, -deltaZ), bedLoc.clone().add(-1.0, 0.0, -deltaZ), bedLoc.clone().add(0.0, 2.0, 0.0), bedLoc.clone().add(1.0, 1.0, 0.0), bedLoc.clone().add(-1.0, 1.0, 0.0), bedLoc.clone().add(2.0, 0.0, 0.0), bedLoc.clone().add(-2.0, 0.0, 0.0), bedLoc.clone().add(0.0, 0.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(0.0, 1.0, deltaZ + deltaZ), bedLoc.clone().add(1.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(-1.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(0.0, 2.0, deltaZ), bedLoc.clone().add(1.0, 1.0, deltaZ), bedLoc.clone().add(-1.0, 1.0, deltaZ), bedLoc.clone().add(2.0, 0.0, deltaZ), bedLoc.clone().add(-2.0, 0.0, deltaZ)));
            glasses = new ArrayList<>(Arrays.asList(bedLoc.clone().add(0.0, 0.0, -deltaZ * 3), bedLoc.clone().add(0.0, 1.0, -deltaZ * 2), bedLoc.clone().add(-1.0, 0.0, -deltaZ * 2), bedLoc.clone().add(1.0, 0.0, -deltaZ * 2), bedLoc.clone().add(1.0, 1.0, -deltaZ), bedLoc.clone().add(-1.0, 1.0, -deltaZ), bedLoc.clone().add(2.0, 0.0, -deltaZ), bedLoc.clone().add(-2.0, 0.0, -deltaZ), bedLoc.clone().add(0.0, 2.0, -deltaZ), bedLoc.clone().add(-3.0, 0.0, 0.0), bedLoc.clone().add(3.0, 0.0, 0.0), bedLoc.clone().add(2.0, 1.0, 0.0), bedLoc.clone().add(-2.0, 1.0, 0.0), bedLoc.clone().add(1.0, 2.0, 0.0), bedLoc.clone().add(-1.0, 2.0, 0.0), bedLoc.clone().add(0.0, 3.0, 0.0), bedLoc.clone().add(0.0, 0.0, deltaZ * 3 + deltaZ), bedLoc.clone().add(0.0, 1.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(-1.0, 0.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(1.0, 0.0, deltaZ * 2 + deltaZ), bedLoc.clone().add(1.0, 1.0, deltaZ + deltaZ), bedLoc.clone().add(-1.0, 1.0, deltaZ + deltaZ), bedLoc.clone().add(2.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(-2.0, 0.0, deltaZ + deltaZ), bedLoc.clone().add(0.0, 2.0, deltaZ + deltaZ), bedLoc.clone().add(-3.0, 0.0, deltaZ), bedLoc.clone().add(3.0, 0.0, deltaZ), bedLoc.clone().add(2.0, 1.0, deltaZ), bedLoc.clone().add(-2.0, 1.0, deltaZ), bedLoc.clone().add(1.0, 2.0, deltaZ), bedLoc.clone().add(-1.0, 2.0, deltaZ), bedLoc.clone().add(0.0, 3.0, deltaZ)));
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
