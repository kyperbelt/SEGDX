package com.segdx.game.managers;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
	public static final int LOAD_ASSETS = 0;
	public static final int INTRO_ASSETS = 1;
	public static final int MENU_ASSETS = 2;
	public static final int GAME_ASSETS = 3;
	
	public static final AssetManager manager = new AssetManager();
	public static int currentload =-1;
	public static void loadBlock(int blockid){
		currentload = blockid;
		switch (blockid) {
		case LOAD_ASSETS:
			
			break;
		case INTRO_ASSETS:
			
			break;
		case MENU_ASSETS:
			
			break;
		case GAME_ASSETS:
			
			break;

		default:
			assert false;
			break;
		}
		
	}
	
	public static void disposeBlock(int blockid){
		
	}

}
