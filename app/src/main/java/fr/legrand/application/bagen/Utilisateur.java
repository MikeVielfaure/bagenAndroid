package fr.legrand.application.bagen;

import java.io.Serializable;

public class Utilisateur implements Serializable {
  private String id;
  private String mail;
  private String mdp;
  private String nom;
  private String prenom;
  private String adresse;
  private String ville;
  private String codepostal;
  private boolean session = false;

  public boolean isSession() {
    return session;
  }

  public void setSession(boolean session) {
    this.session = session;
  }



  public Utilisateur(String id, String mail, String mdp, String nom, String prenom, String adresse, String ville, String codepostal) {
    this.id = id;
    this.mail = mail;
    this.mdp = mdp;
    this.nom = nom;
    this.prenom = prenom;
    this.adresse = adresse;
    this.ville = ville;
    this.codepostal = codepostal;
  }

  public String getId() {
    return id;
  }

  public String getMail() {
    return mail;
  }

  public String getMdp() {
    return mdp;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public String getAdresse() {
    return adresse;
  }

  public String getVille() {
    return ville;
  }

  public String getCodepostal() {
    return codepostal;
  }
}
