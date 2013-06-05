package de.paleocrafter.pmfw.recipes;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.paleocrafter.pmfw.recipes.data.MultiInput;
import de.paleocrafter.pmfw.recipes.data.RecipeData;
import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoMachineFramework
 * 
 * MultiInputRecipes
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiInputRecipes {
    private Table<MultiInput, String, RecipeData> recipes;
    private int inputAmount;
    private boolean shapeless;

    /**
     * 
     * Creates a new instance of the MultiInputRecipes class.
     * 
     * @param inputAmount
     *            Determines, how many ItemStacks are used in all the recipes.
     * @param shapeless
     *            Determines, whether all the recipes are shapeless.
     */
    public MultiInputRecipes(int inputAmount, boolean shapeless) {
        this.inputAmount = inputAmount;
        this.shapeless = shapeless;
        recipes = HashBasedTable.create();
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of items in the inputs
     * array must match the input amount given in the constructor
     * 
     * @param inputs
     *            An array of ItemStacks needed to get the output.
     * @param output
     *            The output ItemStack of the recipe.
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack output) {
        return addRecipe(inputs, output, null);
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of items in the inputs
     * array must match the input amount given in the constructor
     * 
     * @param inputs
     *            An array of ItemStacks needed to get the output.
     * @param output
     *            The output ItemStack of the recipe.
     * @param additionalData
     *            An array of RecipeData objects. The data will be added among
     *            its keys to the recipe for later use. Can be null if you don't
     *            have additional data
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack output,
            RecipeData[] additionalData) {
        if (inputs != null)
            if (output != null)
                if (!(inputs.length < inputAmount || inputs.length > inputAmount)) {
                    MultiInput recipeInput = new MultiInput(shapeless, inputs);
                    recipes.put(recipeInput, "output", new RecipeData("output",
                            output));
                    if (additionalData != null && additionalData.length > 0)
                        for (RecipeData obj : additionalData) {
                            if (obj != null && !obj.getKey().equals("output"))
                                recipes.put(recipeInput, obj.getKey(), obj);
                        }
                    return true;
                }
        return false;
    }

    /**
     * 
     * Gets the result for the given inputs. Can take infinite arguments. The
     * amount of arguments must match the input amount given in the constructor.
     * 
     * @param inputs
     *            An array of ItemStacks used as inputs. Can also be a list of
     *            infinite arguments (like: ItemStack input1, ItemStack input2
     *            ...)
     * @return The ItemStack resulting from the recipe, null if the recipe
     *         doesn't exist.
     */
    public ItemStack getResult(ItemStack... inputs) {
        if (inputs != null && inputs.length == inputAmount) {
            MultiInput key = new MultiInput(shapeless, inputs);
            if (recipes.containsRow(key)) {
                return (ItemStack) recipes.row(key).get("output").getValue();
            }
        }
        return null;
    }

    /**
     * 
     * Checks if the given inputs are part of a recipe. Can take infinite
     * arguments. The amount of arguments must match the input amount given in
     * the constructor.
     * 
     * @param inputs
     *            An array of ItemStacks used as inputs. Can also be a list of
     *            infinite arguments (like: ItemStack input1, ItemStack input2
     *            ...)
     * @return true, if the recipe is valid, otherwise false.
     */
    public boolean isRecipeValid(ItemStack... inputs) {
        return getResult(inputs) != null;
    }

    /**
     * 
     * Gets the additional value of the recipe for the given key and inputs.
     * 
     * @param inputs
     *            The inputs of the recipe to get the value for.
     * @param key
     *            The key of the value.
     * @return null, if the value or the recipe don't exist.
     */
    public Object getAdditionalValue(ItemStack[] inputs, String key) {
        if (inputs != null) {
            MultiInput stack = new MultiInput(shapeless, inputs);
            if (recipes.containsRow(stack)) {
                if (key != null) {
                    if (recipes.row(stack).containsKey(key))
                        return recipes.row(stack).get(key).getValue();
                }
            }
        }
        return null;
    }
}
