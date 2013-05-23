package de.paleocrafter.multiio.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

/**
 * 
 * MMO Materials
 * 
 * MultiInput
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiInput {

    private boolean shapeless;
    private RecipeItemStack[] inputs;

    /**
     * 
     * Creates a new instance of the MultiInput class.
     * 
     * @param isShapeless
     *            Defines whether the recipe is shapeless or not.
     * @param inputs
     *            The input ItemStacks needed for the recipe. They need to be in
     *            the right order for a shaped recipe.
     */
    public MultiInput(boolean shapeless, ItemStack... inputs) {
        this.shapeless = shapeless;
        ArrayList<RecipeItemStack> temp = new ArrayList<RecipeItemStack>();
        for (ItemStack stack : inputs)
            temp.add(new RecipeItemStack(stack));
        this.inputs = temp.toArray(new RecipeItemStack[inputs.length]);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MultiInput) {
            MultiInput input = (MultiInput) obj;
            if (shapeless) {
                if (input.isShapeless())
                    if (inputs.length == input.getInputs().length)
                        return compareInputs(true, inputs, input.getInputs());
            } else {
                if (!input.isShapeless())
                    if (inputs.length == input.getInputs().length)
                        return compareInputs(false, inputs, input.getInputs());
            }
        }
        return false;
    }

    /**
     * 
     * Checks, if the inputs can be shapeless.
     * 
     * @return true, if the recipe is shapeless
     */
    public boolean isShapeless() {
        return shapeless;
    }

    /**
     * 
     * Returns an ItemStack array of all the inputs.
     * 
     * @return the ItemStack array of the inputs
     */
    public ItemStack[] getInputStacks() {
        ArrayList<ItemStack> temp = new ArrayList<ItemStack>();
        for (RecipeItemStack input : inputs)
            temp.add(input.getStack());
        return temp.toArray(new ItemStack[inputs.length]);
    }

    /**
     * 
     * Returns an RecipeItemStack array of all the inputs. Used for internals.
     * 
     * @return the RecipeItemStack array of the inputs
     */
    public RecipeItemStack[] getInputs() {
        return inputs;
    }

    private boolean compareInputs(boolean shapeless, RecipeItemStack[] inputs1,
            RecipeItemStack[] inputs2) {
        boolean valid = false;
        if (shapeless) {
            Set<RecipeItemStack> set1 = new HashSet<RecipeItemStack>(
                    Arrays.asList(inputs1));
            Set<RecipeItemStack> set2 = new HashSet<RecipeItemStack>(
                    Arrays.asList(inputs2));
            return set1.containsAll(set2);
        } else {
            for (int i = 0; i < inputs1.length; i++) {
                if (inputs1[i].equals(inputs2[i]))
                    valid = true;
                else
                    break;
            }
        }
        return valid;
    }
}
