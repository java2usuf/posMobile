package com.srm.billpodo.billingsystem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.data.Trasaction;
import com.srm.billpodo.billingsystem.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed-Mariam on 5/18/2016.
 */
public class DateHistoryAdapter extends RecyclerView.Adapter<DateHistoryAdapter.DateHistoryHolder> {

    ArrayList<String> set = new ArrayList<>();
    List<Trasaction> txndetails = new ArrayList<>();
    private boolean makeWhite = false;
    DatabaseHandler db;

    public DateHistoryAdapter() {

    }

    public DateHistoryAdapter(ArrayList<String> set, Context context) {
        this.set = set;
        db = new DatabaseHandler(context);

    }

    public class DateHistoryHolder extends RecyclerView.ViewHolder {
        public TextView date, netamount, lessamount;
        public CardView cardView;

        public DateHistoryHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cardview);
            date = (TextView) view.findViewById(R.id.date);
            lessamount = (TextView) view.findViewById(R.id.lessamount);
            netamount = (TextView) view.findViewById(R.id.netamount);
        }
    }

    @Override
    public DateHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.datelist, parent, false);

        return new DateHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateHistoryHolder holder, int position) {
        settingZigZagColor(holder);
        setItems(holder, position);
    }

    private void setItems(DateHistoryHolder holder, int position) {
        String date = set.get(position);
        holder.date.setText(date);

        txndetails = db.getTransactionOn(date);
        int less = 0, net = 0;
        for (Trasaction d : txndetails) {
            if (d.getDate().equals(date)) {
                less += d.getTotalAfterDiscount();
                net += d.getTotalBeforeDiscount();
            }
        }

        holder.lessamount.setText("Discount: " + less);
        holder.netamount.setText("NetTotal: " + net);
    }

    private void settingZigZagColor(DateHistoryHolder holder) {
        if (!makeWhite) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#BDBDBD"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }
        makeWhite = !makeWhite;
    }


    @Override
    public int getItemCount() {
        return set.size();
    }

}
