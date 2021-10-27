package fr.legrand.application.bagen;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

public class AccesDistantCompteDetail implements AsyncResponse {

  // constante
  private static final String SERVERADDR = "https://bagen.alwaysdata.net/bagen/android/connection/serveurCompteDetail.php";
  //private static final String SERVERADDR = "http://192.168.43.190/Suividevosfrais/serveursuividevosfrais.php";

  private static final int STATE_VALIDE = 1;
  private static final int STATE_ERREUR = 2;
  private static final int STATE_MAIL_USED = 3;
  private static final int VALIDE_NEW_BUDGET = 4;
  private static final int VALIDE_AJOUT_DEPENSE = 5;
  private static final int VALIDE_MODIF_DEPENSE = 6;
  private static final int VALIDE_SUPPRIMER_DEPENSE = 7;
  private static final int VALIDE_SUPPRIMER_COMPTE = 8;

  private ArrayList<Compte> mesComptes;
  private ArrayList<Budget> mesBudgets;
  private ArrayList<Depenses> mesDepenses;

  public ArrayList<Compte> getMesComptes() {
    return mesComptes;
  }

  public ArrayList<Budget> getMesBudgets() {
    return mesBudgets;
  }

  public ArrayList<Depenses> getMesDepenses() {
    return mesDepenses;
  }



  private Handler mHandler;


  /**
   * Constructeur
   */
  public AccesDistantCompteDetail(Handler handler) {
    this.mHandler = handler;
    //super();
  }

  /**
   * Retour du serveur HTTP
   *
   * @param output
   */
  @Override
  public void processFinish(String output) {

    // pour vérification, affiche le contenu du retour dans la console
    Log.d("serveur", "************" + output);
    // découpage du message reçu
    String[] message = output.split("%");
    Log.d("serveur", "************ " + message[0]+" "+message[1]);
    // contrôle si le retour est correct (au moins 2 cases)
    if (message.length > 0) {
      if (message[0].equals("recupcompte")) {
        if(message[1].equals("erreur") || message[2].equals("erreur") || message[3].equals("erreur") ){
        Message msg = new Message();
        msg.what = STATE_ERREUR;
        mHandler.sendMessage(msg);
      }else{
        if(message[1].equals("no")){

        }else{
          mesComptes = new ArrayList<Compte>();
          String[] lesComptes = message[1].split("=");
          for(int k = 0 ; k < lesComptes.length ; k++){
            String[] lesChamps = lesComptes[k].split(":");
            String id = lesChamps[0];
            String idutilisateur_id = lesChamps[1];
            String intitule = lesChamps[2];
            String datecreation = lesChamps[3];
            String datecloture = lesChamps[4];
            String datemodif = lesChamps[5];
            Log.d("compte :","************ "+id+" "+idutilisateur_id+" "+intitule+" "+datecreation+" "+datecloture+" "+datemodif);
            Compte leCompte = new Compte(id, idutilisateur_id, intitule, datecreation, datecloture, datemodif);
            mesComptes.add(leCompte);
          }
        }
        if(message[2].equals("no")){

        }else{
          mesBudgets = new ArrayList<Budget>();
          String[] lesBudgets = message[2].split("=");
          for(int k = 0 ; k < lesBudgets.length ; k++){
            String[] lesChamps = lesBudgets[k].split(":");
            String id = lesChamps[0];
            String idcompte_id = lesChamps[1];
            String montant = lesChamps[2];
            String date = lesChamps[3];
            Log.d("budget :","************ "+id+" "+idcompte_id+" "+montant+" "+date);
            Budget leBudget = new Budget(id, idcompte_id, montant, date);
            mesBudgets.add(leBudget);
          }
        }
        if(message[3].equals("no")){

        }else{
          mesDepenses = new ArrayList<Depenses>();
          String[] lesDepenses = message[3].split("=");
          for(int k = 0 ; k < lesDepenses.length ; k++){
            String[] lesChamps = lesDepenses[k].split(":");
            String id = lesChamps[0];
            String idcompte_id = lesChamps[1];
            String libelle = lesChamps[2];
            String montant = lesChamps[3];
            String date = lesChamps[4];
            String datemodif = lesChamps[5];
            Log.d("Depense :","************ "+id+" "+idcompte_id+" "+libelle+" "+montant+" "+date+" "+datemodif);
            Depenses laDepense = new Depenses(id, idcompte_id, libelle, montant, date, datemodif);
            mesDepenses.add(laDepense);
          }

        }
        Message msg = new Message();
        msg.what = STATE_VALIDE;
        mHandler.sendMessage(msg);
      }

      } else {
        if(message[0].equals("modifbudget")){
          if(message[1].equals("valide")){
            Message msg = new Message();
            msg.what = VALIDE_NEW_BUDGET;
            mHandler.sendMessage(msg);
          }else{
            Message msg = new Message();
            msg.what = STATE_ERREUR;
            mHandler.sendMessage(msg);
          }

        }else{
          if(message[0].equals("ajoutdepense")){
            if(message[1].equals("valide")){
              Message msg = new Message();
              msg.what = VALIDE_AJOUT_DEPENSE;
              mHandler.sendMessage(msg);
            }else{
              Message msg = new Message();
              msg.what = STATE_ERREUR;
              mHandler.sendMessage(msg);
            }

          }else {
            if (message[0].equals("modifdepense")) {
              if (message[1].equals("valide")) {
                Message msg = new Message();
                msg.what = VALIDE_MODIF_DEPENSE;
                mHandler.sendMessage(msg);
              } else {
                Message msg = new Message();
                msg.what = STATE_ERREUR;
                mHandler.sendMessage(msg);
              }

            } else {
              if (message[0].equals("supprimerdepense")) {
                if (message[1].equals("valide")) {
                  Message msg = new Message();
                  msg.what = VALIDE_SUPPRIMER_DEPENSE;
                  mHandler.sendMessage(msg);
                } else {
                  Message msg = new Message();
                  msg.what = STATE_ERREUR;
                  mHandler.sendMessage(msg);
                }

              }else{
                if (message[0].equals("supprimercompte")) {
                  if (message[1].equals("valide")) {
                    Message msg = new Message();
                    msg.what = VALIDE_SUPPRIMER_COMPTE;
                    mHandler.sendMessage(msg);
                  } else {
                    Message msg = new Message();
                    msg.what = STATE_ERREUR;
                    mHandler.sendMessage(msg);
                  }

                }else{
                  Message msg = new Message();
                  msg.what = STATE_ERREUR;
                  mHandler.sendMessage(msg);
                }

              }

            }
          }

        }

      }
    }else{
      Message msg = new Message();
      msg.what = STATE_ERREUR;
      mHandler.sendMessage(msg);
    }

  }



  /**
   * Envoi de données vers le serveur distant
   * @param operation information précisant au serveur l'opération à exécuter
   * @param lesDonneesJSON les données à traiter par le serveur
   */

  public void envoi(String operation, JSONArray lesDonneesJSON){
    AccesHTTP accesDonnees = new AccesHTTP();
    // lien avec AccesHTTP pour permettre à delegate d'appeler la méthode processFinish
    // au retour du serveur
    accesDonnees.delegate = this;
    // ajout de paramètres dans l'enveloppe HTTP
    accesDonnees.addParam("operation", operation);
    accesDonnees.addParam("lesdonnees", lesDonneesJSON.toString());
    // envoi en post des paramètres, à l'adresse SERVERADDR
    accesDonnees.execute(SERVERADDR);
  }



}

