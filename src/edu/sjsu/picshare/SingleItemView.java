package edu.sjsu.picshare;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class SingleItemView extends Activity {

	String myImage;
	String myImageName;
	private EditText imgTitle;
	private EditText imgLoc;
	private TextView imgTitleView;
	private TextView imgLocView;
	private Button saveImgDetails;
	String retImageDesc;
	String retImageLoc;
	ImageLoader imageLoader = new ImageLoader(this);

	// ImageLoader imageNameLoader = new ImageLoader(this);
	List<ParseObject> imgdet;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Get the view from singleitemview.xml
		setContentView(R.layout.singleitemview);

		intent = getIntent();
		// i2 = getIntent();
		// Get the intent from ListViewAdapter
		myImage = intent.getStringExtra("myimage");
		// Locate the ImageView in singleitemview.xml
		ImageView imgphoto = (ImageView) findViewById(R.id.mysingleimage);

		// Load image into the ImageView
		imageLoader.DisplayImage(myImage, imgphoto);
		myImageName = intent.getStringExtra("myimagename");

		imgTitle = (EditText) findViewById(R.id.imgeditText1);
		imgLoc = (EditText) findViewById(R.id.imgeditText2);
		imgTitleView = (TextView) findViewById(R.id.imgTitleView);
		imgLocView = (TextView) findViewById(R.id.imgLocView);
		saveImgDetails = (Button) findViewById(R.id.imgbutton1);
		
		if (intent.getBooleanExtra("isReadOnly", false)) {
			imgTitle.setVisibility(View.GONE);
			imgLoc.setVisibility(View.GONE);
			saveImgDetails.setVisibility(View.GONE);
			imgTitleView.setVisibility(View.VISIBLE);
			imgLocView.setVisibility(View.VISIBLE);
		}

		final ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("ImageUpload");
		query2.whereEqualTo("objectId", myImageName);
		query2.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject retImageObj, ParseException e) {
				if (e == null) {
					retImageDesc = retImageObj.getString("ImageTitle");
					retImageLoc = retImageObj.getString("ImageLocation");
					if (retImageDesc != null) {
						imgTitle.setText(retImageDesc.toString(),
								TextView.BufferType.EDITABLE);
						imgLoc.setText(retImageLoc.toString(),
								TextView.BufferType.EDITABLE);
						imgTitleView.setText(retImageDesc.toString());
						imgLocView.setText(retImageLoc.toString());
					}
				}
			}
		});

		saveImgDetails.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				ParseQuery<ParseObject> query = ParseQuery.getQuery("ImageUpload");
				query.whereEqualTo("objectId", myImageName);
				query.getFirstInBackground(new GetCallback<ParseObject>() {
					public void done(ParseObject object, ParseException e) {
						if (object == null) {

						} else {
							object.put("ImageTitle", imgTitle.getText().toString());
							object.put("ImageLocation", imgLoc.getText().toString());
							object.saveInBackground(new SaveCallback() {
								public void done(ParseException e) {
									if (e == null) {
										Toast.makeText(SingleItemView.this, "Image Details Saved",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					}
				});
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_settings:
        	Intent intent = new Intent(this, MainActivity.class);			
			startActivity(intent);
            break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
}
