package com.oleksii.simplechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.constants.NetworkConstants;
import com.oleksii.simplechat.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();
    private static String firstname, lastname;
    public NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    @Inject Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppComponent appComponent = DaggerAppComponent.builder().build();
        appComponent.inject(this);

        firstname = getIntent().getStringExtra("name");
        lastname = getIntent().getStringExtra("surname");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mSocket.connected()) {
            // TODO Connection StatusBar
        }
        mSocket.emit(NetworkConstants.SYNC_EVENT_ID, new Gson()
                .toJson(new User(FirebaseAuth.getInstance().getUid(), firstname, lastname)));
        mSocket.on(NetworkConstants.SYNCED_EVENT_ID, synced);
        mSocket.on(NetworkConstants.CONNECT_EVENT_ID, onConnectEvent);
        mSocket.on(NetworkConstants.RECONNECT_EVENT_ID, onReconnectEvent);
        mSocket.on(NetworkConstants.DISCONNECT_EVENT_ID, onDisconnectEvent);
    }

    private Emitter.Listener synced = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                firstname = data.getString("firstname");
                lastname = data.getString("lastname");
                runOnUiThread(MainActivity.this::setNames);
                runOnUiThread(MainActivity.this::setModeOnline);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReconnectEvent = args -> {
        mSocket.emit(NetworkConstants.SYNC_EVENT_ID, new Gson()
                .toJson(new User(FirebaseAuth.getInstance().getUid(), firstname, lastname)));
    };

    private Emitter.Listener onDisconnectEvent = args -> {
        runOnUiThread(MainActivity.this::setModeOffline);
    };

    private Emitter.Listener onConnectEvent = args -> {
        runOnUiThread(MainActivity.this::setModeOnline);
    };

    private void setModeOnline() {
        View headerView = navigationView.getHeaderView(0);
        TextView statusText = headerView.findViewById(R.id.online_status);
        statusText.setText(R.string.online);
    }

    private void setModeOffline() {
        View headerView = navigationView.getHeaderView(0);
        TextView statusText = headerView.findViewById(R.id.online_status);
        statusText.setText(R.string.offline);
    }

    private void setNames() {
        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.user_name);
        LogoView logoView = headerView.findViewById(R.id.user_logo);
        String str = firstname + " " + lastname;
        logoView.setText(str);
        textView.setText(str);
    }

    public void toggleDrawer() {
        if (!mDrawerLayout.isOpen()) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.new_group:
                Intent intent = new Intent(this, NewGroupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                toggleDrawer();
                break;
            case R.id.saved_messages:
                Navigation.findNavController(this, R.id.fragments_container)
                        .navigate(R.id.action_roomsListFragment_to_savedMessagesFragment);
                toggleDrawer();
                break;
            case R.id.settings:
                // TODO
                break;
            case R.id.invite_friends:
                Navigation.findNavController(this, R.id.fragments_container)
                        .navigate(R.id.action_chatsListFragment_to_inviteFriendsFragment);
                toggleDrawer();
                break;
            default:
                return false;
        }
        return true;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
