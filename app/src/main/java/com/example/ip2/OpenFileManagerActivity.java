package com.example.ip2;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class OpenFileManagerActivity extends AppCompatActivity {
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";

    public void startFileManager(ActivityResultLauncher<Intent> mStartForResult) {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);//permission request code is just an int
            } else {
                Intent intent = new Intent(getApplicationContext(), FolderActivity.class);
                mStartForResult.launch(intent);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent(getApplicationContext(), FolderActivity.class);
                mStartForResult.launch(intent);
            }
        }
    }
}
