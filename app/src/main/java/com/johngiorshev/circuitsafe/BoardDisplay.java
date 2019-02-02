package com.johngiorshev.circuitsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class BoardDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_display);

        // Gets path of file selected by user
        String filePath = getIntent().getStringExtra("FILEPATH");
        File f = new File(filePath);
    }
}
