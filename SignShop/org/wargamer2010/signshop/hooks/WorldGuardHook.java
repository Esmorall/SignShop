package org.wargamer2010.signshop.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WorldGuardHook implements Hook  {

    @Override
    public String getName() {
        return "WorldGuard";
    }

    @Override
    public Boolean canBuild(Player player, Block block) {
        if(HookManager.getHook("WorldGuard") == null)
            return true;
        WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();
		RegionQuery query = platform.getRegionContainer().createQuery();
		return query.testBuild(BukkitAdapter.adapt(block.getLocation()), WorldGuardPlugin.inst().wrapPlayer(player));
    }

    @Override
    public Boolean protectBlock(Player player, Block block) {
        return false;
    }
}
