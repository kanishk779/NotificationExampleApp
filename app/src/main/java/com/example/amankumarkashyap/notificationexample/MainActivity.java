package com.example.amankumarkashyap.notificationexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    NotificationCompat.Builder notification;
    private static final String CHANNEL_ID = "simpleNotification";
    private static final int REQUEST_CODE_HELLO = 100;
    private static final int REQUEST_CODE_GOODBYE = 101;
    private static final int REQUEST_CODE_SIMPLE =103;
    private Button notifyBtn;
    private TextView messageText;
    private String msg;
    private NotificationManager nm;
    private static final int NOTIFICATION_ID = 102;

    /**
     * Notification Channels provide us with the ability to group the notifications that our application sends into manageable groups.
     * Once our notifications are in these channels, we no longer have input into their functionality — so it is up to the user to manage these channels.
     * When it comes to altering the settings for our application notifications, the user will be presented with these options:
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        nm = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        /**
         * The received intent is null check what is the matter
         */
        Intent message_from_user = this.getIntent();
        if(message_from_user != null)
        {
            msg = message_from_user.getStringExtra("message");
            messageText.setText(msg);
        }

        notification = new NotificationCompat.Builder(this,CHANNEL_ID);

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotification();
                nm.notify(NOTIFICATION_ID,notification.build());
            }
        });
    }

    /**
     * This is for the app condition when the "notification" is initialised within onCreate():-
     * 1.one notification already and another triggered -> the action button added up and time also did not reset
     * 1.used the simple click of the notification to launch the current activity while the app is still running and then pressed
     * the generate notification button -> as expected : new notification appeared in the status bar because the onCreate is called.
     * 2.used sendHello in the notification action and result were similar to the above case.
     * 3.The Problem is that another time (i.e more than one time )generate notification button is clicked without using any of the 3 methods of the
     * notification action -> than the time is not reset and the action are also added up.
     *The solution to the above problem is to initialise the "notification" object in the generateNotification() method
     */
    public void generateNotification()
    {
        Intent HelloIntent = new Intent(this,MainActivity.class);
        HelloIntent.putExtra("message","Hello to you");

        Intent GoodByeIntent = new Intent(this,MainActivity.class);
        GoodByeIntent.putExtra("message","GoodBye !!");

        Intent simpleIntent = new Intent(this,MainActivity.class);

        /**
         * Replace the requestCode and check what happens.I have defined two codes and also check what happens when you fire one more notification when already one is
         * present in the notification drawer.
         * So what i learned is that the request code should be all different for different pending intent.
         * But the problem is that i'm getting the intent in onCreate and when the activity is already open and user taps on the action button it does not
         * perform the desired action so let's try putting that in the onStart() method.
         *
         * The above line(only one immediate above line) is incorrect
         *
         * Another thing learned is that if a notification is already existing and another is launched than the old one is replaced with the current one ,I
         * think this is happening because of the flag that is set FLAG_UPDATE_CURRENT(But the time shown beside the app name is the not resetting).
         * try setting another flag and see what will happen.
         * This All i will be doing in another branch called experiment1 so switch over that branch(list of what I will do):-
         * 1.change the Flags
         * 2.Try using setAutoCancel on all the three action the user can perform(hello,goodbye,simpleStart) so that the notification disappears.
         */
        PendingIntent helloPending = PendingIntent.getActivity(this,REQUEST_CODE_HELLO,HelloIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent goodByePending = PendingIntent.getActivity(this,REQUEST_CODE_GOODBYE,GoodByeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        /**
         * The below intent is not launching the specified activity which is the expected action of this intent?
         */
        PendingIntent simplePending = PendingIntent.getActivity(this,REQUEST_CODE_SIMPLE,simpleIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setTicker("Hello !!");
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        notification.setContentTitle("First Notification");
        notification.addAction(R.drawable.ic_launcher_foreground,"Send Hello",helloPending);
        notification.addAction(R.drawable.ic_launcher_background,"Goodbye",goodByePending);
        notification.setContentIntent(simplePending);

        /**
         * I was facing another problem that is after sometime the action starting adding up in the notification (the screenshot is in mobile) and i think this is because the
         * "notification" object is getting initialised in the onCreate and that is why this is happening and also let's check what will happen for the time beside the app name if
         * I initialise the notification in the method generateNotification()
         */
    }
    private void initialise()
    {
        notifyBtn = findViewById(R.id.notification_button);
        messageText = findViewById(R.id.message_text);
    }
}
