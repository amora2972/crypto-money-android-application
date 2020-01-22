package com.example.cryptomoney.ui.currencies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cryptomoney.LoginActivity;
import com.example.cryptomoney.MySingelton;
import com.example.cryptomoney.R;
import com.example.cryptomoney.ui.home.Currency;
import com.example.cryptomoney.ui.home.CurrencyAdapter;
import com.example.cryptomoney.ui.transactions.AddCurrency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrenciesFragment extends Fragment {

    private CurrenciesViewModel currenciesViewModel;
    private ListView listView;
    JSONArray jsonArray;
    JSONObject jsonObject;
    CurrencyAdapter currencyAdapter;
    Currency currency;
    LoginActivity loginActivity = new LoginActivity();
    String user_token = loginActivity.getUser_token();
    Button add_currency_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_currencies, container, false);
        listView = root.findViewById(R.id.listview);
        this.getCurrenciesInfo();
        add_currency_button = root.findViewById(R.id.add_currency_button);

        add_currency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddCurrency.class);
                startActivity(intent);
            }
        });

        return root;
    }

    public void getCurrenciesInfo() {
        String url = "http://10.0.2.2:8000/api/currency";
        Map<String, String> params = new HashMap();

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("success").equals("true")) {
                                jsonArray = response.getJSONObject("result").getJSONArray("data");
                                currencyAdapter = new CurrencyAdapter(getContext(), R.layout.row_layout);
                                listView.setAdapter(currencyAdapter);

                                try {
                                    int count = 0;
                                    String id, symbol, name;
                                    String[] titles = new String[]{
                                            "id",
                                            "symbol",
                                            "name"
                                    };

                                    id = titles[0];
                                    symbol = titles[1];
                                    name = titles[2];

                                    Currency currency = new Currency(id, symbol, name);
                                    currencyAdapter.add(currency);

                                    while (count < jsonArray.length()) {
                                        while (count < jsonArray.length()) {
                                            JSONObject JO = jsonArray.getJSONObject(count);
                                            name = JO.getString("name");
                                            id = JO.getString("id");
                                            symbol = JO.getString("symbol");

                                            currency = new Currency(name, id, symbol);
                                            currencyAdapter.add(currency);
                                            count++;
                                        }
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