package com.segdx.game.entity;

public class Resource {
	
	private String name;
	
	private String description;
	
	private int id;
	
	private float rarity;
	//determines how much capacity it takes up;
	private float mass;
	
	private float basevalue;
	
	private String image;

	
	public Resource(int id,String image,String name,String description,float rarity,float mass,float basevalue){
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setRarity(rarity);
		this.setMass(mass);
		this.setBasevalue(basevalue);
		this.setImage(image);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String
			name) {
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
	
	public Resource clone(){
		Resource clone = new Resource(this.getId(),this.getImage(),this.getName(),this.getDescription(),
				this.getRarity(),this.getMass(),this.getBasevalue());
		return clone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
