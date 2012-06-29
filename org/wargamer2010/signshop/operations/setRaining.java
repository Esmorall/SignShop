package org.wargamer2010.signshop.operations;

import org.bukkit.World;
import org.wargamer2010.signshop.player.SignShopPlayer;
import org.wargamer2010.signshop.SignShop;

public class setRaining implements SignShopOperation {    
    @Override
    public Boolean setupOperation(SignShopArguments ssArgs) {
        return true;
    }

    @Override
    public Boolean checkRequirements(SignShopArguments ssArgs, Boolean activeCheck) {
        World world = ssArgs.get_ssPlayer().getPlayer().getWorld();
        if(world.hasStorm() && world.isThundering()) {
            ssArgs.get_ssPlayer().sendMessage(SignShop.Errors.get("already_raining"));
            return false;
        }                
        return true;
    }
    
    @Override
    public Boolean runOperation(SignShopArguments ssArgs) {
        World world = ssArgs.get_ssPlayer().getPlayer().getWorld();
        world.setStorm(true);
        world.setThundering(true);

        SignShopPlayer.broadcastMsg(world,SignShop.Errors.get("made_rain").replace("!player",ssArgs.get_ssPlayer().getPlayer().getDisplayName()));
        return true;
    }
}
