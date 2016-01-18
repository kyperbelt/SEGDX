package com.segdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.ExtractResource;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.entity.ships.CarrierShip;
import com.segdx.game.entity.ships.CruiserShip;
import com.segdx.game.entity.ships.EnterpriseShip;
import com.segdx.game.entity.ships.GuillotineShip;
import com.segdx.game.entity.ships.InterceptorShip;
import com.segdx.game.entity.ships.MarauderShip;
import com.segdx.game.entity.ships.RaiderShip;
import com.segdx.game.entity.ships.SentinelShip;
import com.segdx.game.entity.ships.StarterShip;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;
import com.segdx.game.modules.EnergyCannon;
import com.segdx.game.modules.EngineBoosters;
import com.segdx.game.modules.ExtraHaul;
import com.segdx.game.modules.FuelReserves;
import com.segdx.game.modules.MPGModule;
import com.segdx.game.modules.MiningModule;
import com.segdx.game.modules.RailGun;
import com.segdx.game.modules.RefineryModule;
import com.segdx.game.modules.RepairDrones;
import com.segdx.game.modules.RepairKitModule;
import com.segdx.game.modules.ScannerModule;
import com.segdx.game.modules.ShieldGenerator;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

public class Player extends SpaceEntity{
	public static final float FOOD_MASS = .5f;
	
	private boolean traveling;
	private int destination;
	
	private int food;
	
	private float currentFuel;
	private float currentCapacity;
	
	private float distanceTraveled;
	
	private boolean travelDisabled;
	
	private float currency;
	
	private boolean incombat;
	
	private Enemy currentEnemy;
	
	private Array<ShipModule> modules;
	
	private Array<ShipAbility> abilities;
	
	
	private Array<Resource> resources;
	
	private GameState state;
	
	
	public Player(){
		state = (GameState) StateManager.get().getState(StateManager.GAME);
		resources = new Array<Resource>();
		modules = new Array<ShipModule>();
		setAbilities(new Array<ShipAbility>());
		ship = new StarterShip(3);
		
		
		food = 0;
		setFood(10);
		setCurrentFuel(ship.getMaxfuel());
		setCurrentHull(ship.getHull());
		setResources(new Array<Resource>());
		setCurrency(1300);
		setDistanceTraveled(0);
		
		
		//TEST RESOURCES
		addResource(ResourceStash.DRIDIUM.clone());
		addResource(ResourceStash.DRIDIUM.clone());
		
		addResource(ResourceStash.LATTERIUM.clone());
		addResource(ResourceStash.LATTERIUM.clone());
		
		addResource(ResourceStash.KNIPTORYTE.clone());
		
		//TEST MODULES
		installNewModule(new MiningModule(1));
		ShipModule m = new FuelReserves(1);
		installNewModule(m);
		installNewModule(new EngineBoosters(2));
	
	}
	
	public Vector2 getOriginPosition(){
		return new Vector2(getX()+(getShip().getSprite().getWidth()/2), getY()+(getShip().getSprite().getHeight()/2));
	}
	
	@Override
	public void repairDamage(float repair) {
		state.hullinfo.setText(getCurrentHull()+"/"+getShip().getHull());
		super.repairDamage(repair);
	}
	
	public int getUpgradePointsUsed(){
		int used = 0;
		for (int i = 0; i < modules.size; i++) {
			used+=modules.get(i).getCost();
		}
		return used;
	}
	
	public ShipModule removeShipModuleClass(ShipModule m){
		for (int i = 0; i < modules.size; i++) {
			if(modules.get(i).getClass().getName().equals(m.getClass().getName())){
				return modules.removeIndex(i);
			}
			
		}
		return null;
	}
	
	public boolean containsModuleClass(ShipModule m){
		for (int i = 0; i < modules.size; i++) {
			if(modules.get(i).getClass().getName().equals(m.getClass().getName())){
				return true;
			}
			
		}
		return false;
	}
	
	public Array<ShipModule> getModules(){
		return modules;
	}
	
	public void installNewModule(ShipModule module){
		if(module.installModule(this))
			modules.add(module);
		else{
			//TODO:If module could not be installed do something here
			System.out.println("failed to install "+module.getName());
		}
	}
	
	public float getMissingHull(){
		return ship.getHull()-getCurrentHull();
	}
	
	public void setModules(Array<ShipModule> m){
		this.modules = m;
	}
	
	public void removeModule(ShipModule module){
		for (int i = 0; i < modules.size; i++) {
			if(modules.get(i).getId()==module.getId()){
				modules.get(i).removeModule(this);
				modules.removeIndex(i);
			}
		}
	}
	
	public int getUpgradePointsAvailable(){
		return ship.getUpgradePoints()-getUpgradePointsUsed();
	}
	
	
	
	public boolean containsModuleId(int id){
		for (int i = 0; i < modules.size; i++) {
			if(modules.get(i).getId()==id)
				return true;
		}
		return false;
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
				if(this.food >= 100)
					AchievementManager.get().grantAchievement("Overwhelming Hunger");
				Stats.get().replaceIfHigher("most food", 1);
	}
	
	
	
	public Ship getShip(){
		return ship;
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
		if(ship instanceof InterceptorShip){
			AchievementManager.get().grantAchievement("Too Fast To Care");
		}else if(ship instanceof RaiderShip){
			AchievementManager.get().grantAchievement("Lets Get Some Booty");
		}else if(ship instanceof SentinelShip){
			AchievementManager.get().grantAchievement("For The Empire!");
		}else if(ship instanceof GuillotineShip){
			AchievementManager.get().grantAchievement("Ride Deaths Steed");
		}else if(ship instanceof MarauderShip){
			AchievementManager.get().grantAchievement("Thick Hull");
		}else if(ship instanceof CruiserShip){
			AchievementManager.get().grantAchievement("Im Just Cruising");
		}else if(ship instanceof CarrierShip){
			AchievementManager.get().grantAchievement("Delivery Guy");
		}else if(ship instanceof EnterpriseShip){
			AchievementManager.get().grantAchievement("The Big Honcho");
		}
		StateManager.get().getGameState().hullinfo.setText(this.getCurrentHull()+"/"+this.getShip().getHull());
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
		if(this.currentHull<0){
			GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
			GameOver.setCurrentGameOver(new GameOver(GameOver.OUT_OF_HP,this,state.difficulty,state.size));
			Assets.loadBlock(Assets.GAMEOVER_ASSETS);
			StateManager.get().changeState(StateManager.LOAD);
			
		}else if(this.currentHull > ship.getHull()){
			this.currentHull = ship.getHull();
		}
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
		setFood(1);
	}
	
	public void removeFood(){
		this.food-=1f;
		this.currentCapacity-=FOOD_MASS;
		
		//if you run out of food produce a game over
		if(getFood()<0){
			
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
		
		if(this.currency > currency){
			//money spent
			Stats.get().increment("currency spent", (int) (this.currency - currency));
		}else if(this.currency!=0){
			//money gayned
			Stats.get().increment("currency earned", (int) (currency - this.currency));
		}
		
		this.currency = currency;
		
		Stats.get().replaceIfHigher("most currency", (int) this.currency);
		
		if(currency>=10000)
			AchievementManager.get().grantAchievement("Moving Up");
		if(currency>=50000)
			AchievementManager.get().grantAchievement("Go Getter");
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
	
	@Override
	public boolean inflictDamage(float damage, boolean isEnergy) {
		getCurrentNode().addEntry(NodeEvent.COMBAT_LOG_ENTRY, "You were hit for "+damage+" "+(isEnergy ? "energy":"physical")+" damage");
		return super.inflictDamage(damage, isEnergy);
	}

	public void setIncombat(boolean incombat) {
		this.incombat = incombat;
		if(this.incombat){
			this.setCurrentEnemy(((CombatEvent)this.getCurrentNode().getEvent()).getEnemies().first());
			getCurrentNode().getMap().createEnemyFrames(((CombatEvent)getCurrentNode().getEvent()).getEnemies());
			getCurrentNode().getMap().disableNodes();
			getCurrentNode().addEntry(NodeEvent.HELP_LOG_ENTRY, "You have enterd combat.");
			AchievementManager.get().grantAchievement("Enter Combat!", Achievement.GAMEPLAY_ACHIEMENT, state.uistage,state.tm);
		}
		else{ 
			getCurrentNode().getMap().enableNodes();
			currentEnemy = null;
			getCurrentNode().getMap().deleteEnemyFrames();
			getCurrentNode().addEntry(NodeEvent.HELP_LOG_ENTRY, "You have left combat.");
		}
	}
	
	/**
	 * check if the player has the specified amount of the given resource
	 * @param resource
	 * @param amount
	 * @return
	 */
	public boolean containsResource(Resource resource,int amount){
		int available = 0;
		for (int i = 0; i < resources.size; i++) {
			if(resources.get(i).getId()==resource.getId())
				available++;
		}
		if(available >= amount)
			return true;
		else return false;
	}

	public Enemy getCurrentEnemy() {
		return currentEnemy;
	}

	public void setCurrentEnemy(Enemy currentEnemy) {
		this.currentEnemy = currentEnemy;
	}
	
	public boolean hasEnemy(){
		if(currentEnemy==null)
			return false;
		else return true;
	}
	
	public boolean canAddResource(Resource r){
		if(getShip().getCapacity()-getCurrentCapacity()>=r.getMass())
			return true;
		return false;
	}

	public Array<ShipAbility> getAbilities() {
		return abilities;
	}

	public void setAbilities(Array<ShipAbility> abilities) {
		this.abilities = abilities;
	}

	public boolean isTravelDisabled() {
		return travelDisabled;
	}
	
	public boolean isMining(){
		for (int i = 0; i < abilities.size; i++) {
			if(abilities.get(i) instanceof ExtractResource){
				ExtractResource e = (ExtractResource) abilities.get(i);
				if(e.isOnCooldown())
					return true;
			}
		}
		return false;
	}

	public void setTravelDisabled(boolean travelDisabled) {
		this.travelDisabled = travelDisabled;
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		if(this.isTravelDisabled()){
			state.travelbar.setVisible(false);
		}else if(!isMining()){
			state.travelbar.setVisible(true);
		}
		
	}

}
