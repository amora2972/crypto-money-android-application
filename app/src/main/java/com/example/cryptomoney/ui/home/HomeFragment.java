package com.example.cryptomoney.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cryptomoney.LoginActivity;
import com.example.cryptomoney.MainActivity;
import com.example.cryptomoney.MySingelton;
import com.example.cryptomoney.R;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

  private HomeViewModel homeViewModel;
  public TextView debugger;
  JSONObject jsonObject;
  JSONArray jsonArray;
  CurrencyAdapter currencyAdapter;
  TransactionAdapter transactionAdapter;
  ListView listView, listViewTwo;
  View root;
  LoginActivity loginActivity = new LoginActivity();
  String user_token = loginActivity.getUser_token();

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    root = inflater.inflate(R.layout.fragment_home, container, false);
    listView = root.findViewById(R.id.listview);
    listViewTwo = root.findViewById(R.id.listviewTwo);
    this.getCurrenciesInfo();
    this.getLastTransactions();
    return root;
  }

  public void getCurrenciesInfo() {
    currencyAdapter = new CurrencyAdapter(getContext(), R.layout.row_layout);
    listView.setAdapter(currencyAdapter);
    String obj = loadJSONFromAsset(getContext());

    try {
      jsonObject = new JSONObject(obj);
      jsonArray = jsonObject.getJSONArray("currencies");
      int count = 0;
      String id, symbol, name, price;
      while (count < jsonArray.length()) {
        JSONObject JO = jsonArray.getJSONObject(count);
        Random rand = new Random();
        price = String.valueOf(new DecimalFormat("#.##").format(rand.nextDouble() * (6 - 0)));
        JO.put("price", price);
        name = JO.getString("name");

        id = JO.getString("id");
        symbol = JO.getString("symbol");
        price = JO.getString("price");

        Currency currency = new Currency(name, id, symbol, price);
        currencyAdapter.add(currency);
        count++;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public String loadJSONFromAsset(Context context) {
    String json = null;
    try {
      InputStream is = context.getAssets().open("currencies_prices.json");

      int size = is.available();

      byte[] buffer = new byte[size];

      is.read(buffer);

      is.close();

      json = new String(buffer, "UTF-8");


    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;

  }

  public void getLastTransactions() {
    String url = "http://10.0.2.2:8000/api/transaction";
    Map<String, String> params = new HashMap();

    JSONObject parameters = new JSONObject(params);
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {

          @Override
          public void onResponse(JSONObject response) {

            try {
              if (response.getString("success").equals("true")) {
                jsonArray = response.getJSONObject("result").getJSONArray("data");
                transactionAdapter = new TransactionAdapter(getContext(), R.layout.row_layout);
                listViewTwo.setAdapter(transactionAdapter);

                try {
                  int count = 0;
                  String id, buying_rate, total, amount;

                  String[] titles = new String[]{
                      getResources().getString(R.string.id_title),
                      getResources().getString(R.string.buying_rate),
                      getResources().getString(R.string.amount_title),
                      getResources().getString(R.string.total_title)
                  };

                  total = titles[3];
                  id = titles[0];
                  buying_rate = titles[1];
                  amount = titles[2];
                  Transaction transaction = new Transaction(amount, buying_rate, total, id);
                  transactionAdapter.add(transaction);

                  while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    total = JO.getString("total");
                    id = JO.getString("id");
                    buying_rate = JO.getString("buying_rate");
                    amount = JO.getString("amount");
                    transaction = new Transaction(amount, buying_rate, total, id);
                    transactionAdapter.add(transaction);
                    count++;
                  }

                  Log.d("result", "reached the end");
                } catch (JSONException e) {
                  e.printStackTrace();
                }


              } else {
                JSONObject result = response.getJSONObject("result");
                JSONArray names = result.names();

                ArrayList<String> list = new ArrayList<String>();

                if (names != null) {
                  int len = names.length();
                  for (int i = 0; i < len; i++) {
                    list.add(names.get(i).toString());
                  }
                }

                String error_message = "";
                for (String name : list) {
                  error_message += result.getJSONArray(name).opt(0).toString();
                }
              }

            } catch (JSONException e) {
              Log.d("this is the error", e.getMessage());
            }
          }
        }, new Response.ErrorListener() {

          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d("this is the lassssssst", error.getMessage());
            error.printStackTrace();
          }
        }) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();

        headers.put("Accept", "application/json");

        headers.put("Authorization", ("Bearer " + user_token));

        return headers;
      }
    };
    MySingelton.getInstance(getContext()).addToRequsetQueue(jsonObjectRequest);
  }
}