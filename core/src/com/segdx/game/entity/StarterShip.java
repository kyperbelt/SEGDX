package com.segdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.managers.Assets;

public class StarterShip extends Ship{
	
	public StarterShip(){
		this.setName("Your Fathers Ship");
		this.setDescription("This was your fathers pride and joy. its a rare model type, highly "
				+ "sought after by collectors.");
		this.setCapacity(30);
		this.setMaxfuel(100);
		this.setHull(40);
		this.setCost(3000);
		this.setSpeed(4);
		this.setFuelEconomy(8);
		this.setImage("map/shuttle.png");
		this.setUpgradePoints(10);
		this.setDetectionLevel(0);
		Sprite shipsprite = new Sprite(Assets.manager.get("map/shuttle.png",Texture.class));
		shipsprite.setOriginCenter();
		this.setSprite(shipsprite);
		this.setX(0);
		this.setY(0);
	}

}
