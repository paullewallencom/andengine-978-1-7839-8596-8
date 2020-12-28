package is.kul.learningandengine.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.opengl.util.GLState;

public class VerticalParallaxEntity extends ParallaxEntity {
	
	IEntity entity;
	float parallaxFactor;

	public VerticalParallaxEntity(float parallaxFactor, IEntity entity) {
		super(parallaxFactor, entity);
		this.entity = entity;
		this.parallaxFactor = parallaxFactor;
	}
	
	public void onDraw(final GLState pGLState, final Camera pCamera, final float pParallaxValue) {
		pGLState.pushModelViewGLMatrix();
		{
			final float cameraHeight = pCamera.getHeight();
			final float entityHeightScaled = entity.getHeight() * entity.getScaleY();
			float baseOffset = (pParallaxValue * parallaxFactor) % entityHeightScaled;

			while (baseOffset > 0) {
				baseOffset -= entityHeightScaled;
			}
			pGLState.translateModelViewGLMatrixf(0, baseOffset, 0);

			float currentMaxY = baseOffset;

			do {
				entity.onDraw(pGLState, pCamera);
				pGLState.translateModelViewGLMatrixf(0, entityHeightScaled, 0);
				currentMaxY += entityHeightScaled;
			} while (currentMaxY < cameraHeight);
		}
		pGLState.popModelViewGLMatrix();
	}	

}
