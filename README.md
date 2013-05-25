PaleoMachineFramework
=====================

The PaleoMachineFramework (or PMFW) is a system created for MinecraftForge.
It allows you to create recipe handlers for you custom machines (and much more to come).
Its main features are:
- Easy-to-use handler for simple recipes (1 input and 1 output)
- Handlers for recipes with:
  - Multiple inputs
  - Multiple outputs
  - Multiple in- and outputs
- The ability to set chances to get a certain output
- Additional values for the recipes (e.g. a needed level of heat for the recipe to work)

Later on there will be other things to make the creation of machines even easier.
Some features planned are:
- Integration of the upcoming PowerGrid Framework
- Multiple base classes for your machines' TileEntities to make processing easier

Setup
-----

To get the whole framework set up you have to follow these simple steps:

1. Download the latest version of the framework
  - You can get the ZIP file provided in the root directory, containing the latest stable version of the framework
  - You can get the source code by browsing to the "pmfw" package inside the "pmfw_src" folder
2. (Extract from the "src" folder and) put the files somewhere inside your modding project, I recommend putting them in a package called "pmfw" inside your main util package
3. You're done! For your first steps with the framework, have a look at the [Getting started](#getting-started) section

Getting started
---------------

Before trying to create a recipe handler for multiple in- and outputs, you should create a handler for simple recipes with just one in- and output.
To do so, simply create an instance of ```SimpleRecipes``` (it's called "yourSimpleRecipes" in the following) somewhere in your mod and make it ```static```. That way you can access it from everywhere.
The object you just created has to be defined as well, for this you only have to do the following in your mod's ```@Init``` (or somewhere else, the main thing is, that it's created) method:
```java
@Init
public void load(FMLInitializationEvent event) {
  yourSimpleRecipes = new SimpleRecipes();
}
```

Now, to add recipes to that object, simply call ```addRecipe(ItemStack yourInput, ItemStack yourOutput)``` on the instance and the recipe will be added to the internal list.

You're also able to add additional values to the recipes, for example a needed level heat for the recipe to work. To do so you have to add an argument of the type ```RecipeData``` after the output item in the ```addRecipe``` method. The first argument of the constructor is the key the value will be findable under later on and the value object is self-explanatory. You can add as many RecipeData objects at the end as you want. You can also create an array for the additional data.

To get that value back later, you have to use ```getAdditionalValue(ItemStack input, String key)``` on your ```SimpleRecipes``` object. It'll bring back an object of the ```Object``` class for the given input ItemStack and the given key.

Finally, if you want to check if a given recipe is valid, call ```isRecipeValid(ItemStack input)``` on your ```SimpleRecipes``` object (To check for a custom value you put in the recipe, see the previous paragraph). And to get the result of the recipe (if it's valid) simply call ```getResult(ItemStack input)```.

Getting started with multiple in- and outputs
---------------------------------------------

First of all, you have to create your machine and decide, how many in- and/or output slots it should have. For this example, we're going to use a machine using 3 input and 2 output slots, where one has a certain percentage of getting that item.
This is just an example which shows all the functionality of the framework. All the classes and methods themselves are well documented and there shouldn't be any problem with using it and changing the example to just use one input or output.

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
To see, how to add and get additional values to your recipe, see the [Getting Started](#getting-started) section.

Contributing
------------
Any help to improve this project is welcome. If you just want to make a suggestion, do so! If you find bugs or have a more performant solution for a certain task, create a new issue or pull request.

License
-------
The framework is distributed under the LGPL. For details see COPYING.LESSER. You're allowed to use the framwork without asking or anything else. You're free to change anything in the source files as long as you mention me (PaleoCrafter) as one of the authors. If you redistribute the framework as part of your own project it can be distributed under another license, but you have to name me (PaleoCrafter) somewhere. You're NOT allowed to redistribute the API under another license.