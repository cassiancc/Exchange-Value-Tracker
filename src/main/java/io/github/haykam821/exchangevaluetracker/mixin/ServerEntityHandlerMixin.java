package io.github.haykam821.exchangevaluetracker.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.exchangevaluetracker.board.ValueBoard;
import io.github.haykam821.exchangevaluetracker.board.ValueBoardHolder;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(targets = "net.minecraft.server.world.ServerWorld$ServerEntityHandler")
public class ServerEntityHandlerMixin {
	// ServerWorld.this synthetic field
	@SuppressWarnings("target")
	@Shadow
	@Final
	private ServerWorld field_26936;

	@Inject(method = "startTracking", at = @At("HEAD"))
	private void startTrackingValueBoard(Entity entity, CallbackInfo ci) {
		if (entity instanceof ServerPlayerEntity player) {
			ValueBoard board = ((ValueBoardHolder) this.field_26936).getValueBoard();

			if (board != null) {
				board.startTracking(player);
			}
		}
	}

	@Inject(method = "stopTracking", at = @At("HEAD"))
	private void stopTrackingValueBoard(Entity entity, CallbackInfo ci) {
		if (entity instanceof ServerPlayerEntity player) {
			ValueBoard board = ((ValueBoardHolder) this.field_26936).getValueBoard();

			if (board != null) {
				board.stopTracking(player);
			}
		}
	}
}
