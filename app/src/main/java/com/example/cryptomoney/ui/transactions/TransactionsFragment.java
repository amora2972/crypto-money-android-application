package com.example.cryptomoney.ui.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cryptomoney.LoginActivity;
import com.example.cryptomoney.MySingelton;
import com.example.cryptomoney.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.cryptomoney.ui.home.Transaction;
import com.example.cryptomoney.ui.home.TransactionAdapter;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel transactionsViewModel;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TransactionAdapter transactionAdapter;
    ListView listView;
    View root;
    LoginActivity loginActivity = new LoginActivity();
    String user_token = loginActivity.getUser_token();
    Button add_transaction_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionsViewModel =
                ViewModelProviders.of(this).get(TransactionsViewModel.class);
        root = inflater.inflate(R.layout.fragment_transactions, container, false);
        listView = root.findViewById(R.id.listview);
        this.getTransactionInfo();

        return root;
    }

    public void getTransactionInfo() {
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
                                listView.setAdapter(transactionAdapter);

                                try {
                                    int count = 0;
                                    String id, buying_rate, total, amount;

                                    String[] titles = new String[]{
                                            getResources().getString(R.string.id_title),
                                            getResources().getString(R.string.buying_rate),
                                            getResources().getString(R.string.amount_title),
                                            getResources().getString(R.string.total_title),
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