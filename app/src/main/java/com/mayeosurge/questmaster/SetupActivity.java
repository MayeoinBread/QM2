package com.mayeosurge.questmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetupActivity extends Activity {

    int selectedHero = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        findViewById(R.id.heroRed).setOnClickListener(ocl);
        findViewById(R.id.heroGreen).setOnClickListener(ocl);
        findViewById(R.id.heroBlue).setOnClickListener(ocl);
    }

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.heroRed:
                    selectedHero = 1;
                    break;
                case R.id.heroGreen:
                    selectedHero = 2;
                    break;
                case R.id.heroBlue:
                    selectedHero = 3;
                    break;
            }
        }
    };

    public void continueClick(View v){
        EditText et = (EditText) findViewById(R.id.etLegion);
        Intent i = new Intent(SetupActivity.this, GamePage.class);
        i.putExtra("hero", selectedHero);
        i.putExtra("name", et.getText().toString());
        startActivity(i);
    }
}
