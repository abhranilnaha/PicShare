package edu.sjsu.picshare;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadPhoto extends Activity {	
	private static int RESULT_LOAD_IMG = 1;
	private Button createNewAlbum;
	private Button uploadtToExisting;
	private String email;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.photo_upload_options);
		
		Intent intent = getIntent();
		email = intent.getExtras().getString("email");
		System.out.println("in UploadPhoto clas....User email is "+ email);
	     
		createNewAlbum = (Button) findViewById(R.id.createNew);
		createNewAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickSelectPhoto();
			}
			   
        });    
	    
		uploadtToExisting = (Button) findViewById(R.id.viewAlbums);
		uploadtToExisting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               onClickPostPhoto();
            }
        });
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
	
	private void onClickSelectPhoto() {
    	Intent intent = new Intent(this, CreateAlbum.class);
    	intent.putExtra("email",email);
        startActivity(intent);
    }
	
	private void onClickPostPhoto() {
    	Intent intent = new Intent(this, AlbumListDisplay.class);
    	intent.putExtra("email",email);
        startActivity(intent);
    }
	public void onClickViewPhoto(){
		Intent intent = new Intent(this, FetchImages.class);   
		//intent.putExtra("image", retrievedImages);
		System.out.println("in intent method");
		intent.putExtra("email",email);
		startActivity(intent);
    }
}
