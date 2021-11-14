package com.ghannah;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@RequiresApi(api = Build.VERSION_CODES.N)
public class ServerRequestHandler
{
    private String ip = null;
    private Socket socket = null;
    private Short port = 0;

    public ServerRequestHandler(final String ip, final Short port)
    {
        this.ip = ip;
        this.port = port;
    }

    public void setIp(final String ip)
    {
        this.ip = ip;
    }

    public void setPort(final Short port)
    {
        this.port = port;
    }

    public String getIp()
    {
        return this.ip;
    }

    public Short getPort()
    {
        return this.port;
    }

    /**
     * Sends request to the backend and gets the
     * response. This is done on a separate thread.
     * The supplyAync() method of CompletableFuture
     * runs the supplied task on ForkJoinPool.
     *
     * @param data JSON-encoded request
     * @return A CompletableFuture<String> instance
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<String> request(final String data)
            throws IOException
    {
        CompletableFuture<String> future = new CompletableFuture<>();

        future.supplyAsync(() -> {

            DataOutputStream dos = null;

            try
            {
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(data);
                dos.flush();

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                final String ret = (String)dis.readUTF();
                socket.close();
                return ret;
            }
            catch (Exception e)
            {
                return "error";
            }
        });

        return future;
    }
}
