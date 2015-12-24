package com.segdx.game.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {
	public static final int ALPHA = 0;

	@Override
	public int getValues(Sprite target, int type, float[] values) {
		switch (type) {
		case ALPHA: values[0] = target.getColor().a;
			return 1;
		default:
			return -1;
		}
	}

	@Override
	public void setValues(Sprite target, int type, float[] values) {
		switch (type) {
		case ALPHA:
			target.setAlpha(values[0]);
			break;

		default:
			break;
		}
	}

}
