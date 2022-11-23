package me.whizvox.thermonukes.data.server;

import me.whizvox.thermonukes.common.lib.ThermonukesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class ThermonukesRecipeProvider extends RecipeProvider {

  public ThermonukesRecipeProvider(DataGenerator gen) {
    super(gen);
  }

  @Override
  protected void buildCraftingRecipes(Consumer<FinishedRecipe> writer) {
    ShapelessRecipeBuilder.shapeless(ThermonukesItems.SALTY_MIXTURE_TUBE.get())
        .requires(Items.BAMBOO).requires(Items.GLOWSTONE_DUST).requires(Items.CLAY_BALL)
        .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
        .save(writer);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ThermonukesItems.SALTY_MIXTURE_TUBE.get()), ThermonukesItems.CLEANSING_SALT.get(), 0.0F, 200)
        .unlockedBy("has_salty_mixture_tube", has(ThermonukesItems.SALTY_MIXTURE_TUBE.get()))
        .save(writer);
  }
}
