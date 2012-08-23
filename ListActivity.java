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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends PypActivity{

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ListView list = (ListView)findViewById(R.id.EffectsList);
		String[] values = new String[] {"Original", "GrayScale", "Sepia", 
										"Inverse", "Darkening", "Sweet dream", 
										"Vivid"};
		ArrayAdapter<String> adapter = 
			new ArrayAdapter<String>(this, R.layout.list_item, values);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//We click on an item, it marks the type of effect
				//Then we finish() and call onDestroy(), which sends the type of effect
				//to the MainActivity, where the onRestoreInstanceState() method 
				//is called, and we set the appropriate effect.
				TextView text = (TextView)view;
				String strText = text.getText().toString();
				//Intent backToMain = new Intent(ListActivity.this, MainActivity.class);
				if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_original))) {
					//backToMain.putExtra("chosenEffect", 1);
					chosenEffect = 1;
				}else if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_grayscale))){
					//backToMain.putExtra("chosenEffect", 2);
					chosenEffect = 2;
				}else if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_sepia))){
					//backToMain.putExtra("chosenEffect", 3);
					chosenEffect = 3;
				}else if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_inverse))){
					//backToMain.putExtra("chosenEffect", 4);
					chosenEffect = 4;
				}else if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_darkening))){
					//backToMain.putExtra("chosenEffect", 4);
					chosenEffect = 5;
				}else if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_dreams))){
					//backToMain.putExtra("chosenEffect", 4);
					chosenEffect = 6;
				}else if (strText.equalsIgnoreCase(getResources().getString(R.string.effects_vivid))){
					//backToMain.putExtra("chosenEffect", 4);
					chosenEffect = 7;
				}
				//startActivity(backToMain);
				finish();
			}
			
		});
	}
}

