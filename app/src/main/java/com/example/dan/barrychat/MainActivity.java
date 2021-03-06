package com.example.dan.barrychat;

import android.app.Activity;
import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

    private static final String LOGTAG = "MainPAge";

    private ServerConnect mSC;

    EditText cmd;

    String whoToMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOGTAG, "onCreate entered");

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
        switch (item.getItemId()) {
            case R.id.action_kill:
                mSC.send("DISCONNECT");
                System.exit(0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void confirmUserName(View view) {
        final EditText userName = (EditText) findViewById(R.id.userNameEditText);
        // Connect to server and continue from here TODO
        //// Your code should go here

        //Initialize the ServerConnect
        mSC = new ServerConnect(this);
        mSC.start();

        setContentView(R.layout.fragment_main);
        mSC.send("REGISTER "+userName.getText().toString());

        //Send the input command
        cmd = (EditText) findViewById(R.id.cmdInput);
        ImageButton buttonSend = (ImageButton) findViewById(R.id.btnSendCmd);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSC.sendMsg(userName.getText().toString(),whoToMsg,cmd.getText().toString());
            }
        });

        ImageButton buttonRefresh = (ImageButton) findViewById(R.id.btnRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //OnClick actions here
                mSC.send("WHO");
            }
        });

        populateListView();
        registerClickCallback();
    }

    private void populateListView() {
        String [] items = {"PLEASE","CLICK","REFRESH","BUTTON"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.userlist_textview,
                items);
        ListView list = (ListView) findViewById(R.id.userListView);
        list.setAdapter(adapter);
    }


    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.userListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textview = (TextView) viewClicked;
                whoToMsg = textview.getText().toString();
                mSC.send("INVITE "+whoToMsg);
                TextView whoToText = (TextView) findViewById(R.id.whoToTextView);
                whoToText.setText("To \""+whoToMsg+"\"");
            }
        });
    }

}
