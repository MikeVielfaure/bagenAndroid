package fr.legrand.application.bagen;

public class Budget {
  private String id;
  private String idcompte_id;
  private String montant;
  private String date;

  public Budget(String id, String idcompte_id, String montant, String date) {
    this.id = id;
    this.idcompte_id = idcompte_id;
    this.montant = montant;
    this.date = date;
  }

  public String getId() {
    return id;
  }

  public String getIdcompte_id() {
    return idcompte_id;
  }

  public String getMontant() {
    return montant;
  }

  public String getDate() {
    return date;
  }
}
