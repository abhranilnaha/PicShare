package edu.sjsu.picshare;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class CreateAlbum extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_create);

		byte[] imageData = null;

		try {

			final int THUMBNAIL_SIZE = 64;

			FileInputStream fis = new FileInputStream("HalfMoon Bay");
			Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

			Float width = Float.valueOf(imageBitmap.getWidth());
			Float height = Float.valueOf(imageBitmap.getHeight());
			Float ratio = width / height;
			imageBitmap = Bitmap.createScaledBitmap(imageBitmap,
					(int) (THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			imageData = baos.toByteArray();

		} catch (Exception ex) {

		}

	}
}