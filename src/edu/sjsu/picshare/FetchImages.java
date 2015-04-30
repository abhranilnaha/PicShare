package edu.sjsu.picshare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.facebook.Profile;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FetchImages extends Activity {
	// Declare Variables
	GridView gridview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	GridViewAdapter adapter;
	private List<ImageList> imageArrayList = null;
	private String albumName;
	private String email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		albumName = getIntent().getExtras().getString("albumName");
		email = getIntent().getExtras().getString("email");
		setTitle(String.format(getResources().getString(R.string.album),
				albumName));
		// Get the view from gridview_main.xml
		setContentView(R.layout.gridview_main);

		((Button) findViewById(R.id.shareAlbum))
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(FetchImages.this,
						ShareAlbumWithFriends.class);
				intent.putExtra("albumName", albumName);
				intent.putExtra("email", email);
				startActivity(intent);
				
			}
			
		});
		
//		((Button) findViewById(R.id.shareAlbum))
//				.setOnClickListener(new View.OnClickListener() {
//					public void onClick(View view) {
//						Intent intent = new Intent(FetchImages.this,
//								FetchImages.class);
//						intent.putExtra("albumName", albumName);
//						PendingIntent pendingIntent = PendingIntent
//								.getActivity(FetchImages.this, 0, intent, 0);
//
//						Profile profile = Profile.getCurrentProfile();
//						Notification notification = new Notification.Builder(
//								FetchImages.this)
//								.setContentTitle(
//										profile.getName()
//												+ " has shared an album: "
//												+ albumName)
//								.setSmallIcon(R.drawable.icon)
//								.setContentIntent(pendingIntent).build();
//						NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//						// hide the notification after its selected
//						notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//						notificationManager.notify(0, notification);
//					}
//				});

		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(FetchImages.this);
			// Set progressdialog title
			mProgressDialog.setTitle("FetchingImages");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			imageArrayList = new ArrayList<ImageList>();
			try {
				// Locate the class table named "SamsungPhones" in Parse.com

				System.out.println(albumName);
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageUpload");
				// Locate the column named "position" in Parse.com and order
				// list
				// by ascending
				query.whereEqualTo("AlbumName", albumName);

				query.orderByAscending("position");
				ob = query.find();
				for (ParseObject country : ob) {
					ParseFile image = (ParseFile) country.get("ImageFile");
					ImageList map = new ImageList();
					map.setMyImage(image.getUrl());
					imageArrayList.add(map);
				}
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Locate the gridview in gridview_main.xml
			gridview = (GridView) findViewById(R.id.gridview);
			// Pass the results into ListViewAdapter.java
			adapter = new GridViewAdapter(FetchImages.this, imageArrayList);
			// Binds the Adapter to the ListView
			gridview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
	}
}