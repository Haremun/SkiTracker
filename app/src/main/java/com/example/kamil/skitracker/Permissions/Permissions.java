package com.example.kamil.skitracker.Permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Kamil on 21.03.2018.
 */

public class Permissions {

    public static void askForPermissions(Activity activity){

        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        /*ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);*/
    }

}
