package com.alnajjarchattingfirebase.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alnajjarchattingfirebase.R;
import com.alnajjarchattingfirebase.adapters.UsersRecyclerAdapters;
import com.alnajjarchattingfirebase.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersRecyclerAdapters usersRecyclerAdapters;
    private List<Users> mUsers;

    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_users, container, false );
        // Inflate the layout for this fragment
        recyclerView = view.findViewById( R.id.rcv_users );
        mUsers= new ArrayList<>(  );
        readUsers();

        search_users = view.findViewById( R.id.search_users );
        search_users.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUsers(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );



        usersRecyclerAdapters = new UsersRecyclerAdapters(getContext(),mUsers, false);
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );

        recyclerView.setHasFixedSize( true );


        return view;
    }

    private void searchUsers(String s) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild( "search" )
                .startAt( s )
                .endAt( s+"\uf8ff" );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);

                    if (!users.getId().equals( firebaseUser.getUid() )){

                        mUsers.add( users );
                    }
                }
                usersRecyclerAdapters = new UsersRecyclerAdapters( getContext() , mUsers , false );
                recyclerView.setAdapter( usersRecyclerAdapters );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference("Users");

        DBRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals( "" )) {

                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users users = snapshot.getValue( Users.class );
                        //                    GenericTypeIndicator<Map<String, List<Users>>> genericTypeIndicator = new GenericTypeIndicator<Map<String, List<Users>>>() {};
                        //                    Map<String, List<Users>> hashMap = dataSnapshot.getValue(genericTypeIndicator);
                        //
                        //
                        //                    for (Map.Entry<String,List<Users>> entry : hashMap.entrySet()) {
                        //                        List<Users> usersList = entry.getValue();
                        //                        for (Users users: usersList){
                        //                            Log.i(TAG, users.getId());
                        //                            Toast.makeText( getActivity(), "users"+ users.getId(),Toast.LENGTH_SHORT ).show();
                        //                        }}
                        //Toast.makeText( getActivity(), "users"+dataSnapshot.getValue(),Toast.LENGTH_LONG ).show();
                        assert users != null;
                        assert firebaseUser != null;
                        if (!users.getId().equals( firebaseUser.getUid() )) {
                            mUsers.add( users );
                        }
                    }
                    recyclerView.setAdapter( usersRecyclerAdapters );
                    usersRecyclerAdapters.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


}
