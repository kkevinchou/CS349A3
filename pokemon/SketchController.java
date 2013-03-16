package pokemon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;

@SuppressWarnings("serial")
class SketchController extends JComponent {
	private Mode mode;
	private Image image;
	private Graphics2D graphics2D;
	private int currentX, currentY, oldX, oldY;
	
	private CanvasView canvasView;
	private SceneModel sceneModel;

	public SketchController() {
		mode = Mode.DRAW;
		setDoubleBuffered(false);
		
		sceneModel = new SceneModel();
		canvasView = new CanvasView(sceneModel);
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				oldX = e.getX();
				oldY = e.getY();
				
				if (mode == Mode.DRAW) {
					sceneModel.beginEntity(e.getX(), e.getY());
				} else if (mode == Mode.SELECT) {
					
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				clear();
				
				currentX = e.getX();
				currentY = e.getY();
				
				if (mode == Mode.DRAW) {
					sceneModel.addPointToEntity(e.getX(), e.getY());
				} else if (mode == Mode.ERASE) {
					sceneModel.erase(oldX, oldY, currentX, currentY);
				}
				
				oldX = currentX;
				oldY = currentY;
				
				repaint();
			}
		});
		
		addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
            	if (mode == Mode.DRAW) {
                	sceneModel.finishEntity();
            	}
            }
        });
	}

	public void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D) image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			canvasView.setGraphics(graphics2D);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	private void clear() {
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
	}
}