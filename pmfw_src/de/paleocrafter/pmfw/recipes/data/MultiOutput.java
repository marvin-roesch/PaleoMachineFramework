package de.paleocrafter.pmfw.recipes.data;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoMachineFramework
 * 
 * MultiOutput
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiOutput {
    private RecipeItemStack[] outputs;
    private int[] percentage;

    /**
     * 
     * Creates a new instance of the MultiInput class.
     * 
     * @param chances
     *            An array of Integers for the percentages of getting the
     *            certain item
     * @param outputs
     *            An array of ItemStacks given when the input is correct. Can
     *            also be a list of infinite arguments (like: ItemStack output1,
     *            ItemStack output2 ...)
     */
    public MultiOutput(int[] chances, ItemStack... outputs) {
        ArrayList<RecipeItemStack> temp = new ArrayList<RecipeItemStack>();
        for (ItemStack stack : outputs)
            temp.add(new RecipeItemStack(stack));
        this.outputs = temp.toArray(new RecipeItemStack[outputs.length]);
        this.percentage = chances;
    }

    /**
     * 
     * Returns an ItemStack for the given ID.
     * 
     * @param id
     *            The ID of the ItemStack
     * @return The ItemStack in the position of the given ID, null if it's out
     *         of bounds.
     */
    public ItemStack getOutputStack(int id) {
        if (id >= outputs.length)
            return null;
        return outputs[id].getStack();
    }

    /**
     * 
     * Returns a percentage for the given ID.
     * 
     * @param id
     *            The ID of the ItemStack to get the chance from
     * @return The percentage to get the ItemStack in the position of the given
     *         ID, zero if it's out of bounds.
     */
    public int getChance(int id) {
        if (id >= percentage.length)
            return 0;
        return percentage[id];
    }
}
