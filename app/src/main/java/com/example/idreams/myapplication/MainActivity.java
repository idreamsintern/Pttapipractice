package com.example.idreams.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;

import android.widget.Toast;

import org.json.JSONObject;

import com.loopj.android.http.*;


public class MainActivity extends Activity {
    final static String LOG_TAG = "MainActivity";
    String Board="gossip";
    String Period="1";
    String Limit ="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "onCreate Called! i");
        Log.d(LOG_TAG, "onCreate Called! d");
        Log.v(LOG_TAG, "onCreate Called! v");
        Log.w(LOG_TAG, "onCreate Called! w");
        Log.e(LOG_TAG, "onCreate Called! e");

        //Get串接傳輸按鈕與Post串接傳輸按鈕
        Button mBoard = (Button) findViewById(R.id.board);
        Button mPost = (Button) findViewById(R.id.post);
        Button mPeriod =(Button) findViewById(R.id.Periodbutton);
        final EditText mLimit =(EditText)findViewById(R.id.limitedittext);
        final TextView output = (TextView) findViewById(R.id.textView);

        mPeriod.setOnClickListener(new View.OnClickListener(){
            @Override
                public  void onClick(View v)
            {
                PeriodAlertDialog();
            }
        });
        mBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardAlertDialog();
            }
            });
        //按下mGet按鈕進行HttpGet串接傳輸
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                RestClient myclient = new RestClient();
                RequestParams params = new RequestParams();
                params.put("period", Period);
                params.put("limit", mLimit.getText().toString());
                params.put("board", Board);
                params.put("token", "api_doc_token");

                myclient.post("http://api.ser.ideas.iii.org.tw:80/api/top_article/ptt", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // If the response is JSONObject instead of expected JSONArray
                            output.setText(response.toString());
                            Log.i(LOG_TAG, "Success! " + response.toString());
                            Log.i(LOG_TAG, "Response Message: " + response.getString("message"));
                            Log.i(LOG_TAG, "Response Period" + response.getInt("period"));
                            Log.i(LOG_TAG, "Response Result" + response.getJSONArray("result").getJSONObject(0).getString("time"));
                        } catch (Exception err) {
                            Log.e(LOG_TAG, err.getMessage());
                        }

                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(LOG_TAG, "Fail! " + errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e(LOG_TAG, "Fail! " + throwable.getMessage());
                    }
                });
                //background service 更新




            }


        });
    }

    private void BoardAlertDialog() {
        final String[] ListStr = {"Gossip","Hate", "Sex", "Joke", "LOL"};
        AlertDialog.Builder MyListAlertDialog = new AlertDialog.Builder(this);
        MyListAlertDialog.setTitle("選擇你要的板");
        // 建立List的事件
        DialogInterface.OnClickListener ListClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, ListStr[which],// 顯示所點選的選項
                        Toast.LENGTH_LONG).show();
                Board = ListStr[which];
                Log.i(LOG_TAG,Board);
            }
        };
        // 建立按下取消什麼事情都不做的事件
        DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        MyListAlertDialog.setItems(ListStr, ListClick);
        MyListAlertDialog.setNeutralButton("取消", OkClick);
        MyListAlertDialog.show();
    }

    public void PeriodAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set the Period less than 100");
        final EditText inputperiod = new EditText(this);
        inputperiod.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(inputperiod);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Period = inputperiod.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {
            @Override
                    public  void onClick(DialogInterface d,int which)
            {
                d.cancel();
            }
        });
        builder.show();
    }
}