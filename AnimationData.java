

public class AnimationData {
	public boolean visible;
	public Vector2D position;
	
	public AnimationData() {
		visible = true;
		position = Vector2D.ZERO();
	}
	
	public AnimationData(boolean visible, Vector2D position) {
		this.visible = visible;
		this.position = position.copy();
	}
	
	public AnimationData copy() {
		return new AnimationData(visible, position);
	}
}
