package com.alnajjarchattingfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alnajjarchattingfirebase.ChatActivity;
import com.alnajjarchattingfirebase.R;
import com.alnajjarchattingfirebase.model.Chat;
import com.alnajjarchattingfirebase.model.Users;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerAdapters  extends RecyclerView.Adapter<UsersRecyclerAdapters.ViewHolder> {
    private Context mcontext;
    private List<Users> usersList;
    private boolean ischat;
    String theLastMessage;

    public UsersRecyclerAdapters() {
    }

    public UsersRecyclerAdapters(Context mcontext, List<Users> usersList, boolean ischat) {
        this.mcontext = mcontext;
        this.usersList = usersList;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( mcontext ).inflate( R.layout.items_users,  viewGroup , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Users users= usersList.get( i );
        holder.userName.setText( users.getUsername());
        Toast.makeText( mcontext, users.getImgURL(), Toast.LENGTH_SHORT ).show();
      //  Glide.with( mcontext ).load( users.getImgURL() ).into( holder.img_users );

      if (users.getImgURL().equals( "default" )){

            holder.img_users.setImageResource( R.mipmap.ic_launcher );
        }else {

            Glide.with( mcontext ).load( users.getImgURL() ).into( holder.img_users );
        }
        if (ischat){
          lastMessage( users.getId(), holder.lastmsg );
        }else {
          holder.lastmsg.setVisibility( View.GONE );
        }

        if (ischat){

          if (users.getStatus().equals( "online" )){
              holder.online.setVisibility( View.VISIBLE );
              holder.offline.setVisibility( View.GONE );
          }else{
              holder.online.setVisibility( View.GONE );
              holder.offline.setVisibility(View.VISIBLE );
          }
        }else{
          holder.online.setVisibility( View.GONE );
          holder.offline.setVisibility( View.GONE );
        }

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mcontext, ChatActivity.class );
                intent.putExtra( "userid", users.getId() );
                mcontext.startActivity( intent );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         CircleImageView img_users, offline,online;
         TextView userName,lastmsg;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            img_users = itemView.findViewById( R.id.circle_img_users );
            offline = itemView.findViewById( R.id.offline_img );
            online = itemView.findViewById( R.id.online_img );
            userName = itemView.findViewById( R.id.tv_username_users );
            lastmsg = itemView.findViewById( R.id.tv_last_msg );
        }
    }


    //check for last msg
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);
//                    if (chat.getReceiver().equals( firebaseUser.getUid() )&& chat.getSender().equals( userid )
//                            || chat.getReceiver().equals( userid ) && chat.getSender().equals( firebaseUser.getUid() )){
//
//                        theLastMessage = chat.getMessage();
//                    }

                }
                switch (theLastMessage){

                    case "default" :
                        last_msg.setText( "No Message" );
                        break;
                        default:
                            last_msg.setText( theLastMessage );
                            break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
