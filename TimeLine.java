

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
			
			Map<Entity, AnimationData> timeFrame;
			
			if (frame < timeFrames.size()) {
				timeFrame = timeFrames.get(frame);
			} else {
				timeFrame = new HashMap<Entity, AnimationData>();
				
				if (frame > 0) {
					for (Map.Entry<Entity, AnimationData> entry : timeFrames.get(frame - 1).entrySet()) {
						Entity entity = entry.getKey();
						AnimationData data = entry.getValue();
						
						timeFrame.put(entity, data.copy());
					}
				}
				timeFrames.add(timeFrame);
			}
			
			for (Entity entity : selectedEntities) {
				AnimationData data = new AnimationData();
				data.visible = entity.visible;
				data.position = entity.getPosition();
				
				timeFrame.put(entity, data);
			}
			setCurFrame(frame);
			
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
	public boolean cloning;
	
	public TimeLine(CanvasView view, JSlider slider) {
		cloning = false;
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
		if (curFrame < 0) {
			return;
		}
		
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
	
	private int startFrame;
	
	public void cloneFrames() {
		if (cloning) {
			return;
		}
		cloning = true;
		startFrame = curFrame;
	}
	
	public void finishCloneFrames() {
		if (!cloning) {
			return;
		}
		cloning = false;
		
		int numNewFrames = curFrame - startFrame;
		
		if (numNewFrames <= 0) {
			return;
		}
		
		int prevLastPosition = timeFrames.size() - 1;
		
		System.out.println("NUM NEW FRAMES " + numNewFrames);
		
		for (int i = 0; i < numNewFrames; i++) {
			timeFrames.add(new HashMap<Entity, AnimationData>());
		}
		
		int counter1 = 0;
	}
	
//	public void finishCloneFrames() {
//		if (!cloning) {
//			return;
//		}
//		cloning = false;
//		
//		System.out.println("FINISH CLONE");
//		
//		int numNewFrames = curFrame - startFrame;
//		
//		if (numNewFrames <= 0) {
//			return;
//		}
//		
//		int prevLastPosition = timeFrames.size() - 1;
//		
//		System.out.println("NUM NEW FRAMES " + numNewFrames);
//		
//		for (int i = 0; i < numNewFrames; i++) {
//			timeFrames.add(new HashMap<Entity, AnimationData>());
//		}
//		
//		int counter1 = 0;
//		
//		int numFramesAfterCopySegment = prevLastPosition - numNewFrames;
//		for (int i = 0; i < numFramesAfterCopySegment; i++) {
//			counter1++;
//			
//			int originPosition = prevLastPosition - i;
//			Map<Entity, AnimationData> originFrame = timeFrames.get(originPosition);
//			Map<Entity, AnimationData> targetFrame = timeFrames.get(originPosition + numNewFrames);
//			
//			for (Map.Entry<Entity, AnimationData> entry : originFrame.entrySet()) {
//				targetFrame.put(entry.getKey(), entry.getValue().copy());
//			}
//		}
//		
//		int counter2 = 0;
//		
//		for (int i = startFrame + 1; i <= prevLastPosition; i++) {
//			counter2++;
//			Map<Entity, AnimationData> sourceFrame = timeFrames.get(startFrame);
//			Map<Entity, AnimationData> targetFrame = timeFrames.get(i);
//
//			targetFrame.clear();
//			for (Map.Entry<Entity, AnimationData> entry : sourceFrame.entrySet()) {
//				targetFrame.put(entry.getKey(), entry.getValue().copy());
//			}
//		}
//		
//		System.out.println(counter1);
//		System.out.println(counter2);
//		
//		slider.setMaximum(slider.getMaximum() + numNewFrames);
//	}
}
