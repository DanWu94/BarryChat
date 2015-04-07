package com.example.dan.barrychat;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends Activity {

    private static final String LOGTAG = "MainPAge";

    private ServerConnect mSC;

    EditText cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        Log.i(LOGTAG, "onCreate entered");

        // Connect to server and continue from here TODO
        //// Your code should go here

        //Initialize the ServerConnect
        mSC = new ServerConnect(this);
        mSC.start();

        //Makes the receiving text area scrollable
        TextView tv = (TextView) findViewById(R.id.txtServerResponse);
        tv.setMovementMethod(new ScrollingMovementMethod());

        //Refresh the user list
        Button buttonRefreshUsr = (Button) findViewById(R.id.btnRefreshUsr);
        buttonRefreshUsr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //OnClick actions here
                mSC.send("WHO");
            }
        });

        //Send the input command
        cmd = (EditText) findViewById(R.id.cmdInput);
        Button buttonSend = (Button) findViewById(R.id.btnSendCmd);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //OnClick actions here
                String cmdString = cmd.getText().toString();
                mSC.send(cmdString);
            }
        });

        //This is an example of how to set events to button clicks
        Button button = (Button) findViewById(R.id.btnKill);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //OnClick actions here
                System.exit(0);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
