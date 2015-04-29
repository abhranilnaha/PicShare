package edu.sjsu.picshare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

public class FriendsListActivity extends Activity 

{
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_item);
        System.out.println("Inside FriendsListActivity");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("user_friends"));
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("email"));
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        System.out.println(accessToken.getPermissions());
        final List<String> friendList  = new ArrayList<String>();
        final ListView mListView;
        final Context c = this;

        
        new GraphRequest(accessToken,"/me/friends",null,HttpMethod.GET,
        	    new GraphRequest.Callback() {
        	        public void onCompleted(GraphResponse response) 
        	        {
        	        	try 
    					{
            	    		System.out.println("response is "+ response);
            	    		JSONObject json = (JSONObject)response.getJSONObject();
             	    	   	JSONArray jarray = json.getJSONArray("data");
             	    	   	System.out.println("Array length is :"+jarray.length());
    						for(int i = 0; i < jarray.length(); i++)
    						{
    	        	    	     JSONObject friends = jarray.getJSONObject(i);
    	        	    	     String friendName = friends.getString("name");
    	        	    	     System.out.println("friend is "+ friendName);
    	        	    	     
    	        	    	     friendList.add(friendName);
    	        	    	}
    					}
    					catch (JSONException e) 
    					{
    						e.printStackTrace();
    					}
        	        }
        	    }
        	).executeAsync();
        
        
//        GraphRequestBatch batch = new GraphRequestBatch(
//                GraphRequest.newMyFriendsRequest(
//                        accessToken,
//                        new GraphRequest.GraphJSONArrayCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONArray jsonArray,
//                                    GraphResponse response) {
//                                // Application code for users friends
//                                System.out.println("getFriendsData onCompleted : jsonArray " + jsonArray);
//                                System.out.println("getFriendsData onCompleted : response " + response);
//                                try {
//                                    JSONObject jsonObject = response.getJSONObject();
//                                    System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
//                                    JSONObject summary = jsonObject.getJSONObject("summary");
//                                    System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        })
//
//        );
//        batch.addCallback(new GraphRequestBatch.Callback() {
//            @Override
//            public void onBatchCompleted(GraphRequestBatch graphRequests) {
//                // Application code for when the batch finishes
//            }
//        });
//        batch.executeAsync();

        
        
//        GraphRequest request = GraphRequest.newMyFriendsRequest(accessToken,new GraphRequest.GraphJSONArrayCallback() 
//        {
//                    @Override
//                    public void onCompleted(JSONArray jsonArray, GraphResponse response) 
//                    {
//                    	try 
//    					{
//            	    		System.out.println("response is "+ response);
//            	    		JSONObject json = (JSONObject)response.getJSONObject();
//             	    	   	JSONArray jarray = json.getJSONArray("data");
//             	    	   	System.out.println("Array length is :"+jarray.length());
//    						for(int i = 0; i < jarray.length(); i++)
//    						{
//    	        	    	     JSONObject friends = jarray.getJSONObject(i);
//    	        	    	     String friendName = friends.getString("name");
//    	        	    	     System.out.println("friend is "+ friendName);
//    	        	    	     
//    	        	    	     friendList.add(friendName);
//    	        	    	}
//
//    					}
//    					catch (JSONException e) 
//    					{
//    						e.printStackTrace();
//    					}
//            	    	   
//                    }
//         });
//        request.executeAsync();
        
        
        
//        Intent intent = new Intent(this, FriendsView.class);
//        intent.putStringArrayListExtra("test", (ArrayList<String>) friendList);
//		startActivity(intent);
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "friends");
//        request.setParameters(parameters);
//        request.executeAsync();
        
//        new GraphRequest(
//        		accessToken,
//        	    "/me/friends",
//        	    null,
//        	    HttpMethod.GET,
//        	    new GraphRequest.Callback() {
//        	       @Override
//					public void onCompleted(GraphResponse response) 
//        	       {
//        	    	try 
//					{
//        	    		
//        	    		System.out.println("response is "+ response);
//        	    		JSONObject json = (JSONObject)response.getJSONObject();
//         	    	   	JSONArray jarray = json.getJSONArray("data");
//         	    	   	System.out.println("Array length is :"+jarray.length());
//						for(int i = 0; i < jarray.length(); i++)
//						{
//	        	    	     JSONObject friends = jarray.getJSONObject(i);
//	        	    	     String friendName = friends.getString("name");
//	        	    	     System.out.println("friend is "+ friendName);
//	        	    	}	        	    	 
//					}
//					catch (JSONException e) 
//					{
//						e.printStackTrace();
//					}
//        	    	   
//        	    	   
//					}
//        	    }
//        	).executeAsync();
        
	}

}