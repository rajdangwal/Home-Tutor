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

import com.google.gson.Gson;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterTutorActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	EditText ETTutName, ETTutEmail, ETTutPhone, ETTutAddress, ETTutQualification, ETTutOccupation, ETTutPassword,
			ETTutRePassword;

	Button BSubmit;

	Spinner SPQual;
	RadioGroup rgGender;

	String name, email, phone, address, qualification, occupation, gender, password, repassword;

	static String regexPhone = "^[0-9]{10,10}$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.regtut);

		ETTutName = (EditText) findViewById(R.id.etTutNameReg);
		ETTutEmail = (EditText) findViewById(R.id.etTutEmailReg);
		ETTutPhone = (EditText) findViewById(R.id.etTutPhoneReg);
		ETTutAddress = (EditText) findViewById(R.id.etTutAddressReg);
		ETTutQualification = (EditText) findViewById(R.id.etTutQualificationReg);
		ETTutOccupation = (EditText) findViewById(R.id.etTutOccupationReg);
		rgGender = (RadioGroup) findViewById(R.id.rgGenderTut);
		ETTutPassword = (EditText) findViewById(R.id.etTutPasswordReg);
		ETTutRePassword = (EditText) findViewById(R.id.etTutRePasswordReg);

		SPQual = (Spinner) findViewById(R.id.spQual);
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.qual, R.layout.spinnerlayout);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SPQual.setAdapter(adapter1);

		BSubmit = (Button) findViewById(R.id.btnTutSubmitReg);

		BSubmit.setOnClickListener(this);
		ETTutQualification.setEnabled(false);
		ETTutQualification.setFocusable(false);
		SPQual.setOnItemSelectedListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnTutSubmitReg) {
			String url = WebHelper.baseUrl + "/RegisterTutorServlet";

			name = ETTutName.getText().toString();
			email = ETTutEmail.getText().toString();
			phone = ETTutPhone.getText().toString();
			address = ETTutAddress.getText().toString();
			occupation = ETTutOccupation.getText().toString();
			qualification = SPQual.getSelectedItem().toString();
			if (qualification.equals("Others")) {
				qualification = ETTutQualification.getText().toString();
			}

			gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();

			password = ETTutPassword.getText().toString();
			repassword = ETTutRePassword.getText().toString();

			if (validate()) {

				if (!repassword.equals(password)) {
					Toast.makeText(RegisterTutorActivity.this, "Password doesn't match.", 6).show();

				} else if (!validateEmail()) {
					Toast.makeText(RegisterTutorActivity.this, "Invalid Email!!!.", 6).show();
				} else if (!validatePhone()) {
					Toast.makeText(RegisterTutorActivity.this, "Invalid Phone No.!!!.", 6).show();
				} else {
					Tutor t = new Tutor(0, name, email, phone, address, qualification, occupation, gender, password,
							"Free", "Free", 0);

					// convert data of user in json
					Gson g = new Gson();
					String TutorJson = g.toJson(t);
					// send user json to server using asynctask

					RegisterTask task = new RegisterTask();
					task.execute(url, TutorJson);

				}
			}
		}

	}

	class RegisterTask extends AsyncTask<String, Void, String> {

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
				Log.e("Error", ex.getMessage());
			}

			return result;
		}// eof doInBack

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONObject obj = new JSONObject(result);
				int res = obj.getInt("result");
				if (res == 1) {
					Toast.makeText(RegisterTutorActivity.this, "Registration Done", 6).show();
					finish();
				} else {
					Toast.makeText(RegisterTutorActivity.this, "Registration Failed. Try Again.", 6).show();
				}
			} catch (Exception ex) {

			}

		}

	}

	private boolean validatePhone() {
		// TODO Auto-generated method stub
		return phone.matches(regexPhone);
	}

	private boolean validateEmail() {
		// TODO Auto-generated method stub
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private boolean validate() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(name)) {
			ETTutName.setError("Name field cannot be empty.");
			return false;
		}

		else if (TextUtils.isEmpty(email)) {
			ETTutEmail.setError("Email field cannot be empty.");
			return false;
		}

		else if (TextUtils.isEmpty(phone)) {
			ETTutPhone.setError("Phone field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(address)) {
			ETTutAddress.setError("Address field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(occupation)) {
			ETTutOccupation.setError("Occupation field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(password)) {
			ETTutPassword.setError("Password field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(repassword)) {
			ETTutRePassword.setError("Please enter the same password again.");
			return false;
		}

		else if (TextUtils.isEmpty(qualification)) {
			ETTutQualification.setError("Please enter your qualification.");
			return false;
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String qual = SPQual.getItemAtPosition(arg2).toString();
		if (qual.equals("Others")) {
			ETTutQualification.setEnabled(true);
			ETTutQualification.setFocusableInTouchMode(true);
			;
		}

		else {
			ETTutQualification.setEnabled(false);
			ETTutQualification.setFocusable(false);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
