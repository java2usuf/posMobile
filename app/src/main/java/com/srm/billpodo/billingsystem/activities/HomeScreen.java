package com.srm.billpodo.billingsystem.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.common.base.Strings;
import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.data.Trasaction;
import com.srm.billpodo.billingsystem.db.DatabaseHandler;
import com.srm.billpodo.billingsystem.fragments.AddItem;
import com.srm.billpodo.billingsystem.fragments.ViewCart;
import com.srm.billpodo.billingsystem.interfaces.BillCallBack;
import com.srm.billpodo.billingsystem.interfaces.PercentageCallBack;
import com.srm.billpodo.billingsystem.interfaces.fragmentLifeCycle;
import com.srm.billpodo.billingsystem.transformation.ShowNumbersTransformationMethod;
import com.srm.billpodo.billingsystem.util.AppConstants;
import com.srm.billpodo.billingsystem.util.ReceiptPrinter;
import com.srm.billpodo.billingsystem.util.ShowMsg;
import com.srm.billpodo.billingsystem.util.SystemConfig;
import com.srm.billpodo.billingsystem.util.Utility;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.srm.billpodo.billingsystem.singleton.AppController.getInstance;

public class HomeScreen extends AppCompatActivity implements BillCallBack, PercentageCallBack, ReceiveListener {

    EditText prc, tot;
    public static int width;
    public static int height;
    public static int discountTotal, reducedAmount;
    private Adapter adapter;
    private Context mContext = null;
    ViewPager viewPager;
    DatabaseHandler db;
    Trasaction txnDetails = getInstance().getTxnDetails();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        db = new DatabaseHandler(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        prc = (EditText) findViewById(R.id.pricelabel);
        tot = (EditText) findViewById(R.id.totalLabel);


        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                int currentPosition = 0;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int newPosition) {
                    fragmentLifeCycle fragmentToShow = (fragmentLifeCycle) adapter.getItem(newPosition);
                    fragmentToShow.onResumeFragment();
                    fragmentLifeCycle fragmentToHide = (fragmentLifeCycle) adapter.getItem(currentPosition);
                    fragmentToHide.onPauseFragment();
                    currentPosition = newPosition;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        display();
    }

    private void display() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    private void getSalesReport() {
        LocalDateTime localDateTime = new LocalDateTime();
        DateTimeFormatter fmt;
        fmt = DateTimeFormat.forPattern(AppConstants.D_MM_YY);
        String str = localDateTime.toString(fmt);
        List<Trasaction> trasactionList = db.getAllTransantions();
        int billableAmount = 0, discountAmount = 0;
        for (Trasaction details : trasactionList) {
            if (details.getDate().contains(str)) {
                billableAmount += details.getTotalBeforeDiscount();
                discountAmount += details.getTotalAfterDiscount();
            }
        }
        Toast.makeText(HomeScreen.this, "2354134652-" + billableAmount + "%%7658486543-" + discountAmount, Toast.LENGTH_LONG).show();
    }

    private void clearList() {
        getInstance().getBag().clear();
        getInstance().setTxnDetails(new Trasaction());
        ViewCart.mAdapter.swap(getInstance().getBag());
    }

    private void setConfig() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.config_screen);
        String title = "System Config";
        dialog.setTitle(title);

        final EditText day, ip;
        final Button save;

        day = (EditText) dialog.findViewById(R.id.day);
        day.setText("" + SystemConfig.getInstance().getDay());
        day.setGravity(Gravity.CENTER);

        ip = (EditText) dialog.findViewById(R.id.ip);
        ip.setText(SystemConfig.getInstance().getIp());
        save = (Button) dialog.findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date1 = day.getText().toString();
                String ip1 = ip.getText().toString();

                getInstance().getEditor().
                        putString(AppConstants.PRINTER_IP, ip1);
                getInstance().getEditor().
                        putString(AppConstants.Number_of_Days, date1);
                getInstance().getEditor().commit();

                SystemConfig.getInstance().setIp(getInstance().getSharedpreferences().getString(AppConstants.PRINTER_IP, null));
                SystemConfig.getInstance().setDay(getInstance().getSharedpreferences().getString(AppConstants.Number_of_Days, null));

                if (date1.length() > 0 && ip1.length() > 0) {
                    SystemConfig.getInstance().setDay(date1);
                    SystemConfig.getInstance().setIp(ip1);
                    dialog.dismiss();
                } else if (date1.length() > 0) {
                    SystemConfig.getInstance().setDay(date1);
                    dialog.dismiss();
                } else if (ip1.length() > 0) {
                    SystemConfig.getInstance().setIp(ip1);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "No Changes", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void discountDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.discount);
        dialog.setTitle(AppConstants.MSG_PERCENTAGE_DIALOG);
        final EditText price, percentage;
        price = (EditText) dialog.findViewById(R.id.pricediscount);
        percentage = (EditText) dialog.findViewById(R.id.percentage);
        price.setTransformationMethod(new ShowNumbersTransformationMethod());
        percentage.setTransformationMethod(new ShowNumbersTransformationMethod());

        Button dialogButton = (Button) dialog.findViewById(R.id.calculate);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Strings.isNullOrEmpty(price.getText().toString()) && Strings.isNullOrEmpty(percentage.getText().toString())) {
                    Toast.makeText(HomeScreen.this, AppConstants.PLEASE_ENTER_PERCENTAGE_OR_AMOUNT, Toast.LENGTH_LONG).show();
                    View focus = HomeScreen.this.getCurrentFocus();
                    if (focus != null) {
                        hiddenKeyboard(focus);
                    }
                    dialog.dismiss();
                } else {
                    if (getInstance().getBag().size() > 0) {
                        if (price.getText().toString().isEmpty()) {
                            int total = getInstance().getTotal();
                            int percent = Integer.parseInt(percentage.getText().toString());
                            txnDetails.setDiscountProvidedInPercentage("" + percent);
                            Toast.makeText(HomeScreen.this, "Applying " + percent + " % on the Total Amount ...", Toast.LENGTH_LONG).show();
                            double p = ((double) percent / 100);
                            double result = (double) total * p;
                            reducedAmount = (int) result;
                            total -= reducedAmount;
                            discountTotal = total;
                            txnDetails.setDisacountGiven(discountTotal);
                            getInstance().setIsDiscountOn(true);
                            ViewCart.mAdapter.swap(getInstance().getBag());
                            dialog.dismiss();
                        } else if (percentage.getText().toString().isEmpty()) {
                            int total = getInstance().getTotal();
                            reducedAmount = Integer.parseInt(price.getText().toString());
                            txnDetails.setDiscountProvidedInAmount(reducedAmount);
                            total -= reducedAmount;
                            discountTotal = total;
                            txnDetails.setDisacountGiven(discountTotal);
                            getInstance().setIsDiscountOn(true);
                            percentage.setEnabled(false);
                            ViewCart.mAdapter.swap(getInstance().getBag());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(HomeScreen.this, AppConstants.CAN_T_APPLY_THE_DISCOUNT_AMOUNT, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                }
            }
        });
        dialog.show();
    }

    private void confirmExit() {
        Context context = this;
        String title = AppConstants.DIALOG_TITLE_MEN_CITY;
        String message = AppConstants.DIALOG_EXIT_MSG;
        String button1String = "Yes";
        String button2String = "No";

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);

        ad.setPositiveButton(
                button1String,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        finish();
                    }
                }
        );

        ad.setNegativeButton(
                button2String,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        // do nothing
                    }
                }
        );

        //
        ad.show();
    }

    class PrinterAsyncTask extends android.os.AsyncTask<Object, Void, String> {

        ReceiveListener activy;

        public PrinterAsyncTask(ReceiveListener activy) {
            this.activy = activy;
        }


        @Override
        protected String doInBackground(Object... params) {
            try {
                txnDetails.setTotalBeforeDiscount(getInstance().getTotal());
                txnDetails.setTotalAfterDiscount(discountTotal);
                txnDetails.setLineItems(getInstance().getBag());
                new ReceiptPrinter().printLogic(activy,txnDetails);
            } catch (Exception e) {
                Log.e("test", "", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            billNoIncrementing();
        }
    }

    private void confirmPrint() {
        Context context = this;
        String title = "BillPoDu";
        String message = "Really want to Print";
        String button1String = "Yes";
        String button2String = "No";

        final PrinterAsyncTask printJob = new PrinterAsyncTask(this);

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(
                button1String,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        printJob.execute();
                    }
                }
        );

        ad.setNegativeButton(
                button2String,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        // do nothing

                    }
                }
        );

        //
        ad.show();
    }

    private void billNoIncrementing() {
        if (getInstance().isDiscountOn()) {
            db.addTransaction(new Trasaction(HomeScreen.discountTotal, HomeScreen.reducedAmount));
        } else {
            db.addTransaction(new Trasaction(getInstance().getTotal(), 0));
        }

        getInstance().getEditor().
                putInt("billno", (getInstance().getSharedpreferences().getInt("billno", 0) + 1));
        getInstance().getEditor().commit();
        getInstance().setIsDiscountOn(false);
        clearList();

        viewPager.setCurrentItem(0, true);
    }


    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            setConfig();
            return true;
        }

        if (id == R.id.report) {
            startActivity(DateHistoryScreen.class);
            return true;
        }

        if (id == R.id.sales) {
            getSalesReport();
        }
        if (id == R.id.clear) {
            clearList();
            getInstance().setIsDiscountOn(false);
            viewPager.setCurrentItem(0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void startActivity(Class to) {
        Intent i = new Intent(HomeScreen.this, to);
        startActivity(i);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager(), this);
        adapter.addFragment(new AddItem(), AppConstants.BILL_IT);
        adapter.addFragment(new ViewCart(), AppConstants.BILL_DETAILS);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void printBill() {
        confirmPrint();
    }

    @Override
    public void calculatePercentage() {
        discountDialog();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private static Context context;

        public Adapter(FragmentManager fm, Context c) {
            super(fm);
            context = c;
        }


        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);

        }


        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    @Override
    protected void onResume() {
        getInstance().onActivityResumed(this);
        super.onResume();
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                dispPrinterWarnings(status);

                updateButtonState(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }
        });
    }

    private void finalizeObject() {
        if (getInstance().getmPrinter() == null) {
            return;
        }

        getInstance().getmPrinter().clearCommandBuffer();

        getInstance().getmPrinter().setReceiveEventListener(null);

        getInstance().setmPrinter(null);
    }


    private void updateButtonState(boolean state) {
/*        Button btnReceipt = (Button)findViewById(R.id.save);
        btnReceipt.setEnabled(state);*/
    }

    private void disconnectPrinter() {
        if (getInstance().getmPrinter() == null) {
            return;
        }

        try {
            getInstance().getmPrinter().endTransaction();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            getInstance().getmPrinter().disconnect();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

        Toast.makeText(getApplicationContext(),warningsMsg, Toast.LENGTH_LONG).show();
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

}
