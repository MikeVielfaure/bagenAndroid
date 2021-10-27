package fr.legrand.application.bagen;

import java.util.ArrayList;

public class Compte {
  private String id;
  private String idutilisateur_id;
  private String intitule;
  private String datecreation;
  private String datecloture;
  private String datemodif;
  private ArrayList<Depenses> lesDepenses;
  private Budget leBudget;

  public Compte(String id, String idutilisateur_id, String intitule, String datecreation, String datecloture, String datemodif) {
    this.id = id;
    this.idutilisateur_id = idutilisateur_id;
    this.intitule = intitule;
    this.datecreation = datecreation;
    this.datecloture = datecloture;
    this.datemodif = datemodif;
    lesDepenses = new ArrayList<Depenses>();
    leBudget = new Budget(null,null,null,null);
  }

  public String getId() {
    return id;
  }

  public String getIdutilisateur_id() {
    return idutilisateur_id;
  }

  public String getIntitule() {
    return intitule;
  }

  public String getDatecreation() {
    return datecreation;
  }

  public String getDatecloture() {
    return datecloture;
  }

  public String getDatemodif() {
    return datemodif;
  }

  public ArrayList<Depenses> getLesDepenses() {
    return lesDepenses;
  }

  public void setLesDepenses(ArrayList<Depenses> lesDepenses) {
    this.lesDepenses = lesDepenses;
  }

  public Budget getLeBudget() {
    return leBudget;
  }

  public void setLeBudget(Budget leBudget) {
    this.leBudget = leBudget;
  }

  public String getTotalDepense(){
    float total = 0;
    for(Depenses depense : lesDepenses){
      total = total + Float.parseFloat(depense.getMontant());
    }
    return String.valueOf(total);
  }
}
