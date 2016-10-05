package com.mayeosurge.questmaster;

public class Weapon extends InvItem {

    int strength;
    int stealth;
    int magic;
    boolean attackDefense;
    // accuracy, defense, weight/speed

    public Weapon(Weapon w){
        super(w);
        strength = w.strength;
        stealth = w.stealth;
        magic = w.magic;
        attackDefense = w.attackDefense;
    }

    public Weapon(int id, int qty, boolean attDef) {
        super(id, qty, true, true, false);
        this.name = ArrayVars.items[id];
        // weaponVars is separate array, have to "reset" id
        id -= 5;
        strength = ArrayVars.weaponVars[id][0];
        stealth = ArrayVars.weaponVars[id][1];
        magic = ArrayVars.weaponVars[id][2];
        attackDefense = attDef;
    }
}
