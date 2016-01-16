package com.segdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	
	public static Table getshipTable(GameState state,Ship s){
		Table t = new Table();
		Table info = new Table();
		Image im = new Image(s.getSprite());
		Label name = new Label(""+s.getName(), state.skin);
		name.setFontScale(.8f);
		Label description = new Label(""+s.getDescription()+"\n\n"+
					"Hull:"+s.getHull()+"\n"+
				"Upgrade Points:"+s.getUpgradePoints()+"\n"+
					"Fuel Econ:"+s.getFuelEconomy()
				+ "Speed:"+s.getSpeed()+"\n"
						+ "Capacity:"+s.getCapacity(), state.skin);
		description.setFontScale(.5f);
		description.setWrap(true);
		info.add(name).left().expand().fillX().row();
		info.add(description).left().expand().fillX().row();
		Table buytable = new Table();
		Label cost = new Label("$"+s.getCost(), state.skin);
		cost.setFontScale(.5f);
		TextButton buy = new TextButton("Buy", state.skin);
		buy.getLabel().setFontScale(.8f);
		buytable.add().left().expand().fill();
		buytable.add(cost).expand();
		buytable.add(buy).expand();
		
		t.add(im).left().expand().fill();
		t.add().left().expand().fill().row();
		t.add().left().expand().fillX();
		return t;
	}
	

}
