package com.segdx.game.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public static final int LOAD_ASSETS = 0;
	public static final int INTRO_ASSETS = 1;
	public static final int MENU_ASSETS = 2;
	public static final int GAME_ASSETS = 3;
	public static final int GAMEOVER_ASSETS = 4;
	
	public static final AssetManager manager = new AssetManager();
	public static int currentload =-1;
	public static void loadBlock(int blockid){
		currentload = blockid;
		switch (blockid) {
		case LOAD_ASSETS:
				manager.load("loading.png",Texture.class);
			break;
		case INTRO_ASSETS:
				manager.load(SoundManager.BLINDSHIFT,Music.class);
				manager.load(SoundManager.STARLIGHT,Music.class);
				manager.load(SoundManager.OPTIONPRESSED,Sound.class);
				manager.load("kyperbeltlogo.png",Texture.class);
				manager.load("poweredbylibgdx.png",Texture.class);
			break;
		case MENU_ASSETS:
				manager.load("title.png",Texture.class);
				Assets.manager.load("ui/uiskin.atlas", TextureAtlas.class);
		        Assets.manager.load("ui/uiskin.json", Skin.class, new SkinLoader.SkinParameter("ui/uiskin.atlas"));
		        
			break;
		case GAME_ASSETS:
				manager.load(SoundManager.BOARDINGPARTY,Music.class);
				manager.load(SoundManager.NODESELECT,Sound.class);
				
				//nodes
				manager.load("map/bluenode.png",Texture.class);
				manager.load("map/rednode.png",Texture.class);
				manager.load("map/greennode.png",Texture.class);
				manager.load("map/passivenode.png",Texture.class);
				
				//ships
				manager.load("map/shuttle.png",Texture.class);
				manager.load("map/mother2b.png",Texture.class);
				manager.load("map/mothershipbu7.png",Texture.class);
				manager.load("map/ship2.png",Texture.class);
				manager.load("map/redshipr.png",Texture.class);
				manager.load("map/spaceship1.png",Texture.class);
				
				//effects
				manager.load("map/sprshield.png",Texture.class);
				
				//ui icons
				manager.load("map/foodicon.png",Texture.class);
				manager.load("map/fuelicon.png",Texture.class);
				manager.load("map/currencyicon.png",Texture.class);
				manager.load("map/hullicon.png",Texture.class);
				
				
				//backgrounds
				manager.load("map/m42orionnebula.png",Texture.class);
				
				//ore and item icons
				manager.load("map/dridium.png",Texture.class);
				manager.load("map/latterium.png",Texture.class);
				manager.load("map/naquidra.png",Texture.class);
				manager.load("map/kniptoryte.png",Texture.class);
				manager.load("map/module.png",Texture.class);
				
			break;
		case GAMEOVER_ASSETS:
				manager.load("map/godsandidols3.png",Texture.class);
			break;
		default:
			assert false;
			break;
		}
		
	}
	
	public static void disposeBlock(int blockid){
		
	}

}
