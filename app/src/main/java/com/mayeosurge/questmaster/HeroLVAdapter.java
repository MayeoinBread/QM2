package com.mayeosurge.questmaster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class HeroLVAdapter extends BaseAdapter {
    private Context ctx;
    private final List<Hero> heroes;

    public HeroLVAdapter(Context c, List<Hero> h){
        ctx = c;
        heroes = h;
    }

    @Override
    public int getCount(){
        return heroes.size();
    }

    @Override
    public Hero getItem(int position){
        return heroes.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        View gridView = convertView;
        if(gridView == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            gridView = inflater.inflate(R.layout.inv_lv_hero, null);
            Hero h = heroes.get(pos);
            ((TextView)gridView.findViewById(R.id.tvHName)).setText(h.name);
            String s = String.format(ctx.getResources().getString(R.string.hp), h.currentHealth, h.maxHealth);
            ((TextView)gridView.findViewById(R.id.tvHP)).setText(s);
            ProgressBar pb = (ProgressBar)gridView.findViewById(R.id.pbHealth);
            pb.setMax(h.maxHealth);
            pb.setProgress(h.currentHealth);
            ((ImageView)gridView.findViewById(R.id.ivHero)).setImageResource(ArrayVars.hColours[h.type-1]);
        }
        return gridView;
    }
}
