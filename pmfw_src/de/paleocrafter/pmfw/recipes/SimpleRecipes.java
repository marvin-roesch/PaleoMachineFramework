package de.paleocrafter.pmfw.recipes;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.minecraft.item.ItemStack;

import de.paleocrafter.pmfw.recipes.data.RecipeData;
import de.paleocrafter.pmfw.recipes.data.RecipeItemStack;

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
    private Table<RecipeItemStack, String, RecipeData> recipes;

    /**
     * 
     * Creates a new instance of the SimpleRecipes class. The object given can
     * be used to create simple recipes with 1 in- and output.
     * 
     */
    public SimpleRecipes() {
        recipes = HashBasedTable.create();
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
        input = checkNotNull(input);
        output = checkNotNull(output);
        RecipeItemStack recipeStack = new RecipeItemStack(input);
        recipes.put(recipeStack, "output", new RecipeData("output", output));
        if (additionalData != null && additionalData.length > 0)
            for (RecipeData obj : additionalData) {
                if (obj != null && !obj.getKey().equals("output"))
                    recipes.put(recipeStack, obj.getKey(), obj);
            }
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
            if (recipes.containsRow(stack))
                return (ItemStack) recipes.row(stack).get("output").getValue();
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
