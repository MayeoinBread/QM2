package com.mayeosurge.questmaster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class InvGridAdapter extends BaseAdapter {
    private Context ctx;
    private final List<InvItem> items;

    public InvGridAdapter(Context ctx, List<InvItem> items){
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public InvItem getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        View gridView = convertView;
        if(gridView == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            gridView = inflater.inflate(R.layout.inv_grid_item, null);
            String qty = items.get(pos).getQty()+"";
            ((TextView)gridView.findViewById(R.id.tvQty)).setText(qty);
            Bitmap bmp = BitmapFactory.decodeResource(gridView.getResources(), ArrayVars.invImgs[items.get(pos).getItemId()]);
            bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), false);
            //((ImageView)gridView.findViewById(R.id.ivItem)).setImageResource(ArrayVars.invImgs[items.get(pos).getItemId()]);
            ((ImageView)gridView.findViewById(R.id.ivItem)).setImageBitmap(bmp);
            if(items.get(pos).heroOnly)
                ((ImageView)gridView.findViewById(R.id.ivBG)).setImageResource(R.drawable.inv_gv_item_hero);
        }
        return gridView;
    }
}
