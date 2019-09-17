package com.example.majorproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactUs extends Activity implements OnClickListener {

	Button BSend;
	EditText ETEmail, ETMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);

		BSend = (Button) findViewById(R.id.btSendMsg);
		ETEmail = (EditText) findViewById(R.id.etYourEmail);
		ETMessage = (EditText) findViewById(R.id.etMessage);

		BSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (validateEmail()) {

			if (TextUtils.isEmpty(ETMessage.getText().toString()))
				ETMessage.setError("This field cannot be empty.");
			else
				new MailTask().execute();
		} else
			Toast.makeText(ContactUs.this, "Invalid Email!!!.", 6).show();
	}

	private boolean validateEmail() {
		// TODO Auto-generated method stub
		return android.util.Patterns.EMAIL_ADDRESS.matcher(ETEmail.getText().toString()).matches();

	}

	class MailTask extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ContactUs.this);
			pd.setMessage("Wait....");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String to = "hometutorssagar@gmail.com";
			String subject = ETEmail.getText().toString();
			String message = ETMessage.getText().toString();

			GMailSender mailsender = new GMailSender("hometutorssagar@gmail.com", "hometutor");
			try {
				mailsender.sendMail(subject, message, "hometutorssagar@gmail.com", to);
				return "Message Sent";

			} catch (Exception e) {
				return e.toString();
			}

		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(ContactUs.this, result, Toast.LENGTH_LONG).show();
			pd.cancel();
			finish();
		}
	}
}
