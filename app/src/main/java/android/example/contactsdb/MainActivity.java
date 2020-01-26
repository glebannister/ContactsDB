package android.example.contactsdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import data.Contact;
import model.ContactDatabase;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactDatabase contactDatabase;
    private String name, secondName, email, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);


        contactDatabase = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class,
                "ContactDB").build();

        new ShowAllContacts().execute();

        contactsAdapter = new ContactsAdapter(this, contactsArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContact(false, null, -1);
            }


        });

    }

    public void addAndEditContact(final boolean isUpdate, final Contact contact, final int position) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_layout, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView newContactTitle = view.findViewById(R.id.newContactTitle);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText secondNameEditText = view.findViewById(R.id.secondNameEditText);
        final EditText emailEditText = view.findViewById(R.id.emailEditText);
        final EditText phoneEditText = view.findViewById(R.id.phoneEditText);

        newContactTitle.setText(!isUpdate ? "Add Car" : "Edit Car");

        if (isUpdate && contact != null) {
            nameEditText.setText(contact.getName());
            secondNameEditText.setText(contact.getSecondName());
            emailEditText.setText(contact.getEmail());
            phoneEditText.setText(contact.getPhone());
        }
            alertDialogBuilderUserInput
                    .setCancelable(true)
                    .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                        }
                    })
                    .setNegativeButton(isUpdate ? "Delete" : "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {

                                    if (isUpdate) {

                                        deleteContact(contact, position);
                                    } else {

                                        dialogBox.cancel();

                                    }

                                }
                            });


            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = nameEditText.getText().toString();
                    secondName = secondNameEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    phone = phoneEditText.getText().toString();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(MainActivity.this, "Enter name!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(secondName)) {
                        Toast.makeText(MainActivity.this, "Enter second name!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(email)) {
                        Toast.makeText(MainActivity.this, "Enter email!", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(MainActivity.this, "Enter phone!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        alertDialog.dismiss();
                    }

                    if (isUpdate && contact != null) {
                       updateContact(name, secondName, email, phone, position);
                    } else {
                        createContact(name, secondName, email, phone);
                    }
                }
            });
        }

    private void createContact (String name, String secondName, String email, String phone){
        new CreateContactTask().execute(new Contact(0, name, secondName, email, phone));
    }

    private class CreateContactTask extends AsyncTask <Contact, Void, Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            long id = contactDatabase.getContactDao().addContact(contacts[0]);
            Contact contact = contactDatabase.getContactDao().getContact(id);
            if (contact != null){
                contactsArrayList.add(0, contact);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

   private class ShowAllContacts extends AsyncTask <Void, Void, Void>{

       @Override
       protected Void doInBackground(Void... voids) {
           contactsArrayList.addAll(contactDatabase.getContactDao().getAllContacts());
           return null;
       }
   }

    private void deleteContact ( Contact contact, int position){
        contactsArrayList.remove(position);
        new DeleteContactTask().execute(contact);
    }

    private class DeleteContactTask extends AsyncTask<Contact, Void, Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDatabase.getContactDao().deleteContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private void updateContact (String name, String secondName, String email, String phone, int position){
        Contact contact = contactsArrayList.get(position);
        contact.setName(name);
        contact.setSecondName(secondName);
        contact.setEmail(email);
        contact.setPhone(phone);

        new UpdateContactTask().execute(contact);

    }

    private class UpdateContactTask extends AsyncTask<Contact, Void, Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDatabase.getContactDao().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }
}
