package com.ahmed.usuf.billingdesign.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ahmed.usuf.billingdesign.Fragments.BillEnterFragment;
import com.ahmed.usuf.billingdesign.Fragments.ProductAdapter;
import com.ahmed.usuf.billingdesign.Adapters.ProductDetails;
import com.ahmed.usuf.billingdesign.R;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BillEnterFragment.netTotalCallBack,ReceiveListener{

    EditText prc,tot;
    BillEnterFragment billEnterFragment;
    int totalCount;
    Printer mPrinter;
    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        prc=(EditText)findViewById(R.id.pricelabel);
        tot=(EditText)findViewById(R.id.totalLabel);

        final FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    mPrinter = new Printer(Printer.TM_T82,
                            Printer.MODEL_CHINESE,
                            MainActivity.this);

                    mPrinter.setReceiveEventListener(MainActivity.this);

                    StringBuilder textData = new StringBuilder();
                    for (ProductDetails details : BillEnterFragment.printerList) {
                        textData.append(details.getProductName() + "\t" + details.getPrice() + "\t" + details.getQty() + "\t" + details.getTotal() + "\n");
                    }
                    Log.d("Ahmed", "Data:" + textData);
                    mPrinter.addText(textData.toString());
                    mPrinter.addCut(Printer.CUT_FEED);

                    mPrinter.connect("TCP:192.168.2.2", Printer.PARAM_DEFAULT);

                    mPrinter.beginTransaction();

                    mPrinter.sendData(Printer.PARAM_DEFAULT);

                    mPrinter.disconnect();
                } catch (Epos2Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fab.setVisibility(View.GONE);
        final FrameLayout frame=(FrameLayout)findViewById(R.id.frame);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                View focus;
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                        frame.setVisibility(View.GONE);
                        focus = getCurrentFocus();
                        if (focus != null) {
                            hiddenKeyboard(focus);
                        }
                        break;
                    case 1:
                        tabLayout.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.VISIBLE);
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
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

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new BillEnterFragment(),"Bill It!!");
        adapter.addFragment(new ProductAdapter(),"Bill Details");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void displaySnackView(String s) {
        View v=(View)findViewById(R.id.fab);
        Snackbar.make(v,"Net Total: "+s,Snackbar.LENGTH_LONG).setAction("Action",null).show();
    }

    @Override
    public void onPtrReceive(Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                // ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                //dispPrinterWarnings(status);

                //updateButtonState(true);

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
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                   // ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    //ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
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
}
