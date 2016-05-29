package com.ahmed.usuf.billingdesign.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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

import com.ahmed.usuf.billingdesign.utili.AppConstants;
import com.ahmed.usuf.billingdesign.DatabaseHandler.DatabaseHandler;
import com.ahmed.usuf.billingdesign.Fragments.AddItem;
import com.ahmed.usuf.billingdesign.Fragments.ViewCart;
import com.ahmed.usuf.billingdesign.data.LineItem;
import com.ahmed.usuf.billingdesign.Interfaces.BillCallBack;
import com.ahmed.usuf.billingdesign.Interfaces.PercentageCallBack;
import com.ahmed.usuf.billingdesign.Interfaces.fragmentLifeCycle;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.singleton.AppController;
import com.ahmed.usuf.billingdesign.data.TrasactionDetails;
import com.ahmed.usuf.billingdesign.utili.SystemConfig;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.common.base.Strings;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends AppCompatActivity implements ReceiveListener,BillCallBack,PercentageCallBack {

    EditText prc, tot;
    int totalCount;
    Printer mPrinter;
    public static int width;
    public static int height;
    public static int discountTotal, reducedAmount;
    private Adapter adapter;
    ViewPager viewPager;
    DatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseHandler(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        prc = (EditText) findViewById(R.id.pricelabel);
        tot = (EditText) findViewById(R.id.totalLabel);

        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                int currentPosition = 0;

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
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View focus;
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.setVisibility(View.VISIBLE);
                        focus = getCurrentFocus();
                        if (focus != null) {
                            hiddenKeyboard(focus);
                        }
                        break;
                    case 1:
                        tabLayout.setVisibility(View.GONE);
                        focus = getCurrentFocus();
                        if (focus != null) {
                            hiddenKeyboard(focus);
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        display();
    }

    private void getSalesReport() {
        LocalDateTime localDateTime = new LocalDateTime();
        DateTimeFormatter fmt;
        fmt = DateTimeFormat.forPattern("d/MM/yy");
        String str = localDateTime.toString(fmt);
        List<TrasactionDetails> trasactionDetailsList = db.getAllTransantions();
        int billableAmount = 0, discountAmount = 0;
        for (TrasactionDetails details : trasactionDetailsList) {
            if (details.getDate().contains(str)) {
                billableAmount += details.getFinalTotal();
                discountAmount += details.getDiscount();
            }
        }
        Toast.makeText(HomeScreen.this, "2354134652-" + billableAmount + "%%7658486543-" + discountAmount, Toast.LENGTH_LONG).show();
    }

    private void display() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    public static String rupeeFormat(String value){
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }

    private void clearList() {
        AppController.getInstance().getBag().clear();
        ViewCart.mAdapter.swap(AppController.getInstance().getBag());
    }

    private void setConfig() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.config_screen);
        dialog.setTitle("Set Configuration");

        final EditText day,ip;
        final Button save;

        day=(EditText)dialog.findViewById(R.id.day);
        day.setText(""+SystemConfig.getInstance().getDay());
        day.setGravity(Gravity.CENTER);

        ip=(EditText)dialog.findViewById(R.id.ip);
        ip.setText(SystemConfig.getInstance().getIp());
        save=(Button)dialog.findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date1 = day.getText().toString();
                String ip1 = ip.getText().toString();

                AppController.getInstance().getEditor().
                        putString(AppConstants.PRINTER_IP, ip1);
                AppController.getInstance().getEditor().
                        putString(AppConstants.Number_of_Days, date1);
                AppController.getInstance().getEditor().commit();

                SystemConfig.getInstance().setIp(AppController.getInstance().getSharedpreferences().getString(AppConstants.PRINTER_IP, null));
                SystemConfig.getInstance().setDay(AppController.getInstance().getSharedpreferences().getString(AppConstants.Number_of_Days, null));

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
                }else{
                    Toast.makeText(getApplicationContext(),"No Changes",Toast.LENGTH_LONG).show();
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

        Button dialogButton = (Button) dialog.findViewById(R.id.calculate);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Strings.isNullOrEmpty(price.getText().toString()) && Strings.isNullOrEmpty(percentage.getText().toString())) {
                    Toast.makeText(HomeScreen.this, "Please enter Percentage or Amount", Toast.LENGTH_LONG).show();
                    View focus = HomeScreen.this.getCurrentFocus();
                    if (focus != null) {
                        hiddenKeyboard(focus);
                    }
                    dialog.dismiss();
                } else {
                    if (AppController.getInstance().getBag().size() > 0) {
                        if (price.getText().toString().isEmpty()) {
                            int total = AppController.getInstance().getTotal();
                            int percent = Integer.parseInt(percentage.getText().toString());
                            Toast.makeText(HomeScreen.this, "Applying " + percent + " % on the Total Amount ...", Toast.LENGTH_LONG).show();
                            double p = ((double) percent / 100);
                            double result = (double) total * p;
                            reducedAmount = (int) result;
                            total -= reducedAmount;
                            discountTotal = total;
                            AppController.getInstance().setIsDiscountOn(true);
                            ViewCart.mAdapter.swap(AppController.getInstance().getBag());
                            dialog.dismiss();
                        } else if (percentage.getText().toString().isEmpty()) {
                            int total = AppController.getInstance().getTotal();
                            reducedAmount = Integer.parseInt(price.getText().toString());
                            total -= reducedAmount;
                            discountTotal = total;
                            AppController.getInstance().setIsDiscountOn(true);
                            percentage.setEnabled(false);
                            ViewCart.mAdapter.swap(AppController.getInstance().getBag());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(HomeScreen.this, "Can't Apply the Discount Amount", Toast.LENGTH_LONG).show();
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
        String title = "MenCity";
        String message = "Are you sure want to Exit";
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

    class AsyncTask extends android.os.AsyncTask<String ,Void, String> {
        @Override
        protected String doInBackground(String... params) {
            printLogic();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            billNoIncrementing();
        }
    }

    private void confirmPrint() {
        Context context = this;
        String title = "MensCity";
        String message = "Really want to Print";
        String button1String = "Yes";
        String button2String = "No";

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(
                button1String,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        new AsyncTask().execute();
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
        if(AppController.getInstance().isDiscountOn()) {
            db.addTransaction(new TrasactionDetails(HomeScreen.discountTotal, HomeScreen.reducedAmount));
        }else{
            db.addTransaction(new TrasactionDetails(AppController.getInstance().getTotal(), 0));
        }
        AppController.getInstance().getEditor().
                putInt("billno", ((int) AppController.getInstance().getSharedpreferences().getInt("billno", 0) + 1));
        AppController.getInstance().getEditor().commit();
        AppController.getInstance().setIsDiscountOn(false);
        clearList();
        viewPager.setCurrentItem(0, true);
    }

    private void printLogic() {
        Intent intent = null;
        try {
            mPrinter = new Printer(Printer.TM_T82,
                    Printer.MODEL_CHINESE,
                    HomeScreen.this);

            mPrinter.setReceiveEventListener(HomeScreen.this);
            String printerkey="printer.design.";
            mPrinter.addTextSize(4, 2);
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addText(AppController.getInstance().getProperty(printerkey + "shopname", getApplicationContext()));
            mPrinter.addFeedLine(2);

            mPrinter.addTextSize(1, 1);
            mPrinter.addText(AppController.getInstance().getProperty(printerkey + "location", getApplicationContext()));
            mPrinter.addFeedLine(1);

            mPrinter.addTextSize(2, 1);
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addText("Phone: " + AppController.getInstance().getProperty(printerkey + "mobile", getApplicationContext())); ;
            mPrinter.addFeedLine(2);

            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            mPrinter.addTextSize(1, 1);
            mPrinter.addText(AppController.getInstance().getProperty(printerkey + "tin", getApplicationContext()));
            mPrinter.addText("\t");

            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            LocalDateTime localDateTime = new LocalDateTime();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("d/MM/yy hh:mm a");
            String str = localDateTime.toString(fmt);
            mPrinter.addText(str);
            mPrinter.addFeedLine(1);


            mPrinter.addText("Bill No : " + Strings.padStart("" + AppController.getInstance().getSharedpreferences().getInt("billno", 0), 5, '0'));
            mPrinter.addFeedLine(1);
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            mPrinter.addText(AppConstants.PRINTER_LINE_FEED);
            mPrinter.addText("No\tItem\t\tPrice\tQty\t Total\n");
            mPrinter.addText(AppConstants.PRINTER_LINE_FEED);

            StringBuffer sb = new StringBuffer("");
            if (AppController.getInstance().getBag().size() > 0) {
                int itemNo=0,qtyCount=0;
                for(LineItem lineItem:AppController.getInstance().getBag()){
                    String temp=String.format("%2s", ++itemNo +"\t")+
                            String.format("%-10s", lineItem.getProductName())+"\t"+String.format("%4s", lineItem.getPrice())+"\t"+
                    String.format("%3s", lineItem.getQty()+"\t"+ String.format("%8s", lineItem.getTotal() +"\n"));
                    sb.append(temp);
                    qtyCount+=Integer.parseInt(lineItem.getQty());
                }

                mPrinter.addText(sb.toString());
                mPrinter.addText(AppConstants.PRINTER_LINE_FEED);

                String temp=String.format("%2s","\t")+
                        String.format("%-10s", "")+"\t"+String.format("%4s", "\t")+String.format("%3s",qtyCount+ "\t")+ String.format("%8s", AppController.getInstance().getTotal() +  "\n");

                mPrinter.addText(temp);

                mPrinter.addText(AppConstants.PRINTER_LINE_FEED);
                if(AppController.getInstance().isDiscountOn()){
                    mPrinter.addTextAlign(Printer.ALIGN_RIGHT);

                    mPrinter.addText("Discount : "+"-"+HomeScreen.reducedAmount);
                    mPrinter.addFeedLine(1);
                    mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                    mPrinter.addTextSize(1,2);
                    mPrinter.addText("------------------------------------------------\n\n\n");
                    mPrinter.addTextSize(3, 3);
                    mPrinter.addText("Total : ");
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.TRUE);
                    mPrinter.addText(HomeScreen.discountTotal + "/-");
                }else{
                    mPrinter.addText("\n\n");
                    mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                    mPrinter.addTextSize(3, 3);
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.FALSE);
                    mPrinter.addText("Total : ");
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.TRUE);
                    mPrinter.addText(AppController.getInstance().getTotal() + "/-");
                    mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.FALSE);
                }
                mPrinter.addText("\n\n");
                mPrinter.addFeedLine(1);

            }

            mPrinter.addCut(Printer.PARAM_DEFAULT);

            mPrinter.connect("TCP:" + SystemConfig.getInstance().getIp(), Printer.PARAM_DEFAULT);
            mPrinter.beginTransaction();
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Epos2Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        if (id==R.id.sales){
            getSalesReport();
        }
        if (id==R.id.clear){
            clearList();
            AppController.getInstance().setIsDiscountOn(false);
            viewPager.setCurrentItem(0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void startActivity(Class to){
        Intent i=new Intent(HomeScreen.this,to);
        startActivity(i);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager(), this);
        adapter.addFragment(new AddItem(), "Bill It!!");
        adapter.addFragment(new ViewCart(), "Bill Details");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
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
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    // ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                }
            });
        }

        finalizeObject();
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
        AppController.getInstance().onActivityResumed(this);
        super.onResume();
    }
}
