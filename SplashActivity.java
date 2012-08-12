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

import com.pixelpixel.pyp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SplashActivity extends PypActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    /*
     * Setting up the animation for the logo
     * */
        final ImageView splash_logo = (ImageView) findViewById(R.id.SplashLogo);
        Animation sa1 = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        sa1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				/*
				 * After the end of the "growing" animation, we call the "shrinking" animation.
				 * */
				Animation sa2 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_anim2);
				sa2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
						/*
						 * After the end of the "shrinking" animation, we proceed to the
						 * next screen, the start screen.
						 * */
						Handler mHandler = new Handler();
						mHandler.postDelayed(new Runnable() {
							public void run() {
								startActivity(new Intent(SplashActivity.this, StartActivity.class));
								SplashActivity.this.finish();
					        }
					    }, 5000);	
					}

					@Override
					public void onAnimationRepeat(Animation animation) {}

					@Override
					public void onAnimationStart(Animation animation) {}
					
				});
				splash_logo.startAnimation(sa2);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {}
        	
        });
        
        splash_logo.startAnimation(sa1);
    }
    /*
     * In the case of pausing the app at the splash screen, we have to clear the animations.
     * */
    public void onPause() {
    	super.onPause();
    	ImageView logo = (ImageView) findViewById(R.id.SplashLogo);
    	logo.clearAnimation();
    }
}