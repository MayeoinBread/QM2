package com.mayeosurge.questmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class HeroStatsActivity extends Activity {

    Hero h;
    String legion;
    Context ctx;
    GridView gv;
    InvGridAdapter iga;

    Inventory playerInventory;

    int invItemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herostats);
        h = (Hero) getIntent().getSerializableExtra("hero");
        playerInventory = (Inventory) getIntent().getSerializableExtra("pInv");
        legion = getIntent().getStringExtra("legion");
        ctx = this;

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

        ((ImageView)findViewById(R.id.ivShield)).setImageResource(h.shield!=null?ArrayVars.invImgs[h.shield.getItemId()]:R.drawable.inv_blank);
        ((ImageView)findViewById(R.id.ivMelee)).setImageResource(h.melee1!=null?ArrayVars.invImgs[h.melee1.getItemId()]:R.drawable.inv_blank);

        gv = (GridView)findViewById(R.id.gvInventory);
        iga = new InvGridAdapter(this, h.inventory.inventoryList);
        gv.setAdapter(iga);
        gv.setOnItemClickListener(icl);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        i.putExtra("hero", h);
        i.putExtra("pInv", playerInventory);
        setResult(RESULT_OK, i);
        finish();
    }

    public void eqMelee(View v){
        if(h.melee1 != null){
            PopupMenu popup = new PopupMenu(ctx, v);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.inv_transfer:
                            h.inventory.delete(h.melee1, 1);
                            playerInventory.add(new Weapon(h.melee1), 1);
                            h.dequipWeapon(h.melee1);
                            setProperties();
                            return true;
                        case R.id.inv_dequip:
                            h.dequipWeapon(h.melee1);
                            setProperties();
                            return true;
                        case R.id.inv_delete:
                            h.inventory.delete(h.melee1, 1);
                            h.dequipWeapon(h.melee1);
                            setProperties();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.inflate(R.menu.menu_dequip);
            popup.show();
        }
    }

    public void eqShield(View v){
        if(h.shield != null){
            PopupMenu popup = new PopupMenu(ctx, v);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.inv_transfer:
                            h.inventory.delete(h.shield, 1);
                            playerInventory.add(new Weapon(h.shield), 1);
                            h.dequipWeapon(h.shield);
                            setProperties();
                            return true;
                        case R.id.inv_dequip:
                            h.dequipWeapon(h.shield);
                            setProperties();
                            return true;
                        case R.id.inv_delete:
                            h.inventory.delete(h.shield, 1);
                            h.dequipWeapon(h.shield);
                            setProperties();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.inflate(R.menu.menu_dequip);
            popup.show();
        }
    }

    AdapterView.OnItemClickListener icl = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InvItem i = (InvItem)parent.getItemAtPosition(position);
            invItemPos = i.getItemId();
            PopupMenu popup = new PopupMenu(ctx, view);
            popup.setOnMenuItemClickListener(micl);
            popup.inflate(R.menu.inv_menu);
            popup.show();
        }
    };

    PopupMenu.OnMenuItemClickListener micl = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            InvItem it = h.inventory.findItemById(invItemPos);
            switch(item.getItemId()){
                case R.id.inv_equip:
                    if (it instanceof Weapon)
                        h.equipWeapon((Weapon)it);
                    setProperties();
                    return true;
                case R.id.inv_transfer:
                    h.inventory.delete(it, 1);
                    if(it instanceof Weapon)
                        playerInventory.add(new Weapon((Weapon)it), 1);
                    else
                        playerInventory.add(new InvItem(it), 1);
                    setProperties();
                    return true;
                case R.id.inv_dequip:
                    if(it instanceof Weapon)
                        h.dequipWeapon((Weapon)it);
                    setProperties();
                    return true;
                case R.id.inv_delete:
                    h.inventory.delete(it, 1);
                    setProperties();
                    return true;
                default:
                    return false;
            }
        }
    };
}
