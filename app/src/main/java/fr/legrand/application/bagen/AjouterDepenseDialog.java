package fr.legrand.application.bagen;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.Date;

public class AjouterDepenseDialog extends Dialog {

  //fields
  private Button btnAnnuler;
  private Button btnValider;
  private EditText titre;
  private EditText montant;
  private DatePicker date;


  //constructor
  public AjouterDepenseDialog(Activity activity){
    super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
    setContentView(R.layout.layout_ajouter_depense_dialog);
    btnAnnuler = (Button)findViewById(R.id.btnAnnuler);
    btnValider = (Button) findViewById(R.id.btnValider);
    titre = (EditText) findViewById(R.id.etDepenseTitre);
    montant = (EditText)findViewById(R.id.etDepenseMontant);
    date = (DatePicker) findViewById(R.id.datAjouterDepense);
    this.getWindow().setLayout(800,1200);
  }

  public Button getBtnAnnuler() {
    return btnAnnuler;
  }

  public Button getBtnValider() {
    return btnValider;
  }

  public EditText getTitre() {
    return titre;
  }

  public EditText getMontant() {
    return montant;
  }

  public DatePicker getDate() {
    return date;
  }



  public void build(){
    show();
  }
}

