package com.mayeosurge.questmaster;

import java.io.Serializable;

public class InvItem implements Serializable {

    String name;
    int id;
    int qty;
    boolean isUsable;
    boolean isEquippable;
    boolean isQuestItem;

    public InvItem(int id, int qty, boolean iUse, boolean iEquip, boolean iQI){
        this.id = id;
        isUsable = iUse;
        isEquippable = iEquip;
        isQuestItem = iQI;
        try{
            name = ArrayVars.items[id];
        }
        catch (IndexOutOfBoundsException e){
            name = id+"_placeholder";
        }
        this.qty = qty;
    }

    public void addQty(int n){
        qty += n;
    }

    public void delQty(int n){
        qty = (n < qty) ? qty-n : 0;
    }

    public int getItemId(){
        return id;
    }

    public int getQty(){
        return qty;
    }
}
