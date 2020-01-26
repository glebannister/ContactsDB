package android.example.contactsdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import data.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact> contactsArrayList;
    private MainActivity mainActivity;

    public ContactsAdapter(Context context, ArrayList<Contact> contactsArrayList, MainActivity mainActivity) {
        this.context = context;
        this.contactsArrayList = contactsArrayList;
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Contact contacts = contactsArrayList.get(position);
        holder.nameTextView.setText(contacts.getName());
        holder.secondNameTextView.setText(contacts.getSecondName());
        holder.emailTextView.setText(contacts.getEmail());
        holder.phoneTextView.setText(contacts.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               mainActivity.addAndEditContact(true, contacts, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView secondNameTextView;
        public TextView emailTextView;
        public TextView phoneTextView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            secondNameTextView = itemView.findViewById(R.id.secondNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }

    }
}
