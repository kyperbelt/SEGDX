package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.SEGDX;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.InputManager;
import com.segdx.game.managers.StateManager;

public class GameState implements Screen{

	private SpaceMap map;
	private boolean lore;
	private Stage uistage;
	private InputManager input;	
	
	
	public int size;
	public int piracy;
	public int draft;
	public int slore;
	public int difficulty;
	
	@Override
	public void show() {
		if(map==null){
			Gdx.app.log("ERROR:", "An instance of the class "+SpaceMap.class+" has not yet been created!");
			System.exit(0);
		}
		uistage = new Stage();
		input = new InputManager();
		for (int i = 0; i < map.getAllnodes().size; i++) {
			map.getAllnodes().get(i).getButton().addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event,Actor actor) {System.out.println(" pressed");
					GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
					SpaceMap map = state.getSpaceMap();
					ButtonGroup<ImageButton> nodebuttons = map.getNodebuttons();
					Array<ImageButton> buttons = nodebuttons.getAllChecked();
					for (int i = 0; i < buttons.size; i++) {
							if(i!=buttons.size-1){
								buttons.get(i).setSize(16, 16);
								buttons.get(i).setChecked(false);
							}else{
								buttons.get(i).setChecked(true);
								buttons.get(i).setSize(32, 32);
							}
					}
					
					
				}
			});
		}
		map.getStage().setDebugAll(SEGDX.DEBUG);
		Gdx.input.setInputProcessor(map.getStage());
	}
	
	public void setLoreEnabled(boolean lore){
		this.lore = lore;
	}
	
	public boolean isLoreEnabled(){
		return lore;
	}

	@Override
	public void render(float delta) {
		SEGDX.clear();
		map.getStage().act();
		map.getStage().draw();
		map.getCam().update();
		
	}
	
	public void setMap(SpaceMap map){
		this.map = map;
	}
	
	public SpaceMap getSpaceMap(){
		return map;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		//batch.dispose();
		Assets.disposeBlock(Assets.GAME_ASSETS);
	}

}
