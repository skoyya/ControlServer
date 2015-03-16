import java.io.*;
import java.net.Socket;
import java.util.Date;

class ClientConnection extends Thread
{

    ClientConnection(Server server, Socket socket)
    {
	
        _clientSock = socket;
        _callBackHandler = server;
    }

    public void run()
    {
        try
        {
            _outWriter = _clientSock.getOutputStream();
            InputStream inputstream = _clientSock.getInputStream();
            String s;
            for(byte abyte0[] = new byte[124]; inputstream.read(abyte0) != 0; _callBackHandler.onMessage(s, this))
               { s = new String(abyte0); s= s.split("\n")[0];}

        }
        catch(Exception exception)
        {
            _callBackHandler.clientDisconnected(this);
        }
    }

    public synchronized void writeToClient(String s)
        throws IOException
    {
        byte abyte0[] = s.getBytes();
        _outWriter.write(abyte0);
    }

    public void setClntName(String s)
    {
        _clientName = s;
    }

    public String getClntName()
    {
        return _clientName;
    }

    public void Stop()
    {
        try
        {
            _clientSock.close();
        }
        catch(Exception exception)
        {
            System.out.println("Oh no! "+ exception.toString());
        }
    }

    Socket _clientSock;
    Server _callBackHandler;
    String _clientName;
    OutputStream _outWriter;
}
