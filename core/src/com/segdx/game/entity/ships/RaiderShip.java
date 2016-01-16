package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class RaiderShip extends Ship {
	
	public RaiderShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/ship2.png");
		this.setName("Raider");
		this.setCost(1500);
		this.setSpeed(5);
		this.setUpgradePoints(21);
		this.setFuelEconomy(7);
		this.setMaxfuel(120);
		this.setCapacity(35);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(45);
		this.setDescription("An average sized ship. Very durable and manuverable. Great for skirmishes and the"
				+ "preffered ship of pirates.");
	}

}
