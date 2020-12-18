package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import pe.pucp.tel306.firebox.Entity.Usuario;

public class LoginyRegistro extends AppCompatActivity {

    private int google_sign_in = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginyregistro);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.esc_reutilizable, new IniciarSesion()).commit();


        /*final EditText inputEmail, inputContra;
        inputEmail = (EditText) findViewById(R.id.editTextSesion);
        inputContra = (EditText) findViewById(R.id.editTextTextPassword);
        Button btnRegistro,btnIniciarSesion,btnGoogle;

        //btnRegistro = (Button) findViewById(R.id.buttonRegistrarseInicio);
        btnIniciarSesion = (Button) findViewById(R.id.buttonIniciarSesionPri);
        btnGoogle = (Button) findViewById(R.id.buttonRegistroConGoogle);

        /*btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputEmail.getText().toString().equals("") && !inputContra.getText().toString().equals("")){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail.getText().toString(), inputContra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                File archivo = new File(directorio, "imagen.jpg");

                                try {

                                }catch ()
                                InputStream stream = new FileInputStream(archivo);
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReference();
                                StorageReference carpeRoot = storageReference.child("users").child(id);
                                Log.d("infoApp", carpeRoot.toString());
                                ingresoExitoso(inputEmail.getText().toString());
                            }else {
                                mostrarError();
                            }
                        }
                    });
                }

                }
            }
        );*/


        session();
    }

        public void iniciarSesion (final String email, String contra) {
            if (!email.equals("") && !contra.equals("")){
                try {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                ingresoExitoso(email);
                                Usuario usuario = new Usuario();
                                usuario = obtenerDatosFireStore();

                            }else {
                                mostrarError();
                            }
                        }
                    });
                }catch (Error error){
                    Toast.makeText(getApplicationContext(),"Error: no esta registrado", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Error: debe colocar sus datos", Toast.LENGTH_SHORT).show();
            }
        }

    public Usuario obtenerDatosFireStore() {
        final Usuario usuario = new Usuario();
        FirebaseFirestore dbF = FirebaseFirestore.getInstance();
        dbF.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            usuario.setNombre(documentSnapshot.getString("nombre"));
                            usuario.setTipo_usuario(documentSnapshot.getString("Tipo de usuario"));
                            usuario.setAlmacenamiento(Integer.parseInt(documentSnapshot.getString("Espacio de almacenamiento")));
                        }
                    }
                }
        );
    return  usuario;
    }


    String idusuario;
    public void Registro (final Usuario usuario){
        if (!usuario.getEmail().equals("") && !usuario.getPassword().equals("")){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        idusuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        //subirArchivoPutStream(idusuario);
                        crearColeccionFireStore(usuario);
                        String data = "Bienvenido a tu cuenta free";
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference();
                        StorageReference carpeRoot = storageReference.child("users/").child(idusuario).child("Bienvenido.txt");
                        UploadTask uploadTask = carpeRoot.putBytes(data.getBytes());
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ingresoExitoso2(usuario.getEmail(), usuario.getNombre(),usuario.getApellido(),usuario.getTipo_usuario(),usuario.getAlmacenamiento());
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginyRegistro.this, "no se subio", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        /*Log.d("infoApp", carpeRoot.toString());
                        ingresoExitoso(inputEmail.getText().toString());*/
                    }else {
                        mostrarError();
                    }
                }
            });
        }
    }



        public void inicioDeSesionConGoogle() {
            GoogleSignInOptions googleconf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient googleClient = GoogleSignIn.getClient(getApplicationContext(),googleconf);
            googleClient.signOut();

            startActivityForResult(googleClient.getSignInIntent(),google_sign_in);
        }

    public void subirArchivoPutStream (String id){
        int permission = ContextCompat.checkSelfPermission(LoginyRegistro.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(externalStoragePublicDirectory, "imagen1.png");
            try {
                InputStream stream = new FileInputStream(file);
                storageReference.child(id + "/subido.jpg").putStream(stream)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("infoApp", "subida exitosa");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("infoApp", "error");
                            }
                        });
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1) {
            subirArchivoPutStream(idusuario);
        }
    }

    public void session(){
        SharedPreferences pref = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String email = pref.getString("email",null);
        if (email != null){
            ingresoExitoso(email);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == google_sign_in){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                ingresoExitoso2(account.getEmail(), account.getGivenName(),account.getFamilyName(),"cuenta basica",200);
                            }else {
                                mostrarError();
                            }
                        }
                    });
                }
            } catch (ApiException e) {
                mostrarError();
            }
        }
    }

    public void mostrarError(){
        Toast.makeText(getApplicationContext(),"Error: el usuario no se pudo autenticar correctamente",
                Toast.LENGTH_SHORT).show();
    }

    public void ingresoExitoso(String inputEmail){
        Bundle params = new Bundle();
        params.putString("email",inputEmail);
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        i.putExtras(params);
        startActivity(i);
    }
    public void crearColeccionFireStore(Usuario usuario){

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", usuario.getNombre() + " " + usuario.getApellido() );
        user.put("Tipo de usuario",usuario.getTipo_usuario() );
        user.put("Espacio de almacenamiento", usuario.getAlmacenamiento());
        user.put("Correo electrónico", usuario.getEmail());
        user.put("archivo",usuario.getArchivos_privados());

        Map<String, Object> aPrivados = new HashMap<>();
        for(int i=0;i<usuario.getArchivos_privados().size();i++) {
            aPrivados.put("archivo " + i+1 ,usuario.getArchivos_privados().get(i) );
        }


        FirebaseFirestore dbF = FirebaseFirestore.getInstance();
        DocumentReference usersColeccion = dbF.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        usersColeccion.set(user);
        usersColeccion.collection("archivos privados").add(aPrivados);


    }

    public void ingresoExitoso2(String inputEmail, String nombres, String apellidos, String tipoCuenta, int almacenamiento){
        Bundle params = new Bundle();
        params.putString("email",inputEmail);
        params.putString("nombres",nombres);
        params.putString("apellidos",apellidos);
        params.putString("tipoCuenta",tipoCuenta);
        params.putInt("almacenamiento",almacenamiento);
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        i.putExtras(params);
        startActivity(i);
    }




}