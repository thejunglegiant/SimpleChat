package com.oleksii.simplechat.mainflow;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.RoomsListAdapter;
import com.oleksii.simplechat.objects.ChatObject;
import com.oleksii.simplechat.objects.Message;
import com.oleksii.simplechat.utils.IUserRest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoomsListFragment extends Fragment {

    private static final String TAG = "RoomsListFragment";
    private MainActivity parentActivity;
    private RoomsListAdapter adapter;
    private IUserRest IUserRest;
    public Socket mSocket;

    public RoomsListFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.urlString)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUserRest = retrofit.create(IUserRest.class);

        View rootView = inflater.inflate(R.layout.fragment_rooms_list, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search_button) {
                Log.i(TAG, item.getItemId() + "");
                return true;
            }
            return super.onOptionsItemSelected(item);
        });
        toolbar.setNavigationOnClickListener(v -> {
            parentActivity.toggleDrawer();
        });

        RecyclerView chatsList = rootView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoomsListAdapter(new ArrayList<>(), getContext());
        chatsList.setAdapter(adapter);

        RoomsListViewModel viewModel = new ViewModelProvider(this).get(RoomsListViewModel.class);

        viewModel.setCurrentUser(new ChatObject("Chat#1", new Message("Hello Vasya", new Date(1)), false));
        viewModel.setCurrentUser(new ChatObject("OOP FIOT", new Message("Hello Vasya", new Date(1)), false));

        viewModel.rooms.observe(getViewLifecycleOwner(), rooms -> {
            adapter.submitAll(rooms);
        });

        Button button = rootView.findViewById(R.id.test_button);
        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            FirebaseAuth.getInstance().getCurrentUser().delete();
        });

//        mSocket.on("register", onRegisterRequired);

        return rootView;
    }

    private Emitter.Listener onRegisterRequired = args -> getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {

        }
    });

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
}
