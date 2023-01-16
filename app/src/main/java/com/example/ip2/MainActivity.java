package com.example.ip2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;

public class MainActivity extends OpenFileManagerActivity implements OnFragmentSendDataListener {
    private String selectedItem;
    boolean isGetActivityResult = true;
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    assert intent != null;
                    String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                    switch(accessMessage) {
                        case "DELETE":
                            ListViewFragment fragment = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.list_view_fragment);
                            if (fragment != null) {
                                fragment.deleteElement(selectedItem);
                            }
                            selectedItem = "";
                            break;
                        case "CREATE":
                            ListViewFragment fragment1 = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.list_view_fragment);
                            if (fragment1 != null) {
                                Bundle extras = intent.getExtras();
                                Product p = (Product) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment1.addElement(p);
                            }
                            selectedItem = "";
                            break;
                        case "CHANGE":
                            ListViewFragment fragment2 = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.list_view_fragment);
                            if (fragment2 != null) {
                                Bundle extras = intent.getExtras();
                                String oldName = intent.getStringExtra(ProductEditorActivity.CHANGE_NAME);
                                Product p = (Product) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment2.changeElement(oldName, p);
                            }
                            selectedItem = "";
                            break;
                        default:
                            File file = new File(accessMessage);
                            if(file.exists()) {
                                ProductCreatorFragment fragment11 = (ProductCreatorFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.detailCreateFragment);
                                ProductEditorFragment fragment10 = (ProductEditorFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.detailFragment);
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                if (fragment11 != null && fragment11.visibility == View.VISIBLE) {
                                    ImageView i2 = findViewById(R.id.c_productImage);
                                    fragment11.setBitmap(file.getAbsolutePath());
                                    i2.setImageBitmap(bitmap);
                                } else if (fragment10 != null && fragment10.visibility == View.VISIBLE) {
                                    ImageView i = findViewById(R.id.productImage);
                                    fragment10.setBitmap(file.getAbsolutePath());
                                    i.setImageBitmap(bitmap);
                                }
                            }
                            isGetActivityResult = false;
                            break;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(isGetActivityResult) {
                changeVisibility(false);
            }
            isGetActivityResult = true;
        }
    }

    @Override
    public void onSendData(String s, Product product) {
        selectedItem = s;
        ProductEditorFragment fragment = (ProductEditorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isVisible()) {
            fragment.setProduct(product);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    ProductEditorActivity.class);
            intent.putExtra(ProductEditorActivity.SELECTED_ITEM, product);
            mStartForResult.launch(intent);
        }
    }

    public void createElement(int src, String name, Integer count, String url) {
        ListViewFragment fragment1 = (ListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.list_view_fragment);
        assert fragment1 != null;
        fragment1.addElement(new Product(src, url, name, count));
        selectedItem = "";
    }

    public void changeElement(int src, String oldName, String name, Integer count, String url) {
        ListViewFragment fragment2 = (ListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.list_view_fragment);
        assert fragment2 != null;
        fragment2.changeElement(oldName, new Product(src, url, name, count));
        selectedItem = "";
    }

    public void deleteElement() {
        ListViewFragment fragment = (ListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.list_view_fragment);
        assert fragment != null;
        fragment.deleteElement(selectedItem);
        selectedItem = "";
    }

    public void createElementActivity(String name) {
        Intent intent = new Intent(getApplicationContext(),
                ProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.FRAGMENT_TYPE, "CREATE");
        intent.putExtra(ProductEditorActivity.CREATE_NAME, name);
        mStartForResult.launch(intent);
    }

    public void changeVisibility(boolean bool) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ProductEditorFragment fragment = (ProductEditorFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailFragment);
            ProductCreatorFragment fragment2 = (ProductCreatorFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailCreateFragment);
            assert fragment != null;
            assert fragment2 != null;
            if (bool) {
                fragment.changeVisibility(View.INVISIBLE);
                fragment2.changeVisibility(View.VISIBLE);
            } else {
                fragment.changeVisibility(View.VISIBLE);
                fragment2.changeVisibility(View.INVISIBLE);
            }
        }
    }
    public void startFileManager() {
        super.startFileManager(mStartForResult);
    }
}