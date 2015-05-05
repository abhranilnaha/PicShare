package edu.sjsu.picshare;
import java.util.ArrayList;
import java.util.List;
 
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
 
public class GridViewAdapter extends BaseAdapter {
 
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	//ImageLoader imageNameLoader;
	private List<ImageList> imagearraylist = null;
	private List<ImageNameList> imageNameList = null;
	private ArrayList<ImageList> arraylist;
	private ArrayList<ImageNameList> arrayNameList;
	private boolean isReadOnly;
 
	public GridViewAdapter(Context context, List<ImageList> imagearraylist, List<ImageNameList> imageNameList, boolean isReadOnly) {
		this.context = context;
		this.imagearraylist = imagearraylist;
		this.imageNameList = imageNameList;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<ImageList>();
		this.arrayNameList= new ArrayList<ImageNameList>();
		this.arraylist.addAll(imagearraylist);
		this.arrayNameList.addAll(imageNameList);
		this.isReadOnly = isReadOnly;
		imageLoader = new ImageLoader(context);				
	}

	public class ViewHolder {
		ImageView myimage;
		ImageView myimagename;
		
	}
 
	@Override
	public int getCount() {
		return imagearraylist.size();
	}
 
	@Override
	public Object getItem(int position) {
		return imagearraylist.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.gridview_item, null);
			// Locate the ImageView in gridview_item.xml
			holder.myimage = (ImageView) view.findViewById(R.id.mygridimage);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Load image into GridView
		imageLoader.DisplayImage(imagearraylist.get(position).getImage(),
				holder.myimage);
		// Capture GridView item click
		view.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(context, SingleItemView.class);
				
				// Pass all data phone
				intent.putExtra("myimage", imagearraylist.get(position).getImage());
				intent.putExtra("myimagename", imageNameList.get(position).getImageName());
				intent.putExtra("isReadOnly", isReadOnly);
				
				context.startActivity(intent);
			}
		});
		return view;
	}
}