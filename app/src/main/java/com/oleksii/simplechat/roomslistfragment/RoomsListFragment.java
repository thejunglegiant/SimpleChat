package com.oleksii.simplechat.roomslistfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.RoomsListAdapter;

import java.util.ArrayList;

public class RoomsListFragment extends Fragment {

    private static final String TAG = "RoomsListFragment";
    private MainActivity parentActivity;

    public RoomsListFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms_list, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search_button) {
                Log.i(TAG, item.getItemId() + "");
                // TODO
                // temporary
                FirebaseAuth.getInstance().signOut();
                return true;
            }
            return super.onOptionsItemSelected(item);
        });
        toolbar.setNavigationOnClickListener(v -> {
            parentActivity.toggleDrawer();
        });

        RecyclerView chatsList = rootView.findViewById(R.id.rooms_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
        RoomsListAdapter adapter = new RoomsListAdapter(new ArrayList<>());
        chatsList.setAdapter(adapter);

        RoomListVMFactory factory = new RoomListVMFactory(parentActivity.getSocket());
        RoomsListViewModel viewModel = new ViewModelProvider(this, factory).get(RoomsListViewModel.class);

        viewModel.availableRooms.observe(getViewLifecycleOwner(), adapter::submitAll);

//        Navigation.findNavController(getActivity(), R.id.fragments_container)
//                .navigate(R.id.action_chatsListFragment_to_newGroupFragment);

        return rootView;
    }
}
