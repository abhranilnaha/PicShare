package edu.sjsu.picshare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class AlbumListDisplay extends Activity 
{
	GridView gvAlbums = null;
	AlbumListAdapter adapterAlbums;
	ArrayList<Album> albums = null;
	String email;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(R.string.view_albums);
		setContentView(R.layout.album_grid);
		albums = new ArrayList<Album>();
		gvAlbums = (GridView) findViewById(R.id.grid_albums);
		Intent i = getIntent();
		email = i.getExtras().getString("email");
		gvAlbums.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Album album = albums.get(position);
				
				System.out.println("In AlbumListDisplay class... User email is "+ email);
				Intent intent = new Intent(AlbumListDisplay.this, CustomGallery.class);
				Bundle bundle = new Bundle();
				bundle.putString("albumName", album.getTitle());
				bundle.putString("email",email);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("AlbumList");
		query.whereEqualTo("Owner", email);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> albumList, ParseException e) {
				if (e == null) {
					try {						
						for (int i = 0; i < albumList.size(); i++) 
						{
							ParseObject obj = query.get(albumList.get(i).getObjectId());
							String albumTitle = (String) obj.get("AlbumTitle");
							String albumDesc = (String) obj.get("AlbumDesc");
							String owner = (String) obj.get("Owner");
							Album album = new Album();
							album.setTitle(albumTitle);
							album.setDesc(albumDesc);
							album.setOwner(owner);
							albums.add(album);
						}
						adapterAlbums = new AlbumListAdapter(
								AlbumListDisplay.this, albums);
						gvAlbums.setAdapter(adapterAlbums);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
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
}