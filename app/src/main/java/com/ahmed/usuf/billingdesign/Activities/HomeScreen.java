package com.ahmed.usuf.billingdesign.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.ahmed.usuf.billingdesign.Fragments.AddItem;
import com.ahmed.usuf.billingdesign.Fragments.ViewCart;
import com.ahmed.usuf.billingdesign.Adapters.LineItem;
import com.ahmed.usuf.billingdesign.Interfaces.fragmentLifeCycle;
import com.ahmed.usuf.billingdesign.R;
import com.ahmed.usuf.billingdesign.Volley.AppController;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends AppCompatActivity implements ReceiveListener {

    EditText prc, tot;
    AddItem billEnterFragment;
    int totalCount;
    Printer mPrinter;
    public static int width;
    public static int height;
    public static int discountTotal, reducedAmount;
    public static boolean isDiscountSet = false;
    public static boolean isBillChanged = false;
    private Adapter adapter;
    ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        billEnterFragment = new AddItem();
        prc = (EditText) findViewById(R.id.pricelabel);
        tot = (EditText) findViewById(R.id.totalLabel);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPrint();
            }
        });

        FloatingActionButton clearfab = (FloatingActionButton) findViewById(R.id.clearbutton);
        clearfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }
        });

        FloatingActionButton server = (FloatingActionButton) findViewById(R.id.server);
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ServerCodegoesHere

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.discount);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountDialog();
            }
        });

        fab.setVisibility(View.GONE);
        final FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        final FrameLayout frameserver = (FrameLayout) findViewById(R.id.frameserver);
        final FrameLayout frameClear = (FrameLayout) findViewById(R.id.frameclear);
        frame.setVisibility(View.GONE);
        frameserver.setVisibility(View.GONE);
        frameClear.setVisibility(View.GONE);

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
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                View focus;
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                        frame.setVisibility(View.GONE);
                        frameserver.setVisibility(View.GONE);
                        frameClear.setVisibility(View.GONE);
                        focus = getCurrentFocus();
                        if (focus != null) {
                            hiddenKeyboard(focus);
                        }
                        break;
                    case 1:
                        tabLayout.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.VISIBLE);
                        frameClear.setVisibility(View.VISIBLE);
                        frameserver.setVisibility(View.VISIBLE);
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

    private void clearList() {
        AppController.getInstance().getBag().clear();
        ViewCart.mAdapter.swap(AppController.getInstance().getBag());
    }

    private void discountDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.discount);
        dialog.setTitle("Enter Percentage or Price to Discount");
        final EditText price, percentage;
        price = (EditText) dialog.findViewById(R.id.pricediscount);
        percentage = (EditText) dialog.findViewById(R.id.percentage);

        Button dialogButton = (Button) dialog.findViewById(R.id.calculate);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price.getText().toString() == null && percentage.getText().toString() == null) {
                    Toast.makeText(HomeScreen.this, "Fill any one Fiels", Toast.LENGTH_LONG).show();
                } else {
                    if (price.getText().toString().isEmpty()) {
                        int total = AppController.getInstance().getTotal();
                        int percent = Integer.parseInt(percentage.getText().toString());
                        Toast.makeText(HomeScreen.this, percent + "=percent", Toast.LENGTH_LONG).show();
                        double p = percent / 100;
                        Toast.makeText(HomeScreen.this, p + "", Toast.LENGTH_LONG).show();
                        double result = total * p;
                        reducedAmount = (int) result;
                        total -= reducedAmount;
                        discountTotal = total;
                        isDiscountSet = true;
                        price.setEnabled(false);
                        ViewCart.mAdapter.swap(AddItem.printerList);
                        isDiscountSet = false;
                        dialog.dismiss();
                    }

                    if (percentage.getText().toString().isEmpty()) {
                        int total = AppController.getInstance().getTotal();
                        reducedAmount = Integer.parseInt(price.getText().toString());
                        total -= reducedAmount;
                        discountTotal = total;
                        isDiscountSet = true;
                        percentage.setEnabled(false);
                        ViewCart.mAdapter.swap(AddItem.printerList);
                        isDiscountSet = false;
                        dialog.dismiss();
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

    private void confirmPrint() {
        Context context = this;
        String title = "MenCity";
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

                        Intent intent = null;
                        try {
                            mPrinter = new Printer(Printer.TM_T82,
                                    Printer.MODEL_CHINESE,
                                    HomeScreen.this);

                            mPrinter.setReceiveEventListener(HomeScreen.this);

                            StringBuilder textData = new StringBuilder();
                            for (LineItem details : AppController.getInstance().getBag()) {
                                textData.append(details.getProductName() + "\t" + details.getPrice() + "\t" + details.getQty() + "\t" + details.getTotal() + "\n");
                            }
                            Log.d("Ahmed---", "Data:" + textData);
                            mPrinter.addText(textData.toString());
                            mPrinter.addCut(Printer.CUT_FEED);

                            mPrinter.connect("TCP:192.168.2.2", Printer.PARAM_DEFAULT);

                            mPrinter.beginTransaction();

                            mPrinter.sendData(Printer.PARAM_DEFAULT);

                            mPrinter.disconnect();
                        } catch (Epos2Exception e) {
                            e.printStackTrace();
                        }
                        AppController.getInstance().getEditor().
                                putInt("billno", ((int) AppController.getInstance().getSharedpreferences().getInt("billno", 0) + 1));
                        AppController.getInstance().getEditor().commit();
                        clearList();
                        viewPager.setCurrentItem(0,true);

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
            Log.d("Position",""+position);
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
