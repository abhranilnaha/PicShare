package edu.sjsu.picshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadPhoto extends Activity {

	private static int RESULT_LOAD_IMG = 1;
	private Button createNewAlbum;
	private Button uploadtToExisting;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_upload_options);

		createNewAlbum = (Button) findViewById(R.id.createNew);
		createNewAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickSelectPhoto();
			}

		});

		uploadtToExisting = (Button) findViewById(R.id.uploadExisting);
		uploadtToExisting.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPostPhoto();
			}
		});

	}

	private void onClickSelectPhoto() {
		Intent intent = new Intent(this, CreateAlbum.class);
		startActivity(intent);
	}

	private void onClickPostPhoto() {
		Intent intent = new Intent(this, CustomGallery.class);
		startActivity(intent);
	}

}