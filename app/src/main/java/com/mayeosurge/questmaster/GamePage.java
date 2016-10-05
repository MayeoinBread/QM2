package com.mayeosurge.questmaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class GamePage extends Activity {

    String[] names = {"Red", "Green", "Blue"};

    int totalGold = 0;

    Context c;

    Hero h;

    String legion;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage);
        int hero = getIntent().getIntExtra("hero", 1);
        legion = getIntent().getStringExtra("name");
        h = new Hero(hero);
        ((TextView)findViewById(R.id.tvLegion)).setText("Legion: "+legion);
        ((TextView)findViewById(R.id.tvHero)).setText("Hero: "+names[hero-1]);
        findViewById(R.id.quest1).setOnClickListener(questCL);
        findViewById(R.id.quest2).setOnClickListener(questCL);
        findViewById(R.id.quest3).setOnClickListener(questCL);
        c = this;
    }

    View.OnClickListener questCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int qNum;
            switch(v.getId()){
                default:
                case R.id.quest1:
                    qNum = 0;
                    break;
                case R.id.quest2:
                    qNum = 1;
                    break;
                case R.id.quest3:
                    qNum = 2;
                    break;
            }
            final Hero ah = h;
            final int qn = qNum;
            final View fV = v;
            AlertDialog.Builder a = new AlertDialog.Builder(c);
            a.setTitle(ArrayVars.qTitles[qNum])
                    .setMessage(ArrayVars.qMsg[qNum])
                    .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Quest q = new Quest(qn);
                            q.runQuest(ah);
                            findViewById(fV.getId()).setEnabled(false);
                            ((TextView)findViewById(R.id.tvOutput)).setText("Quest success chance: "+q.successChance);
                            if(q.questSucceeded)
                                addReward(h, q.reward, q.goldReward);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    };

    public void statsPopup(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(h.name)
                .setMessage(getHeroStats(h))
                .show();
    }

    public void statsActivity(View v){
        Intent i = new Intent(GamePage.this, HeroStatsActivity.class);
        i.putExtra("hero", h);
        i.putExtra("legion", legion);
        startActivity(i);
    }

    public void equipShield(View v){
        h.equipWeapon((Weapon)h.inventory.findItemById(8));
        updateViews();
    }

    public void equipMelee(View v){
        h.equipWeapon((Weapon)h.inventory.findItemById(5));
        updateViews();
    }

    public void dequipShield(View v){
        h.dequipWeapon((Weapon)h.inventory.findItemById(8));
        updateViews();
    }

    public void dequipMelee(View v){
        h.dequipWeapon((Weapon)h.inventory.findItemById(5));
        updateViews();
    }

    public String getHeroStats(Hero hero){
        StringBuilder sb = new StringBuilder();
        sb.append("Cost: ").append(hero.cost).append("\n");
        sb.append("Successful Quests: ").append(hero.successfulQuests).append("\n");
        sb.append("Type: ").append(hero.type).append("\n");
        sb.append("Stealth: ").append(hero.stealth).append("\n");
        sb.append("Strength: ").append(hero.strength).append("\n");
        sb.append("Knowledge: ").append(hero.knowledge).append("\n");
        sb.append("Max Health: ").append(hero.maxHealth).append("\n");
        sb.append("Current Health: ").append(hero.currentHealth).append("\n");
        sb.append("Magic: ").append(hero.magic).append("\n");
        sb.append("\nEquipped Items:\n");
        sb.append("Shield: ").append(hero.shield==null ? "":hero.shield.name).append("\n");
        sb.append("Melee1: ").append(hero.melee1==null ? "":hero.melee1.name).append("\n");
        sb.append("\nInventory:\n");
        for (InvItem i:hero.inventory.inventoryList) {
            sb.append(" - ").append(i.name).append("  (x").append(i.qty).append(")\n");
        }
        return sb.toString();
    }

    public void addReward(Hero h, List<InvItem> reward, int goldReward){
        h.passQuest();
        totalGold += goldReward;
        if(reward != null){
            for(InvItem r:reward){
                h.inventory.add(r, r.getQty());
            }
        }
        updateViews();
    }

    public void updateViews(){
        if(h.inventory.findItemById(8) != null)
            findViewById(R.id.btnShield).setEnabled(true);
        if(h.inventory.findItemById(5) != null)
            findViewById(R.id.btnMelee).setEnabled(true);
        findViewById(R.id.dequipShield).setEnabled(h.shield != null);
        findViewById(R.id.dequipMelee).setEnabled(h.melee1 != null);
        ((TextView)findViewById(R.id.tvGold)).setText("Gold: "+totalGold);
    }
}
