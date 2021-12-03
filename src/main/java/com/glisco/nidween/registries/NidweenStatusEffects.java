package com.glisco.nidween.registries;

import com.glisco.nidween.Nidween;
import com.glisco.nidween.statuseffects.FlightStatusEffect;
import com.glisco.nidween.statuseffects.FreezingStatusEffect;
import com.glisco.nidween.statuseffects.NidweenStatusEffect;
import com.glisco.nidween.util.potion.GlowingPotion;
import com.glisco.nidween.util.potion.PotionMixture;
import com.glisco.nidween.util.recipe.GlowingPotionDyeRecipe;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.function.BiFunction;

public class NidweenStatusEffects {

    private static final HashMap<Identifier, Potion> POTIONS = new HashMap<>();

    public static final StatusEffect LIFE_LEECH = new NidweenStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF0000);
    public static final StatusEffect STEADFAST = new NidweenStatusEffect(StatusEffectCategory.BENEFICIAL, 0x222222).addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "ea838142-499a-4460-b92c-a12d1e329b77", 1, EntityAttributeModifier.Operation.ADDITION);
    public static final StatusEffect FLIGHT = new FlightStatusEffect(StatusEffectCategory.BENEFICIAL, 0x6666FF);
    public static final StatusEffect FREEZING = new FreezingStatusEffect(StatusEffectCategory.HARMFUL, 0x000066);

    public static void register() {
        registerEffectAndPotions(LIFE_LEECH, "life_leech", 2400, true, true);
        registerEffectAndPotions(STEADFAST, "steadfast", 4800, true, true);
        registerEffectAndPotions(FREEZING, "freezing", 600, true, true);

        registerEffectAndPotions(FLIGHT, "flight", 2400, true, false);

        registerPotions(StatusEffects.RESISTANCE, "resistance", 4800, true, true);
        registerPotions(StatusEffects.GLOWING, "glowing", 9600, true, false, GlowingPotion::new);
        registerPotions(StatusEffects.WITHER, "wither", 400, true, true);
        registerPotions(StatusEffects.HUNGER, "hunger", 1200, true, true);

        Registry.register(Registry.POTION, Nidween.id("dubious"), PotionMixture.DUBIOUS_POTION);
        Registry.register(Registry.RECIPE_SERIALIZER, Nidween.id("crafting_special_potiondye"), GlowingPotionDyeRecipe.SERIALIZER);
    }

    private static void registerEffectAndPotions(StatusEffect effect, String baseName, int baseDuration, boolean registerLong, boolean registerStrong) {
        Registry.register(Registry.STATUS_EFFECT, Nidween.id(baseName), effect);
        registerPotions(effect, baseName, baseDuration, registerLong, registerStrong);
    }

    private static void registerEffectAndPotions(StatusEffect effect, String baseName, int baseDuration, boolean registerLong, boolean registerStrong, BiFunction<String, StatusEffectInstance, Potion> potionCreator) {
        Registry.register(Registry.STATUS_EFFECT, Nidween.id(baseName), effect);
        registerPotions(effect, baseName, baseDuration, registerLong, registerStrong, potionCreator);
    }

    private static void registerPotions(StatusEffect effect, String baseName, int baseDuration, boolean registerLong, boolean registerStrong) {
        registerPotions(effect, baseName, baseDuration, registerLong, registerStrong, Potion::new);
    }

    private static void registerPotions(StatusEffect effect, String baseName, int baseDuration, boolean registerLong, boolean registerStrong, BiFunction<String, StatusEffectInstance, Potion> potionCreator) {
        POTIONS.put(Nidween.id(baseName), Registry.register(Registry.POTION, Nidween.id(baseName), potionCreator.apply(baseName, new StatusEffectInstance(effect, baseDuration))));

        if (registerLong)
            POTIONS.put(Nidween.id("long_" + baseName), Registry.register(Registry.POTION, Nidween.id("long_" + baseName), new Potion(baseName, new StatusEffectInstance(effect, baseDuration * 2))));
        if (registerStrong)
            POTIONS.put(Nidween.id("strong_" + baseName), Registry.register(Registry.POTION, Nidween.id("strong_" + baseName), new Potion(baseName, new StatusEffectInstance(effect, (int) (baseDuration * 0.5), 1))));

    }

    public static Potion getPotion(Identifier id) {
        return POTIONS.get(id);
    }

}
