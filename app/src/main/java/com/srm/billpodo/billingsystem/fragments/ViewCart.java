package com.srm.billpodo.billingsystem.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.activities.HomeScreen;
import com.srm.billpodo.billingsystem.adapters.RecycleViewAdapter;
import com.srm.billpodo.billingsystem.data.LineItem;
import com.srm.billpodo.billingsystem.interfaces.BillCallBack;
import com.srm.billpodo.billingsystem.interfaces.PercentageCallBack;
import com.srm.billpodo.billingsystem.interfaces.fragmentLifeCycle;
import com.srm.billpodo.billingsystem.singleton.AppController;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class ViewCart extends android.support.v4.app.Fragment implements ReceiveListener, fragmentLifeCycle {


    public RecyclerView recyclerView;
    public static RecycleViewAdapter mAdapter;
    public java.util.List<LineItem> pDetails;
    TextView emptycart;
    BillCallBack billCallBack;
    PercentageCallBack percentageCallBack;

    @Override
    public void onAttach(Activity activity) {
        percentageCallBack = (PercentageCallBack) activity;
        billCallBack = (BillCallBack) activity;
        super.onAttach(activity);
    }

    public ViewCart() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.billlist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        emptycart = (TextView) view.findViewById(R.id.empty);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecycleViewAdapter(pDetails);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        final Button bill = (Button) view.findViewById(R.id.bill);
        final Button discount = (Button) view.findViewById(R.id.discount);
        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percentageCallBack.calculatePercentage();
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billCallBack.printBill();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        emptycart = (TextView) getActivity().findViewById(R.id.empty);
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {

    }


    @Override
    public void onPauseFragment() {
        Log.d("ViewCart", "onPau");
    }

    @Override
    public void onResumeFragment() {
        try {
            emptycart = (TextView) getActivity().findViewById(R.id.empty);
            if (AppController.getInstance().getBag().size() > 0) {
                emptycart.setVisibility(View.GONE);
            } else {
                emptycart.setVisibility(View.VISIBLE);
                emptycart.setTextSize(HomeScreen.width / 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
