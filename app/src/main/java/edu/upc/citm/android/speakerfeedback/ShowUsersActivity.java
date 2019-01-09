package edu.upc.citm.android.speakerfeedback;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowUsersActivity extends AppCompatActivity {

    private List<String> users;
    private Adapter adapter;
    private RecyclerView users_name_list_view;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        users = new ArrayList<>();
        adapter = new Adapter();

        users_name_list_view = findViewById(R.id.users_list_view);
        users_name_list_view.setLayoutManager(new LinearLayoutManager(this));
        users_name_list_view.setAdapter(adapter);

        app = (App)getApplication();
    }

    @Override
    protected void onStart() {
        if (app.getRoomId() != null) {
            db.collection("users").whereEqualTo("room", app.getRoomId()).addSnapshotListener(this, usersListener);
        }

        super.onStart();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.user_view = itemView.findViewById(R.id.user_view);
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.user, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.user_view.setText(users.get(position));
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    private EventListener<QuerySnapshot> usersListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.e("SpeakerFeedback","Error al rebre els 'users'", e);
                return;
            }

            users.clear();

            for (DocumentSnapshot doc : documentSnapshots)
                users.add(doc.getString("name"));

            adapter.notifyDataSetChanged();
        }
    };
}