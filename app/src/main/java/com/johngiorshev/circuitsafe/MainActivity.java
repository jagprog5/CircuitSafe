package com.johngiorshev.circuitsafe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Ignore when the file picker UI closes when resetting error message text.
    boolean ignoreNextResume = false;
    Button  pickerButton = null;
    TextView errorView;
    File holderFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickerButton = (Button)findViewById(R.id.filePickerButton);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreNextResume = true;
                openFilePickerUI();
            }
        });

        errorView = (TextView)findViewById(R.id.errorMessage);

        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickerButton.isEnabled()) {
                    errorView.setText("File hasn't been selected!");
                    errorView.setVisibility(View.VISIBLE);
                    return; // file hasn't been selected yet
                }
                Log.d("SUBMIT", holderFile.getAbsolutePath() +
                        " exists? " + holderFile.exists());

                // CHECK IF OTHER VALUES AREN'T BLANK


                // IF EVERYTHING IS GOOD, PUT HTTP request with holderfile and editTextValues

                // Wait until http request is returned

                // Switch to other activity.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ignoreNextResume) {
            ignoreNextResume = false;
            return;
        }
        // Hide error message if there hasn't been an error yet
        TextView errorView = (TextView)findViewById(R.id.errorMessage);
        errorView.setVisibility(View.GONE);
        pickerButton.setEnabled(true);
        pickerButton.setText("CHOOSE A GERBER FILE");
    }

    private static final int REQUESTCODE = 42069;
    private static final String[] VALIDEXTENSIONS = new String[] {"dri","gbl","gbo","gbs","gml",
            "gpi","gtl","gto","gtp","gts","txt"};

    public void openFilePickerUI() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // All files
        intent.setType("*/*");
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK && resultData != null) {
            Intent intent = new Intent(getBaseContext(), BoardDisplay.class);

            // This doesn't work.
            // String path = resultData.getData().getPath();

            String fakePath = resultData.getData().getPath();

            // Check if extension is valid
            // Parsing:
            // Get text after last '.'
            // Bring to lowercase
            String extension = fakePath.substring(fakePath.lastIndexOf(".") + 1)
                    .toLowerCase();
            if (Arrays.asList(VALIDEXTENSIONS).contains(extension)) {
                // Write file to place dir where app has control. Only place app can access file object
                try {
                    holderFile = new File(getFilesDir() + "placeholderfile");
                    InputStream in = getContentResolver().openInputStream(resultData.getData());
                    OutputStream out = new FileOutputStream(holderFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.close();
                    in.close();
                    pickerButton.setText("File Selected: "
                            + fakePath.substring(fakePath.lastIndexOf("/") + 1));
                    errorView.setVisibility(View.GONE);
                    pickerButton.setEnabled(false);
                } catch (Exception e) {
                    // nah
                }

                String realPath = holderFile.getAbsolutePath();
            } else {
                errorView.setText("Invalid File Extension.\n" +
                        "Valid extensions include \"gtl\" or \"gbl\".\nChoose a different file.");
                errorView.setVisibility(View.VISIBLE);
            }
        }
    }
}
