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

import java.util.*;

public final class GenerateRandomBlock extends JavaPlugin implements Listener {
    public Map<Player, Block> playerBlockMap = new HashMap<>();
    public Map<Player, Location> playerLocationMap = new HashMap<>();
    public List<Material> blockList ;

    @Override
    public void onEnable() {

        //ブロックリストの作成
        Material[] materials = Material.values();
        for (Material material : materials) {
            if (material.isBlock() && !material.isAir()) {
                blockList.add(material);
            }
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onShowBlock(PlayerMoveEvent e){
        Player player = e.getPlayer();
        try{
            Block block = player.rayTraceBlocks(16).getHitBlock();
            Block changeBlock = block.getRelative(player.rayTraceBlocks(16).getHitBlockFace());

            Material material;
            if(block.getType().isAir()) return;
            if(playerLocationMap.containsKey(player)){
                if(playerLocationMap.get(player)==block.getLocation()) return;
                material = playerBlockMap.get(player).getType();
                changeBlock.setType(material);
            }else{
                Random r = new Random();
                material = blockList.get(r.nextInt(blockList.size()));
                changeBlock.setType(material);
                playerBlockMap.put(player,changeBlock);
                playerLocationMap.put(player,changeBlock.getLocation());
            }
        } catch(NullPointerException exception){
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
