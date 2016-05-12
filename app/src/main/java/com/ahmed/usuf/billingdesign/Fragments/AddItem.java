package com.ahmed.usuf.billingdesign.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.Toast;


import com.ahmed.usuf.billingdesign.Adapters.LineItem;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.Adapters.RecycleViewAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 4/1/2016.
 */
public class AddItem extends Fragment {
    SharedPreferences sharedpreferences;
    private static int total;
    String[] productNames;
    String[] qt;
    public EditText prc,tot,billno;
    public static int billCount=00001;


    AppCompatSpinner pName,qty;
    ViewCart productAdapter;
    public List<LineItem> list;
    public static List<LineItem> printerList;
    netTotalCallBack callBack;

    String selectedItem,slectedQty;

    public interface netTotalCallBack{
        public void displaySnackView(String total);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack=(netTotalCallBack)activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.bill_enter,container,false);

        sharedpreferences= getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();



        pName=(AppCompatSpinner)v.findViewById(R.id.productname);
        qty=(AppCompatSpinner)v.findViewById(R.id.qtyspinner);
        prc=(EditText)v.findViewById(R.id.pricelabel);
        tot=(EditText)v.findViewById(R.id.totalLabel);
        tot.setEnabled(false);
        billno=(EditText)v.findViewById(R.id.billno);
        billno.setEnabled(false);



        prc.addTextChangedListener(new TextWatcher() {
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
                    int calculatedAmount = Integer.parseInt(buffer) * Integer.parseInt(prc.getText().toString());
                    Log.d("Ahmed", "" + calculatedAmount);
                    tot.setText("" + calculatedAmount);
                } catch (NumberFormatException e) {
                    tot.setText("");
                    e.printStackTrace();
                }
            }

        });

        final RecycleViewAdapter recycleViewAdapter=new RecycleViewAdapter();

        FloatingActionButton fab=(FloatingActionButton)v.findViewById(R.id.fab);
        list=new ArrayList<LineItem>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               View focus = getActivity().getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }

                if (prc.getText().toString().isEmpty()){
                    prc.setError("Please Enter Product price");
                }else{
                    prc.setError(null);
                    Toast.makeText(AddItem.this.getActivity(), "Adding to the Cart1", Toast.LENGTH_SHORT).show();
                    //  Log.d("Ahmed", "ListSize:" + RecycleViewAdapter.itemList.size());
                    list.add(new LineItem(qty.getSelectedItem().toString(), prc.getText().toString(),billno.getText().toString(), tot.getText().toString(), pName.getSelectedItem().toString()));
                    ViewCart.mAdapter.swap(list);
                    qty.setSelection(0);
                    pName.setSelection(0);
                    prc.setText("");
                    tot.setText("");

                    int totalCount =  0;
                    for (LineItem details:list){
                        totalCount+=Integer.parseInt(details.getTotal());
                    }
                    total=totalCount;
                    printerList=list;
                }

            }
        });

        try {
            readItemData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, productNames);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        pName.setAdapter(LTRadapter);

        try {
            readQuantityData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, qt);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        qty.setAdapter(LTRadapter);
        qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View focus = getActivity().getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
                if (prc.getText().toString().isEmpty()) {

                } else {
                    prc.setText("");
                    tot.setText("");
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
                if (prc.getText().toString().isEmpty()) {

                } else {
                    prc.setText("");
                    tot.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static int getTotal(){
        return  total;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void readItemData() throws IOException{
        String str="";
        StringBuffer buf=new StringBuffer();
        InputStream is=this.getResources().openRawResource(R.drawable.itemlname);
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        if (is!=null){
            Log.d("ahmed","InsideIF loop");
            while ((str=reader.readLine())!=null){
                Log.d("ahmed","InsideWHILE loop");
                buf.append(str);
            }
            is.close();
            productNames=buf.toString().split(",");
        }
    }

    public void readQuantityData() throws IOException{
        String str="";
        StringBuffer buf=new StringBuffer();
        InputStream is=this.getResources().openRawResource(R.drawable.qtylists);
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        if (is!=null){
            while ((str=reader.readLine())!=null){
                buf.append(str);
            }
            is.close();
            qt=buf.toString().split(",");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedpreferences.contains("billno")) {
            billno.setText(""+sharedpreferences.getInt("billno", 0));
        }else {
            billno.setText(""+billCount);
        }
    }
}
