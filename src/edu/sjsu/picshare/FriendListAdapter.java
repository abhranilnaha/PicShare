package edu.sjsu.picshare;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


 class Friend 
 {
	
	String name;
	boolean box;
	String emailAddress;
	
	public Friend(String name, boolean box, String emailAddress)
	{
		this.name = name;
		this.box = box;
		this.emailAddress = emailAddress;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBox() {
		return box;
	}

	public void setBox(boolean box) {
		this.box = box;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

 }
	public class FriendListAdapter extends ArrayAdapter<Friend> 
	{
		ArrayList<Friend> mylist;
		Context context;

		public FriendListAdapter(Context context,ArrayList<Friend> mylist) 
		{
			super(context,R.layout.friend_item , mylist);
			this.mylist = mylist;
			this.context = context;
		}
		
		private static class FriendViewHolder 
		{
			public CheckBox check;
			public TextView name;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//Friend fr = getItem(position);
			View v = convertView;
			FriendViewHolder holder;

			if (convertView == null) 
			{
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) context.getSystemService(inflater);
				v = vi.inflate(R.layout.friend_item, null);
				holder = new FriendViewHolder();
				holder.check = (CheckBox) v.findViewById(R.id.checkBox);
				holder.name = (TextView) v.findViewById(R.id.friendName);
				holder.check.setOnCheckedChangeListener((ShareAlbumWithFriends) context);
//				convertView.setTag(holder);
//				
//				holder.name.setOnClickListener( new View.OnClickListener() {  
//				     public void onClick(View v) {  
//				      CheckBox cb = (CheckBox) v ;  
//				      Friend f = (Friend) cb.getTag();  
//				      f.setBox(cb.isChecked());
//				      
//				     }  
//				    });

			} 
			else 
			{
				holder = (FriendViewHolder) v.getTag();
			}
			Friend f = mylist.get(position);
			holder.name.setText(f.getName());
			holder.check.setChecked(f.isBox());
			holder.check.setTag(f);
			return v;
		}
		
	}
	
	


