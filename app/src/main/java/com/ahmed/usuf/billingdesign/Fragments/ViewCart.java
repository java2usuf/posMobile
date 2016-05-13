package com.ahmed.usuf.billingdesign.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmed.usuf.billingdesign.Adapters.LineItem;
import com.ahmed.usuf.billingdesign.Interfaces.fragmentLifeCycle;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.Adapters.RecycleViewAdapter;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class ViewCart extends android.support.v4.app.Fragment implements ReceiveListener,fragmentLifeCycle{


    public RecyclerView recyclerView;
    public static RecycleViewAdapter mAdapter;
    public java.util.List<LineItem> pDetails;

    public ViewCart(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.billlist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        mAdapter=new RecycleViewAdapter(pDetails);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        return view;
    }

    private void customDialog(){
      /*  // Created a new Dialog
        Dialog dialog = new Dialog(MyActivity.this);

// Set the title
        dialog.setTitle("Dialog Title");

// inflate the layout
        dialog.setContentView(R.layout.dialog_view);

// Set the dialog text -- this is better done in the XML
        TextView text = (TextView)dialog.findViewById(R.id.dialog_text_view);
        text.setText("This is the text that does in the dialog box");

// Display the dialog
        dialog.show();*/
    }



    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {

    }


    @Override
    public void onPauseFragment() {
        Log.d("ViewCart", "onPause");
    }

    @Override
    public void onResumeFragment() {
        Log.d("ViewCart","OnResume");
    }
}
