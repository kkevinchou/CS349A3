package pokemon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JSlider;
import javax.swing.Timer;

public class TimeLine {
	class RecordTimer implements ActionListener {
		private List<Map<Entity, AnimationData>> timeFrames;
		
		public RecordTimer(List<Map<Entity, AnimationData>> timeFrames) {
			this.timeFrames = timeFrames;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int frame = slider.getValue();
			
			while (timeFrames.size() > frame) {
				timeFrames.remove(frame);
			}
			curFrame = frame;
			
			Map<Entity, AnimationData> timeFrame = new HashMap<Entity, AnimationData>();
			for (Entity entity : selectedEntities) {
				AnimationData data = new AnimationData();
				data.visible = entity.visible;
				data.position = entity.getPosition();
				
				timeFrame.put(entity, data);
			}
			timeFrames.add(timeFrame);
			slider.setMaximum(timeFrames.size());
			slider.setValue(frame + 1);
		}
	}
	
	class AnimationTimer implements ActionListener {
		private List<Map<Entity, AnimationData>> timeFrames;
		
		public AnimationTimer(List<Map<Entity, AnimationData>> timeFrames) {
			this.timeFrames = timeFrames;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int frame = slider.getValue();
			
			if (frame >= timeFrames.size()) {
				timer.stop();
				return;
			}
			
			setCurFrame(frame);
			view.updateView();
			slider.setValue(frame + 1);
		}
	}

	private List<Entity> selectedEntities;
	private List<Map<Entity, AnimationData>> timeFrames;
	
	private RecordTimer recordTimer;
	private AnimationTimer animationTimer;
	private Timer timer;
	private CanvasView view;
	private JSlider slider;
	
	private int curFrame;
	
	public TimeLine(CanvasView view, JSlider slider) {
		curFrame = 0;
		this.view = view;
		timeFrames = new ArrayList<Map<Entity, AnimationData>>();
		recordTimer = new RecordTimer(timeFrames);
		animationTimer = new AnimationTimer(timeFrames);
		timer = new Timer(16, null);
		this.slider = slider;
	}
	
	public void setCurFrame(int frame) {
		curFrame = (frame >= timeFrames.size()) ? timeFrames.size() - 1 : frame;
		
		Map<Entity, AnimationData> timeFrame = timeFrames.get(curFrame);
		
		for (Map.Entry<Entity, AnimationData> entry : timeFrame.entrySet()) {
			Entity entity = entry.getKey();
			AnimationData data = entry.getValue();
			
			entity.setPosition(data.position);
			entity.visible = data.visible;
		}
	}
	
	public void record(List<Entity> selectedEntities) {
		timer.stop();
		this.selectedEntities = selectedEntities;
		timer = new Timer(16, recordTimer);
		timer.start();
	}
	
	public void endRecord() {
		timer.stop();
	}
	
	public void play() {
		timer.stop();
		timer = new Timer(16, animationTimer);
		timer.start();
	}
	
	public void pause() {
		timer.stop();
	}
	
	public void erase(Entity entity) {
		for (int i = curFrame; i < timeFrames.size(); i++) {
			Map<Entity, AnimationData> frame = timeFrames.get(i);
			AnimationData data = frame.get(entity);
			data.visible = false;
		}
	}
	
	public void addEntity(Entity entity) {
		for (int i = 0; i < curFrame; i++) {
			Map<Entity, AnimationData> frame = timeFrames.get(i);
			
			AnimationData data = new AnimationData();
			data.position = entity.getPosition();
			data.visible = false;
			
			frame.put(entity,  data);
		}
	}
}
