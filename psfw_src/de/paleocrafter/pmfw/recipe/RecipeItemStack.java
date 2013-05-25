package de.paleocrafter.pmfw.recipe;

import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoMachineFramework
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

        ItemStack is = ((RecipeItemStack) obj).getStack();
        if (is.itemID == stack.itemID)
            if (is.getItemDamage() == stack.getItemDamage())
                if (is.stackSize == stack.stackSize)
                    if (is.hasTagCompound() || stack.hasTagCompound()) {
                        if (is.hasTagCompound() && stack.hasTagCompound()) {
                            return is.getTagCompound().equals(
                                    stack.getTagCompound());
                        }
                    } else
                        return true;
        return false;
    }

    public ItemStack getStack() {
        return stack;
    }
}