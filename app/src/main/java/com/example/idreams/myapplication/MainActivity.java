package com.example.idreams.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.*;


public class MainActivity extends AppCompatActivity {
    final static String LOG_TAG = "MainActivity";
    String Board = "lol";
    String Period = "10";
    String Limit = "30";
    private JSONObject j;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get串接傳輸按鈕與Post串接傳輸按鈕
        Button mBoard = (Button) findViewById(R.id.board);
        Button mPost = (Button) findViewById(R.id.post);
        Button mPeriod = (Button) findViewById(R.id.Periodbutton);
        final EditText mLimit = (EditText) findViewById(R.id.limitedittext);
        output = (TextView) findViewById(R.id.textView);
        updatetextview();
        mLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLimit.setInputType(InputType.TYPE_CLASS_NUMBER);
                Limit = mLimit.getText().toString();
                updatetextview();
            }
        });

        mPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                params.put("limit", Limit);
                params.put("board", Board);
                params.put("token", "api_doc_token");

                myclient.post("http://api.ser.ideas.iii.org.tw:80/api/top_article/ptt", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // If the response is JSONObject instead of expected JSONArray
                            //response.getJSONArray("result").getJSONObject(0)
                            createshowlistactivity(response);
                        } catch (Exception err) {
                            Log.e(LOG_TAG, err.getMessage());
                        }

                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Fail to connect")
                                .setMessage(errorResponse.toString()).show();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // 为ActionBar扩展菜单项
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void BoardAlertDialog() {
        final String[] ListStr = {"Gossip", "Hate", "Sex", "Joke", "LOL"};
        AlertDialog.Builder MyListAlertDialog = new AlertDialog.Builder(this);
        MyListAlertDialog.setTitle("選擇你要的板");
        // 建立List的事件
        DialogInterface.OnClickListener ListClick = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, ListStr[which],// 顯示所點選的選項
                        Toast.LENGTH_LONG).show();
                Board = ListStr[which];
                Log.i(LOG_TAG, Board);
                updatetextview();
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

    private void PeriodAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set the Period less than 100");
        final EditText inputperiod = new EditText(this);
        inputperiod.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(inputperiod);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Integer.valueOf(inputperiod.getText().toString()) < 100
                        && Integer.valueOf(inputperiod.getText().toString()) > 0
                        ) {
                    Period = inputperiod.getText().toString();
                    updatetextview();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Alert")
                            .setMessage("Period should >0 && <100 && int").show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                d.cancel();
            }
        });
        builder.show();
    }

    private void createshowlistactivity(JSONObject j) {
        this.j = j;
        Intent intent = new Intent(this, Showlist.class);
        Bundle bundle = new Bundle();
        try {
            int t = j.getInt("total");
            int p = j.getInt("period");
            String m = j.getString("message");
            String[] titles = new String[Integer.valueOf(Limit)];
            for (int i = 0; i < Integer.valueOf(Limit); i++) {
                titles[i] = j.getJSONArray("result").getJSONObject(i).getString("title");
            }
            String[] urls = new String[Integer.valueOf(Limit)];
            for (int i = 0; i < Integer.valueOf(Limit); i++) {
                urls[i] = j.getJSONArray("result").getJSONObject(i).getString("url");
            }
            Log.i(LOG_TAG, "total" + t);
            Log.i(LOG_TAG, "peroid" + p);
            Log.i(LOG_TAG, "message" + m);

            bundle.putInt("total", t);
            bundle.putInt("period", p);
            bundle.putString("message", m);
            bundle.putStringArray("titles", titles);
            bundle.putStringArray("urls", urls);
            intent.putExtras(bundle);
            startActivity(intent);

        } catch (JSONException e) {
            Log.i(LOG_TAG, "create err" + e.getMessage());
        }
    }

    public void updatetextview() {
        output.setText("Board : " + Board + "\nLimit :" + Limit + "\nPeriod : " + Period);
    }
}