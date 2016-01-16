package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class EnterpriseShip extends Ship{
	
	public EnterpriseShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/redshipr.png");
		this.setName("Enterprise");
		this.setCost(11000);
		this.setSpeed(6);
		this.setUpgradePoints(125);
		this.setFuelEconomy(5);
		this.setMaxfuel(350);
		this.setCapacity(90);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(120);
		this.setDescription("A Beastly ship in size and in capability. It is said that a fully operational Enterprise"
				+ " can single handedly take over a small system.");
	}

}
