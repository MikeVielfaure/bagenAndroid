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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CompteAdapter extends BaseAdapter {


  private final ArrayList<Compte> lesComptes ;
  private ArrayList<Compte> lesComptesBis = new ArrayList<Compte>();
  private  ArrayList<Compte> lesComptesGauche = new ArrayList<Compte>();
  private ArrayList<Compte> lesComptesDroite = new ArrayList<Compte>() ;
  private final LayoutInflater inflater ;
  private Context context;
  private String key;
  private int number = -1;
  //private Activity activity;

  private void partageCompte(){
    for(int k = 0; k < lesComptesBis.size(); k++){
      if(k % 2 == 0){
        lesComptesGauche.add(lesComptesBis.get(k));
      }else{
        lesComptesDroite.add(lesComptesBis.get(k));
      }
    }
  }

  /**
   * Constructeur de l'adapter pour valoriser les propriétés
   * @param context Accès au contexte de l'application
   * @param lesComptes Liste des frais hors forfait
   */
  public CompteAdapter(Context context, ArrayList<Compte> lesComptes, String key ) {
    inflater = LayoutInflater.from(context) ;
    this.lesComptesBis = lesComptes ;
    this.context = context;
    this.key = key;

    partageCompte();
    this.lesComptes = this.lesComptesGauche;
    //this.activity = activity;
  }

  /**
   * retourne le nombre d'éléments de la listview
   */
  @Override
  public int getCount() {
    return lesComptes.size() ;
  }

  /**
   * retourne l'item de la listview à un index précis
   */
  @Override
  public Object getItem(int index) {
    return lesComptes.get(index) ;
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
    TextView txtIntitule ;
    TextView txtBudget;
    TextView txtDepense;
    Button btnVoir;

    TextView txtIntitule2 ;
    TextView txtBudget2;
    TextView txtDepense2;
    Button btnVoir2;

    ProgressBar progress_bar;
    ProgressBar progress_bar2;
    TextView text_view_progress;
    TextView text_view_progress2;

    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;
    RelativeLayout relativeLayoutImg;
    RelativeLayout relativeLayoutImg2;

    LinearLayout div2;

  }

  /**
   * Affichage dans la liste
   */
  @Override
  public View getView(int index, View convertView, ViewGroup parent) {
    ViewHolder holder ;
    if (convertView == null) {
      holder = new ViewHolder() ;
      convertView = inflater.inflate(R.layout.layout_liste_compte2, parent, false) ;
      holder.txtIntitule = convertView.findViewById(R.id.txtIntitule);
      holder.txtDepense = convertView.findViewById(R.id.txtDepense);
      holder.txtBudget = convertView.findViewById(R.id.txtBudget);
      holder.btnVoir = convertView.findViewById(R.id.btnVoir);

      holder.txtIntitule2 = convertView.findViewById(R.id.txtIntitule2);
      holder.txtDepense2 = convertView.findViewById(R.id.txtDepense2);
      holder.txtBudget2 = convertView.findViewById(R.id.txtBudget2);
      holder.btnVoir2 = convertView.findViewById(R.id.btnVoir2);

      holder.div2 = convertView.findViewById(R.id.div2);

      holder.progress_bar = convertView.findViewById(R.id.progress_bar);
      holder.progress_bar2 = convertView.findViewById(R.id.progress_bar2);
      holder.text_view_progress = convertView.findViewById(R.id.text_view_progress);
      holder.text_view_progress2 = convertView.findViewById(R.id.text_view_progress2);

      holder.relativeLayout = convertView.findViewById(R.id.relativeLayout);
      holder.relativeLayout2 = convertView.findViewById(R.id.relativeLayout2);
      holder.relativeLayoutImg = convertView.findViewById(R.id.relativeLayoutImg);
      holder.relativeLayoutImg2 = convertView.findViewById(R.id.relativeLayoutImg2);

      convertView.setTag(holder) ;
    }else{
      holder = (ViewHolder)convertView.getTag();
    }


    Log.d("aaa",String.valueOf(index));
    Log.d("bbb",String.valueOf(number));

    if((this.lesComptes.size() > this.lesComptesDroite.size()) && (String.valueOf(index).equals(String.valueOf(lesComptes.size()-1 )))  ){
      if(lesComptes.get(index).getLeBudget().getMontant() == "null"){
        visibleLayout(holder.relativeLayoutImg);
        invisibleLayout(holder.relativeLayout);
        holder.txtIntitule.setText(lesComptes.get(index).getIntitule());
        holder.txtDepense.setText(lesComptes.get(index).getTotalDepense()+" €");
        holder.btnVoir.setTag(lesComptes.get(index).getId());

      }else {
        visibleLayout(holder.relativeLayout);
        invisibleLayout(holder.relativeLayoutImg);

        Log.d("tttt", String.valueOf(index) + " " + String.valueOf(lesComptes.size() - 1));
        holder.txtIntitule.setText(lesComptes.get(index).getIntitule());
        holder.txtBudget.setText(lesComptes.get(index).getLeBudget().getMontant() + " €");
        holder.txtDepense.setText(lesComptes.get(index).getTotalDepense() + " €");
        holder.btnVoir.setTag(lesComptes.get(index).getId());

        // pour progressbar
        Double budget = Double.valueOf(lesComptes.get(index).getLeBudget().getMontant());
        Double totalDepense = Double.valueOf(lesComptes.get(index).getTotalDepense());
        int pourcent = (int) (Math.round((totalDepense / budget) * 100));
        Log.d("pourcent", "****** " + pourcent);
        holder.text_view_progress.setText(pourcent + "%");
        holder.progress_bar.setProgress(pourcent);
      }

      //holder.div2.setVisibility(View.INVISIBLE);
      holder.btnVoir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // on récupère l'index
          String index = (String)v.getTag();
          Serializer.serialize("numCompte",index,context);
          // on actualise la view
          Intent intent = new Intent(context, compteDetail.class) ;
          context.startActivity(intent) ;

          notifyDataSetChanged() ;
        }
      });

      if(holder.txtBudget2.getText().toString().equals("hello")){
        holder.div2.setVisibility(View.INVISIBLE);
      }
    }else {

        if(lesComptes.get(index).getLeBudget().getMontant() == "null"){
          visibleLayout(holder.relativeLayoutImg);
          invisibleLayout(holder.relativeLayout);
          holder.txtIntitule.setText(lesComptes.get(index).getIntitule());
          holder.txtDepense.setText(lesComptes.get(index).getTotalDepense()+" €");
          holder.btnVoir.setTag(lesComptes.get(index).getId());
        }else {
          visibleLayout(holder.relativeLayout);
          invisibleLayout(holder.relativeLayoutImg);

          holder.txtIntitule.setText(lesComptes.get(index).getIntitule());
          holder.txtBudget.setText(lesComptes.get(index).getLeBudget().getMontant() + " €");
          holder.txtDepense.setText(lesComptes.get(index).getTotalDepense() + " €");
          holder.btnVoir.setTag(lesComptes.get(index).getId());

          // pour progressbar
          Double budget = Double.valueOf(lesComptes.get(index).getLeBudget().getMontant());
          Double totalDepense = Double.valueOf(lesComptes.get(index).getTotalDepense());
          int pourcent = (int) (Math.round((totalDepense / budget) * 100));
          Log.d("pourcent", "****** " + pourcent);
          holder.text_view_progress.setText(pourcent + "%");
          holder.progress_bar.setProgress(pourcent);
        }

        holder.btnVoir.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // on récupère l'index
            String index = (String) v.getTag();
            Serializer.serialize("numCompte", index, context);
            // on actualise la view
            Intent intent = new Intent(context, compteDetail.class);
            context.startActivity(intent);

            notifyDataSetChanged();

          }
        });

        if(lesComptesDroite.get(index).getLeBudget().getMontant() == "null"){
          visibleLayout(holder.relativeLayoutImg2);
          invisibleLayout(holder.relativeLayout2);
          holder.txtIntitule2.setText(lesComptesDroite.get(index).getIntitule());
          holder.txtDepense2.setText(lesComptesDroite.get(index).getTotalDepense()+" €");
          holder.btnVoir2.setTag(lesComptesDroite.get(index).getId());
        }else {
          holder.txtIntitule2.setText(lesComptesDroite.get(index).getIntitule());
          holder.txtBudget2.setText(lesComptesDroite.get(index).getLeBudget().getMontant() + " €");
          holder.txtDepense2.setText(lesComptesDroite.get(index).getTotalDepense() + " €");
          holder.btnVoir2.setTag(lesComptesDroite.get(index).getId());

          // pour progressbar
          Double budget2 = Double.valueOf(lesComptesDroite.get(index).getLeBudget().getMontant());
          Double totalDepense2 = Double.valueOf(lesComptesDroite.get(index).getTotalDepense());
          int pourcent2 = (int) (Math.round((totalDepense2 / budget2) * 100));
          Log.d("pourcent", "****** " + pourcent2);
          holder.text_view_progress2.setText(pourcent2 + "%");
          holder.progress_bar2.setProgress(pourcent2);
        }

        holder.btnVoir2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // on récupère l'index
            String index = (String) v.getTag();
            Serializer.serialize("numCompte", index, context);
            // on actualise la view
            Intent intent = new Intent(context, compteDetail.class);
            context.startActivity(intent);

            notifyDataSetChanged();

          }
        });

    }



    // gestion de l'événement clic sur le bouton de suppression

    number++;
    return convertView ;
  }

  private void invisibleLayout(RelativeLayout relativeLayout){
    relativeLayout.setVisibility(View.INVISIBLE);
    ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
    //Changes the height and width to the specified *pixels*
    params.height = 0;
    params.width = 0;
    relativeLayout.setLayoutParams(params);
  }

  private void visibleLayout(RelativeLayout relativeLayout){
    relativeLayout.setVisibility(View.VISIBLE);
    ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
    //Changes the height and width to the specified *pixels*
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
    relativeLayout.setLayoutParams(params);
  }

}
