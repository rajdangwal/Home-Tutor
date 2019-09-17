package com.example.majorproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.majorproject.StudentMainActivity.MailTask;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TutorMainActivity extends Activity implements OnClickListener {

	EditText ETUsername, ETPassword;
	Button BLogIn;
	TextView TVRegisterLink, TVForgotPWLink;
	String forgemail, forgpass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reglogtut);

		ETUsername = (EditText) findViewById(R.id.etTutUsername);
		ETPassword = (EditText) findViewById(R.id.etTutPassword);

		BLogIn = (Button) findViewById(R.id.btnTutLogIn);
		BLogIn.setOnClickListener(this);

		TVRegisterLink = (TextView) findViewById(R.id.tvTutRegister);
		TVForgotPWLink = (TextView) findViewById(R.id.tvTutForgotPass);

		TVRegisterLink.setOnClickListener(this);
		TVForgotPWLink.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent in;

		switch (v.getId()) {
		case R.id.tvTutRegister:
			in = new Intent(this, RegisterTutorActivity.class);
			startActivity(in);

			break;

		case R.id.tvTutForgotPass: {
			if (TextUtils.isEmpty(ETUsername.getText().toString()))
				ETUsername.setError("Enter email first.");
			else {
				Tutor t = new Tutor();
				t.setEmail(ETUsername.getText().toString());

				Gson g = new Gson();
				String Tutorjson = g.toJson(t);
				String url = WebHelper.baseUrl + "/ForgotTutorServlet";

				ForgotTask task = new ForgotTask();
				task.execute(url, Tutorjson);
			}
		}

			break;

		case R.id.btnTutLogIn: {
			String email = ETUsername.getText().toString();
			String password = ETPassword.getText().toString();
			if (TextUtils.isEmpty(email))
				ETUsername.setError("Username cannot be empty.");
			else if (TextUtils.isEmpty(password))
				ETPassword.setError("Password cannot be empty.");
			else {
				Tutor t = new Tutor();
				t.setEmail(email);
				t.setPassword(password);

				Gson g = new Gson();
				String tutorjson = g.toJson(t);
				String url = WebHelper.baseUrl + "/LoginTutorServlet";

				LoginTask task = new LoginTask();
				task.execute(url, tutorjson);
			}
		}

			break;
		}

	}

	class LoginTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			String url = params[0];
			String json = params[1];
			// send using POST type request

			HttpPost postReq = new HttpPost(url);
			// add parameter to postrequest
			BasicNameValuePair pair1 = new BasicNameValuePair("tutordata", json);

			ArrayList<BasicNameValuePair> listPairs = new ArrayList<BasicNameValuePair>();

			listPairs.add(pair1);
			String result = "";
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listPairs);
				// attach entity with request
				postReq.setEntity(entity);

				// send req to server
				HttpClient client = new DefaultHttpClient();
				HttpResponse resp = client.execute(postReq);
				InputStream in = resp.getEntity().getContent();
				InputStreamReader reader = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(reader);

				while (true) {
					String str = br.readLine();
					if (str == null)
						break;
					result = result + str;
				}

				br.close();

			} catch (Exception ex) {
			}

			return result;
		}// eof doInBack

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.e("login result", result);
				JSONObject obj = new JSONObject(result);

				int res = obj.getInt("result");
				if (res == 1) // save logged in user in shared preference
				{
					JSONObject objStudent = obj.getJSONObject("data");

					// create sharedpref file
					SharedPreferences sp = getSharedPreferences("settingstut", MODE_PRIVATE);

					// open sharedpref file for editing
					SharedPreferences.Editor editor = sp.edit();
					// add users data to it
					editor.putInt("tutor_id", objStudent.getInt("tutorid"));
					editor.putString("name", objStudent.getString("name"));
					editor.putString("email", objStudent.getString("email"));

					editor.commit();

					Intent in = new Intent(TutorMainActivity.this, TutorSpaceActivity.class);
					ETPassword.setText("");
					startActivity(in);
					
				} else {
					Toast.makeText(TutorMainActivity.this, "Invalid Username Or Password.", 6).show();
					ETUsername.setText("");
					ETPassword.setText("");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

	class ForgotTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			String url = params[0];
			String json = params[1];
			// send using POST type request

			HttpPost postReq = new HttpPost(url);
			// add parameter to postrequest
			BasicNameValuePair pair1 = new BasicNameValuePair("tutordata", json);

			ArrayList<BasicNameValuePair> listPairs = new ArrayList<BasicNameValuePair>();

			listPairs.add(pair1);
			String result = "";
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listPairs);
				// attach entity with request
				postReq.setEntity(entity);

				// send req to server
				HttpClient client = new DefaultHttpClient();
				HttpResponse resp = client.execute(postReq);
				InputStream in = resp.getEntity().getContent();
				InputStreamReader reader = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(reader);

				while (true) {
					String str = br.readLine();
					if (str == null)
						break;
					result = result + str;
				}

				br.close();

			} catch (Exception ex) {
			}

			return result;
		}// eof doInBack

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.e("forgot result", result);
				JSONObject obj = new JSONObject(result);

				int res = obj.getInt("result");
				if (res == 1) {
					JSONObject objTutor = obj.getJSONObject("data");

					forgpass = objTutor.getString("password");
					forgemail = objTutor.getString("email");

					new MailTask().execute();

				} else {
					Toast.makeText(TutorMainActivity.this, "Invalid Email.", 6).show();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	class MailTask extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(TutorMainActivity.this);
			pd.setMessage("Wait....");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String to = forgemail;
			String subject = "Home Tutor Password";
			String message = "Your account password is " + forgpass;

			GMailSender mailsender = new GMailSender("hometutorssagar@gmail.com", "hometutor");
			try {
				mailsender.sendMail(subject, message, "hometutorssagar@gmail.com", to);
				return "Password sent to email-id.";

			} catch (Exception e) {
				return "Connection Error";
			}

		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(TutorMainActivity.this, result, Toast.LENGTH_LONG).show();
			pd.cancel();
		}
	}
}
