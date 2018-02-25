package com.github.smk7758.PositionTimerForOld;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import com.github.smk7758.PositionTimerForOld.Position.PositionType;
import com.github.smk7758.PositionTimerForOld.Util.SendLog;

public class Main extends JavaPlugin {
	public static final String plugin_name = "PositionTimer";
	public static boolean debug_mode = true;
	private CommandExecuter command_executer = new CommandExecuter(this);
	private PositionListner position_listner = new PositionListner(this);
	private ConfigManager config_manager = new ConfigManager(this);
	public Set<Position> positions = new HashSet<>();
	public Map<Player, Boolean> player_except = new HashMap<>();
	private Scoreboard scoreboard = null;

	@Override
	public void onEnable() {
		if (!Main.plugin_name.equals(getDescription().getName())) getPluginLoader().disablePlugin(this);
		getCommand(plugin_name).setExecutor(command_executer);
		scoreboard = getServer().getScoreboardManager().getNewScoreboard();
		saveDefaultConfig();
		position_listner.startLoop();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void saveConfig() {
		super.saveConfig();
		save();
	}

	@Override
	public void saveDefaultConfig() {
		super.saveDefaultConfig();
		reloadConfig();
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		load();
		debug_mode = getConfig().getBoolean("DebugMode");
	}

	public void setPositionEnable(String name) {
		Position position = getPositionWithCreate(name);
		position.enable = !position.enable;
	}

	public void setPositionEnable(String name, boolean enable) {
		getPositionWithCreate(name).setEnable(enable);
	}

	public void setPositionLocation(String name, PositionType type, Player player) {
		if (player == null) throw new IllegalArgumentException("Player is null.");
		getPositionWithCreate(name).setLocation(player.getLocation().getBlock().getLocation(), type);
	}

	public void removePositionLocation(String name, PositionType type) {
		getPositionWithCreate(name).setLocation(null, type);
	}

	public Position getPositionWithCreate(String name) {
		Position position = getPosition(name);
		if (position != null) {
			return position;
		} else {
			position = new Position(name);
			positions.add(position);
			return position;
		}
	}

	public Position getPosition(String name) {
		for (Position position : positions) {
			if (position.equalName(name)) {
				return position;
			}
		}
		return null;
	}

	public void removePosition(String name) {
		getConfigManager().removePosition(name);
		Position position = getPosition(name);
		if (position != null) positions.remove(position);
	}

	// load
	public void load() {
		SendLog.debug("loadPositions");
		loadPositionLocations(PositionType.Start);
		loadPositionLocations(PositionType.End);
		loadEnablePositions();
	}

	public void loadPositionLocations(PositionType type) {
		for (String name : config_manager.getConfigPositionNames()) {
			getPositionWithCreate(name).setLocation(config_manager.getConfigLocation(name, type), type);
		}
	}

	public void loadEnablePositions() {
		for (String name : config_manager.getConfigPositionNames()) {
			getPositionWithCreate(name).setEnable(config_manager.getConfigEnable(name));
		}
	}

	// save
	public void save() {
		positions.forEach(position -> config_manager.setPosition(position));
	}

	public CommandExecuter getCommandExecuter() {
		return command_executer;
	}

	public PositionListner getPositionListner() {
		return position_listner;
	}

	public ConfigManager getConfigManager() {
		return config_manager;
	}

	public Scoreboard getScoreBoard() {
		return scoreboard;
	}

	// player config 保存 rank
	// Timeを表示するのは、Title, Sidebar, Chatか。
}