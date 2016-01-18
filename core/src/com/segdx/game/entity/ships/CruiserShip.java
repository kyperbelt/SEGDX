package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class CruiserShip extends Ship{
	
	public CruiserShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/mothershipbu7.png");
		this.setName("Cruiser");
		this.setCost(3100);
		this.setSpeed(6);
		this.setUpgradePoints(45);
		this.setFuelEconomy(4);
		this.setMaxfuel(210);
		this.setCapacity(60);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, false);
		this.setHull(80);
		this.setDescription("Not the fastest ship, but definitely able to keep up. Overall well rounded ship");
	}

}
