package com.ahmed.usuf.billingdesign;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Strings;

import java.util.List;

/**
 * Created by Ahmed-Mariam on 3/31/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    public static List<ProductDetails> itemList;

    public RecycleViewAdapter() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, priceView, qtyView, totalView;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.productTitle);
            qtyView= (TextView) view.findViewById(R.id.qtyview);
            priceView = (TextView) view.findViewById(R.id.priceview);
            totalView=(TextView) view.findViewById(R.id.totalvalue);
        }
    }

    public RecycleViewAdapter(List<ProductDetails> moviesList) {
        try {
            this.itemList = moviesList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("ahmed","ahmedBindViewHolder");
        if (position==0){
            holder.itemName.setText("Name");
            holder.priceView.setText("Price");
            holder.qtyView.setText("Qty");
            holder.totalView.setText("Total");
        }else {
            ProductDetails details = itemList.get(position - 1);
            holder.itemName.setText(details.getProductName());
            holder.priceView.setText(details.getPrice());
            holder.qtyView.setText(details.getQty());
            holder.totalView.setText(details.getTotal());
        }
    }

    public void swap(List<ProductDetails> list){
            itemList=list;
            notifyDataSetChanged();
    }

    public void notifyData(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
       return itemList==null?0:itemList.size()+1;
}
}
