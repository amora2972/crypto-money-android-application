package com.example.cryptomoney.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.cryptomoney.MySingelton;
import com.example.cryptomoney.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

	private ProfileViewModel profileViewModel;

	public LoginActivity token = new LoginActivity();
	String user_token = token.getUser_token();

	TextView user_name_profile;
	TextView email_profile;
	TextView join_date_profile;
	TextView profile_user_wallet;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		profileViewModel =
				ViewModelProviders.of(this).get(ProfileViewModel.class);
		View root = inflater.inflate(R.layout.fragment_profile, container, false);
//		final TextView textView = root.findViewById(R.id.text_home);
//		profileViewModel.getText().observe(this, new Observer<String>() {
//			@Override
//			public void onChanged(@Nullable String s) {
//				textView.setText(s);
//			}
//		});

		user_name_profile 	= root.findViewById(R.id.profile_user_name);
		email_profile 		= root.findViewById(R.id.email_profile);
		join_date_profile 	= root.findViewById(R.id.profile_join_date);
		profile_user_wallet = root.findViewById(R.id.profile_user_wallet);

		String url = "http://10.0.2.2:8000/api/user";
		Map<String, String> params = new HashMap();

		JSONObject parameters = new JSONObject(params);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
				(Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							if (response.getString("success").equals("true")) {
								JSONObject user_data = response.getJSONObject("result").getJSONObject("data");

								String name = user_data.getString("name");
								String email = user_data.getString("email");
								String wallet = user_data.getString("wallet");
								String join_date = user_data.getString("created_at");

								user_name_profile.setText(name);
								email_profile.setText(email);
								join_date_profile.setText(join_date);
								profile_user_wallet.setText(wallet);

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
							Log.d("this is exception error", e.getMessage());
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("this is response error: ", error.getMessage());
						error.printStackTrace();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<>();

				headers.put("Accept", "application/json");


				Log.d("this is token", user_token);

				headers.put("Authorization", ("Bearer " + user_token));

				return headers;
			}
		};
		MySingelton.getInstance(getContext()).addToRequsetQueue(jsonObjectRequest);



		Button logout_button = root.findViewById(R.id.logout_button);

		logout_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String url = "http://10.0.2.2:8000/api/logout";
				Map<String, String> params = new HashMap();

				JSONObject parameters = new JSONObject(params);
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
						(Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {

								try {
									if (response.getString("success").equals("true")) {
										String message = response.getString("message");
										Intent intent = new Intent(getContext(), LoginActivity.class);

										// kill the login and register activities.
										intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
										// start the new activity
										startActivity(intent);

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
									Log.d("this is exception error", e.getMessage());
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Log.d("this is response error: ", error.getMessage());
								error.printStackTrace();
							}
						}) {
					@Override
					public Map<String, String> getHeaders() throws AuthFailureError {
						HashMap<String, String> headers = new HashMap<>();

						headers.put("Accept", "application/json");


						Log.d("this is token", user_token);

						headers.put("Authorization", ("Bearer " + user_token));

						return headers;
					}
				};
				MySingelton.getInstance(getContext()).addToRequsetQueue(jsonObjectRequest);
			}
		});

		return root;
	}
}