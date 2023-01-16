package com.example.ip2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;

public class FolderActivity extends Activity {
    FolderLayout localFolders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders);

        localFolders = findViewById(R.id.localfolders);
        localFolders.setIFolderItemListener(this);
    }

    public void OnCannotFileRead(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon)
                .setTitle(
                        "[" + file.getName()
                                + "] folder can't be read!")
                .setPositiveButton("OK",
                        (dialog, which) -> finish()).show();
    }

    public void OnFileClicked(File file) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon)
                .setTitle("[" + file.getName() + "]")
                .setPositiveButton("OK",
                        (dialog, which) -> {
                            Intent data = new Intent();
                            data.putExtra(OpenFileManagerActivity.ACCESS_MESSAGE, file.getAbsolutePath());
                            setResult(RESULT_OK, data);
                            finish();
                        }).show();
    }
}