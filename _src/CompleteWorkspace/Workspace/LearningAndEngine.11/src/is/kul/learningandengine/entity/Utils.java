package is.kul.learningandengine.entity;

import is.kul.learningandengine.GameActivity;

import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

public class Utils {
	public static void wraparound(CollidableEntity ce) {
		
		if (ce.getX() + ce.getWidth() / 2 < 0) {
			ce.getBody().setTransform((GameActivity.CAMERA_WIDTH + ce.getWidth() / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					ce.getBody().getPosition().y, 0);
		} else if (ce.getX() - ce.getWidth() / 2 > GameActivity.CAMERA_WIDTH) {
			ce.getBody().setTransform((- ce.getWidth() / 2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					ce.getBody().getPosition().y, 0);
		}	
	}
}
