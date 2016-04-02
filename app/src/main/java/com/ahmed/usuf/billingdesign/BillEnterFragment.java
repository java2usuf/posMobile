package com.ahmed.usuf.billingdesign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Ahmed-Mariam on 4/1/2016.
 */
public class BillEnterFragment extends Fragment implements ReceiveListener {

    String[] productNames={"CHUDI","CHUDI MATERIAL","BRA","BABYS","PANT&SSHIRTS","KIDS","FORG","UNDERWEAR","PANTYS","VEST","TOPS","LEGINS"};
    String[] qt={"1","2","3","4","5","6","7","8","9","10"};
    Printer mPrinter;
    EditText prc,tot;

    Spinner pName,qty;

    List list=new ArrayList<ProductDetails>();

    String selectedItem,slectedQty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.bill_enter,container,false);

        pName=(Spinner)v.findViewById(R.id.productname);
        qty=(Spinner)v.findViewById(R.id.qtyspinner);
        prc=(EditText)v.findViewById(R.id.pricelabel);
        tot=(EditText)v.findViewById(R.id.totalLabel);

        FloatingActionButton fab=(FloatingActionButton)v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillEnterFragment.this.getActivity(),"Executing",Toast.LENGTH_SHORT).show();
                runPrintReceiptSequence();

            }
        });

        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, productNames);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        pName.setAdapter(LTRadapter);

        LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, qt);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        qty.setAdapter(LTRadapter);

        pName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = productNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             slectedQty=qt[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(Printer.TM_T82,Printer.MODEL_SOUTHASIA,this.getActivity());
        }
        catch (Exception e) {
            ShowMsg.showException(e, "Printer", this.getActivity());
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    private boolean createReceiptData() {
        String method = "";
        StringBuilder textData = new StringBuilder();

        if (mPrinter == null) {
            return false;
        }

        try {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);

/*            method = "addImage";
            mPrinter.addImage(logoData, 0, 0,
                    logoData.getWidth(),
                    logoData.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO); */
textData.append("Hi Ahmed");
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            textData.append("Hi Yousuf\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());

            method = "addTextSize";
            mPrinter.addTextSize(2, 2);

            method = "addText";
            mPrinter.addText("TextSize\n");

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
        }
        catch (Exception e) {
            ShowMsg.showException(e, method,this.getActivity());
            return false;
        }

        textData = null;

        return true;
    }


    private boolean runPrintReceiptSequence() {
        if (!initializeObject()) {
            return false;
        }

        if (!createReceiptData()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            mPrinter.connect("TCP:192.168.2.2", Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "connect", this.getActivity());
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", this.getActivity());
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            }
            catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        }
        else if (status.getOnline() == Printer.FALSE) {
            return false;
        }
        else {
            ;//print available
        }

        return true;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status),this.getActivity());
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "sendData", this.getActivity());
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    void calculationLogic(View v){
        int price=Integer.parseInt(prc.getText().toString());
        if(price==0) {
            int qty = Integer.parseInt(slectedQty);
            int total = price * qty;
            list.add(new ProductDetails(slectedQty.toString(), prc.getText().toString(), selectedItem, tot.getText().toString()));
        }else {
            Snackbar.make(v, "Please Enter Price", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {

                ShowMsg.showResult(i, makeErrorMessage(printerStatusInfo), this.getActivity());

                dispPrinterWarnings(printerStatusInfo);

                // updateButtonState(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }


    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        }
        catch (final Exception e) {

                    ShowMsg.showException(e, "endTransaction", this.getActivity());

        }

        try {
            mPrinter.disconnect();
        }
        catch (final Exception e) {

                    ShowMsg.showException(e, "disconnect", this.getActivity());

        }

        finalizeObject();
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += "Paper is goona be End";
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += "Low Battery";
        }
        Toast.makeText(this.getActivity(),warningsMsg,Toast.LENGTH_LONG).show();
    }
}
