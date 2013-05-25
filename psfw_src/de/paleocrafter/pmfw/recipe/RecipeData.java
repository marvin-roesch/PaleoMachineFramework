package de.paleocrafter.pmfw.recipe;

/**
 *
 * PaleoMachineFramework
 *
 * RecipeData
 *
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */
public class RecipeData {
    private String key;
    private Object value;
    
    public RecipeData(String key, Object value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }
    
    public Object getValue() {
        return value;
    }
}
