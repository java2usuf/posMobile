package com.ahmed.usuf.billingdesign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 4/1/2016.
 */
public class BillEnterFragment extends Fragment {

    String[] productNames={"CHUDI","CHUDI MATERIAL","BRA","BABYS","PANT&SSHIRTS","KIDS","FORG","UNDERWEAR","PANTYS","VEST","TOPS","LEGINS"};
    String[] qt={"1","2","3","4","5","6","7","8","9","10"};
    EditText prc,tot;


    AppCompatSpinner pName,qty;
    ProductAdapter productAdapter;
    public List<ProductDetails> list;
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

        pName=(AppCompatSpinner)v.findViewById(R.id.productname);
        qty=(AppCompatSpinner)v.findViewById(R.id.qtyspinner);
        prc=(EditText)v.findViewById(R.id.pricelabel);
        tot=(EditText)v.findViewById(R.id.totalLabel);

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
        list=new ArrayList<ProductDetails>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillEnterFragment.this.getActivity(), "Adding to the Cart1", Toast.LENGTH_SHORT).show();
                //  Log.d("Ahmed", "ListSize:" + RecycleViewAdapter.itemList.size());
                list.add(new ProductDetails(qty.getSelectedItem().toString(), prc.getText().toString(), pName.getSelectedItem().toString(), tot.getText().toString()));
                ProductAdapter.mAdapter.swap(list);
                qty.setSelection(0);
                pName.setSelection(0);
                prc.setText("");
                tot.setText("");
            }
        });

        FloatingActionButton fab2=(FloatingActionButton)v.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalCount =  0;
                for (ProductDetails details:list){
                    totalCount+=Integer.parseInt(details.getTotal());
                }
                callBack.displaySnackView(""+totalCount);
            }
        });

        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, productNames);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        pName.setAdapter(LTRadapter);

        LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, qt);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        qty.setAdapter(LTRadapter);
        qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    @Override
    public void onPause() {
        super.onPause();

    }

}
