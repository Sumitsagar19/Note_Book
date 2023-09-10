package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class DetailActivity extends AppCompatActivity {
    EditText Title, Description;
    Button addNote;
    TextView textView;

    String title,desc,docId;

    boolean isEditMode = false;

   TextView deleteNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Title = findViewById(R.id.title_note);
        Description = findViewById(R.id.desc);
        addNote = findViewById(R.id.Add);
        textView = findViewById(R.id.textView);
        deleteNote = findViewById(R.id.text_delete);


        // receive data
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("Desc");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        Title.setText(title);
        Description.setText(desc);
        if (isEditMode){
            textView.setText("Edit your Note");
            deleteNote.setVisibility(View.VISIBLE);
        }



        addNote.setOnClickListener(v -> saveNote());

        deleteNote.setOnClickListener(v -> deleteNoteFromFirebase());

    }

    private void deleteNoteFromFirebase() {

        DocumentReference documentReference;

            documentReference = utility.getCollectionReferenceForNotes().document(docId);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // notes is delete
                    utility.showToast(DetailActivity.this,"Note deleted successful");
                    finish();
                }else {
                    //notes is not added
                    utility.showToast(DetailActivity.this,"Failed while deleting note");
                }

            }
        });

    }

    private void saveNote() {

        String noteTitle = Title.getText().toString();
        String noteDesc = Description.getText().toString();

        if (noteTitle.isEmpty()) {
            Title.setError("Title is required");
            return;
        }

        NoteModal note = new NoteModal();
        note.setTitle(noteTitle);
        note.setDesc(noteDesc);
        note.setTimestamp(Timestamp.now());


        SaveNoteToFirebase(note);


    }

    private void SaveNoteToFirebase(NoteModal note) {

        DocumentReference documentReference;
        if (isEditMode){
            // update the note
            documentReference = utility.getCollectionReferenceForNotes().document(docId);

        }else {
            // create new note
            documentReference = utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // notes is added
                    utility.showToast(DetailActivity.this,"Note added successful");
                    finish();
                }else {
                    //notes is not added
                    utility.showToast(DetailActivity.this,"Failed while adding note");
                }

            }
        });
        
    }

}
