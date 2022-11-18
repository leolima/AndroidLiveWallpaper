package com.leo.wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AndroidWallpaper implements WallpaperHandling  {
    Context context;
    AndroidWallpaper(Context context){
        this.context = context;
    }
    @Override
    public void askSetWallpaper() {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

        // set the wallpaper by calling the setResource function and
        // passing the drawable file

        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, Service.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);
        context.startActivity(intent);
    }
}
