package com.ahmed.usuf.billingdesign.Adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.usuf.billingdesign.Activities.HomeScreen;
import com.ahmed.usuf.billingdesign.Fragments.AddItem;
import com.ahmed.usuf.billingdesign.R;

import java.util.List;

/**
 * Created by Ahmed-Mariam on 3/31/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    public static List<LineItem> itemList;
    AddItem bill;
    public RecycleViewAdapter() {
    bill=new AddItem();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, priceView, qtyView, totalView;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.productTitle);
            qtyView= (TextView) view.findViewById(R.id.qtyview);
            priceView = (TextView) view.findViewById(R.id.priceview);
            totalView=(TextView) view.findViewById(R.id.totalvalue);
            totalView.setWidth(HomeScreen.width);
        }
    }

    public RecycleViewAdapter(List<LineItem> moviesList) {
        try {
            this.itemList = moviesList;
            bill=new AddItem();
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
        Log.d("ahmed", "ahmedBindViewHolder");
        Log.d("pos", "pos" + position);

        holder.itemName.setVisibility(View.VISIBLE);
        holder.priceView.setVisibility(View.VISIBLE);
        holder.qtyView.setVisibility(View.VISIBLE);
        holder.itemName.setWidth(HomeScreen.width / 4);
        holder.qtyView.setWidth(HomeScreen.width/2);
        holder.priceView.setWidth(HomeScreen.width / 2);
        holder.totalView.setWidth(HomeScreen.width / 2);
        holder.totalView.setTextColor(Color.BLACK);
        holder.totalView.setGravity(Gravity.START);
        holder.itemName.setTextSize(16);
        holder.qtyView.setTextSize(16);
        holder.priceView.setTextSize(16);
        holder.totalView.setTextSize(16);


        if (position==0){
            Log.d("position1", "pos" + position);

            holder.itemName.setText("Product Name");
            holder.itemName.setTypeface(null, Typeface.BOLD);
            holder.priceView.setTypeface(null, Typeface.BOLD);
            holder.qtyView.setTypeface(null, Typeface.BOLD);
            holder.totalView.setTypeface(null, Typeface.BOLD);
            holder.priceView.setText("Price");
            holder.qtyView.setText("Qty");
            holder.totalView.setText("Total");
        }
        if(position+1<itemList.size()+2&&position>0) {
            Log.d("position2", "pos" + position);
            LineItem details = itemList.get(position - 1);
            holder.itemName.setText(details.getProductName());
            holder.priceView.setText(details.getPrice());
            holder.qtyView.setText(details.getQty());
            holder.totalView.setText(details.getTotal());

        }
        if(position+1==itemList.size()+2){
            Log.d("position3", "pos" + position);
            holder.totalView.setTextSize(70);
            holder.itemName.setVisibility(View.GONE);
            holder.priceView.setVisibility(View.GONE);
            holder.qtyView.setVisibility(View.GONE);
            holder.totalView.setWidth(HomeScreen.width);
            holder.totalView.setGravity(Gravity.CENTER_HORIZONTAL);
            holder.totalView.setTextColor(Color.RED);
            holder.totalView.setText("$ "+ AddItem.getTotal());
        }
    }

    public void swap(List<LineItem> list){
            itemList=list;
        bill=new AddItem();
            notifyDataSetChanged();
    }

    public void notifyData(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
       return itemList==null?0:itemList.size()+2;
}
}
