package de.haw.informatik.devsupport.wp1516.audime;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.AgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.controller.AsyncReceiverTask;
import de.haw.informatik.devsupport.wp1516.audime.controller.Controller;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
public class TimeSyncUnitTest {


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Test
    public void timesync() throws Exception {
        Controller controller = new Controller(new NetworkServiceMock(), new AgentCommunicationService("localhost", 12345), false);
        AsyncReceiverTask task = controller.createReceiverTask(null);
        task.execute();
        while (task.getStatus() != AsyncTask.Status.FINISHED)
            Thread.sleep(1000);
        assertTrue("Time offset should be 50 but is: " + (controller.getTimeService().getTime()-System.currentTimeMillis()),controller.getTimeService().getTime() == System.currentTimeMillis()+50);
    }
}