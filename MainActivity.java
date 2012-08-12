//Copyright (c) 2012 Bedleg Akos
//Contact information: a.bedleg@gmail.com
/*
 * This file is part of Pimp You Picture.

    Pimp Your Picture is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Pimp Your Picture is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Pimp Your Picture.  If not, see <http://www.gnu.org/licenses/>.
 * */
package com.pixelpixel.pyp;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class MainActivity extends PypActivity{

	ImageButton backButton;
	ImageButton effectsButton;
	ImageButton extrasButton;
	ImageButton saveButton;
	ImageButton shareButton;
	View.OnClickListener ButtonListener;
	PopupMenu.OnMenuItemClickListener PopupMenuListener;
	ImageView img; 
	Bitmap picture; //Holds the original bitmap
	Uri picUri; //Holds the uri of the original picture
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //We get the ImageView, and using the intent extra information
        //which contains the picture taken with the camera or
        //the picture picked from the gallery, we set the image for the ImageView.
        ImageView image = (ImageView)findViewById(R.id.main_picture);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	//We have picture taken with the camera
        	if (extras.containsKey("cPic")) {
        		Uri picUri = (Uri)extras.get("cPic");
        		image.setImageURI(picUri);
        		getIntent().removeExtra("cPic");
            //We have picture picked from gallery
        	}else if (extras.containsKey("gPic")){
        		Uri gPicUri = (Uri)extras.get("gPic");
        		image.setImageURI(gPicUri);
        		//Next line removes the extra information from the intent (the URI)
        		getIntent().removeExtra("gPic");
        	}
        }
        Log.i("TAG", "Image Displayed");
        //Declaring imageview bitmap for further effects
        img = (ImageView)findViewById(R.id.main_picture);
		Drawable drawing = img.getDrawable();
		picture = ((BitmapDrawable)drawing).getBitmap();
        
        //Implementing the onClickListeners for the buttons:
        //First we get the ImageButtons
        backButton = (ImageButton)findViewById(R.id.MainImageButtonBack);
        effectsButton = (ImageButton)findViewById(R.id.MainImageButtonEffects);
        extrasButton = (ImageButton)findViewById(R.id.MainImageButtonExtras);
        saveButton = (ImageButton)findViewById(R.id.MainImageButtonSave);
        shareButton = (ImageButton)findViewById(R.id.MainImageButtonShare);
        
        
        Log.i("TAG", "Buttons recognized");
        
        //Defining listener for the popup menu
        //POPUP MENU IS NOT WORKING ON MINSDK=8!
      /*  PopupMenuListener = new PopupMenu.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int menuItemId = item.getItemId();		
				if (menuItemId == R.id.MenuGrayScale) {
					Log.i("TAG", "Button GrayScale indentified");
					applyGrayscaleEffect();
				}else if (menuItemId == R.id.MenuSepia) {
					Log.i("TAG", "Button Sepia identified");
					applySepiaEffect();
				}else if (menuItemId == R.id.MenuOriginal) {
					applyOriginal();
				}else if (menuItemId == R.id.MenuInverse) {
					applyInverseEffect();
				}
				return true;
			}
		};*/
        
        //Defining listener for ImageButtons
        View.OnClickListener ButtonListener = new View.OnClickListener() {
			

			@Override
			public void onClick(View v) {
				ImageButton button = (ImageButton)v;
				//If we click on the back button
				if (button == backButton) {
					// A new alert dialog is created
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Are you sure you want to exit?\nAll your changes will be lost.")
					.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						//If we click Yes, the current activity is finished,
						//and we return to the previous activity.
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.this.finish();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						//If we click No, we return to the current activity
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}); 
					AlertDialog alert = builder.create();
					alert.show(); 
				}else if (button == effectsButton) {
					startActivity(new Intent(MainActivity.this, ListActivity.class));
					/*PopupMenu popup = new PopupMenu(MainActivity.this, v);
				    MenuInflater inflater = popup.getMenuInflater();
				    inflater.inflate(R.menu.effects, popup.getMenu());
				    popup.setOnMenuItemClickListener(PopupMenuListener);
				    popup.show();*/ 
				}else if (button == extrasButton) {
					//TODO: implement this function
				}else if (button == saveButton) {
					// A new alert dialog is created
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Do you want to save your picture?")
					.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						//If we click Yes, picture is saved to gallery/Pimped Pictures
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								saveFile();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						//If we click No, we return to the current activity
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}else if (button == shareButton) {
					try {
						saveFile();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					
					Intent share = new Intent(Intent.ACTION_SEND);
					share.setType("image/*");
					share.putExtra(Intent.EXTRA_STREAM, picUri);
					share.putExtra(Intent.EXTRA_TEXT, "Pyp test");
					startActivity(Intent.createChooser(share, "Share image"));
				}
			}
        }; 
        backButton.setOnClickListener(ButtonListener);
		effectsButton.setOnClickListener(ButtonListener);
		saveButton.setOnClickListener(ButtonListener);
		shareButton.setOnClickListener(ButtonListener);
	}
	
	//The state of the activity is saved when we leave it
	//This is necessary because when we click on the button Effects, we actually
	//leave the activity and proceed to the ListActivity. We are saving the state
	//to have access to the ImageView image which has to be changed.
	/*public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("ImageBitmap", picUri);;
	}*/
	//WHEN STARTING THIS ACTIVITY FROM LISTACTIVITY, ONCREATE() METHOD IS CALLED
	//AND NOT THE FOLLOWING ONRESTOREINSTANCE() METHOD. HAS TO MODIFY:
	//OR CHANGE STARTACTIVITY TO RESUMEACTIVITY(?) IN LISTACTIVITY
	//OR HAVE TO PUT THE FOLLOWING OPERATIONS INTO ONCREATE() METHOD!
	/*public void OnRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		picUri = savedInstanceState.getParcelable("ImageBitmap");
		try {
			picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		img = (ImageView)findViewById(R.id.main_picture);
		if (chosenEffect > 0) {
			switch (chosenEffect) {
			case 1: {
				applyOriginal();
				break;
			}
			case 2: {
				applyGrayscaleEffect();
				break;
			}
			case 3: {
				applySepiaEffect();
				break;
			}
			case 4: {
				applyInverseEffect();
				break;
			}
			default: break;
			}
			chosenEffect = 0;
		}
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey("chosenEffect")) {
				if (extras.getInt("chosenEffect") == 1) {
					applyOriginal();
				}else if (extras.getInt("chosenEffect") == 2) {
					applyGrayscaleEffect();
				}else if (extras.getInt("chosenEffect") == 3) {
					applySepiaEffect();
				}else if (extras.getInt("chosenEffect") == 4) {
					applyInverseEffect();
				}
			}
		}
		getIntent().removeExtra("chosenEffect");
	}*/
	
	//After resuming from ListActivity, we set the appropriate effect.
	public void onResume() {
		super.onResume();
		if (chosenEffect > 0) {
			switch (chosenEffect) {
			case 1: {
				applyOriginal();
				break;
			}
			case 2: {
				applyGrayscaleEffect();
				break;
			}
			case 3: {
				applySepiaEffect();
				break;
			}
			case 4: {
				applyInverseEffect();
				break;
			}
			default: break;
			}
			//After setting the effect, we reset the variable.
			chosenEffect = 0;
		}
	}
	
	//Saving the file to Gallery/Pimped Pictures
	public void saveFile() throws IOException{
		Drawable drw = img.getDrawable();
		Bitmap savedPic = ((BitmapDrawable)drw).getBitmap();
		OutputStream fOut = null;
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Pimped pictures");
		if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Pimp my picture", "failed to create directory");
                return;
            }
        }
		try {
			File picFile = new File(mediaStorageDir.getPath() + File.separatorChar +
			        "Pimped_"+ timeStamp + ".jpg");
			fOut = new FileOutputStream(picFile);
			picUri = Uri.fromFile(picFile);
			savedPic.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	//GrayScale effect
	public void applyGrayscaleEffect() {
		Log.i("TAG", "Bitmap created");
		
		ColorMatrix bwMatrix = new ColorMatrix();
	    bwMatrix.setSaturation(0);
	    final ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(bwMatrix);
	    Bitmap rBitmap = picture.copy(Bitmap.Config.ARGB_8888, true);
	    Paint paint = new Paint();
	    paint.setColorFilter(colorFilter);
	    Canvas myCanvas = new Canvas(rBitmap);
	    myCanvas.drawBitmap(rBitmap, 0, 0, paint);
	    
	    /*picture.recycle();
	    picture = null;*/
		
		img.setImageBitmap(rBitmap);
	}
	
	//Sepia effect
	public void applySepiaEffect(){		
		ColorMatrix sepiaMatrix =new ColorMatrix();
		float[] sepMat={0.3930000066757202f, 0.7689999938011169f, 0.1889999955892563f, 0, 0, 0.3490000069141388f, 0.6859999895095825f, 0.1679999977350235f, 0, 0, 0.2720000147819519f, 0.5339999794960022f, 0.1309999972581863f, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1};
		sepiaMatrix.set(sepMat);
		final ColorMatrixColorFilter colorFilter= new ColorMatrixColorFilter(sepiaMatrix);
		Bitmap rBitmap = picture.copy(Bitmap.Config.ARGB_8888, true);
		Paint paint=new Paint();
		paint.setColorFilter(colorFilter);
		Canvas myCanvas =new Canvas(rBitmap);
		myCanvas.drawBitmap(rBitmap, 0, 0, paint);
		
		img.setImageBitmap(rBitmap);
	}
	
	//Inverse effect
	public void applyInverseEffect() {
		float invert[] =
		{
		-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 
		0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
		0.0f, 0.0f, -1.0f, 1.0f, 1.0f, 
		0.0f, 0.0f, 0.0f, 1.0f, 0.0f
		};
		ColorMatrix invMatrix = new ColorMatrix(invert);
		final ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(invMatrix);
		Bitmap rBitmap = picture.copy(Bitmap.Config.ARGB_8888, true);
		Paint paint = new Paint();
		paint.setColorFilter(colorFilter);
		
		Canvas myCanvas =new Canvas(rBitmap);
		myCanvas.drawBitmap(rBitmap, 0, 0, paint);
		
		img.setImageBitmap(rBitmap);
	}
	
	//Back to original colors
	public void applyOriginal() {
		img.setImageBitmap(picture);
	}
}
