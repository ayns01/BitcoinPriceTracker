package com.example.ayana.bitcoinpricetracker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    TextView mPriceTextView;
    TextView mWeekPriceTextView;
    TextView mMonthlyPriceTextView;
    TextView mDailyPriceTextView;
    TextView mHourlyPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);
        mWeekPriceTextView = findViewById(R.id.weekly_btc);
        mMonthlyPriceTextView = findViewById(R.id.monthly_btc);
        mDailyPriceTextView = findViewById(R.id.daily_btc);
        mHourlyPriceTextView = findViewById(R.id.hourly_btc);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //ユーザーの選択への応答
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Bitcoin", ""+adapterView.getItemAtPosition(i));
                String finalUrl = BASE_URL + adapterView.getItemAtPosition(i);
                letsDoNetworking(finalUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin","Nothing selected");

            }
        });
    }

    private void letsDoNetworking(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("Bitcoin", "JSON" + response.toString());
                try {
                    String price = response.getString("last");
                    mPriceTextView.setText(price);

//                    JSONObject changes = response.getJSONObject("changes");
//                    JSONArray priceArray = (JSONArray) changes.get("price");
//                    Log.d("Bitcoin", "priceArray" + priceArray.length());
//                    for (int i = 0; i < priceArray.length(); i++) {
//                        JSONObject childObject = priceArray.getJSONObject(i);
//                        String week = childObject.getString("week");
//                        mWeekPriceTextView.setText(week);
//                        Log.d("Bitcoin", "week" + week);
//                    }

                    String week = response.getJSONObject("changes").getJSONObject("price").getString("week"); //JSONObjectでで親keyのJSON取得して、その中の子keyの値を取得。
                    mWeekPriceTextView.setText(week);
                    Log.d("Bitcoin", "week" + week);

                    String month = response.getJSONObject("changes").getJSONObject("price").getString("month"); //JSONObjectでで親keyのJSON取得して、その中の子keyの値を取得。
                    mMonthlyPriceTextView.setText(month);
                    Log.d("Bitcoin", "month" + month);

                    String day = response.getJSONObject("changes").getJSONObject("price").getString("day"); //JSONObjectでで親keyのJSON取得して、その中の子keyの値を取得。
                    mDailyPriceTextView.setText(day);
                    Log.d("Bitcoin", "day" + day);

                    String hour = response.getJSONObject("changes").getJSONObject("price").getString("hour"); //JSONObjectでで親keyのJSON取得して、その中の子keyの値を取得。
                    mHourlyPriceTextView.setText(hour);
                    Log.d("Bitcoin", "hour" + hour);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });
    }
}
