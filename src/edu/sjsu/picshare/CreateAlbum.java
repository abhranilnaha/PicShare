package edu.sjsu.picshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class CreateAlbum extends Activity {
	private EditText albumTitle;
	private EditText albumDesc;
	private Button saveAlbum;
	String email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.create_album);
		setContentView(R.layout.album_create);
		Intent intent = getIntent();
		email = intent.getExtras().getString("email");
		System.out.println("In CreateAlbum class... User email is "+ email);
		 
		albumTitle = (EditText) findViewById(R.id.albumTitle);
		albumDesc = (EditText) findViewById(R.id.albumDesc);
		saveAlbum = (Button) findViewById(R.id.saveAlbum);
		saveAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ParseObject parseObj = new ParseObject("AlbumList");
				
				parseObj.put("AlbumTitle", albumTitle.getText().toString());
				parseObj.put("AlbumDesc", albumDesc.getText().toString());
				parseObj.put("Owner", email);

				// Create the class and the columns
				parseObj.saveInBackground(new SaveCallback() {
					public void done(ParseException e) {
						if (e == null) {
							Intent intent = new Intent(CreateAlbum.this, AlbumListDisplay.class);
							intent.putExtra("email",email);
							startActivity(intent);
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