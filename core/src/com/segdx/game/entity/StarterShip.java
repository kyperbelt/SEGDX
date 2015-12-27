package com.segdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.segdx.game.managers.Assets;

public class StarterShip extends Ship{
	
	public StarterShip(){
		this.setName("Your Fathers Shuttle");
		
		this.setCapacity(30);
		this.setMaxfuel(100);
		this.setHull(40);
		this.setCost(10000);
		this.setSpeed(1);
		Sprite shipsprite = new Sprite(Assets.manager.get("map/shuttle.png",Texture.class));
		shipsprite.setOriginCenter();
		this.setSprite(shipsprite);
		this.setX(0);
		this.setY(0);
	}

}
