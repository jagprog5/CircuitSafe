package com.johngiorshev.circuitsafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public class BoardDisplay extends AppCompatActivity {

    // e.g. {"currentRating":0.07263097538259303,"otherProperty":58585}
    // Content is only one level deep.
    static String jsonOutput = null;
    static JSONObject jsonInput = null;
    static String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_display);

        final ImageView imageView = (ImageView)findViewById(R.id.imageView);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://35.230.167.95/output.png");
                    final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            imageView.setImageBitmap(bmp);;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        TextView longText = (TextView)findViewById(R.id.longTextDisplay);
        longText.setText("");
        longText.append("Inputs:\n");
        longText.append("File: \"" + fileName + "\"\n");

        try {
            longText.append("Minimum Distance (mm): " + jsonInput.getString("min_dist") + "\n");
            longText.append("Minimum Width (mm): " + jsonInput.getString("min_width") + "\n");
            longText.append("Voltage (Volts): " + jsonInput.getString("voltage") + "\n");
            longText.append("Current (Amps): " + jsonInput.getString("current") + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        longText.append("Outputs:\n");
        String pairs[] = jsonOutput.substring(1, jsonOutput.length()-1).split(",");
        for (String s : pairs) {
            String[] pair = s.split(":");
            String property = pair[0].substring(1, pair[0].length() - 1);
//            for (char c : property.toCharArray()) {
//
//            }
            longText.append(property + ": " + pair[1] + "\n");
        }

        Log.d("TEXT", longText.getText().toString());

    }
}
