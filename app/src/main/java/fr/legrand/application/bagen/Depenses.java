package fr.legrand.application.bagen;

public class Depenses {

  private String id;
  private String idcompte_id;
  private String libelle;
  private String montant;
  private String date;
  private String datemodif;

  public Depenses(String id, String idcompte_id, String libelle, String montant, String date, String datemodif) {
    this.id = id;
    this.idcompte_id = idcompte_id;
    this.libelle = libelle;
    this.montant = montant;
    this.date = date;
    this.datemodif = datemodif;
  }

  public String getId() {
    return id;
  }

  public String getIdcompte_id() {
    return idcompte_id;
  }

  public String getLibelle() {
    return libelle;
  }

  public String getMontant() {
    return montant;
  }

  public String getDate() {
    return date;
  }

  public String getDatemodif() {
    return datemodif;
  }
}
