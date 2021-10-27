package fr.legrand.application.bagen;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;

public class AccesDistantRegister implements AsyncResponse {

  // constante
  private static final String SERVERADDR = "https://bagen.alwaysdata.net/bagen/android/connection/serveurRegister.php";
  //private static final String SERVERADDR = "http://192.168.43.190/Suividevosfrais/serveursuividevosfrais.php";

  private static final int STATE_VALIDE = 1;
  private static final int STATE_ERREUR = 2;
  private static final int STATE_MAIL_USED = 3;

  private Handler mHandler;


  /**
   * Constructeur
   */
  public AccesDistantRegister(Handler handler) {
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
      if (message[0].equals("register")) {
        if (message[1].equals("valide")) {
          Message msg = new Message();
          msg.what = STATE_VALIDE;
          mHandler.sendMessage(msg);
        }else{
          if(message[1].equals("used")){
            Message msg = new Message();
            msg.what = STATE_MAIL_USED;
            mHandler.sendMessage(msg);
          }else{
            Message msg = new Message();
            msg.what = STATE_ERREUR;
            mHandler.sendMessage(msg);
            }
          }
        }

      }else {
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

