package com.segdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameOverState;
import com.segdx.game.states.GameState;
import com.segdx.game.states.IntroState;
import com.segdx.game.states.LoadState;
import com.segdx.game.states.MenuState;
import com.segdx.game.tween.CameraAccessor;
import com.segdx.game.tween.PlayerAccessor;
import com.segdx.game.tween.SpriteAccessor;
import com.segdx.game.tween.TableAccessor;

import aurelienribon.tweenengine.Tween;
//TODO: enemy spawns if certain amount of wealth is accumulated. 
//TODO: make the resources in the haul tab stack
//TODO: add statistics tracking
//TODO: complete achievements

public class SEGDX extends Game {
	public static final boolean DEBUG = false;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public float vol;
	public Kryo kryo;
	@Override
	public void create () {
		
		ResourceStash.init();
		Assets.create();
	    kryo = new Kryo();
		kryo.register(Stats.class);
		kryo.register(AchievementManager.class);
		kryo.register(Achievement.class);
		
		Stats.init(Stats.load(kryo));
		AchievementManager.init(AchievementManager.load(kryo));
		
		//REGISTER TWEEN accessors
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
		Tween.registerAccessor(Player.class, new PlayerAccessor());
		Tween.registerAccessor(Table.class, new TableAccessor());
		
		//create states
		StateManager sm = StateManager.create(this);
		sm.addState(StateManager.LOAD, new LoadState());
		sm.addState(StateManager.INTRO, new IntroState());
		sm.addState(StateManager.MENU, new MenuState());
		sm.addState(StateManager.GAME, new GameState());
		sm.addState(StateManager.GAMEOVER, new GameOverState());
		
		//load loadingscreen assets
		Assets.loadBlock(Assets.LOAD_ASSETS);
		while(!Assets.manager.update()){
			
		}
		Pixmap pm = new Pixmap(Gdx.files.internal("ui/pointer.png"));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, (int) (pm.getHeight()*.2)));
		pm.dispose();
		//set intro assets to load queue
		Assets.loadBlock(Assets.INTRO_ASSETS);
		//change state to load state
		sm.changeState(StateManager.LOAD);
		
		
	}
	
	public static void clear(){
		Gdx.gl.glClearColor(0, 0, 0, 1 );
	    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
	}
	
	@Override
	public void pause() {
		vol = SoundManager.get().getVolume();
		SoundManager.get().setVolume(0);
		super.pause();
	}
	
	@Override
	public void resume() {
		SoundManager.get().setVolume(vol);
		super.resume();
	}
	
	@Override
	public void dispose() {
		Stats.save(kryo);
		AchievementManager.save(kryo);
		super.dispose();
	}

}
