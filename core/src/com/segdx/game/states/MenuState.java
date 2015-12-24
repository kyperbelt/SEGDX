package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.managers.Assets;

import aurelienribon.tweenengine.TweenManager;

public class MenuState implements Screen{
	
	private SpriteBatch batch;
	private TweenManager tm;
	private OrthographicCamera cam;
	private Sprite titlesprite;
	private TextButton play,exit;
	private Table table;
	private Stage stage;
	private Skin skin;

	@Override
	public void show() {
		batch = new SpriteBatch();
		tm = new TweenManager();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		titlesprite = new Sprite(Assets.manager.get("title.png",Texture.class));
		titlesprite.setOriginCenter();
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		cam.position.set(titlesprite.getOriginX(), titlesprite.getOriginY(), 0);
		stage = new Stage(new ScreenViewport(cam), batch);
		Gdx.input.setInputProcessor(stage);
		play = new TextButton("Play", skin);
		play.setSize(300, 90);
		play.setPosition(titlesprite.getOriginX(), titlesprite.getOriginY());
		stage.addActor(play);
		
		
	}

	@Override
	public void render(float delta) {
		SEGDX.clear();
		stage.act(delta);
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		titlesprite.draw(batch);
		batch.end();
		stage.draw();
		
		cam.update();
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
		batch.dispose();
		Assets.disposeBlock(Assets.MENU_ASSETS);
	}

}
