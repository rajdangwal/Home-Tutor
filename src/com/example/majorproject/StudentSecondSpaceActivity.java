package com.example.majorproject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StudentSecondSpaceActivity extends Activity implements OnClickListener{

	TextView TVTutorName,TVTutorQual,TVTutorGender,TVTutorOccu,TVTutorPhone,TVTutorEmail,TVTutorStatus;
	Button BHireTutor;
	String subname;
	int timeslot;
	String ts1status, ts2status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentsecondspace);
		
		TVTutorName = (TextView) findViewById(R.id.tvTutorNameDet);
		TVTutorQual = (TextView) findViewById(R.id.tvTutorQualDet);
		TVTutorGender = (TextView) findViewById(R.id.tvTutorGenderDet);
		TVTutorOccu = (TextView) findViewById(R.id.tvTutorOccuDet);
		TVTutorPhone = (TextView) findViewById(R.id.tvTutorPhoneDet);
		TVTutorEmail = (TextView) findViewById(R.id.tvTutorEmailDet);
		TVTutorStatus = (TextView) findViewById(R.id.tvTutorStatusDet);
		
		BHireTutor = (Button) findViewById(R.id.btnHireTutor);
		BHireTutor.setOnClickListener(this);
		
		Intent in = getIntent();
		Tutor t = (Tutor)in.getSerializableExtra("tutor");
		ts1status = t.getTs1status();
		ts2status = t.getTs2status();
		subname = (String) in.getSerializableExtra("subject");
		String ts = (String) in.getSerializableExtra("timeslot");
		timeslot = Integer.parseInt(ts);
		
		TVTutorName.setText(t.getName());
		TVTutorQual.setText(t.getQualification());
		TVTutorGender.setText(t.getGender());
		TVTutorOccu.setText(t.getOccupation());
		TVTutorPhone.setText(t.getPhone());
		TVTutorEmail.setText(t.getEmail());
		
		if(t.getTs1status().equals("Free")||t.getTs2status().equals("Free"))	
		TVTutorStatus.setText("Available");
		
		else
		TVTutorStatus.setText("Not Available");
		
		
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(TVTutorStatus.getText().toString().equals("Not Available"))
			Toast.makeText(this, "Tutor is not available.", Toast.LENGTH_LONG).show();
		else
		{
		SharedPreferences sp=
				getSharedPreferences("settingsstu", MODE_PRIVATE);
			
			String stuemail=sp.getString("email","");
			
			String tutemail = TVTutorEmail.getText().toString();
			
			if(ts1status.equals("Free")&& timeslot!=2)
				{
				timeslot=1;
				ts1status="Assigned";
				}
			else
				{
				timeslot=2;
				ts2status="Assigned";
				}
			
			String url=WebHelper.baseUrl+"AssignTutorServlet?stuemail="+stuemail+"&tutemail="+tutemail+"&timeslot="+timeslot+"&subname="+subname;
			url=url.replaceAll(" ", "%20");
			AssignTutorTask task=new AssignTutorTask();
			task.execute(url);
		}
	}
	
	class AssignTutorTask extends AsyncTask
	  <String, Void, String>
	{
		
		protected String doInBackground(String... params) 
		{
			String url=params[0];
			HttpGet getReq=new HttpGet(url);
			HttpClient client=new DefaultHttpClient();
			
			String result="";
			try
			{
			 HttpResponse resp=client.execute(getReq); 
			 InputStream in=resp.getEntity().getContent();
			 
			 InputStreamReader reader=
					 new InputStreamReader(in);
			 
			 BufferedReader br
			 		=new BufferedReader(reader);
			 while(true)
			 {
				 String str=br.readLine();
				 if(str==null) break;
				 result=result+str;
			 }//eof while		 
			 br.close();
			 			
			}catch(Exception ex) {   }		
			
			return result;
		}//eof doInBack...
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try
			{
				JSONObject obj=new JSONObject(result);
				int res=obj.getInt("result");
				Log.e("Result", res+"");
				if(res==1)
				{
					Toast.makeText(StudentSecondSpaceActivity.this,
							"Congrats!!! Tutor Hired.", 6).show();
					finish();
				}
				else if(res==5)
				{
					Toast.makeText(StudentSecondSpaceActivity.this,
							"You have already hired the tutor.", 6).show();
				}
				else 
				{
					Toast.makeText(StudentSecondSpaceActivity.this,
							"Request Failed. Try again.", 6).show();
				}
			}catch(Exception ex)
			{
				Log.e("Error", ex.toString());
			}
		}
		
	}
	
}
