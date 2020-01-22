package com.example.cryptomoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

	Button register_button;
	EditText email, password;
	public TextView errors;

	static String user_token = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// to hide the title bar
		try {
			this.getSupportActionBar().hide();
		} catch (NullPointerException e) {

		}

		setContentView(R.layout.activity_login);
		register_button = findViewById(R.id.register_button);

		register_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});

		email = findViewById(R.id.email_input);
		password = findViewById(R.id.password_input);
		errors = findViewById(R.id.error);
	}

	public static String getUser_token(){
		return user_token;
	}

	public void login_button(View view) {
		if (password.getText().toString().isEmpty())
			errors.setText("password can't be left empty");
		else if (!isValidEmailAddress(email.getText().toString()) && !email.getText().toString().isEmpty())
			errors.setText("Email can't be left empty");

		else if (password.getText().toString().length() < 8)
			errors.setText("password must be at least 8 characters");
		else {
			String url = "http://10.0.2.2:8000/api/login";
			Map<String, String> params = new HashMap();
			params.put("password", password.getText().toString());
			params.put("email", email.getText().toString());

			JSONObject parameters = new JSONObject(params);


			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
					(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {

							try {

								if (response.getString("success").equals("true"))
								{
									// get the user token of the current user and assign it to the static variable "user_token"
									user_token = response.getJSONObject("result").getJSONObject("data").getString("token");

									Intent intent = new Intent(getApplicationContext(), MainActivity.class);

									// start the new activity
									startActivity(intent);
									finish();

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
										errors.setText(error_message);
									}
								}

							} catch (JSONException e) {
								errors.setText(e.getMessage());
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							error.printStackTrace();
						}
					});

			// Access the RequestQueue through your singleton class.
			MySingelton.getInstance(LoginActivity.this).addToRequsetQueue(jsonObjectRequest);
		}
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}
}
