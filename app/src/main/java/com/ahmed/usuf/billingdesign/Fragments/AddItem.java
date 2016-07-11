package com.ahmed.usuf.billingdesign.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ahmed.usuf.billingdesign.Transformation.ShowNumbersTransformationMethod;
import com.ahmed.usuf.billingdesign.data.LineItem;
import com.ahmed.usuf.billingdesign.Interfaces.fragmentLifeCycle;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.singleton.AppController;
import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 4/1/2016.
 */
public class AddItem extends Fragment implements fragmentLifeCycle {
    String[] productNames;
    String[] qt;
    public EditText priceEidtText, tot, billno;
    AppCompatSpinner pName, qty;

    public static List<LineItem> printerList;
    int billNumber;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bill_enter, container, false);

        pName = (AppCompatSpinner) v.findViewById(R.id.productname);
        qty = (AppCompatSpinner) v.findViewById(R.id.qtyspinner);
        priceEidtText = (EditText) v.findViewById(R.id.pricelabel);
        tot = (EditText) v.findViewById(R.id.totalLabel);
        tot.setEnabled(false);
        billno = (EditText) v.findViewById(R.id.billno);
        billno.setEnabled(false);
        final Button add=(Button)v.findViewById(R.id.angry_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        priceEidtText.setTransformationMethod(new ShowNumbersTransformationMethod());

        priceEidtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String buffer;
                    buffer = qty.getSelectedItem().toString();
                    String priceValue = priceEidtText.getText().toString();
                    if (!Strings.isNullOrEmpty(priceValue)) {
                        int calculatedAmount = Integer.parseInt(buffer) * Integer.parseInt(priceValue);
                        tot.setText("" + calculatedAmount);
                    }
                } catch (NumberFormatException e) {
                    tot.setText("");
                    e.printStackTrace();
                }
            }

        });

        try {
            readItemData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_style, productNames);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        pName.setAdapter(LTRadapter);

        try {
            readQuantityData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LTRadapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_style, qt);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        qty.setAdapter(LTRadapter);
        qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View focus = getActivity().getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
                if (priceEidtText.getText().toString().isEmpty()) {

                } else {
                    int price = Integer.parseInt(priceEidtText.getText().toString());
                    price *= Integer.parseInt(qty.getSelectedItem().toString());
                    tot.setText("" + price);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View focus = getActivity().getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    private void addToCart() {
        View focus = getActivity().getCurrentFocus();
        if (focus != null) {
            hiddenKeyboard(focus);
        }

        if (priceEidtText.getText().toString().isEmpty()) {
            priceEidtText.setError("Please Enter Product price");
        } else {
            priceEidtText.setError(null);
            Toast.makeText(AddItem.this.getActivity(), "Adding to the Cart", Toast.LENGTH_LONG).show();
            AppController.getInstance().getBag().add(new LineItem(qty.getSelectedItem().toString(), priceEidtText.getText().toString(), billno.getText().toString(), tot.getText().toString(), pName.getSelectedItem().toString()));
            ViewCart.mAdapter.swap(AppController.getInstance().getBag());
            qty.setSelection(0);
            pName.setSelection(0);
            priceEidtText.setText("");
            tot.setText("");

        }
    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void readItemData() throws IOException {
        String str = "";
        StringBuffer buf = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.drawable.itemlname);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is != null) {
            while ((str = reader.readLine()) != null) {
                buf.append(str);
            }
            is.close();
            productNames = buf.toString().split(",");
        }
    }

    public void readQuantityData() throws IOException {
        String str = "";
        StringBuffer buf = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.drawable.qtylists);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is != null) {
            while ((str = reader.readLine()) != null) {
                buf.append(str);
            }
            is.close();
            qt = buf.toString().split(",");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppController.getInstance().getSharedpreferences().contains("billno")) {
            billno.setText(Strings.padStart("" + AppController.getInstance().getSharedpreferences().getInt("billno", 0), 5, '0'));
            AppController.getInstance().getTxnDetails().setBillNumber(AppController.getInstance().getSharedpreferences().getInt("billno", 0));
        }
    }


    @Override
    public void onPauseFragment() {
        Log.d("AddItem", "onPause");

    }

    @Override
    public void onResumeFragment() {
        Log.d("AddItem", "OnResume");

        AppController.getInstance().setIsDiscountOn(false);

        if (AppController.getInstance().getSharedpreferences().contains("billno")) {
            try {
                AppController.getInstance().getSharedpreferences();
                billno.setText(Strings.padStart("" + AppController.getInstance().getSharedpreferences().getInt("billno", 0), 5, '0'));
                AppController.getInstance().getTxnDetails().setBillNumber(AppController.getInstance().getSharedpreferences().getInt("billno", 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
