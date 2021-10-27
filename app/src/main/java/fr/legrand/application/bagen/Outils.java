package fr.legrand.application.bagen;

import java.util.ArrayList;

public abstract class Outils {

  //format jj-mm-yyyy
  public static final String changeDate(String date){
    String[] tabDate = date.split("-");
    String annee = tabDate[0];
    String mois = tabDate[1];
    String jours = tabDate[2];

    return jours+"-"+mois+"-"+annee;
  }

  // retourne le jour d'une date format yyyy-mm-jj
  public static final String pickJour(String date){
    String[] tabDate = date.split("-");
    String jours = tabDate[2];
    return jours;
  }

  // retourne le mois d'une date format yyyy-mm-jj
  public static final String pickMois(String date){
    String[] tabDate = date.split("-");
    String mois = tabDate[1];
    return mois;
  }

  // retourne le jour d'une date format yyyy-mm-jj
  public static final String pickAnnee(String date){
    String[] tabDate = date.split("-");
    String annee = tabDate[0];
    return annee;
  }


}
