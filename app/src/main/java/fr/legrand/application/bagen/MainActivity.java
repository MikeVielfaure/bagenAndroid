package fr.legrand.application.bagen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.security.SecureRandom;
import java.util.ArrayList;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {

  String mesUtilisateur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mesUtilisateur = Serializer.deSerialize("numCompte",getBaseContext()).toString();
        Log.d("numCompte : ","*********** "+mesUtilisateur);

    }
}
