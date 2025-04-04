package io.github.haykam821.exchangevaluetracker.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.exchangevaluetracker.board.ValueBoard;
import io.github.haykam821.exchangevaluetracker.board.ValueBoardHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ValueBoardHolder {
	@Unique
	private final ValueBoard valueBoard = ((ServerWorld) (Object) this).getRegistryKey() == World.OVERWORLD ? null : new ValueBoard((ServerWorld) (Object) this);

	@Inject(method = "tick", at = @At("HEAD"))
	public void updateValueBoard(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (this.valueBoard != null) {
			this.valueBoard.update();
		}
	}

	@Override
	public ValueBoard getValueBoard() {
		return this.valueBoard;
	}
}
