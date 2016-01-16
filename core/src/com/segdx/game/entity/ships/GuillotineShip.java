package com.segdx.game.entity.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.entity.Ship;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.Assets;

public class GuillotineShip extends Ship {
	
	public GuillotineShip(int version) {
		this.setVersion(NodeEvent.getRandomInt(1, 3));
		if(version>0)
			this.setVersion(version);
		this.setImage("map/ship1.png");
		this.setName("Guillotine");
		this.setCost(3500);
		this.setSpeed(5);
		this.setUpgradePoints(40);
		this.setFuelEconomy(6);
		this.setMaxfuel(190);
		this.setCapacity(50);
		this.setDetectionLevel(Ship.NO_DETECTION);
		this.setSprite(new Sprite(Assets.manager.get(this.getImage(),Texture.class)));
		this.getSprite().flip(false, true);
		this.setHull(70);
		this.setDescription("The Guillotine strikes fear into its victims. This ship is recognized as the"
				+ " preffered choice of Bloodthirsty berserkers.");
	}

}
