package com.sinto.sinto.visitour2.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sinto.sinto.visitour2.R;
import com.sinto.sinto.visitour2.data.MessageEntity;
import com.sinto.sinto.visitour2.data.UserEntity;
import com.sinto.sinto.visitour2.legacy.SignInActivity;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity implements ChatActivityView {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public ImageView messageImageView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "https://visitour-2a6d8.firebaseio.com/message";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    public static final String INSTANCE_ID_TOKEN_RETRIEVED = "iid_token_retrieved";
    public static final String FRIENDLY_MSG_LENGTH = "friendly_msg_length";

    private UserEntity user;
    private SharedPreferences sharedPreferences;

    private DatabaseReference firebaseDatabaseReference;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseRecyclerAdapter<MessageEntity, MessageViewHolder> firebaseAdapter;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.sendButton) Button sendButton;
    @BindView(R.id.messageEditText) EditText editText;
    @BindView(R.id.addMessageImageView) ImageView addMessageImageView;
    @BindView(R.id.messageRecyclerView) RecyclerView messageRecyclerView;
    LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        user = new UserEntity();
        user.displayName = ANONYMOUS;
        user.email = ANONYMOUS;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAdapter = new FirebaseRecyclerAdapter<MessageEntity, MessageViewHolder>(
                MessageEntity.class, R.layout.item_message,
                MessageViewHolder.class, firebaseDatabaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected MessageEntity parseSnapshot(DataSnapshot snapshot) {
                MessageEntity message = super.parseSnapshot(snapshot);
                if (message != null) {
                    message.messageId = snapshot.getKey();
                }
                return message;
            }

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, MessageEntity message, int position) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (message.text != null) {
                    viewHolder.messageTextView.setText(message.text);
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else {
                    String imageUrl = message.imageUrl.toString();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Glide.with(viewHolder.messageImageView.getContext())
                                            .load(downloadUrl)
                                            .into(viewHolder.messageImageView);
                                } else {
                                    Log.w(TAG, "Getting download unsuccessful", task.getException());
                                }
                            }
                        });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(message.imageUrl)
                                .into(viewHolder.messageImageView);
                    }
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }

                viewHolder.messengerTextView.setText(message.senderId);
                if (message.imageUrl == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this, R.drawable.logoo));
                } else {
                    Glide.with(viewHolder.messageImageView.getContext())
                            .load(message.imageUrl)
                            .into(viewHolder.messageImageView);
                }

                if (message.text != null) {
                    FirebaseAppIndex.getInstance().update(getMessageIndexable(message));
                }
                FirebaseUserActions.getInstance().end(getMessageViewAction(message));
            }
        };

        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    messageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        if (firebaseUser == null) {
            redirectToLoginActivity();
            return;
        } else {
            user.displayName = firebaseUser.getDisplayName();
            if (firebaseUser.getPhotoUrl() != null) {
                user.photoUrl = firebaseUser.getPhotoUrl();
            }
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.i(TAG, "Google API Connection Failed.");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setAdapter(firebaseAdapter);

        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(
                        sharedPreferences.getInt(FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageEntity message = new MessageEntity();
                message.text = editText.getText().toString();
                message.senderId = user.displayName;
                message.imageUrl = user.photoUrl;

                firebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
                editText.setText("");
            }
        });
    }


    private Action getMessageViewAction(MessageEntity message) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(message.senderId, MESSAGE_URL.concat(message.messageId))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }

    private Indexable getMessageIndexable(MessageEntity message) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(user.email == message.senderId)
                .setName(message.senderId)
                .setUrl(MESSAGE_URL.concat(message.messageId + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(user.email)
                .setUrl(MESSAGE_URL.concat(message.messageId + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(message.text)
                .setUrl(MESSAGE_URL.concat(message.messageId))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
    }

    public void redirectToLoginActivity() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    MessageEntity message = new MessageEntity();
                    message.senderId = user.email;
                    message.imageUrl = user.photoUrl;

                    firebaseDatabaseReference.child(MESSAGES_CHILD).push()
                            .setValue(message, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();
                                        StorageReference storageReference =
                                                FirebaseStorage.getInstance()
                                                        .getReference(firebaseUser.getUid())
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());

                                        putImageInStorage(storageReference, uri, key);
                                    } else {
                                        Log.w(TAG, "Unable to write message to database.",
                                                databaseError.toException());
                                    }
                                }
                            });
                }
            }
        } else if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_sent");

                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(ChatActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            MessageEntity message = new MessageEntity();
                            message.senderId = user.email;
                            message.imageUrl = user.photoUrl;
                            firebaseDatabaseReference.child(MESSAGES_CHILD).child(key)
                                    .setValue(message);
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_menu:
                sendInvitation();
                return true;
            case R.id.crash_menu:
//                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
//                causeCrash();
                return true;
            case R.id.sign_out_menu:
//                firebaseAuth.signOut();
//                Auth.GoogleSignInApi.signOut(googleApiClient);
//                firebaseUser = null;
//                username = ANONYMOUS;
//                photoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            case R.id.fresh_config_menu:
//                fetchConfig();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
}

