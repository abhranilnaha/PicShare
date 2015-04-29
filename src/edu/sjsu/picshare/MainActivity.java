package edu.sjsu.picshare;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class MainActivity extends FragmentActivity {

	private static final String PERMISSION = "publish_actions";
	
	private final String PENDING_ACTION_BUNDLE_KEY = "edu.sjsu.picshare:PendingAction";

	private Button postStatusUpdateButton;
	private Button postPhotoButton;
	private Button uploadPhotoButton;
	private Button inviteFriendsButton;
	private ProfilePictureView profilePictureView;
	private TextView greeting;
	private PendingAction pendingAction = PendingAction.NONE;
	private boolean canPresentShareDialog;
	private boolean canPresentShareDialogWithPhotos;
	private CallbackManager callbackManager;
	private ProfileTracker profileTracker;
	private ShareDialog shareDialog;
	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onCancel() {
			Log.d("Facebook", "Canceled");
		}

		@Override
		public void onError(FacebookException error) {
			Log.d("Facebook", String.format("Error: %s", error.toString()));
			String title = getString(R.string.error);
			String alertMessage = error.getMessage();
			showResult(title, alertMessage);
		}

		@Override
		public void onSuccess(Sharer.Result result) {
			Log.d("Facebook", "Success!");
			if (result.getPostId() != null) {
				String title = getString(R.string.success);
				String id = result.getPostId();
				String alertMessage = getString(R.string.successfully_posted_post, id);
				showResult(title, alertMessage);
			}
		}

		private void showResult(String title, String alertMessage) {
			new AlertDialog.Builder(MainActivity.this).setTitle(title)
					.setMessage(alertMessage)
					.setPositiveButton(R.string.ok, null).show();
		}
	};

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			PackageInfo info = getPackageManager().getPackageInfo("edu.sjsu.picshare", 
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		FacebookSdk.sdkInitialize(this.getApplicationContext());

		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						handlePendingAction();
						updateUI();
					}

					@Override
					public void onCancel() {
						if (pendingAction != PendingAction.NONE) {
							showAlert();
							pendingAction = PendingAction.NONE;
						}
						updateUI();
					}

					@Override
					public void onError(FacebookException exception) {
						if (pendingAction != PendingAction.NONE && exception 
								instanceof FacebookAuthorizationException) {
							showAlert();
							pendingAction = PendingAction.NONE;
						}
						updateUI();
					}

					private void showAlert() {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle(R.string.cancelled)
								.setMessage(R.string.permission_not_granted)
								.setPositiveButton(R.string.ok, null).show();
					}
				});

		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.main);

		profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
				updateUI();
				handlePendingAction();
			}
		};

		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		greeting = (TextView) findViewById(R.id.greeting);

		postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
		postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPostStatusUpdate();
			}
		});

		postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
		postPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPostPhoto();
			}
		});

		uploadPhotoButton = (Button) findViewById(R.id.uploadPhotoButton);
		uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickUploadPhoto();
			}
		});

		inviteFriendsButton = (Button) findViewById(R.id.inviteFriendsButton);
		inviteFriendsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickViewFriends();
			}
		});

		// Can we present the share dialog for regular links?
		canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);

		// Can we present the share dialog for photos?
		canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(this);
		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		profileTracker.stopTracking();
	}

	private void updateUI() {
		boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

		postStatusUpdateButton.setEnabled(enableButtons	|| canPresentShareDialog);
		postPhotoButton.setEnabled(enableButtons || canPresentShareDialogWithPhotos);

		Profile profile = Profile.getCurrentProfile();
		if (enableButtons && profile != null) {
			profilePictureView.setProfileId(profile.getId());
			greeting.setText(getString(R.string.hello_user,	profile.getFirstName()));
		} else {
			profilePictureView.setProfileId(null);
			greeting.setText(null);
		}
		//loadUserData();
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;		
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case NONE:
			break;
		case POST_PHOTO:
			postPhoto();
			break;
		case POST_STATUS_UPDATE:
			postStatusUpdate();
			break;
		}
	}

	private void onClickPostStatusUpdate() {
		performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
	}

	private void onClickViewFriends() {
		Intent intent = new Intent(this, SendInviteAcitvity.class);
		startActivity(intent);

	}

	private void postStatusUpdate() {
		Profile profile = Profile.getCurrentProfile();
		ShareLinkContent linkContent = new ShareLinkContent.Builder()
				.setContentTitle("Hello Facebook")
				.setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
				.setContentUrl(Uri.parse("http://developers.facebook.com/docs/android"))
				.build();
		if (canPresentShareDialog) {
			shareDialog.show(linkContent);
		} else if (profile != null && hasPublishPermission()) {
			ShareApi.share(linkContent, shareCallback);
		} else {
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}

	private void onClickUploadPhoto() {
		Intent intent = new Intent(this, UploadPhoto.class);
		startActivity(intent);
	}

	private void onClickPostPhoto() {
		performPublish(PendingAction.POST_PHOTO, canPresentShareDialogWithPhotos);
	}

	private void postPhoto() {
		Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
		SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
		ArrayList<SharePhoto> photos = new ArrayList<SharePhoto>();
		photos.add(sharePhoto);

		SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().setPhotos(photos).build();
		if (canPresentShareDialogWithPhotos) {
			shareDialog.show(sharePhotoContent);
		} else if (hasPublishPermission()) {
			ShareApi.share(sharePhotoContent, shareCallback);
		} else {
			pendingAction = PendingAction.POST_PHOTO;
		}
	}

	private boolean hasPublishPermission() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null && accessToken.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action, boolean allowNoToken) {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
				return;
			} else {
				// We need to get new permissions, then complete the action when
				// we get called back.
				LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(PERMISSION));
				return;
			}
		}

		if (allowNoToken) {
			pendingAction = action;
			handlePendingAction();
		}
	}
	
	public void loadUserData() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken == null)
			return;
        GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "me/taggable_friends", null);

        HashSet<String> extraFields = new HashSet<String>();
        Set<String> fields = new HashSet<String>(extraFields);
        String[] requiredFields = new String[]{"id", "name"};
        fields.addAll(Arrays.asList(requiredFields));
        fields.add(String.format(Locale.US, "picture.height(%d).width(%d)", 50, 50));

        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);

        request.setCallback(new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
            	JSONArray data = response.getJSONObject().optJSONArray("data");            	
            }
        });
        
        final GraphRequestBatch batch = new GraphRequestBatch(request);
        GraphRequest.executeBatchAsync(batch);
        
        GraphRequest myRequest = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        
                    }
                });
        Bundle params = new Bundle();
        params.putString("fields", "id,name,link,email,gender,birthday");
        myRequest.setParameters(params);
        myRequest.executeAsync();
    }
}
