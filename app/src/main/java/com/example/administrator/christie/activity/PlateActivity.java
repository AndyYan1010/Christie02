package com.example.administrator.christie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;
import com.example.administrator.christie.R;
import com.example.administrator.christie.fragment.CardLayoutFragment;

public class PlateActivity extends BaseActivity implements CardLayoutFragment.FragmentInteraction{
    private String count;
    private FloatingActionButton fab,fab_add,fab_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);
        fab_back = (FloatingActionButton)findViewById(R.id.fab_back);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab_add = (FloatingActionButton)findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count.equals("5")){
                    Toast.makeText(PlateActivity.this,"最多添加5个车牌",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(PlateActivity.this, AddPlatenoActivity.class));
                    finish();
                }
            }
        });
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new CardLayoutFragment()).commit();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getFragmentManager().beginTransaction().add(R.id.container, new CardLayoutFragment()).commit();
            }
        });
    }

    @Override
    public void process(String str) {
        count = str;
    }
}
