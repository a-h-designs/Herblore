package com.herblore;

import com.google.inject.Provides;

import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Herblore",
		description = "Displays how many herbs you clean/potions you make.",
		tags = {"herblore", "herb", "potion", "ingredient"})

@PluginDependency(XpTrackerPlugin.class)
public class HerblorePlugin extends Plugin {
	private static final Pattern CLEAN_HERB_PATTERN = Pattern.compile(
			"You " +
					"(?:mix|clean)" +
					" (?:the|) " +
					"(?:Grimy|whiteberries|dragon scale) " +
					"(?:into your potion|guam|marrentill|tarromin|harralander|ranarr weed|toadflax|irit leaf|avantoe|kwuarm|snapdragon|cadantine|lantadyme|dwarf weed|torstol)" +
					"(?:\\.|!)");
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HerbloreOverlay overlay;

	@Inject
	private HerbloreConfig config;

	@Getter
	@Nullable
	@Setter(AccessLevel.PACKAGE)
	private HerbloreSession session;

	private boolean hasSuperDefence = false;
	private boolean hasWeaponPoison = false;
	private boolean hasAntifirePotion = false;
	@Provides
	HerbloreConfig getConfig(ConfigManager configManager) {
		return configManager.getConfig(HerbloreConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
		session = null;
	}


	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event) {
		if (event.getItemContainer() == client.getItemContainer(InventoryID.INVENTORY)) {
			Item[] items = event.getItemContainer().getItems();
			for (Item i : items) {
				switch (i.getId()) {
					case ItemID.CADANTINE_POTION_UNF:
						hasSuperDefence = true;
						break;
					case ItemID.KWUARM_POTION_UNF:
						hasWeaponPoison = true;
						break;
					case ItemID.LANTADYME_POTION_UNF:
						hasAntifirePotion = true;
						break;
				}
			}
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		String chatMessage = event.getMessage();

		if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
			if (session == null) {
				session = new HerbloreSession();
			}

			switch (chatMessage) {
				case "You clean the Grimy cadantine.":
					session.updateHerbloreSession(ItemID.CADANTINE, +1);
					break;
				case "You clean the Grimy lantadyme.":
					session.updateHerbloreSession(ItemID.LANTADYME, +1);
					break;

				case "You mix the white berries into your potion.":
					if (hasSuperDefence) {
						session.updateHerbloreSession(ItemID.SUPER_DEFENCE3, +1);
					} else {
						session.updateHerbloreSession(ItemID.DEFENCE_POTION3, +1);
					}
					break;
				case "You mix the dragon scale into your potion.":
					if (hasWeaponPoison) {
						session.updateHerbloreSession(ItemID.WEAPON_POISON, +1);
					} else if (hasAntifirePotion) {
						session.updateHerbloreSession(ItemID.ANTIFIRE_POTION3, +1);
					}
					break;
			}
		}
	}
}
