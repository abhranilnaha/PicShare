package edu.sjsu.picshare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;
 
import java.util.ArrayList;
import java.util.List;

public class CreateAlbum extends Activity implements AbsListView.OnScrollListener {
	List albums;
    GridView gvAlbums = null;
    AlbumListAdapter adapterAlbums; 
 
    private boolean lvBusy = false;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_create);
 
        // populate data
        albums = new ArrayList();
        albums.add(new Album("Orange","http://farm5.staticflickr.com/4142/4787427683_3672f1db9a_s.jpg"));
        albums.add(new Album("Apple","http://farm4.staticflickr.com/3139/2780642603_8d2c90e364_s.jpg"));
        albums.add(new Album("Pineapple","http://farm2.staticflickr.com/1008/1420343003_13eeb0f9f3_s.jpg"));
        albums.add(new Album("Appricot","http://farm5.staticflickr.com/4118/4784687474_0eca8226b0_z.jpg"));
 
        //
        gvAlbums = (GridView) findViewById( R.id.grid_albums);
        adapterAlbums = new AlbumListAdapter(this, albums);
        gvAlbums.setAdapter(adapterAlbums);
 
    }
 
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
 
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
                adapterAlbums.notifyDataSetChanged();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                lvBusy = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                lvBusy = true;
                break;
        }
    }
 
 
    public boolean isLvBusy(){
        return lvBusy;
    }
}