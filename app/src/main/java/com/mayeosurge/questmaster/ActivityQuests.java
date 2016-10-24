package com.mayeosurge.questmaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuests extends Activity {

    Hero h;
    int totalGold;
    Context c;
    List<Quest> activeQuests;
    List<Quest> succeededQuests;
    GridView aGV;
    QuestGridAdapter aGA;
    GridView cGV;
    QuestGridAdapter cGA;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        h = (Hero) getIntent().getSerializableExtra("hero");
        totalGold = getIntent().getIntExtra("totalGold", 0);

        c = this;

        initItems();

        findViewById(R.id.quest1).setOnClickListener(questCL);
        findViewById(R.id.quest2).setOnClickListener(questCL);
        findViewById(R.id.quest3).setOnClickListener(questCL);
    }

    private void setGridView(){
        aGV = (GridView)findViewById(R.id.gvActQuest);
        aGA = new QuestGridAdapter(this, activeQuests);
        aGV.setAdapter(aGA);

        cGV = (GridView)findViewById(R.id.gvCompQuest);
        cGA = new QuestGridAdapter(this, succeededQuests);
        cGV.setAdapter(cGA);
        cGV.setOnItemClickListener(ocl);
    }

    AdapterView.OnItemClickListener ocl = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Quest q = succeededQuests.get(position);
            if(checkReward(q)) {
                succeededQuests.remove(q);
                Toast.makeText(c, "Quest \""+q.title+"\" completed. Reward collected!", Toast.LENGTH_SHORT).show();
            }
            cGA.notifyDataSetChanged();
        }
    };

    private void setViews(){
        for(Quest q:activeQuests){
            if(testFinished(q)){
                activeQuests.remove(q);
                aGA.notifyDataSetChanged();
            }else{
                switch((int)q.id){
                    case 1:
                        findViewById(R.id.quest1).setEnabled(false);
                        break;
                    case 2:
                        findViewById(R.id.quest2).setEnabled(false);
                        break;
                    case 3:
                        findViewById(R.id.quest3).setEnabled(false);
                        break;
                }
            }
        }
    }

    private void initItems(){
        activeQuests = new ArrayList<>();
        succeededQuests = new ArrayList<>();
        setGridView();
        DbQuestAdapter dbqa = new DbQuestAdapter(this);
        Cursor c = dbqa.getActiveQuests();
        //Cursor c = dbqa.getAllQuests();
        if(c.moveToFirst()){
            do{
                activeQuests.add(new Quest(c));
            }while (c.moveToNext());
        }
        c.close();
        setViews();
        Cursor c2 = dbqa.getSucceededQuests();
        if(c2.moveToFirst()){
            do{
                succeededQuests.add(new Quest(c2));
            }while (c2.moveToNext());
        }
        c2.close();
        dbqa.close();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        i.putExtra("hero", h);
        i.putExtra("totalGold", totalGold);
        setResult(RESULT_OK, i);
        finish();
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
            final DbQuestAdapter dQA = new DbQuestAdapter(c);
            dQA.open();
            //final Hero ah = h;
            final View fV = v;
            final Quest q = dQA.getQuest(qNum+1);
            AlertDialog.Builder a = new AlertDialog.Builder(c);
            a.setTitle(ArrayVars.qTitles[qNum])
                    .setMessage(ArrayVars.qMsg[qNum]+"\nDuration: "+q.duration/1000)
                    .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Notifiers.scheduleNotification(c, Notifiers.getNotification(c, "Delay: "+q.duration/1000), (int)q.duration);
                            dQA.startQuest(q.id, SystemClock.elapsedRealtime());
                            findViewById(fV.getId()).setEnabled(false);
                            //if(q.questSucceeded)
                            //    addReward(h, q.reward, q.goldReward);
                            activeQuests.add(q);
                            aGA.notifyDataSetChanged();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            })
            .show();
            dQA.close();
        }
    };

    private boolean testFinished(Quest q){
        DbQuestAdapter dbqa = new DbQuestAdapter(this);
        Boolean ret = dbqa.questSucceeded(q.id, SystemClock.elapsedRealtime());
        dbqa.close();
        return ret;
    }

    private boolean checkReward(Quest q){
        if(q.questActive && q.questSucceeded) {
            addReward(h, q.reward, q.goldReward);
            DbQuestAdapter dbqa = new DbQuestAdapter(this);
            if(dbqa.questCompleted(q.id)){
                dbqa.close();
                return true;
            }
            dbqa.close();
            return false;
        }else
            System.out.println("Quest not ready?");
        return false;
    }

    public void randomQuest(View v){
        final DbQuestAdapter dbqa = new DbQuestAdapter(this);
        //final Quest q = dbqa.newQuest();
        final Quest q = Quest.makeQuest();
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Quest")
                .setMessage(q.description+"\nDuration: "+q.duration/1000)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbqa.newQuest(q);
                        Notifiers.scheduleNotification(c, Notifiers.getNotification(c, "Delay: "+q.duration/1000), (int)q.duration);
                        dbqa.startQuest(q.id, SystemClock.elapsedRealtime());
                        activeQuests.add(q);
                        aGA.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
        dbqa.close();
    }

    public void addReward(Hero h, List<InvItem> reward, int goldReward){
        h.passQuest();
        totalGold += goldReward;
        if(reward != null){
            for(InvItem r:reward){
                h.inventory.add(r, r.getQty());
            }
        }
    }

    public void setNotification(View v){
        Notifiers.scheduleNotification(this, Notifiers.getNotification(this, "30-sec delay"), 30000);
        Toast.makeText(this, "Notification set for 30 second's time", Toast.LENGTH_SHORT).show();
    }
}
