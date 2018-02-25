package com.github.smk7758.PositionTimerForOld;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Position {
	public String name = "";
	private Location start_loc = null, end_loc = null;
	public boolean enable = true;
	public Map<Player, LocalDateTime> players_started = new HashMap<>();
	public Map<OfflinePlayer, Duration> player_times = new HashMap<>();

	public enum PositionType {
		Start, End;
	}

	public Position(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Position && equalName(((Position) o).name));
	}

	public boolean equalName(String name) {
		return this.name.equals(name);
	}

	// TODO: 雑。
	@Override
	public int hashCode() {
		return 0;
	}

	public Position setLocation(Location loc, PositionType type) {
		if (type.equals(PositionType.Start)) this.start_loc = loc;
		else if (type.equals(PositionType.End)) this.end_loc = loc;
		else throw new IllegalArgumentException("Illegal PositionType.");
		return this;
	}

	public Location getLocation(PositionType type) {
		if (type.equals(PositionType.Start)) return this.start_loc;
		else if (type.equals(PositionType.End)) return this.end_loc;
		else throw new IllegalArgumentException("Illegal PositionType.");
	}

	public Position setEnable(boolean enable) {
		this.enable = enable;
		return this;
	}

	// TODO: not sorted, bug
	public Stream<Entry<OfflinePlayer, Duration>> getPlayerTimesSorted() {
		return player_times.entrySet().stream().sorted(Entry.comparingByValue());
		// return player_times.entrySet().stream().sorted();
		// return player_times.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByValue()));
	}
}
