package com.example.ip2;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductEditorFragment extends Fragment {
    public ProductEditorFragment(){
        super(R.layout.fragment_product_editor);
    }
    String oldName = "";
    String url = null;
    int src;
    int visibility = View.INVISIBLE;

    public void setBitmap(String s) {
        url = s;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button b = view.findViewById(R.id.backButton);
        Button delete = view.findViewById(R.id.deleteButton);
        Button apply = view.findViewById(R.id.applyButton);
        ImageView i = view.findViewById(R.id.productImage);
        i.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((MainActivity) requireActivity()).startFileManager();
            } else {
                ((ProductEditorActivity) requireActivity()).startFileManager();
            }
        });
        delete.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                MainActivity a = (MainActivity) requireActivity();
                reset(a);
                a.deleteElement();
            } else {
                ((ProductEditorActivity) requireActivity()).sendMessage("DELETE");
            }
        });
        b.setOnClickListener((View v) -> {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                reset(requireActivity());
            } else {
                requireActivity().finish();
            }
        });
        apply.setOnClickListener((View v) -> {
            createElement();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_editor, container, false);
    }

    public void changeVisibility(int change) {
        if(change != -1) {
            visibility = change;
        } else {
            visibility = (visibility == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
        }
        requireView().setVisibility(visibility);
    }

    private void reset(androidx.fragment.app.FragmentActivity a) {
        ((ImageView) a.findViewById(R.id.productImage)).setImageResource(R.drawable.none);
        ((TextView) a.findViewById(R.id.productNameChange)).setText("");
        ((TextView) a.findViewById(R.id.productCountChange)).setText("");
    }

    public void setProduct(Product p) {
        if(p == null) {
            reset(requireActivity());
            return;
        }
        url = p.bitmapUrl;
        src = p.imageSrc;
        if(getActivity() != null) {
            if(p.bitmapUrl == null || p.bitmapUrl.equals("")) {
                ((ImageView) getActivity().findViewById(R.id.productImage)).setImageResource(p.imageSrc);
            } else {
                Bitmap bit = BitmapFactory.decodeFile(p.bitmapUrl);
                ((ImageView) getActivity().findViewById(R.id.productImage)).setImageBitmap(bit);
            }
            oldName = p.name;
            ((TextView) getActivity().findViewById(R.id.productNameChange)).setText(p.name);
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MainActivity a = (MainActivity) getActivity();
            assert a != null;
            a.changeElement(src, oldName, name, num, url);
            reset(getActivity());
        } else {
            ProductEditorActivity a = (ProductEditorActivity) getActivity();
            assert a != null;
            a.sendProductChange(src, oldName, name, num, url);
        }
    }
}