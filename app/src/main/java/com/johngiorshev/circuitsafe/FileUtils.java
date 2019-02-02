package com.johngiorshev.circuitsafe;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUtils {
    private FileUtils() {}

    private static String crlf = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary =  "*****";

    public static void sendFile(File f, String url) throws Exception {
        // THIS METHOD IS BASED OFF:
        // https://stackoverflow.com/a/11826317

        HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(
                httpUrlConnection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"gerberfile\";filename=\"" +
                f.getName() + "\"" + crlf);
        request.writeBytes(crlf);

        request.write(getBytesFromFile(f));

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary +
                twoHyphens + crlf);
        request.flush();
        request.close();
    }

    private static byte[] getBytesFromFile(File f) {
        byte[] bytes = new byte[(int)f.length()];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(f));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
