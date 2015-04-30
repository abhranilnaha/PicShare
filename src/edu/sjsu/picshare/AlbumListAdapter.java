package edu.sjsu.picshare;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlbumListAdapter extends ArrayAdapter<Album> {	
	ArrayList<Album> mylist;

	public AlbumListAdapter(Context context, ArrayList<Album> mylist) {
		super(context, R.layout.display_app_friends_list, mylist);
		this.mylist = mylist;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Album album = getItem(position);

		ProductViewHolder holder;

		if (convertView == null) {
			convertView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			convertView = vi.inflate(R.layout.album_item, parent, false);
			
			holder = new ProductViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(holder);
		} else {
			holder = (ProductViewHolder) convertView.getTag();
		}
		
		holder.populate(album);
		return convertView;
	}

	static class ProductViewHolder {
		public ImageView img;
		public TextView title;

		void populate(Album p) {
			title.setText(p.title);
		}
	}
}
