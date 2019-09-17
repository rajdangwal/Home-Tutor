package com.example.majorproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TutorSpaceTwoActivity extends Activity implements OnClickListener{

	TextView TVStudentName,TVStudentEmail,TVStudentPhone,TVStudentAddress,TVStudentStandard;
	Button BBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutspacetwo);
		
		TVStudentName = (TextView) findViewById(R.id.tvStudentNameDet);
		TVStudentEmail = (TextView) findViewById(R.id.tvStudentEmailDet);
		TVStudentPhone = (TextView) findViewById(R.id.tvStudentPhoneDet);
		TVStudentAddress = (TextView) findViewById(R.id.tvStudentAddressDet);
		TVStudentStandard = (TextView) findViewById(R.id.tvStudentStandardDet);
		
		
		BBack = (Button) findViewById(R.id.btnBack);
		BBack.setOnClickListener(this);
		
		Intent in = getIntent();
		Student s= (Student)in.getSerializableExtra("student");
		
		
		TVStudentName.setText(s.getName());
		TVStudentEmail.setText(s.getEmail());
		TVStudentPhone.setText(s.getPhone());
		TVStudentAddress.setText(s.getAddress());
		TVStudentStandard.setText(s.getStandard()+"");
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		finish();
	}
	
}
