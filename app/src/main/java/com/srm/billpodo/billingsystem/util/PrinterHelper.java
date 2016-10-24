package com.srm.billpodo.billingsystem.util;

import android.util.Log;

import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.StatusChangeListener;

/**
 * Created by myousuff on 7/30/16.
 */
public class PrinterHelper implements StatusChangeListener {

    @Override
    public void onPtrStatusChange(Printer printer, int i) {
        Log.i("Printer listener _____", ""+i);
    }
}
