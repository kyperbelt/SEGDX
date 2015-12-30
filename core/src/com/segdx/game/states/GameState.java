package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.InputManager;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.tween.PlayerAccessor;
import com.segdx.game.tween.SpriteAccessor;
import com.segdx.game.tween.TableAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class GameState implements Screen{
	
	public static final float SLOW = 5;
	public static final float NORMAL = 3;
	public static final float FAST = 1.5f;

	private SpaceMap map;
	private Sprite background;
	private boolean lore;
	private Stage uistage;
	private InputManager input;	
	private Table travelbar,actionbar,tradebar,infobar,shipinfobar,menubar,invetorybar;
	public TextButton travel,
						// window tab buttons
						resourcetab,shiptab,
						shipinfotab,
						menutab,
						inventorytab
						//misc buttons
						;
						
	public Label hullinfo,fuelinfo,foodinfo,cycleinfo,currencyinfo,cycletimer,
				 nodedistance,
				 //a small description of the selected node
				//some variables may affect the detail.
				 selectednodeinfo,
				 
				 //shiptab information
				 shipname,shipdescription,shipcapacity,shipfueleconomy,shipspeed;
	
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
		
		background = new Sprite(Assets.manager.get("map/m42orionnebula.png",Texture.class));
		
		Skin skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		Drawable defaultbackground = new Button(skin).getBackground();
		float textscale = .5f;
		float padding = 8f;
		infobar = new Table();
		infobar.setSize(uistage.getWidth(), uistage.getHeight()*.05f);
		infobar.setPosition(0, uistage.getHeight()*.95f);
		infobar.setColor(Color.DARK_GRAY);
		infobar.setBackground(defaultbackground);
		Table infobarleft = new Table();
		infobarleft.setSize(infobar.getWidth()/2, infobar.getHeight());
		infobarleft.left();
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
		infobarright.right();
		infobarright.setSize(infobar.getWidth()/2, infobar.getHeight());
		cycleinfo = new Label("1", skin);
		cycleinfo.setFontScale(textscale);
		cycletimer = new Label("1:34", skin);
		cycletimer.setFontScale(textscale);
		
		infobarright.add(cycleinfo);
		infobarright.add().pad(padding);
		infobarright.add(cycletimer);
		infobarright.add().pad(padding);
		
		infobar.add(infobarleft).left().expand().fill();
		infobar.add(infobarright).right().expand().fill();
		
		
		travelbar = new Table(skin);
		travelbar.setPosition(uistage.getWidth()*.6f, 0);
		travelbar.setSize(uistage.getWidth()*.4f, uistage.getHeight()*.2f);
		travelbar.setColor(Color.DARK_GRAY);
		travelbar.setBackground(defaultbackground);
		travelbar.setVisible(true);
		
		Table travelbarinfo = new Table();
		selectednodeinfo = new Label("No information to display", skin);
		selectednodeinfo.setFontScale(textscale);
		selectednodeinfo.setWrap(true);
		
		travelbarinfo.add(selectednodeinfo).width(travelbar.getWidth()-(travelbar.getWidth()*.2f));
		
		Table travelbuttonanddistance = new Table();
		travelbuttonanddistance.setSize(travelbar.getWidth(), travelbar.getHeight()*.3f);
		nodedistance = new Label("distance:", skin);
		nodedistance.setFontScale(textscale);
		travel = new TextButton("TRAVEL", skin);
		travel.getLabel().setFontScale(textscale);
		travel.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if(map.getNodebuttons().getCheckedIndex()==-1)
						return;
				final SpaceNode selectednode = map.getAllnodes().get(map.getNodebuttons().getCheckedIndex());
				if(map.getPlayer().isTraveling()||map.getPlayer().getCurrentNode().getIndex() == selectednode.getIndex()){
					return;
				}
				
				if(!map.getPlayer().isTraveling())
					map.getPlayer().getShip().getSprite().setRotation(SpriteAccessor.getAngle(new Vector2(map.getPlayer().getX(),
							map.getPlayer().getY()), new Vector2(selectednode.getX(),selectednode.getY())));
				
				map.getPlayer().setTraveling(true);
				map.getPlayer().setDestination(selectednode.getIndex());
				
				Vector2 destination = new Vector2(selectednode.getX(),selectednode.getY());
				Vector2 start = new Vector2(getSpaceMap().getPlayer().getX(),getSpaceMap().getPlayer().getY());
				
				float distance = Vector2.dst(start.x, start.y, destination.x, destination.y);
				
				Timeline.createSequence()
				.beginParallel()
					.push(Tween.to(map.getPlayer(), PlayerAccessor.POSITION, (distance/10)/map.getPlayer().getShip().getSpeed()).target(selectednode.getX(),selectednode.getY()).setCallback(new TweenCallback()
					{
						
						@Override
						public void onEvent(int type, BaseTween<?> arg1) {
							
							if(type == COMPLETE){
								map.getPlayer().setTraveling(false);
								map.getPlayer().setCurrentNode(selectednode);
								map.getPlayer().setDistanceTraveled(0);
							}
							
						}
					}))
				.end()
				.start(tm);
				
				
				SoundManager.get().playSound(SoundManager.OPTIONPRESSED);
			}
		});
		
		travelbuttonanddistance.add(nodedistance);
		travelbuttonanddistance.add().expand().fill();
		travelbuttonanddistance.add().pad(padding);
		travelbuttonanddistance.add().pad(padding);
		travelbuttonanddistance.add(travel).right();
		
		travelbar.add(travelbarinfo).expand().row();;
		travelbar.add(travelbuttonanddistance);
		
		
		shipinfobar = new Table();
		shipinfobar.setSize(uistage.getWidth()*.3f, uistage.getHeight()*.5f);
		shipinfobar.setPosition(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		
		Table shipinfotabcontainer = new Table();
		shipinfotabcontainer.bottom();
		shipinfotabcontainer.setSize(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		shipinfotab = new TextButton("ship<<<", skin);
		shipinfotab.getLabel().setFontScaleX(textscale);
		shipinfotab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if(shipinfotab.isChecked()){
					shipinfotab.setText("ship>>>");
					Tween.to(shipinfobar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-shipinfobar.getWidth()).start(tm);
				}else{
					shipinfotab.setText("ship<<<");
					Tween.to(shipinfobar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-(uistage.getWidth()*.05f)).start(tm);
				}
				
			}
		});
		
		shipinfotabcontainer.add().padBottom(shipinfobar.getHeight()-shipinfotab.getHeight()).row();
		shipinfotabcontainer.add(shipinfotab).bottom();
		
		Table shipinformationcontainer = new Table();
		shipinformationcontainer.setBackground(defaultbackground);
		shipinformationcontainer.setSize(shipinfobar.getWidth()*.8f, shipinfobar.getHeight());
		shipname = new Label("Name: "+map.getPlayer().getShip().getName(), skin);
		shipname.setFontScale(textscale*.8f);
		shipname.setWrap(true);
		
		shipdescription = new Label("Desc: "+map.getPlayer().getShip().getDescription(), skin);
		shipdescription.setFontScale(textscale*.8f);
		shipdescription.setWrap(true);
		
		
		shipinformationcontainer.add(shipname).width(shipinformationcontainer.getWidth()).row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipdescription).fillX().row();
		shipinformationcontainer.add();
		shipinformationcontainer.add();
		shipinformationcontainer.add();
		
		
		shipinfobar.add(shipinfotabcontainer).left();
		shipinfobar.add(shipinformationcontainer).expand().fill();
		shipinfobar.add();
		
		
		uistage.addActor(shipinfobar);
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
		uistage.getBatch().setProjectionMatrix(uistage.getCamera().combined);
		uistage.getBatch().begin();
		background.draw(uistage.getBatch());
		uistage.getBatch().end();
		map.render(delta);
		
		uistage.draw();
		cam.update();
		tm.update(delta);
		
		//update infobar
		fuelinfo.setText(map.getPlayer().getCurrentFuel()+"/"+map.getPlayer().getShip().getMaxfuel());
		cycleinfo.setText("cycle:"+map.getTimer().getCurrentCycle()+"  timer:");
		cycletimer.setText(""+(int)map.getTimer().getTimeLeft());
		foodinfo.setText(""+map.getPlayer().getFood());
		
		
		
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
