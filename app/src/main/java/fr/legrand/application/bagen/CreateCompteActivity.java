package fr.legrand.application.bagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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

public class CreateCompteActivity extends AppCompatActivity {
private Button btnCreate;
private EditText txtNom;
private EditText txtBudget;
private AccesDistantCreerCompte accesDistantCreerCompte;
private Utilisateur utilisateur = new Utilisateur(null,null,null,null,null,null,null,null);

// propriétés pour l'état de l'authentification
private static final int STATE_VALIDE = 1;
private static final int STATE_NON_VALIDE = 2;
private static final int STATE_ERREUR = 3;

  // propriétés qui va agir en fonction de l'état de la connexion reçu depuis l'AccesDistant
  public Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
      switch(msg.what){
        case STATE_VALIDE:

          break;
        case STATE_NON_VALIDE:
          Toast.makeText(getBaseContext(), "Vous avez déja un compte avec ce nom", Toast.LENGTH_LONG).show();
          break;
        case STATE_ERREUR:

          break;


      }
      return true;
    }
  });


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_compte);

    init();

  }

  private void init(){
    utilisateur = (Utilisateur)Serializer.deSerialize("utilisateur",getBaseContext());

    btnCreate = (Button)findViewById(R.id.btnCreate);
    btnCreate.setEnabled(false);
    txtBudget = (EditText)findViewById(R.id.txtBudget);
    txtNom = (EditText)findViewById(R.id.txtNom);

    verfiChamp(this.txtNom);
    clickValider(btnCreate);
  }

  private void verfiChamp(final EditText txtNom){
    txtNom.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if(txtNom.getText().toString() != ""){
          btnCreate.setEnabled(true);
        }else{
          btnCreate.setEnabled(false);
        }
      }
    });
  }

  private void clickValider(Button btnCreate){
    btnCreate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Toast.makeText(getBaseContext(), "verifnom", Toast.LENGTH_SHORT).show();
        verifNom();
      }
    });
  }

  private void verifNom(){
    //Log.d("click", "************ " + txtNom.getText().toString()+" "+utilisateur.getId().toString());
    List list = new ArrayList();
    list.add(txtNom.getText().toString());
    list.add(utilisateur.getId().toString());
    if(txtBudget.getText().toString() != ""){
      list.add(txtBudget.getText().toString() );
    }
    JSONArray jsonList = new JSONArray(list);
    accesDistantCreerCompte = new AccesDistantCreerCompte(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantCreerCompte.envoi("verifnom",jsonList);
  }


}
