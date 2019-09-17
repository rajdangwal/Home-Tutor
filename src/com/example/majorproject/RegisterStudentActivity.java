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

import com.example.majorproject.RegisterTutorActivity.RegisterTask;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterStudentActivity extends Activity implements OnClickListener {

	EditText ETStuName, ETStuEmail, ETStuPhone, ETStuAddress, ETStuInstitution, ETStuPassword, ETStuRePassword;

	Spinner SPArea, SPStandard;
	RadioGroup rgGender;
	Button BSubmit;

	String name, email, phone, address, institution, area, gender, password, repassword;
	int standard;

	static String regexPhone = "^[0-9]{10,10}$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regstu);

		ETStuName = (EditText) findViewById(R.id.etStuNameReg);
		ETStuEmail = (EditText) findViewById(R.id.etStuEmailReg);
		ETStuPhone = (EditText) findViewById(R.id.etStuPhoneReg);
		ETStuAddress = (EditText) findViewById(R.id.etStuAddressReg);
		ETStuInstitution = (EditText) findViewById(R.id.etStuInstitutionReg);
		ETStuPassword = (EditText) findViewById(R.id.etStuPasswordReg);
		ETStuRePassword = (EditText) findViewById(R.id.etStuRePasswordReg);
		rgGender = (RadioGroup) findViewById(R.id.rgGenderStu);

		SPArea = (Spinner) findViewById(R.id.spArea);
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.area, R.layout.spinnerlayout);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SPArea.setAdapter(adapter1);

		SPStandard = (Spinner) findViewById(R.id.spStandard);
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.standard, R.layout.spinnerlayout);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SPStandard.setAdapter(adapter2);

		BSubmit = (Button) findViewById(R.id.btnStuSubmitReg);

		BSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.btnStuSubmitReg) {
			String url = WebHelper.baseUrl + "/RegisterStudentServlet";

			name = ETStuName.getText().toString().trim();
			email = ETStuEmail.getText().toString().trim();
			phone = ETStuPhone.getText().toString().trim();
			address = ETStuAddress.getText().toString().trim();
			institution = ETStuInstitution.getText().toString().trim();
			area = SPArea.getSelectedItem().toString();
			gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();

			standard = Integer.parseInt(SPStandard.getSelectedItem().toString());
			password = ETStuPassword.getText().toString();
			repassword = ETStuRePassword.getText().toString();

			if (validate()) {

				if (!repassword.equals(password)) {
					Toast.makeText(RegisterStudentActivity.this, "Password doesn't match.", 6).show();

				} else if (!validateEmail()) {
					Toast.makeText(RegisterStudentActivity.this, "Invalid Email!!!.", 6).show();
				} else if (!validatePhone()) {
					Toast.makeText(RegisterStudentActivity.this, "Invalid Phone No.!!!.", 6).show();
				} else {
					Student s = new Student(0, name, email, phone, address, institution, area, gender, standard,
							password);

					// convert data of user in json
					Gson g = new Gson();
					String StudentJson = g.toJson(s);
					// send user json to server using asynctask

					RegisterTask task = new RegisterTask();
					task.execute(url, StudentJson);

				}
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
			ETStuName.setError("Name field cannot be empty.");
			return false;
		}

		else if (TextUtils.isEmpty(email)) {
			ETStuEmail.setError("Email field cannot be empty.");
			return false;
		}

		else if (TextUtils.isEmpty(phone)) {
			ETStuPhone.setError("Phone field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(address)) {
			ETStuAddress.setError("Address field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(institution)) {
			ETStuInstitution.setError("School field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(password)) {
			ETStuPassword.setError("Password field cannot be empty.");
			return false;
		} else if (TextUtils.isEmpty(repassword)) {
			ETStuRePassword.setError("Please enter the same password again.");
			return false;
		}
		return true;

	}

	class RegisterTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			String url = params[0];
			String json = params[1];
			// send using POST type request

			HttpPost postReq = new HttpPost(url);
			// add parameter to postrequest
			BasicNameValuePair pair1 = new BasicNameValuePair("studentdata", json);

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
					Toast.makeText(RegisterStudentActivity.this, "Registration Done", 6).show();
					finish();
				} else {
					Toast.makeText(RegisterStudentActivity.this, "Registration Failed. Try Again.", 6).show();
				}
			} catch (Exception ex) {

			}

		}

	}
}
