package fr.legrand.application.bagen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

  // propriété de création d'une table dans la base de données
  private String creation="create table compte ("
    + "id TEXT PRIMARY KEY,"
    + "idutilisateur_id TEXT NOT NULL,"
    + "intitule TEXT NOT NULL,"
    + "datecreation TEXT NOT NULL,"
    + "datecloture TEXT,"
    + "datemodif TEXT NOT NULL);";

  // propriété de création d'une table dans la base de données
  private String creation2="create table depense ("
    + "id TEXT PRIMARY KEY,"
    + "idcompte_id TEXT NOT NULL,"
    + "libelle TEXT NOT NULL,"
    + "montant TEXT NOT NULL,"
    + "date TEXT NOT NULL,"
    + "datemodif TEXT);";

  // propriété de création d'une table dans la base de données
  private String creation3="create table budget ("
    + "id TEXT PRIMARY KEY,"
    + "idcompte_id TEXT NOT NULL,"
    + "montant TEXT NOT NULL,"
    + "date TEXT NOT NULL);";



  /**
   * Construction de l'accès à une base de données locale
   * @param context
   * @param name
   * @param version
   */
  public MySQLiteOpenHelper(Context context, String name, int version) {
    super(context, name, null, version);
    // TODO Auto-generated constructor stub
  }

  /**
   * méthode redéfinie appelée automatiquement par le constructeur
   * uniquement si celui-ci repère que la base n'existe pas encore
   * @param db
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub
    db.execSQL(creation);
    db.execSQL(creation2);
    db.execSQL(creation3);
  }

  /**
   * méthode redéfinie appelée automatiquement s'il y a changement de version de la base
   * @param db
   * @param oldVersion
   * @param newVersion
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // TODO Auto-generated method stub

  }

}
