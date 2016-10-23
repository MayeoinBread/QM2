package com.mayeosurge.questmaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GamePage extends Activity {

    String[] names = {"Red", "Green", "Blue"};

    int totalGold = 0;

    Context c;
    Inventory playerInventory;
    GridView pGV;
    InvGridAdapter pIGA;

    Hero h;

    String legion;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage);
        int hero = getIntent().getIntExtra("hero", 0);
        legion = getIntent().getStringExtra("legion");
        h = new Hero(hero);
        playerInventory = new Inventory();
        ((TextView)findViewById(R.id.tvLegion)).setText("Legion: "+legion);
        c = this;
        createInventory();
        setGridView();
    }

    private void createInventory(){
        for(int i=0; i<ArrayVars.items.length; i++){
            if(!ArrayVars.items[i].equals("")){
                if(i<5)
                    playerInventory.add(new InvItem(i, 1, true, true, false, false), 1);
                else if(i<10)
                    playerInventory.add(new Melee(i, 1, true), 1);
                else
                    playerInventory.add(new Shield(i, 1, true), 1);
            }
        }
    }

    private void setGridView(){
        pGV = (GridView)findViewById(R.id.gvPlayerInventory);
        pIGA = new InvGridAdapter(this, playerInventory.inventoryList);
        pGV.setAdapter(pIGA);

        List<Hero> hList = new ArrayList<>();
        hList.add(h);
        GridView hgv = (GridView)findViewById(R.id.gvHero);
        HeroLVAdapter hlv = new HeroLVAdapter(this, hList);
        hgv.setAdapter(hlv);
        hgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hero h = (Hero)parent.getItemAtPosition(position);
                if(h != null)
                    statsActivity(h);
            }
        });
    }

    public void statsPopup(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(h.name)
                .setMessage(getHeroStats(h))
                .show();
    }

    public void statsActivity(Hero hero){
        Intent i = new Intent(GamePage.this, HeroStatsActivity.class);
        i.putExtra("hero", hero);
        i.putExtra("pInv", playerInventory);
        i.putExtra("legion", legion);
        //startActivity(i);
        startActivityForResult(i, 1);
    }

    public void questActivity(View v){
        Intent i = new Intent(GamePage.this, ActivityQuests.class);
        i.putExtra("hero", h);
        i.putExtra("totalGold", totalGold);
        startActivityForResult(i, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    h = (Hero)data.getSerializableExtra("hero");
                    playerInventory = (Inventory)data.getSerializableExtra("pInv");
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    h = (Hero)data.getSerializableExtra("hero");
                    totalGold = data.getIntExtra("totalGold", 0);
                }
                break;
        }
        updateViews();
        setGridView();
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
        ((TextView)findViewById(R.id.tvGold)).setText("Gold: "+totalGold);
    }
}
