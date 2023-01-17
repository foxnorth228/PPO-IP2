package com.example.ip2;

import android.content.Context;
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

public class ListViewFragment extends Fragment {
    private static boolean viewInit = false;
    private static final Database database = new Database();
    private static final ArrayList<String> orderName = new ArrayList<>();
    private static ArrayList<Integer> orderId;
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
            orderId = database.initialize(str);
            orderName.addAll(Arrays.asList(str));
            viewInit = true;
        }
        ListView listView = view.findViewById(R.id.listView);
        EditText editText = view.findViewById(R.id.editText);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, orderName);
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
            fragmentSendDataListener.onSendData(orderName.get(pos), database.get(orderId.get(pos)));
        });
    }

    public void addElement(Order p) {
        orderName.add(p.name);
        orderId.add(database.put(p));
        adapter.notifyDataSetChanged();
    }

    public void changeElement(Integer id, Order p) {
        String name = orderName.get(orderId.indexOf(id));
        orderName.remove(name);
        orderName.add(p.name);
        database.change(id, p);
        orderId.remove(id);
        orderId.add(id);
        adapter.notifyDataSetChanged();
    }

    public void deleteElement(Integer id) {
        String name = orderName.get(orderId.indexOf(id));
        orderName.remove(name);
        orderId.remove(id);
        database.remove(id);
        adapter.notifyDataSetChanged();
    }
}
