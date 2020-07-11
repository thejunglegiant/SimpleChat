package com.oleksii.simplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.authentication.SplashScreenActivity;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.objects.User;
import com.oleksii.simplechat.utils.Constants;
import com.oleksii.simplechat.utils.IRest;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static String name, surname;
    public NavigationView navigationView;
    public ChatApplication app;
    private DrawerLayout mDrawerLayout;
    private IRest IRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CHAT_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IRest = retrofit.create(IRest.class);
        app = (ChatApplication) this.getApplication();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loginUser();
    }

    public void toggleDrawer() {
        if (!mDrawerLayout.isOpen()) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void loginUser() {
        User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                name, surname, new Timestamp(System.currentTimeMillis()));

        Call<User> call = IRest.registerUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Code: " + response.code());
                    Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                User user = response.body();
                if (user != null) {
                    MainActivity.name = user.getFirstname();
                    MainActivity.surname = user.getLastname();

                    View headerView = navigationView.getHeaderView(0);
                    TextView textView = headerView.findViewById(R.id.user_name);
                    LogoView logoView = headerView.findViewById(R.id.user_logo);
                    String str = user.getFirstname() + " " + user.getLastname();
                    logoView.addText(str);
                    textView.setText(str);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
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

    public String getName() {
        return name;
    }
}
