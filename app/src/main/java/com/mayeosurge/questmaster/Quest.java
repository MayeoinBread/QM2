package com.mayeosurge.questmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quest {

    int id;
    int minStealth;
    int minStrength;
    int minKnowledge;
    int minMagic;
    double successChance;
    List<InvItem> reward;
    int goldReward;
    boolean questSucceeded = false;

    public Quest(int id){
        this.id = id;
        reward = new ArrayList<>();
        minStealth = ArrayVars.questVars[id][0];
        minStrength = ArrayVars.questVars[id][1];
        minKnowledge = ArrayVars.questVars[id][2];
        minMagic = ArrayVars.questVars[id][3];
        setReward(ArrayVars.questRewards[id], ArrayVars.questGold[id]);
    }

    public void getMissionSuccessChance(Hero h){
        int pC = 0;
        // Armour affects stealth (heavy = loud)
        if(h.stealth >= minStealth)
            pC += 10;
        else
            pC += minStealth - h.stealth;

        // Weapons affect strength
        if(h.strength >= minStrength)
            pC += 10;
        else
            pC += minStrength - h.stealth;

        if(h.knowledge >= minKnowledge)
            pC += 10;
        else
            pC += minKnowledge - h.stealth;

        if(h.magic >= minMagic)
            pC += 10;
        else
            pC += minMagic - h.magic;

        // Armour affects health?
        pC += (h.currentHealth / h.maxHealth)*10;

        successChance = pC / 50.0;
    }

    public void setReward(InvItem[] r, int g){
        Collections.addAll(reward, r);
        goldReward = g;
    }

    public void runQuest(Hero h){
        getMissionSuccessChance(h);
        if(id == 1)
            goldReward += h.cost;
        System.out.println("Quest "+id+":\nSuccess chance: "+successChance);
        questSucceeded = true;
    }
}
