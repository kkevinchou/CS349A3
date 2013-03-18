

public class Vector2D {
	public double x;
	public double y;
	
	public Vector2D() {
		x = 0;
		y = 0;
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return x + ", " + y;
	}
	
	public double dot(Vector2D vector) {
		return x * vector.x + y * vector.y;
	}
	
	public double normalizedProjection(Vector2D v) {
		return dot(v.normalize());
	}
	
	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vector2D normalize() {
		return new Vector2D(x / magnitude(), y / magnitude());
	}

	public static Vector2D ZERO() {
		return new Vector2D();
	}
	
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
	
	public Vector2D sub(Vector2D vector) {
		return new Vector2D(x - vector.x, y - vector.y);
	}
	
	public Vector2D add(Vector2D vector) {
		return new Vector2D(x + vector.x, y + vector.y);
	}
	
	public Vector2D mult(double scalar) {
		return new Vector2D(x * scalar, y * scalar);
	}
	
	public double angleBetween(Vector2D v) {
		if (this.magnitude() == 0 || v.magnitude() == 0) {
			return Double.NaN;
		}
		
		double dotValue = this.dot(v);
		
		double dotNormalized = dotValue / this.magnitude() / v.magnitude();
		double angle = Math.acos(dotNormalized);
		
		double angle1 = Math.atan2(this.y, this.x);
		double angle2 = Math.atan2(v.y, v.x);
		
		if (angle1 < 0) {
			angle1 += 2 * Math.PI;
		}
		
		if (angle2 < 0) {
			angle2 += 2 * Math.PI;
		}
		
		// This is to handle the edge case where the mouse moves past
		// The 0 angle
		
		double shiftedAngle = angle1 - angle2;
		if (shiftedAngle < 0) {
			shiftedAngle += 2 * Math.PI;
		}
		
		if (shiftedAngle > Math.PI) {
			angle = -angle;
		}
		
		return angle;
	}
	
	Vector2D rotateRelative(double angle, double pivotx, double pivoty) {
		Vector2D rotatedVector = new Vector2D();
		
		double tx = x - pivotx;
		double ty = y - pivoty;

		double tx2 = tx * Math.cos(angle) - ty * Math.sin(angle);
		double ty2 = tx * Math.sin(angle) + ty * Math.cos(angle);

		tx2 += pivotx;
		ty2 += pivoty;

		rotatedVector.x = tx2;
		rotatedVector.y = ty2;
		
		return rotatedVector;
	}
	
	Vector2D rotate(double angle) {
		return rotateRelative(angle, 0, 0);
	}
}