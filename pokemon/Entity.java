package pokemon;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entity {
	private List<Point> points;
	
	public Entity(int x, int y) {
		points = new ArrayList<Point>();
		points.add(new Point(x, y));
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
}
