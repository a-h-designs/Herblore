package com.herblore;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("herblore")
public interface HerbloreConfig extends Config {
	@ConfigItem(
			keyName = "statTimeout",
			name = "Reset stats",
			description = "Duration the herblore indicator and session stats are displayed before being reset")
	@Units(Units.MINUTES)
	default int statTimeout() {
		return 5;
	}

	@ConfigItem(
			keyName = "showHerbloreStats",
			name = "Show session stats",
			description = "Configures whether to display herblore session stats")
	default boolean showHerbloreStats() {
		return true;
	}

	@ConfigItem(
			keyName = "showIconsOnly",
			name = "Show icons only",
			description = "Display cleaned herbs and potions made as item images instead of any text")
	default boolean showIconsOnly() {
		return false;
	}
}
