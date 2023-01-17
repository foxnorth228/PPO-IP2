package com.example.ip2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnFragmentSendDataListener {
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
                                Bundle extras = intent.getExtras();
                                Order p = (Order) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment.deleteElement(p.id);
                            }
                            break;
                        case "CREATE":
                            ListViewFragment fragment1 = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.fragment_container_view);
                            if (fragment1 != null) {
                                Bundle extras = intent.getExtras();
                                Order p = (Order) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment1.addElement(p);
                            }
                            break;
                        case "CHANGE":
                            ListViewFragment fragment2 = (ListViewFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.fragment_container_view);
                            if (fragment2 != null) {
                                Bundle extras = intent.getExtras();
                                Order p = (Order) extras.getSerializable(ProductEditorActivity.SELECTED_ITEM);
                                fragment2.changeElement(p.id, p);
                            }
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

    public void createElementActivity(String name) {
        Intent intent = new Intent(getApplicationContext(),
                ProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.FRAGMENT_TYPE, "CREATE");
        intent.putExtra(ProductEditorActivity.CREATE_NAME, name);
        mStartForResult.launch(intent);
    }
}