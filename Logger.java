import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Logger
{

    public Logger(String s)
    {
        fileName = s;
        _verbose = true;
    }

    void setVerbose(boolean flag)
    {
        _verbose = flag;
    }

    public void logMessage(String s)
    {
        try
        {
            if(log == null)
                log = new BufferedWriter(new FileWriter(new File(fileName)));
	    s = (new Date()).toString() +" : " + s + "\n";
            log.write(s);
            if(_verbose)
                System.out.println(s);
            log.flush();
        }
        catch(IOException ioexception)
        {
            System.out.println(ioexception);
        }
    }

    BufferedWriter log;
    String fileName;
    boolean _verbose;
}
