package pocsag.sender;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.List;
import java.util.Objects;

import pocsag.sender.sql.DbHelper;

public class MessageListActivity extends Activity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<Message> messages;
    private Toolbar mToolbar;
    private String number;
    private EditText messageEdit;
    private View sendButton;
    private DbHelper dbHelper;
    private final LinearLayoutManager layout = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        final long id = Objects.requireNonNull(getIntent().getExtras()).getLong("id");
        final String name = Objects.requireNonNull(getIntent().getExtras()).getString("name");
        number = Objects.requireNonNull(getIntent().getExtras()).getString("number");
        dbHelper = new DbHelper(this.getBaseContext());
        messages = dbHelper.getListOfMessage(id);
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageRecycler.setNestedScrollingEnabled(false);

        layout.setReverseLayout(false);
        layout.setStackFromEnd(true);

        mMessageRecycler.setLayoutManager(layout);
        mToolbar = findViewById(R.id.toolbar2);
        mToolbar.setTitle(name == null ? number : name);
        mToolbar.setTitleTextColor(Color.WHITE);
        mMessageAdapter = new MessageListAdapter(this, messages);
        mMessageRecycler.setAdapter(mMessageAdapter);
        messageEdit = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageEdit.setActivated(false);
                sendButton.setActivated(false);
                String string = messageEdit.getText().toString();
                if (string.replaceAll("\\s", "").isEmpty())
                    return;
                Intent myIntent = new Intent(v.getContext(), SendMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.getLong("id", id);
                bundle.putString("number", number);
                bundle.putString("message", string);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, 0);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Message message = dbHelper.addMessage(
                    Objects.requireNonNull(data.getExtras()).getLong("id"),
                    Objects.requireNonNull(data.getExtras()).getString("message"));
            messageEdit.getText().clear();
            messages.add(message);
            mMessageAdapter.notifyItemInserted(messages.size() - 1);
            layout.scrollToPosition(messages.size() - 1);
        }
        messageEdit.setActivated(true);
        sendButton.setActivated(true);
    }
}