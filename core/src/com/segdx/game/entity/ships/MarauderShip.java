package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class MarauderShip extends Ship{
	
	public MarauderShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/ship8.png");
		this.setName("Marauder");
		this.setCost(4800);
		this.setSpeed(5);
		this.setUpgradePoints(65);
		this.setFuelEconomy(5);
		this.setMaxfuel(200);
		this.setCapacity(50);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(160);
		this.setDescription("The marauder is one of the deadliest ships around. It can take a hit and when"
				+ "propperly upgraded it can dish out some serious damage. ");
	}

}
