package com.segdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ship {
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
	
	private float x;
	private float y;
	
	
	
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
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
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
	

}
