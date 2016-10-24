package com.mayeosurge.questmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
        for (Armour a:h.armour)
            tDiff += a != null ? a.stealth : 0;
        sb3.append(tDiff).append("\n");

        sb1.append("Strength\n");
        sb2.append(h.strength).append("\n");
        tDiff = 0;
        if(h.shield != null)
            tDiff += h.shield.strength;
        if(h.melee1 != null)
            tDiff += h.melee1.strength;
        for (Armour a:h.armour)
            tDiff += a != null ? a.strength : 0;
        sb3.append(tDiff).append("\n");

        sb1.append("Knowledge\n");
        sb2.append(h.knowledge).append("\n");
        tDiff = 0;
        for (Armour a:h.armour)
            tDiff += a != null ? a.knowledge : 0;
        sb3.append(tDiff).append("\n");

        sb1.append("Max Health\n");
        sb2.append(h.maxHealth).append("\n");
        tDiff = 0;
        for (Armour a:h.armour)
            tDiff += a != null ? a.health : 0;
        sb3.append(tDiff).append("\n");

        sb1.append("Magic\n");
        sb2.append(h.magic).append("\n");
        tDiff = 0;
        if(h.shield != null)
            tDiff += h.shield.magic;
        if(h.melee1 != null)
            tDiff += h.melee1.magic;
        for (Armour a:h.armour)
            tDiff += a != null ? a.magic : 0;
        sb3.append(tDiff).append("\n");

        ((TextView)findViewById(R.id.tvStat)).setText(sb1.toString());
        ((TextView)findViewById(R.id.tvVal)).setText(sb2.toString());
        ((TextView)findViewById(R.id.tvDiff)).setText(sb3.toString());
        ((TextView)findViewById(R.id.tvName)).setText(h.name);
        ((TextView)findViewById(R.id.tvLegion)).setText(legion);
        String successfulQ = h.successfulQuests+"";
        ((TextView)findViewById(R.id.tvQuests)).setText(successfulQ);

        ((ImageView)findViewById(R.id.ivShield)).setImageResource(h.shield!=null?ArrayVars.invImgs[h.shield.getItemId()]:R.drawable.inv_blank);
        ((ImageView)findViewById(R.id.ivMelee)).setImageResource(h.melee1!=null?ArrayVars.invImgs[h.melee1.getItemId()]:R.drawable.inv_blank);
        ((ImageView)findViewById(R.id.ivHelmet)).setImageResource(h.armour[0]!=null?ArrayVars.invImgs[h.armour[0].getItemId()]:R.drawable.inv_blank);
        ((ImageView)findViewById(R.id.ivRarm)).setImageResource(h.armour[1]!=null?ArrayVars.invImgs[h.armour[1].getItemId()]:R.drawable.inv_blank);
        ((ImageView)findViewById(R.id.ivLarm)).setImageResource(h.armour[2]!=null?ArrayVars.invImgs[h.armour[2].getItemId()]:R.drawable.inv_blank);
        ((ImageView)findViewById(R.id.ivTorso)).setImageResource(h.armour[3]!=null?ArrayVars.invImgs[h.armour[3].getItemId()]:R.drawable.inv_blank);

        gv = (GridView)findViewById(R.id.gvInventory);
        iga = new InvGridAdapter(this, h.inventory.inventoryList);
        gv.setAdapter(iga);
        gv.setOnItemClickListener(icl);

        ProgressBar pb = (ProgressBar)findViewById(R.id.pbHealth);
        pb.setMax(h.maxHealth);
        pb.setProgress(h.currentHealth);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        i.putExtra("hero", h);
        i.putExtra("pInv", playerInventory);
        setResult(RESULT_OK, i);
        finish();
    }

    public void eqArmour(View v){
        int sel = -1;
        switch(v.getId()){
            case R.id.ivHelmet:
                sel = 0;
                break;
            case R.id.ivRarm:
                sel = 1;
                break;
            case R.id.ivLarm:
                sel = 2;
                break;
            case R.id.ivTorso:
                sel = 3;
                break;
        }
        if(sel>-1){
            final int s = sel;
            PopupMenu popup = new PopupMenu(ctx, v);
            if(!h.armour[sel].heroOnly)
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Transfer");
            popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Dequip");
            if(!h.armour[sel].heroOnly)
                popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Delete");
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case 1:
                            transferItems(true, h.armour[s]);
                            return true;
                        case 2:
                            dequipItem(h.armour[s]);
                            return true;
                        case 3:
                            deleteItem(h.armour[s], true, 1);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }
    }

    public void eqMelee(View v){
        if(h.melee1 != null){
            PopupMenu popup = new PopupMenu(ctx, v);
            if(!h.melee1.heroOnly)
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Transfer");
            popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Dequip");
            if(!h.melee1.heroOnly)
                popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Delete");
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case 1:
                            transferItems(true, h.melee1);
                            return true;
                        case 2:
                            dequipItem(h.melee1);
                            return true;
                        case 3:
                            deleteItem(h.melee1, true, 1);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }
    }

    public void eqShield(View v){
        if(h.shield != null){
            PopupMenu popup = new PopupMenu(ctx, v);
            if(!h.shield.heroOnly)
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Transfer");
            popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Dequip");
            if(!h.shield.heroOnly)
                popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Delete");
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case 1:
                            transferItems(true, h.shield);
                            return true;
                        case 2:
                            dequipItem(h.shield);
                            return true;
                        case 3:
                            deleteItem(h.shield, true, 1);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }
    }

    AdapterView.OnItemClickListener icl = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InvItem i = (InvItem)parent.getItemAtPosition(position);
            System.out.println(i.name+"_"+i.getItemId()+"_"+i.isEquippable+"_"+i.isUsable+"_"+i.getClass());
            invItemPos = i.getItemId();
            PopupMenu popup = new PopupMenu(ctx, view);
            if(i.isUsable)
                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Use");
            if(i.isEquippable)
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Equip");
            if(!i.heroOnly) {
                popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Delete");
                popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Transfer");
            }
            popup.setOnMenuItemClickListener(micl);
            popup.show();
        }
    };

    PopupMenu.OnMenuItemClickListener micl = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            InvItem it = h.inventory.findItemById(invItemPos);
            switch(item.getItemId()){
                case 0:
                    return true;
                case 1:
                    equipItem(it);
                    return true;
                case 2:
                    deleteItem(it, false, 1);
                    return true;
                case 3:
                    transferItems(false, it);
                    return true;
                default:
                    return false;
            }
        }
    };

    private void transferItems(boolean equipped, InvItem item){
        h.inventory.delete(item, 1);
        if(equipped) h.dequipWeapon((Weapon)item);
        if(item instanceof Shield) playerInventory.add(new Shield((Weapon)item), 1);
        else if(item instanceof Melee) playerInventory.add(new Melee((Weapon)item), 1);
        else playerInventory.add(new InvItem(item), 1);
        setProperties();
    }

    private void equipItem(InvItem item){
        if(item instanceof Weapon)
            h.equipWeapon((Weapon) item);
        else if(item instanceof Armour)
            h.equipArmour((Armour) item);
        setProperties();
    }

    private void dequipItem(InvItem item){
        if(item instanceof Weapon)
            h.dequipWeapon((Weapon) item);
        else if(item instanceof Armour)
            h.dequipArmour((Armour) item);
        setProperties();
    }

    private void deleteItem(InvItem item, boolean equipped, int qty){
        if(equipped)
            h.dequipWeapon((Weapon)item);
        h.inventory.delete(item, qty);
        setProperties();
    }
}
