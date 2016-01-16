package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.tween.CameraAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class MenuState implements Screen{
	
	private static final boolean DEBUG = SEGDX.DEBUG; 
	
	private SpriteBatch batch;
	private TweenManager tm;
	private OrthographicCamera cam;
	private Sprite titlesprite;
	private TextButton play,credits,startgame,back;
	private Table main,setup;
	private Stage stage;
	private Skin skin;
	private ButtonGroup<TextButton> mapsizes;
	private ButtonGroup<TextButton> piracy;
	private ButtonGroup<TextButton> draft;
	private ButtonGroup<TextButton> lore;
	private ButtonGroup<TextButton> difficulty;
	

	@Override
	public void show() {
		batch = new SpriteBatch();
		tm = new TweenManager();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		titlesprite = new Sprite(Assets.manager.get("ui/title.png",Texture.class));
		titlesprite.setOriginCenter();
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		//initiate stage with my own camera
		cam.position.set(titlesprite.getOriginX(), titlesprite.getOriginY(), 0);
		stage = new Stage(new ScreenViewport(cam), batch);
		Gdx.input.setInputProcessor(stage);
		
		//create the main menu
		main = new Table();
		main.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		main.center();
		play = new TextButton("Play", skin);
		play.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				startgame.setDisabled(true);
				back.setDisabled(true);
				Tween.to(cam, CameraAccessor.POSITION_X, 1).target(setup.getX()+(setup.getWidth()/2)).setCallback(
						new TweenCallback() {
							
							@Override
							public void onEvent(int type, BaseTween<?> arg1) {
								if(type == TweenCallback.COMPLETE){
									startgame.setDisabled(false);
									back.setDisabled(false);
								}
							}
						}).start(tm);
				return true;
			}
		});
		//play.setSize(300, 90);
		main.add(play).width(300).height(90);
		setup = new Table();
		setup.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		setup.setPosition(main.getX()+setup.getWidth()+10, main.getY());
		setup.center();
		
		//create setup ui
		Table headercontainer = new Table();
		headercontainer.center();
		Label setupheader = new Label("SETUP", skin);
		setupheader.setFontScale(2);
		headercontainer.add(setupheader).fill();
		
		HorizontalGroup sizecontainer = new HorizontalGroup();
		sizecontainer.align(Align.left);
		Label sizelabel = new Label("Map Size:",skin);
		sizecontainer.addActor(sizelabel);
		TextButton smallsize = new TextButton("small", skin,"toggle");
		TextButton mediumsize = new TextButton("medium",skin,"toggle");
		TextButton largesize = new TextButton("large",skin,"toggle");
		mapsizes = new ButtonGroup<TextButton>(smallsize,mediumsize,largesize);
		mapsizes.setMaxCheckCount(1);
		mapsizes.setMinCheckCount(1);
		mapsizes.setChecked("small");
		sizecontainer.addActor(smallsize);
		sizecontainer.addActor(mediumsize);
		sizecontainer.addActor(largesize);
		
		HorizontalGroup piracycontainer = new HorizontalGroup();
		piracycontainer.align(Align.left);
		Label piracylabel = new Label("Piracy:",skin);
		piracycontainer.addActor(piracylabel);
		TextButton piracyenabled = new TextButton("enabled", skin,"toggle");
		piracyenabled.setChecked(true);
		TextButton piracydisabled = new TextButton("disabled", skin,"toggle");
		piracy = new ButtonGroup<TextButton>(piracydisabled,piracyenabled);
		piracy.setMaxCheckCount(1);
		piracy.setMinCheckCount(1);
		piracycontainer.addActor(piracydisabled);
		piracycontainer.addActor(piracyenabled);
		
		HorizontalGroup draftcontainer = new HorizontalGroup();
		draftcontainer.align(Align.left);
		Label draftlabel = new Label("Draft:",skin);
		draftcontainer.addActor(draftlabel);
		TextButton draftenabled = new TextButton("enabled", skin,"toggle");
		draftenabled.setChecked(true);
		TextButton draftdisabled = new TextButton("disabled", skin,"toggle");
		draft = new ButtonGroup<TextButton>(draftdisabled,draftenabled);
		draft.setMaxCheckCount(1);
		draft.setMinCheckCount(1);
		draftcontainer.addActor(draftdisabled);
		draftcontainer.addActor(draftenabled);
		
		HorizontalGroup lorecontainer = new HorizontalGroup();
		lorecontainer.align(Align.left);
		Label lorelabel = new Label("Lore:",skin);
		lorecontainer.addActor(lorelabel);
		TextButton loreenabled = new TextButton("enabled", skin,"toggle");
		loreenabled.setChecked(true);
		TextButton loredisabled = new TextButton("disabled", skin,"toggle");
		lore = new ButtonGroup<TextButton>(loredisabled,loreenabled);
		lore.setMaxCheckCount(1);
		lore.setMinCheckCount(1);
		lorecontainer.addActor(loredisabled);
		lorecontainer.addActor(loreenabled);
		
		HorizontalGroup difficultycontainer = new HorizontalGroup();
		Label difficultylabel = new Label("Difficulty:", skin);
		difficultycontainer.addActor(difficultylabel);
		TextButton easy = new TextButton("easy", skin, "toggle");
		easy.setChecked(true);
		TextButton normal = new TextButton("normal", skin, "toggle");
		TextButton hard = new TextButton("hard", skin, "toggle");
		difficulty = new ButtonGroup<TextButton>(easy,normal,hard);
		difficulty.setMaxCheckCount(1);
		difficulty.setMinCheckCount(1);
		difficultycontainer.addActor(easy);
		difficultycontainer.addActor(normal);
		difficultycontainer.addActor(hard);
		
		
		Table optioncontainer = new Table();
		optioncontainer.center();
		
		startgame = new TextButton("START", skin);
		startgame.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Assets.loadBlock(Assets.GAME_ASSETS);
				GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
				state.size = mapsizes.getCheckedIndex();
				state.piracy = piracy.getCheckedIndex();
				state.draft = draft.getCheckedIndex();
				state.slore = lore.getCheckedIndex();
				state.difficulty = difficulty.getCheckedIndex();
				
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				StateManager.get().changeState(StateManager.LOAD);
				SoundManager.get().skipToNextMusic();
				return true;
			}
		});
		back = new TextButton("BACK", skin);
		back.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				Tween.to(cam, CameraAccessor.POSITION_X, 1).target(main.getX()+(main.getWidth()/2)).start(tm);
				return true;
			}
		});
		
		HorizontalGroup startcontainer = new HorizontalGroup();
		startcontainer.align(Align.right);
		startcontainer.addActor(startgame);
		
		HorizontalGroup backcontainer = new HorizontalGroup();
		backcontainer.align(Align.left);
		backcontainer.addActor(back);
		
		optioncontainer.add(backcontainer).width(setup.getWidth()/2).height(setup.getHeight()*.1f);
		optioncontainer.add(startcontainer).width(setup.getWidth()/2).height(setup.getHeight()*.1f);
		
		
		
		
		setup.add(headercontainer).width(300).height(setup.getHeight()*.2f).row();;
		setup.add(sizecontainer).width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		setup.add(piracycontainer).width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		setup.add(draftcontainer).width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		setup.add(lorecontainer).width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		setup.add(difficultycontainer).width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		setup.add().width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		setup.add(optioncontainer).width(setup.getWidth()).height(setup.getHeight()*.1f).row();
		
		
		
		//add tables to stage
		stage.addActor(main);
		stage.addActor(setup);
		
		
		//debug 
		if(DEBUG){
			main.debug();
			setup.debug();
		}
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
		tm.update(delta);
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
