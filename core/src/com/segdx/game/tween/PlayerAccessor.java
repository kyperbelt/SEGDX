package com.segdx.game.tween;

import com.segdx.game.entity.Player;

import aurelienribon.tweenengine.TweenAccessor;

public class PlayerAccessor implements TweenAccessor<Player> {
	public static final int POSITION = 9;

	@Override
	public int getValues(Player target, int type, float[] values) {
		switch (type) {
		case POSITION:
				values[0] = target.getOriginPosition().x;
				values[1] = target.getOriginPosition().y;
			return 2;

		default:
			break;
		}
		return -1;
	}

	@Override
	public void setValues(Player target, int type, float[] values) {
		switch (type) {
		case POSITION:
			target.setX(values[0]);
			target.setY(values[1]);
			break;

		default:
			break;
		}
	}

}
