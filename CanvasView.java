

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.List;

public class CanvasView implements IView {
	private SceneModel model;
	private Graphics2D graphics;
	private Sketch sketch;
	private Polygon selection;
	List<Entity> selectedEntities;
	
	public CanvasView(SceneModel model, Sketch sketch, Polygon selection, List<Entity> selectedEntities) {
		this.model = model;
		this.sketch = sketch;
		this.model.addView(this);
		this.selection = selection;
		this.selectedEntities = selectedEntities;
	}

	@Override
	public void updateView() {
		if (graphics == null) {
			return;
		}
		
		sketch.clear();
		
		List<Entity> entities = model.getEntities();
		
		graphics.setColor(new Color(255, 132, 44));
		for (Entity entity : entities) {
			if (!entity.visible) {
				continue;
			}
			
			List<Point> points = entity.getPoints();
			
			Stroke normalStyle = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
			Stroke selectedStyle = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
			
			Stroke style = normalStyle;
			if (selectedEntities.contains(entity)) {
				style = selectedStyle;
			}
			graphics.setStroke(style);
			
			Point prevPoint = points.get(0);
			for (int i = 1; i < points.size(); i++) {
				Point curPoint = points.get(i);
//				graphics.drawLine(prevPoint.x + dx, prevPoint.y + dy, curPoint.x + dx, curPoint.y + dy);
				graphics.drawLine(prevPoint.x, prevPoint.y, curPoint.x, curPoint.y);
				prevPoint = curPoint;
			}
		}
		
//		Polygon selection = model.getSelection();
		if (selection != null) {
			Stroke drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
			graphics.setStroke(drawingStroke);
			graphics.setColor(new Color(0, 74, 255));
			graphics.drawPolyline(selection.xpoints, selection.ypoints, selection.npoints);
		}
		
		sketch.redraw();
	}
	
	public void setGraphics(Graphics2D graphics) {
		this.graphics = graphics;
	}
}
