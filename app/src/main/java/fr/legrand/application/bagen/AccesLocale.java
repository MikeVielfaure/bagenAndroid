package fr.legrand.application.bagen;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class AccesLocale {

  // propriétés
  private String nomBase = "bdBagen.sqlite";
  private Integer versionBase = 2;
  private MySQLiteOpenHelper accesBD;
  private SQLiteDatabase bd;

  /**
   * Constructeur
   * création de la connection à la bdd sqlite
   * @param context
   */
  public AccesLocale(Context context){
    this.accesBD = new MySQLiteOpenHelper(context, nomBase, versionBase);
  }

  /**
   *
   * @param id
   * @param idutilisateur_id
   * @param intitule
   * @param datecreation
   * @param datecloture
   * @param datemodif
   */
  public void ajoutCompte(String id, String idutilisateur_id , String intitule, String datecreation, String datecloture, String datemodif){
    // accès en écriture sur la BDD
    this.bd = accesBD.getWritableDatabase();
    // création de la requête d'ajout
    String req = "insert into compte (id, idutilisateur_id,intitule,datecreation,datecloture,datemodif) values ";
    req += "(\"" + id + "\","
      + "\"" + idutilisateur_id + "\","
      + "\"" + intitule + "\","
      + "\"" + datecreation + "\","
      + "\"" + datecloture + "\","
      + "\"" + datemodif + "\")";

    // exécution de la requête
    bd.execSQL(req);
  }

  public void ajoutBudget(String id, String idcompte_id , String montant, String date){
    // accès en écriture sur la BDD
    this.bd = accesBD.getWritableDatabase();
    // création de la requête d'ajout
    String req = "insert into budget (id, idcompte_id,montant,date) values ";
    req += "(\"" + id + "\","
      + "\"" + idcompte_id + "\","
      + "\"" + montant + "\","
      + "\"" + date + "\")";

    // exécution de la requête
    bd.execSQL(req);
  }

  public void ajoutDepense(String id, String idcompte_id , String libelle, String montant, String date, String datemodif){
    // accès en écriture sur la BDD
    this.bd = accesBD.getWritableDatabase();
    // création de la requête d'ajout
    String req = "insert into depense (id, idcompte_id,libelle,montant,date,datemodif) values ";
    req += "(\"" + id + "\","
      + "\"" + idcompte_id + "\","
      + "\"" + libelle + "\","
      + "\"" + montant + "\","
      + "\"" + date + "\","
      + "\"" + datemodif + "\")";

    // exécution de la requête
    bd.execSQL(req);
  }

  public void updateBudget(String newBudget, String idbudget){
    // accès en écriture sur la BDD
    this.bd = accesBD.getWritableDatabase();
    // création de la requête d'ajout
    String req = "Update budget Set montant= " + "\"" +newBudget+ "\""+"where id="+Integer.parseInt(idbudget);
    Log.d("requete : ","********* "+req);
    // exécution de la requête
    bd.execSQL(req);
  }

  public ArrayList<Compte> recupCompte(String idutilisateur){
    Compte compte = null;
    ArrayList<Compte> mesComptes = new ArrayList<Compte>();
    // accès en lecture à la BDD
    this.bd = accesBD.getReadableDatabase();
    // requête de récupération des profils
    String req = "select * from compte where idutilisateur_id = "+Integer.parseInt(idutilisateur);
    // curseur pour récupérer le résultat de l'exécution de la requête
    Cursor curseur = bd.rawQuery(req, null);
    curseur.moveToFirst();
    // contrôle s'il y a au moins une ligne (donc un profil)
    while(!curseur.isAfterLast()){
      // récupération des champs
      //Log.d("date", "***********************"+dateMesure);
      String id = curseur.getString(0);
      String idutilisateur_id = curseur.getString(1);
      String intitule = curseur.getString(2);
      String datecreation = curseur.getString(3);
      String datecloture = curseur.getString(4);
      String datemodif = curseur.getString(5);
      // création du profil
      compte = new Compte(id, idutilisateur_id, intitule, datecreation, datecloture, datemodif);
      mesComptes.add(compte);
      curseur.moveToNext();
    }
    curseur.close();
    return mesComptes;
  }

  public Compte recupCompteId(String idcompte){
    Compte compte = null;
    // accès en lecture à la BDD
    this.bd = accesBD.getReadableDatabase();
    // requête de récupération des profils
    String req = "select * from compte where id = "+Integer.parseInt(idcompte);
    // curseur pour récupérer le résultat de l'exécution de la requête
    Cursor curseur = bd.rawQuery(req, null);
    curseur.moveToFirst();
    // contrôle s'il y a au moins une ligne (donc un profil)
    while(!curseur.isAfterLast()){
      // récupération des champs
      //Log.d("date", "***********************"+dateMesure);
      String id = curseur.getString(0);
      String idutilisateur_id = curseur.getString(1);
      String intitule = curseur.getString(2);
      String datecreation = curseur.getString(3);
      String datecloture = curseur.getString(4);
      String datemodif = curseur.getString(5);
      // création du profil
      compte = new Compte(id, idutilisateur_id, intitule, datecreation, datecloture, datemodif);
      curseur.moveToNext();
    }
    curseur.close();
    return compte;
  }

  public ArrayList<Depenses> recupDepense(String idcompte){
    Depenses depense = null;
    ArrayList<Depenses> mesDepenses = new ArrayList<Depenses>();
    // accès en lecture à la BDD
    this.bd = accesBD.getReadableDatabase();
    // requête de récupération des profils
    String req = "select * from depense where idcompte_id = "+Integer.parseInt(idcompte);
    // curseur pour récupérer le résultat de l'exécution de la requête
    Cursor curseur = bd.rawQuery(req, null);
    curseur.moveToFirst();
    // contrôle s'il y a au moins une ligne (donc un profil)
    while(!curseur.isAfterLast()){
      // récupération des champs
      //Log.d("date", "***********************"+dateMesure);
      String id = curseur.getString(0);
      String idcompte_id = curseur.getString(1);
      String libelle = curseur.getString(2);
      String montant = curseur.getString(3);
      String date = curseur.getString(4);
      String datemodif = curseur.getString(5);
      // création du profil
      depense = new Depenses(id, idcompte_id, libelle, montant, date, datemodif);
      mesDepenses.add(depense);
      curseur.moveToNext();
    }
    curseur.close();
    return mesDepenses;
  }

  public Budget recupBudget(String idcompte){
    Budget budget = null;
    // accès en lecture à la BDD
    this.bd = accesBD.getReadableDatabase();
    // requête de récupération des profils
    String req = "select * from budget where idcompte_id = "+Integer.parseInt(idcompte);
    // curseur pour récupérer le résultat de l'exécution de la requête
    Cursor curseur = bd.rawQuery(req, null);
    curseur.moveToFirst();
    // contrôle s'il y a au moins une ligne (donc un profil)
    while(!curseur.isAfterLast()){
      // récupération des champs
      //Log.d("date", "***********************"+dateMesure);
      String id = curseur.getString(0);
      String idcompte_id = curseur.getString(1);
      String montant = curseur.getString(2);
      String date = curseur.getString(3);
      // création du profil
      budget = new Budget(id, idcompte_id, montant, date);
      curseur.moveToNext();
    }
    curseur.close();
    return budget;
  }


  /*
  public ArrayList<Ville> recupListeVille(){
    Ville ville = null;
    ArrayList<Ville> mesVilles = new ArrayList<Ville>();
    // accès en lecture à la BDD
    this.bd = accesBD.getReadableDatabase();
    // requête de récupération des profils
    String req = "select * from villesDeFrance";
    // curseur pour récupérer le résultat de l'exécution de la requête
    Cursor curseur = bd.rawQuery(req, null);
    curseur.moveToFirst();
    // contrôle s'il y a au moins une ligne (donc un profil)
    while(!curseur.isAfterLast()){
      // récupération des champs
      //Log.d("date", "***********************"+dateMesure);
      String nom = curseur.getString(1);
      Double latitude = Double.valueOf(curseur.getInt(2));
      Double longitude = Double.valueOf(curseur.getInt(3));
      String departement = curseur.getString(4);
      nom += " "+"("+departement+")";
      // création du profil
      ville = new Ville(nom, departement, latitude, longitude);
      mesVilles.add(ville);
      curseur.moveToNext();
    }
    curseur.close();
    return mesVilles;
  }

  public String latitude(String nom){
    String maLatitude = "";
    if(Outils.possedeDep(nom)) {
      String departement = Outils.recupDep(nom);
      String leNom = Outils.recupNom(nom);
      // accès en lecture à la BDD
      this.bd = accesBD.getReadableDatabase();
      // requête de récupération des profils
      String req = "select latitude from villesDeFrance where nom = ";
      req+= "\"" + leNom + "\" "
        + "and departement = "
        + "\"" + departement + "\" ";
      // curseur pour récupérer le résultat de l'exécution de la requête
      Cursor curseur = bd.rawQuery(req, null);
      curseur.moveToFirst();
      if(!curseur.isAfterLast()){
        maLatitude = curseur.getString(0);
      }else{
        maLatitude="";
      }
      curseur.close();
      return maLatitude;

    }else{
      String leNom = Outils.recupDep(nom);
      //accès en lecture à la BDD
      this.bd = accesBD.getReadableDatabase();
      // requête de récupération des profils
      String req = "select latitude from villesDeFrance where nom = ";
      req+= "\"" + leNom + "\" ";
      // curseur pour récupérer le résultat de l'exécution de la requête
      Cursor curseur = bd.rawQuery(req, null);
      curseur.moveToFirst();
      if(!curseur.isAfterLast()){
        maLatitude = curseur.getString(0);
      }else{
        maLatitude="";
      }
      curseur.close();
      return maLatitude;
    }

  }


  public String longitude(String nom){
    String maLongitude = "";
    if(Outils.possedeDep(nom)) {
      String departement = Outils.recupDep(nom);
      String leNom = Outils.recupNom(nom);
      // accès en lecture à la BDD
      this.bd = accesBD.getReadableDatabase();
      // requête de récupération des profils
      String req = "select longitude from villesDeFrance where nom = ";
      req+= "\"" + leNom + "\" "
        + "and departement = "
        + "\"" + departement + "\" ";
      // curseur pour récupérer le résultat de l'exécution de la requête
      Cursor curseur = bd.rawQuery(req, null);
      curseur.moveToFirst();
      if(!curseur.isAfterLast()){
        maLongitude = curseur.getString(0);
      }else{
        maLongitude="";
      }
      curseur.close();
      return maLongitude;

    }else{
      String leNom = Outils.recupDep(nom);
      //accès en lecture à la BDD
      this.bd = accesBD.getReadableDatabase();
      // requête de récupération des profils
      String req = "select longitude from villesDeFrance where nom = ";
      req+= "\"" + leNom + "\" ";
      // curseur pour récupérer le résultat de l'exécution de la requête
      Cursor curseur = bd.rawQuery(req, null);
      curseur.moveToFirst();
      if(!curseur.isAfterLast()){
        maLongitude = curseur.getString(0);
      }else{
        maLongitude="";
      }
      curseur.close();
      return maLongitude;
    }

  }*/

}

