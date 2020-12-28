package is.kul.learningandengine.scene;

import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.util.adt.color.Color;

public class MyTextMenuItemDecorator extends ColorMenuItemDecorator {
	
	private TextMenuItem textMenuItem;

	public MyTextMenuItemDecorator(TextMenuItem textMenuItem, Color pSelectedColor,
			Color pUnselectedColor) {
		super(textMenuItem, pSelectedColor, pUnselectedColor);
		this.textMenuItem = textMenuItem;
	}
	
	public void setText(CharSequence text) {
		textMenuItem.setText(text);
	}
	
}
