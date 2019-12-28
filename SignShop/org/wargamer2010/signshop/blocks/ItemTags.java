package org.wargamer2010.signshop.blocks;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemTags implements IItemTags {
    @Override
    public ItemStack copyTags(ItemStack from, ItemStack to) {
        if(from == null || to == null || from.getItemMeta() == null)
            return to;
        to.setItemMeta(from.getItemMeta().clone());
        return to;
    }

	@Override
    public ItemStack getCraftItemstack(Material mat, Integer amount, Short damage) {
		ItemStack item = new ItemStack(mat, amount);
		if(item.getItemMeta() instanceof Damageable) {
			((Damageable)item.getItemMeta()).setDamage(damage);
		}
        return item;
    }
}
