package com.mayeosurge.questmaster;

public class Shield extends Weapon {

    public Shield(Weapon w){
        super(w);
    }

    public Shield(int shieldType, int qty){
        super(shieldType+ArrayVars.SHIELD_START, qty);
        attackDefense = false;
    }

    public Shield(int allType, int qty, boolean startup){
        super(allType, qty);
        attackDefense = false;
    }
}
