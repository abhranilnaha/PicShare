package edu.sjsu.picshare;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class CustomGallery extends Activity {

	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	private ImageAdapter imageAdapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private Button UploadToAlbum;
	private Button viewPhotos;

	Bitmap thumbnail = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_custom);
		UploadToAlbum = (Button) findViewById(R.id.button1);
		
		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

		final Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				columns, null, null, orderBy + " DESC");

		this.imageUrls = new ArrayList<String>();

		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(imagecursor.getString(dataColumnIndex));

			System.out.println("=====> Array path => " + imageUrls.get(i));
		}

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.com_facebook_profile_picture_blank_portrait)
				.showImageForEmptyUri(R.drawable.com_facebook_profile_picture_blank_square)
				.cacheInMemory().cacheOnDisc().build();

		imageAdapter = new ImageAdapter(this, imageUrls);

		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(imageAdapter);
		UploadToAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
				for (int i = 0; i < selectedItems.size(); i++) {
					imagecursor.moveToPosition(i);
					int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
					String uriString = imagecursor.getString(dataColumnIndex);
					imageUrls.add(uriString);

					Uri fileUri = Uri.parse(uriString);
					String displayName = new File(fileUri.getPath()).getName();

					System.out.println("=====> Array path => " + imageUrls.get(i));

					thumbnail = (BitmapFactory.decodeFile(selectedItems.get(i)));
					// converting image into Bitmap
					Bitmap bm = BitmapFactory.decodeFile(selectedItems.get(i));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					
					byte[] byteArrayImage = baos.toByteArray();
					// converting image into Base64
					String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
					Log.d("Encoded Image", encodedImage);
					Log.d("TOTAL IMAGES", i + "");
					// Create the ParseFile
					ParseFile file = new ParseFile(displayName, byteArrayImage);
					// Upload the image into Parse Cloud
					file.saveInBackground();
					String albumName = getIntent().getExtras().getString("albumName");

					// Create a New Class called "ImageUpload" in Parse
					ParseObject imgupload = new ParseObject("ImageUpload");

					// Create a column named "ImageName" and set the string
					
					imgupload.put("AlbumName", albumName);

					// Create a column named "ImageName" and set the string
					imgupload.put("ImageName", displayName);

					// Create a column named "ImageFile" and insert the image
					imgupload.put("ImageFile", file);

					// Create the class and the columns
					imgupload.saveInBackground();

					// Show a simple toast message
					Toast.makeText(CustomGallery.this, i + 1 + " Image Uploaded", Toast.LENGTH_SHORT).show();
				}
				Toast.makeText(CustomGallery.this, "Upload Completed", Toast.LENGTH_SHORT).show();
				System.out.println("images uploaded");
				System.out.println("retrieving....");
			}
		});
		
		
	
	viewPics();
		
	}

	private void viewPics() {
		// TODO Auto-generated method stub
		viewPhotos = (Button) findViewById(R.id.viewSavedPhotos);

		viewPhotos.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String albumName = getIntent().getExtras().getString("albumName");
			Intent intent = new Intent(CustomGallery.this, FetchImages.class);  
			intent.putExtra("albumname", albumName);
				startActivity(intent);
				//System.out.println("in view method");
			}
		});
		
	}

	@Override
	protected void onStop() {
		imageLoader.stop();
		super.onStop();
	}

	public class ImageAdapter extends BaseAdapter {

		ArrayList<String> mList;
		LayoutInflater mInflater;
		Context mContext;
		SparseBooleanArray mSparseBooleanArray;

		public ImageAdapter(Context context, ArrayList<String> imageList) {
			// TODO Auto-generated constructor stub
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
			mSparseBooleanArray = new SparseBooleanArray();
			mList = new ArrayList<String>();
			this.mList = imageList;

		}

		public ArrayList<String> getCheckedItems() {
			ArrayList<String> mTempArry = new ArrayList<String>();
			for (int i = 0; i < mList.size(); i++) {
				if (mSparseBooleanArray.get(i)) {
					mTempArry.add(mList.get(i));
				}
			}
			return mTempArry;
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.gallery_item, null);
			}

			CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
			final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);

			imageLoader.displayImage("file://" + imageUrls.get(position),
					imageView, options, new SimpleImageLoadingListener() {

					});

			mCheckBox.setTag(position);
			mCheckBox.setChecked(mSparseBooleanArray.get(position));
			mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
			return convertView;
		}

		OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mSparseBooleanArray.put((Integer) buttonView.getTag(),
						isChecked);
			}
		};
	}

}