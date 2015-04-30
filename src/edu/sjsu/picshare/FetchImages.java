package edu.sjsu.picshare;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

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
	GridViewAdapter nameAdapter;
	private List<ImageList> imageArrayList = null;
	private List<ImageNameList> imageNameList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from gridview_main.xml
		setContentView(R.layout.gridview_main);
		// Execute RemoteDataTask AsyncTask
		
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
			imageNameList = new ArrayList<ImageNameList>();
			try {
				// Locate the class table named "albumname" in Parse.com
				String albumName = getIntent().getExtras().getString("albumname");
				System.out.println(albumName);
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageUpload");
				// Locate the column named "position" in Parse.com and order list
				// by ascending
		        query.whereEqualTo("AlbumName",albumName);

				query.orderByAscending("position");
				ob = query.find();
				for (ParseObject fetchedimg : ob) {
					ParseFile image = (ParseFile) fetchedimg.get("ImageFile");
					ImageList map = new ImageList();
					map.setMyImage(image.getUrl());
					imageArrayList.add(map);
				}
				
				for (ParseObject fetchedimgName : ob) {
					String imageName = (String) fetchedimgName.getObjectId();
					ImageNameList mapImgName = new ImageNameList();
					mapImgName.setMyImageName(imageName);
					imageNameList.add(mapImgName);
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
			adapter = new GridViewAdapter(FetchImages.this,imageArrayList,imageNameList);
			// Binds the Adapter to the ListView
			//nameAdapter=new GridViewAdapter(FetchImages.this,imageNameList);
			gridview.setAdapter(adapter);
			//gridview.setAdapter(nameAdapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
		}
		
	}
}