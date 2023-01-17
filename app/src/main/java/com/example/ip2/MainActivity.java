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
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements OnFragmentSendDataListener {
    private String selectedItem;
    boolean isGetActivityResult = true;
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    assert intent != null;
                    String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                    switch(accessMessage) {
                        case "DELETE":
                            ListViewFragment fragment = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.fragment_container_view);
                            if (fragment != null) {
                                fragment.deleteElement(selectedItem);
                            }
                            selectedItem = "";
                            break;
                        case "CREATE":
                            ListViewFragment fragment1 = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.fragment_container_view);
                            if (fragment1 != null) {
                                Bundle extras = intent.getExtras();
                                Order p = (Order) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment1.addElement(p);
                            }
                            selectedItem = "";
                            break;
                        case "CHANGE":
                            ListViewFragment fragment2 = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.fragment_container_view);
                            if (fragment2 != null) {
                                Bundle extras = intent.getExtras();
                                String oldName = intent.getStringExtra(ProductEditorActivity.CHANGE_NAME);
                                Order p = (Order) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment2.changeElement(oldName, p);
                            }
                            selectedItem = "";
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
    }

    @Override
    public void onSendData(String s, Order product) {
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
                .findFragmentById(R.id.fragment_container_view);
        assert fragment1 != null;
        fragment1.addElement(new Order(0, name, new GregorianCalendar(), count));
        selectedItem = "";
    }

    public void changeElement(int src, String oldName, String name, Integer count, String url) {
        ListViewFragment fragment2 = (ListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_view);
        assert fragment2 != null;
        fragment2.changeElement(oldName, new Order(0, name, new GregorianCalendar(), count));
        selectedItem = "";
    }

    public void deleteElement() {
        ListViewFragment fragment = (ListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_view);
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
}