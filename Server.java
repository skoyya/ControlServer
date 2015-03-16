import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.*;

class Server extends Thread
{

    public Server()
    {
	_heartBeatServer = new HeartBeatServer(this);
        _mapClients = new HashMap<String,ClientConnection>();
	_logger = new Logger("liveControlServer.log");
    }

    public void run()
    {
	_heartBeatServer.start();
        try
        {
            _serverSock = new ServerSocket(19000);
        }
        catch(IOException ioexception)
        {
            _logger.logMessage((new StringBuilder()).append("Error in server socket").append(ioexception).toString());
            return;
        }
        _logger.logMessage("Waiting for Client's connection ...");
        try
        {
            do
            {
                java.net.Socket socket = _serverSock.accept();
                ClientConnection clientconnection = new ClientConnection(this, socket);
                clientconnection.start();
            } while(true);
        }
        catch(IOException ioexception1)
        {
            _logger.logMessage((new StringBuilder()).append("IOException: <").append(ioexception1).append(">").toString());
        }
        catch(Exception exception)
        {
            _logger.logMessage((new StringBuilder()).append("Exception: <").append(exception).append(">").toString());
        }
    }

    public synchronized void onMessage(String s, ClientConnection clientconnection)
        throws IOException
    {
        _logger.logMessage("Message from Client :"+s);
        String s1 = s.substring(0, 10);
        if(s1.equals("LOGON_CLNT"))
        {
            String s2 = s.substring(10).split("@")[0];
            if(_mapClients.get(s2) != null)
            {
                _logger.logMessage(s2 + " Client already connected");
                clientconnection.Stop();
                return;
            }
            _mapClients.put(s2, clientconnection);
            clientconnection.writeToClient(("LOGON_SUCC# Login success message" ));
            clientconnection.setClntName(s2);
        } else
        if(s1.equals("LOGOUT_CLNT"))
	{
		clientDisconnected(clientconnection);
	}
	else
        if(!s1.equals("HEART_BEAT"));
    }

    public synchronized void clientDisconnected(ClientConnection clientconnection)
    {
        _logger.logMessage("Client :"+clientconnection.getClntName() + " Disconnected");
        clientconnection.Stop();
        _mapClients.remove(clientconnection.getClntName());
    }

    public synchronized void dispatch(String s, String s1)
        throws IOException
    {
        _logger.logMessage("Sending to client: " + s );
        ((ClientConnection)_mapClients.get(s)).writeToClient(s1);
    }

    public void broadCast(String s)
        throws IOException
    {
        for(Iterator iterator = _mapClients.keySet().iterator(); iterator.hasNext(); dispatch((String)iterator.next(), s));
    }

    public ClientConnection getClient(String s)
    {
        return (ClientConnection)_mapClients.get(s);
    }

    public HashMap getAllClients()
    {
        return _mapClients;
    }

    public Logger getLogger() { return _logger ; } 
    public static void main(String[] args)
    {
	Server server = new Server();
	server.start();
    }

    Logger _logger;
    ServerSocket _serverSock;
    HeartBeatServer _heartBeatServer;
    HashMap _mapClients;
}
