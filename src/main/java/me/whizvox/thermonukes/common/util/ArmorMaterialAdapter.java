package me.whizvox.thermonukes.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ArmorMaterialAdapter implements ArmorMaterial {

  private final int[] durabilities;
  private final int[] defenses;
  private final int enchantmentValue;
  private final Supplier<SoundEvent> equipSoundSupplier;
  private final Supplier<Ingredient> repairIngredientSupplier;
  private final String name;
  private final float toughness;
  private final float knockbackResistance;

  private ArmorMaterialAdapter(int[] durabilities, int[] defenses, int enchantmentValue, Supplier<SoundEvent> equipSoundSupplier, Supplier<Ingredient> repairIngredientSupplier, String name, float toughness, float knockbackResistance) {
    this.durabilities = durabilities;
    this.defenses = defenses;
    this.enchantmentValue = enchantmentValue;
    this.equipSoundSupplier = equipSoundSupplier;
    this.repairIngredientSupplier = repairIngredientSupplier;
    this.name = name;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
  }

  @Override
  public int getDurabilityForSlot(EquipmentSlot slot) {
    return durabilities[slot.getIndex()];
  }

  @Override
  public int getDefenseForSlot(EquipmentSlot slot) {
    return defenses[slot.getIndex()];
  }

  @Override
  public int getEnchantmentValue() {
    return enchantmentValue;
  }

  @Override
  public SoundEvent getEquipSound() {
    return equipSoundSupplier.get();
  }

  @Override
  public Ingredient getRepairIngredient() {
    return repairIngredientSupplier.get();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public float getToughness() {
    return toughness;
  }

  @Override
  public float getKnockbackResistance() {
    return knockbackResistance;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final int[] durabilities;
    private final int[] defenses;
    private int enchantmentValue;
    private Supplier<SoundEvent> equipSoundSupplier;
    private Supplier<Ingredient> repairIngredientSupplier;
    private String name;
    private float toughness;
    private float knockbackResistance;

    private Builder() {
      durabilities = new int[4];
      defenses = new int[4];
      enchantmentValue = 0;
      // TODO Works well enough, but misleading subtitle refers specifically to leather armor
      equipSoundSupplier = () -> SoundEvents.ARMOR_EQUIP_LEATHER;
      repairIngredientSupplier = () -> Ingredient.EMPTY;
      name = "thermonukes.unnamed_armor";
      toughness = 0.0F;
      knockbackResistance = 0.0F;
    }

    public Builder durability(int head, int chest, int legs, int feet) {
      durabilities[0] = feet;
      durabilities[1] = legs;
      durabilities[2] = chest;
      durabilities[3] = head;
      return this;
    }

    public Builder durability(int durability) {
      return durability(durability, durability, durability, durability);
    }

    public Builder defense(int head, int chest, int legs, int feet) {
      defenses[0] = feet;
      defenses[1] = legs;
      defenses[2] = chest;
      defenses[3] = head;
      return this;
    }

    public Builder enchantmentValue(int enchantmentValue) {
      this.enchantmentValue = enchantmentValue;
      return this;
    }

    public Builder equipSound(Supplier<SoundEvent> equipSoundSupplier) {
      this.equipSoundSupplier = equipSoundSupplier;
      return this;
    }

    public Builder repairIngredient(Supplier<Ingredient> repairIngredientSupplier) {
      this.repairIngredientSupplier = repairIngredientSupplier;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder name(ResourceLocation location) {
      return name(location.toString());
    }

    public Builder toughness(float toughness) {
      this.toughness = toughness;
      return this;
    }

    public Builder knockbackResistance(float knockbackResistance) {
      this.knockbackResistance = knockbackResistance;
      return this;
    }

    public ArmorMaterialAdapter build() {
      return new ArmorMaterialAdapter(durabilities, defenses, enchantmentValue, equipSoundSupplier, repairIngredientSupplier, name, toughness, knockbackResistance);
    }
  }
}
