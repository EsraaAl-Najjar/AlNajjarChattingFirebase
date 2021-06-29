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
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT= 1;

    private Context mcontext;
    private List<Chat> chatList;
    private String imageurl;
    FirebaseUser firebaseUser;


    public MessageAdapter(Context mcontext, List<Chat> ChatList, String imgurl) {
        this.mcontext = mcontext;
        this.chatList = ChatList;
        this.imageurl = imgurl;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from( mcontext ).inflate( R.layout.chat_items_right,  viewGroup , false);
            return new ViewHolder(view);

        }else if (viewType == MSG_TYPE_LEFT){
            View view2 = LayoutInflater.from( mcontext ).inflate( R.layout.chat_items_left,  viewGroup , false);
            return new ViewHolder(view2);
        }else{
            View view3 = LayoutInflater.from( mcontext ).inflate( R.layout.chat_items_left,  viewGroup , false);
            return new ViewHolder(view3);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Chat chat= chatList.get( i );
        holder.show_message.setText( chat.getMessage() );

        if (imageurl.equals( "default" )){

            holder.img_Chat.setImageResource( R.mipmap.ic_launcher );
        }else {

            Glide.with( mcontext ).load( imageurl ).into( holder.img_Chat );
        }

        if (i == chatList.size() - 1){
            if (chat.isIsseen()){
                holder.txt_seen.setText( "Seen" );
            }else {
                holder.txt_seen.setText( "Delivered" );
            }
        }else {
            holder.txt_seen.setVisibility( View.GONE );
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_Chat;
        TextView show_message,txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            img_Chat = itemView.findViewById( R.id.img_pp );
            show_message = itemView.findViewById( R.id.show_message );
            txt_seen = itemView.findViewById( R.id.txt_seen );
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get( position ).getSender().equals( firebaseUser.getUid() )){
            return MSG_TYPE_RIGHT;
        }else if (!chatList.get( position ).getSender().equals( firebaseUser.getUid() )){
            return MSG_TYPE_LEFT;
        }else
        {
            return MSG_TYPE_LEFT;
        }
    }
}
