package com.mayeosurge.questmaster;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HeroStatsActivity extends Activity {

    Hero h;
    String legion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herostats);
        h = (Hero) getIntent().getSerializableExtra("hero");
        legion = getIntent().getStringExtra("legion");

        setProperties();
    }

    private void setProperties(){
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();

        sb1.append("Stat\n\n").append("Stealth\n");
        sb2.append("Value\n\n").append(h.stealth).append("\n");
        sb3.append("Diff.\n\n");
        int tDiff = 0;
        if(h.shield != null)
            tDiff += h.shield.stealth;
        if(h.melee1 != null)
            tDiff += h.melee1.stealth;
        sb3.append(tDiff).append("\n");

        sb1.append("Strength\n");
        sb2.append(h.strength).append("\n");
        tDiff = 0;
        if(h.shield != null)
            tDiff += h.shield.strength;
        if(h.melee1 != null)
            tDiff += h.melee1.strength;
        sb3.append(tDiff).append("\n");

        sb1.append("Knowledge\n");
        sb2.append(h.knowledge).append("\n");
        tDiff = 0;
        sb3.append(tDiff).append("\n");

        sb1.append("Max Health\n");
        sb2.append(h.maxHealth).append("\n");
        tDiff = 0;
        sb3.append(tDiff).append("\n");

        sb1.append("Magic\n");
        sb2.append(h.magic).append("\n");
        tDiff = 0;
        if(h.shield != null)
            tDiff += h.shield.magic;
        if(h.melee1 != null)
            tDiff += h.melee1.magic;
        sb3.append(tDiff).append("\n");

        ((TextView)findViewById(R.id.tvStat)).setText(sb1.toString());
        ((TextView)findViewById(R.id.tvVal)).setText(sb2.toString());
        ((TextView)findViewById(R.id.tvDiff)).setText(sb3.toString());
        ((TextView)findViewById(R.id.tvName)).setText(h.name);
        ((TextView)findViewById(R.id.tvLegion)).setText(legion);
        ((TextView)findViewById(R.id.tvQuests)).setText(h.successfulQuests+"");

        if(h.shield!=null)
            ((ImageView)findViewById(R.id.ivShield)).setImageResource(ArrayVars.invImgs[h.shield.getItemId()]);
        if(h.melee1!= null)
            ((ImageView)findViewById(R.id.ivMelee)).setImageResource(ArrayVars.invImgs[h.melee1.getItemId()]);

        GridView gv = (GridView)findViewById(R.id.gvInventory);
        gv.setAdapter(new InvGridAdapter(this, h.inventory.inventoryList));
    }
}
