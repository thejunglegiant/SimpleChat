package com.oleksii.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static String name, surname;
    public NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Socket mSocket; {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSocket.connect();
        if (!mSocket.connected()) {
            // TODO Connection StatusBar
        }
        mSocket.emit("sync", new Gson()
                .toJson(new User(FirebaseAuth.getInstance().getUid(), name, surname)));
        mSocket.on("synced", synced);
        mSocket.on("connect", onConnectEvent);
        mSocket.on("reconnect", onReconnectEvent);
        mSocket.on("disconnect", onDisconnectEvent);
    }

    private Emitter.Listener synced = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                name = data.getString("firstname");
                surname = data.getString("lastname");
                runOnUiThread(MainActivity.this::setNames);
                runOnUiThread(MainActivity.this::setModeOnline);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReconnectEvent = args -> {
        mSocket.emit("sync", new Gson()
                .toJson(new User(FirebaseAuth.getInstance().getUid(), name, surname)));
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
        String str = name + " " + surname;
        logoView.addText(str);
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
                Navigation.findNavController(this, R.id.fragments_container)
                        .navigate(R.id.action_chatsListFragment_to_newGroupFragment);
                toggleDrawer();
                break;
            case R.id.saved_messages:
                // TODO
                break;
            case R.id.settings:
                // TODO
                break;
            case R.id.faq:
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

    public Socket getSocket() {
        return this.mSocket;
    }

    public String getName() {
        return name;
    }
}
