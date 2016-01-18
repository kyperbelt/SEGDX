package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.SpaceNode;
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
	private Image background;
	private TextButton play,options,achievements,credits,startgame,back;
	private Table main,setup,settings,statistics,creditpage,holder;
	private Stage stage;
	private Skin skin;
	private ButtonGroup<TextButton> mapsizes;
	private ButtonGroup<TextButton> piracy;
	private ButtonGroup<TextButton> draft;
	private ButtonGroup<TextButton> lore;
	private ButtonGroup<TextButton> difficulty;
	
	public TextButton statstab,achievestab;
	

	@Override
	public void show() {
		batch = new SpriteBatch();
		tm = new TweenManager();
		background = new Image(Assets.manager.get("ui/title.png",Texture.class));
		
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		skin.get("default-font", BitmapFont.class).getData().markupEnabled = true;
		//initiate stage with my own camera
		stage = new Stage(new ScreenViewport(), batch);
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
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_X, 1).target(setup.getX()+(setup.getWidth()/2)).setCallback(
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
		
		options = new TextButton("Options", skin);
		options.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				startgame.setDisabled(true);
				back.setDisabled(true);
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_X, 1).target(settings.getX()+(settings.getWidth()/2)).setCallback(
						new TweenCallback() {
							
							@Override
							public void onEvent(int type, BaseTween<?> arg1) {
								if(type == TweenCallback.COMPLETE){
									startgame.setDisabled(false);
									back.setDisabled(false);
								}
							}
						}).start(tm);
			}
		});
		
		achievements = new TextButton("Achievements", skin);
		achievements.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				startgame.setDisabled(true);
				back.setDisabled(true);
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_Y, 1).target(statistics.getY()+(statistics.getHeight()/2)).setCallback(
						new TweenCallback() {
							
							@Override
							public void onEvent(int type, BaseTween<?> arg1) {
								if(type == TweenCallback.COMPLETE){
									startgame.setDisabled(false);
									back.setDisabled(false);
								}
							}
						}).start(tm);
			}
		});
		
		credits = new TextButton("Credits", skin);
		credits.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				startgame.setDisabled(true);
				back.setDisabled(true);
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_Y, 1).target(creditpage.getY()+(creditpage.getHeight()/2)).setCallback(
						new TweenCallback() {
							
							@Override
							public void onEvent(int type, BaseTween<?> arg1) {
								if(type == TweenCallback.COMPLETE){
									startgame.setDisabled(false);
									back.setDisabled(false);
								}
							}
						}).start(tm);
			}
		});
		
		//play.setSize(300, 90);
		main.add(background).expand().colspan(2).row();
		main.add(achievements).width(200).height(80).expand().colspan(2).row();
		main.add(options).width(200).height(80).expand();
		main.add(play).width(200).height(80).expand().row();
		main.add(credits).width(200).height(80).expand().colspan(2).row();
		main.add().colspan(2).expand().fill();
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
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_X_Y, 1).target(main.getX()+(main.getWidth()/2),main.getY()+(main.getHeight()/2)).start(tm);
				return true;
			}
		});
		
		TextButton back2 = new TextButton("BACK", skin);
		back2.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_X_Y, 1).target(main.getX()+(main.getWidth()/2),main.getY()+(main.getHeight()/2)).start(tm);
				return true;
			}
		});
		
		TextButton back3 = new TextButton("BACK", skin);
		back3.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_X_Y, 1).target(main.getX()+(main.getWidth()/2),main.getY()+(main.getHeight()/2)).start(tm);
				return true;
			}
		});
		
		TextButton back4 = new TextButton("BACK", skin);
		back4.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
				Tween.to(stage.getCamera(), CameraAccessor.POSITION_X_Y, 1).target(main.getX()+(main.getWidth()/2),main.getY()+(main.getHeight()/2)).start(tm);
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
		
		statistics = new Table();
		statistics.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		statistics.setPosition(main.getX(), main.getY()+statistics.getHeight()+10);
		statistics.center();
		
		holder = new Table();
		holder.add(this.achievementsTable(skin,AchievementManager.get())).expand().fill();
		
		achievestab = new TextButton("Achievements", skin,"toggle");
		achievestab.addListener(new ClickListener(){@Override
			public void clicked(InputEvent event, float x, float y) {
				holder.clearChildren();
				holder.add(achievementsTable(skin,AchievementManager.get())).expand().fill();
			}});
		statstab = new TextButton("Statistics", skin ,"toggle");
		statstab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				holder.clearChildren();
				holder.add(getStatsTable(skin, Stats.get())).expand().fill();
			}
		});
		
		ButtonGroup<TextButton> statistictabs = new ButtonGroup<TextButton>(achievestab,
			statstab);
		statistictabs.setMaxCheckCount(1);
		statistictabs.setChecked("Achievements");
		
		Table actionbartabstable = new Table();
		actionbartabstable.left();
		actionbartabstable.add(achievestab,statstab);
		
		statistics.add(actionbartabstable).align(Align.topLeft).expandX().row();
		statistics.add(holder).expand().fill().row();
		statistics.add(back2);
		
		
		settings = new Table();
		settings.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		settings.setPosition(main.getX()-settings.getWidth()+10, main.getY());
		settings.left();
		settings.add(getMenuTable(skin)).left().expand();
		settings.add(back3).expand();
		
		
		creditpage = new Table();
		creditpage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		creditpage.setPosition(main.getX(), main.getY()-(creditpage.getHeight()+10));
		creditpage.bottom();
		creditpage.add(back4).expand().row();;
		creditpage.add(getCreditsTable(skin)).left().expand().fill();
		
		
		//add tables to stage
		stage.addActor(main);
		stage.addActor(statistics);
		stage.addActor(setup);
		stage.addActor(settings);
		stage.addActor(creditpage);
		
		
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
		
		stage.draw();
		
		stage.getCamera().update();
		tm.update(delta);
	}
	
	public Table getStatsTable(Skin skin,Stats s){
		Table table = null;
		if(table == null){
			table = new Table();
			table.setBackground(new Button(skin).getBackground());
			table.setColor(Color.DARK_GRAY);
			Label statsshow = new Label("Statistics", skin);
			table.add(statsshow).expand().row();
			
			Table holder = new Table();
			
			for (int i = 0; i < s.stattable.size; i++) {
				
				String name = s.getStatName(i);
				int value = s.getStatValue(i);
				Label stat  = new Label(name+": "+value, skin);
				stat.setFontScale(.7f);
				holder.add(stat).left().expand().row();
			}
			
			ScrollPane pane = new ScrollPane(holder, skin);
			pane.setColor(Color.FIREBRICK);
			table.add(pane).expand().fill();
		}
		return table;
	}
	
	public Table achievementsTable(Skin skin ,AchievementManager a){
	    Table holderss = null;
		if(holderss==null){
			holderss = new Table();
			holderss.setBackground(new Button(skin).getBackground());
			Label points = new Label("Points "+a.getPoints()+"/"+a.getTotalPoints(), skin);
			Table achieveholder = new Table();
			//achieveholder.setFillParent(true);
			int index;
			for (int i = 0; i < a.game_achievements.size; i+=2) {
				Achievement aa = a.getAchievementValue(i);
				Achievement b = a.getAchievementValue(i+1);
				achieveholder.add(a.getAchievementTableDos(skin, aa)).expand().fillX();
				achieveholder.add(a.getAchievementTableDos(skin, b)).expand().fillX().row();
			}
			if(a.game_achievements.size%2!=0){
				Achievement aa = a.game_achievements.get(a.getAchievementName(a.game_achievements.size));
				achieveholder.add(a.getAchievementTableDos(skin, aa)).expand().row();
			}
		
			holderss.add(points).expand().row();
			ScrollPane pane = new ScrollPane(achieveholder,skin);
			pane.layout();
			pane.setColor(Color.BROWN);
			holderss.add(pane).expand().fill();
		}
		return holderss;
	}
	
	public Table getMenuTable(Skin skin){
		Table table = new Table();
		Label ml = new Label("Game Options", skin);
		ml.setFontScale(3);
		Label volume = new Label("Volume:", skin);
		final Slider volslide = new Slider(0, 1, .01f, false, skin);
		volslide.setValue(SoundManager.get().getVolume());
		volslide.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				SoundManager.get().setVolume(volslide.getValue());
			}
		});
		
		table.add(ml).left().expand().row();
		final TextButton fullscreen = new TextButton("fullscreen", skin,"toggle");
		fullscreen.setChecked(Gdx.graphics.isFullscreen());
		fullscreen.addListener(new ClickListener(){@Override
		public void clicked(InputEvent event, float x, float y) {
			Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fullscreen.isChecked());
		}});
		table.add(volume).left().row();;
		table.add(volslide).left().expand().row();
		table.add(fullscreen).left().expand().row();
		
		return table;
	}
	
	public Table getCreditsTable(Skin skin){
		Table table = new Table();
		Label cl = new Label("Credits", skin);
		cl.setFontScale(3);
		Label gheader  = new Label("Graphics", skin);
		Label graphics = new Label("	[RED]MOST OF THESE GRAPHICS WERE RESIZED OR TRANSFORMED TO FIT THE GAME.[]\n"
				+ "shuttlespaceship: kindlyfire opengameart.org\nm42orionnebula google images.\n"
				+ "Game Post Mortem:Hard Vacuum art by Daniel Cook (Lostgarden.com)\n\n		-mother2.bmp"
				+"						Spaceship by phobi opengameart.org\n"
					+"					mothershipbu7.png by clayster2012 opengameart.org\n"
						+"				redshipr.png by MillionthVector http://millionthvector.blogspot.de opengameart.org\n"
							+"			Gods-and-idols_screenshots jattenalle opengameart.org  www.GodsAndIdols.com\n"
								+"		spr_shield.png by bonsaiheldin opengameart.org\n"
									+"	part2art by Skorpio (original parts) and Wubitog.  part2art.com  arrall.com opengameart.org\n"
							+"			\n\n"
								+"		pointer by qubodub opengamart.org\n", skin);
		graphics.setFontScale(.5f);
		graphics.setWrap(true);
		Label soundheader = new Label("Audio", skin);
		Label sound = new Label("Blind Shift by PetterTheSturgeon opengameart.org\nStar Light by Eric Matyas opengameart.org\nCrazy Space by rubberduck opengameart.org\nBoarding Party by Trevor Lentz opengameart.org\n", skin);
		sound.setFontScale(.5f);
		sound.setWrap(true);
		table.add(cl).left().expand().fillX().row();
		table.add(gheader).left().expand().fillX().row();
		table.add(graphics).left().expand().fillX().row();
		table.add(soundheader).left().fillX().expand().row();
		table.add(sound).left().expand().fillX().row();
		return table;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		tm.pause();
	}

	@Override
	public void resume() {
		tm.resume();
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
