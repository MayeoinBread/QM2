package com.mayeosurge.questmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Quest {

    int id;
    int minStealth;
    int minStrength;
    int minKnowledge;
    int minMagic;
    double successChance;
    List<InvItem> reward;
    int goldReward;
    String description;
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

    public Quest(int id, String desc){
        this.id = id;
        reward = new ArrayList<>();
        description = desc;
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

    public static Quest makeQuest(){
        String[][] qString;
        StringBuilder sb = new StringBuilder();
        sb.append("I need ");
        Random r = new Random();

        int rand;
        int rIW;

        int c1 = r.nextInt(3);
        switch (c1){
            case 0:
            default:
                qString = ArrayVars.qMaker1;
                break;
            case 1:
                qString = ArrayVars.qMaker2;
                break;
            case 2:
                qString = ArrayVars.qMaker3;
                break;
        }

        int s1 = r.nextInt(qString[0].length);
        int s2 = r.nextInt(qString[1].length);
        int s3 = r.nextInt(qString[2].length);
        int s4 = r.nextInt(qString[3].length);
        int loc = r.nextInt(ArrayVars.locations.length);
        int rwrd = ArrayVars.rewards[c1][s2];

        sb.append(qString[0][s1]).append(" ")
                .append(qString[1][s2]).append(" ")
                .append(qString[2][s3]).append(" ")
                .append(qString[3][s4]).append(" ")
                .append(ArrayVars.locations[loc]).append(".\n\n")
                .append("Reward:\n- ");

        Quest q = new Quest(5, sb.toString());

        switch(rwrd){
            case 1:
                q.goldReward = (100 + r.nextInt(10)*10);
                q.reward = null;
                break;
            case 2:
                int rItem = r.nextInt(3);
                q.reward.add(new InvItem(rItem, 1, true, false, false));
                q.goldReward = 0;
                break;
            case 3:
                int rWeapon = 5 + r.nextInt(7);
                if(rWeapon != 9) {
                    if (rWeapon >= ArrayVars.SHIELD_START) {
                        System.out.println("rWeapon: " + rWeapon);
                        q.reward.add(new Shield(rWeapon, 1, false));
                    } else{
                        System.out.println("rWeapon: "+rWeapon);
                        q.reward.add(new Melee(rWeapon, 1, false));
                    }
                }
                q.goldReward = 0;
                break;
            case 4:
                rIW = r.nextInt(19);
                if(rIW != 3 && rIW != 4 && rIW != 9 && rIW != 12 && rIW != 13 && rIW != 14){
                    if(rIW >= ArrayVars.ARMOUR_START){
                        System.out.println("rIW: "+rIW);
                        q.reward.add(new Armour(rIW, 1, false));
                    }else if(rIW >= ArrayVars.SHIELD_START) {
                        System.out.println("rIW: " + rIW);
                        q.reward.add(new Shield(rIW, 1, false));
                    }else if(rIW >= ArrayVars.WEAPON_START) {
                        q.reward.add(new Melee(rIW, 1, false));
                        System.out.println("rIW: " + rIW);
                    }else{
                        System.out.println("rIW: "+rIW);
                        q.reward.add(new InvItem(rIW, 1, true, false, false));
                    }
                }
                q.goldReward = 0;
                break;
            case 5:
                rand = r.nextInt(4);
                if(rand == 2){
                    rIW = r.nextInt(19);
                    if(rIW != 3 && rIW != 4 && rIW != 9 && rIW != 12 && rIW != 13 && rIW != 14){
                        if(rIW >= ArrayVars.ARMOUR_START) {
                            System.out.println("rIW: " + rIW);
                            q.reward.add(new Armour(rIW, 1, false));
                        }else if(rIW >= ArrayVars.SHIELD_START) {
                            System.out.println("rIW: " + rIW);
                            q.reward.add(new Shield(rIW, 1, false));
                        }else if(rIW >= ArrayVars.WEAPON_START) {
                            System.out.println("rIW: " + rIW);
                            q.reward.add(new Melee(rIW, 1, false));
                        }else{
                            System.out.println("rIW: "+rIW);
                            q.reward.add(new InvItem(rIW, 1, true, false, false));
                        }
                    }
                    q.goldReward = 0;
                }else {
                    q.goldReward = 100 + r.nextInt(10) * 10;
                    q.reward = null;
                }
                break;
            case 6:
                rand = r.nextInt(4);
                if(rand != 2){
                    rIW = r.nextInt(19);
                    if(rIW != 3 && rIW != 4 && rIW != 9 && rIW != 12 && rIW != 13 && rIW != 14){
                        if(rIW >= ArrayVars.ARMOUR_START) {
                            System.out.println("rIW: " + rIW);
                            q.reward.add(new Armour(rIW, 1, false));
                        }else if(rIW >= ArrayVars.SHIELD_START) {
                            System.out.println("rIW: " + rIW);
                            q.reward.add(new Shield(rIW, 1, false));
                        }else if(rIW >= ArrayVars.WEAPON_START) {
                            System.out.println("rIW: " + rIW);
                            q.reward.add(new Melee(rIW, 1, false));
                        }else{
                            System.out.println("rIW: "+rIW);
                            InvItem i = new InvItem(rIW, 1, true, false, true);
                            System.out.println(i.name);
                            q.reward.add(i);
                        }
                    }
                    q.goldReward = 0;
                }else {
                    q.goldReward = 100 + r.nextInt(10) * 10;
                    q.reward = null;
                }
                break;
        }

        if(q.reward != null && q.reward.size() > 0){
            q.description += q.reward.get(0).name;
        }else{
            q.description += q.goldReward+" Gold";
        }

        return q;
    }
}
