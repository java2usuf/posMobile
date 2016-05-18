package com.ahmed.usuf.billingdesign.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmed.usuf.billingdesign.DatabaseHandler.DatabaseHandler;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.data.TrasactionDetails;
import com.ahmed.usuf.billingdesign.utili.SystemConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/17/2016.
 */
public class ConfigListAdapter extends RecyclerView.Adapter<ConfigListAdapter.ConfigViewHolder> {

    private List<TrasactionDetails> itemList;
    public boolean makeWhite=false;

    public class ConfigViewHolder extends RecyclerView.ViewHolder {

        public TextView date, subtotal, billedamount, discountedAmount,percentage;
        public LinearLayout linear;


        public ConfigViewHolder(View view) {

            super(view);

            linear=(LinearLayout)view.findViewById(R.id.linear);
            percentage=(TextView) view.findViewById(R.id.percentage);
            date=(TextView)view.findViewById(R.id.datetv);
            subtotal=(TextView)view.findViewById(R.id.subtotal);
            billedamount=(TextView)view.findViewById(R.id.billedamount);
            discountedAmount=(TextView)view.findViewById(R.id.discountedamount);
        }
    }

    public ConfigListAdapter(List<TrasactionDetails> list) {
        try {
            this.itemList = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConfigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.configlist, parent, false);

        return new ConfigViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConfigViewHolder holder, int position) {

        setZigZagColor(holder);
        setHeader(holder, position);
        setItems(holder, position);

    }

    private void setZigZagColor(ConfigViewHolder holder) {
        if (makeWhite){
            holder.linear.setBackgroundColor(Color.WHITE);
        }else {
            holder.linear.setBackgroundColor(Color.GRAY);
        }
        makeWhite=!makeWhite;
    }

    private int getDiscount(int billedAmount,int reducedAmount){

        int discount= 0;
        try {
            discount = (billedAmount+reducedAmount)/reducedAmount;
        } catch (ArithmeticException e) {
            e.printStackTrace();
            discount=0;
        }

        return discount;
    }

    private void setHeader(ConfigViewHolder holder, int position) {
        if (position==0) {
            holder.date.setText("Date");
            holder.discountedAmount.setText("DiscountAmount");
            holder.billedamount.setText("BilledAmount");
            holder.subtotal.setText("SubTotal");
            holder.percentage.setText("percent");
        }
    }

    private void setItems(ConfigViewHolder holder, int position) {
        if (position>0) {
            TrasactionDetails details = itemList.get(position - 1);

            int subtotal=details.getDiscountedTotal() + details.getFinalTotal();
            int billedAmount=details.getFinalTotal();
            int lessAmount=details.getDiscountedTotal();

           holder.date.setText("" + details.getDate());
           holder.discountedAmount.setText("" + lessAmount);
           holder.billedamount.setText("" + billedAmount);
           holder.subtotal.setText("" + subtotal);
           holder.percentage.setText(getDiscount(billedAmount,lessAmount)+" %");
       }
    }

    @Override
    public int getItemCount() {
        return itemList.size()+1;
    }

}