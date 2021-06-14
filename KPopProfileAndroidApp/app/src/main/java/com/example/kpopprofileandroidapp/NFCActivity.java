package com.example.kpopprofileandroidapp;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/*
    Uses Android Beam to send friend request messages from one device to another
    (snippets of code to push an ndef messageretrieved from
    https://stackoverflow.com/questions/17587963/reading-an-ndef-message-from-an-nfc-tag-from-an-android-application)
 */
@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NFCActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{
    private String username;
    private NfcAdapter nfcAdapter;
    private TextView nfcStatusTextView, nfcResponseView,  senderTextView, outputTextView;
    private PendingIntent pendingIntent;
    private BandViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_activity);
        username = this.getIntent().getStringExtra("username");

        //make viewmodel to do a GET request to fetch band favourites for this user
        viewModel = ViewModelProviders.of(this).get(BandViewModel.class);
        viewModel.initialiseAllBands();

        //check if NFC feature available on phone
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcStatusTextView = findViewById(R.id.nfcStatusTextView);

        if(nfcAdapter == null)
        {
            nfcStatusTextView.setTextColor(Color.RED);
            nfcStatusTextView.setText("NFC not available for this device!");
        }
        else if(nfcAdapter != null)
        {
            outputTextView = findViewById(R.id.outputContentText);
            nfcStatusTextView.setTextColor(Color.GREEN);
            nfcStatusTextView.setText("NFC available for this device!");
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        nfcAdapter.setNdefPushMessageCallback(this, this);

        System.out.println("~~~~~~~~~~~~~ ON RESUME!!!");
    }

    //when android beam is activated (NFC event), this message is automatically created
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        List<String> favouriteBands = getFavouriteBands();
        StringBuilder builder = new StringBuilder();

        System.out.println("BANDS SIZE "+favouriteBands.size());
        for(String bandName : favouriteBands)
        {
            builder.append(bandName+"-");
        }
        builder.setLength(builder.length()-1);
        System.out.println("RECORD TO SEND -----> "+builder);

        NdefMessage message = new NdefMessage(new NdefRecord[]
                {NdefRecord.createApplicationRecord(username),
                        NdefRecord.createApplicationRecord(builder.toString()),
                });

        System.out.println("NDEF MESSAGE CREATED");
        return message;
    }

    //helper method to get fav bands to add to friend request message
    private List getFavouriteBands()
    {
        viewModel.getFavouriteBands(username);
        List<String> favouriteBands = viewModel.favouriteBands;

        return favouriteBands;
    }

    //display friend request message to popup view
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        NdefMessage friendRequestMessage = getNdefMessages(intent);
        NdefRecord[] records = friendRequestMessage.getRecords();

        //(first record is username, second record is list of bands)
        System.out.println(displayByteArray(records[0].getPayload()));

        String str = new String(records[1].getPayload());

        String[] bands = str.split("-");
        List<String> senderFaveBands = new ArrayList<>();
        for(int i = 0; i < bands.length; i++)
        {
            System.out.println("SENDER'S BANDS -> "+i+". "+bands[i]);
            senderFaveBands.add(bands[i]);
        }

        showFriendRequestPopup(new String(records[0].getPayload()), senderFaveBands);
    }

    static String displayByteArray(byte[] bytes) {
        String res="";
        StringBuilder builder = new StringBuilder().append("[");
        for (int i = 0; i < bytes.length; i++) {
            res+=(char)bytes[i];
        }
        return res;
    }

    //show popup for friend request component
    public void showFriendRequestPopup(String sender, List<String> senderBands)
    {
        //create view model to add friends
        FriendViewModel friendModel = new FriendViewModel();

        //show friend request popup
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View friendPopup = getLayoutInflater().inflate(R.layout.friend_popup, null);
        dialogBuilder.setView(friendPopup);

        //add to output text
        outputTextView.append("\nFriend Request received from "+sender);

        //get favourite bands for user incase it wasnt called initially, used to compare with sender's bands
        viewModel.getFavouriteBands(username);

        String friendRequestMessage = "New Friend request from: ";
        Spannable senderUsername = new SpannableString(sender+"\n");
        senderUsername.setSpan(new ForegroundColorSpan(Color.BLUE), 0, senderUsername.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        StringBuilder bandsInCommon = new StringBuilder();
        StringBuilder otherBands = new StringBuilder();
        otherBands.append("\n\n"+sender+"'s Other Favourite Bands:\n")
        for(String bandName : senderBands)
        {
            if(viewModel.favouriteBands.contains(bandName))
            {
                bandsInCommon.append("\nYou both like "+bandName+"!");
            }
            else
                otherBands.append("\n"+bandName);
        }

        //add listener for "Accept friend request" button
        Button acceptButton = findViewById(R.id.addFriendNFCButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Spannable commonBands = new SpannableString(bandsInCommon.toString());
        commonBands.setSpan(new ForegroundColorSpan(Color.GREEN), 0, commonBands.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        senderTextView =  friendPopup.findViewById(R.id.friendRequestTextView);
        senderTextView.setText(friendRequestMessage);
        senderTextView.append(senderUsername);
        senderTextView.append(commonBands);
        senderTextView.append(otherBands);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    //get friend request message from sender (via android beam)
    private NdefMessage getNdefMessages(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            NdefMessage message = (NdefMessage)rawMessages[0];

            return message;
        } else {
            return null;
        }
    }
}
