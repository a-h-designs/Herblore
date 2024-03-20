package com.herblore;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;

import java.awt.*;

import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class HerbloreOverlay extends OverlayPanel {
    private static final String HERBLORE_RESET = "Reset";

    private final Client client;
    private final HerblorePlugin plugin;
    private final HerbloreConfig config;
    private final ItemManager itemManager;
    private final XpTrackerService xpTrackerService;

    @Inject
    private HerbloreOverlay(final Client client, final HerblorePlugin plugin, final HerbloreConfig config, ItemManager itemManager, XpTrackerService xpTrackerService) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.itemManager = itemManager;
        this.xpTrackerService = xpTrackerService;
        addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Herblore overlay");
        addMenuEntry(MenuAction.RUNELITE_OVERLAY, HERBLORE_RESET, "Herblore overlay", e -> plugin.setSession(null));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        HerbloreSession session = plugin.getSession();

        int cadantineClean = session.getCadantineClean();
        int lantadymeClean = session.getLantadymeClean();

        int superdefencePotion = session.getSuperdefencePotion();
        int weaponPoison = session.getWeaponPoison();
        int antifirePotion = session.getAntifirePotion();

        if (cadantineClean == 0 && lantadymeClean == 0 && superdefencePotion == 0 && weaponPoison == 0 &&
                antifirePotion == 0) {
            return null;
        }

        if (!config.showHerbloreStats()) {
            return null;
        }

        if(!config.showIconsOnly()) {
            panelComponent.setOrientation(ComponentOrientation.VERTICAL);
            setPosition(OverlayPosition.TOP_LEFT);
            /*int actions = xpTrackerService.getActions(Skill.HERBLORE);
            if (actions > 0) {

            }*/
            if (cadantineClean > 0 || lantadymeClean > 0) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Herbs")
                        .color(Color.YELLOW)
                        .build());
            }

            if (cadantineClean > 0) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Cadantine:")
                        .right(Integer.toString(cadantineClean))
                        .build());
            }
            if (lantadymeClean > 0) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Lantadyme:")
                        .right(Integer.toString(lantadymeClean))
                        .build());
            }

            if (superdefencePotion > 0 || weaponPoison > 0 || antifirePotion > 0) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Potions")
                        .color(Color.YELLOW)
                        .build());
            }

            if (superdefencePotion > 0) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Super Defence:")
                        .right(Integer.toString(superdefencePotion))
                        .build());
            }
            if (weaponPoison > 0) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Weapon Poison:")
                        .right(Integer.toString(weaponPoison))
                        .build());
            }
            if (antifirePotion > 0) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Antifire:")
                        .right(Integer.toString(antifirePotion))
                        .build());
            }
        } else {
            panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
            setPosition(OverlayPosition.TOP_CENTER);
            if (cadantineClean > 0) {
                panelComponent.getChildren().add(new ImageComponent(itemManager.getImage(ItemID.CADANTINE, cadantineClean, true)));
            }
            if (lantadymeClean > 0) {
                panelComponent.getChildren().add(new ImageComponent(itemManager.getImage(ItemID.LANTADYME, lantadymeClean, true)));
            }

            if (superdefencePotion > 0) {
                panelComponent.getChildren().add(new ImageComponent(itemManager.getImage(ItemID.SUPER_DEFENCE3, superdefencePotion, true)));
            }
            if (weaponPoison > 0) {
                panelComponent.getChildren().add(new ImageComponent(itemManager.getImage(ItemID.WEAPON_POISON, weaponPoison, true)));
            }
            if (antifirePotion > 0) {
                panelComponent.getChildren().add(new ImageComponent(itemManager.getImage(ItemID.ANTIFIRE_POTION3, antifirePotion, true)));
            }
        }
        return super.render(graphics);
    }
}