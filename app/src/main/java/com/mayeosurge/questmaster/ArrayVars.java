package com.mayeosurge.questmaster;

public class ArrayVars {

    final static int WEAPON_START = 5;
    final static int SHIELD_START = 10;
    final static int ARMOUR_START = 15;

    public static final String[] items = {"Key", "Letter", "Ration Pack", "", "",
                                          "Dagger", "Short Sword", "Long Sword", "Basic Bow", "",
                                          "Damaged Shield", "Shield", "", "", "",
                                          "Leather Helmet", "Leather Right Arm", "Leather Left Arm", "Leather Torso", ""};

    public static final int[] invImgs = {R.drawable.inv_key, R.drawable.inv_letter, R.drawable.inv_ration_pack, R.drawable.inv_blank, R.drawable.inv_blank,
                                         R.drawable.inv_dagger, R.drawable.inv_short_sword, R.drawable.inv_long_sword, R.drawable.inv_simple_bow, R.drawable.inv_blank,
                                         R.drawable.inv_dmg_shield, R.drawable.inv_shield, R.drawable.inv_blank, R.drawable.inv_blank, R.drawable.inv_blank,
                                         R.drawable.inv_leath_helmet, R.drawable.inv_leath_rarm, R.drawable.inv_leath_larm, R.drawable.inv_leath_torso, R.drawable.inv_blank};

    // Weapons: str, stea, mag
    // Armour: str, stea, mag, know, health
    public static final int[][] weaponVars =
            {{},{},{},{},{},
             {1, 0, 0}, {2, 0, 0}, {3, -1, 0}, {0, 0, 0}, {},
             {0, -1, 0}, {0, -1, 0}, {}, {}, {},
             {0, -1, 0, 1, 0},{0, 0, 0, 0, 1},{0, 0, 0, 0, 1},{1, 0, 0, 0, 1},{}};

    public static final int[][] questVars = {{6, 2, 3, 0}, {4, 6, 3, 0}, {0, 6, 0, 0}};
    public static final InvItem[][] questRewards =
            {{new Melee(2, 5), new Shield(1, 5)},
                    {new InvItem(1, 1, false, false, true, true), new InvItem(2, 20, true, false, false)},
                    {}};
    public static final int[] questGold = {0, 0, 50};

    public static final String[] names = {"Thomas the Red", "Cyrus the Green", "Richard the Blue"};

    public static final String[] qTitles = {"The Key", "The Shipment", "The Lesson"};
    public static final String[] qMsg = {"There\'s a key we need to get into the guard weapon stores for Drogston. The key is kept on the Mayor at all times but he leaves it beside him when sleeping. If you can steal the key we\'ll split the weapons stash 75/25\n\nReward:\n5 long swords\n5 shields",
    "I need somebody to intercept a shipment on the way out of town. If you intercept it you can keep everything, but I need the letter he's carrying. I'll give you gold, to cover the costs of the men you hire.\n\nReward:\n20 ration packs",
    "I need you to send a message. Beat the living hell out of Stanley, he deserves it. But don't kill him.\n\nReward:\n50 Gold"};
}
