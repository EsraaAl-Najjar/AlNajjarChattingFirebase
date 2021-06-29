package com.alnajjarchattingfirebase.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alnajjarchattingfirebase.R;
import com.alnajjarchattingfirebase.adapters.UsersRecyclerAdapters;
import com.alnajjarchattingfirebase.model.Chat;
import com.alnajjarchattingfirebase.model.ChatList;
import com.alnajjarchattingfirebase.model.Users;
import com.alnajjarchattingfirebase.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView rcv_conv_list;

    private UsersRecyclerAdapters usersRecyclerAdapters;
    private List<Users> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference dbReference;
    private List<ChatList> usersList;

    
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate( R.layout.fragment_chat, container,false );

         rcv_conv_list = view.findViewById( R.id.rcv_conversations_list );
         rcv_conv_list.setHasFixedSize( true );
         rcv_conv_list.setLayoutManager( new LinearLayoutManager( getContext() ) );

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

         usersList = new ArrayList<>();
//         dbReference = FirebaseDatabase.getInstance().getReference("Chats");
//         dbReference.addValueEventListener( new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 usersList.clear();
//                 for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//
//                     Chat chat = snapshot.getValue(Chat.class);
//
//                     if (chat.getSender().equals( firebaseUser.getUid() )){
//                         usersList.add( chat.getReceiver() );
//                         readChats();
//                     }
//                     if (chat.getReceiver().equals( firebaseUser.getUid() )){
//                         usersList.add( chat.getSender() );
//                         readChats();
//
//                     }
//                 }
//
//             }
//
//             @Override
//             public void onCancelled(@NonNull DatabaseError databaseError) {
//
//             }
//         } );
         dbReference = FirebaseDatabase.getInstance().getReference("ChatList").child( firebaseUser.getUid() );
         dbReference.addValueEventListener( new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 usersList.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                     ChatList chatList = snapshot.getValue(ChatList.class);
                     usersList.add( chatList );
                 }
                 chatList();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         } );

         updateToken( FirebaseInstanceId.getInstance().getToken( ) );
            return view;
     }

     private void updateToken(String token){
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Token");
         Token token1 = new Token( token );
         reference.child( firebaseUser.getUid()).setValue( token1 );

     }
    private void chatList() {

         mUsers = new ArrayList<>();
         dbReference = FirebaseDatabase.getInstance().getReference("Users");
         dbReference.addValueEventListener( new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 mUsers.clear();
                 for (DataSnapshot snapshot  : dataSnapshot.getChildren()){
                     Users users = snapshot.getValue(Users.class);
                     for (ChatList chatList : usersList){
                         if (users.getId().equals( chatList.getId() )){
                             mUsers.add( users );
                         }
                     }

                 }
                 usersRecyclerAdapters = new UsersRecyclerAdapters( getContext(), mUsers, true );
                 rcv_conv_list.setAdapter( usersRecyclerAdapters );
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         } );
    }


//     private void readChats(){
//         mUsers = new ArrayList<>();
//         dbReference = FirebaseDatabase.getInstance().getReference("Users");
//
//         dbReference.addValueEventListener( new ValueEventListener() {
//             @Override
//             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                 mUsers.clear();
//                 // display one users from chats
//                 for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                     Users users = snapshot.getValue(Users.class);
//                     for (String sid : usersList){
//                          if (users.getId().equals( sid )){
//                              if(mUsers.size() != 0 ){
//                                  for (Users users1 : mUsers){
//                                      if (!users.getId().equals( users1.getId() )){
//                                          mUsers.add( users );
//                                      }
//                                  }
//                              }else {
//                                  mUsers.add( users );
//                              }
//                          }
//
//                     }
//
//                 }
//
//                 usersRecyclerAdapters = new UsersRecyclerAdapters( getActivity(),mUsers, true );
//                 rcv_conv_list.setAdapter( usersRecyclerAdapters );
//
//             }
//
//             @Override
//             public void onCancelled(@NonNull DatabaseError databaseError) {
//
//             }
//         } );
//
//     }

}
