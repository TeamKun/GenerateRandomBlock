package net.kunmc.lab.generaterandomblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public final class GenerateRandomBlock extends JavaPlugin implements Listener {
    public Map<Player, Block> playerBlockMap = new HashMap<>();
    public List<Material> blockList = new ArrayList<>();

    @Override
    public void onEnable() {
        //ブロックリストの作成
        Material[] materials = Material.values();
        for (Material material : materials) {
            if (material.isBlock() && !material.isAir()) {
                if(material != Material.AIR && material != Material.CAVE_AIR && material != Material.VOID_AIR){
                    blockList.add(material);
                }
            }
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    public double solidDistance(Location l1,Location l2){
        double p1x = l1.getX() , p1y = l1.getY() , p1z = l1.getZ();
        double p2x = l2.getX() , p2y = l2.getY() , p2z = l2.getZ();
        return Math.sqrt(Math.pow(p2x-p1x,2)+Math.pow(p2y-p1y,2)+Math.pow(p2z-p1z,2));
    }

    @EventHandler
    public void onShowBlock(PlayerMoveEvent e){
        try{
            Player p = e.getPlayer();

            RayTraceResult result = p.rayTraceBlocks(16);

            Block hitBlock = result.getHitBlock();
            Block changeBlock = hitBlock.getRelative(result.getHitBlockFace());

            if(hitBlock.getType().isAir()) return;
            if(!changeBlock.getType().isAir()) return;

            if(solidDistance(p.getLocation(),changeBlock.getLocation()) < 1) return;

            if(!playerBlockMap.containsKey(p)){
                Random r = new Random();
                Material m = blockList.get(r.nextInt(blockList.size()));
                changeBlock.setType(m);
                playerBlockMap.put(p,changeBlock);
            }else{
                if(changeBlock.getLocation() == playerBlockMap.get(p).getLocation()) return;
                Material m = playerBlockMap.get(p).getType();
                changeBlock.setType(m);
            }


        } catch(NullPointerException ignored){
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Random r = new Random();
        Material material = blockList.get(r.nextInt(blockList.size()));
        Block block = playerBlockMap.get(player);
        block.setType(material);
        playerBlockMap.put(player,block);
    }

}
