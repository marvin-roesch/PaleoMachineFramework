package de.paleocrafter.multiio;

import java.util.HashMap;

import de.paleocrafter.multiio.recipe.MultiInput;

import net.minecraft.item.ItemStack;

/**
 * 
 * MMO Materials
 * 
 * MultiInputRecipes
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MultiInputRecipes {
    private HashMap<MultiInput, ItemStack> recipes;
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

        recipes = new HashMap<MultiInput, ItemStack>();
    }

    /**
     * 
     * Adds a recipe to the internal list. Can take infinite arguments for the
     * input. The amount of arguments must match the input amount given in the
     * constructor.
     * 
     * @param output
     *            The output ItemStack of the recipe.
     * @param inputs
     *            An array of ItemStacks needed to get the output. Can also be a
     *            list of infinite arguments (like: ItemStack input1, ItemStack
     *            input2 ...)
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack output, ItemStack... inputs) {
        if (!(inputs.length < inputAmount || inputs.length > inputAmount)) {
            recipes.put(new MultiInput(shapeless, inputs), output);
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
        MultiInput key = new MultiInput(shapeless, inputs);
        if (recipes.containsKey(key)) {
            return recipes.get(key);
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
}
