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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TutorSpaceActivity extends Activity implements OnClickListener {

	ListView LVStudents;
	ArrayList<Student> listStudents = new ArrayList<Student>();
	Student_ItemAdapter adapter;
	Button BViewStudent;
	String temail;
	TextView TVStuListStatus;
	Spinner SPTimeslot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutspace);

		LVStudents = (ListView) findViewById(R.id.lvStudents);
		BViewStudent = (Button) findViewById(R.id.btnViewBatch);
		SPTimeslot = (Spinner) findViewById(R.id.spTimeslot2);

		TVStuListStatus = (TextView) findViewById(R.id.tvStuListStatus);
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.timeslot2, R.layout.spinnerlayout);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SPTimeslot.setAdapter(adapter1);

		BViewStudent.setOnClickListener(this);

		SharedPreferences sp = getSharedPreferences("settingstut", MODE_PRIVATE);
		temail = sp.getString("email", "");

		adapter = new Student_ItemAdapter(TutorSpaceActivity.this, R.layout.entityitem, listStudents);

		LVStudents.setAdapter(adapter);
	}

	@Override
	public void onBackPressed()
	{
			backButtonHandler();
	}
		
		private void backButtonHandler() {
			// TODO Auto-generated method stub
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(TutorSpaceActivity.this);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int timeslot = 0;
		if (!(SPTimeslot.getSelectedItem().toString().equals("All")))
			timeslot = Integer.parseInt(SPTimeslot.getSelectedItem().toString());

		String url = WebHelper.baseUrl + "StudentListServlet?temail=" + temail + "&timeslot=" + timeslot;
		url = url.replaceAll(" ", "%20");
		GetStudentTask task = new GetStudentTask();
		task.execute(url);
	}

	class Student_ItemAdapter extends BaseAdapter {
		Context context;
		int resource;
		ArrayList<Student> studentList;

		public Student_ItemAdapter(Context context, int resource, ArrayList<Student> studentList) {
			super();
			this.context = context;
			this.resource = resource;
			this.studentList = studentList;
		}

		public int getCount() {
			return studentList.size();
		}

		public Student getItem(int position) {
			return studentList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// get the object at position
			final Student s = studentList.get(position);

			// load the item design or UI
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			View itemView = inf.inflate(resource, null);

			// initialize itemView with post data

			TextView TVName = (TextView) itemView.findViewById(R.id.tvEntityName);

			Button BDetails = (Button) itemView.findViewById(R.id.btnEntityDetails);

			TVName.setText(s.getName());

			BDetails.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent in = new Intent(context, TutorSpaceTwoActivity.class);
					in.putExtra("student", s);
					context.startActivity(in);
				}
			});

			return itemView;

		}

	}

	class GetStudentTask extends AsyncTask<String, Void, String> {
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
				listStudents.clear();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					int studentid = obj.getInt("studentid");
					String name = obj.getString("name");
					String email = obj.getString("email");
					String phone = obj.getString("phone");
					String address = obj.getString("address");
					String institution = obj.getString("institution");
					String area = obj.getString("area");
					String gender = obj.getString("gender");
					int standard = obj.getInt("standard");

					Student s = new Student(studentid, name, email, phone, address, institution, area, gender, standard,
							null);

					listStudents.add(s);

				}
				adapter.notifyDataSetChanged();

				int count = LVStudents.getCount();
				if (count == 0)
					TVStuListStatus.setText("No student in this batch.");
				else
					TVStuListStatus.setText(count + " Student(s) in this batch.");

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}// eof task
}
