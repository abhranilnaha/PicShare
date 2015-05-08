package edu.sjsu.picshare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home, menu);
	    return true;
	}
	
	private void onClickShareWithFriendsButton() {
		ArrayList<Friend> fList = dataAdapter.mylist;
		System.out.println("dataAdapter.mylist; :"+dataAdapter.mylist);
		ArrayList<String> emailList = new ArrayList<String>(); 
		for(int i=0;i<fList.size();i++) {
		     Friend friend = fList.get(i);
		     System.out.println("friend:::"+friend.name);
		     if(friend.isBox()) {
		    	 System.out.println("friend:::"+friend.emailAddress);
		    	 emailList.add(friend.emailAddress);
		     }
		 }
		System.out.println("onClickShareWithFriendsButton....emailList is "+ emailList);
		System.out.println("onClickShareWithFriendsButton....albumName is "+ albumName);
		saveSharedAlbumDetails(emailList, albumName);
	}
	
	private void saveSharedAlbumDetails(final ArrayList<String> emailList, final String albumName) {
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Customers");		
		query.whereContainedIn("email", emailList);
		query.findInBackground(new FindCallback<ParseObject>() {			
			public void done(List<ParseObject> customers, ParseException ex) {
				if (ex == null) {
					for (int i = 0; i < customers.size(); i++) {
						ParseObject parseObject = customers.get(i);
						List<String> albums = parseObject.getList("AlbumsSharedWithMe");
						if (albums == null)
							albums = new ArrayList<String>();
						if (!albums.contains(albumName))
							albums.add(albumName);
						parseObject.put("AlbumsSharedWithMe", albums);
						parseObject.saveInBackground(new SaveCallback() {
							public void done(ParseException e) {
								if (e == null) {
									Toast.makeText(ShareAlbumWithFriends.this, 
											"Shared the album!", Toast.LENGTH_SHORT).show();
									sendNotification();
									sendEmailNotification(emailList);
								}
							}
						});
					}
				}
			}
		});	
	}
	
	private void sendEmailNotification(ArrayList<String> emailList)	{
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		String[] emailArr = new String[emailList.size()];
		emailArr = emailList.toArray(emailArr);
			System.out.println(emailArr);
			emailIntent.setData(Uri.parse("mailto:"));
			emailIntent.setType("text/plain");
			emailIntent.putExtra(Intent.EXTRA_EMAIL,(emailArr));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared album from Picshare");
			emailIntent.putExtra(Intent.EXTRA_TEXT, "Please check your PicShare for albums shared with you!");
	      	try {
	      			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	      			finish();
	      	} catch (android.content.ActivityNotFoundException ex) {
	      		Toast.makeText(this, 
	      				"There is no email client installed.", Toast.LENGTH_SHORT).show();
	      	}		
	}
	
	private void sendNotification() {		
		Intent intent = new Intent(ShareAlbumWithFriends.this, FetchImages.class);
		intent.putExtra("albumName", albumName);
		PendingIntent pendingIntent = PendingIntent
				.getActivity(ShareAlbumWithFriends.this, 0, intent, 0);

		Profile profile = Profile.getCurrentProfile();
		Notification notification = new Notification.Builder(
				ShareAlbumWithFriends.this).setContentTitle(
						profile.getName() + " has shared an album: " + albumName)
				.setSmallIcon(R.drawable.icon)
				.setContentIntent(pendingIntent).build();
		NotificationManager notificationManager = 
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, notification);		
	}

	private void displayListView() throws ParseException {
		friendList = new ArrayList<Friend>();
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Customers");		
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> nameList, ParseException e) {
				if (e == null) {
					try {						
						for (int i = 0; i < nameList.size(); i++) {
							ParseObject obj = query.get(nameList.get(i).getObjectId());
							String friendName = (String) obj.get("name");
							String emailAddr = (String) obj.get("email");
							
							if (!email.equals(emailAddr)) {
								Friend friend = new Friend(friendName, false, emailAddr);
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
            case R.id.menu_settings:
            	Intent intent = new Intent(this, MainActivity.class);			
				startActivity(intent);
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
			//Toast.makeText(this, "Clicked on Friend :"+ f.getName(), Toast.LENGTH_SHORT).show();
		}		
	}
}