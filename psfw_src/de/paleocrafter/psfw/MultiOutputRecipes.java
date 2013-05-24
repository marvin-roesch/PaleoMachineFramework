package de.paleocrafter.psfw;

import java.util.Arrays;
import java.util.HashMap;

import de.paleocrafter.psfw.recipe.MultiOutput;
import de.paleocrafter.psfw.recipe.RecipeItemStack;

import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoSlotFramework
 * 
 * MultiOutputRecipes
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiOutputRecipes {
    private HashMap<RecipeItemStack, MultiOutput> recipes;
    private int outputAmount;

    /**
     * 
     * Creates a new instance of the MultiInputRecipes class.
     * 
     * @param outputAmount
     *            Determines, how many ItemStacks every output of the recipe
     *            holds.
     */
    public MultiOutputRecipes(int outputAmount) {
        this.outputAmount = outputAmount;

        recipes = new HashMap<RecipeItemStack, MultiOutput>();
    }

    /**
     * 
     * Adds a recipe to the internal list. Can take infinite arguments for the
     * output. The amount of arguments must match the output amount given in the
     * constructor.
     * 
     * @param input
     *            The input ItemStack of the recipe.
     * @param outputs
     *            An array of ItemStacks given when the input is correct. Can
     *            also be a list of infinite arguments (like: ItemStack output1,
     *            ItemStack output2 ...)
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack input, ItemStack... outputs) {
        if (!(outputs.length < outputAmount || outputs.length > outputAmount)) {
            int[] chances = new int[outputs.length];
            Arrays.fill(chances, 100);
            recipes.put(new RecipeItemStack(input), new MultiOutput(chances,
                    outputs));
            return true;
        }
        return false;
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of arguments for the
     * output and the chances have to match the output amount given in the
     * constructor.
     * 
     * @param input
     *            The input ItemStack of the recipe.
     * @param outputs
     *            An array of ItemStacks given when the input is correct.
     * @param chances
     *            An array of Integers for the percentages of getting the
     *            matching certain item
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack input, ItemStack[] outputs, int[] chances) {
        if (!(outputs.length < outputAmount || outputs.length > outputAmount)) {
            recipes.put(new RecipeItemStack(input), new MultiOutput(chances,
                    outputs));
            return true;
        }
        return false;
    }

    /**
     * 
     * Gets the result for the given input.
     * 
     * @param input
     *            The Input ItemStack to get the output.
     * @return The outputs resulting from the recipe, null if the recipe doesn't
     *         exist.
     */
    public MultiOutput getResult(ItemStack input) {
        RecipeItemStack key = new RecipeItemStack(input);
        if (recipes.containsKey(key)) {
            return recipes.get(key);
        }
        return null;
    }

    /**
     * 
     * Checks if the given input is part of a recipe.
     * 
     * @param inputs
     *            An ItemStack object which represents the input.
     * @return true, if the recipe is valid, otherwise false.
     */
    public boolean isRecipeValid(ItemStack input) {
        return getResult(input) != null;
    }
}
