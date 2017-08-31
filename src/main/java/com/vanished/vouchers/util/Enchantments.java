package com.vanished.vouchers.util;

@SuppressWarnings("unused")
public enum Enchantments {
    PROTECTION_ENVIRONMENTAL("protection"),
    PROTECTION_FIRE("fire protection"),
    PROTECTION_FALL("feather falling"),
    PROTECTION_EXPLOSIONS("blast protection"),
    PROTECTION_PROJECTILE("projectile protection"),
    OXYGEN("respiration"),
    WATER_WORKER("aqua affinity"),
    THORNS("thorns"),
    DAMAGE_ALL("sharpness"),
    DAMAGE_UNDEAD("smite"),
    DAMAGE_ARTHROPODS("bane of arthropods"),
    KNOCKBACK("knockback"),
    FIRE_ASPECT("fire aspect"),
    LOOT_BONUS_MOBS("looting"),
    DIG_SPEED("efficiency"),
    SILK_TOUCH("silk touch"),
    DURABILITY("unbreaking"),
    ARROW_DAMAGE("power"),
    ARROW_KNOCKBACK("punch"),
    ARROW_FIRE("flame"),
    ARROW_INFINITE("infinity"),
    LUCK("luck of the sea"),
    LURE("lure"),
    LOOT_BONUS_BLOCKS("fortune"),
    DEPTH_STRIDER("depth strider"),
    FROST_WALKER("frost walker"),
    MENDING("mending"),
    BINDING_CURSE("curse of binding"),
    VANISHING_CURSE("curse of vanishing"),
    SWEEPING_EDGE("sweeping edge");

    private final String niceName;

    Enchantments(String niceName) {
        this.niceName = niceName;
    }

    public String getNiceName() {
        return niceName;
    }
}
