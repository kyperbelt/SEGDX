package com.segdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.Draft;
import com.segdx.game.managers.GossipManager;
import com.segdx.game.managers.NodeEventManager;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.managers.TradePostManager;
import com.segdx.game.managers.WorkManager;
import com.segdx.game.states.GameState;
import com.segdx.game.work.CollectorsDesire;

import aurelienribon.tweenengine.TweenManager;
/**
 * SPACEMAP
 * 	
 * 
 * @author Jonathan Camarena -kyperbelt
 *
 */
public class SpaceMap {
	
	private int difficulty;
	private boolean piracy;
	private boolean isdraft;
	private OrthographicCamera cam;
	private Stage stage;
	private ShapeRenderer sr;
	private SpriteBatch batch;
	private ButtonGroup<ImageButton> nodebuttons;
	private Array<SpaceNode> allnodes;
	private Array<SpaceNode> passivenodes;
	private Array<SpaceNode> tradenodes;
	private Array<SpaceNode> restnodes;
	private int mapheight;
	private int mapwidth;
	private int numberofnodes;
	private TweenManager tm;
	private CycleTimer timer;
	private NodeEventManager nodeEventManager;
	private Draft draft;
	private WorkManager workManager;
	private TradePostManager tradePostManager;
	private GossipManager gossipManager;
	
	private Array<Table> enemyframes;
	
	private Player player;
	
	public void render(float delta){
		
		
		if(SEGDX.DEBUG||true){
			sr.setProjectionMatrix(stage.getCamera().combined);
			sr.begin();
			
			sr.setColor(Color.LIME);
			sr.line(new Vector2(player.getOriginPosition().x,player.getOriginPosition().y), 
					new Vector2(allnodes.get(player.getDestination()).getOriginPosition().x,
					allnodes.get(player.getDestination()).getOriginPosition().y));
			
			int index = nodebuttons.getCheckedIndex();
			if(index!=-1&&!player.isIncombat()){
				Circle effectradius = allnodes.get(index).getEffectradius();
				sr.circle(effectradius.x, effectradius.y, effectradius.radius);
			}else if(player.isIncombat()&&player.getCurrentEnemy()!=null){
				Rectangle selection = new Rectangle(player.getCurrentEnemy().getRect());
				sr.setColor(Color.RED);
				sr.rect(selection.x, selection.y, selection.width, selection.height);
			}
			
			sr.end();
		}
		if(player.isIncombat()){
			batch.begin();
			player.getShip().getSprite().draw(batch);
			batch.end();
		}
			
			stage.draw();
			
			stage.act(delta);
		
		
		batch.setProjectionMatrix(stage.getCamera().combined);
		batch.begin();
		
		if(player.isIncombat()&&enemyframes==null){
			createEnemyFrames(((CombatEvent)player.getCurrentNode().getEvent()).getEnemies());
		}else if(!player.isIncombat()){
			player.getShip().getSprite().draw(batch);
		}
		batch.end();
		cam.update();
		timer.update(delta);
		
		//use fuel
				if(player.isTraveling()){
					float distancetraveled = Vector2.dst(player.getCurrentNode().getX(), player.getCurrentNode().getY(), player.getX(), player.getY());
					if(player.getDistanceTraveled()+player.getShip().getFuelEconomy() < distancetraveled){
						player.setCurrentFuel(player.getCurrentFuel()-1);
						Stats.get().increment("total distance traveled", (int)(distancetraveled-player.getDistanceTraveled()));
						player.setDistanceTraveled(distancetraveled);
					}
				}
				
		//update nodes
				for (int i = 0; i < allnodes.size; i++) {
					allnodes.get(i).update();
				}
	}
	
	public void animateHeal(SpaceEntity e){
		
	}
	
	public void animateDamageTaken(SpaceEntity e){
		
	}
	
	public void createEnemyFrames(final Array<Enemy> enemies) {
		if(enemyframes!=null)
			deleteEnemyFrames();
		enemyframes = new Array<Table>();
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		for (int i = 0; i < enemies.size; i++) {
			Table t = new Table();
			t.setPosition(enemies.get(i).getX(), enemies.get(i).getY());
			t.setWidth(enemies.get(i).getShip().getSprite().getWidth());
			t.setHeight(enemies.get(i).getShip().getSprite().getHeight()+40);
			final int ii = i;
			Image healthicon = new Image(Assets.manager.get("map/hullicon.png",Texture.class));
			Image shieldicon = new Image(Assets.manager.get("map/shieldicon.png",Texture.class));
			Label health = new Label(""+enemies.get(i).getCurrentHull(),state.skin);
			health.setFontScale(.5f);
			Label shield = new Label(""+enemies.get(i).getCurrentShield(),state.skin);
			shield.setFontScale(.5f);
			ImageButton shipbutton = new ImageButton(new Image(enemies.get(i).getShip().getSprite().getTexture()).getDrawable());
			shipbutton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					player.setCurrentEnemy(enemies.get(ii));
					SoundManager.get().playSound(SoundManager.NODESELECT);
				}
			});
			Image disable = new Image(Assets.manager.get("map/disabledicon.png",Texture.class));
			
			t.add(shieldicon).left().expand();
			t.add(shield).left().expand().row();
			t.add(healthicon).left().expand();
			t.add(health).left().expand().row();
			if(enemies.get(i).isDisabled())
				t.add(disable).left().expand().row();
			t.add(shipbutton).left().expand().row();;
			
			
			enemyframes.add(t);
			stage.addActor(t);
			
		}	
		
	}
	
	public void deleteEnemyFrames(){
		if(enemyframes==null)
			return;
		for (int i = 0; i < enemyframes.size; i++) {
			enemyframes.get(i).remove();
		}
		enemyframes = null;
	}

	public SpaceMap(){
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		this.stage = new Stage(new ScreenViewport(cam));
		
	}
	
	public void disableNodes(){
		for (int i = 0; i < allnodes.size; i++) {
			allnodes.get(i).getButton().setVisible(false);
		}
	}
	
	
	public void enableNodes(){
		for (int i = 0; i < allnodes.size; i++) {
			allnodes.get(i).getButton().setVisible(true);
		}
	}
	
	
	public OrthographicCamera getCam(){return cam;}
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public boolean isPiracy() {
		return piracy;
	}

	public void setPiracy(boolean piracy) {
		this.piracy = piracy;
	}

	public boolean isDraft() {
		return isdraft;
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setDraft(boolean draft) {
		this.isdraft = draft;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public int getMapheight() {
		return mapheight;
	}

	public CycleTimer getTimer() {
		return timer;
	}

	public void setTimer(CycleTimer timer) {
		this.timer = timer;
	}

	public void setMapheight(int mapheight) {
		this.mapheight = mapheight;
	}

	public int getMapwidth() {
		return mapwidth;
	}

	public void setMapwidth(int mapwidth) {
		this.mapwidth = mapwidth;
	}

	public int getNumberofnodes() {
		return numberofnodes;
	}

	public void setNumberofnodes(int numberofnodes) {
		this.numberofnodes = numberofnodes;
	}
	
	public ButtonGroup<ImageButton> getNodebuttons() {
		return nodebuttons;
	}

	public void setNodebuttons(ButtonGroup<ImageButton> nodebuttons) {
		this.nodebuttons = nodebuttons;
	}

	public Array<SpaceNode> getAllnodes() {
		return allnodes;
	}

	public void setAllnodes(Array<SpaceNode> allnodes) {
		this.allnodes = allnodes;
	}

	public Array<SpaceNode> getPassivenodes() {
		return passivenodes;
	}

	public void setPassivenodes(Array<SpaceNode> passivenodes) {
		this.passivenodes = passivenodes;
	}

	public Array<SpaceNode> getRestnodes() {
		return restnodes;
	}

	public void setRestnodes(Array<SpaceNode> restnodes) {
		this.restnodes = restnodes;
	}

	public Array<SpaceNode> getTradenodes() {
		return tradenodes;
	}

	public void setTradenodes(Array<SpaceNode> tradenodes) {
		this.tradenodes = tradenodes;
	}

	public static SpaceMap generateSpaceMap(int size,int piracy,int draft,int difficulty){
		SpaceMap map = null;
		map = new SpaceMap();
		map.piracy = intToBool(piracy);
		map.isdraft = intToBool(draft);
		map.difficulty = difficulty;
		map.setPlayer(new Player());
		map.setTimer(new CycleTimer());
		
		switch (difficulty) {
		case 0:
			map.getTimer().setCycleLength(CycleTimer.SLOW_CYCLE);
			break;
		case 1:
			map.getTimer().setCycleLength(CycleTimer.NORMAL_CYCLE);
			break;
		case 2:
			map.getTimer().setCycleLength(CycleTimer.FAST_CYCLE);
			break;

		default:
			break;
		}
		
		addCycleTasks(map.getTimer());
		addTimedTasks(map.getTimer());
		
		switch (size) {
		case 0:
			map.setMapheight(1600);
			map.setMapwidth(1600);
			map.setNumberofnodes(48);
			
			break;
		case 1:
			map.setMapheight(2400);
			map.setMapwidth(2400);
			map.setNumberofnodes(96);
			break;
		case 2:
			map.setMapheight(3200);
			map.setMapwidth(3200);
			map.setNumberofnodes(144);
			break;
		default:
			break;
		}
		
		ButtonGroup<ImageButton> nodebuttons = new ButtonGroup<ImageButton>();
		nodebuttons.setMinCheckCount(0);
		nodebuttons.setMaxCheckCount(1);
		final Array<SpaceNode> nodes = createSpaceNodes(map);
		for (int i = 0; i < nodes.size; i++) {
			nodes.get(i).setButton(SpaceNode.createButton(nodes.get(i)));
			map.getStage().addActor(nodes.get(i).getButton());
			nodes.get(i).getButton().setDebug(SEGDX.DEBUG);
			nodes.get(i).getButton().setTouchable(Touchable.enabled);
			
			nodebuttons.add(nodes.get(i).getButton());
				
		}
		map.setAllnodes(nodes);
		map.setNodebuttons(nodebuttons);
		
		return map;
	}
	
	public static Array<SpaceNode> createSpaceNodes(SpaceMap map){
		float spawnradiusmin = 40;
		float effectradiusmin = 240;
		float effectradiusmax = 320;
		float restSpawnArea = 400;
		float tradeSpawnArea = 650;
		if(map.getDifficulty()==0){
			restSpawnArea = 300;
			tradeSpawnArea = 520;
		}
		else if(map.getDifficulty()==2){
			restSpawnArea = 500;
			tradeSpawnArea = 800;
		}
		
		
		Array<SpaceNode> nodes = new Array<SpaceNode>();
		Array<SpaceNode> trade = new Array<SpaceNode>();
		Array<SpaceNode> rest = new Array<SpaceNode>();
		Array<SpaceNode> passive = new Array<SpaceNode>();
		
		SpaceNode node = new SpaceNode(map);
		node.setX(MathUtils.random(map.getMapwidth()-10)+10);
		node.setY(MathUtils.random(map.getMapheight()-10)+10);
		node.setEffectradius(new Circle(new Vector2(node.getX(), node.getY()), MathUtils.random(effectradiusmax)+effectradiusmin));
		node.setNodeType(SpaceNode.REST);
		node.setReststop(node.newRestStop());
		node.getReststop().setWork(new CollectorsDesire());
		nodes.add(node);
		rest.add(node);
		map.getCam().position.set(node.getX(), node.getY(), 0);
		map.getPlayer().setX(node.getX());
		map.getPlayer().setY(node.getY());
		map.player.setCurrentNode(node);
	
		while(nodes.size <= map.getNumberofnodes()-1){
			boolean created = false;
			
			while(!created){
				
				created = true;
				float x = MathUtils.random(map.getMapwidth()-32)+32;
				float y = MathUtils.random(map.getMapheight()-32)+32;
				Circle newminradius = new Circle(x, y,spawnradiusmin);
				
				for (int i = 0; i < nodes.size; i++) {
					SpaceNode n = nodes.get(i);
					Circle nminradius = new Circle(n.getX(), n.getY(), spawnradiusmin);
					if(newminradius.overlaps(nminradius)){
						created = false;
						break;
					}
					
				}
				if(created){
					SpaceNode newnode = new SpaceNode(map);
					newnode.setIndex(nodes.size);
					newnode.setX(x);
					newnode.setY(y);
					newnode.setEffectradius(new Circle(new Vector2(newnode.getX(), newnode.getY()), MathUtils.random(effectradiusmax)+effectradiusmin));
					boolean isTradeCapable = true;
					boolean isRestCapable = true;
					for (int i = 0; i < nodes.size; i++) {
						if(nodes.get(i).getIndex()!=newnode.getIndex()){
							Circle tradespawnradius = new Circle(new Vector2(newnode.getX(),newnode.getY()), tradeSpawnArea);
							if(nodes.get(i).getNodeType()==SpaceNode.TRADE&&tradespawnradius.contains(nodes.get(i).getX(), nodes.get(i).getY())){
								isTradeCapable = false;
							}
							Circle restspawnradius = new Circle(new Vector2(newnode.getX(),newnode.getY()), restSpawnArea);
							if(nodes.get(i).getNodeType()==SpaceNode.REST&&restspawnradius.contains(nodes.get(i).getX(), nodes.get(i).getY())){
								isRestCapable = false;
							}
		
		
						}
					}
					
					if(isTradeCapable){
						newnode.setNodeType(SpaceNode.TRADE);
						newnode.setTradepost(new TradePost());
						trade.add(newnode);
					}else if(isRestCapable){
						newnode.setNodeType(SpaceNode.REST);
						newnode.setReststop(newnode.newRestStop());
						rest.add(newnode);
					}else {
						newnode.setNodeType(SpaceNode.NEUTRAL);
						passive.add(newnode);
					}
					
					
					nodes.add(newnode);
				}
				
			}
		}
					map.setTradenodes(trade);
					map.setRestnodes(rest);
					map.setPassivenodes(passive);
		
		return nodes;
		
	}
	
	public static void addCycleTasks(final CycleTimer timer){
			
	}
	
	public static void addTimedTasks(final CycleTimer timer){
		timer.addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
				if(state.getSpaceMap().getPlayer().getCurrentNode().getNodeType() == SpaceNode.REST
						&&!state.getSpaceMap().getPlayer().isTraveling())
					return;
				state.getSpaceMap().getPlayer().removeFood();
			}
		}).setSleep(15-StateManager.get().getGameState().difficulty).repeat();
		
		
	}
	
	public static boolean intToBool(int bool){
		if(bool == 1)
			return true;
		else 
			return false;
	}

	public TweenManager getTm() {
		return tm;
	}

	public void setTm(TweenManager tm) {
		this.tm = tm;
	}

	public NodeEventManager getNodeEventManager() {
		return nodeEventManager;
	}

	public void setNodeEventManager(NodeEventManager nodeEventManager) {
		this.nodeEventManager = nodeEventManager;
	}

	public TradePostManager getTradePostManager() {
		return tradePostManager;
	}

	public void setTradePostManager(TradePostManager tradePostManager) {
		this.tradePostManager = tradePostManager;
	}

	public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	public WorkManager getWorkManager() {
		return workManager;
	}

	public void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}

	public GossipManager getGossipManager() {
		return gossipManager;
	}

	public void setGossipManager(GossipManager gossipManager) {
		this.gossipManager = gossipManager;
	}


}
