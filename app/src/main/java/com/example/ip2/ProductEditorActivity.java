package com.example.ip2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class ProductEditorActivity extends AppCompatActivity {
    static final String ACCESS_MESSAGE="ACCESS_MESSAGE";
    public final static String SELECTED_ITEM = "SELECTED_ITEM";
    public final static String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public final static String CHANGE_NAME = "CHANGE_NAME";
    public final static String CREATE_NAME = "CREATE_NAME";
    Order product;
    String newName;
    public Calendar dateOrder = Calendar.getInstance();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        Intent intent = getIntent();
        String type_fragment = intent.getStringExtra(FRAGMENT_TYPE);
        if (Objects.equals(type_fragment, "CREATE")) {
            Intent intent1 = getIntent();
            newName = intent1.getStringExtra(CREATE_NAME);
            setContentView(R.layout.activity_detail_create);
        } else {
            setContentView(R.layout.activity_detail);
            Bundle extras = getIntent().getExtras();
            product = (Order) extras.getSerializable(SELECTED_ITEM);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductEditorFragment fragment = (ProductEditorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        ProductCreatorFragment fragment1 = (ProductCreatorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailCreateFragment);
        if(fragment1 != null) {
            fragment1.setName(newName);
        }
        if(fragment != null) {
            fragment.setProduct(product);
        }
    }

    public void sendMessage(String message){
        Intent data = new Intent();
        data.putExtra(MainActivity.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();
    }

    public void sendProduct(int src, String name, Integer count, String url) {
        Intent data = new Intent();
        data.putExtra(ACCESS_MESSAGE, "CREATE");
        data.putExtra(SELECTED_ITEM, new Order(0, name, dateOrder, count));
        setResult(RESULT_OK, data);
        finish();
    }

    public void sendProductChange(int src, String oldName, String name, Integer count, String url) {
        Intent data = new Intent();
        data.putExtra(ACCESS_MESSAGE, "CHANGE");
        data.putExtra(CHANGE_NAME, oldName);
        data.putExtra(SELECTED_ITEM, new Order(0, name, dateOrder, count));
        setResult(RESULT_OK, data);
        finish();
    }

    public void setDate(View v) {
        new DatePickerDialog(ProductEditorActivity.this, d,
                dateOrder.get(Calendar.YEAR),
                dateOrder.get(Calendar.MONTH),
                dateOrder.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d= (view, year, monthOfYear, dayOfMonth) -> {
        ProductEditorFragment fragment = (ProductEditorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        ProductCreatorFragment fragment1 = (ProductCreatorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailCreateFragment);

        dateOrder.set(Calendar.YEAR, year);
        dateOrder.set(Calendar.MONTH, monthOfYear);
        dateOrder.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(fragment1 != null) {
            fragment1.changeDate(dateFormat.format(dateOrder.getTime()));
        } else if (fragment != null) {
            fragment.changeDate(dateFormat.format(dateOrder.getTime()));
        }
    };
}