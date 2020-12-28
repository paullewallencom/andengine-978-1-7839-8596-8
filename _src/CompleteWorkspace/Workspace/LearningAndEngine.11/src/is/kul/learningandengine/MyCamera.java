package is.kul.learningandengine;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.IEntity;

public class MyCamera extends SmoothCamera {
	
	private IEntity chaseEntity;
	
	private boolean gameOver = false;

	public MyCamera(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight, 3000f, 1000f, 1f);
	}
	
	@Override
	public void setChaseEntity(IEntity pChaseEntity) {
		super.setChaseEntity(pChaseEntity);
		this.chaseEntity = pChaseEntity;
	}

	/**
	 * Updates the chase entity only when it reaches higher point and doesn't update x coordinate
	 */
	@Override
	public void updateChaseEntity() {
		if (chaseEntity != null) {
			if (chaseEntity.getY() > getCenterY()) {
				setCenter(getCenterX(), chaseEntity.getY());
			} else if (chaseEntity.getY() < getYMin() && !gameOver) {
				setCenter(getCenterX(), chaseEntity.getY() - getHeight());
				gameOver = true;
			}
		}
	}

	@Override
	public void reset() {
		super.reset();
		gameOver = false;
		set(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
		setCenterDirect(GameActivity.CAMERA_WIDTH / 2, GameActivity.CAMERA_HEIGHT / 2);
	}
	
	

}
