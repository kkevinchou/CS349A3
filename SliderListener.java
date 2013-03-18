

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class SliderListener implements ChangeListener {
	TimeLine timeLine;
	
	public SliderListener(TimeLine timeLine) {
		this.timeLine = timeLine;
	}
	
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (source.getValueIsAdjusting() && !timeLine.cloning) {
            timeLine.setCurFrameAndDraw(source.getValue());
        } else if (timeLine.cloning) {
        	timeLine.setCurFrame(source.getValue());
        }
    }
}