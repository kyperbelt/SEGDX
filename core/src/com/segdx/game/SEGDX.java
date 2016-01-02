package com.segdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.segdx.game.entity.Player;
import com.segdx.game.managers.Assets;
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

public class SEGDX extends Game {
	public static final boolean DEBUG = false;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	@Override
	public void create () {
		
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
		Assets.manager.finishLoading();
		//set intro assets to load queue
		Assets.loadBlock(Assets.INTRO_ASSETS);
		//change state to load state
		sm.changeState(StateManager.LOAD);
		
		
	}
	
	public static void clear(){
		Gdx.gl.glClearColor(0, 0, 0, 1 );
	    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
	}

}
