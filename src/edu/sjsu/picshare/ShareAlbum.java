package edu.sjsu.picshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShareAlbum extends Activity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.gallery_custom);
	Intent intent = getIntent();
	String albumName = intent.getExtras().getString("albumName");
	String email = intent.getExtras().getString("email");
	
	}
}
