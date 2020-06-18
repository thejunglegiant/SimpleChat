package com.oleksii.simplechat.mainflow;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.ChatsListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatsListFragment extends Fragment {

    private static final String TAG = "ChatsListFragment";
    private MainActivity parentActivity;
    public Socket mSocket;

    public ChatsListFragment() { }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        parentActivity = (MainActivity) getActivity();
//        if (parentActivity != null && parentActivity.getSupportActionBar() != null)
//            parentActivity.getSupportActionBar().hide();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats_list, container, false);

        if (getActivity() != null)
            mSocket = ((MainActivity) getActivity()).mSocket;


        Button button = rootView.findViewById(R.id.test_button);
        button.setOnClickListener(v -> {
            mSocket.emit("verify_user", FirebaseAuth.getInstance().getUid());
        });
        mSocket.on("verified", onNewMessage);

//        RecyclerView chatsList = rootView.findViewById(R.id.chats_list);
//        ChatsListAdapter adapter = new ChatsListAdapter(this, );
//        chatsList.setAdapter(adapter);

        return rootView;
    }

    private Emitter.Listener onNewMessage = args -> getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            try {
                username = data.getString("username");
                message = data.getString("message");
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
                return;
            }

            // add the message to view
            String str = username + ": " + message;
            Log.i(TAG, str);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.i("ChatsListFragment", String.valueOf(id));

        return super.onOptionsItemSelected(item);
    }
}
