package com.mayeosurge.questmaster;

public class Melee extends Weapon {

    public Melee(Weapon w){
        super(w);
    }

    public Melee(int meleeType, int qty){
        super(meleeType+ArrayVars.WEAPON_START, qty);
        attackDefense = true;
    }

    public Melee(int allType, int qty, boolean startup){
        super(allType, qty);
        attackDefense = true;
    }
}
