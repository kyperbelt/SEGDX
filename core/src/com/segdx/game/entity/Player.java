package com.segdx.game.entity;

import com.badlogic.gdx.utils.Array;

public class Player {
	
	private Ship ship;
	private boolean traveling;
	private SpaceNode currentNode;
	private int destination;
	
	private int food;
	
	private float currentFuel;
	private float currentCapacity;
	private float currentHull;
	
	private float currency;
	
	
	private Array<Resource> resources;
	
	public Player(){
		ship = new StarterShip();
		setFood(10);
		setCurrentFuel(ship.getMaxfuel());
		setCurrentHull(ship.getHull());
		setResources(new Array<Resource>());
		setCurrency(1000);
	}
	
	public float getX(){
		return ship.getX();
	}
	
	public float getY(){
		return ship.getY();
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

	public void setFood(int food) {
		this.food = food;
	}

	public Array<Resource> getResources() {
		return resources;
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

}
