package de.haw.informatik.devsupport.wp1516.audime.audio;

import java.io.IOException;

public interface IAudioService {
	
	/**
	 * Spielt das Signal ab
	 * @return true wenn das Signal erfolgreich abgespielt worden ist
	 * @throws IOException
	 */
	boolean playSignal() throws IOException;
	
	/**
	 * Stoppt das abspielen des Signals
	 * @throws IOException
	 */
	void stopSignal() throws IOException;
	
	/**
	 * Startet die Soundaufnahme
	 * @throws IOException
	 */
	void startRecord() throws IOException;
	
	/**
	 * Stoppt die Soundaufnahme
	 * @throws IOException
	 */
	void stopRecord() throws IOException;
	
	/**
	 * Liefert den Zeitpunkt des gefundenen Tones
	 * @return
	 */
	long getResult();

	/**
	 *
	 * @return Zietpunkt zu dem das Signa gespielt wurde
	 */
	long getSignalPlayedTimestamp();
}
