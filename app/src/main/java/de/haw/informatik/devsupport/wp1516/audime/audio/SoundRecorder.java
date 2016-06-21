package de.haw.informatik.devsupport.wp1516.audime.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import de.haw.informatik.devsupport.wp1516.audime.time.ITimeService;
import de.haw.informatik.devsupport.wp1516.audime.time.TimeService;

public class SoundRecorder extends Thread {

    private int sampleRate;
    private int channelCount;
    private int PCMEncoding;
    private AudioRecord aR;
    private ITimeService timeService;
    private long timing;
    private boolean soundFound;

    public SoundRecorder(ITimeService timeService){
        this.timeService = timeService;
        soundFound = false;
    }

    @Override
    public void start(){
//        aR = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_8BIT, buffersize);
        aR = findAudioRecord();
        aR.startRecording();
        super.start();
    }

    public void stopRecording(){
        try {
            aR.stop();
            aR.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            int ampLevel;
            int x = 0;
//            float bitsPerSecond = sampleRate * PCMEncoding * channelCount;
            float bitsPerSecond = sampleRate * PCMEncoding;
            float bytesPerSecond = bitsPerSecond / 8;
            int bufferSize = (int)bytesPerSecond / 1000;
            System.out.println("bytesPerSecond" + bytesPerSecond);
            System.out.println("BufferSize: " + bufferSize);
//            byte[] frame = new byte[bufferSize];
            //shor 16bit byte 8bit
            short[] frame = new short[bufferSize/2];

            long preTime = timeService.getTime();
//            while(((aR.read(frame, 0, frame.length) != -1) && !soundFound)){
            while(((aR.read(frame, 0, frame.length) != -1) && !soundFound)){
                x++;
//                if(x % 1000 == 0){
//                    System.out.println("Sekunde vergangen");
//                }

//                ampLevel = calculateRMSLevel(frame);
                ampLevel = (int)calculateRMSLevel3(frame);
                System.out.println(ampLevel);
                if(ampLevel > 20){
                    System.out.println("Sound detected after: " + x + " ms");
                    // ist der ganze quatsch nicht egal wenn wir hier eh nen timing uns holen?
                    timing = preTime + x;
                    return;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Hilfsmethode zum berechnen des Amplitude
     */
    private int calculateRMSLevel(byte[] audioData) {
        long lSum = 0;
        for (byte anAudioData : audioData) lSum = lSum + anAudioData;

        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0d;

        for (byte anAudioData : audioData) sumMeanSquare += Math.pow(anAudioData - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;

        return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
    }

    private double calculateRMSLevel2(short[] buffer) {
        double mAlpha, mGain;
        double mRmsSmoothed = 0;
        double rms = 0;
        for (int i = 0; i < buffer.length; i++) {
            rms += buffer[i] * buffer[i];
        }
        rms = Math.sqrt(rms / buffer.length);
        mAlpha = 0.9;   mGain = 0.0044;
            /*Compute a smoothed version for less flickering of the
            // display.*/
        mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
        double rmsdB = mGain * mRmsSmoothed > 0 ? 20.0 * Math.log10(mGain * mRmsSmoothed) : -999.99;
        return rmsdB;
    }

    private int calculateRMSLevel3(short[] buffer) {
        double rms = 0;
        for (int i = 0; i < buffer.length; i++) {
            rms += buffer[i] * buffer[i];
        }

        double amplitude = rms / buffer.length;
        return (int)Math.sqrt(amplitude);
    }


    public long getTiming(){ return this.timing; }

    private static int[] mSampleRates = new int[] { 44100, 22050, 11025, 8000 };
    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            sampleRate = rate;
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                PCMEncoding = audioFormat == 2 ? 16 : 8;
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d("", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                channelCount = channelConfig == 12 ? 2 : 1;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("", rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }

}
