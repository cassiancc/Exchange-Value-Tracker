package io.github.haykam821.exchangevaluetracker.board;

import java.util.HashSet;
import java.util.Set;

import eu.pb4.sidebars.api.Sidebar;
import io.github.haykam821.exchangevaluetracker.MineCalculator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class ValueBoard {
	private final ServerWorld world;
	private final Sidebar sidebar = new Sidebar(getTitle(null), Sidebar.Priority.MEDIUM);

	private final Set<ServerPlayerEntity> players = new HashSet<>();

	public ValueBoard(ServerWorld world) {
		this.world = world;
	}

	public void update() {
		this.sidebar.setTitle(getTitle(this.world));

		this.sidebar.set(lines -> {
			this.players.stream()
				.map(ValueBoardEntry::new)
				.sorted()
				.forEachOrdered(entry -> {
					entry.addTo(lines);
				});
		});

		this.sidebar.show();
	}

	public void startTracking(ServerPlayerEntity player) {
		this.sidebar.addPlayer(player);
		this.players.add(player);
	}

	public void stopTracking(ServerPlayerEntity player) {
		this.sidebar.removePlayer(player);
		this.players.remove(player);
	}

	private static Text getTitle(ServerWorld world) {
		String ordinal = MineCalculator.getMineOrdinal(world);
		double multiplier = MineCalculator.getMultiplier(world);

		return Text.literal("Mine " + ordinal + " • x" + multiplier).formatted(Formatting.YELLOW);
	}

	protected static double round(double value) {
		return Math.round(value * 1000) / 1000d;
	}
}
