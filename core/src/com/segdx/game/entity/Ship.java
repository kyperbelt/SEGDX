package com.segdx.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

public class Ship {
	
	public static final int NO_DETECTION = 0;
	public static final int SOME_DETECTION = 1;
	public static final int FULL_DETECTION = 2;
	
	
	//the amount of fuel carried by this ship
	private float maxfuel;
	//the speed of this ship
	private float speed;
	//the current hull of this ship
	private float hull;
	//the amount this ship is able to carry
	private float capacity;
	//the sprite for this ship
	private Sprite sprite;
	//the type of ship this is
	private String name;
	//the cost of this hip
	private int cost;
	
	private String description;
	
	private float fuelEconomy;
	
	private int detectionLevel;
	
	private String image;
	
	private int version;
	
	private int upgradePoints;
	
	
	public float getMaxfuel() {
		return maxfuel;
	}
	public void setMaxfuel(float maxfuel) {
		this.maxfuel = maxfuel;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
		if(this.speed > 10)
			AchievementManager.get().grantAchievement("Faster Than The Falcon");
	}
	public float getX() {
		return sprite.getX();
	}
	public void setX(float x) {
		sprite.setX(x);
	}
	public float getY() {
		return sprite.getY();
	}
	public void setY(float y) {
		this.sprite.setY(y);
	}
	public float getCapacity() {
		return capacity;
	}
	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	public String getName() {
		return name+" v"+getVersion()*10;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return cost*version;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public float getHull() {
		return hull;
	}
	public void setHull(float hull) {
		this.hull = hull;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getFuelEconomy() {
		return fuelEconomy;
	}
	public void setFuelEconomy(float fuelEconomy) {
		this.fuelEconomy = fuelEconomy;
		
	}
	public int getDetectionLevel() {
		return detectionLevel;
	}
	public void setDetectionLevel(int detectionLevel) {
		this.detectionLevel = detectionLevel;
	}
	public int getUpgradePoints() {
		return (int) (upgradePoints+(upgradePoints*getVersion()));
	}
	public void setUpgradePoints(int upgradePoints) {
		this.upgradePoints = upgradePoints;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public float getVersion() {
		return  (version*.1f);
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public static Table getshipTable(final GameState state,final Ship s){
		Table t = new Table();
		Table tt = new Table();
		Table info = new Table();
		Image im = new Image(s.getSprite());
		Label name = new Label(""+s.getName(), state.skin);
		name.setFontScale(.8f);
		Label description = new Label("Hull:"+s.getHull()+"\n"+
				"Upgrade Points:"+s.getUpgradePoints()+"\n"+
				"Fuel Econ:"+s.getFuelEconomy()
			+ "\nSpeed:"+s.getSpeed()+"\n"
					+ "Capacity:"+s.getCapacity()+"\n"
							+ "Description:\n"+s.getDescription()
					, state.skin);
		description.setFontScale(.4f);
		description.setWrap(true);
		info.add(name).left().expand().fillX().row();
		
		Table buytable = new Table();
		Label cost = new Label("$"+s.getCost(), state.skin);
		cost.setFontScale(.5f);
		TextButton buy = new TextButton("Buy", state.skin);
		buy.getLabel().setFontScale(.8f);
		buy.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Player p = state.getSpaceMap().getPlayer();
				if(s.getCost()>p.getCurrency()){
					ShipAbility.showMessage("Not enough funds        ", "You need more currency to purchase this ship", state.skin).show(state.uistage);
					return;
				}
				
				if(p.getUpgradePointsUsed()>s.getUpgradePoints()){
					ShipAbility.showMessage("Failed                  ", ""
							+ "This ship has less upgradepoints available then you are currently using."
							+ "please uninstall modules from the mods tab until you are using less than "+s.getUpgradePoints()+"points", state.skin).show(state.uistage);
					return;
				}
				
				p.setCurrency(p.getCurrency()-s.getCost());
				((TradePost)p.getCurrentNode().getTradepost()).setShip(null);
				state.updateTradeBar();
				s.setX(p.getX());
				s.setY(p.getY());
				Array<ShipModule> sm = new Array<ShipModule>(p.getModules());
				p.setShip(s);
				
				for (int i = 0; i < sm.size; i++) {
					p.installNewModule(sm.get(i));
				}
			}
		});
			
		
		buytable.add().left().expand().fill();
		buytable.add(cost).expand();
		buytable.add(buy).expand();
		
		ScrollPane pane = new ScrollPane(description, state.skin);
		pane.setColor(Color.FOREST);
		info.add(pane).left().expand().fillX().row();
		t.add(im).left().expand().fill();
		t.add(info).left().expand().fill().row();
		t.add(buytable).left().expand().fillX();
		return t;
	}
	

}
