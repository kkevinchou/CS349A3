

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

enum Mode {
	DRAW,
	ERASE,
	SELECT,
	ANIMATE,
	BAD
}

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("SKETCHERS");
		
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());

		final Sketch sketch = new Sketch();
		content.add(sketch, BorderLayout.CENTER);
		
		JToolBar toolbar = createToolBar(sketch);
		content.add(toolbar, BorderLayout.NORTH);
		
		JPanel panel = createPanel(sketch);
		content.add(panel, BorderLayout.SOUTH);
		
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private static JToolBar createToolBar(final Sketch sketch) {
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
	
	private static JPanel createPanel(final Sketch sketch) {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel subPanel = new JPanel();
		
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sketch.playAnimation();
			}
		});
		subPanel.add(playButton);
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sketch.pauseAnimation();
			}
		});
		subPanel.add(pauseButton);
		
		panel.add(subPanel, BorderLayout.PAGE_END);
		
		JSlider animationSlider = new JSlider();
		animationSlider.setBorder(BorderFactory.createTitledBorder("Animation Slider"));
		animationSlider.setValue(0);
		animationSlider.setMaximum(0);
		animationSlider.setMajorTickSpacing(16);
		
		animationSlider.setPaintTicks(true);
		panel.add(animationSlider, BorderLayout.CENTER);
		
		sketch.setSlider(animationSlider);
		animationSlider.addChangeListener(new SliderListener(sketch.timeLine));
		animationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
		        JSlider source = (JSlider)e.getSource();
				if (source.getValueIsAdjusting()) {
					sketch.clearSelection();
		        }
			}
		});
		
		return panel;
	}
}