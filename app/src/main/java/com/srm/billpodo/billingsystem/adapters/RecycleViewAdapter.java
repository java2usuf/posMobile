package com.srm.billpodo.billingsystem.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.activities.HomeScreen;
import com.srm.billpodo.billingsystem.data.LineItem;
import com.srm.billpodo.billingsystem.singleton.AppController;

import java.util.List;

/**
 * Created by Ahmed-Mariam on 3/31/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    public static List<LineItem> itemList;
    int rowLength;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, priceView, qtyView, totalView;
        public ImageButton remove;

        public MyViewHolder(View view) {
            super(view);
            remove = (ImageButton) view.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Position: " + getAdapterPosition());
                    if (getAdapterPosition() != 0) {
                        delete(getAdapterPosition() - 1);
                    }
                }
            });
            itemName = (TextView) view.findViewById(R.id.productTitle);
            qtyView = (TextView) view.findViewById(R.id.qtyview);
            priceView = (TextView) view.findViewById(R.id.priceview);
            totalView = (TextView) view.findViewById(R.id.totalvalue);
            totalView.setWidth(HomeScreen.width);
        }
    }

    public RecycleViewAdapter(List<LineItem> moviesList) {
        try {
            itemList = moviesList;
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
        holder.itemName.setVisibility(View.VISIBLE);
        holder.itemName.setGravity(Gravity.CENTER);
        holder.priceView.setVisibility(View.VISIBLE);
        holder.qtyView.setVisibility(View.VISIBLE);
        holder.itemName.setWidth(HomeScreen.width / 3);
        holder.qtyView.setWidth(HomeScreen.width / 5);
        holder.priceView.setWidth(HomeScreen.width / 5);
        holder.totalView.setWidth(HomeScreen.width / 5);
        holder.totalView.setGravity(Gravity.CENTER);
        holder.priceView.setGravity(Gravity.CENTER);
        holder.qtyView.setGravity(Gravity.CENTER);
        holder.remove.setVisibility(View.GONE);
        holder.itemName.setTextSize(HomeScreen.width / 50);
        holder.qtyView.setTextSize(HomeScreen.width / 50);
        holder.priceView.setTextSize(HomeScreen.width / 50);
        holder.totalView.setTextSize(HomeScreen.width / 50);

        if (position == 0) {
            settingHeaderValue(holder);
            return;
        }

        if (AppController.getInstance().isDiscountOn()) {
            if (position + 1 <= rowLength - 3 && position > 0) {
                addLineItem(holder, position);
                return;
            }

        } else {
            if (position + 1 < rowLength && position > 0) {
                addLineItem(holder, position);
                return;
            }

        }

        if (position + 1 == rowLength) {
            showTotalAmount(holder);
            return;
        }

        if (position + 1 == rowLength - 2 && AppController.getInstance().isDiscountOn()) {
            holder.itemName.setVisibility(View.GONE);
            holder.priceView.setVisibility(View.GONE);
            holder.qtyView.setVisibility(View.GONE);
            holder.totalView.setWidth(HomeScreen.width);
            holder.totalView.setTextColor(Color.parseColor("#FF5722"));
            holder.totalView.setGravity(Gravity.RIGHT);
            holder.totalView.setTextSize(25);

            holder.totalView.setText("SubTotal : " + Strings.padStart("" + AppController.getInstance().getTotal(), 30, ' '));
            return;
        }

        if (position + 1 == rowLength - 1 && AppController.getInstance().isDiscountOn()) {
            holder.itemName.setVisibility(View.GONE);
            holder.priceView.setVisibility(View.GONE);
            holder.qtyView.setVisibility(View.GONE);
            holder.totalView.setWidth(HomeScreen.width);
            holder.totalView.setGravity(Gravity.RIGHT);
            holder.totalView.setTextColor(Color.parseColor("#FF5722"));
            holder.totalView.setTextSize(25);
            holder.totalView.setText("Discount : " + Strings.padStart("-" + HomeScreen.reducedAmount, 30, ' '));
            return;
        }
    }

    private void showTotalAmount(MyViewHolder holder) {
        holder.totalView.setTextSize(70);
        holder.itemName.setVisibility(View.GONE);
        holder.priceView.setVisibility(View.GONE);
        holder.qtyView.setVisibility(View.GONE);
        holder.totalView.setWidth(HomeScreen.width);
        holder.totalView.setGravity(Gravity.CENTER_HORIZONTAL);
        holder.totalView.setTextColor(Color.parseColor("#FF5722"));
        holder.totalView.setTypeface(Typeface.SANS_SERIF);

        if (AppController.getInstance().isDiscountOn()) {
            holder.totalView.setText("\u20B9 " + HomeScreen.discountTotal);
        } else {
            holder.totalView.setText("\u20B9 " + AppController.getInstance().getTotal());
        }
    }

    private void addLineItem(MyViewHolder holder, int position) {
        LineItem lineItem = itemList.get(position - 1);

        holder.priceView.setTextColor(Color.parseColor("#6200EA"));
        holder.totalView.setTextColor(Color.parseColor("#6200EA"));
        holder.itemName.setTextColor(Color.parseColor("#6200EA"));
        holder.qtyView.setTextColor(Color.parseColor("#6200EA"));

        if(lineItem.isReturnProduct() && (Integer.parseInt(lineItem.getTotal()) >0)){
            lineItem.setProductName(lineItem.getProductName() + "(R)");
            int total =Integer.parseInt(lineItem.getTotal()) * -1;
            lineItem.setTotal("" + total);
        }

        holder.itemName.setText(lineItem.getProductName());
        holder.priceView.setText(lineItem.getPrice());
        holder.qtyView.setText(lineItem.getQty());
        holder.totalView.setText(lineItem.getTotal());
        holder.remove.setVisibility(View.VISIBLE);

        holder.priceView.setTypeface(Typeface.SERIF, Typeface.BOLD);
        holder.qtyView.setTypeface(Typeface.SERIF, Typeface.BOLD);
        holder.totalView.setTypeface(Typeface.SERIF, Typeface.BOLD);


    }

    private void settingHeaderValue(MyViewHolder holder) {
        holder.itemName.setTypeface(Typeface.SERIF, Typeface.BOLD);
        holder.priceView.setTypeface(Typeface.SERIF, Typeface.BOLD);
        holder.qtyView.setTypeface(Typeface.SERIF, Typeface.BOLD);
        holder.totalView.setTypeface(Typeface.SERIF, Typeface.BOLD);
        holder.remove.setVisibility(View.VISIBLE);

        holder.itemName.setText("ITEM");
        holder.priceView.setText("PRICE");
        holder.totalView.setText("TOTAL");
        holder.qtyView.setText("QTY");

        holder.priceView.setTextColor(Color.parseColor("#33cc33"));
        holder.totalView.setTextColor(Color.parseColor("#33cc33"));
        holder.itemName.setTextColor(Color.parseColor("#33cc33"));
        holder.qtyView.setTextColor(Color.parseColor("#33cc33"));
    }

    public void swap(List<LineItem> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        itemList.remove(position);
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
        } catch (Exception e) {
            return 0;
        }

    }
}
