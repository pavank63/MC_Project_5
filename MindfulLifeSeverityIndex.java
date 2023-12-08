package com.example.app.ui.emergency;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.app.R;
import com.example.app.ui.home.MainActivity;

import java.util.ArrayList;
import java.util.Map;

public class CustomDialogFragment extends DialogFragment {

    private static final int SELECTED_PIC = 1;
    private static final int REQUEST_CALL = 1;
    protected DrawerLayout draw;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    ArrayList<String> phoneNumbers = new ArrayList<>();
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 0;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.emergency_title)
                .setMessage(R.string.emergency_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        firestore.collection("users").document(currentUser.getUid()).collection("contactLog")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, Object> contactItem = document.getData();
                                                String phoneNumber = contactItem.get("phoneNumber").toString();
                                                phoneNumbers.add(phoneNumber);
                                            }
                                        }
                                    }
                                });

                        for (int i = 0; i < phoneNumbers.size(); i++) {
                            sendMessage("tel:" + phoneNumbers.get(i));
                        }

                        makeCall("tel://911");
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    public void makeCall(String phoneNumber) {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phoneNumber));
            startActivity(callIntent);
        }
    }

    public void sendMessage(String phoneNumber) {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        } else {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            sendIntent.putExtra("sms_body", "I am currently experiencing an emergency and have notified emergency services. This message was sent by the Mentally app");
            Intent shareMessage = Intent.createChooser(sendIntent, null);
            startActivity(shareMessage);
            Toast.makeText(requireActivity().getApplicationContext(), "SMS sent successfully", Toast.LENGTH_LONG).show();
        }
    }
}
