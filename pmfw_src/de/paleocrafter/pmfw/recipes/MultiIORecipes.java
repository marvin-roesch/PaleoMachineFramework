package de.paleocrafter.pmfw.recipes;

import java.util.Arrays;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.paleocrafter.pmfw.recipes.data.MultiInput;
import de.paleocrafter.pmfw.recipes.data.MultiOutput;
import de.paleocrafter.pmfw.recipes.data.RecipeData;

import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoMachineFramework
 * 
 * MultiIORecipes
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiIORecipes {
    private Table<MultiInput, String, RecipeData> recipes;
    private int inputAmount;
    private int outputAmount;
    private boolean shapeless;

    /**
     * 
     * Creates a new instance of the MultiInputRecipes class.
     * 
     * @param inputAmount
     *            Determines, how many ItemStacks are used in all the recipes.
     * @param outputAmount
     *            Determines, how many ItemStacks every output of the recipe
     *            holds.
     * @param shapeless
     *            Determines, whether all the recipes are shapeless.
     */
    public MultiIORecipes(int inputAmount, int outputAmount, boolean shapeless) {
        this.inputAmount = inputAmount;
        this.outputAmount = outputAmount;
        this.shapeless = shapeless;

        recipes = HashBasedTable.create();
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of arguments must match
     * the output and input amount given in the constructor. The recipe won't
     * have additional data and the chance for every output will be 100(%).
     * 
     * @param inputs
     *            An array of ItemStacks needed to get the output.
     * @param outputs
     *            An array of ItemStacks given when the input is correct.
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack[] outputs) {
        return addRecipe(inputs, outputs, null);
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of arguments must match
     * the output and input amount given in the constructor. The chance for
     * every output will be 100(%).
     * 
     * @param inputs
     *            An array of ItemStacks needed to get the output.
     * @param outputs
     *            An array of ItemStacks given when the input is correct.
     * @param additionalData
     *            An array of RecipeData objects. The data will be added among
     *            its keys to the recipe for later use. Can be null if you don't
     *            have additional data
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack[] outputs,
            RecipeData[] additionalData) {
        int[] chances = new int[outputs.length];
        Arrays.fill(chances, 100);
        return addRecipe(inputs, outputs, chances, additionalData);
    }

    /**
     * 
     * Adds a recipe to the internal list. The amount of arguments must match
     * the output and input amount given in the constructor.
     * 
     * @param input
     *            An array of ItemStacks needed to get the output.
     * @param outputs
     *            An array of ItemStacks given when the input is correct.
     * @param chances
     *            An array of Integers for the percentages of getting the
     *            matching certain item
     * @param additionalData
     *            An array of RecipeData objects. The data will be added among
     *            its keys to the recipe for later use. Can be null if you don't
     *            have additional data
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack[] outputs,
            int[] chances, RecipeData[] additionalData) {
        if (inputs != null)
            if (outputs != null)
                if (outputs.length == outputAmount
                        && inputs.length == inputAmount
                        && chances.length == outputAmount) {
                    MultiInput recipeInput = new MultiInput(shapeless, inputs);
                    recipes.put(recipeInput, "output", new RecipeData("output",
                            new MultiOutput(chances, outputs)));
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
     * Gets the result for the given inputs.
     * 
     * @param inputs
     *            An array of ItemStacks used as inputs. Can also be a list of
     *            infinite arguments (like: ItemStack input1, ItemStack input2
     *            ...)
     * @return The outputs resulting from the recipe, null if the recipe doesn't
     *         exist.
     */
    public MultiOutput getResult(ItemStack... inputs) {
        if (inputs != null && inputs.length == inputAmount) {
            MultiInput key = new MultiInput(shapeless, inputs);
            if (recipes.containsRow(key)) {
                return (MultiOutput) recipes.row(key).get("output").getValue();
            }
        }
        return null;
    }

    /**
     * 
     * Checks if the given inputs are part of a recipe.
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
