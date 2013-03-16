package pokemon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class CanvasView implements IView {
	private SceneModel model;
	private Graphics2D graphics;
	
	public CanvasView(SceneModel model) {
		this.model = model;
		this.model.addView(this);
	}

	@Override
	public void updateView() {
		if (graphics == null) {
			return;
		}
		
		List<Entity> entities = model.getEntities();
		
		for (Entity entity : entities) {
			List<Point> points = entity.getPoints();
			Point prevPoint = points.get(0);
			
			for (int i = 1; i < points.size(); i++) {
				Point curPoint = points.get(i);
				graphics.drawLine(prevPoint.x, prevPoint.y, curPoint.x, curPoint.y);
				prevPoint = curPoint;
			}
		}
	}
	
	public void setGraphics(Graphics2D graphics) {
		this.graphics = graphics;
	}
}
