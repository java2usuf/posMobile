package com.ahmed.usuf.billingdesign;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 3/30/2016.
 */
public class ProductAdapter extends android.support.v4.app.Fragment{

   public static List itemList=new ArrayList<ProductDetails>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.billlist, container, false);
        setupRecyclerView(rv);


        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        List list=new ArrayList<ProductDetails>();
        for(int i=0;i<=5;i++){
            list.add(new ProductDetails("1","120","chudi","120"));
        }
        recyclerView.addItemDecoration(new ItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));


        recyclerView.setAdapter(new RecycleViewAdapter(list));
    }


}
