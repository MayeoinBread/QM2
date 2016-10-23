package com.mayeosurge.questmaster;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

public class Conversions {

    public static String rewardToString(List<InvItem> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null){
            for (InvItem i : list) {
                sb.append(i.id).append(":").append(i.getQty()).append(";");
            }
        }
        return sb.toString();
    }

    public static List<InvItem> stringToReward(String string){
        List<String> ls = new ArrayList<>();
        List<InvItem> ret = new ArrayList<>();
        if(string.length() > 3) {
            String strip = string;
            do {
                ls.add(strip.substring(0, strip.indexOf(';')));
                if (strip.length() > strip.indexOf(';') + 1)
                    strip = strip.substring(strip.indexOf(';') + 1);
                else strip = "";
            } while (strip.contains(";"));

            for (String s : ls) {
                int id = Integer.parseInt(s.substring(0, s.indexOf(':')));
                int qty = Integer.parseInt(s.substring(s.indexOf(':') + 1));
                // Not sure of bool's here...
                ret.add(new InvItem(id, qty, false, true, true));
            }
            return ret;
        }
        return ret;
    }

    public static String lvlsToString(Quest q){
        return String.valueOf(q.minStealth) + "," + q.minStrength + "," +
                q.minKnowledge + "," + q.minMagic;
    }

    public static int[] stringToLevels(String s){
        int[] levels = new int[4];
        for(int i=0; i<levels.length-1; i++){
            levels[i] = Integer.parseInt(s.substring(0, s.indexOf(',')));
            s = s.substring(s.indexOf(',')+1);
        }
        levels[levels.length-1] = Integer.parseInt(s);
        return levels;
    }

    public static ContentValues questToArgs(Quest q){
        ContentValues args = new ContentValues();
        args.put(DbQuestAdapter.KEY_TITLE, q.title);
        args.put(DbQuestAdapter.KEY_DESC, q.description);
        args.put(DbQuestAdapter.KEY_REWARD, rewardToString(q.reward));
        args.put(DbQuestAdapter.KEY_GOLD, q.goldReward);
        args.put(DbQuestAdapter.KEY_LEVELS, lvlsToString(q));
        args.put(DbQuestAdapter.KEY_ACTIVE, q.questActive ? 1:0);
        args.put(DbQuestAdapter.KEY_START_TIME, q.startTime);
        args.put(DbQuestAdapter.KEY_DURATION, q.duration);
        args.put(DbQuestAdapter.KEY_COMPLETED, q.questSucceeded ? 1:0);

        return args;
    }
}
