package com.example.ip2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductEditorFragment extends Fragment {
    public ProductEditorFragment(){
        super(R.layout.fragment_product_editor);
    }
    String oldName = "";
    String url = null;
    int src;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProductEditorActivity act = (ProductEditorActivity)getActivity();
        assert act != null;
        Calendar date = act.dateOrder;
        SimpleDateFormat dateFormat = act.dateFormat;

        TextView dateView = view.findViewById(R.id.date);
        dateView.setText(dateFormat.format(date.getTime()));
        dateView.setOnClickListener((View v) -> {
            act.setDate(view);
        });

        Button b = view.findViewById(R.id.backButton);
        Button delete = view.findViewById(R.id.deleteButton);
        Button apply = view.findViewById(R.id.applyButton);
        delete.setOnClickListener((View v) -> {
            ((ProductEditorActivity) requireActivity()).sendMessage("DELETE");
        });
        b.setOnClickListener((View v) -> {
            requireActivity().finish();
        });
        apply.setOnClickListener((View v) -> {
            createElement();
        });
    }

    public void changeDate(String date) {
        TextView dateView = requireView().findViewById(R.id.date);
        dateView.setText(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_editor, container, false);
    }

    private void reset(androidx.fragment.app.FragmentActivity a) {
        ((TextView) a.findViewById(R.id.productNameChange)).setText("");
        ((TextView) a.findViewById(R.id.productCountChange)).setText("");
    }

    public void setProduct(Order p) {
        if(p == null) {
            reset(requireActivity());
            return;
        }
        if(getActivity() != null) {
            SimpleDateFormat df = ProductEditorActivity.dateFormat;
            oldName = p.name;
            ((TextView) getActivity().findViewById(R.id.productNameChange)).setText(p.name);
            ((TextView) getActivity().findViewById(R.id.date)).setText(df.format(p.date.getTime()));
            ((TextView) getActivity().findViewById(R.id.productCountChange)).setText((p.cost.toString()));
        }
    }

    public void createElement() {
        EditText nameEdit = requireActivity().findViewById(R.id.productNameChange);
        EditText countEdit = requireActivity().findViewById(R.id.productCountChange);
        String name = nameEdit.getText().toString().trim();
        String count = countEdit.getText().toString().trim();
        int num;
        if(name.equals("") || count.equals("")) {
            return;
        }
        try {
            num = Integer.parseInt(count);
        }
        catch (Exception e){
            return;
        }
        ProductEditorActivity a = (ProductEditorActivity) getActivity();
        assert a != null;
        a.sendProductChange(src, oldName, name, num, url);
    }
}