package com.srm.billpodo.billingsystem.fragments;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.base.Strings;
import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.data.LineItem;
import com.srm.billpodo.billingsystem.interfaces.fragmentLifeCycle;
import com.srm.billpodo.billingsystem.singleton.AppController;
import com.srm.billpodo.billingsystem.transformation.ShowNumbersTransformationMethod;
import com.srm.billpodo.billingsystem.util.Utility;

import java.io.IOException;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Ahmed-Mariam on 4/1/2016.
 */
public class AddItem extends Fragment implements fragmentLifeCycle {
    private String[] productNames;
    private String[] quantity;
    private EditText pricePerUnit, totalAmount;
    private  AppCompatSpinner productDropDownList, quantityDropDownList;
    private View view;
    private RadioGroup radioGroup;
    private RadioButton currentRadioTxnTypeSelected;

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
        this.view = v;

        AdView mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("DC70A7A1E0C8F50A9261CC411D7820AD")
                        .addTestDevice("2A708BF5A842DDA8A7A823FE1B648766").build();

        mAdView.loadAd(adRequest);


        productDropDownList = (AppCompatSpinner) v.findViewById(R.id.productname);
        quantityDropDownList = (AppCompatSpinner) v.findViewById(R.id.qtyspinner);
        pricePerUnit = (EditText) v.findViewById(R.id.pricelabel);
        totalAmount = (EditText) v.findViewById(R.id.totalLabel);
        totalAmount.setEnabled(false);
        final FancyButton add = (FancyButton) v.findViewById(R.id.angry_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(v);
            }
        });

        pricePerUnit.setTransformationMethod(new ShowNumbersTransformationMethod());

        radioGroup = (RadioGroup) v.findViewById(R.id.radioTxnType);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                currentRadioTxnTypeSelected=(RadioButton) radioGroup.findViewById(checkedId);

                if(currentRadioTxnTypeSelected != null && currentRadioTxnTypeSelected.getText().equals("RETURN")){
                    totalAmount.setBackgroundColor(Color.RED);
                    totalAmount.setTextColor(Color.YELLOW);
                }else{
                    totalAmount.setBackgroundColor(Color.YELLOW);
                    totalAmount.setTextColor(Color.RED);
                }

            }
        });

        pricePerUnit.addTextChangedListener(new TextWatcher() {
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
                    buffer = quantityDropDownList.getSelectedItem().toString();
                    String priceValue = pricePerUnit.getText().toString();
                    if (!Strings.isNullOrEmpty(priceValue)) {
                        int calculatedAmount = Integer.parseInt(buffer) * Integer.parseInt(priceValue);
                        totalAmount.setText("" + calculatedAmount);
                    }
                } catch (NumberFormatException e) {
                    totalAmount.setText("");
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
        productDropDownList.setAdapter(LTRadapter);

        try {
            readQuantityData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LTRadapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_style, quantity);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        quantityDropDownList.setAdapter(LTRadapter);
        quantityDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View focus = getActivity().getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
                if (pricePerUnit.getText().toString().isEmpty()) {

                } else {
                    int price = Integer.parseInt(pricePerUnit.getText().toString());
                    price *= Integer.parseInt(quantityDropDownList.getSelectedItem().toString());
                    totalAmount.setText("" + price);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        productDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void onTxnTypeBuy(View view){
        System.out.println("00000");
    }

    public void onTxnTypeReturn(View view){
        System.out.println("00000222");
    }


    private void addToCart(View v) {
        View focus = getActivity().getCurrentFocus();
        if (focus != null) {
            hiddenKeyboard(focus);
        }

        if (pricePerUnit.getText().toString().isEmpty()) {
            pricePerUnit.setError("Please Enter Product price");
        } else {
            pricePerUnit.setError(null);
            Toast.makeText(AddItem.this.getActivity(), "Adding to the Cart", Toast.LENGTH_LONG).show();


            boolean returnProduct = false;
            if(currentRadioTxnTypeSelected != null && currentRadioTxnTypeSelected.getText().equals("RETURN")){
                returnProduct = true;
            }

            LineItem lineItem = new LineItem(
                    quantityDropDownList.getSelectedItem().toString(),
                    pricePerUnit.getText().toString(),
                    totalAmount.getText().toString(),
                    productDropDownList.getSelectedItem().toString(),returnProduct);


            AppController.getInstance().getBag().add(lineItem);
            ViewCart.mAdapter.swap(AppController.getInstance().getBag());
            quantityDropDownList.setSelection(0);
            productDropDownList.setSelection(0);
            pricePerUnit.setText("");
            totalAmount.setText("");
            radioGroup.clearCheck();
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
        productNames = Utility.getBrandProperty(R.string.product_list).split(",");
    }

    public void readQuantityData() throws IOException {
        String str = "";
        quantity = Utility.getBrandProperty(R.string.qty_id).split(",");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppController.getInstance().getSharedpreferences().contains("billno")) {
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
                AppController.getInstance().getTxnDetails().setBillNumber(AppController.getInstance().getSharedpreferences().getInt("billno", 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
