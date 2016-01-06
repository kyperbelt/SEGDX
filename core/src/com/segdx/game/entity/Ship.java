package com.segdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

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
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return cost;
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
		return upgradePoints;
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
	

}
