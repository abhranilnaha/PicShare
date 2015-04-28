package edu.sjsu.picshare;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsView extends ListActivity  

{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		Intent data = getIntent();
		ArrayList<String> friends = data.getStringArrayListExtra("test");
		String[] FRIENDS = friends.toArray(new String[friends.size()]);
		
		ListView listView = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, FRIENDS);
		listView.setAdapter(adapter);
		
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//			    // When clicked, show a toast with the TextView text
//			    Toast.makeText(getApplicationContext(),
//				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
//			}
//		});
 
	}
	
	
}
