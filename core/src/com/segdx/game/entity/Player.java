package com.segdx.game.entity;

import com.badlogic.gdx.utils.Array;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class Player {
	public static final float FOOD_MASS = .5f;
	
	private Ship ship;
	private boolean traveling;
	private SpaceNode currentNode;
	private int destination;
	
	private int food;
	
	private float currentFuel;
	private float currentCapacity;
	private float currentHull;
	
	private float distanceTraveled;
	
	private float currency;
	
	private boolean incombat;
	
	
	private Array<Resource> resources;
	
	public Player(){
		resources = new Array<Resource>();
		
		ship = new StarterShip();
		
		
		food = 0;
		setFood(10);
		setCurrentFuel(ship.getMaxfuel());
		setCurrentHull(ship.getHull());
		setResources(new Array<Resource>());
		setCurrency(1000);
		setDistanceTraveled(0);
		
		
		//TEST RESOURCES
		addResource(ResourceStash.DRIDIUM.clone());
		addResource(ResourceStash.DRIDIUM.clone());
		addResource(ResourceStash.DRIDIUM.clone());
		addResource(ResourceStash.DRIDIUM.clone());
		
		addResource(ResourceStash.LATTERIUM.clone());
		addResource(ResourceStash.LATTERIUM.clone());
		addResource(ResourceStash.LATTERIUM.clone());
		addResource(ResourceStash.LATTERIUM.clone());
		
		addResource(ResourceStash.KNIPTORYTE.clone());
		addResource(ResourceStash.KNIPTORYTE.clone());
		addResource(ResourceStash.KNIPTORYTE.clone());
		addResource(ResourceStash.KNIPTORYTE.clone());
	}
	
	public float getX(){
		return ship.getX();
	}
	
	public float getY(){
		return ship.getY();
	}
	
	public void setFood(float food){
		this.food+= food;
		currentCapacity+=food*FOOD_MASS;
		//if you run out of food produce a game over
				if(getFood()<0){
					GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
					GameOver.setCurrentGameOver(new GameOver(GameOver.OUT_OF_FOOD,this,state.difficulty,state.size));
					Assets.loadBlock(Assets.GAMEOVER_ASSETS);
					StateManager.get().changeState(StateManager.LOAD);
				}
	}
	
	public void setX(float x){
		ship.setX(x);
	}
	
	public void setY(float y){
		ship.setY(y);;
	}
	
	public Ship getShip(){
		return ship;
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
	}

	public boolean isTraveling() {
		return traveling;
	}

	public void setTraveling(boolean traveling) {
		this.traveling = traveling;
	}

	public SpaceNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(SpaceNode currentNode) {
		this.currentNode = currentNode;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public float getCurrentFuel() {
		return currentFuel;
	}

	public void setCurrentFuel(float currentFuel) {
		this.currentFuel = currentFuel;
		
		//if you run out of fuel produce a game over
		if(this.currentFuel<0){
			GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
			GameOver.setCurrentGameOver(new GameOver(GameOver.OUT_OF_FUEL,this,state.difficulty,state.size));
			Assets.loadBlock(Assets.GAMEOVER_ASSETS);
			StateManager.get().changeState(StateManager.LOAD);
			
		}else if(this.currentFuel > ship.getMaxfuel()){
			this.currentFuel = ship.getMaxfuel();
		}
		
			
	}

	public float getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(float currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public float getCurrentHull() {
		return currentHull;
	}

	public void setCurrentHull(float currentHull) {
		this.currentHull = currentHull;
	}

	public float getFood() {
		return food;
	}

	public void addFood(){
		if(this.getCurrentCapacity()+.5f > ship.getCapacity()){
			//not enought room in cargo for foood, you better get rid of some or 
			//risk starving
			System.out.println("not enough room for food in your cargo!");
		}
		this.food+=1f;
		this.currentCapacity+=FOOD_MASS;
	}
	
	public void removeFood(){
		this.food-=1f;
		this.currentCapacity-=FOOD_MASS;
		
		//if you run out of food produce a game over
		if(getFood()<0){
			GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
			GameOver.setCurrentGameOver(new GameOver(GameOver.OUT_OF_FOOD,this,state.difficulty,state.size));
			Assets.loadBlock(Assets.GAMEOVER_ASSETS);
			StateManager.get().changeState(StateManager.LOAD);
		}
	}

	public Array<Resource> getResources() {
		return resources;
	}
	
	public void addResource(Resource resource){
		if(currentCapacity+resource.getMass() > ship.getCapacity()){
			//not enough room in your ship cargo
			System.out.println("not enough capacity");
			return;
		}
		resources.add(resource);
		setCurrentCapacity(getCurrentCapacity()+resource.getMass());
		
	}
	
	public Resource removeResource(int id){
		Resource r = null;
		for (int i = 0; i < resources.size; i++) {
			if(resources.get(i).getId()==id){
				r = resources.get(i);
				resources.removeIndex(i);
				this.currentCapacity-=r.getMass();
				break;
			}
		}
		return r;
	}

	public void setResources(Array<Resource> resources) {
		this.resources = resources;
	}

	public float getCurrency() {
		return currency;
	}

	public void setCurrency(float currency) {
		this.currency = currency;
	}

	public float getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(float distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	public boolean isIncombat() {
		return incombat;
	}

	public void setIncombat(boolean incombat) {
		this.incombat = incombat;
	}

}
