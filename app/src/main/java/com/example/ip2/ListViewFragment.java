package com.example.ip2;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class ListViewFragment extends Fragment {
    private static boolean viewInit = false;
    private static final Database database = new Database();
    private static final ArrayList<String> productsName = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private OnFragmentSendDataListener fragmentSendDataListener;

    public ListViewFragment() {
        super(R.layout.fragment_list_view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @MainThread
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] str = getResources().getStringArray(R.array.product_list);
        if(!viewInit) {
            database.initialize(str);
            productsName.addAll(Arrays.asList(str));
            viewInit = true;
        }
        ListView listView = view.findViewById(R.id.listView);
        EditText editText = view.findViewById(R.id.editText);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, productsName);
        listView.setAdapter(adapter);
        editText.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN)
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    String name = editText.getText().toString();
                    editText.setText("");
                    InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            editText.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    MainActivity m = (MainActivity) getActivity();
                    assert m != null;
                    m.createElementActivity(name);
                    return true;
                }
            return false;
        });
        listView.setOnItemClickListener((parent, item, pos, id) -> {
            MainActivity main = (MainActivity) getActivity();
            assert main != null;
            fragmentSendDataListener.onSendData(productsName.get(pos), database.get(productsName.get(pos)));
        });
    }

    public void addElement(Product p) {
        if(productsName.contains(p.name)) {
            return;
        }
        productsName.add(p.name);
        database.put(p);
        adapter.notifyDataSetChanged();
    }

    public void changeElement(String name, Product p) {
        if(!productsName.contains(name)) {
            return;
        }
        if(!Objects.equals(name, p.name) && productsName.contains(p.name)) {
            return;
        }
        productsName.remove(name);
        productsName.add(p.name);
        database.change(name, p);
        adapter.notifyDataSetChanged();
    }

    public void deleteElement(String str) {
        if(productsName.contains(str)) {
            productsName.remove(str);
            database.remove(str);
            adapter.notifyDataSetChanged();
        }
    }
}
