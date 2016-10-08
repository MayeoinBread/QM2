package com.mayeosurge.questmaster;

import java.io.Serializable;
import java.util.Random;

public class Hero implements Serializable {

    int ID;
    int allegiance;
    int cost;
    int chanceRun;
    int stealth;
    int strength;
    int knowledge;
    int maxHealth;
    int currentHealth;
    int magic;
    Armour[] armour;
    Shield shield;
    Melee melee1;
    Inventory inventory;
    int type;
    int successfulQuests;
    String name;

    int aStealth[] = {2, 5, 7};
    int aStrength[] = {9, 7, 3};
    int aKnowledge[] = {2, 5, 5};
    int aHealth[] = {8, 6, 6};
    int aMagic[] = {0, 4, 1};

    final int INV_MAX_SIZE = 32;

    public Hero(){}

    public Hero(int t){
        // t = -1 for "null"/"blank" heroes ie. opening pager array
        type = t;
        if(t != -1){
            Random r = new Random();
            cost = r.nextInt(100);
            successfulQuests = 0;
            inventory = new Inventory();
            //TODO Set to number of pieces of armour
            // helmet, armL, armR, torso, legL, legR
            armour = new Armour[4];
            // shield, melee 1, melee 2?
            shield = null;
            melee1 = null;
            stealth = aStealth[t];
            strength = aStrength[t];
            knowledge = aKnowledge[t];
            maxHealth = aHealth[t];
            currentHealth = maxHealth;
            magic = aMagic[t];
            name = ArrayVars.names[t];

            spawnHasWeapon();
            spawnHasArmour();
        }
    }

    public void passQuest(){
        successfulQuests++;
    }

    private void spawnHasWeapon(){
        Random r = new Random();
        int rand = r.nextInt(5);
        Weapon w;
        if(rand == 4)
            w = new Shield(0, 1);
        else if(rand == 2)
            w = new Melee(0, 1);
        else
            w = null;
        if(w!=null){
            w.heroOnly = true;
            equipWeapon(w);
        }
    }

    private void spawnHasArmour(){
        Armour a;
        Random r = new Random();
        int rand = r.nextInt(4);
        a = new Armour(rand, 1);
        a.heroOnly = true;
        equipArmour(a);
    }

    public void equipWeapon(Weapon w){
        strength += w.strength;
        stealth += w.stealth;
        magic += w.magic;
        inventory.delete(w, 1);
        if(w instanceof Shield){
            if(shield != null) dequipWeapon(shield);
            shield = (Shield)w;
        }else{
            if(melee1 != null) dequipWeapon(melee1);
            melee1 = (Melee)w;
        }
    }

    public void equipArmour(Armour a){
        strength += a.strength;
        stealth += a.stealth;
        magic += a.magic;
        maxHealth += a.health;
        knowledge += a.knowledge;
        inventory.delete(a, 1);
        armour[a.getItemId()-ArrayVars.ARMOUR_START] = a;
    }

    public void dequipWeapon(Weapon w){
        if((w instanceof Shield && shield != null) || (w instanceof Melee && melee1 != null)) {
            strength -= w.strength;
            stealth -= w.stealth;
            magic -= w.magic;
            inventory.add(w, 1);
            if (w instanceof Shield) shield = null;
            else if (melee1 != null && melee1.getItemId() == w.id)
                melee1 = null;
        }
    }

    public void dequipArmour(Armour a){
        strength -= a.strength;
        stealth -= a.stealth;
        magic -= a.magic;
        maxHealth -= a.health;
        knowledge -= a.knowledge;
        inventory.add(a, 1);
        armour[a.getItemId()-ArrayVars.ARMOUR_START]=null;
    }
}
