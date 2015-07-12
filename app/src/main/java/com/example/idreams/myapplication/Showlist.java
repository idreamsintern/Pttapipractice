package com.example.idreams.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Showlist extends ListActivity {

    final static String LOG_TAG = "ShowList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cn't use setcontentview
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.i(LOG_TAG,"get intent");
        int total = bundle.getInt("total");
        int period = bundle.getInt("period");
        String message = bundle.getString("message");
        String[] boardlist = bundle.getStringArray("titles");
        Log.i(LOG_TAG,"total" + total);
        Log.i(LOG_TAG, "peroid" + period);
        Log.i(LOG_TAG, "message" + message);
        //ListView list = (ListView)findViewById(R.id.listView) ;
        ArrayAdapter<String> a =new ArrayAdapter<>(this,R.layout.list_item, boardlist);
        setListAdapter(a);

    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_showlist, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
    @Override
    protected void onListItemClick(ListView l,View v,int position,long id)
    {
        //super.onListItemClick(l,v,position,id);
    }
}
