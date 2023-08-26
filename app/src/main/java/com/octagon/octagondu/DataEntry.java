package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DataEntry extends AppCompatActivity {

    private Spinner spinnerBusName;
    private Spinner spinnerBusType;
    private TextView textViewBusId;
    private ImageView imageViewTime;
    private TextView textViewRouteSt;
    private TextView textViewRoute, viewtime;
    String inputTime;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        // Find views
        spinnerBusName = findViewById(R.id.spinnerBusName);
        spinnerBusType = findViewById(R.id.spinnerBusType);
        textViewBusId = findViewById(R.id.textViewBusId);
        imageViewTime = findViewById(R.id.setTime);
        textViewRouteSt = findViewById(R.id.textViewRouteSt);
        textViewRoute = findViewById(R.id.textViewRoute);
        viewtime = findViewById(R.id.viewTime);
        inputTime = "12:00";

        // Set click listener for the Submit button
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected values from the spinners and the input fields
                String busName = spinnerBusName.getSelectedItem().toString();
                String busType = spinnerBusType.getSelectedItem().toString();
                String busId = textViewBusId.getText().toString();
                String time = inputTime;
                String startLocation = textViewRouteSt.getText().toString();
                String destinationLocation = textViewRoute.getText().toString();
                if (!busId.isEmpty() && !time.isEmpty() && !startLocation.isEmpty() && !destinationLocation.isEmpty()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    BusInformation busInformation = new BusInformation(busType, busId, startLocation, destinationLocation);
                    databaseReference.child("Bus Name").child(busName).child(time).setValue(busInformation);
                    Toast.makeText(DataEntry.this, "Successfully Submitted Response", Toast.LENGTH_SHORT).show();
                    clearForm();
                } else {
                    Toast.makeText(DataEntry.this, "Please Fill Completely", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void clearForm () {
        // Clear TextViews
        textViewBusId.setText("");
        textViewRouteSt.setText("");
        textViewRoute.setText("");

        // Reset Spinners to default selection (usually the first item)
        spinnerBusName.setSelection(0);
        spinnerBusType.setSelection(0);
    }
    public void showTimePickerDialog(View view) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update the selected time in the TextView
                        DataEntry.this.inputTime = String.format("%02d:%02d", hourOfDay, minute);
                        viewtime.setText(DataEntry.this.inputTime);
                    }
                },
                hour,
                minute,
                true // Use true if you want 24-hour format, false for 12-hour format
        );

        // Show the dialog
        timePickerDialog.show();
    }
}