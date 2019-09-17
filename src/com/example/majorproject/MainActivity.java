package com.example.majorproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button BTutor, BStudent, BHelp, BContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		BTutor = (Button) findViewById(R.id.btnTutor);
		BStudent = (Button) findViewById(R.id.btnStudent);
		BHelp = (Button) findViewById(R.id.btnHelp);
		BContact = (Button) findViewById(R.id.btnContact);

		BTutor.setOnClickListener(this);
		BStudent.setOnClickListener(this);
		BHelp.setOnClickListener(this);
		BContact.setOnClickListener(this);
	}

	
	@Override
public void onBackPressed()
{
		backButtonHandler();
}
	
	private void backButtonHandler() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		alertDialog.setTitle("Leave Application?");
		alertDialog.setMessage("Are you sure you want to leave the application?");
		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				finish();
			}


		});
alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.cancel();
			}


		});
		alertDialog.show();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent in;

		switch (v.getId()) {
		case R.id.btnTutor:
			in = new Intent(this, TutorMainActivity.class);
			startActivity(in);

			break;

		case R.id.btnStudent:
			in = new Intent(this, StudentMainActivity.class);
			startActivity(in);
			break;

		case R.id.btnHelp: {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("Help");
			builder.setMessage("Tutor\nBy registering as a tutor you will be appyling to work under us to give"
					+ " tutions as per the requirements."
					+ "\nAfter you successfully register as a tutor you have to come to our center for an interview"
					+ " after which you will be able to teach."
					+ "\nYour salary depends on the no. of students you teach."
					+ "\n\nStudent\nBy registering as a student you will be able to select a home tutor as per your "
					+ "requirements.\nFees per subject is Rs.4000.\nYou can take 3 days demo class but have to pay"
					+ " Rs.100 for each day (Fuel charge Only) to the tutor. If you continue after the demo classes"
					+ " the money you have paid will be considered in fees, otherwise the money is non refundable.");

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
				}
			});

			AlertDialog d = builder.create();
			d.show();
			d.getWindow().setLayout(350, 450);

		}
			break;

		case R.id.btnContact:
			in = new Intent(this, ContactUs.class);
			startActivity(in);

			break;
		}

	}

}
