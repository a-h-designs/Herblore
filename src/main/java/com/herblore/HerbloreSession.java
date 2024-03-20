package com.herblore;

import ch.qos.logback.classic.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ItemID;

class HerbloreSession {
    @Getter(AccessLevel.PACKAGE)
    private int cadantineClean;
    @Getter(AccessLevel.PACKAGE)
    private int lantadymeClean;

    @Getter(AccessLevel.PACKAGE)
    private int superdefencePotion;
    @Getter(AccessLevel.PACKAGE)
    private int weaponPoison;
    @Getter(AccessLevel.PACKAGE)
    private int antifirePotion;

    private Logger log;

    void updateHerbloreSession(int item, int count) {
        switch (item) {
            case ItemID.CADANTINE:
                cadantineClean +=count;
                break;
            case ItemID.LANTADYME:
                lantadymeClean += count;
                break;
            case ItemID.SUPER_DEFENCE3:
                superdefencePotion +=count;
                break;
            case ItemID.WEAPON_POISON:
                weaponPoison +=count;
                break;
            case ItemID.ANTIFIRE_POTION3:
                antifirePotion +=count;
                break;
            default:
                log.debug("Invalid ingredients specified. The herb/potion count will not be updated.");
        }
    }
}