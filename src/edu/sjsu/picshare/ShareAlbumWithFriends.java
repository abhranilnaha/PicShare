package edu.sjsu.picshare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ShareAlbumWithFriends extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener {
	private String albumName;
	private String email;
	private ListView mainListView;  
	FriendListAdapter dataAdapter;
	ArrayList<Friend> friendList;
	Button shareAlbumButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.display_app_friends_list);
		albumName = getIntent().getExtras().getString("albumName");
		email = getIntent().getExtras().getString("email");
		System.out.println("Inside ShareAlbumWithFriends...Received intent email..: "+email);
		mainListView = (ListView) findViewById(R.id.appFriendsListView);
		try {
			displayListView();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		shareAlbumButton = (Button) findViewById(R.id.shareAlbumWithFriendsButton);
		shareAlbumButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {				
				onClickShareWithFriendsButton();
			}
		});
	}
	
	private void onClickShareWithFriendsButton() {
		ArrayList<Friend> fList = dataAdapter.mylist;
		System.out.println("dataAdapter.mylist; :"+dataAdapter.mylist);
		ArrayList<String> emailList = new ArrayList<String>(); 
		for(int i=0;i<fList.size();i++)
		{
		     Friend friend = fList.get(i);
		     if(friend.isBox())
		     {
		    	 emailList.add(friend.getEmailAddress());
		     }
		 }
		System.out.println("onClickShareWithFriendsButton....emailList is "+ emailList);
		System.out.println("onClickShareWithFriendsButton....albumName is "+ albumName);
		boolean success =  saveSharedAlbumDetails(emailList, albumName);
		Toast.makeText(this, "Shared the album!", Toast.LENGTH_SHORT).show();
		//Change this -- Sindhu
//		if(success)
//		{
//			Toast.makeText(this, "Shared the album!", Toast.LENGTH_SHORT).show();
//		}
//		else
//		{
//			Toast.makeText(this, "Error while sharing the Album!", Toast.LENGTH_SHORT).show();
//		}
		
//		Intent intent = new Intent(this, SaveShareAlbumDetails.class);
//		intent.putExtra("emailList", emailList);
//		intent.putExtra("albumName", albumName);
//		startActivity(intent);
	}
	boolean inserted = false;
	private boolean saveSharedAlbumDetails(ArrayList<String> emailList,String albumName) {
		
		final String alb = albumName;
		for(final String e : emailList)
		{
			System.out.println("Email from list is "+ e);
			
			final ParseQuery<ParseObject> query = ParseQuery.getQuery("Customers");		
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> nameList, ParseException ex) {
					if (ex == null) {
						try {						
							for (int i = 0; i < nameList.size(); i++) 
							{
								ParseObject obj = query.get(nameList.get(i).getObjectId());
								String emailAddr = (String) obj.get("email");								
								if (e.equals(emailAddr))
								{
									obj.add("AlbumsSharedWithMe", alb);
									inserted =  true;
								}
							}
							
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}
		return inserted;	
	}

	private void displayListView() throws ParseException {
		friendList = new ArrayList<Friend>();
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Customers");		
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> nameList, ParseException e) {
				if (e == null) {
					try {						
						for (int i = 0; i < nameList.size(); i++) 
						{
							ParseObject obj = query.get(nameList.get(i).getObjectId());
							String friendName = (String) obj.get("name");
							String emailAddr = (String) obj.get("email");
							
							if (!email.equals(emailAddr))
							{
								Friend friend = new Friend(friendName, false, email);
								System.out.println("Friend name is : "+friendName);
								System.out.println("Friend email is : "+emailAddr);
								friendList.add(friend);
							}
						}
						dataAdapter = new FriendListAdapter(ShareAlbumWithFriends.this, friendList);
						System.out.println("Friend List in DisplayListView is : "+friendList);
						mainListView.setAdapter(dataAdapter);
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int pos = mainListView.getPositionForView(buttonView);
		if(pos != ListView.INVALID_POSITION) {
			Friend f = friendList.get(pos);
			f.setBox(isChecked);
			Toast.makeText(this, "Clicked on Friend :"+ f.getName(), Toast.LENGTH_SHORT).show();
		}		
	}
}