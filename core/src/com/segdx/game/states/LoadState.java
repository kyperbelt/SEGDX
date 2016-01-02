package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.segdx.game.SEGDX;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.tween.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class LoadState implements Screen{
	private static final float LOAD_DELAY = .1f;
	public static boolean fired = false;
	
	private SpriteBatch batch;
	private Sprite loading;
	private OrthographicCamera cam;
	private TweenManager tm;
	private boolean paused;
	@Override
	public void show() {
		paused = false;
		batch = new SpriteBatch();
		loading = new Sprite(Assets.manager.get("loading.png", Texture.class));
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		tm = new TweenManager();
		
		
		Tween.to(loading, SpriteAccessor.ALPHA, .5f).target(0).repeatYoyo(200, .2f).start(tm);
		
	}

	@Override
	public void render(float delta) {
		
		SEGDX.clear();
	    batch.setProjectionMatrix(cam.combined);
		batch.begin();
		loading.draw(batch);
		batch.end();
		tm.update(delta);
		cam.update();
		
		if(paused==true)
			return;
		if(Assets.manager.update()&&!fired){
			fired = true;
			switch (Assets.currentload) {
			case Assets.INTRO_ASSETS:
				Timer.schedule(new Task() {
					
					@Override
					public void run() {
						StateManager.get().changeState(StateManager.INTRO);
					}
				}, LOAD_DELAY);
				
				break;
			case Assets.MENU_ASSETS:
				Timer.schedule(new Task() {
					
					@Override
					public void run() {
						StateManager.get().changeState(StateManager.MENU);
					}
				}, LOAD_DELAY);
				break;
			case Assets.GAME_ASSETS:
				Timer.schedule(new Task() {
					
					@Override
					public void run() {
						GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
						state.setLoreEnabled(SpaceMap.intToBool(state.slore));
						state.setMap(SpaceMap.generateSpaceMap(state.size, state.piracy, state.draft, state.difficulty));
						StateManager.get().changeState(StateManager.GAME);
					}
				}, LOAD_DELAY);
				break;
			case Assets.GAMEOVER_ASSETS:
				Timer.schedule(new Task() {
					
					@Override
					public void run() {
						StateManager.get().changeState(StateManager.GAMEOVER);
						SoundManager.get().playMusic(SoundManager.BOARDINGPARTY);
					}
				}, LOAD_DELAY);
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		paused = true;
		tm.pause();
	}

	@Override
	public void resume() {
		paused = false;
		tm.resume();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		System.out.println("disposed "+StateManager.GAME);
		batch.dispose();
		Assets.disposeBlock(Assets.LOAD_ASSETS);
	}

}
