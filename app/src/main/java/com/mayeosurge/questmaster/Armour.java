package com.mayeosurge.questmaster;

public class Armour extends InvItem {

    int strength;
    int stealth;
    int magic;
    int knowledge;
    int health;

    public Armour(int id, int qty){
        super(id+ArrayVars.ARMOUR_START, qty, false, true, false);
        id+=ArrayVars.ARMOUR_START;
        strength = ArrayVars.weaponVars[id][0];
        stealth = ArrayVars.weaponVars[id][1];
        magic = ArrayVars.weaponVars[id][2];
        knowledge = ArrayVars.weaponVars[id][3];
        health = ArrayVars.weaponVars[id][4];
    }
}
