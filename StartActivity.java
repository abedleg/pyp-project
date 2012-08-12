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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class StartActivity extends PypActivity {
	
	static final int CAMERA_REQUEST = 1;
	static final int GALLERY_REQUEST = 2;
	final String TAG = "Start Activity";
	File mediaFile;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        ImageButton camImg = (ImageButton)findViewById(R.id.startCamImg);
        ImageButton galImg = (ImageButton)findViewById(R.id.startGalImg);
        
        camImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Launch the camera and save the photo to manipulate
				//Have to make sure that the file exists before taking the picture
				//or else some types of android phones will be create a bug after
				//taking the picture.
				
				Intent pictureIntent = 
					new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//Get the picture taken
				Uri fileUri = Uri.fromFile(getOutputMediaFile());
				camPicUri = fileUri;
				//Send the picture with the intent
				pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(pictureIntent, CAMERA_REQUEST);
			}
		});
        
        galImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Launch the gallery to select an image to manipulate
				Intent pickPhoto = new Intent(Intent.ACTION_PICK);
				pickPhoto.setType("image/*");
				startActivityForResult(pickPhoto, GALLERY_REQUEST);
			}
		});
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode) {
    	case CAMERA_REQUEST:
    		if (resultCode == Activity.RESULT_CANCELED) {
    			//CAMERA MODE CANCELLED
    		}else if (resultCode == Activity.RESULT_OK) {
    			Intent toMain = new Intent(StartActivity.this, MainActivity.class);
    			//NEXT LINE IS WRONG!!!!
    			toMain.putExtra("cPic", camPicUri);
    			startActivity(toMain);
    		}
    		break;
    	case GALLERY_REQUEST:
    		if (resultCode == Activity.RESULT_CANCELED) {
    			//GALLERY MODE CANCELLED
    		}else if (resultCode == Activity.RESULT_OK) {
    			Intent toMain = new Intent(StartActivity.this, MainActivity.class);
    			toMain.putExtra("gPic", data.getData());
    			startActivity(toMain);   			
    		}
    		break;
    	}
    }
    
    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
    	File mediaStorageDir;
    	if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
    		mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Pimped pictures");
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("Pimp my picture", "failed to create directory");
                    return null;
                }
            }
    	}else{
    		new AlertDialog.Builder(StartActivity.this).setMessage("External media storage is needed.").setCancelable(true).create().show();
    		Log.d("Pimp my picture", "external media not found or not writable");
    		return null;
    	}
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       
        mediaFile = new File(mediaStorageDir.getPath() + File.separatorChar +
            "PYPic_"+ timeStamp + ".jpg");
        return mediaFile;
    }
    
    
}