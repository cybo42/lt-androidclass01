package com.ltree.expenses;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mjrw on 24/11/15.
 * Helper class to centralize the requests for permissions.
 * If a permission has not been granted, will request it but only if:
 *  it is in the Manifest
 *  It is Dangerous
 *  The user has not told Android to stop requesting the permission
 */
public class PermissionsManager {

    private Activity activity;

    public PermissionsManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * Request permissions from the user.
     * NB: the permissions requested here must match the "dangerous permissions" requested in the Manifest.
     * If permissions are not requested in the manifest then the system will not prompt for them.
     * Run adb shell pm list permissions -d -g to get a list of the permissions considered dangerous
     *
     * Our dangerous permissions are "android.permission.CAMERA" and "android.permission.WRITE_EXTERNAL_STORAGE"
     */
    public void requestPermissionsIfNeeded() {
        ArrayList<String> requiredPermissions = new ArrayList<String>();
        checkIfPermissionGranted(requiredPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkIfPermissionGranted(requiredPermissions, Manifest.permission.CAMERA);
        checkIfPermissionGranted(requiredPermissions, Manifest.permission.ACCESS_COARSE_LOCATION);
        checkIfPermissionGranted(requiredPermissions, Manifest.permission.ACCESS_FINE_LOCATION);

        if(requiredPermissions.size() > 0) {
            // Request Permissions Now
            ActivityCompat.requestPermissions(activity,
                    requiredPermissions.toArray(new String[requiredPermissions.size()]),
                    Constants.REQUEST_PERMISSIONS);
        }
    }

    /**
     * Checks if the requersted permission has been granted. If NOT then the permission string is added
     * to the List of required permissions
     * @param requiredPermissions
     * @param permissionToCheck
     */
    private void checkIfPermissionGranted(List<String> requiredPermissions, String permissionToCheck){
        if (ActivityCompat.checkSelfPermission(activity, permissionToCheck)
                != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(permissionToCheck);
        }
    }
}
