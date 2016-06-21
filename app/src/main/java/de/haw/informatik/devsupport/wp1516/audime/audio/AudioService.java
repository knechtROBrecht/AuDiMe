package de.haw.informatik.devsupport.wp1516.audime.audio;

import java.io.IOException;

import de.haw.informatik.devsupport.wp1516.audime.time.ITimeService;
import de.haw.informatik.devsupport.wp1516.audime.time.TimeService;


public class AudioService implements IAudioService {

	private SoundPlayer player;
	private SoundRecorder recorder;
	private ITimeService timeService;

	public AudioService(ITimeService timeService) {
		this.timeService = timeService;
		this.player = new SoundPlayer(this.timeService);
	}

	@Override
	public boolean playSignal() throws IOException {
		try {
			player.playBeep();
		} catch (Exception e) {
            e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void stopSignal() throws IOException {
		player.stopSound();
	}

	@Override
	public void startRecord() throws IOException {
		this.recorder = new SoundRecorder(this.timeService);
		recorder.start();
	}

	@Override
	public void stopRecord() throws IOException {
		recorder.stopRecording();
	}

	@Override
	public long getResult() {
		return recorder.getTiming();
	}

    @Override
    public long getSignalPlayedTimestamp() {
        return player.getTiming();
    }

}
