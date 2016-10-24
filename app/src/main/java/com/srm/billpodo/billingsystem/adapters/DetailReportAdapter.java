package com.srm.billpodo.billingsystem.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.data.Trasaction;

import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/17/2016.
 */
public class DetailReportAdapter extends RecyclerView.Adapter<DetailReportAdapter.ConfigViewHolder> {

    private List<Trasaction> itemList;
    public boolean makeWhite = false;

    public class ConfigViewHolder extends RecyclerView.ViewHolder {

        public TextView date, subtotal, billedamount, discountedAmount, percentage;
        public LinearLayout linear;


        public ConfigViewHolder(View view) {

            super(view);

            linear = (LinearLayout) view.findViewById(R.id.linear);
            percentage = (TextView) view.findViewById(R.id.percentage);
            date = (TextView) view.findViewById(R.id.datetv);
            subtotal = (TextView) view.findViewById(R.id.subtotal);
            billedamount = (TextView) view.findViewById(R.id.billedamount);
            discountedAmount = (TextView) view.findViewById(R.id.discountedamount);
        }
    }

    public DetailReportAdapter(List<Trasaction> list) {
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
        if (makeWhite) {
            holder.linear.setBackgroundColor(Color.WHITE);
        } else {
            holder.linear.setBackgroundColor(Color.GRAY);
        }
        makeWhite = !makeWhite;
    }

    private int getDiscount(int finalBilledAmount, int amountGivenAsDiscount) {

        int discount = 0;
        try {
            int netAmount = finalBilledAmount + amountGivenAsDiscount;
            discount =  (int) (((((float)amountGivenAsDiscount/(float)netAmount))) * 100.0);
        } catch (ArithmeticException e) {
            //Log.e(getClass().getName(), "getDiscount: ",e );
            discount = 0;
        }

        return discount;
    }

    private void setHeader(ConfigViewHolder holder, int position) {
        if (position == 0) {
            holder.date.setText("Date");
            holder.discountedAmount.setText("DiscountAmount");
            holder.billedamount.setText("BilledAmount");
            holder.subtotal.setText("SubTotal");
            holder.percentage.setText("percent");
        }
    }

    private void setItems(ConfigViewHolder holder, int position) {
        if (position > 0) {
            Trasaction details = itemList.get(position - 1);

            int subtotal = details.getTotalAfterDiscount() + details.getTotalBeforeDiscount();
            int billedAmount = details.getTotalBeforeDiscount();
            int lessAmount = details.getTotalAfterDiscount();

            holder.date.setText("" + details.getDate());
            holder.discountedAmount.setText("" + lessAmount);
            holder.billedamount.setText("" + billedAmount);
            holder.subtotal.setText("" + subtotal);
            holder.percentage.setText(getDiscount(billedAmount, lessAmount) + " %");
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() + 1;
    }

}