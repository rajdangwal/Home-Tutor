package com.example.majorproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StudentFirstSpaceActivity extends Activity implements OnClickListener {

	ListView LVTutors;
	ArrayList<Tutor> listTutors = new ArrayList<Tutor>();
	Tutor_ItemAdapter adapter;
	Button BGetTutors;
	TextView TVCount;
	String subject;
	int standard;
	int timeslot;

	Spinner SPSubject, SPGender, SPTimeslot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentfirstspace);

		LVTutors = (ListView) findViewById(R.id.lvTutorList);
		BGetTutors = (Button) findViewById(R.id.btnTempGetTutors);

		SPSubject = (Spinner) findViewById(R.id.spSubject);
		SPGender = (Spinner) findViewById(R.id.spGender);
		SPTimeslot = (Spinner) findViewById(R.id.spTimeslot);

		BGetTutors.setOnClickListener(this);

		TVCount = (TextView) findViewById(R.id.tvCount);

		SharedPreferences sp = getSharedPreferences("settingsstu", MODE_PRIVATE);
		int student_id = sp.getInt("studentid", 0);
		String name = sp.getString("name", "");
		standard = sp.getInt("standard", 0);

		adapter = new Tutor_ItemAdapter(StudentFirstSpaceActivity.this, R.layout.entityitem, listTutors);

		LVTutors.setAdapter(adapter);

		if (standard == 10) {
			ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.subject_10, R.layout.spinnerlayout);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SPSubject.setAdapter(adapter1);
		} else if (standard == 12) {
			ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.subject_12, R.layout.spinnerlayout);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SPSubject.setAdapter(adapter1);
		}

		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.gender, R.layout.spinnerlayout);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SPGender.setAdapter(adapter2);

		ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.timeslot, R.layout.spinnerlayout);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SPTimeslot.setAdapter(adapter3);
	}

	@Override
	public void onBackPressed()
	{
			backButtonHandler();
	}
		
		private void backButtonHandler() {
			// TODO Auto-generated method stub
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudentFirstSpaceActivity.this);
			alertDialog.setTitle("Log Out?");
			alertDialog.setMessage("Are you sure you want to log out?");
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		subject = SPSubject.getSelectedItem().toString();
		subject = subject + " " + standard;
		String gender = SPGender.getSelectedItem().toString();
		if (!(SPTimeslot.getSelectedItem().toString().equals("No Preference")))
			timeslot = Integer.parseInt(SPTimeslot.getSelectedItem().toString());
		else
			timeslot = 0;

		String url = WebHelper.baseUrl + "TutorListServlet?subject=" + subject + "&gender=" + gender + "&timeslot="
				+ timeslot;
		url = url.replaceAll(" ", "%20");
		GetTutorTask task = new GetTutorTask();
		task.execute(url);

	}

	class Tutor_ItemAdapter extends BaseAdapter {
		Context context;
		int resource;
		ArrayList<Tutor> tutorList;

		public Tutor_ItemAdapter(Context context, int resource, ArrayList<Tutor> tutorList) {
			super();
			this.context = context;
			this.resource = resource;
			this.tutorList = tutorList;
		}

		public int getCount() {
			return tutorList.size();
		}

		public Tutor getItem(int position) {
			return tutorList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// get the object at position
			final Tutor t = tutorList.get(position);

			// load the item design or UI
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			View itemView = inf.inflate(resource, null);

			// initialize itemView with post data

			TextView TVName = (TextView) itemView.findViewById(R.id.tvEntityName);

			Button BDetails = (Button) itemView.findViewById(R.id.btnEntityDetails);

			TVName.setText(t.getName());

			BDetails.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent in = new Intent(context, StudentSecondSpaceActivity.class);
					in.putExtra("tutor", t);
					in.putExtra("subject", subject);
					in.putExtra("timeslot", timeslot + "");
					context.startActivity(in);
				}
			});

			return itemView;

		}

	}

	class GetTutorTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			String url = params[0];
			HttpGet getReq = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();

			String result = "";
			try {
				HttpResponse resp = client.execute(getReq);
				InputStream in = resp.getEntity().getContent();

				InputStreamReader reader = new InputStreamReader(in);

				BufferedReader br = new BufferedReader(reader);
				while (true) {
					String str = br.readLine();
					if (str == null)
						break;
					result = result + str;
				} // eof while
				br.close();

			} catch (Exception ex) {
			}

			return result;
		}// eof doInBack...

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.e("json", result);

			// json parsing
			try {
				JSONArray jsonArray = new JSONArray(result);
				listTutors.clear();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					int tutorid = obj.getInt("tutorid");
					String name = obj.getString("name");
					String email = obj.getString("email");
					String phone = obj.getString("phone");
					String address = obj.getString("address");
					String qualification = obj.getString("qualification");
					String occupation = obj.getString("occupation");
					String gender = obj.getString("gender");
					String ts1status = obj.getString("ts1status");
					String ts2status = obj.getString("ts2status");
					int approved = obj.getInt("approved");

					Tutor t = new Tutor(tutorid, name, email, phone, address, qualification, occupation, gender, null,
							ts1status, ts2status, approved);

					listTutors.add(t);

				}
				adapter.notifyDataSetChanged();
				int count = LVTutors.getCount();
				if (count == 0)
					TVCount.setText("No tutor found.\nTry different parameters.");
				else
					TVCount.setText(count + " Tutor(s) Found.");

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}// eof task

}
