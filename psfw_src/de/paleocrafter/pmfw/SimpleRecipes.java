package de.paleocrafter.pmfw;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

import de.paleocrafter.pmfw.recipe.RecipeData;
import de.paleocrafter.pmfw.recipe.RecipeItemStack;

/**
 * 
 * PaleoMachineFramework
 * 
 * SimpleRecipese
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class SimpleRecipes {
    private HashMap<RecipeItemStack, HashMap<String, RecipeData>> recipes;

    /**
     * 
     * Creates a new instance of the SimpleRecipes class. The object given can
     * be used to create simple recipes with 1 in- and output.
     * 
     */
    public SimpleRecipes() {
        recipes = new HashMap<RecipeItemStack, HashMap<String, RecipeData>>();
    }

    /**
     * 
     * Adds a recipe to the internal list without additional data.
     * 
     * @param input
     *            An ItemStack needed to get the output.
     * @param outputs
     *            An ItemStack given when the input is correct.
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack input, ItemStack output) {
        return addRecipe(input, output, new RecipeData[] {});
    }

    /**
     * 
     * Adds a recipe to the internal list.
     * 
     * @param input
     *            An ItemStack needed to get the output.
     * @param outputs
     *            An ItemStack given when the input is correct.
     * @param additionalData
     *            An array of RecipeData objects. The data will be added among
     *            its keys to the recipe for later use. Can be null if you don't
     *            have additional data. Can also be an infinite list of
     *            arguments.
     * @return true, if the recipe was added successfully. Otherwise false
     */
    public boolean addRecipe(ItemStack input, ItemStack output,
            RecipeData... additionalData) {
        HashMap<String, RecipeData> data = new HashMap<String, RecipeData>();
        if (output != null)
            data.put("output", new RecipeData("output", output));
        else
            return false;
        if (additionalData != null && additionalData.length > 0)
            for (RecipeData obj : additionalData) {
                if (obj != null && !obj.getKey().equals("output"))
                    data.put(obj.getKey(), obj);
            }
        if (input == null)
            return false;
        recipes.put(new RecipeItemStack(input), data);
        return true;
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
    public ItemStack getResult(ItemStack input) {
        if (input != null) {
            RecipeItemStack stack = new RecipeItemStack(input);
            if (recipes.containsKey(stack))
                return (ItemStack) recipes.get(stack).get("output").getValue();
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
            if (recipes.containsKey(stack)) {
                if (key != null) {
                    if (recipes.get(stack).containsKey(key))
                        return recipes.get(stack).get(key).getValue();
                }
            }
        }
        return null;
    }
}
