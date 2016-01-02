package com.segdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ObjectMap;
import com.segdx.game.SEGDX;
import com.segdx.game.states.LoadState;

public class StateManager {
	public static final String LOAD = "load_state";
	public static final String INTRO = "intro_state";
	public static final String MENU = "menu_state";
	public static final String GAME = "game_state";
	public static final String GAMEOVER = "gameover_state";
	
	private static StateManager sm;
	public static StateManager get(){
		if(sm==null){
			Gdx.app.log("ERROR:", "no instance of StateManager.class exists.");
			System.exit(0);
		}
		return sm;
	}
	public static StateManager create(SEGDX game){
		sm = new StateManager(game);
		return sm;
	}
	
	private SEGDX game;
	private ObjectMap<String, Screen> states;
	
	private StateManager(SEGDX game){
		this.game = game;
		this.states = new ObjectMap<String, Screen>();
	}
	
	public SEGDX getGame(){
		return game;
	}
	
	public void addState(String statename,Screen state){
		if(states.containsKey(statename))
			return;
		states.put(statename, state);
	}
	
	public void changeState(String state){
		if(!states.containsKey(state)){
			Gdx.app.log("ERROR:", "there is no trace of the "+state+" object, maybe you forgot to add it you made a type."
					+ "to prevent this from happening please use the StateManager CONSTANTS.");
			System.exit(0);
		}
		game.setScreen(states.get(state));
		LoadState.fired = false;
	}
	
	public void dispose(String state){
		if(!states.containsKey(state)){
			Gdx.app.log("ERROR:", "there is no trace of the "+state+" object, maybe you forgot to add it you made a type."
					+ "to prevent this from happening please use the StateManager CONSTANTS.");
			System.exit(0);
		}
		
		states.remove(state).dispose();
	}
	
	public Screen getState(String state){
		if(!states.containsKey(state)){
			Gdx.app.log("ERROR:", "there is no trace of the "+state+" object, maybe you forgot to add it you made a type."
					+ "to prevent this from happening please use the StateManager CONSTANTS.");
			System.exit(0);
		}
		return states.get(state);
	}
	

}
