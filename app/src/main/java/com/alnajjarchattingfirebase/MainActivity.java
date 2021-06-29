package com.alnajjarchattingfirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alnajjarchattingfirebase.fragments.ChatFragment;
import com.alnajjarchattingfirebase.fragments.UsersFragment;
import com.alnajjarchattingfirebase.model.Users;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_img;
    TextView tv_username;
    FirebaseUser firebaseUser;
    DatabaseReference DbReference;
    TabLayout tabLayout;
    ViewPager viewPager;
    public static PubNub pubnub; // PubNub instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbarmain );
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( null );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        initPubnub();
        createChannel();
        profile_img = findViewById( R.id.profile_image );
        tv_username = findViewById( R.id.usernameMain );

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DbReference = FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );

        DbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user =dataSnapshot.getValue(Users.class);
                tv_username.setText( user.getUsername() );
/*
                   if (user.getImgURL().equals( "def" ) ){
                    Toast.makeText( MainActivity.this, "entered"+user.getImgURL(), Toast.LENGTH_SHORT ).show();
                    //img_uri!=null&&!img_uri.isEmpty()&&!img_uri.equals("null")&&img_uri.equals("image")
                    profile_img.setImageResource( R.mipmap.ic_launcher );
                }
                else{
                    Toast.makeText( MainActivity.this, "entered2", Toast.LENGTH_SHORT ).show();
                    Glide.with( getApplicationContext() ).load( user.getImgURL()).into( profile_img );
                    //Picasso.get().load(  user.getImgURL() ).into( profile_img );

                }*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );

         tabLayout = findViewById( R.id.tablayoutMain );
         viewPager = findViewById( R.id.viewPagerMain );

         ViewPagerAdpater viewPagerAdpater = new ViewPagerAdpater( getSupportFragmentManager() );
            viewPagerAdpater.addFragment( new ChatFragment(),"Chats" );
            viewPagerAdpater.addFragment( new UsersFragment(), " Users" );
            viewPager.setAdapter( viewPagerAdpater );
            tabLayout.setupWithViewPager( viewPager );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu,menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent( MainActivity.this, StartActivity.class ).setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK ));
                finish();
                break;
            case R.id.profile:
                Intent intent = new Intent( MainActivity.this, ProfileActivity.class );
                startActivity( intent );


                return true;

        }
        return false;
    }

    class ViewPagerAdpater extends FragmentPagerAdapter
    {

        private ArrayList<Fragment> fragmentArrayList;
        private ArrayList<String> titleStrings;


        public ViewPagerAdpater(FragmentManager fm) {
            super( fm );

            this.fragmentArrayList = new ArrayList<>();
            this.titleStrings = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get( position );
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        public void addFragment(Fragment frag,String title){
            fragmentArrayList.add( frag );
            titleStrings.add( title );
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleStrings.get( position );

        }
    }


    private void status(String status){
        DbReference  = FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );

        HashMap<String, Object > hashMap = new HashMap<>();
        hashMap.put( "status", status );

        DbReference.updateChildren( hashMap );
    }

    // Creates PubNub instance with your PubNub credentials. https://admin.pubnub.com/
    // This instance will be used when we need to create connection to PubNub.
    private void initPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("YOUR_PUB_KEY_HERE");
        pnConfiguration.setSubscribeKey("YOUR_SUB_KEY_HERE");
        pnConfiguration.setSecure(true);
        pubnub = new PubNub(pnConfiguration);
    }
    // Creates notification channel.
    private void createChannel() {
        // Notification channel should only be created for devices running Android API level 26+.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel chan1 = new NotificationChannel(
                    "default",
                    "default",
                    NotificationManager.IMPORTANCE_NONE);
            chan1.setLightColor(Color.TRANSPARENT);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            notificationManager.createNotificationChannel(chan1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status( "online" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        status( "offline" );
    }
}
