package com.segdx.game.states;

import java.text.DecimalFormat;
import java.util.Iterator;

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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.Resource;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.entity.TradePost;
import com.segdx.game.entity.ship.ShipAbility;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.InputManager;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.tween.CameraAccessor;
import com.segdx.game.tween.PlayerAccessor;
import com.segdx.game.tween.SpriteAccessor;
import com.segdx.game.tween.TableAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class GameState implements Screen{
	//TODO: make it so that distance is displayed different colors depending on fuel consumption
				//green for little to no fuel
				//yellow for considerable amount
				//red for not enough fuel

	private SpaceMap map;
	private Sprite background;
	private Skin skin;
	private boolean lore;
	public Stage uistage;
	private InputManager input;
	private Drawable defaultbackground;
	public ScrollPane resourcescroll,modulescroll,abilityscroll;
	public ButtonGroup<TextButton> resttabs,tradetabs;
	public Table travelbar,actionbar,restbar,tradebar,infobar,shipinfobar
				  ,menubar,cargobar,modbar,abilitybar,shipinformationcontainer,
				  //"tabs" inside the restbar
				  randrtab,gossiptab,missiontab,
				  //tabs inside the tradebar
				  oretab,buymodtab,buyshipstab;
	public TextButton travel,
						// window tab buttons
						resourcetab,shiptab,
						shipinfotab,
						menutab,
						cargotab,
						modtab,
						abilitytab,
						//misc buttons
						locateship
						;
						
	public Label hullinfo,fuelinfo,foodinfo,cycleinfo,currencyinfo,cycletimer,
				 nodedistance,
				 //a small description of the selected node
				//some variables may affect the detail.
				 selectednodeinfo,
				 coordinateinfo,
				 
				 //shiptab information
				 shipname,shipdescription,shipcapacity,shipfueleconomy,shipspeed,
				 
				 //ship cargo capacity info
				 capacityleft,
				 upgradepoints,
				 abilitieslabel;
	
	public Image foodicon,fuelicon,hullicon,currencyicon,timericon,shiptabimage;
	private OrthographicCamera cam;
	private TweenManager tm;
	
	public int size;
	public int piracy;
	public int draft;
	public int slore;
	public int difficulty;
	
	public static DecimalFormat df = new DecimalFormat("##.##");
	
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
		
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		defaultbackground = new Button(skin).getBackground();
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
		travelbar.setSize(uistage.getWidth()*.4f, uistage.getHeight()*.24f);
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
		
		//changed in the SpaceNode.class
		nodedistance = new Label("distance:", skin);
		nodedistance.setFontScale(textscale);
		travel = new TextButton("TRAVEL", skin);
		travel.getLabel().setFontScaleX(textscale);
		travel.setSize(uistage.getWidth()*1f, uistage.getHeight()*1f);
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
				
				updateRestBar();
				updateTradeBar();
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
								updateRestBar();
								updateTradeBar();
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
		
		//SHIP INFORMATION -------------------------------------------------------------
		shipinfobar = new Table();
		shipinfobar.setSize(uistage.getWidth()*.3f, uistage.getHeight()*.5f);
		shipinfobar.setPosition(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		
		Table shipinfotabcontainer = new Table();
		shipinfotabcontainer.bottom();
		shipinfotabcontainer.setSize(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		shipinfotab = new TextButton("ship<<<", skin);
		shipinfotab.getLabel().setFontScaleX(textscale);
		shipinfotab.setColor(Color.DARK_GRAY);
		shipinfotab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				shipinfobar.toFront();
				
				if(shipinfotab.isChecked()){
					updateShipInfo();
					shipinfotab.setText("ship>>>");
					menutab.setVisible(false);
					cargotab.setVisible(false);
					Tween.to(shipinfobar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-shipinfobar.getWidth()).start(tm);
				}else{
					shipinfotab.setText("ship<<<");
					menutab.setVisible(true);
					cargotab.setVisible(true);
					Tween.to(shipinfobar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-(uistage.getWidth()*.05f)).start(tm);
				}
				
			}
		});
		
		shipinfotabcontainer.add().padBottom(shipinfobar.getHeight()-shipinfotab.getHeight()).row();
		shipinfotabcontainer.add(shipinfotab).bottom();
		
		shipinformationcontainer = new Table();
		shipinformationcontainer.setBackground(defaultbackground);
		shipinformationcontainer.setColor(Color.DARK_GRAY);
		shipinformationcontainer.setSize(shipinfobar.getWidth()*.8f, shipinfobar.getHeight());
		
		updateShipInfo();
		
		shipinfobar.add(shipinfotabcontainer).left();
		shipinfobar.add(shipinformationcontainer).expand().fill();
		shipinfobar.add();
		
		//MENU BAR ------------------------------------------------------------------------------
		menubar = new Table();
		menubar.setSize(uistage.getWidth()*.3f, uistage.getHeight()*.5f);
		menubar.setPosition(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		
		Table menubartabcontainer = new Table();
		menubartabcontainer.top();
		menubartabcontainer.setSize(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		
		menutab = new TextButton("menu<<<", skin);
		menutab.getLabel().setFontScaleX(textscale);
		menutab.setColor(Color.FIREBRICK);
		menutab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				menubar.toFront();
				
				if(menutab.isChecked()){
					menutab.setText("menu>>>");
					shipinfotab.setVisible(false);
					cargotab.setVisible(false);
					Tween.to(menubar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-menubar.getWidth()).start(tm);
				}else{
					menutab.setText("menu<<<");
					cargotab.setVisible(true);
					shipinfotab.setVisible(true);
					Tween.to(menubar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-(uistage.getWidth()*.05f)).start(tm);
				}
				
			}
		});
		
		menubartabcontainer.add(menutab).top().row();;
		menubartabcontainer.add().padTop(menubar.getHeight()-menutab.getHeight()).row();
		
		
		Table menubaroptions = new Table();
		menubaroptions.setBackground(defaultbackground);
		menubaroptions.setSize(shipinfobar.getWidth()*.8f, shipinfobar.getHeight());
		menubaroptions.setColor(Color.FIREBRICK);
		
		
		menubar.add(menubartabcontainer).left();
		menubar.add(menubaroptions).expand().fill();
		menubar.add();
		
		
		//CARGO/HAUL tab -------------------------------------------------------------------------------------
		cargobar = new Table();
		cargobar.setSize(uistage.getWidth()*.3f, uistage.getHeight()*.5f);
		cargobar.setPosition(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		
		Table cargotabcontainer = new Table();
		cargotabcontainer.bottom();
		cargotabcontainer.setSize(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		cargotab = new TextButton("Haul<<<", skin);
		cargotab.getLabel().setFontScaleX(textscale);
		cargotab.setColor(Color.DARK_GRAY);
		cargotab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				cargobar.toFront();
				
				if(cargotab.isChecked()){
					updateCargo();
					cargotab.setText("Haul>>>");
					shipinfotab.setVisible(false);
					menutab.setVisible(false);
					Tween.to(cargobar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-cargobar.getWidth()).start(tm);
				}else{
					cargotab.setText("Haul<<<");
					shipinfotab.setVisible(true);
					menutab.setVisible(true);
					uistage.setScrollFocus(null);
					Tween.to(cargobar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-(uistage.getWidth()*.05f)).start(tm);
				}
			}
		});
		cargotabcontainer.add().padBottom(cargobar.getHeight()-(cargotab.getHeight()*3.2f)).expandY().fillY().top().row();
		cargotabcontainer.add(cargotab).bottom().expand().row();
		cargotabcontainer.add();
		
		Table resourcecontainer = new Table();
		resourcecontainer.setBackground(defaultbackground);
		resourcecontainer.setSize(cargobar.getWidth()*.8f, cargobar.getHeight());
		resourcecontainer.setColor(Color.DARK_GRAY);
		
		capacityleft = new Label("", skin);
		capacityleft.setFontScale(textscale);
		updateCargo();
		
		resourcecontainer.add(capacityleft).left().expandX().row();
		resourcecontainer.add(resourcescroll).expand().fill().row();
		resourcecontainer.add().pad(padding).row();
		
		cargobar.add(cargotabcontainer).left();
		cargobar.add(resourcecontainer).expand().fill();
		cargobar.add();
		
		//MODBAR ----------------------------------------------------------------
		modbar = new Table();
		modbar.setSize(uistage.getWidth()*.3f, uistage.getHeight()*.5f);
		modbar.setPosition(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		Table modtabcontainer = new Table();
		modtabcontainer.setSize(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		modtab = new TextButton("Mods<<<", skin);
		modtab.getLabel().setFontScaleX(textscale);
		modtab.setColor(Color.DARK_GRAY);
		modtab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				modbar.toFront();
				
				if(modtab.isChecked()){
					updateModsTab();
					modtab.setText("Mods>>>");
					shipinfotab.setVisible(false);
					menutab.setVisible(false);
					cargotab.setVisible(false);
					Tween.to(modbar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-cargobar.getWidth()).start(tm);
				}else{
					modtab.setText("Mods<<<");
					shipinfotab.setVisible(true);
					menutab.setVisible(true);
					cargotab.setVisible(true);
					uistage.setScrollFocus(null);
					Tween.to(modbar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-(uistage.getWidth()*.05f)).start(tm);
				}
			}
		});
		modtabcontainer.add().padBottom(modbar.getHeight()-(modtab.getHeight()*5.4f)).row();
		modtabcontainer.add(modtab).bottom().expand().row();
		//modtabcontainer.add();
		
		Table modulescontainer = new Table();
		modulescontainer.setBackground(defaultbackground);
		modulescontainer.setSize(modbar.getWidth()*.8f, modbar.getHeight());
		modulescontainer.setColor(Color.DARK_GRAY);
		
		upgradepoints = new Label("", skin);
		upgradepoints.setFontScale(textscale);
		updateModsTab();
		
		modulescontainer.add(upgradepoints).left().expandX().row();
		modulescontainer.add(modulescroll).expand().fill().row();
		modulescontainer.add().pad(padding).row();
		
		modbar.add(modtabcontainer).left();
		modbar.add(modulescontainer).expand().fill();
		modbar.add();
		
		//ABILITYBAR------------------------------------------------------------
		
		abilitybar = new Table();
		abilitybar.setSize(uistage.getWidth()*.3f, uistage.getHeight()*.5f);
		abilitybar.setPosition(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		Table abilitytabcontainer = new Table();
		abilitytabcontainer.setSize(uistage.getWidth()-(uistage.getWidth()*.05f), uistage.getHeight()*.4f);
		abilitytab = new TextButton("Skill<<<", skin);
		abilitytab.getLabel().setFontScaleX(.4f);
		abilitytab.setColor(Color.DARK_GRAY);
		abilitytab.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				abilitybar.toFront();
				
				if(abilitytab.isChecked()){
					updateAbilities();
					abilitytab.setText("Skill>>>");
					shipinfotab.setVisible(false);
					menutab.setVisible(false);
					cargotab.setVisible(false);
					modtab.setVisible(false);
					Tween.to(abilitybar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-cargobar.getWidth()).start(tm);
				}else{
					abilitytab.setText("Skill<<<");
					shipinfotab.setVisible(true);
					menutab.setVisible(true);
					cargotab.setVisible(true);
					uistage.setScrollFocus(null);
					modtab.setVisible(true);
					Tween.to(abilitybar, TableAccessor.POSITION_X, 1).target(uistage.getWidth()-(uistage.getWidth()*.05f)).start(tm);
				}
			}
		});
		abilitytabcontainer.add().padBottom(abilitybar.getHeight()-(abilitytab.getHeight()*7.6f)).row();
		abilitytabcontainer.add(abilitytab).bottom().expand().row();
		//modtabcontainer.add();
		
		Table abilitiescontainer = new Table();
		abilitiescontainer.setBackground(defaultbackground);
		abilitiescontainer.setSize(abilitybar.getWidth()*.8f, abilitybar.getHeight());
		abilitiescontainer.setColor(Color.DARK_GRAY);
		
		abilitieslabel = new Label("Skills & Abilities", skin);
		abilitieslabel.setFontScale(textscale);
		updateAbilities();
		
		abilitiescontainer.add(abilitieslabel).left().expandX().row();
		abilitiescontainer.add(abilityscroll).expand().fill().row();
		abilitiescontainer.add().pad(padding).row();
		
		abilitybar.add(abilitytabcontainer).left();
		abilitybar.add(abilitiescontainer).expand().fill();
		abilitybar.add();
		
		//REST BAR ------------------------------------------------------------
		
		restbar = new Table();
		restbar.setSize(uistage.getWidth()*.6f, uistage.getHeight()*.3f);
		restbar.setPosition(0, 0);
		
		resttabs = new ButtonGroup<TextButton>(new TextButton("R & R", skin ,"toggle"),
											   new TextButton("Gossip", skin,"toggle"),
											   new TextButton("Work", skin,"toggle"));
		for (int i = 0; i < resttabs.getButtons().size; i++) {
			resttabs.getButtons().get(i).setColor(Color.DARK_GRAY);
			resttabs.getButtons().get(i).addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					updateRestBar();
				}
			});
		}
		resttabs.setChecked("R & R");
		resttabs.setMinCheckCount(1);
		resttabs.setMaxCheckCount(1);
		resttabs.setUncheckLast(true);
		
		
		randrtab = new Table();
		randrtab.setBackground(defaultbackground);
		randrtab.setColor(Color.DARK_GRAY);
		randrtab.setSize(restbar.getWidth(), restbar.getHeight()*.8f);
		gossiptab = new Table();
		gossiptab.setBackground(defaultbackground);
		gossiptab.setColor(Color.DARK_GRAY);
		gossiptab.setSize(restbar.getWidth(), restbar.getHeight()*.8f);
		missiontab = new Table();
		missiontab.setBackground(defaultbackground);
		missiontab.setColor(Color.DARK_GRAY);
		missiontab.setSize(restbar.getWidth(), restbar.getHeight()*.8f);
		
		updateRestBar();
		
		//TRADE BAR -------------------------------------------------
		
		tradebar = new Table();
		tradebar.setSize(uistage.getWidth()*.6f, uistage.getHeight()*.3f);
		tradebar.setPosition(0, 0);
		
		tradetabs = new ButtonGroup<TextButton>(new TextButton("Resources", skin ,"toggle"),
											   new TextButton("Modules", skin,"toggle"),
											   new TextButton("Ships", skin,"toggle"));
		for (int i = 0; i < tradetabs.getButtons().size; i++) {
			tradetabs.getButtons().get(i).setColor(Color.FOREST);
			tradetabs.getButtons().get(i).addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					updateTradeBar();
				}
			});
		}
		tradetabs.setChecked("Resources");
		tradetabs.setMinCheckCount(1);
		tradetabs.setMaxCheckCount(1);
		tradetabs.setUncheckLast(true);
		
		
		oretab = new Table();
		oretab.setBackground(defaultbackground);
		oretab.setColor(Color.FOREST);
		oretab.setSize(tradebar.getWidth(), tradebar.getHeight()*.8f);
		buymodtab = new Table();
		buymodtab.setBackground(defaultbackground);
		buymodtab.setColor(Color.FOREST);
		buymodtab.setSize(tradebar.getWidth(), tradebar.getHeight()*.8f);
		buyshipstab = new Table();
		buyshipstab.setBackground(defaultbackground);
		buyshipstab.setColor(Color.FOREST);
		buyshipstab.setSize(tradebar.getWidth(), tradebar.getHeight()*.8f);
		
		updateTradeBar();
		
		
		//add all to the ui -----------------------------------------
		
		//setup scroll leave
			map.getStage().addListener(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					uistage.setScrollFocus(null);
					return super.touchDown(event, x, y, pointer, button);
				}
			});
		uistage.addActor(shipinfobar);
		uistage.addActor(menubar);
		uistage.addActor(cargobar);
		uistage.addActor(abilitybar);
		uistage.addActor(modbar);
		uistage.addActor(infobar);
		uistage.addActor(travelbar);
		uistage.addActor(restbar);
		uistage.addActor(tradebar);
		
		
		//set up game input
		input = new InputManager();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(
														 uistage,
														 map.getStage(),
														 new GestureDetector(input),
														 input));
	}
	
	public void updateAbilities(){
		
		Table abilityscrolltable = new Table();
		Array<ShipAbility> abilities = map.getPlayer().getAbilities();
		if(abilities.size==0){
			Label l = new Label("No abilities to use..", skin);
			l.setFontScale(.4f);
			abilityscrolltable.add(l).center().expand();
		}else{
			for (int i = 0; i < abilities.size; i++) {
				final ShipAbility ability = abilities.get(i);
				TextTooltip tooltip = new TextTooltip(ability.getDesc(), skin);
				tooltip.getActor().setFontScale(.4f);
				tooltip.setInstant(true);
				Table abilitytable = new Table();
				abilitytable.setBackground(defaultbackground);
				abilitytable.setColor(Color.NAVY);
				
				
				Label abilityname = new Label(ability.getName(), skin);
				abilityname.addListener(tooltip);
				abilityname.setFontScaleY(.7f);
				abilityname.setFontScaleX(.3f);
				abilitytable.add().pad(4);
				abilitytable.add(abilityname);
				TextButton useability = new TextButton("use", skin);
				useability.getLabel().setFontScale(1f);
				useability.setSize(64, 32);
				if(!ability.requirementsMet(map.getPlayer())){
					if(ability.isOnCooldown())
						useability.setText(" "+ability.getCooldownLeft());
					useability.setDisabled(true);
					useability.setColor(Color.FIREBRICK);
				}else{
					useability.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							ability.performAbility(map.getPlayer());
							updateAbilities();
							
						}
					});
				}
				abilitytable.add().pad(4);
				abilitytable.add(useability);
				
				abilityscrolltable.add(abilitytable).row();
			}
		}
		
		if(abilityscroll == null){
			abilityscroll = new ScrollPane(abilityscrolltable, skin);
			abilityscroll.setHeight(abilitybar.getHeight()*.8f);
			abilityscroll.setColor(Color.SKY);
			abilityscroll.setScrollingDisabled(true, false);
			
		}else{
			abilityscroll.removeActor(abilityscroll.getWidget());
			abilityscroll.setWidget(abilityscrolltable);
		}
		
	}
	
	public void updateModsTab(){
		
		upgradepoints.setText("UPoints:"+map.getPlayer().getUpgradePointsUsed()+"/"+map.getPlayer().getShip().getUpgradePoints());
		
		Table modulescrolltable = new Table();
		Array<ShipModule> modules = map.getPlayer().getModules();
		if(modules.size==0){
			Label l = new Label("You have no modules..", skin);
			l.setFontScale(.4f);
			modulescrolltable.add(l).center().expand();
		}else{
			for (int i = 0; i < modules.size; i++) {
				final ShipModule module = modules.get(i);
				TextTooltip tooltip = new TextTooltip(module.getDesc()+
						"\n\nUpgradeCost: "+module.getCost(), skin);
				tooltip.getActor().setFontScale(.4f);
				tooltip.setInstant(true);
				Table moduletable = new Table();
				moduletable.setBackground(defaultbackground);
				moduletable.setColor(Color.DARK_GRAY);
				
				Image moduleicon = new Image(Assets.manager.get(ShipModule.ICON,Texture.class));
				moduleicon.addListener(tooltip);
				moduletable.add(moduleicon);
				Label modulename = new Label(module.getName(), skin);
				modulename.addListener(tooltip);
				modulename.setFontScaleY(.7f);
				modulename.setFontScaleX(.3f);
				moduletable.add().pad(4);
				moduletable.add(modulename);
				TextButton removemodule = new TextButton("X", skin);
				removemodule.getLabel().setFontScale(1f);
				removemodule.setSize(64, 32);
				removemodule.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						map.getPlayer().removeModule(module);
						updateModsTab();
						updateAbilities();
						
					}
				});
				moduletable.add().pad(4);
				moduletable.add(removemodule);
				
				modulescrolltable.add(moduletable).row();
			}
		}
		
		if(modulescroll == null){
			modulescroll = new ScrollPane(modulescrolltable, skin);
			modulescroll.setHeight(modbar.getHeight()*.8f);
			modulescroll.setColor(Color.SKY);
			modulescroll.setScrollingDisabled(true, false);
			
		}else{
			modulescroll.removeActor(modulescroll.getWidget());
			modulescroll.setWidget(modulescrolltable);
		}
		
	}
	
	public void updateTradeBar(){
		float textscale = .5f;
		tradebar.setVisible(true);
		tradebar.clearChildren();
		final Player player = map.getPlayer();
		Table tradetabstable = new Table();tradetabstable.left();
		tradetabstable.add(tradetabs.getButtons().get(0),tradetabs.getButtons().get(1),tradetabs.getButtons().get(2));
		tradebar.add(tradetabstable).align(Align.topLeft).expandX().row();
		final SpaceNode tradenode = map.getPlayer().getCurrentNode();
		if(tradenode.getNodeType()!=SpaceNode.TRADE||map.getPlayer().isTraveling()){
			
			tradebar.setVisible(false);
			return;
		}
		
		final TradePost tradepost = tradenode.getTradepost();
		
		switch (tradetabs.getCheckedIndex()) {
		
		case 0:
			oretab.clearChildren();
			
			//create Table to hold buy and sell resources
			Table buysellresourcetable = new Table();
			buysellresourcetable.left();
			
			
			Iterator<Integer> ids = tradepost.getResources().keys();
			while(ids.hasNext()){
				TextTooltip resourcetooltip;
				Table resourceholder = new Table();
				TextButton buybutton = new TextButton("buy", skin);
				TextButton sellbutton = new TextButton("sell", skin);
				Label buyprice = new Label("", skin);
				buyprice.setFontScale(textscale);
				Label sellprice = new Label("",skin);
				sellprice.setFontScale(textscale);
				Image resourceimage;
				//Label amountleft = new Label("",skin);
				
				int id = ids.next();
				Resource resource = null;
				if(id==ResourceStash.DRIDIUM.getId()){
					resource = ResourceStash.DRIDIUM.clone();
				}else if(id == ResourceStash.KNIPTORYTE.getId()){
					resource = ResourceStash.KNIPTORYTE.clone();
				}else if(id == ResourceStash.LATTERIUM.getId()){
					resource = ResourceStash.LATTERIUM.clone();
				}else if(id == ResourceStash.NAQUIDRA.getId()){
					resource = ResourceStash.NAQUIDRA.clone();
				}else if(id == ResourceStash.SALVAGE.getId()){
					resource = ResourceStash.SALVAGE.clone();
				}
				
				final Resource finalresource = resource;
				Label resourcename = new Label(""+resource.getName(), skin);
				
				resourceimage = new Image(Assets.manager.get(resource.getImage(),Texture.class));
				resourcetooltip = new TextTooltip(resource.getName()+"\n\n"
						+ 						  ""+resource.getDescription()+"\n\n"
								+ 				  "mass:"+resource.getMass()+"\n\nBase Value:"+resource.getBasevalue(), skin);
				resourcetooltip.getActor().setFontScale(textscale);
				resourcetooltip.setInstant(true);
				resourceimage.addListener(resourcetooltip);
				resourcename.addListener(resourcetooltip);
				buyprice.setText(tradepost.getResourceBuyPrice(resource)+"/ea");
				sellprice.setText(tradepost.getResourceSellPrice(resource)+"/ea");
				
				if(tradepost.getResourceBuyPrice(finalresource)>player.getCurrency()||
						player.getShip().getCapacity()-player.getCurrentCapacity()<resource.getMass()){
					buybutton.setDisabled(true);
					buybutton.setColor(Color.FIREBRICK);
				}
				
				buybutton.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(((TextButton)event.getListenerActor()).isDisabled())
							return;
						player.addResource(finalresource.clone());
						player.setCurrency(player.getCurrency()-tradepost.getResourceBuyPrice(finalresource));
						if(tradepost.getResourceBuyPrice(finalresource)>player.getCurrency()||
								player.getShip().getCapacity()-player.getCurrentCapacity()<finalresource.getMass())
						updateTradeBar();
						updateCargo();
						
					}
				});
				
				if(!player.containsResource(resource, 1)){
					sellbutton.setDisabled(true);
					sellbutton.setColor(Color.FIREBRICK);
				}
				
				sellbutton.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(((TextButton)event.getListenerActor()).isDisabled())
							return;
						player.removeResource(finalresource.getId());
						player.setCurrency(player.getCurrency()+tradepost.getResourceSellPrice(finalresource));
						updateCargo();
						if(!player.containsResource(finalresource, 1))
						updateTradeBar();
					}
				});
				
				resourceholder.add(resourceimage).left().expand();
				resourceholder.add(resourcename).left().expand();
				resourceholder.add(buybutton).left().expand();
				resourceholder.add(buyprice).left().expand();
				resourceholder.add(sellbutton).left().expand();
				resourceholder.add(sellprice).left().expand();
				
				buysellresourcetable.add(resourceholder).expand().row();
				
				
			}
			
			
			//create scrollpane
			ScrollPane buysellresourcesscrollpane = new ScrollPane(buysellresourcetable);
			
			oretab.add(buysellresourcesscrollpane).expand().fill();
			tradebar.add(oretab).expand().fill();
			
			
			break;
		case 1:
			buymodtab.clearChildren();
			Label l = new Label("no modules available...", skin);
			buymodtab.add(l).center().expand();
			
			tradebar.add(buymodtab).expand().fill();
			
			break;
		case 2:
			buyshipstab.clearChildren();
			Label l2 = new Label("no ships available....", skin);
			buyshipstab.add(l2).center().expand();
			tradebar.add(buyshipstab).expand().fill();
			break;

		default:
			break;
		}
	}
	
	public void updateRestBar(){
		updateCargo();
		restbar.setVisible(true);
		restbar.clearChildren();
		Table restabstable = new Table();restabstable.left();
		restabstable.add(resttabs.getButtons().get(0),resttabs.getButtons().get(1),resttabs.getButtons().get(2));
		restbar.add(restabstable).align(Align.topLeft).expandX().row();
		final SpaceNode restnode = map.getPlayer().getCurrentNode();
		if(restnode.getNodeType()!=SpaceNode.REST||map.getPlayer().isTraveling()){
			restbar.setVisible(false);
			return;
		}
		
		switch (resttabs.getCheckedIndex()) {
		case 0:
			randrtab.clearChildren();
			restbar.add(randrtab).expand().fill();
			Table buyfoodandfuel = new Table();
			Label buyfuel = new Label("Fuel:", skin);
			buyfuel.setFontScale(.5f);
			
			//buy fuel buttons
			final TextButton buyonefuel = new TextButton("buy("+restnode.getReststop().getFuelprice()+")", skin);
			
			if(map.getPlayer().getCurrentFuel()==map.getPlayer().getShip().getMaxfuel()){
				buyonefuel.setDisabled(true);
				buyonefuel.setColor(Color.FIREBRICK);
			}
			buyonefuel.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					float difference = map.getPlayer().getShip().getMaxfuel()-map.getPlayer().getCurrentFuel();
					if(map.getPlayer().getCurrency()<restnode.getReststop().getFuelprice()||difference==0)
						return;
					map.getPlayer().setCurrency(map.getPlayer().getCurrency()-restnode.getReststop().getFuelprice());
					map.getPlayer().setCurrentFuel(map.getPlayer().getCurrentFuel()+1);
					updateRestBar();
				}
			});
			final TextButton buyallfuel = new TextButton("buyall("+df.format(((map.getPlayer().getShip().getMaxfuel()
					-map.getPlayer().getCurrentFuel())*restnode.getReststop().getFuelprice()))+")", skin);
			
			if(((map.getPlayer().getShip().getMaxfuel()
					-map.getPlayer().getCurrentFuel())*restnode.getReststop().getFuelprice())<=0){
				buyallfuel.setDisabled(true);
				buyallfuel.setColor(Color.FIREBRICK);
			}
			buyallfuel.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					float difference = map.getPlayer().getShip().getMaxfuel()-map.getPlayer().getCurrentFuel();
					if(map.getPlayer().getCurrency()<(difference*restnode.getReststop().getFuelprice())||difference==0)return;
					map.getPlayer().setCurrency(map.getPlayer().getCurrency()-(difference*restnode.getReststop().getFuelprice()));
					map.getPlayer().setCurrentFuel(map.getPlayer().getCurrentFuel()+difference);
					updateRestBar();
				}
			});
			
			//buy food buttons
			Label buyfood = new Label("Food:", skin);
			buyfood.setFontScale(.5f);
			
			final TextButton buyonefood = new TextButton("buy("+restnode.getReststop().getFoodprice()+")", skin);
			
			if(map.getPlayer().getShip().getCapacity()-map.getPlayer().getCurrentCapacity()<Player.FOOD_MASS){
				buyonefood.setDisabled(true);
				buyonefood.setColor(Color.FIREBRICK);
			}
			buyonefood.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					float difference = (map.getPlayer().getShip().getCapacity()-
							map.getPlayer().getCurrentCapacity())/Player.FOOD_MASS;
					if(map.getPlayer().getCurrency()<restnode.getReststop().getFoodprice()||difference<1)
						return;
					map.getPlayer().setCurrency(map.getPlayer().getCurrency()-restnode.getReststop().getFoodprice());
					map.getPlayer().addFood();
					updateRestBar();
				}
			});
			
			final TextButton buyallfood = new TextButton("buyall("+df.format((((map.getPlayer().getShip().getCapacity()-
					map.getPlayer().getCurrentCapacity())/Player.FOOD_MASS)*restnode.getReststop().getFoodprice()))+")", skin);
			
			if(map.getPlayer().getShip().getCapacity()-map.getPlayer().getCurrentCapacity()<Player.FOOD_MASS){
				buyallfood.setDisabled(true);
				buyallfood.setColor(Color.FIREBRICK);
			}
			buyallfood.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					float difference = (map.getPlayer().getShip().getCapacity()-
							map.getPlayer().getCurrentCapacity())/Player.FOOD_MASS;
					if(map.getPlayer().getCurrency()<(difference*restnode.getReststop().getFoodprice())||difference<1)return;
					map.getPlayer().setCurrency(map.getPlayer().getCurrency()-(difference*restnode.getReststop().getFoodprice()));
					map.getPlayer().setFood(difference);
					updateRestBar();
				}
			});
			
			
			buyfoodandfuel.add(buyfuel);
			buyfoodandfuel.add(buyonefuel);
			buyfoodandfuel.add(buyallfuel).row();
			buyfoodandfuel.add().pad(16).expand().row();
			buyfoodandfuel.add(buyfood);
			buyfoodandfuel.add(buyonefood);
			buyfoodandfuel.add(buyallfood).row();;
			
			randrtab.add(buyfoodandfuel).left().expand();
			
			break;
		case 1:
			restbar.add(gossiptab).expand().fill();
			break;
		case 2:
			restbar.add(missiontab).expand().fill();
			break;

		default:
			break;
		}
	}
	
	public void updateCargo(){
		//UPDATE all the cargo information
		//like the current cargo capacity and held resources
		capacityleft.setText("cap:"+map.getPlayer().getCurrentCapacity()+"/"+map.getPlayer().getShip().getCapacity());
		
		Table resourcescrolltable = new Table();
		Array<Resource> res = map.getPlayer().getResources();
		if(res.size==0){
			Label l = new Label("You have nothing..", skin);
			l.setFontScale(.4f);
			resourcescrolltable.add(l).center().expand();
		}else{
			for (int i = 0; i < res.size; i++) {
				final Resource r = res.get(i);
				TextTooltip tooltip = new TextTooltip(r.getDescription()+"\n\nBasevalue:"+r.getBasevalue()+
						"\n\nMass: "+r.getMass(), skin);
				tooltip.getActor().setFontScale(.4f);
				tooltip.setInstant(true);
				Table itemtable = new Table();
				
				Image resicon = new Image(Assets.manager.get(r.getImage(),Texture.class));
				resicon.addListener(tooltip);
				itemtable.add(resicon);
				Label itemname = new Label(r.getName(), skin);
				itemname.addListener(tooltip);
				itemname.setFontScale(.4f);
				itemtable.add().pad(4);
				itemtable.add(itemname);
				TextButton jettison = new TextButton("X", skin);
				jettison.getLabel().setFontScale(1f);
				jettison.setSize(64, 32);
				jettison.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						map.getPlayer().removeResource(r.getId());
						updateCargo();
						updateRestBar();
						updateTradeBar();
						
					}
				});
				itemtable.add().pad(4);
				itemtable.add(jettison);
				
				resourcescrolltable.add(itemtable).row();
			}
		}
		
		if(resourcescroll == null){
			resourcescroll = new ScrollPane(resourcescrolltable, skin);
			resourcescroll.setHeight(cargobar.getHeight()*.8f);
			resourcescroll.setColor(Color.SKY);
			
		}else{
			resourcescroll.removeActor(resourcescroll.getWidget());
			resourcescroll.setWidget(resourcescrolltable);
		}
	}
	
	public void updateShipInfo(){
		
		shipinformationcontainer.clearChildren();
		
		//TODO: update all the ship info components here every time the player gets a new ship or ship upgrade
		shiptabimage = new Image(Assets.manager.get(map.getPlayer().getShip().getImage(),Texture.class));
		
		float textscale = .5f;
		shipname = new Label("Name: "+map.getPlayer().getShip().getName(), skin);
		shipname.setFontScale(textscale*.8f);
		shipname.setWrap(true);
		
		shipdescription = new Label("Desc: "+map.getPlayer().getShip().getDescription(), skin);
		shipdescription.setFontScale(textscale*.8f);
		shipdescription.setWrap(true);
		
		shipfueleconomy = new Label("Fuel Economy:"+map.getPlayer().getShip().getFuelEconomy(), skin);
		shipfueleconomy.setFontScale(textscale*.8f);
		shipfueleconomy.setWrap(true);
		
		shipspeed = new Label("Speed: "+map.getPlayer().getShip().getSpeed(), skin);
		shipspeed.setFontScale(textscale*.8f);
		shipspeed.setWrap(true);
		
		shipcapacity = new Label("Haul Capacity: "+map.getPlayer().getShip().getCapacity(), skin);
		shipcapacity.setFontScale(textscale*.8f);
		shipcapacity.setWrap(true);
		
		
		locateship = new TextButton("((locate))", skin);
		locateship.setScale(2f);
		locateship.getLabel().setFontScale(textscale);
		locateship.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				OrthographicCamera c = (OrthographicCamera) map.getStage().getCamera();
				Tween.to(c, CameraAccessor.POSITION_X_Y, .7f).target(map.getPlayer().getX(),map.getPlayer().getY()).start(tm);
			}
		});
		
		float padding = 8;
		
		shipinformationcontainer.add(shiptabimage).expand().fill().row();
		shipinformationcontainer.add(shipname).width(shipinformationcontainer.getWidth()).row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipdescription).fillX().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipfueleconomy).fillX().row();;
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipspeed).fillX().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipcapacity).fillX().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(locateship).row();
		
		
		
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
		currencyinfo.setText(""+df.format(map.getPlayer().getCurrency()));
		

		
		
		
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
	
	public void disposeMap(){
		this.map.getStage().dispose();
		this.map = null;
		this.uistage.dispose();
		this.tm.killAll();
	}

	@Override
	public void dispose() {
		//batch.dispose();
		Assets.disposeBlock(Assets.GAME_ASSETS);
	}

}
