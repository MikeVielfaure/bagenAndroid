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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class AccueilActivity extends AppCompatActivity {

  private Utilisateur utilisateur = new Utilisateur(null,null,null,null,null,null,null,null);
  private AccesDistantRecupAll accesDistantRecupAll;
  private ArrayList<Compte> mesComptes;
  private ArrayList<Budget> mesBudgets;
  private ArrayList<Depenses> mesDepenses;
  private AccesLocale accesLocale;
  private String enregistrement;
  private Button btnCreerCompte;
  private Button btnMesComptes;

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
          Log.d("etat :","********** comptes récupéré");
          try{
            enregistrement = Serializer.deSerialize("enregistrement",getBaseContext()).toString();
            Log.d("etat :","************ try");
          }catch(Exception e){
            enregistrerAll();
            Serializer.serialize("enregistrement","ok",getBaseContext());
            Log.d("etat :","************ serialize enregistrement");
          }
          break;
        case STATE_NON_VALIDE:

          break;
        case STATE_ERREUR:
          Log.d("etat :","********** impossible de récupérer vos comptes");
          break;
      }
      return true;
    }
  });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        init();


    }

    private void clickBtnCreerCompte(){
      btnCreerCompte.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          changeActivity(CreateCompteActivity.class);
        }
      });
    }

    private void clickBtnMesComptes(){
      btnMesComptes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          changeActivity(MesComptesActivity.class);
        }
      });
    }

    private void init(){
      btnCreerCompte = (Button)findViewById(R.id.btnCreerCompte);
      btnMesComptes = (Button)findViewById(R.id.btnMesComptes);
      clickBtnCreerCompte();
      clickBtnMesComptes();
      utilisateur = (Utilisateur)Serializer.deSerialize("utilisateur",getBaseContext());
      try{
        enregistrement = Serializer.deSerialize("enregistrement",getBaseContext()).toString();
      }catch(Exception e) {
        recupAll();
      }
    }

    private void recupAll(){
      List list = new ArrayList();
      list.add(utilisateur.getId());
      JSONArray jsonList = new JSONArray(list);
      accesDistantRecupAll = new AccesDistantRecupAll(handler);
      // envoi au serveur distant les information necessaire pour l'authentification
      accesDistantRecupAll.envoi("recupall",jsonList);
    }

    private  void enregistrerCompte(){
      mesComptes = new ArrayList<Compte>();
      mesComptes = accesDistantRecupAll.getMesComptes();
      accesLocale = new AccesLocale(getBaseContext());
      if(mesComptes != null) {
        for (Compte compte : mesComptes) {
          accesLocale.ajoutCompte(compte.getId(),
                                  compte.getIdutilisateur_id(),
                                  compte.getIntitule(),
                                  compte.getDatecreation(),
                                  compte.getDatecloture(),
                                  compte.getDatemodif()
          );
        }
      }

    }

    private void enregistrerBudget(){
      mesBudgets = new ArrayList<Budget>();
      mesBudgets = accesDistantRecupAll.getMesBudgets();
      accesLocale = new AccesLocale(getBaseContext());
      if(mesBudgets != null){
        for(Budget budget : mesBudgets){
          accesLocale.ajoutBudget(budget.getId(),
                                  budget.getIdcompte_id(),
                                  budget.getMontant(),
                                  budget.getDate()
          );
        }
      }
    }

    private void enregistrerDepense(){
      mesDepenses = new ArrayList<Depenses>();
      mesDepenses = accesDistantRecupAll.getMesDepenses();
      accesLocale = new AccesLocale(getBaseContext());
      if(mesDepenses != null){
        for(Depenses depense : mesDepenses){
           accesLocale.ajoutDepense(depense.getId(),
                                    depense.getIdcompte_id(),
                                    depense.getLibelle(),
                                    depense.getMontant(),
                                    depense.getDate(),
                                    depense.getDatemodif()
           );
        }
      }
    }

    private void enregistrerAll(){
      enregistrerBudget();
      enregistrerCompte();
      enregistrerDepense();
      Log.d("etat :","************ enregistrer all");
    }

    private void changeActivity(Class<?> cls){
      Intent intent = new Intent(AccueilActivity.this, cls) ;
      startActivity(intent) ;
      //this.finish();
    }
}
