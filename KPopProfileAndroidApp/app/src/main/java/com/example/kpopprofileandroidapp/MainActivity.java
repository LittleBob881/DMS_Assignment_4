package com.example.kpopprofileandroidapp;

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
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

/*
    This Activity acts as a listener for the bottom navigation, which uses the NavController to navigate between different fragments
 */
@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{
    BottomNavigationView bottomNavigationView;
    String username = "";
    private NfcAdapter nfcAdapter;
    private TextView nfcStatusTextView;
    private TextView nfcResponseView;
    private TextView sendFriendRequestButton;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Bundle args = new Bundle();

        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        args.putString("username", getIntent().getExtras().getString("username"));

//      create bottom navigation object and obtain its controller
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);

        //check if NFC feature available on phone
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcStatusTextView = findViewById(R.id.nfcStatusTextView);
        if(nfcAdapter == null)
        {
            sendFriendRequestButton.setEnabled(false);
//            nfcStatusTextView.setTextColor(Color.RED);
//            nfcStatusTextView.setText("NFC not available for this device!");
        }
        else if(nfcAdapter != null)
        {   sendFriendRequestButton = findViewById(R.id.sendFriendNFCButton);
//            nfcStatusTextView.setTextColor(Color.GREEN);
//            nfcStatusTextView.setText("NFC available for this device!");
            //sendFriendRequestButton.setOnClickListener(this);

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
       // nfcAdapter.setNdefPushMessage(createNdefMessage(), this);
        System.out.println("~~~~~~~~~~~~~ ON RESUME!!!");
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        NdefMessage message = new NdefMessage(new NdefRecord[]
                {NdefRecord.createApplicationRecord("HELLO BRITNEY BITCH")
                });
        System.out.println("!!!!!!!!!!!!!!!!!! NDEF MESSAGE CREATED");

        return message;
    }

    // displaying message
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        NdefMessage[] messages = getNdefMessages(intent);
       // nfcStatusTextView.setText(displayByteArray(messages[0].toByteArray()));
        System.out.println(displayByteArray(messages[0].toByteArray()));
        System.out.println("~~~~~~~~~~~~~ DISPLAY RESUME!!!");
    }

    static String displayByteArray(byte[] bytes) {
        String res="";
        StringBuilder builder = new StringBuilder().append("[");
        for (int i = 0; i < bytes.length; i++) {
            res+=(char)bytes[i];
        }
        return res;
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        System.out.println("~~~~~~~~~~~~~ GET RAW ~~~~~~~~~~~~");
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
            return messages;
        } else {
            return null;
        }
    }
}
