

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JSlider;

@SuppressWarnings("serial")
class SketchController extends JComponent {
	private Mode mode;
	private Image image;
	private Graphics2D graphics2D;
	private int currentX, currentY, oldX, oldY;
	private int oldWidth, oldHeight;
	
	private CanvasView canvasView;
	private SceneModel sceneModel;
	
	public TimeLine timeLine;
	JSlider slider;
	
	Entity newEntity;
	Polygon selection;
	List<Entity> selectedEntities;
	List<Entity> clipBoardEntities;
	SketchController self = this;
	
	int mouseX;
	int mouseY;

	public SketchController() {
		setMode(Mode.DRAW);
		setDoubleBuffered(false);
		
		sceneModel = new SceneModel();
		selectedEntities = new ArrayList<Entity>();
		clipBoardEntities = new ArrayList<Entity>();
		selection = new Polygon();
		canvasView = new CanvasView(sceneModel, this, selection, selectedEntities);
		newEntity = null;
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				oldX = x;
				oldY = y;
				
				if (mode == Mode.BAD) {
					mode = Mode.SELECT;
				}
				
				if (mode == Mode.SELECT || mode == Mode.ANIMATE) {
					if (selection.contains(x, y)) {
						setMode(Mode.ANIMATE);
					} else {
						setMode(Mode.SELECT);
					}
				}
				
				if (mode == Mode.DRAW) {
					clearSelection();
					
					newEntity = new Entity(x, y);
					sceneModel.addEntity(newEntity);
					timeLine.addEntity(newEntity);
				} else if (mode == Mode.ERASE) {
					clearSelection();
				} else if (mode == Mode.SELECT) {
					clearSelection();
					selection.addPoint(x, y);
				} else if (mode == Mode.ANIMATE) {
					if (selectedEntities.size() > 0) {
						timeLine.record(selectedEntities);
					} else {
						mode = Mode.BAD;
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
			
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				currentX = x;
				currentY = y;
				
				if (mode == Mode.DRAW) {
					newEntity.addPoint(x, y);
				} else if (mode == Mode.ERASE) {
					Point start = new Point(oldX, oldY);
					Point end = new Point(currentX, currentY);
					
					Line2D eraseLine = new Line2D.Float(start, end);
					
					List<Entity> entities = sceneModel.getEntities();
					for (Entity entity : entities) {
						boolean intersects = false;
						for (Line2D line : entity.getLines()) {
							if (eraseLine.intersectsLine(line)) {
								intersects = true;
								break;
							}
						}
						
						if (intersects) {
							entity.visible = false;
							timeLine.erase(entity);
						}
					}
				} else if (mode == Mode.SELECT) {
					selection.addPoint(x, y);
				} else if (mode == Mode.ANIMATE) {
					int dx = currentX - oldX;
					int dy = currentY - oldY;
					
					selection.translate(dx, dy);
					for (Entity entity : selectedEntities) {
						entity.translate(dx, dy);
					}
				}
				
				oldX = currentX;
				oldY = currentY;
			}
		});
		
		addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
            	self.requestFocus();
            	timeLine.finishCloneFrames();
            	
            	if (mode == Mode.DRAW) {
            		newEntity = null;
            	} else if (mode == Mode.SELECT) {
            		List<Entity> entities = sceneModel.getEntities();
                	for (Entity entity : entities) {
	        			boolean fullyContained = true;
	        			
	        			List<Point> points = entity.getPoints();
	        			for (Point point : points) {
	        				if (!selection.contains(point)) {
	        					fullyContained = false;
	        					break;
	        				}
	        			}
	        			
	        			if (fullyContained && entity.visible == true) {
	        				selectedEntities.add(entity);
	        			}
	        		}
            	} else if (mode == Mode.ANIMATE) {
            		timeLine.endRecord();
            	}
            }
        });
		
		addKeyListener(new KeyAdapter() {
			
			private boolean ctrlDown = false;
			
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlDown = true;
				} else if (e.getKeyCode() == KeyEvent.VK_C) {
					if (ctrlDown) {
						copySelection();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_V) {
					if (ctrlDown) {
						pasteSelection();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_Q) {
					System.exit(0);
				}
			}
			
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlDown = false;
				}
			}
        });
	}
	
	private void copySelection() {
		clipBoardEntities.clear();
		for (Entity entity : selectedEntities) {
			clipBoardEntities.add(entity.copy());
		}
	}
	
	private void pasteSelection() {
		for (Entity entity : clipBoardEntities) {
			Entity entityCopy = entity.copy();
			entityCopy.setPosition(new Vector2D(mouseX, mouseY));
			sceneModel.addEntity(entityCopy);
			timeLine.addEntity(entityCopy);
		}
	}
	
	public void cloneFrames() {
		timeLine.cloneFrames();
	}
	
	public void finishCloneFrames() {
		timeLine.finishCloneFrames();
	}
	
	public void clearSelection() {
		selectedEntities.clear();
		selection.reset();
	}
	
	public void setSlider(JSlider slider) {
		this.slider = slider;
		timeLine = new TimeLine(canvasView, slider);
	}
	
	public void redraw() {
		repaint();
	}
	
	public void playAnimation() {
		clearSelection();
		timeLine.play();
	}
	
	public void pauseAnimation() {
		timeLine.pause();
	}

	public void paintComponent(Graphics g) {
		if (image == null || oldWidth != getSize().width || oldHeight != getSize().height) {
			oldWidth = getSize().width;
			oldHeight = getSize().height;
			initImage();
		}

		canvasView.updateView();
		g.drawImage(image, 0, 0, null);
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public void clear() {
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
	}
	
	private void initImage() {
		image = createImage(getSize().width, getSize().height);
		graphics2D = (Graphics2D) image.getGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		canvasView.setGraphics(graphics2D);
		clear();
	}
}