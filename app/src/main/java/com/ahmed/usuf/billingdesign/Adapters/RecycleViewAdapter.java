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
import com.ahmed.usuf.billingdesign.Volley.AppController;

import java.util.List;

/**
 * Created by Ahmed-Mariam on 3/31/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    public static List<LineItem> itemList;
    public static int subtotal,lessAmount,finalAmount;
    AddItem bill;
    int rowLength;

    public RecycleViewAdapter() {
        bill = new AddItem();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, priceView, qtyView, totalView;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.productTitle);
            qtyView = (TextView) view.findViewById(R.id.qtyview);
            priceView = (TextView) view.findViewById(R.id.priceview);
            totalView = (TextView) view.findViewById(R.id.totalvalue);
            totalView.setWidth(HomeScreen.width);
        }
    }

    public RecycleViewAdapter(List<LineItem> moviesList) {
        try {
            this.itemList = moviesList;
            bill = new AddItem();
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
    int count=0;
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d("ahmed", "Checking Discount Boolean"+count +""+ AppController.getInstance().isDiscountOn());
        count++;
        holder.itemName.setVisibility(View.VISIBLE);
        holder.priceView.setVisibility(View.VISIBLE);
        holder.qtyView.setVisibility(View.VISIBLE);
        holder.itemName.setWidth(HomeScreen.width / 4);
        holder.qtyView.setWidth(HomeScreen.width / 2);
        holder.priceView.setWidth(HomeScreen.width / 2);
        holder.totalView.setWidth(HomeScreen.width / 2);
        holder.totalView.setGravity(Gravity.START);
        holder.itemName.setTextSize(30);
        holder.qtyView.setTextSize(30);
        holder.priceView.setTextSize(30);
        holder.totalView.setTextSize(30);


        if (position == 0) {
            Log.d("ahmed", "if pos:" + position);

            holder.itemName.setTypeface(Typeface.SERIF, Typeface.BOLD);
            holder.priceView.setTypeface(Typeface.SERIF, Typeface.BOLD);
            holder.qtyView.setTypeface(Typeface.SERIF, Typeface.BOLD);
            holder.totalView.setTypeface(Typeface.SERIF, Typeface.BOLD);

            holder.itemName.setText("Item");
            holder.priceView.setText("Price");
            holder.totalView.setText("Total");
            holder.qtyView.setText("Qty");

            holder.priceView.setTextColor(Color.parseColor("#F44336"));
            holder.totalView.setTextColor(Color.parseColor("#F44336"));
            holder.itemName.setTextColor(Color.parseColor("#F44336"));
            holder.qtyView.setTextColor(Color.parseColor("#F44336"));



        }
        if (AppController.getInstance().isDiscountOn()){
            Log.d("ahmed", "Checking Discount Boolean" + AppController.getInstance().isDiscountOn());
            if (position + 1 < rowLength - 3 && position > 0) {
                Log.d("ahmed", "pos" + position);
                LineItem details = itemList.get(position - 1);

                holder.itemName.setText(details.getProductName());
                holder.priceView.setText(details.getPrice());
                holder.qtyView.setText(details.getQty());
                holder.totalView.setText(details.getTotal());

                holder.priceView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                holder.qtyView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                holder.totalView.setTypeface(Typeface.SERIF, Typeface.BOLD);

                holder.priceView.setTextColor(Color.parseColor("#6200EA"));
                holder.totalView.setTextColor(Color.parseColor("#6200EA"));
                holder.itemName.setTextColor(Color.parseColor("#6200EA"));
                holder.qtyView.setTextColor(Color.parseColor("#6200EA"));

            }
            if (position + 1 == rowLength) {
                Log.d("position3", "pos" + position);
                holder.totalView.setTextSize(70);
                holder.itemName.setVisibility(View.GONE);
                holder.priceView.setVisibility(View.GONE);
                holder.qtyView.setVisibility(View.GONE);
                holder.totalView.setWidth(HomeScreen.width);
                holder.totalView.setGravity(Gravity.CENTER_HORIZONTAL);
                holder.totalView.setTextColor(Color.RED);
                holder.totalView.setText("\u20B9 " + HomeScreen.discountTotal);
               // finalAmount=HomeScreen.discountTotal;
            }
        }else{
            if (position + 1 < rowLength && position > 0) {
                Log.d("position2", "pos" + position);
                LineItem details = itemList.get(position - 1);

                holder.priceView.setTextColor(Color.parseColor("#6200EA"));
                holder.totalView.setTextColor(Color.parseColor("#6200EA"));
                holder.itemName.setTextColor(Color.parseColor("#6200EA"));
                holder.qtyView.setTextColor(Color.parseColor("#6200EA"));

                holder.itemName.setText(details.getProductName());
                holder.priceView.setText(details.getPrice());
                holder.qtyView.setText(details.getQty());
                holder.totalView.setText(details.getTotal());

                holder.priceView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                holder.qtyView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                holder.totalView.setTypeface(Typeface.SERIF, Typeface.BOLD);

            }
            if (position + 1 == rowLength) {
                Log.d("position3", "pos" + position);
                holder.totalView.setTextSize(70);
                holder.itemName.setVisibility(View.GONE);
                holder.priceView.setVisibility(View.GONE);
                holder.qtyView.setVisibility(View.GONE);
                holder.totalView.setWidth(HomeScreen.width);
                holder.totalView.setGravity(Gravity.CENTER_HORIZONTAL);
                holder.totalView.setTextColor(Color.parseColor("#FF5722"));
                holder.totalView.setTypeface(Typeface.SANS_SERIF
                );
                holder.totalView.setText("\u20B9 " + AppController.getInstance().getTotal());
               // finalAmount=AddItem.getTotal();
            }

        }

        if (position + 1 == rowLength - 2 && AppController.getInstance().isDiscountOn()) {
            Log.d("position3", "pos" + position);
            holder.itemName.setVisibility(View.GONE);
            holder.priceView.setVisibility(View.GONE);
            holder.qtyView.setVisibility(View.GONE);
            holder.totalView.setWidth(HomeScreen.width);
            holder.totalView.setGravity(Gravity.RIGHT);
            holder.totalView.setTextSize(40);
            holder.totalView.setText("SubTotal: " + AppController.getInstance().getTotal());
        }

        if (position + 1 == rowLength - 1&& AppController.getInstance().isDiscountOn()) {
            Log.d("position3", "pos" + position);
            holder.itemName.setVisibility(View.GONE);
            holder.priceView.setVisibility(View.GONE);
            holder.qtyView.setVisibility(View.GONE);
            holder.totalView.setWidth(HomeScreen.width);
            holder.totalView.setGravity(Gravity.RIGHT);
            holder.totalView.setTextSize(40);
            holder.totalView.setText("Discount: " + HomeScreen.reducedAmount);
        }
    }

    public void swap(List<LineItem> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        try {
            if (itemList.size() == 0) {
                return 0;
            } else if (itemList != null && AppController.getInstance().isDiscountOn()) {
                rowLength = itemList.size() + 4;
                return rowLength;
            } else {
                rowLength = itemList.size() + 2;
                return rowLength;
            }
        }catch (Exception e){return 0;}

    }
}
