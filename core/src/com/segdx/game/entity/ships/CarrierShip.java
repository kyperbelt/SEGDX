package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class CarrierShip extends Ship{

	public CarrierShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/mother2b.png");
		this.setName("Carrier");
		this.setCost(4050);
		this.setSpeed(6);
		this.setUpgradePoints(50);
		this.setFuelEconomy(5);
		this.setMaxfuel(300);
		this.setCapacity(120);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(100);
		this.setDescription("The mother of all cargo ships.the carrier is able to make long treks with high amounts of fuel"
				+ "and its speed is nothing to laugh at. ");
	}
}
