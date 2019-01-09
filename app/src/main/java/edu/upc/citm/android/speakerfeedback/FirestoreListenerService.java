package edu.upc.citm.android.speakerfeedback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreListenerService extends Service {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    App app;

    private boolean is_first_time = false;

    @Override
    public void onCreate() {

        app = (App)getApplication();
        super.onCreate();
        Log.i("SpeakerFeedback", "FirestoreListenerService.onCreate");

        db.collection("rooms").document(app.getRoomId()).collection("polls")
                .whereEqualTo("open", true)
                .addSnapshotListener(ListenerPoll);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopNotifications();
        Log.i("SpeakerFeedback", "FirestoreListenerService.onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SpeakerFeedback", "FirestoreListenerService.onStartCommand");

        if (!is_first_time)
            createForegroundNotification();

        return START_NOT_STICKY;
    }

    private void stopNotifications()
    {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.cancelAll();
    }

    private void createForegroundNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(String.format("Connectat a " + app.getRoomId()))
                .setSmallIcon(R.drawable.ic_message)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        is_first_time = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private EventListener<QuerySnapshot> ListenerPoll = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.e("SpeakerFeedback", "Error al rebre els 'polls'", e);
                return;
            }

            for (DocumentSnapshot doc : documentSnapshots) {
                Poll poll = doc.toObject(Poll.class);
                if (poll.isOpen()) {
                    Log.i("SpeakerFeedback", poll.getQuestion());

                    Intent intent = new Intent(FirestoreListenerService.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(FirestoreListenerService.this, 0, intent, 0);

                    Notification notification = new NotificationCompat.Builder(FirestoreListenerService.this, App.CHANNEL_ID)
                            .setContentTitle(String.format("new Poll: %s", poll.getQuestion()))
                            .setSmallIcon(R.drawable.ic_message)
                            .setContentIntent(pendingIntent)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .build();

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    notificationManager.notify(2 , notification);
                }
            }
        }
    };
}
