package com.srm.billpodo.billingsystem.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.srm.billpodo.billingsystem.R;
import com.srm.billpodo.billingsystem.adapters.DateHistoryAdapter;
import com.srm.billpodo.billingsystem.data.Trasaction;
import com.srm.billpodo.billingsystem.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DateHistoryScreen extends ActionBarActivity {

    public RecyclerView recyclerView;
    DatabaseHandler db;
    public DateHistoryAdapter mAdapter;
    private List<Trasaction> list = new ArrayList<Trasaction>();
    private List<Trasaction> dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_screen);

        db = new DatabaseHandler(this);

        dummy = db.getAllTransantions();

        final ArrayList<String> result = new ArrayList<>();

        HashSet<String> set = new HashSet<>();

        for (Trasaction item : dummy) {

            if (!set.contains(item.getDate())) {
                result.add(item.getDate());
                set.add(item.getDate());
            }
        }

        Log.d("Checking", result.toString());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mAdapter = new DateHistoryAdapter(result, this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new DateHistoryScreen.RecyclerTouchListener(this, recyclerView, new DateHistoryScreen.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String d = result.get(position);
                Toast.makeText(getApplicationContext(), result.get(position) + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DateHistoryScreen.this, ConfigScreen.class);
                i.putExtra("date", d);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config_screen, menu);
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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private DateHistoryScreen.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DateHistoryScreen.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
