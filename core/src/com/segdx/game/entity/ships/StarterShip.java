package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class StarterShip extends Ship{
	
	public StarterShip(int version){
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setName("Your Fathers Ship");
		this.setDescription("This was your fathers pride and joy. its a rare model type, highly "
				+ "sought after by collectors.");
		this.setCapacity(30);
		this.setMaxfuel(100);
		this.setHull(35);
		this.setCost(3000);
		this.setSpeed(4);
		this.setFuelEconomy(8);
		this.setImage("map/shuttle.png");
		this.setUpgradePoints(10);
		this.setDetectionLevel(0);
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		Sprite shipsprite = new Sprite(Assets.manager.get(getImage(),Texture.class));
		shipsprite.setOriginCenter();
		this.setSprite(shipsprite);
		this.setX(0);
		this.setY(0);
	}

}
