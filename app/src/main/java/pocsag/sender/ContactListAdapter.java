package pocsag.sender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder> {
    private List<Contact> contactList;
    private RecyclerView mRecyclerView;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ContactListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView number;

        public ContactListViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactListAdapter(List<Contact> contacts, RecyclerView mRecyclerView) {
        this.contactList = contacts;
        this.mRecyclerView = mRecyclerView;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ContactListAdapter.ContactListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childAdapterPosition = mRecyclerView.getChildAdapterPosition(v);
                Contact contact = contactList.get(childAdapterPosition);
                Context context = v.getContext();
                Intent myIntent = new Intent(context, MessageListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", contact.getId());
                bundle.putString("name", contact.getName());
                bundle.putString("number", contact.getNumber());
                myIntent.putExtras(bundle);
                context.startActivity(myIntent);
            }
        });
        return new ContactListViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ContactListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Contact contact = contactList.get(position);
        holder.number.setText(contact.getNumber());
        holder.name.setText(contact.getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

