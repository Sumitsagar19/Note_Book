package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import java.util.Queue;

public class MainActivity extends AppCompatActivity {


    CardView menu_btn;
    RecyclerView recyclerView;
    Adapter adapter;

    FloatingActionButton addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addBtn1);
        recyclerView = findViewById(R.id.recycler);
        menu_btn = findViewById(R.id.menu);


        addBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,DetailActivity.class));
        });

        menu_btn.setOnClickListener(v -> showMenu());
        setRecyclerView();
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menu_btn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                  Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                  startActivity(intent);
                  finish();
                }
                return false;
            }
        });



    }



    private void setRecyclerView() {
        Query query = utility.getCollectionReferenceForNotes()
                .orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteModal> options = new FirestoreRecyclerOptions.Builder<NoteModal>()
                .setQuery(query, NoteModal.class).build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new Adapter(options,this);
        recyclerView.setAdapter(adapter);

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}