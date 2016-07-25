package com.guipenedo.pokeradar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String countdownFromMillis(long millis) {
        return String.format(Locale.US, "%dmin, %dsec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static String timeFromMillis(long millis) {
        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return formatter.format(date);
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static void writeToFile(Activity context, String fileName, String body) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    112);
        }
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Android/data/pgoradar");
        dir.mkdirs();
        File file = new File(dir, fileName + ".txt");

        try {
            FileOutputStream f = new FileOutputStream(file, true);
            f.write(body.getBytes());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadJSONFromFile(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Bitmap bitmapForPokemon(Context context, int pokemonId){
        int resourceID = context.getResources().getIdentifier("p" + pokemonId, "drawable", context.getPackageName());
        return BitmapFactory.decodeResource(context.getResources(), resourceID);
    }
}
