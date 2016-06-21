package de.haw.informatik.devsupport.wp1516.audime;

import android.annotation.TargetApi;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.haw.informatik.devsupport.wp1516.audime.controller.AsyncReceiverTask;
import de.haw.informatik.devsupport.wp1516.audime.controller.Controller;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TimeSyncUnitTest {


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Test
    public void testTimesync() throws Exception {
        Controller controller = new Controller(new NetworkServiceMock());
        AsyncReceiverTask task = controller.createReceiverTask(null);
        task.actAsReceiver();
        assertTrue(controller.getTimeService().getTime() == System.currentTimeMillis()+50);
    }
}