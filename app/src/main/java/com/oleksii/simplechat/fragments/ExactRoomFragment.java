package com.oleksii.simplechat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding4.recyclerview.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding4.recyclerview.RxRecyclerView;
import com.oleksii.simplechat.activities.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.MessagesListAdapter;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.factories.ExactRoomVMFactory;
import com.oleksii.simplechat.utils.DateUtil;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ExactRoomFragment extends Fragment {

    private long roomId;
    private String roomTitle;
    private MainActivity parentActivity;
    private Toolbar toolbar;
    private TextView titleText, membersText, floatingHeader;
    private LogoView roomLogo;
    private ImageButton sendButton;
    private EditText messageEditText;
    private LinearLayoutManager layoutManager;
    private RecyclerView messagesListRecycler;
    private MessagesListAdapter mAdapter = new MessagesListAdapter();
    private ExactRoomViewModel viewModel;

    public ExactRoomFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getLong("roomId");
            roomTitle = getArguments().getString("roomTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exact_room, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        titleText = rootView.findViewById(R.id.room_title);
        membersText = rootView.findViewById(R.id.room_members_amount);
        roomLogo = rootView.findViewById(R.id.room_logo);
        floatingHeader = rootView.findViewById(R.id.floating_header);
        messagesListRecycler = rootView.findViewById(R.id.messages_list);
        sendButton = rootView.findViewById(R.id.send_button);
        messageEditText = rootView.findViewById(R.id.message_box);

        setupToolbar();
        setupAdapter();
        setupViewModel();
        setupFloatingHeader();
        setupMessageBoxSection();

        return rootView;
    }

    private void setupFloatingHeader() {
        final boolean[] animationAllowed = {false};
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        Handler handler = new Handler();
        Runnable runnable = () -> floatingHeader.startAnimation(fadeOut);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingHeader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingHeader.setVisibility(View.GONE);
                animationAllowed[0] = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        RxRecyclerView.scrollEvents(messagesListRecycler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecyclerViewScrollEvent>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) { }

                    @Override
                    public void onNext(@NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) {
                        floatingHeader.setText(DateUtil.getDayMonthString(
                                viewModel.getMessagesList().getValue().get(layoutManager
                                        .findFirstVisibleItemPosition()).getSendingTime()));

                        handler.removeCallbacks(runnable);
                        if (animationAllowed[0]
                                && layoutManager.findLastVisibleItemPosition()
                                < viewModel.getMessagesList().getValue().size() - 1) {
                            animationAllowed[0] = false;
                            floatingHeader.startAnimation(fadeIn);
                        }
                        handler.postDelayed(runnable, 500);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_exactRoomFragment_to_chatsListFragment));
        titleText.setText(roomTitle);
        roomLogo.addText(roomTitle);
    }

    private void setupAdapter() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);
        messagesListRecycler.setLayoutManager(layoutManager);
        messagesListRecycler.setAdapter(mAdapter);
        messagesListRecycler.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                        oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                messagesListRecycler.postDelayed(() ->
                        messagesListRecycler.smoothScrollToPosition(mAdapter.getItemCount()), 100);
            }
        });
    }

    private void setupViewModel() {
        ExactRoomVMFactory factory = new ExactRoomVMFactory(roomId, parentActivity.getFirstname(),
                parentActivity.getLastname(), getContext());
        viewModel = new ViewModelProvider(this, factory).get(ExactRoomViewModel.class);
        viewModel.getMessagesList().observe(getViewLifecycleOwner(), list -> {
            mAdapter.submitAll(list);
            messagesListRecycler.scrollToPosition(mAdapter.getItemCount() - 1);
        });
        viewModel.getMembersAmount().observe(getViewLifecycleOwner(), number -> {
            String str = number + " " + getString(R.string.members);
            membersText.setText(str);
        });
    }

    private void setupMessageBoxSection() {
        Animation appearAnim = AnimationUtils.loadAnimation(getContext(), R.anim.send_button_appear);
        Animation disappearAnim = AnimationUtils.loadAnimation(getContext(), R.anim.send_button_disappear);
        disappearAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && sendButton.getVisibility() == View.GONE) {
                    sendButton.startAnimation(appearAnim);
                    sendButton.setVisibility(View.VISIBLE);
                } else if (s.length() < 1) {
                    sendButton.startAnimation(disappearAnim);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        sendButton.setOnClickListener(v -> {
            viewModel.sendMessage(messageEditText.getText().toString().trim());
            messageEditText.setText("");
        });
    }
}