package com.segdx.game.states;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap.Keys;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.Resource;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.Ship;
import com.segdx.game.entity.SpaceMap;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.entity.TradePost;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.events.ResourceEvent;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.InputManager;
import com.segdx.game.managers.NodeEventManager;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
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
	public Skin skin;
	private boolean lore;
	public Stage uistage;
	private InputManager input;
	public Drawable defaultbackground;
	public Dialog informationdialog,exitdialog;
	public ScrollPane resourcescroll,modulescroll,abilityscroll,buysellresourcesscrollpane;
	public ButtonGroup<TextButton> resttabs,tradetabs,actiontabs,fightabs,resourcetabs;
	public Table travelbar,actionbar,fightbar,resourcebar,restbar,tradebar,infobar,shipinfobar
				  ,menubar,cargobar,modbar,abilitybar,shipinformationcontainer,
				  //"tabs" inside the restbar
				  randrtab,gossiptab,missiontab,
				  //tabs inside the tradebar
				  oretab,buymodtab,buyshipstab,
				  //tabs inside the actiobar
				  infotab,loottab,combattab,logtab;
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
				 nodedistance,shieldinfo,
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
	
	public Image foodicon,fuelicon,hullicon,currencyicon,timericon,shiptabimage,shieldicon;
	private OrthographicCamera cam;
	public TweenManager tm;
	
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
		
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		skin.get("default-font", BitmapFont.class).getData().markupEnabled = true;
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
		
		
		Table shieldsTable = new Table();
		shieldsTable.setWidth(80);
		shieldsTable.setHeight(30);
		shieldicon = new Image(Assets.manager.get("map/shieldicon.png",Texture.class));
		shieldinfo = new Label(""+map.getPlayer().getCurrentShield(), skin);
		shieldinfo.setFontScale(textscale);
		shieldsTable.add(shieldicon).left().expand();
		shieldsTable.add(shieldinfo).left().expand();
		
		
		
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
				updateActionbar();
				AchievementManager.get().grantAchievement("To Infinity And Beyond", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
				Timeline.createSequence()
				.beginParallel()
					.push(Tween.to(map.getPlayer(), PlayerAccessor.POSITION, (distance/map.getPlayer().getShip().getSpeed())/map.getPlayer().getShip().getSpeed())
							.target(selectednode.getX()+(selectednode.getButton().getImage().getWidth()/2),selectednode.getY()+(selectednode.getButton().getImage().getHeight()/2)).setCallback(new TweenCallback()
					{
						
						@Override
						public void onEvent(int type, BaseTween<?> arg1) {
							
							if(type == COMPLETE){
								map.getPlayer().setTraveling(false);
								map.getPlayer().setCurrentNode(selectednode);
								map.getPlayer().setDistanceTraveled(0);
								updateRestBar();
								updateTradeBar();
								updateActionbar();
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
		
		//DIALOGS-----------------------------------------------------------------------
		informationdialog = new Dialog("Information:", skin);
		
		exitdialog = new Dialog("Quit Game:", skin){
			@Override
			protected void result(Object object) {
				boolean result = (Boolean) object;
				if(result){
					Assets.loadBlock(Assets.MENU_ASSETS);
					StateManager.get().changeState(StateManager.LOAD);
				}else{
					
				}
			}
		};
		exitdialog.getTitleLabel().setFontScale(.7f);
		exitdialog.setColor(Color.FIREBRICK);
		Label confirmexit = new Label("Are you sure you want to quit?", skin);
		confirmexit.setFontScale(textscale);
		exitdialog.getContentTable().add(confirmexit).center();
		exitdialog.button("Yes", true).left();
		exitdialog.button("No", false).right();
		
		
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
				openCloseShipInfo();
				
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
				openCloseMenu();
				
			}
		});
		
		menubartabcontainer.add(menutab).top().row();;
		menubartabcontainer.add().padTop(menubar.getHeight()-menutab.getHeight()).row();
		
		
		Table menubaroptions = new Table();
		menubaroptions.setBackground(defaultbackground);
		menubaroptions.setSize(shipinfobar.getWidth()*.8f, shipinfobar.getHeight());
		menubaroptions.setColor(Color.FIREBRICK);
		
		Label volumelabel = new Label("Volume:", skin);
		volumelabel.setFontScale(textscale);
		
		final Slider volumeslider = new Slider(0, 1, .05f, false, skin);
		volumeslider.setValue(SoundManager.get().getVolume());
		volumeslider.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				SoundManager.get().setVolume(volumeslider.getValue());
			}
		});
		
		final TextButton fullscreenbutton = new TextButton("fullscreen", skin, "toggle");
		fullscreenbutton.setChecked(Gdx.graphics.isFullscreen());
		fullscreenbutton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fullscreenbutton.isChecked());
			}
		});
		
		TextButton quitgame = new TextButton("Quit", skin);
		quitgame.getLabel().setFontScale(.8f);
		quitgame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				exitdialog.show(uistage);
			}
		});
		menubaroptions.add(volumelabel).left().expand().row();
		menubaroptions.add(volumeslider).left().expand().fill().row();
		if(Gdx.app.getType() != ApplicationType.Android)
			menubaroptions.add(fullscreenbutton).left().expand().fill().row();
		menubaroptions.add().pad(padding).expand().fill().row();
		menubaroptions.add(quitgame).row();
		
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
				openCloseHaul();
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
				openCloseMods();
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
				openCloseSkill();
			}
		});
		abilitytabcontainer.add().padBottom(abilitybar.getHeight()-(abilitytab.getHeight()*7.7f)).row();
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
		
		//ACTION BAR ---------------------------------------------------
		actionbar = new Table();
		actionbar.setSize(uistage.getWidth() * .6f, uistage.getHeight() * .3f);
		actionbar.setPosition(0, 0);

		actiontabs = new ButtonGroup<TextButton>(new TextButton("Event", skin, "toggle"),
				new TextButton("Combat", skin, "toggle"), 
				new TextButton("Loot", skin, "toggle"),new TextButton("Log", skin,"toggle"));
		for (int i = 0; i < actiontabs.getButtons().size; i++) {
			
			actiontabs.getButtons().get(i).addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					updateActionbar();
				}
			});
		}
		actiontabs.getButtons().get(0).setColor(Color.DARK_GRAY);
		actiontabs.getButtons().get(1).setColor(Color.DARK_GRAY);
		actiontabs.getButtons().get(2).setColor(Color.DARK_GRAY);
		actiontabs.getButtons().get(3).setColor(Color.DARK_GRAY);
		
		actiontabs.setChecked("Event");
		actiontabs.setMinCheckCount(1);
		actiontabs.setMaxCheckCount(1);
		actiontabs.setUncheckLast(true);

		infotab = new Table();
		infotab.setBackground(defaultbackground);
		infotab.setColor(Color.BROWN);
		infotab.setSize(actionbar.getWidth(), actionbar.getHeight() * .8f);
		combattab = new Table();
		combattab.setBackground(defaultbackground);
		combattab.setColor(Color.RED);
		combattab.setSize(actionbar.getWidth(), actionbar.getHeight() * .8f);
		loottab = new Table();
		loottab.setBackground(defaultbackground);
		loottab.setColor(Color.GOLD);
		loottab.setSize(actionbar.getWidth(), actionbar.getHeight() * .8f);
		logtab = new Table();
		logtab.setBackground(defaultbackground);
		logtab.setColor(Color.DARK_GRAY);
		logtab.setSize(actionbar.getWidth(), actionbar.getHeight() * .8f);
		
		updateActionbar();
		
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
		uistage.addActor(actionbar);
		
		
		Vector2 pos = hullicon.localToStageCoordinates(new Vector2(hullicon.getX(),hullicon.getY()));
		shieldsTable.setPosition(pos.x+(uistage.getWidth()*.27f), pos.y-(uistage.getHeight()*.04f));
		shieldsTable.setBackground(defaultbackground);
		shieldsTable.setColor(Color.DARK_GRAY);
		uistage.addActor(shieldsTable);
		shieldsTable.toBack();
		//set up game input
		input = new InputManager();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(
														 uistage,
														 map.getStage(),
														 new GestureDetector(input),
														 input));
	}
	
	public void updateActionbar(){
		final float textscale = .5f;
		
		actionbar.setVisible(true);
		actionbar.clearChildren();
		final Player player = map.getPlayer();
		Table actionbartabstable = new Table();
		actionbartabstable.left();
		actionbartabstable.add(actiontabs.getButtons().get(0),actiontabs.getButtons().get(1),actiontabs.getButtons().get(2),
				actiontabs.getButtons().get(3));
		actionbar.add(actionbartabstable).align(Align.topLeft).expandX().row();
		final SpaceNode passivenode = map.getPlayer().getCurrentNode();
		if(passivenode.getNodeType()!=SpaceNode.NEUTRAL||map.getPlayer().isTraveling()){
			
			actionbar.setVisible(false);
			return;
		}
		
		final NodeEvent event = passivenode.getEvent();
		
		switch (actiontabs.getCheckedIndex()) {
		
		case 0:
			
			infotab.clearChildren();
			if(event==null){
				Label information = new Label("Nothing seems to be going on here, better to move on.", skin);
				information.setWrap(true);
			
				infotab.add(information).expand().fill();
				actionbar.add(infotab).expand().fill();
				break;
			}
			Label infotablelabel = new Label("Info:", skin);
			infotablelabel.setFontScale(.4f);
			
			Table infotable = new Table();
			infotable.setBackground(defaultbackground);
			infotable.setColor(Color.BROWN);
			infotable.left();
			
			Label info = new Label(""+event.getDescription(), skin);
			info.setFontScale(textscale);
			info.setWrap(true);
			infotable.add(info).left().expand().fill();
			
			Label actions = new Label("Actions:", skin);
			actions.setFontScale(.4f);
			
			Table actionstable = new Table();
			actionstable.setBackground(defaultbackground);
			actionstable.setColor(Color.BROWN);
			switch (event.getType()) {
			case NodeEvent.RESOURCE:
				if(((ResourceEvent)event).requiresMining()){
					updateAbilities();
					Label advise_mining = new Label("To extract this resource you will need"
							+ "to have a [GOLD]Mining Module[] installed and use the "
							+ "[SKY]Extract[] ability in the skill Tab.", skin);
					advise_mining.setFontScale(textscale);
					advise_mining.setWrap(true);
					actionstable.add(advise_mining).left().expand().fillX().row();
				}else{
					TextButton searcharea = new TextButton("Search Area", skin);
					searcharea.getLabel().setFontScale(.7f);
					searcharea.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent eevent, float x, float y) {
							//TODO: replace this with random generated number
							if(MathUtils.randomBoolean(.7f)){
							informationdialog.getButtonTable().clearChildren();
							informationdialog.getContentTable().clearChildren();
							
							informationdialog.setColor(Color.FOREST);
							informationdialog.setSize(uistage.getWidth()*.5f, uistage.getHeight()*.3f);
							Label interactionlabel= new Label("You found the wreckage and succesfuly collected the salvage.", skin);
							interactionlabel.setFontScale(textscale);
							interactionlabel.setWrap(true);
							informationdialog.getContentTable().add(interactionlabel).center().expand().fillX().row();
							informationdialog.button("Ok", true).addListener(new ClickListener(){
								public void clicked(InputEvent eevent, float x, float y) {
									ResourceEvent e = (ResourceEvent) event;
									int amount_of_salvage = e.getResourcesAvailable();
									for (int i = 0; i < amount_of_salvage; i++) {
										passivenode.getLoot().add(e.fetchResource());
									}
									e.setShouldRemove(true);
									actiontabs.setChecked("Loot");
									informationdialog.hide();
									updateActionbar();
								}
								
							});
							}else{
								ShipAbility.showMessage("Nothing was found.", " You searched but nothing that you could use was here.", skin).show(uistage);
							}
							//TODO:else if it was a trap and you were ambushed! do some code here
							//--------------------------------------------

							informationdialog.show(uistage);
					
						}
					});
					actionstable.add(searcharea).left().expand().row();
				}
				break;
			case NodeEvent.HELP:
				break;
			case NodeEvent.COMBAT:
					final CombatEvent ce = (CombatEvent) event;
					
					TextButton flee = new TextButton("Flee  ", skin);
					flee.getLabel().setFontScale(.7f);
					flee.addListener(new ClickListener(){
						public void clicked(InputEvent evezsnt, float x, float y) {
							player.setCurrentFuel(player.getCurrentFuel()-(player.getShip().getMaxfuel()*.09f));
							if(ce.attemptFlee(player.getShip().getSpeed())){
								ce.setShouldRemove(true);
								player.setIncombat(false);
								ShipAbility.showMessage("Success       ", "They will remember this as the day you Captain <Copyrighted name> escaped!", skin).show(uistage);
								updateActionbar();
								Stats.get().increment("times fled", 1);
							}else{
								ShipAbility.showMessage("Failure        ", "You almost got away! but you were not fast enough....", skin).show(uistage);
								player.setIncombat(true);
								actiontabs.setChecked("Combat");
								updateActionbar();
								
							}
							
						};
					});
					TextButton fight = new TextButton("Fight!",skin);
					fight.getLabel().setFontScale(.7f);
					fight.addListener(new ClickListener(){
						public void clicked(InputEvent event, float x, float y) {
							player.setIncombat(true);
							actiontabs.setChecked("Combat");
							updateActionbar();
							
						}
					});
					TextButton coerce = new TextButton("Bribe", skin);
					coerce.getLabel().setFontScale(.7f);
					coerce.addListener(new ClickListener(){
						public void clicked(InputEvent a, float x, float y) {
							player.setCurrency(player.getCurrency()-(player.getCurrency()*.08f));
						
							if(ce.attemptCoerce()){
								event.setShouldRemove(true);
								ShipAbility.showMessage("Succes!          ", ce.getSuccesscoerce(), skin).show(uistage);
								player.setIncombat(false);
								Stats.get().increment("succesful bribes", 1);
							}else{
								player.setIncombat(true);
								ShipAbility.showMessage("Failure!         ",ce.getFailcoerce(), skin).show(uistage);
								actiontabs.setChecked("Combat");
								Stats.get().increment("failed bribes", 1);
							}
							updateActionbar();
						};
					});
					
					
					//add specific actions 
					switch (((CombatEvent)event).getCombatType()) {
					case CombatEvent.PIRATE_AMBUSH:
						
						break;
					case CombatEvent.BESERKER_ATTACK:
						break;

					default:
						break;
					}
					actionstable.add(flee).left().expand();
					actionstable.add(coerce).left().expand();
					actionstable.add(fight).left().expand().row();;
				break;
			default:
				break;
			}
			
			//depending on the event change the available actions;
			
			infotab.add(infotablelabel).left().expandX().row();;
			infotab.add(infotable).left().expand().fillX().row();;
			infotab.add(actions).expand().fill().row();;
			infotab.add(actionstable).expand().fill().row();;
			
			actionbar.add(infotab).expand().fill();
			break;
		case 1:
			 
			
			
			if(!player.isIncombat()){
				combattab.clearChildren();
				Label l = new Label("you are not in combat", skin);
				combattab.add(l).center().expand();
			}else{
				float scroll1 = 0;
				float scroll2 = 0;;
				if(combattab.getChildren().size>1){
				 scroll1= ((ScrollPane)combattab.getChildren().get(0)).getScrollPercentY();
				 scroll2= ((ScrollPane)combattab.getChildren().get(1)).getScrollPercentY();
				}
				combattab.clearChildren();
				Table aatable = new Table();
				Table datable = new Table();
				Array<ShipAbility> abs = player.getAbilities();
				for (int i = 0; i < abs.size; i++) {
					final ShipAbility a = abs.get(i);
					if(a.isCombatCappable()){
						if(!a.isOnCooldown()&&a.requirementsMet(player)){
							Table abholder = new Table();
							abholder.setBackground(defaultbackground);
							abholder.setColor(Color.NAVY);
							Table abnametable = new Table();
							Table abbuttontable = new Table();
							Label abname = new Label(a.getName(), skin);
							abname.setFontScale(textscale);
							final TextButton abusebutton = new TextButton("use", skin);
							if(!a.requirementsMet(player)||a.isOnCooldown()){
								abusebutton.setDisabled(true);
								abusebutton.setColor(Color.FIREBRICK);
								if(a.isOnCooldown())
									abusebutton.setText(""+a.getCooldownLeft());
							}
							abusebutton.getLabel().setFontScale(.9f);
							TextTooltip abtp = new TextTooltip(a.getDesc(), skin);
							abtp.setInstant(false);
							//abtp.setAlways(true);
							abtp.getActor().setFontScale(textscale);
							abusebutton.addListener(abtp);
							abusebutton.addListener(new ClickListener(){
								public void clicked(InputEvent event, float x, float y) {
									if(abusebutton.isDisabled()){
										SoundManager.get().playSound(SoundManager.WRONGCHOICE);
										return;
									}
									if(a.isAttack()&&player.getCurrentEnemy()!=null)
										a.performAbility(player.getCurrentEnemy());
									else{
										a.performAbility(player);
									}
									abusebutton.setDisabled(true);
									abusebutton.setColor(Color.FIREBRICK);
									
									
								};
							});
							abnametable.add(abname).left().expand();
							abbuttontable.add(abusebutton).right().expand();
							abholder.add(abnametable).left().expand();
							abholder.add(abbuttontable).right().expand();
							
							//is an attack skill
							if(a.isAttack()){
								aatable.add(abholder).expand().fillX().row();
						
							//is a defensive or misc skill	
							}else{
								datable.add(abholder).expand().fillX().row();
							}
						}
					}
					
					
				}
				for (int i = 0; i < abs.size; i++) {
					final ShipAbility a = abs.get(i);
					if(a.isCombatCappable()){
						if(a.isOnCooldown()||!a.requirementsMet(player)){
							Table abholder = new Table();
							abholder.setBackground(defaultbackground);
							abholder.setColor(Color.NAVY);
							Table abnametable = new Table();
							Table abbuttontable = new Table();
							Label abname = new Label(a.getName(), skin);
							abname.setFontScale(textscale);
							final TextButton abusebutton = new TextButton("use", skin);
							if(!a.requirementsMet(player)||a.isOnCooldown()){
								abusebutton.setDisabled(true);
								abusebutton.setColor(Color.FIREBRICK);
								if(a.isOnCooldown())
									abusebutton.setText(""+a.getCooldownLeft());
							}
							abusebutton.getLabel().setFontScale(.9f);
							TextTooltip abtp = new TextTooltip(a.getDesc(), skin);
							abtp.setInstant(false);
							//abtp.setAlways(true);
							abtp.getActor().setFontScale(textscale);
							abusebutton.addListener(abtp);
							abusebutton.addListener(new ClickListener(){
								public void clicked(InputEvent event, float x, float y) {
									if(abusebutton.isDisabled()){
										SoundManager.get().playSound(SoundManager.WRONGCHOICE);
										return;
									}
									if(a.isAttack()&&player.getCurrentEnemy()!=null)
										a.performAbility(player.getCurrentEnemy());
									else{
										a.performAbility(player);
									}
									abusebutton.setDisabled(true);
									abusebutton.setColor(Color.FIREBRICK);
									
									
								};
							});
							abnametable.add(abname).left().expand();
							abbuttontable.add(abusebutton).right().expand();
							abholder.add(abnametable).left().expand();
							abholder.add(abbuttontable).right().expand();
							
							//is an attack skill
							if(a.isAttack()){
								aatable.add(abholder).expand().fillX().row();
						
							//is a defensive or misc skill	
							}else{
								datable.add(abholder).expand().fillX().row();
							}
						}
					}
				}
				ScrollPane aascroll = new ScrollPane(aatable,skin);
				aascroll.layout();
				aascroll.setScrollPercentY(scroll1);
				aascroll.setScrollingDisabled(true, false);
				aascroll.setColor(Color.RED);
					
				ScrollPane dascroll = new ScrollPane(datable, skin);
				dascroll.layout();
				dascroll.setScrollPercentY(scroll2);
				dascroll.setScrollingDisabled(true, false);
				dascroll.setColor(Color.FOREST);
					
				combattab.add(aascroll).left().expand().fill();
				combattab.add(dascroll).right().expand().fill();		
				
			}
			actionbar.add(combattab).expand().fill();
			
			break;
		case 2:
			loottab.clearChildren();
			Table loottable = new Table();
			//if there is no loot
			if(passivenode.getLoot().size==0){
				Label l2 = new Label("there is no loot ", skin);
				loottab.add(l2).center().expand();
			//else if there is loot
			}else{
				for (int i = 0; i < passivenode.getLoot().size; i++) {
					final int index = i;
					Object o = passivenode.getLoot().get(i);
					Table lootholder = new Table();
					Table collectholder = new Table();
					Image lootimage = null;
					Label lootname = new Label("", skin);
					lootname.setFontScale(textscale);
					TextButton collect = new TextButton("", skin);
					collect.getLabel().setFontScale(.8f);
					TextTooltip tooltip = new TextTooltip("", skin);
					tooltip.setInstant(true);
					tooltip.getActor().setFontScale(textscale);
					if(o instanceof Resource){
						final Resource r = (Resource) o;
						tooltip.getActor().setText(""+r.getDescription()+"\n\n BaseValue: "+r.getBasevalue()+
								"\n\n Mass: "+r.getMass());
						lootimage = new Image(Assets.manager.get(r.getImage(),Texture
								.class));
						lootname.setText(r.getName());
						collect.getLabel().setText("COLLECT");
						collect.addListener(new ClickListener(){
							public void clicked(InputEvent event, float x, float y) {
								if(player.canAddResource(r)){
									player.addResource((Resource)passivenode.getLoot().removeIndex(index));
									updateActionbar();
									updateCargo();
								}else{
									ShipAbility.showMessage("Capacity         ", "You dont have "
											+ "enough haul capacity to carry this resource. you can get "
											+ "rid of other resources to make room for this one or go sell"
											+ "and come back.", skin).show(uistage);
								}
							}
						});
				
					}else if(o instanceof ShipModule){
						final ShipModule m = (ShipModule) o;
						tooltip.getActor().setText(""+m.getDesc()+"\n\n Upgrade Cost:"+
													m.getCost());
						lootimage = new Image(Assets.manager.get(ShipModule.ICON,Texture.class));
						lootname.setText(m.getName());
						collect.getLabel().setText("INSTALL");
						collect.addListener(new ClickListener(){
							@Override
							public void clicked(InputEvent event, float x, float y) {
								if(player.getUpgradePointsAvailable()<m.getCost()){
									ShipAbility.showMessage("Not Enough Points!       ", ""
											+ "You do not have enough upgrade points available to install"
											+ "this module. If you would like to install it please uninstall"
											+ "other modules until you have "+m.getCost()+" available",skin).show(StateManager.get().getGameState().uistage);
									return;
								}
								if(m.canInstall(player)){
									player.installNewModule((ShipModule)passivenode.getLoot().removeIndex(index));
									updateActionbar();
								}else{
									m.wasUnableToInstallDialog();
								}
							}
						});
						
					}else if(o instanceof Ship){
						Ship s = (Ship) o;
						tooltip.getActor().setText(""+s.getDescription()+"\n\n Speed:"+
										s.getSpeed()+"\n Capacity:"+s.getCapacity()+
										"\n FuelEcon:"+s.getFuelEconomy()+"\nUpgrade Points:"+
										s.getUpgradePoints());
						lootimage = new Image(Assets.manager.get("map/shuttle.png",Texture.class));
						lootname.setText(s.getName());
						collect.getLabel().setText("COMMANDEER");
						collect.getLabel().setFontScale(.6f);
					}
					
					lootimage.addListener(tooltip);
					lootname.addListener(tooltip);
					
					lootholder.add(lootimage).left().expand();
					lootholder.add(lootname).left().expand();
					
					collectholder.add(collect).right().expand();
					
					loottable.add(lootholder).left().expand();
					loottable.add(collectholder).right().expand().row();
				}
				
				ScrollPane lootscroll = new ScrollPane(loottable,skin);
				lootscroll.setColor(Color.GOLD);
				lootscroll.setScrollingDisabled(true, false);
				loottab.add(lootscroll).expand().fill();
				
			}
			
			
			actionbar.add(loottab).expand().fill();
			break;
		case 3:
			logtab.clearChildren();
				logtab.add(passivenode.getLogPane(skin)).left().expand().fill();
			actionbar.add(logtab).expand().fill();
			break;
		default:
			break;
		}
	}
	
	public boolean closeOpenTabs(){
		boolean tabbed = false;
		if(shipinfotab.isChecked()){
			shipinfotab.setChecked(false);
			tabbed = openCloseShipInfo();
		}
		if(cargotab.isChecked()){
			cargotab.setChecked(false);
			tabbed =  openCloseHaul();
		}
		if(modtab.isChecked()){
			modtab.setChecked(false);
			tabbed =  openCloseMods();
		}
		if(abilitytab.isChecked()){
			abilitytab.setChecked(false);
			tabbed = openCloseSkill();
		}
	    if(menutab.isChecked()){
			menutab.setChecked(false);
			tabbed =  openCloseMenu();
		}
		return tabbed;
			
		
	}
	
	public boolean openCloseShipInfo(){
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
		return true;
	}
	
	public boolean openCloseHaul(){
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
		return true;
	}
	
	public boolean openCloseMods(){
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
		
		return true;
	}
	
	public boolean openCloseSkill(){
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
		
		return true;
	}
	
	public boolean openCloseMenu(){
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
		
		return true;
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
				if(!ability.isOutOfCombat())
					continue;
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
				if(!ability.requirementsMet(map.getPlayer())||!ability.isOutOfCombat()){
					if(ability.isOnCooldown()&&ability.isOutOfCombat())
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
				
				abilityscrolltable.add(abilitytable).left().expand().row();
			}
			for (int i = 0; i < abilities.size; i++) {
				final ShipAbility ability = abilities.get(i);
				if(ability.isOutOfCombat())
					continue;
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
				if(!ability.requirementsMet(map.getPlayer())||!ability.isOutOfCombat()){
					if(ability.isOnCooldown()&&ability.isOutOfCombat())
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
				
				abilityscrolltable.add(abilitytable).left().expand().row();
			}
		}
		
		if(abilityscroll == null){
			abilityscroll = new ScrollPane(abilityscrolltable, skin);
			abilityscroll.setHeight(abilitybar.getHeight()*.8f);
			abilityscroll.setColor(Color.SKY);
			abilityscroll.setScrollingDisabled(true, false);
			
		}else{
			float scroll = abilityscroll.getScrollPercentY();
			abilityscroll.removeActor(abilityscroll.getWidget());
			abilityscroll.setWidget(abilityscrolltable);
			abilityscroll.setScrollPercentY(scroll);
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
			float tpscroll = 0;
			if(oretab.getChildren().size>0){
				 tpscroll = buysellresourcesscrollpane.getScrollPercentY();
			}
			oretab.clearChildren();
			
			//create Table to hold buy and sell resources
			Table buysellresourcetable = new Table();
			buysellresourcetable.left();
			
			
			Keys ids = tradepost.getResources().keys();
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
				Resource resource = ResourceStash.RESOURCES.get(id);
				
				final Resource finalresource = resource;
				Label resourcecount = new Label(""+tradepost.getResources().get(id, 0), skin);
				resourcecount.setFontScale(textscale);
				boolean available = true;
				if(tradepost.getResources().get(id, 0)==0){
					resourcecount.setColor(Color.FIREBRICK);
					available = false;
				}
				Table resourcecountholder = new Table();
				resourcecountholder.setBackground(defaultbackground);
				resourcecountholder.setColor(Color.LIME);
				resourcecountholder.add(resourcecount).left().expand();
				Label resourcename = new Label(""+resource.getName(), skin);
				
				resourceimage = new Image(Assets.manager.get(resource.getImage(),Texture.class));
				resourcetooltip = new TextTooltip(resource.getName()+"\n\n"
						+ 						  ""+resource.getDescription()+"\n\n"
								+ 				  "mass:"+resource.getMass()+"\n\n[YELLOW]Base Value:[GREEN]"+resource.getBasevalue(), skin);
				resourcetooltip.getActor().setFontScale(textscale);
				resourcetooltip.setInstant(true);
				resourceimage.addListener(resourcetooltip);
				resourcename.addListener(resourcetooltip);
				buyprice.setText(tradepost.getResourceBuyPrice(resource)+"/ea");
				sellprice.setText(tradepost.getResourceSellPrice(resource)+"/ea");
				
				if(tradepost.getResourceBuyPrice(finalresource)>player.getCurrency()||
						!player.canAddResource(resource)||!available){
					buybutton.setDisabled(true);
					buybutton.setColor(Color.FIREBRICK);
				}
				
				buybutton.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(((TextButton)event.getListenerActor()).isDisabled())
							return;
						AchievementManager.get().grantAchievement("Spend Money to Make Money", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
						player.addResource(tradepost.getResource(finalresource));
						player.setCurrency(player.getCurrency()-tradepost.getResourceBuyPrice(finalresource));
						
//						if(tradepost.getResourceBuyPrice(finalresource)>player.getCurrency()||
//								player.getShip().getCapacity()-player.getCurrentCapacity()<finalresource.getMass())
						
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
						AchievementManager.get().grantAchievement("Getting Rid of Some Junk", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
						tradepost.addResource(player.removeResource(finalresource.getId()));
						player.setCurrency(player.getCurrency()+tradepost.getResourceSellPrice(finalresource));
						updateCargo();
						updateTradeBar();
					}
				});
				
				resourceholder.add(resourcecountholder).left().fill().expand();
				resourceholder.add(resourceimage).left().expand();
				resourceholder.add(resourcename).left().expand();
				resourceholder.add(buybutton).right().expand();
				resourceholder.add(buyprice).right().expand();
				resourceholder.add(sellbutton).right().expand();
				resourceholder.add(sellprice).right().expand();
				
				buysellresourcetable.add(resourceholder).right().expand().row();
				
				
			}
			
			
			//create scrollpane
			if(buysellresourcesscrollpane==null)
				buysellresourcesscrollpane = new ScrollPane(buysellresourcetable,skin);
		    buysellresourcesscrollpane.setWidget(buysellresourcetable);
		    buysellresourcesscrollpane.setScrollPercentY(tpscroll);
			buysellresourcesscrollpane.invalidate();
			buysellresourcesscrollpane.setColor(Color.LIME);
			buysellresourcesscrollpane.setScrollingDisabled(true, false);
			//buysellresourcesscrollpane.setScrollBarPositions(true, true);
			
			
			oretab.add(buysellresourcesscrollpane).expand().fill();
			tradebar.add(oretab).expand().fill();
			
			
			
			break;
		case 1:
			buymodtab.clearChildren();
			if(tradepost.getModules().size==0){
				Label l = new Label("no modules available...", skin);
				buymodtab.add(l).center().expand();
			}else{
				buymodtab.add(tradepost.getModulesTable()).expand().fill();
			}
			tradebar.add(buymodtab).expand().fill();
			
			break;
		case 2:
			buyshipstab.clearChildren();
			if(tradepost.getShip()==null){
				Label l2 = new Label("no ships available....", skin);
				buyshipstab.add(l2).center().expand();
			}else {
				buyshipstab.add(Ship.getshipTable(this, tradepost.getShip())).center().expand().fill();
				
			}
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
			buyfoodandfuel.setSize(randrtab.getWidth(), randrtab.getHeight());
			buyfoodandfuel.left();
			Label buyfuel = new Label("Fuel:", skin);
			buyfuel.setFontScale(.5f);
			
			//buy fuel buttons
			final TextButton buyonefuel = new TextButton("buy-", skin);
			Label onefuelprice = new Label(restnode.getReststop().getFuelprice()+"/ea", skin);
			onefuelprice.setFontScale(.5f);
			
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
					AchievementManager.get().grantAchievement("REFUELING", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
				}
			});
			final TextButton buyallfuel = new TextButton("buy-", skin);
			
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
					AchievementManager.get().grantAchievement("REFUELING", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
					
				}
			});
			Label allfuelprice = new Label((int)((map.getPlayer().getShip().getMaxfuel()
					-map.getPlayer().getCurrentFuel())*restnode.getReststop().getFuelprice())+"/all", skin);
			allfuelprice.setFontScale(.5f);
			
			//buy food buttons
			Label buyfood = new Label("Food:", skin);
			buyfood.setFontScale(.5f);
			
			final TextButton buyonefood = new TextButton("buy-", skin);
			
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
					AchievementManager.get().grantAchievement("A Mans Gotta Eat", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
				}
			});
			Label onefoodprice = new Label(restnode.getReststop().getFoodprice()+"/ea", skin);
			onefoodprice.setFontScale(.5f);
			final TextButton buyallfood = new TextButton("buy-", skin);
			
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
					AchievementManager.get().grantAchievement("A Mans Gotta Eat", Achievement.GAMEPLAY_ACHIEMENT, uistage, tm);
				}
			});
			Label allfoodprice = new Label((int)((((map.getPlayer().getShip().getCapacity()-
					map.getPlayer().getCurrentCapacity())/Player.FOOD_MASS)*restnode.getReststop().getFoodprice()))+"/all", skin);
			allfoodprice.setFontScale(.5f);
			
			buyfoodandfuel.add(buyfuel).left().expand();
			buyfoodandfuel.add(buyonefuel).left();
			buyfoodandfuel.add(onefuelprice).left();
			buyfoodandfuel.add(buyallfuel).left();
			buyfoodandfuel.add(allfuelprice).left().expand().row();
			buyfoodandfuel.add().pad(16).expand().row();
			buyfoodandfuel.add(buyfood);
			buyfoodandfuel.add(buyonefood);
			buyfoodandfuel.add(onefoodprice);
			buyfoodandfuel.add(buyallfood);
			buyfoodandfuel.add(allfoodprice).row();
			
			Table repairhulltable = new Table();
			Label repairhulllabel = new Label(":Repair Hull", skin);
			repairhulllabel.setFontScale(.5f);
			
			final TextButton repair = new TextButton(""+map.getPlayer().getMissingHull()*restnode.getReststop().getHullrepairprice(), skin);
			if(!(map.getPlayer().getMissingHull()>0&&map.getPlayer().getMissingHull()*restnode.getReststop().getHullrepairprice()<=map.getPlayer().getCurrency())){
				repair.setDisabled(true);
				repair.setColor(Color.FIREBRICK);
			}
			repair.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(repair.isDisabled())
						return;
					map.getPlayer().setCurrency(map.getPlayer().getCurrency()-
							map.getPlayer().getMissingHull()*restnode.getReststop().getHullrepairprice());
					map.getPlayer().setCurrentHull(map.getPlayer().getShip().getHull());
					hullinfo.setText( map.getPlayer().getCurrentHull()+"/"+map.getPlayer().getShip().getHull());
					updateRestBar();
				}
			});
			repairhulltable.add(repair).right();
			repairhulltable.add(repairhulllabel).right().expand();
			randrtab.add(buyfoodandfuel).left().expand().row();
			randrtab.add(repairhulltable).right().expand();
			
			break;
		case 1:
			restbar.add(gossiptab).expand().fill();
			break;
		case 2:
			missiontab.clearChildren();
			if(restnode.getReststop().getWork()==null){
				Label llll = new Label("there is no work at this Rest Stop.", skin);
				missiontab.add(llll).expand();
			}else{
				System.out.println("sup");
				missiontab.add(restnode.getReststop().getWork().getWorkTable(this)).expand().fill();
			}
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
				itemtable.add(jettison);
				itemtable.add(resicon);
				Label itemname = new Label(r.getName(), skin);
				itemname.addListener(tooltip);
				itemname.setFontScale(.4f);
				itemtable.add().pad(4);
				itemtable.add(itemname);
				
				itemtable.add().pad(4);
				
				
				resourcescrolltable.add(itemtable).left().expand().row();
			}
		}
		
		if(resourcescroll == null){
			resourcescroll = new ScrollPane(resourcescrolltable, skin);
			resourcescroll.setHeight(cargobar.getHeight()*.8f);
			resourcescroll.setColor(Color.SKY);
			
		}else{
			float scroll = resourcescroll.getScrollPercentY();
			resourcescroll.removeActor(resourcescroll.getWidget());
			resourcescroll.setWidget(resourcescrolltable);
			resourcescroll.setScrollPercentY(scroll);
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
		locateship.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				OrthographicCamera c = (OrthographicCamera) map.getStage().getCamera();
				Timeline.createSequence().beginParallel()
				.push(Tween.to(c, CameraAccessor.POSITION_X_Y, .7f).target(map.getPlayer().getX(),map.getPlayer().getY()))
				.push(Tween.to(c, CameraAccessor.ZOOM, .8f).target(1f))
				.end()
				.start(tm);
			}
		});
		
		float padding = 8;
		
		shipinformationcontainer.add(shiptabimage).center().row();
		shipinformationcontainer.add(shipname).fillX().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipdescription).left().fillX().expand().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipfueleconomy).left().fillX().row();;
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipspeed).fillX().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(shipcapacity).fillX().row();
		shipinformationcontainer.add().pad(padding).row();
		shipinformationcontainer.add(locateship).left().expand().row();
		
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
		
		if(!getSpaceMap().getPlayer().isIncombat()){
			uistage.getBatch().begin();
			background.draw(uistage.getBatch());
			uistage.getBatch().end();
		}
		
		map.render(delta);
		
		uistage.draw();
		cam.update();
		tm.update(delta);
		
		//update infobar
		fuelinfo.setText(map.getPlayer().getCurrentFuel()+"/"+map.getPlayer().getShip().getMaxfuel());
		cycleinfo.setText("cycle:"+map.getTimer().getCurrentCycle()+"  timer:");
		cycletimer.setText(""+(int)map.getTimer().getTimeLeft());
		foodinfo.setText(""+map.getPlayer().getFood());
		currencyinfo.setText(""+(int)(map.getPlayer().getCurrency()));
		

		
		
		
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
		tm.pause();
		map.getTimer().setPaused(true);
	}

	@Override
	public void resume() {
		tm.resume();
		map.getTimer().setPaused(false);
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
