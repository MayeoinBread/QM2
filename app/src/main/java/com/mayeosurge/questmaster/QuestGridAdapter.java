package com.mayeosurge.questmaster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class QuestGridAdapter extends BaseAdapter{
    private Context ctx;
    private final List<Quest> quests;

    public QuestGridAdapter(Context ctx, List<Quest> quests){
        this.ctx = ctx;
        this.quests = quests;
    }

    @Override
    public int getCount(){
        return quests.size();
    }

    @Override
    public Quest getItem(int position){
        return quests.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int pos, View cView, ViewGroup parent){
        View gridView = cView;
        Quest cQuest = quests.get(pos);
        if(gridView == null){
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            gridView = inflater.inflate(R.layout.quest_grid_item, null);
            ((TextView)gridView.findViewById(R.id.qTitle)).setText(cQuest.title);
            String des = cQuest.description.length() > 103 ? cQuest.description.substring(0, 100)+"..." : cQuest.description;
            ((TextView)gridView.findViewById(R.id.qDesc)).setText(des);
            String d = String.format(gridView.getResources().getString(R.string.duration), cQuest.duration/1000);
            ((TextView)gridView.findViewById(R.id.qDuration)).setText(d);
        }
        return gridView;
    }
}

