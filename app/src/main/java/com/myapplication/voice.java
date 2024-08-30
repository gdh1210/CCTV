package com.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import java.util.TimeZone;
import androidx.core.app.ActivityCompat;
import com.mizuvoip.jvoip.*; //import the Mizu SIP SDK! You must copy the AJVoIP.aar into the \AJVoIPTest\AJVoIP folder!


public class voice extends Activity
{
    public static String LOGTAG = "AJVoIP";
    EditText mParams = null;
    EditText mDestNumber = null;
    Button mBtnStart = null;
    Button mBtnCall = null;
    Button mBtnHangup = null;
    Button mBtnTest = null;
    TextView mStatus = null;
    TextView mNotifications = null;
    SipStack mysipclient = null;
    Context ctx = null;
    public static voice instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Message messageToMainThread = new Message();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);
        ctx = this;
        instance = this;

        mParams = (EditText) findViewById(R.id.parameters_view);
        mDestNumber = (EditText) findViewById(R.id.dest_number);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnCall = (Button) findViewById(R.id.btn_call);
        mBtnHangup = (Button) findViewById(R.id.btn_hangup);
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mStatus = (TextView) findViewById(R.id.status);
        mNotifications = (TextView) findViewById(R.id.notifications);
        mNotifications.setMovementMethod(new ScrollingMovementMethod());

        DisplayLogs("oncreate");

        StringBuilder parameters = new StringBuilder();

        parameters.append("loglevel=5\r\n");
        parameters.append("serveraddress=192.168.0.108\r\n");
        parameters.append("username=103\r\n");
        parameters.append("password=103\r\n");

        mParams.setText(parameters.toString());
        mDestNumber.setText(""); //default call-to number for our test (testivr3 is a music IVR access number on our test server at voip.mizu-voip.com)

        DisplayStatus("Ready.");

        mBtnStart.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)  //Start button click
            {
                DisplayLogs("Start on click");

                try{
                    // start SipStack if it's not already running
                    if (mysipclient == null) //check if AJVoIP instance already exists
                    {
                        DisplayLogs("Starting SIPStack");

                        //initialize the SIP engine
                        mysipclient = new SipStack();
                        mysipclient.Init(ctx);

                        //subscribe to notification events
                        MyNotificationListener listener = new MyNotificationListener();
                        mysipclient.SetNotificationListener(listener);

                        SetParameters(); //pass the configuration (parameters can be changed also later at run-time)

                        DisplayLogs("SIPStack Start");

                        //start the SIP engine
                        mysipclient.Start();
                        //mysipclient.Register();
                        instance.CheckPermissions();

                        DisplayLogs("SIPStack Started");
                    }
                    else
                    {
                        DisplayLogs("SIPStack already started");
                    }
                }catch (Exception e) { DisplayLogs("ERROR, StartSIPStack"); }
            }
        });

        mBtnCall.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Call button click
            {
                DisplayLogs("Call on click");

                String number = mDestNumber.getText().toString().trim();
                if (number == null || number.length() < 1)
                {
                    DisplayStatus("ERROR, Invalid destination number");
                    return;
                }

                if (mysipclient == null) {
                    DisplayStatus("ERROR, cannot initiate call because SipStack is not started");
                    return;
                }

                instance.CheckPermissions();

                if (mysipclient.Call(-1, number))
                {
                }
            }
        });

        mBtnHangup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Hangup button click
            {
                DisplayLogs("Hangup on click");

                if (mysipclient == null)
                    DisplayStatus("ERROR, cannot hangup because SipStack is not started");
                else
                    mysipclient.Hangup();
            }
        });

        mBtnTest.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Test button click
            {
                //any test code here
                DisplayLogs("Toogle loudspeaker");
                if (mysipclient == null)
                    DisplayStatus("ERROR, SipStack not started");
                else
                    mysipclient.SetSpeakerMode(!mysipclient.IsLoudspeaker());
            }
        });
    }

    public void SetParameters()
    {
        String params = mParams.getText().toString();
        if (params == null || mysipclient == null) return;
        params = params.trim();

        DisplayLogs("SetParameters: " + params);

        mysipclient.SetParameters(params);
    }

    void CheckPermissions()
    {
        if (Build.VERSION.SDK_INT >= 23 && ctx.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            //we need RECORD_AUDIO permission before to make/receive any call
            DisplayStatus("Microphone permission required");
            ActivityCompat.requestPermissions(voice.this, new String[]{Manifest.permission.RECORD_AUDIO}, 555);
        }
    }

    class MyNotificationListener extends SIPNotificationListener
    {
        //here are some examples about how to handle the notifications:
        @Override
        public void onAll(SIPNotification e) {
            //we receive all notifications (also) here. we just print them from here
            DisplayLogs(e.getNotificationTypeText()+" notification received: " + e.toString());
        }

        //handle connection (REGISTER) state
        @Override
        public void onRegister( SIPNotification.Register e)
        {
            //check if/when we are registered to the SIP server
            if(!e.getIsMain()) return; //we ignore secondary accounts here

            switch(e.getStatus())
            {
                case SIPNotification.Register.STATUS_INPROGRESS: DisplayStatus("Registering..."); break;
                case SIPNotification.Register.STATUS_SUCCESS: DisplayStatus("Registered successfully."); break;
                case SIPNotification.Register.STATUS_FAILED: DisplayStatus("Register failed because "+e.getReason()); break;
                case SIPNotification.Register.STATUS_UNREGISTERED: DisplayStatus("Unregistered."); break;
            }
        }

        //an example for STATUS handling
        @Override
        public void onStatus( SIPNotification.Status e)
        {
            if(e.getLine() == -1) return; //we are ignoring the global state here (but you might check only the global state instead or look for the particular lines separately if you must handle multiple simultaneous calls)

            //log call state
            if(e.getStatus() >= SIPNotification.Status.STATUS_CALL_SETUP && e.getStatus() <= SIPNotification.Status.STATUS_CALL_FINISHED)
            {
                DisplayStatus("Call state is: "+ e.getStatusText());
            }

            //catch outgoing call connect
            if(e.getStatus() == SIPNotification.Status.STATUS_CALL_CONNECT && e.getEndpointType() == SIPNotification.Status.DIRECTION_OUT)
            {
                DisplayStatus("Outgoing call connected to "+ e.getPeer());
            }
            //catch incoming calls
            else if(e.getStatus() == SIPNotification.Status.STATUS_CALL_RINGING && e.getEndpointType() == SIPNotification.Status.DIRECTION_IN)
            {
                DisplayStatus("Incoming call from "+ e.getPeerDisplayname());

                //auto accepting the incoming call (instead of auto accept, you might present an Accept/Reject button for the user which will call Accept / Reject)
                mysipclient.Accept(e.getLine());
            }
            //catch incoming call connect
            else if(e.getStatus() == SIPNotification.Status.STATUS_CALL_CONNECT && e.getEndpointType() == SIPNotification.Status.DIRECTION_IN)
            {
                DisplayStatus("Incoming call connected");
            }

        }

        //print important events (EVENT)
        @Override
        public void onEvent( SIPNotification.Event e)
        {
            DisplayStatus("Important event: "+e.getText());
        }

        //IM handling
        @Override
        public void onChat( SIPNotification.Chat e)
        {
            DisplayStatus("Message from "+e.getPeer()+": "+e.getMsg());

            //auto answer
            mysipclient.SendChat(-1, e.getPeer(),"Received");

        }
    }

    public void DisplayStatus(String stat)
    {
        try{
            if (stat == null) return;
            if (mStatus != null) {
                if ( stat.length() > 70)
                    mStatus.setText(stat.substring(0,60)+"...");
                else
                    mStatus.setText(stat);
            }
            DisplayLogs(stat);
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, DisplayStatus", e); }
    }

    public void DisplayLogs(String logmsg)
    {
        try{
            if (logmsg == null || logmsg.length() < 1) return;

            if ( logmsg.length() > 2500) logmsg = logmsg.substring(0,300)+"...";
            logmsg = "["+ new java.text.SimpleDateFormat("HH:mm:ss:SSS").format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime()) +  "] " + logmsg + "\r\n";

            Log.v(LOGTAG, logmsg);
            if (mNotifications != null) mNotifications.append(logmsg);
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, DisplayLogs", e); }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        try{
            super.onDestroy();
            DisplayLogs("ondestroy");
            if (mysipclient != null)
            {
                DisplayLogs("Stop SipStack");
                mysipclient.Stop(true);
            }

            mysipclient = null;
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, on destroy", e); }
    }
}
