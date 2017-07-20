package digitalquantuminc.inscribesecuresms.ChildrenActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import digitalquantuminc.inscribesecuresms.DataType.TypeContact;
import digitalquantuminc.inscribesecuresms.Intent.IntentString;
import digitalquantuminc.inscribesecuresms.R;
import digitalquantuminc.inscribesecuresms.Repository.contactRepository;
import digitalquantuminc.inscribesecuresms.Repository.sessionRepository;
import digitalquantuminc.inscribesecuresms.DataType.TypeSession;
import digitalquantuminc.inscribesecuresms.UserInterface.UserInterfaceColor;

public class ActivitySessionDetail extends AppCompatActivity {

    // UI Components
    TextView text_PartnerName;
    TextView text_PartnerNumber;
    TextView text_PartnerRole;
    TextView text_SecureSessionStatus;
    TextView text_SecureSessionFreshness;
    TextView text_LastSessionHandshake;
    TextView text_SessionHandshakeAge;
    TextView text_SessionNumMessage;
    TextView text_PartnerValidity;
    EditText text_PartnerRSAPubKey;
    EditText text_ECDHSessionPrivateKey;
    EditText text_ECDHSessionPublicKey;
    EditText text_PartnerECDHSessionPublicKey;
    EditText text_PartnerDigitalSignature;
    EditText text_PartnerComputedDigitalSignature;
    EditText text_SharedSecret;
    EditText text_SessionAESKey;
    Button btn_InitiateSession;
    Button btn_EndSession;

    // Intent Variable
    private String ContactPhoneNumber = "";
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Binding UI Component to Code
        UIComponentBinding();

        // Get Intent
        IntentProcessor();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            IntentFeedback(RESULT_OK, IntentString.MainFeedbackCode_DoNothing);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void UIComponentBinding() {
        text_PartnerName = (TextView) findViewById(R.id.text_PartnerName);
        text_PartnerNumber = (TextView) findViewById(R.id.text_PartnerNumber);
        text_PartnerRole = (TextView) findViewById(R.id.text_PartnerRole);
        text_SecureSessionStatus = (TextView) findViewById(R.id.text_SecureSessionStatus);
        text_SecureSessionFreshness = (TextView) findViewById(R.id.text_SecureSessionFreshness);
        text_LastSessionHandshake = (TextView) findViewById(R.id.text_LastSessionHandshake);
        text_SessionHandshakeAge = (TextView) findViewById(R.id.text_SessionHandshakeAge);
        text_SessionNumMessage = (TextView) findViewById(R.id.text_SessionNumMessage);
        text_PartnerValidity = (TextView) findViewById(R.id.text_PartnerValidity);
        text_PartnerRSAPubKey = (EditText) findViewById(R.id.text_PartnerRSAPubKey);
        text_ECDHSessionPrivateKey = (EditText) findViewById(R.id.text_ECDHSessionPrivateKey);
        text_ECDHSessionPublicKey = (EditText) findViewById(R.id.text_ECDHSessionPublicKey);
        text_PartnerECDHSessionPublicKey = (EditText) findViewById(R.id.text_PartnerECDHSessionPublicKey);
        text_PartnerDigitalSignature = (EditText) findViewById(R.id.text_PartnerDigitalSignature);
        text_PartnerComputedDigitalSignature = (EditText) findViewById(R.id.text_PartnerComputedDigitalSignature);
        text_SharedSecret = (EditText) findViewById(R.id.text_SharedSecret);
        text_SessionAESKey = (EditText) findViewById(R.id.text_SessionAESKey);
        btn_InitiateSession = (Button) findViewById(R.id.btn_InitiateSession);
        btn_InitiateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_InitiateSession_onClick(v);
            }
        });
        btn_EndSession = (Button) findViewById(R.id.btn_EndSession);
        btn_EndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_EndSession_onClick(v);
            }
        });
    }

    protected void IntentProcessor() {
        // Get Intent from Main Activity
        Intent intent = getIntent();
        ContactPhoneNumber = intent.getStringExtra(IntentString.MainToSessionDetails_PhoneNum);
        color = intent.getIntExtra(IntentString.MainToSessionDetails_ColorTheme, 0);

        // Preparing Repository
        sessionRepository repo = new sessionRepository(this);
        TypeSession session = repo.getSession(ContactPhoneNumber);

        contactRepository repo2 = new contactRepository(this);
        TypeContact contact = repo2.getContact(ContactPhoneNumber);

        // Set Appearance based on Session
        setTitle("Secure Session with " + session.getName());
        UserInterfaceColor.setStatusBarColor(color, this);
        UserInterfaceColor.setTitleBackgroundColor(color, this);

        text_PartnerName.setText(session.getName());
        text_PartnerNumber.setText(session.getPhone_number());

        // Compute Partner Role
        if (session.getSession_role() == TypeSession.StatusRoleMaster) {
            text_PartnerRole.setText(R.string.session_role_master);
        } else if (session.getSession_role() == TypeSession.StatusRoleSlave) {
            text_PartnerRole.setText(R.string.session_role_slave);
        } else {
            text_PartnerRole.setText(R.string.session_unknown);
        }

        // Compute Secure Session Status
        if (session.getSession_validity() == TypeSession.StatusValid) {
            text_SecureSessionStatus.setText(R.string.session_status_valid);
            text_SecureSessionStatus.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGreen));
        } else if (session.getSession_validity() == TypeSession.StatusNotValid) {
            text_SecureSessionStatus.setText(R.string.session_status_notvalid);
            text_SecureSessionStatus.setTextColor(ContextCompat.getColor(this, R.color.colorDarkRed));
        } else {
            text_SecureSessionStatus.setText(R.string.session_unknown);
            text_SecureSessionStatus.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
        }

        // Compute Secure Session Freshness
        if (session.computeSessionFreshness() == TypeSession.StatusFresh) {
            text_SecureSessionFreshness.setText(R.string.session_status_fresh);
            text_SecureSessionFreshness.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGreen));
        } else if (session.computeSessionFreshness() == TypeSession.StatusStale) {
            text_SecureSessionFreshness.setText(R.string.session_status_stale);
            text_SecureSessionFreshness.setTextColor(ContextCompat.getColor(this, R.color.colorAmber));
        } else if (session.computeSessionFreshness() == TypeSession.StatusDecomposed) {
            text_SecureSessionFreshness.setText(R.string.session_status_decomposed);
            text_SecureSessionFreshness.setTextColor(ContextCompat.getColor(this, R.color.colorDarkRed));
        } else {
            text_SecureSessionFreshness.setText(R.string.session_unknown);
            text_SecureSessionFreshness.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
        }


        text_LastSessionHandshake.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date(session.getSession_handshake_date())));
        text_SessionHandshakeAge.setText(String.format(this.getString(R.string.session_age), session.getSessionElapsedHour(), session.getSessionElapsedMin()));
        text_SessionNumMessage.setText(String.valueOf(session.getSession_num_message()));

        // Compute Partner Validity
        if (session.getSession_ecdh_partner_validity() == TypeSession.StatusDSValid) {
            text_PartnerValidity.setText(R.string.session_ds_valid);
            text_PartnerValidity.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGreen));
        } else if (session.getSession_ecdh_partner_validity() == TypeSession.StatusDSNotValid) {
            text_PartnerValidity.setText(R.string.session_ds_notvalid);
            text_PartnerValidity.setTextColor(ContextCompat.getColor(this, R.color.colorDarkRed));
        } else {
            text_PartnerValidity.setText(R.string.session_unknown);
            text_PartnerValidity.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
        }

        text_PartnerRSAPubKey.setText(contact.getRsa_publickey());
        text_PartnerRSAPubKey.setTextIsSelectable(true);
        text_PartnerRSAPubKey.setKeyListener(null);

        text_ECDHSessionPrivateKey.setText(session.getSession_ecdh_private_key());
        text_ECDHSessionPrivateKey.setTextIsSelectable(true);
        text_ECDHSessionPrivateKey.setKeyListener(null);

        text_ECDHSessionPublicKey.setText(session.getSession_ecdh_public_key());
        text_ECDHSessionPublicKey.setTextIsSelectable(true);
        text_ECDHSessionPublicKey.setKeyListener(null);

        text_PartnerECDHSessionPublicKey.setText(session.getSession_ecdh_partner_public_key());
        text_PartnerECDHSessionPublicKey.setTextIsSelectable(true);
        text_PartnerECDHSessionPublicKey.setKeyListener(null);

        text_PartnerDigitalSignature.setText(session.getSession_ecdh_partner_digital_signature());
        text_PartnerDigitalSignature.setTextIsSelectable(true);
        text_PartnerDigitalSignature.setKeyListener(null);

        text_PartnerComputedDigitalSignature.setText(session.getSession_ecdh_partner_computed_digital_signature());
        text_PartnerComputedDigitalSignature.setTextIsSelectable(true);
        text_PartnerComputedDigitalSignature.setKeyListener(null);

        text_SharedSecret.setText(session.getSession_ecdh_shared_secret());
        text_SharedSecret.setTextIsSelectable(true);
        text_SharedSecret.setKeyListener(null);

        text_SessionAESKey.setText(session.getSession_ecdh_aes_key());
        text_SessionAESKey.setTextIsSelectable(true);
        text_SessionAESKey.setKeyListener(null);

        // Button State
        if (session.getSession_validity() == TypeSession.StatusValid) {
            btn_InitiateSession.setEnabled(false);
            btn_EndSession.setEnabled(true);
        } else if (session.getSession_validity() == TypeSession.StatusNotValid) {
            btn_InitiateSession.setEnabled(true);
            btn_EndSession.setEnabled(false);
        } else {
            btn_InitiateSession.setEnabled(false);
            btn_EndSession.setEnabled(false);
        }
    }

    protected void IntentFeedback(int status, int FeedbackType) {
        Intent intent = getIntent();
        intent.putExtra(IntentString.MainFeedBackCode, FeedbackType);
        setResult(status, intent);
        finish();
    }

    private void btn_InitiateSession_onClick(View v) {

    }


    private void btn_EndSession_onClick(View v) {

    }
}
