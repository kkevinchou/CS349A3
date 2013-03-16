package pokemon;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneModel implements IModel {
	private List<Entity> entities;
	private List<IView> views;
	private Entity currentModel;
	
	public SceneModel() {
		currentModel = null;
		entities = new ArrayList<Entity>();
		views = new ArrayList<IView>();
	}
	
	public void erase(int startX, int startY, int endX, int endY) {
		Point start = new Point(startX, startY);
		Point end = new Point(endX, endY);
		
		Line2D eraseLine = new Line2D.Float(start, end);
		
		int i = 0;
		while (i < entities.size()) {
			Entity entity = entities.get(i);
			
			boolean intersects = false;
			for (Line2D line : entity.getLines()) {
				if (eraseLine.intersectsLine(line)) {
					intersects = true;
				}
			}
			
			if (intersects) {
				entities.remove(i);
			} else {
				i++;
			}
		}
		
		updateAllViews();
	}
	
	public void beginSelection(int x, int y) {
		
	}
	
	public void addPointToSelection(int x, int y) {
		
	}
	
	public void endSelection() {
		
	}

	public void beginEntity(int x, int y) {
		currentModel = new Entity(x, y);
		entities.add(currentModel);
	}
	
	public void addPointToEntity(int x, int y) {
		currentModel.addPoint(x, y);
		updateAllViews();
	}
	
	public void finishEntity() {
		currentModel = null;
	}
	
	public List<Entity> getEntities() {
		return Collections.unmodifiableList(entities);
	}

	@Override
	public void addView(IView view) {
		views.add(view);
	}

	@Override
	public void removeView(IView view) {
		views.remove(view);
	}
	
	private void updateAllViews() {
		for (IView view : views) {
			view.updateView();
		}
	}
}
