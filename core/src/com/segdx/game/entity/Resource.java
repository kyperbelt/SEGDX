package com.segdx.game.entity;

public class Resource {
	
	private String name;
	
	private String description;
	
	private int id;
	
	private float rarity;
	//determines how much capacity it takes up;
	private float mass;
	
	private float basevalue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getBasevalue() {
		return basevalue;
	}

	public void setBasevalue(float basevalue) {
		this.basevalue = basevalue;
	}

	public float getRarity() {
		return rarity;
	}

	public void setRarity(float rarity) {
		this.rarity = rarity;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
