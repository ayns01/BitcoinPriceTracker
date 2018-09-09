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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private final String BASE_URL_ETH = "https://apiv2.bitcoinaverage.com/indices/global/ticker/ETH";
    private final String BASE_URL_XRP = "https://apiv2.bitcoinaverage.com/indices/global/ticker/XRP";
    private final String BASE_URL_BCH = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BCH";


    TextView mPriceTextView;
    TextView mWeekPriceTextView;
    TextView mMonthlyPriceTextView;
    TextView mDailyPriceTextView;
    TextView mBtcHourlyPriceTextView;
    TextView mEthHourlyPriceTextView;
    TextView mXrpHourlyPriceTextView;
    TextView mBchHourlyPriceTextView;
    TextView mBtcPriceView;
    TextView mEthPriceView;
    TextView mXrpPriceView;
    TextView mBchPriceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);
        mBtcHourlyPriceTextView = findViewById(R.id.btc_hour_price);
        mEthHourlyPriceTextView = findViewById(R.id.eth_hour_price);
        mXrpHourlyPriceTextView = findViewById(R.id.xrp_hour_price);
        mBchHourlyPriceTextView = findViewById(R.id.bch_hour_price);
        mBtcPriceView = findViewById(R.id.btc_price);
        mEthPriceView = findViewById(R.id.eth_price);
        mXrpPriceView = findViewById(R.id.xrp_price);
        mBchPriceView = findViewById(R.id.bch_price);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // レイアウトからリストビューを取得
        ListView listView = findViewById(R.id.listview);

        //リストビューに表示する要素の設定
        ArrayList<ListItem> listItems = new ArrayList<>();

        Bitmap bmpBtc = BitmapFactory.decodeResource(getResources(), R.drawable.btc);
        Bitmap bmpEth = BitmapFactory.decodeResource(getResources(), R.drawable.eth);
        Bitmap bmpXrp = BitmapFactory.decodeResource(getResources(), R.drawable.xrp);
        Bitmap bmpBch = BitmapFactory.decodeResource(getResources(), R.drawable.bch);

        listItems.add(new ListItem(bmpBtc, "BTC", "Bitcoin"));
        listItems.add(new ListItem(bmpEth, "ETH", "Ethereum"));
        listItems.add(new ListItem(bmpXrp, "XRP", "Ripple"));
        listItems.add(new ListItem(bmpBch, "BCH","Bitcoin Cash"));

        // 出力結果をビューに表示
        ListAdapter adapter1 = new ListAdapter(this, R.layout.brandlist_item, listItems);
        listView.setAdapter(adapter1);

        //ユーザーのスピナーからの選択への応答
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Bitcoin", ""+adapterView.getItemAtPosition(i));
                String country = (String) adapterView.getItemAtPosition(i);
                String finalUrl = BASE_URL + adapterView.getItemAtPosition(i);
                String finalUrlEth = BASE_URL_ETH + adapterView.getItemAtPosition(i);
                String finalUrlXrp = BASE_URL_XRP + adapterView.getItemAtPosition(i);
                String finalUrlBch = BASE_URL_BCH + adapterView.getItemAtPosition(i);
//                doNetworking(country, finalUrl, finalUrlEth, finalUrlXrp, finalUrlBch);
                newDoNetworking("Bitcoin", country, finalUrl, mBtcPriceView, mBtcHourlyPriceTextView);
                newDoNetworking("Eth", country, finalUrlEth, mEthPriceView, mEthHourlyPriceTextView);
                newDoNetworking("Xrp", country, finalUrlXrp, mXrpPriceView, mXrpHourlyPriceTextView);
                newDoNetworking("Bch", country, finalUrlBch, mBchPriceView, mBchHourlyPriceTextView);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin","Nothing selected");
            }
        });
    }

    private void newDoNetworking(final String tag, final String country, String url, final TextView priceView, final TextView hourlyPriceTextView){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d(tag, "JSON" + response.toString());
                try {
                    String price = response.getString("last");
                    //mPriceTextView.setText(price);
                    priceView.setText(price + " " + country);
                    String hour = response.getJSONObject("changes").getJSONObject("price").getString("hour");
                    hourlyPriceTextView.setText(hour);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(tag, "Request fail! Status code: " + statusCode);
                Log.e("ERROR", e.toString());
            }
        });
    }

//    private void doNetworking(final String country, String urlBtc, String urlEth, String urlXrp, String urlBch) {
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        client.get(urlBtc, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // If the response is JSONObject instead of expected JSONArray
//                Log.d("Bitcoin", "JSON" + response.toString());
//                try {
//                    String price = response.getString("last");
//                    //mPriceTextView.setText(price);
//                    mBtcPriceView.setText(price + " " + country);
//                    String hour = response.getJSONObject("changes").getJSONObject("price").getString("hour");
//                    mBtcHourlyPriceTextView.setText(hour);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
//                Log.e("ERROR", e.toString());
//            }
//        });
//
//        client.get(urlEth, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("Bitcoin", "JSON ETH" + response.toString());
//                try {
//                    String price = response.getString("last");
//                    mEthPriceView.setText(price + " " + country);
//                    String hour = response.getJSONObject("changes").getJSONObject("price").getString("hour");
//                    mEthHourlyPriceTextView.setText(hour);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                Log.e("ERROR", e.toString());
//            }
//        });
//
//        client.get(urlXrp, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("Bitcoin", "JSON XRP" + response.toString());
//                try {
//                    String price = response.getString("last");
//                    mXrpPriceView.setText(price + " " + country);
//                    String hour = response.getJSONObject("changes").getJSONObject("price").getString("hour");
//                    mXrpHourlyPriceTextView.setText(hour);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                Log.e("ERROR", e.toString());
//            }
//        });
//
//        client.get(urlBch, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d("Bitcoin", "JSON BCH" + response.toString());
//                try {
//                    String price = response.getString("last");
//                    mBchPriceView.setText(price + " " + country);
//                    String hour = response.getJSONObject("changes").getJSONObject("price").getString("hour");
//                    mBchHourlyPriceTextView.setText(hour);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                Log.e("ERROR", e.toString());
//            }
//        });
//    }
}
