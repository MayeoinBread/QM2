package com.mayeosurge.questmaster;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Quest {

    //TODO pass Hero id (?) as item in Quest for use later


    //TODO Quest: active = started, succeeded = finished successfully, completed = succeeded and collected

    long id;
    int minStealth;
    int minStrength;
    int minKnowledge;
    int minMagic;
    // Quest time length in millis
    long duration;
    long startTime;
    double successChance;
    List<InvItem> reward;
    int goldReward;
    String title;
    String description;
    boolean questActive;
    boolean questSucceeded;
    boolean questCompleted;

    public Quest(int id){
        this.id = id+1;
        reward = new ArrayList<>();
        minStealth = ArrayVars.questVars[id][0];
        minStrength = ArrayVars.questVars[id][1];
        minKnowledge = ArrayVars.questVars[id][2];
        minMagic = ArrayVars.questVars[id][3];
        duration = ArrayVars.questDuration[id];
        setReward(ArrayVars.questRewards[id], ArrayVars.questGold[id]);
    }

    // Quest from DB, KEEP!!!
    public Quest(long id){
        this.id = id;
        id--;
        reward = new ArrayList<>();
        Random r = new Random();
        if(id < 3){
            title = ArrayVars.qTitles[(int)id];
            description = ArrayVars.qMsg[(int)id];
            minStealth = ArrayVars.questVars[(int)id][0];
            minStrength = ArrayVars.questVars[(int)id][1];
            minKnowledge = ArrayVars.questVars[(int)id][2];
            minMagic = ArrayVars.questVars[(int)id][3];
            setReward(ArrayVars.questRewards[(int)id], ArrayVars.questGold[(int)id]);
        }
        //System.out.println("Quest: id: "+id);
        //System.out.println("Quest: Desc: "+description);
        duration = ArrayVars.questDuration[r.nextInt(ArrayVars.questDuration.length)];
        questActive = false;
        questSucceeded = false;
        questCompleted = false;
    }

    public Quest(Cursor c){
        id = c.getInt(0);
        title = c.getString(1);
        description = c.getString(2);
        reward = Conversions.stringToReward(c.getString(3));
        goldReward = c.getInt(4);
        int[] levels = Conversions.stringToLevels(c.getString(5));
        minStealth = levels[0];
        minStrength = levels[1];
        minKnowledge = levels[2];
        minMagic = levels[3];
        questActive = c.getInt(6) == 1;
        startTime = c.getInt(7);
        duration = c.getInt(8);
        questSucceeded = c.getInt(9) == 1;
        questCompleted = c.getInt(10) == 1;
    }

    public Quest(int id, String desc){
        Random r = new Random();
        this.id = id;
        reward = new ArrayList<>();
        description = desc;
        duration = ArrayVars.questDuration[r.nextInt(3)];
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
        if(SystemClock.elapsedRealtime() < startTime+duration){
            if(id == 1)
                goldReward += h.cost;
            System.out.println("Quest "+id+":\nSuccess chance: "+successChance);
            questSucceeded = true;
        }
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

        //TODO ID (5) is superfluous, just leave String as an argument
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

        q.questActive = false;
        q.questSucceeded = false;
        q.questCompleted = false;
        q.title = "Random Quest";

        return q;
    }
}
