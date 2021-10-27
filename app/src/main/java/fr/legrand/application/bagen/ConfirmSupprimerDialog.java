package fr.legrand.application.bagen;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class ConfirmSupprimerDialog extends Dialog {

  //fields
  private Button btnAnnuler;
  private Button btnValider;


  //constructor
  public ConfirmSupprimerDialog(Activity activity){
    super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
    setContentView(R.layout.layout_confirm_supprimer);
    btnAnnuler = (Button)findViewById(R.id.btnSupprimerAnnuler);
    btnValider = (Button) findViewById(R.id.btnSupprimerValider);

    //this.getWindow().setLayout(800,1200);
  }

  public Button getBtnAnnuler() {
    return btnAnnuler;
  }

  public Button getBtnValider() {
    return btnValider;
  }





  public void build(){
    show();
  }
}

