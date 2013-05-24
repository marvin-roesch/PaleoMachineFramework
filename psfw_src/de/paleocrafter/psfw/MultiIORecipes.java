package de.paleocrafter.psfw;

import java.util.Arrays;
import java.util.HashMap;

import de.paleocrafter.psfw.recipe.MultiInput;
import de.paleocrafter.psfw.recipe.MultiOutput;

import net.minecraft.item.ItemStack;

/**
 * 
 * PaleoSlotFramework
 * 
 * MultiIORecipes
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiIORecipes {
    private HashMap<MultiInput, MultiOutput> recipes;
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

        recipes = new HashMap<MultiInput, MultiOutput>();
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
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack[] outputs) {
        if (!(outputs.length < outputAmount || outputs.length > outputAmount)
                && !(inputs.length < inputAmount || inputs.length > inputAmount)) {
            int[] chances = new int[outputs.length];
            Arrays.fill(chances, 100);
            recipes.put(new MultiInput(shapeless, inputs), new MultiOutput(
                    chances, outputs));
            return true;
        }
        return false;
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
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack[] inputs, ItemStack[] outputs,
            int[] chances) {
        if (!(outputs.length < outputAmount || outputs.length > outputAmount)
                && !(inputs.length < inputAmount || inputs.length > inputAmount)
                && !(chances.length < outputAmount || chances.length > outputAmount)) {
            recipes.put(new MultiInput(shapeless, inputs), new MultiOutput(
                    chances, outputs));
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
        MultiInput key = new MultiInput(shapeless, inputs);
        if (recipes.containsKey(key)) {
            return recipes.get(key);
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
}
