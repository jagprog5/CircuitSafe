package com.johngiorshev.circuitsafe;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;

public class BoardDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_display);

        // Gets path of file selected by user
        String filePath = getIntent().getStringExtra("FILEPATH");
        final File f = new File(filePath);
        Log.d("FILECHOSEN", f.getAbsolutePath() + " exists?: " + f.exists());

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try  {
//                    FileUtils.sendFile(f, "https://peterson-qhacks.herokuapp.com/");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();

    }
}
