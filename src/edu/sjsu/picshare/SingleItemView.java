package edu.sjsu.picshare;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
 
public class SingleItemView extends Activity {
 
	String myImage;
	ImageLoader imageLoader = new ImageLoader(this);
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Get the view from singleitemview.xml
		setContentView(R.layout.singleitemview);
 
		Intent i = getIntent();
		// Get the intent from ListViewAdapter
		myImage = i.getStringExtra("myimage");
 
		// Locate the ImageView in singleitemview.xml
		ImageView imgphone = (ImageView) findViewById(R.id.mysingleimage);
 
		// Load image into the ImageView
		imageLoader.DisplayImage(myImage, imgphone);
	}
}