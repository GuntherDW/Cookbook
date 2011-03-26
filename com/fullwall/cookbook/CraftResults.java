package com.fullwall.cookbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.Block;
import net.minecraft.server.CraftingRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ShapedRecipes;
import net.minecraft.server.ShapelessRecipes;

public class CraftResults {
	private final static CraftResults instance = new CraftResults();
	@SuppressWarnings("rawtypes")
	private List b = new ArrayList();

	public static CraftResults getInstance() {
		instance.addRecipe(Cookbook.recipes);
		instance.addShapelessRecipe(Cookbook.shapelessRecipes);
		return instance;
	}

	// start of minecraft code
	public CraftResults() {
	}

	@SuppressWarnings("unchecked")
	public void addRecipe(ArrayList<ArrayList<Integer>> myRecipes) {
		int count = 0;
		for (ArrayList<Integer> localRecipes : myRecipes) {
			if (localRecipes.size() >= 1) {
				ItemStack[] stackArray = new ItemStack[9];
				int itemID = Cookbook.results.get(count)[0];
				int amount = Cookbook.results.get(count)[1];
				int damage = Cookbook.results.get(count)[2];

				for (int i = 0; i < localRecipes.size(); ++i) {
					int id = localRecipes.get(i);
					int data = Cookbook.recipesData.get(count).get(i);
					if (id == 0)
						continue;
					if (data == 0) {
						if (id < 255) {
							stackArray[i] = new ItemStack(Block.byId[id]);
						} else
							stackArray[i] = new ItemStack(Item.byId[id]);
					} else
						stackArray[i] = new ItemStack(id, 1, data);
				}
				ItemStack recipeResult;
				if (itemID != 0)
					recipeResult = new ItemStack(itemID, amount, damage);
				else
					recipeResult = null;
				ShapedRecipes recipe = new ShapedRecipes(3, 3, stackArray,
						recipeResult);
				this.b.add(recipe);
			}
			count += 1;
		}
	}

	@SuppressWarnings("unchecked")
	public void addShapelessRecipe(ArrayList<ArrayList<Integer>> myRecipes) {
		int count = 0;
		for (ArrayList<Integer> localRecipes : myRecipes) {
			ArrayList<ItemStack> adding = new ArrayList<ItemStack>();
			int itemID = Cookbook.shapelessResults.get(count)[0];
			int amount = Cookbook.shapelessResults.get(count)[1];
			int damage = Cookbook.shapelessResults.get(count)[2];

			for (int j = 0; j < localRecipes.size(); ++j) {
				int id = localRecipes.get(j);
				int data = Cookbook.shapelessRecipesData.get(count).get(j);
				if (id == 0)
					continue;
				net.minecraft.server.ItemStack item = new ItemStack(id, 1, data);
				adding.add(item.j());
			}
			ItemStack recipeResult;
			if (itemID != 0)
				recipeResult = new ItemStack(itemID, amount, damage);
			else
				recipeResult = null;
			this.b.add(new ShapelessRecipes(recipeResult, adding));
			count += 1;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void addShaped(ItemStack itemstack, Object... aobject) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (aobject[i] instanceof String[]) {
			String[] astring = (String[]) ((String[]) aobject[i++]);

			for (int l = 0; l < astring.length; ++l) {
				String s1 = astring[l];

				++k;
				j = s1.length();
				s = s + s1;
			}
		} else {
			while (aobject[i] instanceof String) {
				String s2 = (String) aobject[i++];

				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap hashmap;

		for (hashmap = new HashMap(); i < aobject.length; i += 2) {
			Character character = (Character) aobject[i];
			ItemStack itemstack1 = null;

			if (aobject[i + 1] instanceof Item) {
				itemstack1 = new ItemStack((Item) aobject[i + 1]);
			} else if (aobject[i + 1] instanceof Block) {
				itemstack1 = new ItemStack((Block) aobject[i + 1], 1, -1);
			} else if (aobject[i + 1] instanceof ItemStack) {
				itemstack1 = (ItemStack) aobject[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1) {
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0))) {
				aitemstack[i1] = ((ItemStack) hashmap
						.get(Character.valueOf(c0))).j();
			} else {
				aitemstack[i1] = null;
			}
		}

		this.b.add(new ShapedRecipes(j, k, aitemstack, itemstack));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void addShapeless(ItemStack itemstack, Object... aobject) {
		ArrayList arraylist = new ArrayList();
		Object[] aobject1 = aobject;
		int i = aobject.length;

		for (int j = 0; j < i; ++j) {
			Object object = aobject1[j];

			if (object instanceof ItemStack) {
				arraylist.add(((ItemStack) object).j());
			} else if (object instanceof Item) {
				arraylist.add(new ItemStack((Item) object));
			} else {
				if (!(object instanceof Block)) {
					throw new RuntimeException("Invalid shapeless recipy!");
				}
				arraylist.add(new ItemStack((Block) object));
			}
		}

		this.b.add(new ShapelessRecipes(itemstack, arraylist));
	}

	public ItemStack getResult(InventoryCrafting inventorycrafting) {
		for (int i = 0; i < this.b.size(); ++i) {
			CraftingRecipe craftingrecipe = (CraftingRecipe) this.b.get(i);
			if (craftingrecipe.a(inventorycrafting)) {
				return craftingrecipe.b(inventorycrafting);
			}
		}
		return null;
	}
}