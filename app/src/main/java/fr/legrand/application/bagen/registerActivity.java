package fr.legrand.application.bagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class registerActivity extends AppCompatActivity {
private Button btnValiderRegister ;
private EditText txtMailRegister ;
private EditText txtMDPRegister ;
private EditText txtMDPConfirm ;
private AccesDistantRegister accesDistantRegister;


  // propriétés pour l'état de l'authentification
  private static final int STATE_VALIDE = 1;
  private static final int STATE_ERREUR = 2;
  private static final int STATE_MAIL_USED = 3;

  // propriétés qui va agir en fonction de l'état de la connexion reçu depuis l'AccesDistant
  public Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
      switch(msg.what){
        case STATE_VALIDE:
        Toast.makeText(getBaseContext(),"validé ! ",Toast.LENGTH_SHORT).show();
        goIdentification();
          break;
        case STATE_ERREUR:
          Toast.makeText(getBaseContext(),"erreur ! ",Toast.LENGTH_SHORT).show();
          break;
        case STATE_MAIL_USED:
          Toast.makeText(getBaseContext(),"mail déja utilisé ",Toast.LENGTH_SHORT).show();
          break;

      }
      return true;
    }
  });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    init();
  }

  private void init(){
    btnValiderRegister = (Button)findViewById(R.id.btnValiderRegister);
    txtMailRegister = (EditText)findViewById(R.id.txtMailRegister);
    txtMDPRegister = (EditText)findViewById(R.id.txtMDPRegister);
    txtMDPConfirm = (EditText)findViewById(R.id.txtMDPConfirm);
    clickValider(btnValiderRegister);
  }

  private void clickValider(Button btnValiderRegister){
    btnValiderRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!verifMail()){
          Toast.makeText(getBaseContext(),"mail non valide !",Toast.LENGTH_SHORT).show();
        }else{
          if(!verifMDP()){
            Toast.makeText(getBaseContext(),"mot de passe non identique",Toast.LENGTH_SHORT).show();
          }else{
            registerUser();
          }
        }
      }
    });
  }

  private boolean verifMail(){
    return txtMailRegister.getText().toString().contains("@");
  }

  private boolean verifMDP(){
    return (txtMDPConfirm.getText().toString().equals(txtMDPRegister.getText().toString())
      && (!txtMDPConfirm.getText().toString().equals("") && !txtMDPRegister.getText().toString().equals("")));
  }

  private void registerUser(){
    List list = new ArrayList();
    list.add(txtMailRegister.getText().toString());
    String hashmdp = BCrypt.withDefaults().hashToString(12,txtMDPRegister.getText().toString().toCharArray());
    list.add(hashmdp);
    JSONArray jsonList = new JSONArray(list);
    accesDistantRegister = new AccesDistantRegister(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantRegister.envoi("register",jsonList);
  }

  private void goIdentification() {
    Intent intent = new Intent(registerActivity.this, IdentificationActivity.class) ;
    startActivity(intent) ;
    this.finish();
  }
}
