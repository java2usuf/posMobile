package com.srm.billpodo.billingsystem.util;

import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.ReceiveListener;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.activities.HomeScreen;
import com.srm.billpodo.billingsystem.data.LineItem;
import com.srm.billpodo.billingsystem.data.Trasaction;
import com.srm.billpodo.billingsystem.singleton.AppController;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.srm.billpodo.billingsystem.singleton.AppController.getInstance;
import static com.srm.billpodo.billingsystem.util.AppConstants.PRINTER_LINE_FEED;
import static com.srm.billpodo.billingsystem.util.AppConstants.PRINTER_LINE_WITH_LINE_FEED;

/**
 * Created by myousuff on 7/29/16.
 */
public class ReceiptPrinter {

    static Printer mPrinter;

    public void printLogic(ReceiveListener activity,Trasaction txn) {

        Intent intent = null;
        try {
            mPrinter = new Printer(Printer.TM_T82,
                    Printer.MODEL_CHINESE,
                    AppController.getAppContext());

            getInstance().setmPrinter(mPrinter);

            mPrinter.setReceiveEventListener(activity);
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            mPrinter.addTextSize(1, 1);
            mPrinter.addText("\t" + Utility.getBrandProperty(R.string.brand_store_tin));

            mPrinter.addText("\t\t" + Utility.getBrandProperty(R.string.brand_store_bill) + Strings.padStart("" + getInstance().getSharedpreferences().getInt("billno", 0), 5, '0'));

            txn.setBillNumber(getInstance().getSharedpreferences().getInt("billno", 0));
            mPrinter.addFeedLine(3);

            mPrinter.addTextSize(4, 2);
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addText(Utility.getBrandProperty(R.string.brand_store_name));

            if(Utility.getBrandProperty(R.string.brand_store_name).equals(Utility.getStringValue(R.string.brand_srm_tailor_name))){
                mPrinter.addFeedLine(1);
                mPrinter.addTextSize(1, 1);
                mPrinter.addText(Utility.getStringValue(R.string.brand_srm_tailor_name_short));
                mPrinter.addText(PRINTER_LINE_FEED);
            }
            mPrinter.addFeedLine(2);


            mPrinter.addTextSize(1, 1);
            mPrinter.addText(Utility.getBrandProperty(R.string.brand_store_short_address));
            mPrinter.addFeedLine(1);

            mPrinter.addTextSize(2, 1);
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addText(Utility.getBrandProperty(R.string.brand_store_mobile));
            mPrinter.addFeedLine(2);


            mPrinter.addTextSize(1, 1);
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            LocalDateTime localDateTime = new LocalDateTime();

            DateTimeFormatter fmt = DateTimeFormat.forPattern("d/MMM/yy hh:mm a");
            String str = localDateTime.toString(fmt);

            //Adding Date to Object
            txn.setDate(str);

            mPrinter.addText("\t"+"Date : " + str);
            mPrinter.addFeedLine(1);

            mPrinter.addTextAlign(Printer.ALIGN_LEFT);

            mPrinter.addText(PRINTER_LINE_FEED);
            Log.i("-- Header",Utility.getBrandProperty(R.string.brand_store_printer_header));
            mPrinter.addText("No\tItem\t\tPrice\tQty\tTotal\n");
            mPrinter.addText(PRINTER_LINE_FEED);

            StringBuilder sb = new StringBuilder("");


            if (getInstance().getBag().size() > 0) {
                int itemNo = 0, qtyCount = 0;


                for (LineItem lineItem : getInstance().getBag()) {
                    String temp = String.format("%2s", ++itemNo + "\t") +
                            String.format("%-10s", lineItem.getProductName()) + "\t" + String.format("%4s", lineItem.getPrice()) + "\t" +
                            String.format("%3s", lineItem.getQty() + "\t" + String.format("%8s", lineItem.getTotal() + "\n"));
                    sb.append(temp);
                    qtyCount += Integer.parseInt(lineItem.getQty());
                }

                mPrinter.addText(sb.toString());
                mPrinter.addText(PRINTER_LINE_FEED);
                txn.setTotalQuantity(qtyCount);
                String temp = String.format("%2s", "\t") +
                        String.format("%-10s", "") + "\t" + String.format("%4s", "\t") + String.format("%3s", qtyCount + "\t") + String.format("%8s", getInstance().getTotal() + "\n");

                mPrinter.addText(temp);

                mPrinter.addText(PRINTER_LINE_FEED);
                if (getInstance().isDiscountOn()) {
                    mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
                    mPrinter.addText("Discount : " + "-" + HomeScreen.reducedAmount);
                    mPrinter.addFeedLine(1);
                    mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                    mPrinter.addTextSize(1, 2);
                    mPrinter.addText(PRINTER_LINE_WITH_LINE_FEED);
                    mPrinter.addTextSize(3, 3);
                    mPrinter.addText("Total : ");
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.TRUE);
                    mPrinter.addText(HomeScreen.discountTotal + "/-");
                } else {
                    mPrinter.addText("\n\n");
                    mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                    mPrinter.addTextSize(3, 3);
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.FALSE);
                    mPrinter.addText("Total : ");
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.TRUE);
                    mPrinter.addText(getInstance().getTotal() + "/-");
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.FALSE);
                }
                mPrinter.addText("\n\n");
                mPrinter.addFeedLine(1);
            }
            mPrinter.addCut(Printer.PARAM_DEFAULT);
            String target = "TCP:" + SystemConfig.getInstance().getIp();
            mPrinter.connect(target, Printer.PARAM_DEFAULT);
            Log.i("Connecting to Printer", target);
            mPrinter.beginTransaction();
            mPrinter.sendData(Printer.PARAM_DEFAULT);

            sendMockData(txn);

        } catch (Epos2Exception e) {
            Log.e(ReceiptPrinter.class.getName(), "Epos2Exception Exception", e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(ReceiptPrinter.class.getName(), "IOException Exception", e);
            e.printStackTrace();
        }
    }

    String json;
    void sendMockData(Trasaction txn){
        // Instantiate the RequestQueue.
        String url ="http://srm-server-billing.appspot.com/transaction";

        try {
            Gson gson=new Gson();
            json = gson.toJson(txn);

            Log.i("Testing ---",json);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // your response
                        Log.d("Response",""+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error","Error");
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                 Log.d("JsonValue",""+json);
                return json.getBytes();
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,"StringRequest");
    }
}
