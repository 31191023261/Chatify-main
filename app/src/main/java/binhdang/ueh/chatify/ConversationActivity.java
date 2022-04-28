package binhdang.ueh.chatify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConversationActivity extends Activity {

    TextView      nameofreciever;
    EditText      inputchat;
    ImageButton   backbutton;
    ImageButton   infobutton;
    ImageButton   sendbutton;
    RecyclerView  message_recyclerView;
    MessagesAdapter messAdapter;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private  String msenderid, mrecieverid,mrecievername,senderroom, recieverroom;
    String time;
    ArrayList<Messages> messArraylist;
    Intent intent;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String enteredmessage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        nameofreciever = findViewById(R.id.nameofreciever);
        inputchat = findViewById(R.id.chat_input);
        backbutton = findViewById(R.id.back_button);
        infobutton = findViewById(R.id.info_button);
        sendbutton = findViewById(R.id.send_button);
        message_recyclerView = findViewById(R.id.message_recyclerView);
        messArraylist = new ArrayList<>();
        messAdapter=new MessagesAdapter(ConversationActivity.this,messArraylist);
        message_recyclerView .setAdapter(messAdapter);
        intent=getIntent();


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        msenderid=firebaseAuth.getUid();
        mrecieverid=getIntent().getStringExtra("receiverid");
        mrecievername=getIntent().getStringExtra("name");
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");



        senderroom=msenderid+mrecieverid;
        recieverroom=mrecieverid+msenderid;


        DatabaseReference databaseReference=firebaseDatabase.getReference().child("conversations").child(senderroom).child("messages");
        messAdapter=new MessagesAdapter(ConversationActivity.this,messArraylist);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messArraylist.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Messages messages=snapshot1.getValue(Messages.class);
                    messArraylist.add(messages);
                }
                messAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameofreciever.setText(mrecievername);



        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=inputchat.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                }

                else

                {
                    Date date=new Date();
                    time=simpleDateFormat.format(calendar.getTime());
                    Messages messages=new Messages(enteredmessage,firebaseAuth.getUid(),time);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(recieverroom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    inputchat.setText(null);




                }




            }
        });

    }


}
