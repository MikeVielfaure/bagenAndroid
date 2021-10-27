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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class compteDetail extends AppCompatActivity {

  private TextView txtIntituleCompte;
  private TextView txtMontantBudget;
  private TextView txtMontantDepenses;
  private TextView txtMontantTotalDepenses;
  private Button btnModifierBudget;
  private Button btnAjouterDepense;
  private Button btnSupprCompte;
  private String idCompte;
  private ListView lstDepenses;
  private EditText etxtMontantBudget;
  private Button btnValiderBudget;
  private LinearLayout llBudget;
  private LinearLayout llBudgetModifie;
  private compteDetail activity;
  private AjouterDepenseDialog ajouterDepenseDialog ;
  private ConfirmSupprimerDialog confirmSupprimerDialog;
  private Activity mActivity = this;

  private Compte monCompte;
  private ArrayList<Depenses> mesDepenses;
  private Budget monBudget;
  private AccesLocale accesLocale;

  private Utilisateur utilisateur = new Utilisateur(null,null,null,null,null,null,null,null);
  private  AccesDistantCompteDetail accesDistantCompteDetail;


  private static final int STATE_VALIDE = 1;
  private static final int STATE_NON_VALIDE = 2;
  private static final int STATE_ERREUR = 3;
  private static final int VALIDE_NEW_BUDGET = 4;
  private static final int VALIDE_AJOUT_DEPENSE = 5;
  private static final int VALIDE_MODIF_DEPENSE = 6;
  private static final int VALIDE_SUPPRIMER_DEPENSE = 7;
  private static final int VALIDE_SUPPRIMER_COMPTE = 8;

  // propriétés qui va agir en fonction de l'état de la connexion reçu depuis l'AccesDistant
  public Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(@NonNull Message msg) {
      switch(msg.what){
        case STATE_VALIDE:
          Log.d("etat :","********** compte récupéré");
          valoriseCompte();
          initDonneesAcces();

          break;
        case STATE_NON_VALIDE:

          break;
        case STATE_ERREUR:
          //Log.d("etat :","********** impossible de récupérer vos comptes");
          break;
        case VALIDE_NEW_BUDGET:
          init();
          visibleLayout(llBudget);
          invisibleLayout(llBudgetModifie);
          break;
        case VALIDE_AJOUT_DEPENSE:
          Log.d("ajout dépense :","********** ok");
          ajouterDepenseDialog.dismiss();
          init();
          break;
        case VALIDE_MODIF_DEPENSE:
          Log.d("modif dépense :","********** ok");
          init();
          break;
        case VALIDE_SUPPRIMER_DEPENSE:
          Log.d("supr dépense :","********** ok");
          init();
          break;
        case VALIDE_SUPPRIMER_COMPTE:
          Log.d("supr COMPTE :","********** ok");
          changeActivity(MesComptesActivity.class);
          break;
      }
      return true;
    }
  });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compte_detail);


    init();

  }

  private void init(){
    utilisateur = (Utilisateur)Serializer.deSerialize("utilisateur",getBaseContext());
    llBudget = (LinearLayout)findViewById(R.id.llBudget);
    llBudgetModifie = (LinearLayout)findViewById(R.id.llBudgetModifie);
    btnValiderBudget = (Button)findViewById(R.id.btnValiderBudget);
    etxtMontantBudget = (EditText)findViewById(R.id.etxtMontantBudget);
    txtIntituleCompte = (TextView)findViewById(R.id.txtIntituleCompte);
    txtMontantBudget = (TextView)findViewById(R.id.txtMontantBudget);
    txtMontantDepenses = (TextView)findViewById(R.id.txtMontantDepenses);
    txtMontantTotalDepenses = (TextView)findViewById(R.id.txtMontantTotalDepenses);
    btnAjouterDepense = (Button)findViewById(R.id.btnAjouterDepenses);
    btnModifierBudget = (Button)findViewById(R.id.btnModifierBudget);
    btnSupprCompte = (Button)findViewById(R.id.btnSupprCompte);
    lstDepenses = (ListView)findViewById(R.id.lstDepenses);
    idCompte = Serializer.deSerialize("numCompte",getBaseContext()).toString();
    clickSupprimer(btnSupprCompte);
    //recupCompteSQLITE(idCompte);
    //recupBudgetSQLITE(idCompte);
    //recupDepensesSQLITE(idCompte);
    //monCompte.setLesDepenses(mesDepenses);
    //monCompte.setLeBudget(monBudget);
    recupCompteAccesDistant();

    //etxtMontantBudget.setVisibility(View.VISIBLE);

  }

  private void supprimerCompteAcces(){
    List list = new ArrayList();
    list.add(this.idCompte);
    JSONArray jsonList = new JSONArray(list);
    accesDistantCompteDetail = new AccesDistantCompteDetail(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantCompteDetail.envoi("supprimercompte",jsonList);
  }

  private void clickSupprimer(Button btnSupprCompte){
    btnSupprCompte.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        confirmSupprimerDialog = new ConfirmSupprimerDialog(mActivity);
        Button btnSupprValider = confirmSupprimerDialog.getBtnValider();
        Button btnSupprAnnuler = confirmSupprimerDialog.getBtnAnnuler();

        btnSupprAnnuler.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            confirmSupprimerDialog.dismiss();
          }
        });

        btnSupprValider.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            supprimerCompteAcces();
            confirmSupprimerDialog.dismiss();
          }
        });
        confirmSupprimerDialog.build();

      }
    });
  }

  private void initDonneesAcces(){
    txtIntituleCompte.setText(monCompte.getIntitule());
    txtMontantTotalDepenses.setText(String.valueOf(Float.parseFloat(monCompte.getLeBudget().getMontant())-Float.parseFloat(monCompte.getTotalDepense()))+" €");
    txtMontantDepenses.setText(monCompte.getTotalDepense()+" €");
    txtMontantBudget.setText(monCompte.getLeBudget().getMontant()+" €");

    clickModifieBudget();
    clickValiderBudget();
    clickAjouterDepense();

    DepenseAdapter adapter = new DepenseAdapter(compteDetail.this, mesDepenses, idCompte, this, this.handler) ;
    lstDepenses.setAdapter(adapter) ;
  }


  private void recupCompteSQLITE(String idcompte){
    monCompte = new Compte(null,null,null,null,null,null);
    accesLocale = new AccesLocale(getBaseContext());
    monCompte = accesLocale.recupCompteId(idcompte);
    Log.d("mesComptes :","**************** "+monCompte.getIntitule());

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

  private void clickModifieBudget(){
    btnModifierBudget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        invisibleLayout(llBudget);
        visibleLayout(llBudgetModifie);
        etxtMontantBudget.setText(monCompte.getLeBudget().getMontant());
        etxtMontantBudget.requestFocus();
      }
    });
  }

  private void clickValiderBudget(){
    btnValiderBudget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //validerBudgetSQLITE();
        validerBugetAcces();
      }
    });
  }

  private void validerBudgetSQLITE(){
    accesLocale = new AccesLocale(getBaseContext());
    accesLocale.updateBudget(etxtMontantBudget.getText().toString(), monBudget.getId());
    visibleLayout(llBudget);
    invisibleLayout(llBudgetModifie);
    init();
  }

  private void validerBugetAcces(){
    List list = new ArrayList();
    list.add(this.idCompte);
    list.add(etxtMontantBudget.getText().toString());
    JSONArray jsonList = new JSONArray(list);
    accesDistantCompteDetail = new AccesDistantCompteDetail(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantCompteDetail.envoi("modifbudget",jsonList);
  }

  private void visibleLayout(LinearLayout linearLayout){
    linearLayout.setVisibility(View.VISIBLE);
    ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
    //Changes the height and width to the specified *pixels*
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
    linearLayout.setLayoutParams(params);
  }

  private void invisibleLayout(LinearLayout linearLayout){
    linearLayout.setVisibility(View.INVISIBLE);
    ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
    //Changes the height and width to the specified *pixels*
    params.height = 0;
    params.width = 0;
    linearLayout.setLayoutParams(params);
  }

  private void initAjouterDepenseDialog(){

    activity = this;
    ajouterDepenseDialog = new AjouterDepenseDialog(activity);
    /*
    final EditText lTitre = ajouterDepenseDialog.getTitre();
    final EditText lMontant = ajouterDepenseDialog.getMontant();
    final DatePicker lDate = ajouterDepenseDialog.getDate();

     */

    Button lBtnValider = ajouterDepenseDialog.getBtnValider();
    Button lBtnAnnuler = ajouterDepenseDialog.getBtnAnnuler();

    lBtnAnnuler.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ajouterDepenseDialog.dismiss();
      }
    });

    lBtnValider.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String leMontant = ajouterDepenseDialog.getMontant().getText().toString();
        String leTitre = ajouterDepenseDialog.getTitre().getText().toString();
        String jour = String.valueOf(ajouterDepenseDialog.getDate().getDayOfMonth());
        String mois = String.valueOf(ajouterDepenseDialog.getDate().getMonth());
        String annee = String.valueOf(ajouterDepenseDialog.getDate().getYear());
        String laDate = annee+"-"+mois+"-"+jour;

        //Log.d("ttttttt","********** "+leMontant+" "+leTitre+" "+" "+laDate+" "+idCompte);

        List list = new ArrayList();
        list.add(idCompte);
        list.add(leMontant);
        list.add(leTitre);
        list.add(laDate);
        JSONArray jsonList = new JSONArray(list);
        accesDistantCompteDetail = new AccesDistantCompteDetail(handler);
        // envoi au serveur distant les information necessaire pour l'authentification
        accesDistantCompteDetail.envoi("ajoutdepense",jsonList);

      }
    });

    ajouterDepenseDialog.build();

  }

  private void clickAjouterDepense(){
    btnAjouterDepense
      .setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          initAjouterDepenseDialog();
        }
      });
  }

  private void recupCompteAccesDistant(){
    List list = new ArrayList();
    list.add(utilisateur.getId());
    list.add(this.idCompte);
    JSONArray jsonList = new JSONArray(list);
    accesDistantCompteDetail = new AccesDistantCompteDetail(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantCompteDetail.envoi("recupcompte",jsonList);
  }

  private void valoriseCompte(){
    this.monCompte = accesDistantCompteDetail.getMesComptes().get(0);
    this.monBudget = accesDistantCompteDetail.getMesBudgets().get(0);
    this.mesDepenses = accesDistantCompteDetail.getMesDepenses();

    this.monCompte.setLeBudget(this.monBudget);
    this.monCompte.setLesDepenses(this.mesDepenses);
  }

  private void changeActivity(Class<?> cls){
    Intent intent = new Intent(compteDetail.this, cls) ;
    startActivity(intent) ;
    this.finish();
  }



}
