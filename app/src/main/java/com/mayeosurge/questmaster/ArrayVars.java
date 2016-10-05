package com.mayeosurge.questmaster;

public class ArrayVars {

    public static final String[] items = {"Key", "Letter", "Ration Pack", "", "",
                                          "Long Sword", "Short Sword", "Basic Bow", "Shield", ""};

    public static final int[] invImgs = {R.drawable.inv_key, R.drawable.inv_letter, R.drawable.inv_ration_pack, R.drawable.inv_blank, R.drawable.inv_blank,
                                         R.drawable.inv_long_sword, R.drawable.inv_short_sword, R.drawable.inv_simple_bow, R.drawable.inv_shield, R.drawable.inv_blank};

    public static final int[][] weaponVars =
            {{2, -1, 0},
                    {1, 0, 0},
                    {2, 0, 0},
                    {0, 0, 0}};

    public static final int[][] questVars = {{6, 2, 3, 0}, {4, 6, 3, 0}, {0, 6, 0, 0}};
    public static final InvItem[][] questRewards =
            {{new Weapon(5, 5, true), new Weapon(8, 5, false)},
                    {new InvItem(1, 1, false, false, true), new InvItem(2, 20, true, false, false)},
                    {}};
    public static final int[] questGold = {0, 0, 50};

    public static final String[] names = {"Thomas the Red", "Cyrus the Green", "Richard the Blue"};

    public static final String[] qTitles = {"The Key", "The Shipment", "The Lesson"};
    public static final String[] qMsg = {"There\'s a key we need to get into the guard weapon stores for Drogston. The key is kept on the Mayor at all times but he leaves it beside him when sleeping. If you can steal the key we\'ll split the weapons stash 75/25\n\nReward:\n5 long swords\n5 shields",
    "I need somebody to intercept a shipment on the way out of town. If you intercept it you can keep everything, but I need the letter he's carrying. I'll give you gold, to cover the costs of the men you hire.\n\nReward:\n20 ration packs",
    "I need you to send a message. Beat the living hell out of Stanley, he deserves it. But don't kill him.\n\nReward:\n50 Gold"};
}
