PaleoSlotFrameWork
==================

A completely working framework for creating machines with multiple in- and output slots used in the recipes for MinecraftForge. It supports shaped and shapeless recipes and chances to get a certain output item.

Setup
-----

To get the whole framework set up you have to follow these simple steps:

1. Download the latest version of the framework
  - You can get the ZIP file provided in the root directory, containing the latest stable version of the framework
  - You can get the source code by browsing to the "multiio" package inside the "multiio_src" folder
2. (Extract and) put the files somewhere inside your modding project, I recommend putting them in a package called "multiio" inside your main util package
3. You're done! For your first steps with the framework, have a look at the [Getting started](#getting-started) section

Getting started
---------------

First of all, you have to create your machine and decide, how many in- and/or output slots it should have. For this example, we're going to use a machine using 3 input and 2 output slots, where one has a certain percentage of getting that item.
This is just an example which shows all the functionality of the framework. The whole thing is well documented and there shouldn't be any problem with using it and changing the example to just use one input or output.

But let's get started:

###1. Creating the recipe handler
Create a static instance of ```MultiIORecipes``` somewhere in your mod (I recommend your main mod file or a dedicated recipe handler class, if you want to provide the framework as part of your own API.)
  - If you create a class just for recipe handling of that specific machine, you should write a static method for each: adding recipes, getting results and checking if there's a result for a given recipe.
    Those methods should call their equivalents on a ```private static``` instance of the ```MultiIORecipes``` class. To access that object, create a method like ```public static MultiIORecipes instance()``` which will create a new instance whenever the object is ```null```.
    It should look like this:

```java
public static MultiIORecipes instance() {
  if (instance == null)
    instance = new MultiIORecipes(inputAmount, outputAmount, isTheRecipeShapeless?);
  return instance;
}
```
  - The 3rd (or 2nd for MultiInputRecipes) argument defines, whether the recipe should be shapeless or not (true=shapeless, false=shaped). If it's shaped, the order of adding the input stacks and getting the result has to be the same.

###2. Adding recipes
To add a recipe to the list, simply call ```addRecipe(ItemStack[] inputs, ItemStack[] outputs, int[] chances);``` (For a recipe with getting the n-th output with a n-th chance) or ```java addRecipe(ItemStack[] inputs, ItemStack[] outputs);``` (For a recipe with a 100% chance of getting all outputs)
  - For our example, we'll be using:

```java
addRecipe(new ItemStack[] { new ItemStack(Item.feather, 1), new ItemStack(Item.flint, 1),
  new ItemStack(Item.appleRed, 1) }, new ItemStack[] { new ItemStack(Item.appleGold, 1), new ItemStack(Item.bone, 1) },
  new int[] { 100, 20 });
```
  - For explanation: First of all, we are defining, what is needed as input for the recipe (here: 1 feather, 1 flint and 1 red apple). Then we're saying, what the recipe's output is, when the input is right (here: 1 golden apple and 1 bone). Finally, we're defining with what chance you get which item (the order has to be the same as the one of the output array), here we're using a chance of 100(%) of getting the golden apple and a chance of 20(%) to get the bone. The example isn't really realistic, but it's just an example, isn't it?

###3. Using the recipes
Finally, you have to write your processing code in your tile entity's ```updateEntity()``` method.
  - To check if the recipe given in the inventory matches a recipe just call ```isRecipeValid(inventory[0], inventory[1], inventory[3],...)``` on the ```MultiIORecipes``` object.
  - After processing the valid recipe you can call ```getResult(inventory[0], inventory[1], inventory[2],...)``` on the object and it will give you a ```MultiOutput``` object.
  - You can call ```getOutput(int index)``` on it to get a certain output ItemStack from the multiple outputs (For a single output recipe you'll automatically get an ItemStack). To get the chance for getting that item call the respective method on the object. It depends on how you want to calculate the chance, there are multiple approaches. One could be to get a random int from 1 to the maximum chance and if it's <= to the output chance, you can put the ItemStack into the respective slot.

That's it. As I said, you don't have to use all the features presented here, it's just an example which shows all the features available within the framework.

Contributing
------------
Any help to improve this project is welcome. If you just want to make a suggestion, do so! If you find bugs or have a more performant solution for a certain task, create a new issue or pull request.

License
-------
The framework is distributed under the LGPL. For details see COPYING.LESSER. You're allowed to use the framwork without asking or anything else. You're free to change anything in the source files as long as you mention me (PaleoCrafter) as one of the authors. If you redistribute the framework as part of your own project it can be distributed under another license, but you have to name me (PaleoCrafter) somewhere. You're NOT allowed to redistribute the API under another license.