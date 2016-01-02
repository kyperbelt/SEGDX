package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;

public class GameOverState implements Screen{
	
	private Stage gostage;
	private Skin skin;
	private Sprite background;

	@Override
	public void show() {

		((GameState)StateManager.get().getState(StateManager.GAME)).disposeMap();
		
		gostage = new Stage(new ScreenViewport());
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		background = new Sprite(Assets.manager.get("map/godsandidols3.png",Texture.class));
		
		Gdx.input.setInputProcessor(gostage);
		
	}

	@Override
	public void render(float delta) {
		SEGDX.clear();
		
		gostage.getBatch().begin();
		background.draw(gostage.getBatch());
		gostage.getBatch().end();
		
		gostage.draw();
		gostage.act(delta);
		gostage.getCamera().update();
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
	}

}
