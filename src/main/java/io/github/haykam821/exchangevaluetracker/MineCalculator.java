package io.github.haykam821.exchangevaluetracker;

import net.minecraft.class_11109;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Contains utilities for calculating mine and item values.
 */
public final class MineCalculator {
	private static final String MINE_KEY_PREFIX = "level";

	private static float BASE_MULTIPLIER = 1;
	private static double BASE_VALUE = 10;

	private MineCalculator() {
		return;
	}

	public static String getMineOrdinal(ServerWorld world) {
		if (world != null) {
			// The mine ordinal is not stored in a convenient way, so parse it from the world key
			String path = world.getRegistryKey().getValue().getPath();

			if (path.startsWith(MINE_KEY_PREFIX)) {
				return path.substring(MINE_KEY_PREFIX.length());
			}
		}

		return "???";
	}

	public static float getMultiplier(ServerWorld world) {
		float multiplier = BASE_MULTIPLIER;

		if (world != null) {
			for (class_11109 effect : world.method_69125()) {
				multiplier += effect.experienceModifier();
			}
		}

		return multiplier;
	}

	public static double getValue(ServerPlayerEntity player) {
		double value = BASE_VALUE;

		for (ItemStack stack : player.getInventory()) {
			if (!stack.contains(DataComponentTypes.WORLD_MODIFIERS) && !stack.isIn(ItemTags.CARRY_OVER)) {
				float itemValue = stack.getOrDefault(DataComponentTypes.EXCHANGE_VALUE, Item.DEFAULT_EXCHANGE_VALUE).getValue(player, stack);
				value += itemValue * stack.getCount();
			}
		}

		return value * getMultiplier(player.getServerWorld()) * player.getAttributeValue(EntityAttributes.EXPERIENCE_GAIN_MODIFIER);
	}
}
