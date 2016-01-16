package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class SentinelShip extends Ship{
	
	public SentinelShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/largeblueship.png");
		this.setName("Sentinel");
		this.setCost(2000);
		this.setSpeed(4);
		this.setUpgradePoints(30);
		this.setFuelEconomy(7);
		this.setMaxfuel(140);
		this.setCapacity(40);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(60);
		this.setDescription("A slow but hardy ship and the staple of the Arboros Empire. This ship"
				+ " is why many think the Empire won the war.");
	}

}
