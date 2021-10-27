package fr.legrand.application.bagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MesComptesActivity extends AppCompatActivity {

  private TextView txtMesComptes;
  private AccesLocale accesLocale;
  private Utilisateur utilisateur = new Utilisateur(null,null,null,null,null,null,null,null);
  private ArrayList<Compte> mesComptes;
  private ArrayList<Depenses> mesDepenses;
  private Budget monBudget;

  private AccesDistantRecupAll accesDistantRecupAll;

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
          iniListViaAcces();

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
        setContentView(R.layout.activity_mes_comptes);

        init();
        //initList();
        recupAll();
    }

    private void init(){
      utilisateur = (Utilisateur)Serializer.deSerialize("utilisateur",getBaseContext());
      txtMesComptes = (TextView)findViewById(R.id.txtMesComptes);
      mesComptes = new ArrayList<Compte>();
      mesDepenses = new ArrayList<Depenses>();
      monBudget = new Budget(null, null, null, null);

    }

    private void recupComptesSQLITE(String idutilisateur){
      mesComptes = new ArrayList<Compte>();
      accesLocale = new AccesLocale(getBaseContext());
      mesComptes = accesLocale.recupCompte(idutilisateur);
      Log.d("mesComptes :","**************** "+mesComptes.get(0).getIntitule()+" "+mesComptes.get(1).getIntitule());

    }

    private void recupDepensesSQLITE(String idcompte){

      accesLocale = new AccesLocale(getBaseContext());
      mesDepenses = accesLocale.recupDepense(idcompte);
      //Log.d("mesDepenses :","**************** "+mesDepenses.get(0).getLibelle()+" "+mesDepenses.get(1).getLibelle());

    }

    private void recupBudgetSQLITE(String idcompte){
      monBudget = new Budget(null, null, null, null);
      accesLocale = new AccesLocale(getBaseContext());
      monBudget = accesLocale.recupBudget(idcompte);
      Log.d("monBudget :","**************** "+monBudget.getMontant());

    }

    private void recupAllSQLITE(){
      recupComptesSQLITE(utilisateur.getId());

      for(Compte compte : mesComptes){
        recupDepensesSQLITE(compte.getId());
        recupBudgetSQLITE(compte.getId());
        compte.setLeBudget(monBudget);
        compte.setLesDepenses(mesDepenses);
      }

      /*
      for(int k = 0; k < mesComptes.size(); k++){
        recupBudgetSQLITE(mesComptes.get(k).getId());
        recupDepensesSQLITE(mesComptes.get(k).getId());
        mesComptes.get(k).setLeBudget(monBudget);
        mesComptes.get(k).setLesDepenses(mesDepenses);
      }*/
    }

    private void recupAll(){
      List list = new ArrayList();
      list.add(utilisateur.getId());
      JSONArray jsonList = new JSONArray(list);
      accesDistantRecupAll = new AccesDistantRecupAll(handler);
      // envoi au serveur distant les information necessaire pour l'authentification
      accesDistantRecupAll.envoi("recupall",jsonList);
    }

    private void initList(){
      recupAllSQLITE();
      ListView listView = (ListView) findViewById(R.id.lstComptes);
      CompteAdapter adapter = new CompteAdapter(MesComptesActivity.this, mesComptes,utilisateur.getId()) ;
      listView.setAdapter(adapter) ;
    }

    private void iniListViaAcces(){
      mesComptes = accesDistantRecupAll.getMesComptes();
      recupBudgetAcces();
      recupDepenseAcces();
      ListView listView = (ListView) findViewById(R.id.lstComptes);
      CompteAdapter adapter = new CompteAdapter(MesComptesActivity.this, mesComptes,utilisateur.getId()) ;
      listView.setAdapter(adapter) ;

    }

    private void recupBudgetAcces(){
      for(int k = 0; k<mesComptes.size(); k++){
        for(int g = 0; g<accesDistantRecupAll.getMesBudgets().size(); g++){
          if(mesComptes.get(k).getId().equals(accesDistantRecupAll.getMesBudgets().get(g).getIdcompte_id())){
            mesComptes.get(k).setLeBudget(accesDistantRecupAll.getMesBudgets().get(g));
          }
        }
      }
    }

    private void recupDepenseAcces(){
      for(int k = 0; k<mesComptes.size(); k++){
        ArrayList<Depenses> lesDepenses = new ArrayList<Depenses>();
        for(int g = 0; g<accesDistantRecupAll.getMesDepenses().size(); g++){
          if(mesComptes.get(k).getId().equals(accesDistantRecupAll.getMesDepenses().get(g).getIdcompte_id())){
            lesDepenses.add(accesDistantRecupAll.getMesDepenses().get(g));
          }
        }
        mesComptes.get(k).setLesDepenses(lesDepenses);
      }
    }
}
