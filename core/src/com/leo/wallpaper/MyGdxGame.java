package com.leo.wallpaper;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	WallpaperHandling wallpaperHandling;

	MyGdxGame(WallpaperHandling wallpaperHandling) {
		super();
		this.wallpaperHandling = wallpaperHandling;
	}

	@Override
	public void create () {
		setScreen(new LiveWallpaperScreen(this));
	}
}
