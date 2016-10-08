package com.mayeosurge.questmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends FragmentActivity {

    static List<Hero> HeroList;

    MyAdapter myAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        HeroList = new ArrayList<>();
        // "Blank" hero for backstory
        HeroList.add(new Hero(-1));
        HeroList.add(new Hero(0));
        HeroList.add(new Hero(1));
        HeroList.add(new Hero(2));

        myAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(myAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                findViewById(R.id.btnContinue).setVisibility(position != 0 ? View.VISIBLE:View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetupActivity.this, GamePage.class);
                i.putExtra("hero", mPager.getCurrentItem()-1);
                i.putExtra("legion", "Mah Leejun");
                startActivity(i);
            }
        });
    }

    public class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return SetupActivity.HeroList.size();
        }

        @Override
        public Fragment getItem(int position){
            return HeroTabFragment.newInstance(position);
        }
    }

    public static class HeroTabFragment extends Fragment{

        int mNum;

        static HeroTabFragment newInstance(int num){
            HeroTabFragment f = new HeroTabFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            Hero h = SetupActivity.HeroList.get(mNum);
            View v;
            if(h.type == -1){
                v = inflater.inflate(R.layout.pager_backstory, container, false);
            }else{
                v = inflater.inflate(R.layout.pager_hero_select, container, false);
                ((TextView)v.findViewById(R.id.tvHName)).setText(h.name);
                ((ImageView)v.findViewById(R.id.ivHeroPager)).setImageResource(ArrayVars.hColours[h.type]);
                String s = String.format(getString(R.string.stats), h.maxHealth, h.stealth, h.strength, h.knowledge, h.magic);
                ((TextView)v.findViewById(R.id.tvStats)).setText(s);
            }
            return v;
        }
    }
}
