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
            int returnedId = message_from_user.getIntExtra("ID",0);
            /**
             * The statement below cancels the notification from the status bar
             */
            nm.cancel(returnedId);
        }

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNotification();
                nm.notify(NOTIFICATION_ID,notification.build());
            }
        });
    }

    /**
     * comments in master branch
     */
    public void generateNotification()
    {
        notification = new NotificationCompat.Builder(this,CHANNEL_ID);
        Intent HelloIntent = new Intent(this,MainActivity.class);
        HelloIntent.putExtra("message","Hello to you");
        HelloIntent.putExtra("ID",NOTIFICATION_ID);

        Intent GoodByeIntent = new Intent(this,MainActivity.class);
        GoodByeIntent.putExtra("message","GoodBye !!");
        GoodByeIntent.putExtra("ID",NOTIFICATION_ID);

        Intent simpleIntent = new Intent(this,MainActivity.class);

        /**
         *
         * Another thing learned is that if a notification is already existing and another is launched than the old one is replaced with the current one ,I
         * think this is happening because of the flag that is set FLAG_UPDATE_CURRENT(But the time shown beside the app name is the not resetting).
         * try setting another flag and see what will happen.
         * This All i will be doing in another branch called experiment1 so switch over that branch(list of what I will do):-
         * 1.change the Flags
         * 2.Try using setAutoCancel on all the three action the user can perform(hello,goodbye,simpleStart) so that the notification disappears.
         */
        PendingIntent helloPending = PendingIntent.getActivity(this,REQUEST_CODE_HELLO,HelloIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent goodByePending = PendingIntent.getActivity(this,REQUEST_CODE_GOODBYE,GoodByeIntent,PendingIntent.FLAG_ONE_SHOT);
        /**
         * The below intent is not launching the specified activity which is the expected action of this intent?
         */
        PendingIntent simplePending = PendingIntent.getActivity(this,REQUEST_CODE_SIMPLE,simpleIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        /**
         * Theory about PENDING INTENT FLAGS:
         *
         * FLAG_CANCEL_CURRENT-Flag for use with getActivity(Context, int, Intent, int), getBroadcast(Context, int, Intent, int), and getService(Context, int, Intent, int):
         * if the described PendingIntent already exists, the current one is canceled before generating a new one.
         *
         * FLAG_NO_CREATE-Flag for use with getActivity(Context, int, Intent, int), getBroadcast(Context, int, Intent, int), and getService(Context, int, Intent, int):
         * if the described PendingIntent does not already exist, then simply return null instead of creating it.
         *
         * FLAG_ONE_SHOT-Flag for use with getActivity(Context, int, Intent, int), getBroadcast(Context, int, Intent, int), and getService(Context, int, Intent, int):
         * this PendingIntent can only be used once.
         *
         * FLAG_UPDATE_CURRENT-Flag for use with getActivity(Context, int, Intent, int), getBroadcast(Context, int, Intent, int), and getService(Context, int, Intent, int):
         * if the described PendingIntent already exists, then keep it but its replace its extra data with what is in this new Intent
         */
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setPriority(NotificationCompat.PRIORITY_HIGH);
        notification.setTicker("Hello !!");
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        notification.setContentTitle("First Notification");
        notification.setAutoCancel(true);
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
