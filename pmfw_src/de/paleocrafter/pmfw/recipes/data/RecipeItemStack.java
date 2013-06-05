package de.paleocrafter.pmfw.recipes.data;

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
        if (stack != null && is != null) {
            if (is.itemID == stack.itemID) {
                if (is.getItemDamage() == stack.getItemDamage()) {
                    if (is.hasTagCompound() || stack.hasTagCompound()) {
                        if (is.hasTagCompound() && stack.hasTagCompound()) {
                            return is.getTagCompound().equals(
                                    stack.getTagCompound());
                        }
                    } else {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public ItemStack getStack() {
        return stack;
    }
}