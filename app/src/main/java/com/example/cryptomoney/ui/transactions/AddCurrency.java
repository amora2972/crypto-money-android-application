package com.example.cryptomoney.ui.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cryptomoney.LoginActivity;
import com.example.cryptomoney.MainActivity;
import com.example.cryptomoney.MySingelton;
import com.example.cryptomoney.R;
import com.example.cryptomoney.ui.currencies.CurrenciesFragment;
import com.example.cryptomoney.ui.home.Transaction;
import com.example.cryptomoney.ui.home.TransactionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCurrency extends AppCompatActivity {

    Button add_currency_button;
    EditText full_name;
    EditText name;
    EditText symbol;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TransactionAdapter transactionAdapter;
    View root;
    LoginActivity loginActivity = new LoginActivity();
    String user_token = loginActivity.getUser_token();
    Button add_transaction_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);
        add_currency_button = findViewById(R.id.add_currency_button);
        full_name = findViewById(R.id.full_name);
        name = findViewById(R.id.name);
        symbol = findViewById(R.id.symbol);

        add_currency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:8000/api/currency";
                Map<String, String> params = new HashMap();
                params.put("full_name", full_name.getText().toString());
                params.put("name", name.getText().toString());
                params.put("symbol", symbol.getText().toString());

                JSONObject parameters = new JSONObject(params);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("success").equals("true")) {

                                        try {
                                            Log.d("success", "true");
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            Log.d("ss",e.getMessage());
                                            e.printStackTrace();
                                        }


                                    } else {
                                        Log.d("response from error", response.toString());
                                        JSONObject result = response.getJSONObject("result");
                                        Log.d("error", result.toString());
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
                MySingelton.getInstance(getApplicationContext()).addToRequsetQueue(jsonObjectRequest);
            }
        });
    }
}
