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
    int[] armour;
    Weapon shield;
    Weapon melee1;
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
        Random r = new Random();
        cost = r.nextInt(100);
        successfulQuests = 0;
        type = t;
        // helmet, armL, armR, torso, legL, legR
        armour = new int[6];
        // shield, melee 1, melee 2?
        shield = null;
        melee1 = null;
        inventory = new Inventory();
        stealth = aStealth[t-1];
        strength = aStrength[t-1];
        knowledge = aKnowledge[t-1];
        maxHealth = aHealth[t-1];
        currentHealth = maxHealth;
        magic = aMagic[t-1];
        name = ArrayVars.names[t-1];
    }

    public void passQuest(){
        successfulQuests++;
    }

    public void equipWeapon(Weapon w){
        strength += w.strength;
        stealth += w.stealth;
        magic += w.magic;
        inventory.delete(w, 1);
        if(!w.attackDefense){
            if(shield != null)
                dequipWeapon(shield);
            shield = w;
        }else{
            if(melee1 != null)
                dequipWeapon(melee1);
            melee1 = w;
        }
    }

    public void dequipWeapon(Weapon w){
        strength -= w.strength;
        stealth -= w.stealth;
        magic -= w.magic;
        inventory.add(w, 1);
        System.out.println("attackDefense"+w.attackDefense);
        if(!w.attackDefense)
            shield = null;
        else
            if(melee1 != null && melee1.getItemId() == w.id)
                melee1 = null;
    }
}
