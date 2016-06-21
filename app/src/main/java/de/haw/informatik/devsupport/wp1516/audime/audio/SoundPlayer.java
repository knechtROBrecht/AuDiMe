package de.haw.informatik.devsupport.wp1516.audime.audio;

import android.media.AudioManager;
import android.media.ToneGenerator;

import de.haw.informatik.devsupport.wp1516.audime.time.ITimeService;
import de.haw.informatik.devsupport.wp1516.audime.time.TimeService;


public class SoundPlayer {

	private ITimeService timeService;
    private ToneGenerator toneG;
	private long timing;

	public SoundPlayer(ITimeService timeService){
		this.timeService = timeService;
	}

	public void stopSound() {
		if (toneG != null) {
			toneG.release();
			toneG = null;
		}
	}

	public void playBeep() {
        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 200);
		timing = timeService.getTime();
		toneG.startTone(0, 200);
    }

	public long getTiming(){
		return this.timing;
	}
}
