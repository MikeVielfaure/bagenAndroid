package fr.legrand.application.bagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class IdentificationActivity extends AppCompatActivity {
  private Button btnValider;
  private Button btnRegister;
  private EditText txtMail;
  private EditText txtMDP;
  private CheckBox cbSession;
  private AccesDistant accesDistant;
  public String MDP;
  public String mail;
  private Utilisateur utilisateur;

  // propriétés pour l'état de l'authentification
  private static final int STATE_VALIDE = 1;
  private static final int STATE_NON_VALIDE = 2;
  private static final int STATE_ERREUR = 3;
  private static final int NO_USER = 4;
  private static final int OK_USER = 5;
  private static final int NO_MDP = 6;

  // propriétés qui va agir en fonction de l'état de la connexion reçu depuis l'AccesDistant
  public Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
      switch(msg.what){
        case STATE_VALIDE:
          Toast.makeText(getBaseContext(), "Connecté", Toast.LENGTH_SHORT).show();
          utilisateur = new Utilisateur(null,null,null,null,null,null,null,null);
          utilisateur = accesDistant.getUtilisateur();
          session();
          goAccueil();
          //retourActivityPrincipale();
          break;
        case STATE_NON_VALIDE:
          //Toast.makeText(getBaseContext(), "Identifiant non trouvé", Toast.LENGTH_SHORT).show();
          ((TextView)findViewById(R.id.connectionEnCours)).setText("identifiant non trouvé");
          break;
        case STATE_ERREUR:
          //Toast.makeText(getBaseContext(), "erreur de connexion", Toast.LENGTH_SHORT).show();
          ((TextView)findViewById(R.id.connectionEnCours)).setText("erreur de connexion, vérifier connexion internet");
          break;
        case NO_USER:
          //Toast.makeText(getBaseContext(), "erreur de connexion", Toast.LENGTH_SHORT).show();
          ((TextView)findViewById(R.id.connectionEnCours)).setText("Aucun utilisateur avec ce mail");
          break;
        case OK_USER:
          Log.d("connection", "****************" + accesDistant.getHashmdp());
          BCrypt.Result result = BCrypt.verifyer().verify(MDP.toCharArray(), accesDistant.getHashmdp());
          if(result.verified) {
            connection(mail, accesDistant.getHashmdp());
          }else{
            Message messages = new Message();
            messages.what = NO_MDP;
            handler.sendMessage(messages);
          }
          break;
        case NO_MDP:
          ((TextView)findViewById(R.id.connectionEnCours)).setText("mot de passe incorrect");
          break;
      }
      return true;
    }
  });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);

        init();
    }

    private void init(){
      btnValider = (Button)findViewById(R.id.btnValider);
      btnRegister = (Button)findViewById(R.id.btnRegister);
      txtMail = (EditText)findViewById(R.id.txtMail);
      txtMDP = (EditText)findViewById(R.id.txtMDP);
      cbSession = (CheckBox)findViewById(R.id.cbSession);
      clickValider(btnValider);
      clickRegister(btnRegister);
    }

    private void clickValider(Button btnValider){
      btnValider.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          MDP = txtMDP.getText().toString();
          mail = txtMail.getText().toString();
          verifChamps();
        }
      });
    }

    private void clickRegister(Button btnRegister){
      btnRegister.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          goRegister();
        }
      });
    }

    private void verifMDP(String mail){
      List list = new ArrayList();
      list.add(mail);
      JSONArray jsonList = new JSONArray(list);
      accesDistant = new AccesDistant(handler);
      // envoi au serveur distant les information necessaire pour l'authentification
      accesDistant.envoi("verifmdp",jsonList);
      // affichage du chargement
      ((TextView)findViewById(R.id.connectionEnCours)).setText("Connection en cours...");
    }

    private void connection(String mail, String hashmdp){
      List list = new ArrayList();
      list.add(mail);
      list.add(hashmdp);
      JSONArray jsonList = new JSONArray(list);
      accesDistant = new AccesDistant(handler);
      // envoi au serveur distant les information necessaire pour l'authentification
      accesDistant.envoi("authentification",jsonList);
      // affichage du chargement
      ((TextView)findViewById(R.id.connectionEnCours)).setText("Connection en cours...");
    }

    private void verifChamps(){
      if(txtMDP.getText().toString().equals("") || txtMail.getText().toString().equals("") || (txtMDP.getText().toString().equals("") && txtMail.getText().toString().equals("") ) ){
        Toast.makeText(getBaseContext(), "Tous les champs sont obligatoire", Toast.LENGTH_SHORT).show();
        if(txtMail.getText().toString().equals("")){
          txtMail.requestFocus();
        }else{
          txtMDP.requestFocus();
        }
      }else{
        verifMDP(mail);
      }
    }

    private void session(){
      if (cbSession.isChecked()){
        utilisateur.setSession(true);
      }else{
        utilisateur.setSession(true);
      }
      Serializer.serialize("utilisateur",utilisateur,getBaseContext());

    }

  /**
   * Retour à l'activité principale (le menu)
   */
  private void goAccueil() {
    Intent intent = new Intent(IdentificationActivity.this, AccueilActivity.class) ;
    startActivity(intent) ;
    this.finish();
  }

  private void goRegister(){
    Intent intent = new Intent(IdentificationActivity.this, registerActivity.class) ;
    startActivity(intent) ;
    this.finish();
  }


}
