package com.ahmed.usuf.billingdesign;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class ProductAdapter extends android.support.v4.app.Fragment implements ReceiveListener{

    public RecyclerView recyclerView;
    public static RecycleViewAdapter mAdapter;
    public java.util.List<ProductDetails> pDetails;

    public ProductAdapter(){

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

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {

    }
}
