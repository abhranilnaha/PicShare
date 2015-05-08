package edu.sjsu.picshare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class AlbumListDisplay extends Activity {
	GridView gvAlbums = null;
	AlbumListAdapter adapterAlbums;
	ArrayList<Album> albums = null;
	String email;
	boolean isReadOnly;
	EditText search;
	ImageView searchBut;
	String [] alTitle;
	String [] alDesc;
	String [] alOwner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.view_albums);
		setContentView(R.layout.album_grid);
		search=(EditText) findViewById(R.id.searchView1);
        searchBut= (ImageView) findViewById(R.id.search_logo);
		albums = new ArrayList<Album>();
		gvAlbums = (GridView) findViewById(R.id.grid_albums);
		Intent intent = getIntent();
		email = intent.getExtras().getString("email");
		isReadOnly = intent.getExtras().getBoolean("isReadOnly");
		
		
		gvAlbums.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Album album = albums.get(position);
				if (isReadOnly) {
					Intent intent = new Intent(AlbumListDisplay.this, FetchImages.class);
					intent.putExtra("albumName", album.getTitle());
					intent.putExtra("owner", album.getOwner());
					intent.putExtra("email", email);
					intent.putExtra("isReadOnly", true);
					startActivity(intent);
				} else {									
					System.out.println("In AlbumListDisplay class... User email is "+ email);
					Intent intent = new Intent(AlbumListDisplay.this, CustomGallery.class);
					Bundle bundle = new Bundle();
					bundle.putString("albumName", album.getTitle());					
					bundle.putString("email", email);					
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});		
				
		final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AlbumList");
		if (isReadOnly) {
			ArrayList<String> albums = intent.getStringArrayListExtra("albums");
			query.whereContainedIn("AlbumTitle", albums);
		} else {
			query.whereEqualTo("Owner", email);
		}
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> albumList, ParseException e) {
				if (e == null) {
					try {	
						alTitle = new String[albumList.size()];
						alDesc= new String[albumList.size()];
						alOwner = new String[albumList.size()];
						for (int i = 0; i < albumList.size(); i++) {
							ParseObject obj = query.get(albumList.get(i).getObjectId());
							String albumTitle = (String) obj.get("AlbumTitle");
							String albumDesc = (String) obj.get("AlbumDesc");
							String owner = (String) obj.get("Owner");
							alTitle[i]=albumTitle;
							alDesc[i]=albumDesc;
							alOwner[i]=owner;
							
							Album album = new Album();
							album.setTitle(albumTitle);
							album.setDesc(albumDesc);
							album.setOwner(owner);
							albums.add(album);
							Log.d("Prabhu0", alTitle[i]+"  "+alDesc[i]+"  "+alOwner[i]);
						}
						adapterAlbums = new AlbumListAdapter(AlbumListDisplay.this, albums);
						gvAlbums.setAdapter(adapterAlbums);
						
						
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
					
			}
		});		
		
		searchBut.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String searchList = search.getText().toString();
				int count=0;
				albums.clear();
				for(int i=0; i< alTitle.length; i++){
					
					if(alTitle[i].toLowerCase().contains(searchList.toLowerCase())){
						Album album = new Album();
						album.setTitle(alTitle[i]);
						album.setDesc(alDesc[i]);
						album.setOwner(alOwner[i]);
						
						Log.d("PRABHU1", alTitle[i]+"  "+alDesc[i]+"  "+alOwner[i]);
						albums.add(album);
						count++;
						
						Log.d("PRABHU2",albums+"" );
			
					}

				}
				if(count==0){
					Toast.makeText(AlbumListDisplay.this, "No albums found",
							Toast.LENGTH_SHORT).show();
				}
				adapterAlbums = new AlbumListAdapter(AlbumListDisplay.this, albums);
				gvAlbums.setAdapter(adapterAlbums);
				
				
				Log.d("PRABHU3", ""+albums);
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