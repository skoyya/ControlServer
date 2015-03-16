import java.io.IOException;
import java.util.*;

class HeartBeatServer extends Thread
{

    public HeartBeatServer( Server server )
    {
	_server = server;
    }

    public void run()
    {
        try
        {
            //Thread.currentThread();
            Thread.sleep(1000L);
            do
            {
                _server.broadCast((new StringBuilder()).append("HEART_BEAT#").append(new Date()).toString()+"\n");
                Thread.sleep(SLEEPTIME);
            } while(true);
        }
        catch(Exception exception)
        {
	    _server.getLogger().logMessage("FATAL_ALERT : HeartBeat Server is down........");	    
            return;
        }
    }

    Server _server;
    final long SLEEPTIME = 5000L;
}
