

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entity {
	private Vector2D position;
	private List<Point> points;
	
	public boolean visible;
	
	public Entity(int x, int y) {
		visible = true;
		position = new Vector2D(x, y);
		points = new ArrayList<Point>();
		addPoint(x, y);
	}
	
	public List<Point> getPoints() {
		return Collections.unmodifiableList(points);
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	
	public List<Line2D> getLines() {
		List<Line2D> lines = new ArrayList<Line2D>();
		
		for (int i = 0; i < points.size() - 1; i++) {
			lines.add(new Line2D.Float(points.get(i), points.get(i + 1)));
		}
		
		return Collections.unmodifiableList(lines);
	}
	
	public void translate(int dx, int dy) {
		position = position.add(new Vector2D(dx, dy));
		for (Point point : points) {
			point.translate(dx, dy);
		}
	}
	
	public void translate(Vector2D translation) {
		translate((int)translation.x, (int)translation.y);
	}
	
	public Vector2D getPosition() {
		return position.copy();
	}
	
	public void setPosition(Vector2D position) {
		Vector2D translation = position.sub(this.position);
		translate(translation);
	}
	
	public Entity copy() {
		Entity entity = new Entity((int)position.x, (int)position.y);
		
		for (int i = 1; i < points.size(); i++) {
			Point point = points.get(i);
			entity.addPoint(point.x, point.y);
		}
		
		return entity;
	}
}
