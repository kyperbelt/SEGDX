package com.segdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

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
	private boolean draft;
	private OrthographicCamera cam;
	private Stage stage;
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
	
	private Player player;
	
	public void render(float delta){
		
		stage.draw();
		
		stage.act(delta);
		batch.setProjectionMatrix(stage.getCamera().combined);
		batch.begin();
		player.getShip().getSprite().draw(batch);
		batch.end();
		cam.update();
		
		//use fuel
				if(player.isTraveling()){
					float distancetraveled = Vector2.dst(player.getCurrentNode().getX(), player.getCurrentNode().getY(), player.getX(), player.getY());
					if(player.getDistanceTraveled()+10 < distancetraveled){
						player.setCurrentFuel(player.getCurrentFuel()-1);
						player.setDistanceTraveled(distancetraveled);
					}
				}
	}
	
	public SpaceMap(){
		batch = new SpriteBatch();
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false);
		this.stage = new Stage(new ScreenViewport(cam));
		
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
		return draft;
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
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
		map.draft = intToBool(draft);
		map.difficulty = difficulty;
		map.setPlayer(new Player());
		switch (size) {
		case 0:
			map.setMapheight(1600);
			map.setMapwidth(1600);
			map.setNumberofnodes(32);
			break;
		case 1:
			map.setMapheight(2400);
			map.setMapwidth(2400);
			map.setNumberofnodes(64);
			break;
		case 2:
			map.setMapheight(3200);
			map.setMapwidth(3200);
			map.setNumberofnodes(128);
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
		float effectradiusmin = 80;
		float effectradiusmax = 200;
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
		SpaceNode node = new SpaceNode();
		node.setX(MathUtils.random(map.getMapwidth()-10)+10);
		node.setY(MathUtils.random(map.getMapheight()-10)+10);
		node.setEffectradius(new Circle(new Vector2(node.getX(), node.getY()), MathUtils.random(effectradiusmax)+effectradiusmin));
		node.setNodeType(SpaceNode.REST);
		nodes.add(node);
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
					SpaceNode newnode = new SpaceNode();
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
					}else if(isRestCapable){
						newnode.setNodeType(SpaceNode.REST);
					}else {
						newnode.setNodeType(SpaceNode.NEUTRAL);
					}
					
					
					nodes.add(newnode);
				}
				
				
			}
		}
		
		return nodes;
		
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


}
