package com.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImgCompressor {
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// 图片缩小系统
	public Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options,480, 640);
		options.inJustDecodeBounds = false;
        
		Bitmap myBitmap = BitmapFactory.decodeFile(filePath, options);
		
		int scaleHeight = 640;
		int scaleWidth = 480;

		Matrix matrix = new Matrix();
		matrix.reset();
		
		if (myBitmap.getWidth()>myBitmap.getHeight())
		matrix.setRotate(90);

	
		float mWidth = (float) scaleWidth / (float) myBitmap.getWidth(); // target缩放到的宽度
																			// src原始宽度，这样算出来是个比率
		float mHeight = (float) scaleHeight / (float) myBitmap.getHeight();
		
	
		  matrix.postScale(mHeight, mWidth);
		
		// System.out.println(myBitmap.getWidth()+"====myBitmap.getWidth()="+myBitmap.getHeight());
		myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(),
				myBitmap.getHeight(), matrix, true);

		return myBitmap;
	}
}
