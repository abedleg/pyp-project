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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.LruCache;

public class PypActivity extends Activity{

	public static final String APP_PREFS = "AppPrefs";
	public static Uri originalUri;
	public static int chosenEffect = 0;
	public static LruCache<String, Object> mMemoryCache;
}
