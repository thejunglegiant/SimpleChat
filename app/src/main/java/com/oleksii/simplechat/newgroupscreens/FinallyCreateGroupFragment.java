package com.oleksii.simplechat.newgroupscreens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.PeopleListAdapter;
import com.oleksii.simplechat.customviews.LogoView;

import java.util.ArrayList;

public class FinallyCreateGroupFragment extends Fragment {

    private MainActivity parentActivity;

    public FinallyCreateGroupFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finally_create_group, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_finallyCreateGroupFragment_to_newGroupFragment));

        EditText groupNameText = rootView.findViewById(R.id.group_name_edit);
        LogoView logoView = rootView.findViewById(R.id.room_logo);
        TextView membersText = rootView.findViewById(R.id.members);

        NewGroupVMFactory factory = new NewGroupVMFactory(parentActivity.getSocket());
        NewGroupViewModel viewModel = new ViewModelProvider(requireActivity(), factory)
                .get(NewGroupViewModel.class);

        RecyclerView recyclerView = rootView.findViewById(R.id.users_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PeopleListAdapter adapter = new PeopleListAdapter(new ArrayList<>(),
                viewModel, false);
        recyclerView.setAdapter(adapter);

        groupNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    logoView.setVisibility(View.VISIBLE);
                    logoView.addText(s.toString());
                } else {
                    logoView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        rootView.findViewById(R.id.fab).setOnClickListener(v -> {
            if (!groupNameText.getText().toString().trim().isEmpty()) {
                viewModel.createNewGroup(groupNameText.getText().toString().trim());
                viewModel.clear();
                Navigation.findNavController(v)
                        .navigate(R.id.action_finallyCreateGroupFragment_to_chatsListFragment);
            } else {
                Toast.makeText(getContext(), getString(R.string.enter_group_name_first),
                        Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.checkedUsers.observe(getViewLifecycleOwner(), list -> {
            String str = list.size() + " ";
            if (list.size() > 1) {
                str += getString(R.string.members);
            } else {
                str += getString(R.string.member);
            }

            adapter.submitAll(list);
            membersText.setText(str);
        });

        return rootView;
    }


}