package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.InputManager;

import aurelienribon.tweenengine.TweenManager;

public class GameState implements Screen{

	private SpaceMap map;
	private boolean lore;
	private Stage uistage;
	private InputManager input;	
	private Table travelbar,actionbar,tradebar,infobar;
	public Label hullinfo,fuelinfo,foodinfo,cycleinfo,currencyinfo,cycletimer;
	public Image foodicon,fuelicon,hullicon,currencyicon,timericon;
	private OrthographicCamera cam;
	private TweenManager tm;
	
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
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		uistage = new Stage(new ScreenViewport(cam));
		uistage.setDebugAll(SEGDX.DEBUG);
		map.getStage().setDebugAll(SEGDX.DEBUG);
		tm = new TweenManager();
		map.setTm(tm);
		
		Skin skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		Drawable defaultbackground = new Button(skin).getBackground();
		float textscale = .5f;
		float padding = 8f;
		infobar = new Table();
		infobar.setSize(uistage.getWidth(), uistage.getHeight()*.05f);
		infobar.setPosition(0, uistage.getHeight()*.95f);
		infobar.setColor(Color.DARK_GRAY);
		infobar.setBackground(defaultbackground);
		infobar.center();
		Table infobarleft = new Table();
		infobarleft.setSize(infobar.getWidth()/2, infobar.getHeight());
		foodicon = new Image(Assets.manager.get("map/foodicon.png",Texture.class));
		foodinfo = new Label(map.getPlayer().getFood()+"", skin);
		foodinfo.setFontScale(textscale);
		fuelicon = new Image(Assets.manager.get("map/fuelicon.png",Texture.class));
		fuelinfo = new Label(map.getPlayer().getCurrentFuel()+"/"+map.getPlayer().getShip().getMaxfuel(), skin);
		fuelinfo.setFontScale(textscale);
		hullicon = new Image(Assets.manager.get("map/hullicon.png",Texture.class));
		hullinfo = new Label(map.getPlayer().getCurrentHull()+"/"+map.getPlayer().getShip().getHull(), skin);
		hullinfo.setFontScale(textscale);
		currencyicon = new Image(Assets.manager.get("map/currencyicon.png",Texture.class));
		currencyinfo = new Label(""+map.getPlayer().getCurrency(), skin);
		currencyinfo.setFontScale(textscale);
		
		infobarleft.add(foodicon).left();
		infobarleft.add(foodinfo);
		infobarleft.add().pad(padding);
		infobarleft.add(fuelicon);
		infobarleft.add(fuelinfo);
		infobarleft.add().pad(padding);
		infobarleft.add(hullicon);
		infobarleft.add(hullinfo);
		infobarleft.add().pad(padding);
		infobarleft.add(currencyicon);
		infobarleft.add(currencyinfo);
		
		Table infobarright = new Table();
		infobarright.setSize(infobar.getWidth()/2, infobar.getHeight());
		
		infobar.add(infobarleft).left();
		infobar.add(infobarright).right();
		
		
		travelbar = new Table(skin);
		travelbar.setPosition(uistage.getWidth()*.6f, 0);
		travelbar.setSize(uistage.getWidth()*.4f, uistage.getHeight()*.3f);
		travelbar.setColor(Color.DARK_GRAY);
		travelbar.setBackground(defaultbackground);
		travelbar.setVisible(true);
		
		
		
		uistage.addActor(infobar);
		uistage.addActor(travelbar);
		input = new InputManager();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(
														 uistage,
														 map.getStage(),
														 new GestureDetector(input),
														 input));
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
		uistage.act(delta);
		
		map.render(delta);
		
		uistage.draw();
		cam.update();
		
		
		
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
