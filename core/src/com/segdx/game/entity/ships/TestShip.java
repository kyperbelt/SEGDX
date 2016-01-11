package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.managers.Assets;

public class TestShip extends Ship{
	
	public TestShip() {
		this.setCapacity(100);
		this.setUpgradePoints(100);
		this.setSpeed(10);
		this.setMaxfuel(200);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setHull(100);
		this.setImage("map/spaceship1.png");
		this.setName("Test Ship");
		this.setCost(3000);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.setFuelEconomy(20);
		this.setDescription("Test Ship");
	}

}
