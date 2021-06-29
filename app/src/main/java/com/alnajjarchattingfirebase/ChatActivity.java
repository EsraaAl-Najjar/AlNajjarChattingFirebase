package com.alnajjarchattingfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alnajjarchattingfirebase.adapters.MessageAdapter;
import com.alnajjarchattingfirebase.fragments.APIService;
import com.alnajjarchattingfirebase.model.Chat;
import com.alnajjarchattingfirebase.model.Users;
import com.alnajjarchattingfirebase.notification.Client;
import com.alnajjarchattingfirebase.notification.MyResponse;
import com.alnajjarchattingfirebase.notification.NotifyData;
import com.alnajjarchattingfirebase.notification.Sender;
import com.alnajjarchattingfirebase.notification.Token;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference dbReference;

    Intent intent;

    ImageButton btn_send;
    EditText et_send_text;

    MessageAdapter messageAdapter;
    List<Chat> chatList;

    RecyclerView rcvChat;
    String userId;
    ValueEventListener seenListener;

    APIService apiService;
     boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );
        Toolbar toolbar = findViewById( R.id.toolbarchat);
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        apiService = Client.getClient( "https://fcm.googleapis.com/").create( APIService.class );

        rcvChat = findViewById( R.id.rcv_message);
        rcvChat.setHasFixedSize( true );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext() );
        linearLayoutManager.setStackFromEnd( true );
        rcvChat.setLayoutManager( linearLayoutManager );
        profile_image = findViewById( R.id.profile_image_chat );
        username = findViewById( R.id.usernamechat );
        btn_send = findViewById( R.id.img_btn_send );
        et_send_text = findViewById( R.id.et_send_text );


        intent = getIntent();
        userId = intent.getStringExtra( "userid" );

        btn_send.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify =true;
                String msg = et_send_text.getText().toString();
                if (!msg.equals( "" )){
                    sendMessage( fuser.getUid(), userId ,msg.trim() );
                }else {
                    Toast.makeText( ChatActivity.this, "You Can't send empty message", Toast.LENGTH_SHORT ).show();
                }
                et_send_text.setText("");
            }
        } );
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child( userId );

        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                username.setText( users.getUsername() );
                if (users.getImgURL().equals( "default" )){

                    profile_image.setImageResource( R.mipmap.ic_launcher );
                }else {

                    Glide.with( getApplicationContext() ).load( users.getImgURL() ).into( profile_image);
                }
                readMessages( fuser.getUid(), userId , users.getImgURL() );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        seenMessage( userId );
    }

    private void  seenMessage(final String userid){

        dbReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals( fuser.getUid() )&& chat.getSender().equals( userid )){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put( "isseen", true );
                        snapshot.getRef().updateChildren( hashMap );

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
    private void  sendMessage(String sender, final String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put( "sender", sender );
        hashMap.put( "receiver", receiver );
        hashMap.put( "message", message );
        hashMap.put( "isseen", false );
        databaseReference.child( "Chats" ).push().setValue( hashMap );

        // Adduser to chat Fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child( fuser.getUid() )
                .child( userId );

        chatRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child( "id" ).setValue( userId );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        final String msg = message;
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child( fuser.getUid() );
        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                sendNotification(receiver, users.getUsername(),msg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    private void sendNotification(String receiver, final String username, final String msg) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo( receiver );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    NotifyData notifyData = new NotifyData( fuser.getUid(), R.mipmap.ic_launcher, username+": "+ msg , "New Message",userId );
                    Sender sender = new Sender( notifyData,token.getToken() );
                    apiService.sendNotification( sender ).enqueue( new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200){
                                if (response.body().success == 1){
                                    Toast.makeText( ChatActivity.this, "Failed!", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    } );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private  void readMessages(final String myid, final String userid, final String imageurl){

        chatList = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance().getReference("Chats");
        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals( myid )&& chat.getSender().equals( userid )||
                        chat.getReceiver().equals( userid ) && chat.getSender().equals( myid )){
                                chatList.add( chat );

                    }
                    messageAdapter = new MessageAdapter( ChatActivity.this, chatList, imageurl );
                    rcvChat.setAdapter( messageAdapter );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void status(String status){
        dbReference  = FirebaseDatabase.getInstance().getReference("Users").child( fuser.getUid() );

        HashMap<String, Object > hashMap = new HashMap<>();
        hashMap.put( "status", status );

        dbReference.updateChildren( hashMap );
    }

    @Override
    protected void onResume() {
        super.onResume();
        status( "online" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbReference.removeEventListener( seenListener );
        status( "offline" );
    }
}
