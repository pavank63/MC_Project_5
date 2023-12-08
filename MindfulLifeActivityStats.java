package com.example.app.ui.emergency;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.app.R;
import com.example.app.ui.home.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomEmergencyContactsActivity extends MainActivity {

    private static final String TAG = "CustomEmergencyContactsActivity";

    private Button addContactButton;
    private Button deleteListButton;
    private ListView contactListView;
    private CustomEmergencyContactsActivity.ContactListAdapter contactListAdapter;

    private FirebaseFirestore firestore;
    private String userID;

    private final List<CustomEmergencyContact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.frag_container);
        getLayoutInflater().inflate(R.layout.activity_emergency_contacts, contentFrameLayout);

        this.addContactButton = findViewById(R.id.addContactButton);
        this.deleteListButton = findViewById(R.id.delete_list_button);

        this.firestore = FirebaseFirestore.getInstance();
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.contactListAdapter = new CustomEmergencyContactsActivity.ContactListAdapter();

        this.getAllContactsFromDatabase();

        this.contactListView = findViewById(R.id.contactListView);
        this.contactListView.setAdapter(this.contactListAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final View infoDialogView = getLayoutInflater().inflate(R.layout.info_contact_dialog, null);
                final TextView nameLabel = infoDialogView.findViewById(R.id.nameLabel);
                final TextView phoneNumberLabel = infoDialogView.findViewById(R.id.phoneNumberLabel);
                final TextView emailLabel = infoDialogView.findViewById(R.id.emailLabel);

                final CustomEmergencyContact contact = contacts.get(pos);
                nameLabel.setText(contact.getContactName());
                phoneNumberLabel.setText(String.valueOf(contact.getPhoneNumber()));
                emailLabel.setText(contact.getEmail());

                AlertDialog infoDialog = new AlertDialog.Builder(CustomEmergencyContactsActivity.this)
                        .setView(infoDialogView)
                        .setNegativeButton("Delete Contact", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeContactFromDatabase(contact.getContactId());
                                contacts.remove(contact);
                                contactListAdapter.setData(contacts);
                            }
                        })
                        .create();
                infoDialog.show();
            }
        });

        addContactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View addContactDialogView = getLayoutInflater().inflate(R.layout.add_new_contact_dialog, null);
                final EditText contactName = addContactDialogView.findViewById(R.id.contact_name);
                final EditText phoneNumber = addContactDialogView.findViewById(R.id.phone_number);
                final EditText email = addContactDialogView.findViewById(R.id.email);

                AlertDialog dialog = new AlertDialog.Builder(CustomEmergencyContactsActivity.this)
                        .setView(addContactDialogView)
                        .setPositiveButton(null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (contactName.getText().toString().isEmpty()) {
                                    Toast.makeText(CustomEmergencyContactsActivity.this, "ERROR: Please specify contact name", Toast.LENGTH_SHORT).show();
                                }
                                if (phoneNumber.getText().toString().isEmpty() || (!isPhoneNumberValid(phoneNumber.getText().toString()))) {
                                    Toast.makeText(CustomEmergencyContactsActivity.this, "ERROR: Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                                }
                                if (email.getText().toString().isEmpty() || (!isEmailValid(email.getText().toString()))) {
                                    Toast.makeText(CustomEmergencyContactsActivity.this, "ERROR: Please enter a valid email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CustomEmergencyContactsActivity.this, "Successfully added emergency contact", Toast.LENGTH_SHORT).show();
                                    CustomEmergencyContact newContact;
                                    try {
                                        newContact = new CustomEmergencyContact(contactName.getText().toString(), Long.parseLong(phoneNumber.getText().toString()), email.getText().toString());
                                        contacts.add(newContact);
                                        // Add to database
                                        addContactToDatabase(newContact.getContactId(), contactName.getText().toString(), Long.parseLong(phoneNumber.getText().toString()), email.getText().toString());
                                        contactListAdapter.setData(contacts);
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(CustomEmergencyContactsActivity.this, "ERROR: Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setPositiveButtonIcon(AppCompat
