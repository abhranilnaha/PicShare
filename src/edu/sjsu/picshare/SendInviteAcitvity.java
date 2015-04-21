package edu.sjsu.picshare;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import bolts.AppLinks;

import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;


public class SendInviteAcitvity extends Activity 

{
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends);
        System.out.println("Inside SendInviteAcitvity");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) 
        {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        else {
	        AppLinkData.fetchDeferredAppLinkData(
	            this, new AppLinkData.CompletionHandler() 
	            {
	                @Override
	                public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) 
	                {
	                    
	                }
	            });
	    }
        openDialog();
        
        
        
	}

	private void openDialog() 
	{
		String appLinkUrl, previewImageUrl;

		appLinkUrl = "https://fb.me/529292150554209";
		previewImageUrl = "http://topwalls.net/wallpapers/2013/05/Golden-Gate-Bridge-1024x1024.jpg";

		if (AppInviteDialog.canShow()) 
		{
		    AppInviteContent content = new AppInviteContent.Builder()
		                .setApplinkUrl(appLinkUrl)
		                .setPreviewImageUrl(previewImageUrl)
		                .build();
		    AppInviteDialog.show(this, content);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        System.out.println("Inside FriendsListAcitvity");
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("user_friends"));
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("user_about_me"));
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("read_friendlists"));
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("read_custom_friendlists"));
        
        
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        GraphRequest request = GraphRequest.newMeRequest(accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object,GraphResponse response) 
//                    {
//                        // Application code
//                    	try 
//                    	{
//                    		System.out.println("Inside FriendsListAcitvity... calling Graph API");
//							String name = (String) object.get("name");
//							String id = (String) object.get("id");
//							Toast.makeText(getApplicationContext(), "name is "+name, Toast.LENGTH_SHORT).show();
//							//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
//						} 
//                    	catch (JSONException e) 
//                    	{
//							
//							e.printStackTrace();
//						}
//                    	
//                    	
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name");
//        request.setParameters(parameters);
//        request.executeAsync();
        
       GraphRequest request = GraphRequest.newMeRequest(
                accessToken,new GraphRequest.GraphJSONObjectCallback() 
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) 
                    {
                    	try 
                    	{
                    		System.out.println("Inside FriendsListAcitvity... calling Graph API");
                    		JSONObject innerJson = response.getJSONObject();
                    		System.out.println("innerJson is :"+innerJson);
                    		
                    		
            	        	//JSONArray data = innerJson.getJSONArray("");
//							Object objname = (Object) object.get("friendlists");
//							System.out.println("objname is :"+objname);
							//String id = (String) object.get("id");
							//Toast.makeText(getApplicationContext(), "name is "+name, Toast.LENGTH_SHORT).show();
							//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
						} 
                    	catch (Exception e) 
                    	{
							
							e.printStackTrace();
						}
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "friend-list");
        request.setParameters(parameters);
        request.executeAsync();
       
        /*
        new GraphRequest( accessToken,"/{friend-list-id}",null,HttpMethod.GET,
        	    new GraphRequest.Callback() 
        		{
        	        public void onCompleted(GraphResponse response) 
        	        {
        	        	Toast.makeText(getApplicationContext(), "Viewing friends!! ", Toast.LENGTH_SHORT).show();
        	        	
//        	        	try 
//        	        	{
        	        		
        	        	JSONObject innerJson = response.getJSONObject();
        	        	JSONArray j;
        	        	
        	        	
//        	        		for (int i=0; i< json.length() ; i++)
//        	        		{
        	        			JSONObject obj = (JSONObject)response.getJSONObject();
        	        			System.out.println("json object : "+innerJson);
        	        			System.out.println("response object :: "+response.toString());
        	        			
//        	        			String id = (String)obj.get("id");
//        	        			String name = (String)obj.get("name");
//        	        			System.out.println("friendlist id is :"+id);
//        	        			System.out.println("friendlist name is :"+name);
        	        		//}
//        	        	}
//        	        	catch (JSONException e) 
//        	        	{
//							
//							e.printStackTrace();
//						}
        	        }
        	    }
        	).executeAsync();
        
        
        
       
        
	}*/
	
}
