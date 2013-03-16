package pokemon;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;

enum Mode {
	DRAW,
	ERASE,
	SELECT
}

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("SKETCHERS");
		
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());

		final SketchController sketch = new SketchController();
		content.add(sketch, BorderLayout.CENTER);
		
		JToolBar toolbar = createToolBar(sketch);
		content.add(toolbar, BorderLayout.NORTH);
		
		JPanel panel = createPanel();
		content.add(panel, BorderLayout.SOUTH);
		
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private static JToolBar createToolBar(final SketchController sketch) {
		JToolBar toolbar = new JToolBar();
		toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sketch.setMode(Mode.DRAW);
			}
		});
		toolbar.add(drawButton);
		
		JButton eraseButton = new JButton("Erase");
		eraseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sketch.setMode(Mode.ERASE);
			}
		});
		toolbar.add(eraseButton);
		
		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sketch.setMode(Mode.SELECT);
			}
		});
		toolbar.add(selectButton);
		
		return toolbar;
	}
	
	private static JPanel createPanel() {
		JPanel panel = new JPanel();
		
		JSlider animationSlider = new JSlider();
		animationSlider.setBorder(BorderFactory.createTitledBorder("Animation Slider"));
		animationSlider.setValue(0);
		animationSlider.setMaximum(0);
		panel.add(animationSlider);
		
		return panel;
	}
}