package fr.legrand.application.bagen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class DepenseAdapter extends BaseAdapter {

  private final ArrayList<Depenses> lesDepenses ; // liste des frais du mois
  private ArrayList<Depenses> lesDepensesBis = new ArrayList<Depenses>();
  private  ArrayList<Depenses> lesDepensesGauche = new ArrayList<Depenses>();
  private ArrayList<Depenses> lesDepensesDroite = new ArrayList<Depenses>() ;
  private final LayoutInflater inflater ;
  private Context context;
  private String key;
  private int number = -1;
  private AjouterDepenseDialog ajouterDepenseDialog ;
  private ConfirmSupprimerDialog confirmSupprimerDialog;
  private Activity activity;
  private AccesDistantCompteDetail accesDistantCompteDetail;
  private Handler handler;

  private int myIndex;
  //private Activity activity;

  private void partageDepense(){
    for(int k = 0; k < lesDepensesBis.size(); k++){
      if(k % 2 == 0){
        lesDepensesGauche.add(lesDepensesBis.get(k));
      }else{
        lesDepensesDroite.add(lesDepensesBis.get(k));
      }
    }
  }

  /**
   * Constructeur de l'adapter pour valoriser les propriétés
   * @param context Accès au contexte de l'application
   * @param lesDepenses Liste des frais hors forfait
   */
  public DepenseAdapter(Context context, ArrayList<Depenses> lesDepenses, String key, Activity activity, Handler handler) {
    inflater = LayoutInflater.from(context) ;
    this.lesDepensesBis = lesDepenses ;
    this.context = context;
    this.key = key;
    this.activity = activity;
    this.handler = handler;
    //this.activity = activity;
    partageDepense();
    this.lesDepenses = this.lesDepensesGauche;
  }

  /**
   * retourne le nombre d'éléments de la listview
   */
  @Override
  public int getCount() {
    return lesDepenses.size() ;
  }

  /**
   * retourne l'item de la listview à un index précis
   */
  @Override
  public Object getItem(int index) {
    return lesDepenses.get(index) ;
  }

  /**
   * retourne l'index de l'élément actuel
   */
  @Override
  public long getItemId(int index) {
    return index;
  }

  /**
   * structure contenant les éléments d'une ligne
   */
  private class ViewHolder {
    TextView txtlLibelleDepense ;
    TextView txtDateDepense;
    TextView txtMontantDepenseAdapter;
    Button btnModifierDepense;
    Button btnSupprDepense;

    TextView txtlLibelleDepense2 ;
    TextView txtDateDepense2;
    TextView txtMontantDepenseAdapter2;
    Button btnModifierDepense2;
    Button btnSupprDepense2;

    LinearLayout div2;

  }

  /**
   * Affichage dans la liste
   */
  @Override
  public View getView(int index, View convertView, ViewGroup parent) {
    DepenseAdapter.ViewHolder holder ;
    if (convertView == null) {
      holder = new DepenseAdapter.ViewHolder() ;
      convertView = inflater.inflate(R.layout.layout_liste_depense2, parent, false) ;
      holder.txtlLibelleDepense = convertView.findViewById(R.id.txtLibelleDepense);
      holder.txtDateDepense = convertView.findViewById(R.id.txtDateDepense);
      holder.txtMontantDepenseAdapter = convertView.findViewById(R.id.txtMontantDepenseAdapter);
      holder.btnModifierDepense = convertView.findViewById(R.id.btnModifierDepense);
      holder.btnSupprDepense = convertView.findViewById(R.id.btnSupprDepense);

      holder.txtlLibelleDepense2 = convertView.findViewById(R.id.txtLibelleDepense2);
      holder.txtDateDepense2 = convertView.findViewById(R.id.txtDateDepense2);
      holder.txtMontantDepenseAdapter2 = convertView.findViewById(R.id.txtMontantDepenseAdapter2);
      holder.btnModifierDepense2 = convertView.findViewById(R.id.btnModifierDepense2);
      holder.btnSupprDepense2 = convertView.findViewById(R.id.btnSupprDepense2);

      holder.div2 = convertView.findViewById(R.id.div2);

      convertView.setTag(holder) ;
    }else{
      holder = (DepenseAdapter.ViewHolder)convertView.getTag();
    }
    myIndex = index;
    if((this.lesDepenses.size() > this.lesDepensesDroite.size()) && (String.valueOf(index).equals(String.valueOf(lesDepenses.size()-1 )))  ) {
      holder.txtlLibelleDepense.setText(lesDepenses.get(index).getLibelle());
      holder.txtDateDepense.setText(Outils.changeDate(lesDepenses.get(index).getDate()));
      holder.txtMontantDepenseAdapter.setText(lesDepenses.get(index).getMontant() + " €");
      holder.btnModifierDepense.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // on récupère l'index
          String index = (String) v.getTag();
          ajouterDepenseDialog = new AjouterDepenseDialog(activity);
          ajouterDepenseDialog.getTitre().setText(lesDepenses.get(myIndex).getLibelle());
          ajouterDepenseDialog.getMontant().setText(lesDepenses.get(myIndex).getMontant());
          //ajouterDepenseDialog.getDate().setFirstDayOfWeek(Integer.parseInt(Outils.pickJour(lesDepensesDroite.get(myIndex).getDate())));
          int jour = Integer.parseInt(Outils.pickJour(lesDepenses.get(myIndex).getDate()));
          int mois = Integer.parseInt(Outils.pickMois(lesDepenses.get(myIndex).getDate()))-1;
          int annee = Integer.parseInt(Outils.pickAnnee(lesDepenses.get(myIndex).getDate()));
          Log.d("date :","********* "+jour+"-"+mois+"-"+annee);
          ajouterDepenseDialog.getDate().updateDate(annee,mois,jour);
          //ajouterDepenseDialog.getTitre().setText(lesDepensesDroite.get(Integer.valueOf(index)).getLibelle());
          Button btnAnnuler = ajouterDepenseDialog.getBtnAnnuler();
          Button btnValider = ajouterDepenseDialog.getBtnValider();
          btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ajouterDepenseDialog.dismiss();
            }
          });
          btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String jours = String.valueOf(ajouterDepenseDialog.getDate().getDayOfMonth());
              String mois = String.valueOf(ajouterDepenseDialog.getDate().getMonth());
              String annee = String.valueOf(ajouterDepenseDialog.getDate().getYear());
              String date = annee+"-"+mois+"-"+jours;
              updateDepenseAcces(lesDepenses.get(myIndex).getId(),lesDepenses.get(myIndex).getIdcompte_id(),date,ajouterDepenseDialog.getTitre().getText().toString()
                , ajouterDepenseDialog.getMontant().getText().toString());
              ajouterDepenseDialog.dismiss();
            }
          });



          ajouterDepenseDialog.build();

        }
      });

      holder.btnSupprDepense.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          confirmSupprimerDialog = new ConfirmSupprimerDialog(activity);
          Button btnValiderSuppr = confirmSupprimerDialog.getBtnValider();
          Button btnAnnulerSuppr = confirmSupprimerDialog.getBtnAnnuler();

          btnAnnulerSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              confirmSupprimerDialog.dismiss();
            }
          });

          btnValiderSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              supprimerDepenseAcces(lesDepenses.get(myIndex).getId().toString());
              confirmSupprimerDialog.dismiss();
            }
          });

          confirmSupprimerDialog.build();
        }
      });

      if(holder.txtDateDepense2.getText().toString().equals("hello")){
        holder.div2.setVisibility(View.INVISIBLE);
      }


    }else{
      holder.txtlLibelleDepense.setText(lesDepenses.get(index).getLibelle());
      holder.txtDateDepense.setText(Outils.changeDate(lesDepenses.get(index).getDate()));
      holder.txtMontantDepenseAdapter.setText(lesDepenses.get(index).getMontant() + " €");
      holder.btnModifierDepense.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // on récupère l'index
          String index = (String) v.getTag();
          ajouterDepenseDialog = new AjouterDepenseDialog(activity);
          ajouterDepenseDialog.getTitre().setText(lesDepenses.get(myIndex).getLibelle());
          ajouterDepenseDialog.getMontant().setText(lesDepenses.get(myIndex).getMontant());
          //ajouterDepenseDialog.getDate().setFirstDayOfWeek(Integer.parseInt(Outils.pickJour(lesDepensesDroite.get(myIndex).getDate())));
          int jour = Integer.parseInt(Outils.pickJour(lesDepenses.get(myIndex).getDate()));
          int mois = Integer.parseInt(Outils.pickMois(lesDepenses.get(myIndex).getDate()))-1;
          int annee = Integer.parseInt(Outils.pickAnnee(lesDepenses.get(myIndex).getDate())) ;
          Log.d("date :","********* "+jour+"-"+mois+"-"+annee);
          ajouterDepenseDialog.getDate().updateDate(annee,mois,jour);
          //ajouterDepenseDialog.getTitre().setText(lesDepensesDroite.get(Integer.valueOf(index)).getLibelle());
          Button btnAnnuler = ajouterDepenseDialog.getBtnAnnuler();
          Button btnValider = ajouterDepenseDialog.getBtnValider();
          btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ajouterDepenseDialog.dismiss();
            }
          });
          btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String jours = String.valueOf(ajouterDepenseDialog.getDate().getDayOfMonth());
              String mois = String.valueOf(ajouterDepenseDialog.getDate().getMonth());
              String annee = String.valueOf(ajouterDepenseDialog.getDate().getYear());
              String date = annee+"-"+mois+"-"+jours;
              updateDepenseAcces(lesDepenses.get(myIndex).getId(),lesDepenses.get(myIndex).getIdcompte_id(),date,ajouterDepenseDialog.getTitre().getText().toString()
                , ajouterDepenseDialog.getMontant().getText().toString());
              ajouterDepenseDialog.dismiss();
            }
          });
          ajouterDepenseDialog.build();

        }
      });

      holder.btnSupprDepense.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          confirmSupprimerDialog = new ConfirmSupprimerDialog(activity);
          Button btnValiderSuppr = confirmSupprimerDialog.getBtnValider();
          Button btnAnnulerSuppr = confirmSupprimerDialog.getBtnAnnuler();

          btnAnnulerSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              confirmSupprimerDialog.dismiss();
            }
          });

          btnValiderSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              supprimerDepenseAcces(lesDepenses.get(myIndex).getId().toString());
              confirmSupprimerDialog.dismiss();
            }
          });

          confirmSupprimerDialog.build();
        }
      });

      holder.txtlLibelleDepense2.setText(lesDepensesDroite.get(index).getLibelle());
      holder.txtDateDepense2.setText(Outils.changeDate(lesDepensesDroite.get(index).getDate()));
      holder.txtMontantDepenseAdapter2.setText(lesDepensesDroite.get(index).getMontant() + " €");
      holder.btnModifierDepense2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // on récupère l'index
          String index = (String) v.getTag();

          ajouterDepenseDialog = new AjouterDepenseDialog(activity);
          ajouterDepenseDialog.getTitre().setText(lesDepensesDroite.get(myIndex).getLibelle());
          ajouterDepenseDialog.getMontant().setText(lesDepensesDroite.get(myIndex).getMontant());
          //ajouterDepenseDialog.getDate().setFirstDayOfWeek(Integer.parseInt(Outils.pickJour(lesDepensesDroite.get(myIndex).getDate())));
          int jour = Integer.parseInt(Outils.pickJour(lesDepensesDroite.get(myIndex).getDate()));
          int mois = Integer.parseInt(Outils.pickMois(lesDepensesDroite.get(myIndex).getDate()))-1;
          int annee = Integer.parseInt(Outils.pickAnnee(lesDepensesDroite.get(myIndex).getDate())) ;
          Log.d("date :","********* "+jour+"-"+mois+"-"+annee);
          ajouterDepenseDialog.getDate().updateDate(annee, mois,jour);
          Button btnAnnuler = ajouterDepenseDialog.getBtnAnnuler();
          Button btnValider = ajouterDepenseDialog.getBtnValider();
          btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ajouterDepenseDialog.dismiss();
            }
          });
          btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String jours = String.valueOf(ajouterDepenseDialog.getDate().getDayOfMonth());
              String mois = String.valueOf(ajouterDepenseDialog.getDate().getMonth());
              String annee = String.valueOf(ajouterDepenseDialog.getDate().getYear());
              String date = annee+"-"+mois+"-"+jours;
              updateDepenseAcces(lesDepensesDroite.get(myIndex).getId(),lesDepensesDroite.get(myIndex).getIdcompte_id(),date,ajouterDepenseDialog.getTitre().getText().toString()
              , ajouterDepenseDialog.getMontant().getText().toString());
              ajouterDepenseDialog.dismiss();
            }
          });
          ajouterDepenseDialog.build();

        }
      });

      holder.btnSupprDepense2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          confirmSupprimerDialog = new ConfirmSupprimerDialog(activity);
          Button btnValiderSuppr = confirmSupprimerDialog.getBtnValider();
          Button btnAnnulerSuppr = confirmSupprimerDialog.getBtnAnnuler();

          btnAnnulerSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              confirmSupprimerDialog.dismiss();
            }
          });

          btnValiderSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              supprimerDepenseAcces(lesDepensesDroite.get(myIndex).getId().toString());
              confirmSupprimerDialog.dismiss();
            }
          });

          confirmSupprimerDialog.build();
        }
      });

    }



    // gestion de l'événement clic sur le bouton de suppression
    number++;
    return convertView ;
  }

  private void updateDepenseAcces(String iddepense, String idcompte, String date, String titre, String montant){
    List list = new ArrayList();
    list.add(iddepense);
    list.add(idcompte);
    list.add(date);
    list.add(titre);
    list.add(montant);
    JSONArray jsonList = new JSONArray(list);
    accesDistantCompteDetail = new AccesDistantCompteDetail(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantCompteDetail.envoi("modifdepense",jsonList);
  }

  private void supprimerDepenseAcces(String iddepense){
    List list = new ArrayList();
    list.add(iddepense);
    JSONArray jsonList = new JSONArray(list);
    accesDistantCompteDetail = new AccesDistantCompteDetail(handler);
    // envoi au serveur distant les information necessaire pour l'authentification
    accesDistantCompteDetail.envoi("supprimerdepense",jsonList);
  }

}
