package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.segdx.game.SEGDX;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.tween.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class IntroState implements Screen{
	
	private SpriteBatch batch;
	private Sprite kyperbeltlogo;
	private OrthographicCamera cam;
	private TweenManager tm;

	@Override
	public void show() {
		batch = new SpriteBatch();
		kyperbeltlogo = new Sprite(Assets.manager.get("kyperbeltlogo.png",Texture.class));
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		kyperbeltlogo.setOriginCenter();
		kyperbeltlogo.setAlpha(0);
		cam.setToOrtho(false);
		cam.position.set(kyperbeltlogo.getOriginX(), kyperbeltlogo.getOriginY(), 0);
		tm = new TweenManager();
		
		SoundManager.get().playMusicList();
		Tween.to(kyperbeltlogo, SpriteAccessor.ALPHA, 1.5f).target(1).repeatYoyo(1, 1f).setCallback(
				new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> arg1) {
						if(type == TweenCallback.COMPLETE){
							Assets.loadBlock(Assets.MENU_ASSETS);
							StateManager.get().changeState(StateManager.LOAD);
						}
					}
				}).start(tm);
		
	}

	@Override
	public void render(float delta) {
		tm.update(delta);
		SEGDX.clear();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		kyperbeltlogo.draw(batch);
		batch.end();
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
		Assets.disposeBlock(Assets.INTRO_ASSETS);
	}

}
