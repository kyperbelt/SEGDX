package com.segdx.game.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {
	public static final int ALPHA = 0;
	public static final int ROTATE = 9;

	@Override
	public int getValues(Sprite target, int type, float[] values) {
		switch (type) {
		case ALPHA: values[0] = target.getColor().a;
			return 1;
		case ROTATE:
			values[0] = target.getRotation();
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
		case ROTATE :
			target.setRotation(values[0]);
			break;
		default:
			break;
		}
	}
	
	public static float getAngle(Vector2 point1,Vector2 point2){
		float angle = (float) Math.toDegrees(Math.atan2(point2.y - point1.y, point2.x - point1.x));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}

}
