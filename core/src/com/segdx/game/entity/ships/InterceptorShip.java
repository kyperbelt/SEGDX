package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class InterceptorShip extends Ship {
	
	public InterceptorShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/spaceship1.png");
		this.setName("Interceptor");
		this.setCost(1400);
		this.setSpeed(10);
		this.setUpgradePoints(14);
		this.setFuelEconomy(12);
		this.setMaxfuel(90);
		this.setCapacity(20);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(30);
		this.setDescription("A small but incredibly fast ship. Able to cross vast distances in a short amount "
				+ "of time. The ship of choice for scouts.");
	}

}
