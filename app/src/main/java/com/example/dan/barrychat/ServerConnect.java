package com.example.dan.barrychat;

import android.app.Activity;

import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leeming
 * Code snippets from http://examples.javacodegeeks.com/android/core/socket-core/android-socket-example/
 */
public class ServerConnect extends Thread{

    public static final int BUFFER_SIZE = 2048;

    private Socket socket;
    private static final int SERVERPORT = 9999;         //This is the port that we are connecting to
    //Remember the channel simulator is 9998

    private static final String SERVERIP = "10.0.2.2";  //This address is magically mapped to the host's loopback.
    private static final String LOGTAG = "SocketTester";

    boolean terminated = false;

    private PrintWriter out = null;
    private BufferedReader in = null;

    Activity parentref;

    /**
     *
     * @param parentRef Expects a reference to the calling activity, e.g. new ServerConnect(this);
     */
    public ServerConnect(Activity parentRef)
    {
        parentref=parentRef;
    }

    /**
     * Sends commands to the server. Called from UI thread via a button press
     * @param cmd
     */
    public void send(String cmd)
    {
        try
        {
            Log.i(LOGTAG,"Sending command: "+cmd);
            out.println(cmd);
        }
        catch(Exception e)
        {
            Log.e(LOGTAG,"Failed to send command : "+e);
        }
    }

    /**
     * Main thread loop that grabs incoming messages
     */
    public void run()
    {
        Log.i(LOGTAG,"Running socket thread");


        try
        {
            InetAddress svrAddr = InetAddress.getByName(SERVERIP);
            socket = new Socket(svrAddr, SERVERPORT);

            //Setup i/o streams
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            //Keep listening for messages from server
            while(!terminated)
            {
                final String message = in.readLine();
                if(message != null && message != "")
                {
                    Log.i(LOGTAG,"MSG recv : "+message);

                    //Update GUI with any server responses
                    final TextView txtv = (TextView) parentref.findViewById(R.id.txtServerResponse);
                    final ListView list = (android.widget.ListView) parentref.findViewById(R.id.userListView);

                    parentref.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             *
                             *
                             * This is where the the UI gets updated by the socket
                             *
                             *
                             */
                            if(message.substring(0,3).equals("WHO")){
                                try {
                                    JSONArray userListArr = new JSONArray(message.substring(3));
                                    List<String> userNameList = new ArrayList<String>();
                                    for(int i = 0; i < userListArr.length(); i++){
                                        userNameList.add(userListArr.getString(i));
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            parentref,
                                            R.layout.userlist_textview,
                                            userNameList);
                                    list.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                String currentTimeStamp = dateFormat.format(new Date());
                                txtv.setText(txtv.getText()+"\n"+message+" "+"\n"+currentTimeStamp);
                            }

                        }
                    });
                }
            }

        }
        catch (UnknownHostException uhe)
        {
            Log.e(LOGTAG,"Unknownhost\n"+uhe.getStackTrace().toString());
        }
        catch (Exception e) {
            Log.e(LOGTAG, "Socket failed\n"+e.getMessage());
            e.printStackTrace();
        }

        disconnect();
        Log.i(LOGTAG,"Thread now closing");
    }

    /**
     * Disconnect from the server as well as closing i/o streams
     */
    public void disconnect()
    {
        Log.i(LOGTAG, "Disconnecting from server");
        try
        {
            in.close();
            out.close();
        }
        catch(Exception e)
        {/*do nothing*/}

        try
        {
            socket.close();
        }
        catch(Exception e)
        {/*do nothing*/}


    }

}

