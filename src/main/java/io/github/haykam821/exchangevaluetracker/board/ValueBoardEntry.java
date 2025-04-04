package io.github.haykam821.exchangevaluetracker.board;

import eu.pb4.sidebars.api.lines.LineBuilder;
import io.github.haykam821.exchangevaluetracker.MineCalculator;
import net.minecraft.scoreboard.number.FixedNumberFormat;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public record ValueBoardEntry(ServerPlayerEntity player, double value) implements Comparable<ValueBoardEntry> {
	public ValueBoardEntry(ServerPlayerEntity player) {
		this(player, MineCalculator.getValue(player));
	}

	private boolean isSpectator() {
		return this.player.isSpectator();
	}

	private Text getName() {
		Text name = this.player.getDisplayName();

		if (this.isSpectator()) {
			return name.copy().styled(style -> {
				return style.withStrikethrough(true);
			});
		}

		return name;
	}

	public void addTo(LineBuilder lines) {
		Text score = Text.literal("" + ValueBoard.round(this.value)).styled(style -> {
			return style.withColor(0x80FF20).withShadowColor(0xFF000000);
		});

		lines.add(this.getName(), new FixedNumberFormat(score));
	}

	@Override
	public int compareTo(ValueBoardEntry o) {
		if (this.isSpectator() != o.isSpectator()) {
			return Boolean.compare(this.isSpectator(), o.isSpectator());
		} else if (this.value == o.value) {
			return this.player.getUuid().compareTo(o.player.getUuid());
		}

		return Double.compare(o.value, this.value);
	}
}
