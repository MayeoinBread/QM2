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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuests extends Activity {

    Hero h;
    int totalGold;
    Context c;
    List<Quest> activeQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        h = (Hero) getIntent().getSerializableExtra("hero");
        totalGold = getIntent().getIntExtra("totalGold", 0);

        c = this;

        initItems();
        setViews();

        findViewById(R.id.quest1).setOnClickListener(questCL);
        findViewById(R.id.quest2).setOnClickListener(questCL);
        findViewById(R.id.quest3).setOnClickListener(questCL);
    }

    private void setViews(){
        StringBuilder sb = new StringBuilder();
        for(Quest q:activeQuests){
            if(testFinished(q))
                activeQuests.remove(q);
            else{
                sb.append("ID: ").append(q.id).append("  Title: ").append(q.title).append("\n");
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
        ((TextView)findViewById(R.id.tvActiveQuests)).setText(sb.toString());
    }

    private void initItems(){
        activeQuests = new ArrayList<>();
        DbQuestAdapter dbqa = new DbQuestAdapter(this);
        Cursor c = dbqa.getActiveQuests();
        //Cursor c = dbqa.getAllQuests();
        if(c.moveToFirst()){
            do{
                activeQuests.add(new Quest(c));
            }while (c.moveToNext());
        }
        c.close();
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
            //TODO scrap method, redo with DB stuff properly
            //TODO create Quest in QuestActivity through DataBase
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
        Boolean ret;
        if(dbqa.finishQuest(q.id, SystemClock.elapsedRealtime())) {
            ret = true;
            addReward(h, q.reward, q.goldReward);
        }else
            ret = false;
        dbqa.close();
        return ret;
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
