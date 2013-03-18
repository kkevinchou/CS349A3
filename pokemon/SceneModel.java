package pokemon;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SceneModel implements IModel {
	private List<Entity> entities;
	private List<IView> views;
	public Polygon selection;
	public List<Entity> selectedEntities;
	
	public SceneModel() {
		entities = new ArrayList<Entity>();
		selectedEntities = new ArrayList<Entity>();
		views = new ArrayList<IView>();
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		updateAllViews();
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
