package de.paleocrafter.multiio.recipe;

import net.minecraft.item.ItemStack;

/**
 * 
 * MMO Materials
 * 
 * RecipeItemStack
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class RecipeItemStack {
    private ItemStack stack;

    public RecipeItemStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RecipeItemStack))
            return false;
        return stack.isItemEqual(((RecipeItemStack) obj).getStack());
    }

    public ItemStack getStack() {
        return stack;
    }
}