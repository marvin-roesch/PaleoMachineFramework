package de.paleocrafter.pmfw.recipes;

import java.util.Arrays;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.paleocrafter.pmfw.recipes.data.MultiOutput;
import de.paleocrafter.pmfw.recipes.data.RecipeData;
import de.paleocrafter.pmfw.recipes.data.RecipeItemStack;

import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoMachineFramework
 * 
 * MultiOutputRecipes
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiOutputRecipes {
    private Table<RecipeItemStack, String, RecipeData> recipes;
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

        recipes = HashBasedTable.create();
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of arguments must match
     * the output amount given in the constructor. The chance for every output
     * will be 100(%).
     * 
     * @param input
     *            The input ItemStack of the recipe.
     * @param outputs
     *            An array of ItemStacks given when the input is correct.
     * @param additionalData
     *            An array of RecipeData objects. The data will be added among
     *            its keys to the recipe for later use. Can be null if you don't
     *            have additional data
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack input, ItemStack[] outputs,
            RecipeData[] additionalData) {
        int[] chances = new int[outputs.length];
        Arrays.fill(chances, 100);
        return addRecipe(input, outputs, chances, additionalData);
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
    public boolean addRecipe(ItemStack input, ItemStack[] outputs,
            int[] chances, RecipeData[] additionalData) {
        if (input != null) {
            RecipeItemStack recipeStack = new RecipeItemStack(input);
            if (outputs != null)
                if (outputs.length == outputAmount
                        && chances.length == outputAmount) {
                    recipes.put(recipeStack, "output", new RecipeData("output",
                            new MultiOutput(chances, outputs)));
                    if (additionalData != null && additionalData.length > 0)
                        for (RecipeData obj : additionalData) {
                            if (obj != null && !obj.getKey().equals("output"))
                                recipes.put(recipeStack, obj.getKey(), obj);
                        }
                    return true;
                }
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
        if (input != null) {
            RecipeItemStack key = new RecipeItemStack(input);
            if (recipes.containsRow(key)) {
                return (MultiOutput) recipes.row(key).get("output").getValue();
            }
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

    /**
     * 
     * Gets the additional value of the recipe for the given key and input.
     * 
     * @param input
     *            The input of the recipe to get the value for.
     * @param key
     *            The key of the value.
     * @return null, if the value or the recipe don't exist.
     */
    public Object getAdditionalValue(ItemStack input, String key) {
        if (input != null) {
            RecipeItemStack stack = new RecipeItemStack(input);
            if (recipes.containsRow(stack)) {
                if (key != null) {
                    if (recipes.containsColumn(key))
                        return recipes.row(stack).get(key).getValue();
                }
            }
        }
        return null;
    }
}
