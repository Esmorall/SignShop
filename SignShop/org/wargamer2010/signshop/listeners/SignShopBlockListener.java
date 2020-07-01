package org.wargamer2010.signshop.listeners;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;
import org.wargamer2010.signshop.Seller;
import org.wargamer2010.signshop.SignShop;
import org.wargamer2010.signshop.events.SSDestroyedEvent;
import org.wargamer2010.signshop.events.SSDestroyedEventType;
import org.wargamer2010.signshop.player.SignShopPlayer;
import org.wargamer2010.signshop.util.signshopUtil;

public class SignShopBlockListener implements Listener {

    private List<Block> getAttachables(Block from) {
        List<Block> attachables = new ArrayList<Block>();
        List<BlockFace> checkFaces = new ArrayList<BlockFace>();
        checkFaces.add(BlockFace.UP);
        checkFaces.add(BlockFace.NORTH);
        checkFaces.add(BlockFace.EAST);
        checkFaces.add(BlockFace.SOUTH);
        checkFaces.add(BlockFace.WEST);

        for(BlockFace face : checkFaces) {
            if(from.getRelative(face).getState().getData() instanceof Attachable) {
                Attachable att = (Attachable)from.getRelative(face).getState().getData();
                if(from.getRelative(face).getRelative(att.getAttachedFace()).equals(from))
                    attachables.add(from.getRelative(face));
            }
        }

        return attachables;
    }

    private boolean canBreakBlock(Block block, Player player, boolean recurseOverAttachables) {
        Map<Seller, SSDestroyedEventType> affectedSellers = signshopUtil.getRelatedShopsByBlock(block);
        SignShopPlayer ssPlayer = new SignShopPlayer(player);

        for(Map.Entry<Seller, SSDestroyedEventType> destroyal : affectedSellers.entrySet()) {
            SSDestroyedEvent event = new SSDestroyedEvent(block, ssPlayer, destroyal.getKey(), destroyal.getValue());
            SignShop.scheduleEvent(event);
            if(event.isCancelled())
                return false;
        }

        if(recurseOverAttachables) {
            for(Block attached : getAttachables(block)) {
                if(!canBreakBlock(attached, player, false))
                    return false;
            }
        }

        return true;
    }

    
    public static Material[] linkMaterials = new Material[]{
    		Material.CHEST,
    		Material.TRAPPED_CHEST,
    		Material.DROPPER,
    		Material.HOPPER,
    		Material.ANVIL,
    		Material.DISPENSER,
    		Material.FURNACE,
    		Material.BREWING_STAND,
    		Material.ENCHANTING_TABLE,
    		Material.LEVER,
    		Material.JUKEBOX,
    		Material.OAK_DOOR,
    		Material.OAK_SIGN,
    		Material.OAK_WALL_SIGN,
    		Material.DARK_OAK_DOOR,
    		Material.DARK_OAK_SIGN,
    		Material.DARK_OAK_WALL_SIGN,
    		Material.BIRCH_DOOR,
    		Material.BIRCH_SIGN,
    		Material.BIRCH_WALL_SIGN,
    		Material.SPRUCE_DOOR,
    		Material.SPRUCE_SIGN,
    		Material.SPRUCE_WALL_SIGN,
    		Material.JUNGLE_DOOR,
    		Material.JUNGLE_SIGN,
    		Material.JUNGLE_WALL_SIGN,
    		Material.ACACIA_DOOR,
    		Material.ACACIA_SIGN,
    		Material.ACACIA_WALL_SIGN,
    		Material.IRON_DOOR
    };
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        // We want to run at the Highest level so we can tell if other plugins cancelled the event
        // But we don't want to run at Monitor since we want to be able to cancel the event ourselves
        if(event.isCancelled()) {
        	return;
        }
        if(!Arrays.asList(linkMaterials).contains(event.getBlock().getType())) {
        	return;
        }
        if(!canBreakBlock(event.getBlock(), event.getPlayer(), true))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBurn(BlockBurnEvent event) {
        if(event.isCancelled())
            return;

        if(!canBreakBlock(event.getBlock(), null, true))
            event.setCancelled(true);
    }
}
