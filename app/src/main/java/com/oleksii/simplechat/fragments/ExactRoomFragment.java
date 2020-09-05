package com.oleksii.simplechat.fragments;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding4.recyclerview.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding4.recyclerview.RxRecyclerView;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.oleksii.simplechat.MyApplication;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.MessagesListAdapter;
import com.oleksii.simplechat.adapters.delegates.ClickDelegate;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.factories.ExactRoomVMFactory;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.utils.DateUtil;
import com.oleksii.simplechat.utils.KeyboardUtil;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ExactRoomFragment extends Fragment {

    private long roomId;
    private String roomTitle;
    private Toolbar toolbar, actionsToolbar;
    private TextView titleText, membersText, floatingHeader, typingText, selectedMessagesCounter;
    private LogoView roomLogo;
    private ImageButton sendButton;
    private EditText messageEditText;
    private LinearLayoutManager layoutManager;
    private RecyclerView messagesListRecycler;
    private MessagesListAdapter mAdapter = new MessagesListAdapter();
    private ExactRoomViewModel viewModel;

    private final long TYPING_ANIMATION_SPEED = 300;

    public ExactRoomFragment() { }

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

        toolbar = rootView.findViewById(R.id.main_toolbar);
        actionsToolbar = rootView.findViewById(R.id.actions_toolbar);
        titleText = rootView.findViewById(R.id.room_title);
        membersText = rootView.findViewById(R.id.room_members_amount);
        roomLogo = rootView.findViewById(R.id.room_logo);
        typingText = rootView.findViewById(R.id.typing_event_text);
        floatingHeader = rootView.findViewById(R.id.floating_header);
        selectedMessagesCounter = rootView.findViewById(R.id.selected_messages_counter);
        messagesListRecycler = rootView.findViewById(R.id.messages_list);
        sendButton = rootView.findViewById(R.id.send_button);
        messageEditText = rootView.findViewById(R.id.message_box);

        setupToolbars();
        setupAdapter();
        setupViewModel();
        setupFloatingHeader();
        setupMessageBoxSection();

        return rootView;
    }

    private void setupToolbars() {
        // Main toolbar
        toolbar.inflateMenu(R.menu.toolbar_exact_room);
        toolbar.setNavigationOnClickListener(v -> {
            goToMainList();
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.leave_room_button) {
                viewModel.leaveGroup();
                goToMainList();
                return true;
            }
            return super.onOptionsItemSelected(item);
        });
        titleText.setText(roomTitle);
        roomLogo.setText(roomTitle);

        // Actions toolbar
        actionsToolbar.inflateMenu(R.menu.toolbar_edit);
        actionsToolbar.setNavigationOnClickListener(v -> {
            hideActionsToolbar();
        });
        actionsToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_button:
                    // TODO
                    return true;
                case R.id.copy_text_button:
                    // TODO
                    return true;
                case R.id.save_message_button:
                    viewModel.saveMessages();
                    hideActionsToolbar();
                    return true;
                case R.id.delete_message_button:
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        });
    }

    private void goToMainList() {
        KeyboardUtil.hideKeyboardFrom(messageEditText);
        Navigation.findNavController(toolbar)
                .navigate(R.id.action_exactRoomFragment_to_chatsListFragment);
    }

    private void hideActionsToolbar() {
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                actionsToolbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        actionsToolbar.startAnimation(fadeOut);
        viewModel.clearSaveMessagesList();
        mAdapter.unCheckAllMessages();
    }

    private void setupAdapter() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                actionsToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        mAdapter.attachDelegate(new ClickDelegate() {
            @Override
            public void onShortClickEvent(Message message) {
                viewModel.addMessageToSaveList(message);
                selectedMessagesCounter.setText(String.valueOf(viewModel.getSaveMessagesList().size()));
            }

            @Override
            public void onSecondShortClickEvent(Message message) {
                viewModel.removeMessageFromSaveList(message);
                int listSize = viewModel.getSaveMessagesList().size();
                selectedMessagesCounter.setText(String.valueOf(listSize));
                if (listSize < 1) {
                    hideActionsToolbar();
                }
            }

            @Override
            public void onLongClickEvent(Message message) {
                viewModel.addMessageToSaveList(message);
                selectedMessagesCounter.setText(String.valueOf(viewModel.getSaveMessagesList().size()));

                if (actionsToolbar.getVisibility() != View.VISIBLE)
                    actionsToolbar.startAnimation(fadeIn);
            }
        });
        messagesListRecycler.setLayoutManager(layoutManager);
        messagesListRecycler.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        ExactRoomVMFactory factory = new ExactRoomVMFactory(roomId, getContext(),
                ((MyApplication) getActivity().getApplication()).getApplicationComponent()
                        .getDatabase().messagesDao());
        viewModel = new ViewModelProvider(this, factory).get(ExactRoomViewModel.class);
        viewModel.getmMessagesList().observe(getViewLifecycleOwner(), list -> {
            mAdapter.submitAll(list);
            messagesListRecycler.scrollToPosition(mAdapter.getItemCount() - 1);
        });
        viewModel.getMembersCount().observe(getViewLifecycleOwner(), number -> {
            String str = number + " " + getString(R.string.members);
            membersText.setText(str);
        });
        viewModel.getTypingUser().observe(getViewLifecycleOwner(), name -> {
            String str = name + " " + getString(R.string.is_typing);
            startTypingAnimation(str);
        });
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
                                viewModel.getmMessagesList().getValue().get(layoutManager
                                        .findFirstVisibleItemPosition()).getSendingTime()));

                        handler.removeCallbacks(runnable);
                        if (animationAllowed[0]
                                && layoutManager.findLastVisibleItemPosition()
                                < viewModel.getmMessagesList().getValue().size() - 1) {
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

    private void startTypingAnimation(String str) {
        Handler handler = new Handler();
        typingText.setText(str);
        membersText.setVisibility(View.INVISIBLE);
        typingText.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> {
            typingText.setText(str + ".");
            handler.postDelayed(() -> {
                typingText.setText(str + "..");
                handler.postDelayed(() -> {
                    typingText.setText(str + "...");
                    handler.postDelayed(() -> {
                        membersText.setVisibility(View.VISIBLE);
                        typingText.setVisibility(View.INVISIBLE);
                    }, TYPING_ANIMATION_SPEED);
                }, TYPING_ANIMATION_SPEED);
            }, TYPING_ANIMATION_SPEED);
        }, TYPING_ANIMATION_SPEED);
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
        RxTextView.textChanges(messageEditText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            viewModel.typingEvent();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        sendButton.setOnClickListener(v -> {
            viewModel.sendMessage(messageEditText.getText().toString().trim());
            messageEditText.setText("");
            messagesListRecycler.postDelayed(() ->
                    messagesListRecycler.smoothScrollToPosition(mAdapter.getItemCount()), 100);
        });
    }
}