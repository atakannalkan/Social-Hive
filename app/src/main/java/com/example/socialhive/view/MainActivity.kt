package com.example.socialhive.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.socialhive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth; // Üyelik için firebase tanımlandı.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth;

        val currentUser = auth.currentUser;
        if(currentUser != null) { // Eğer mevcut kullanıcı zaten giriş yaptıysa.
            val intent = Intent(this@MainActivity, HomeActivity::class.java);
            startActivity(intent);
            finish(); // Bu aktiviteye artık geri dönülemez !
        }

        findViewById<ImageView>(R.id.instagram_icon).setOnClickListener { redirectToInstagram(); }
        findViewById<ImageView>(R.id.github_icon).setOnClickListener { redirectToGitHub(); }
        findViewById<ImageView>(R.id.website_icon).setOnClickListener { redirectToWebsite(); }

    }

    // ** REGISTER BUTTON !
    fun registerClicked(view: View)
    {
        val email = editTextEmail.text.toString();
        val password = editTextPassword.text.toString();

        if(email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this@MainActivity, "Email ve Şifre alanı boş bırakılamaz !", Toast.LENGTH_LONG).show();

        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // İşlem başarılıysa !
                    val intent = Intent(this@MainActivity, HomeActivity::class.java);
                    startActivity(intent);
                    finish(); // Bu aktiviteye artık geri dönülemez !

                    Toast.makeText(this@MainActivity, "Başarıyla Kayıt Olundu !", Toast.LENGTH_LONG).show();

                } else { // İşlem Hatalıysa !
                    Toast.makeText(this@MainActivity, "HATA: ${task.exception}", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // ** LOGIN BUTTON !
    fun loginClicked(view: View)
    {
        val email = editTextEmail.text.toString();
        val password = editTextPassword.text.toString();
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this@MainActivity, "HATA: Email ve Şifre alanı boş bırakılamaz !", Toast.LENGTH_LONG).show();

        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {task ->
                if (task.isSuccessful) { // İşlem başarılıysa !
                    val intent = Intent(this@MainActivity, HomeActivity::class.java);
                    startActivity(intent);
                    finish() // Bu aktiviteye artık geri dönülemez !

                    Toast.makeText(this@MainActivity, "Başarıyla Giriş Yapıldı !", Toast.LENGTH_LONG).show();

                } else { // İşlem Hatalıysa !
                    Toast.makeText(this@MainActivity, "HATA: Email veya Parola hatalı !", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    // ** REDIRECTING OPERATIONS !
    private fun redirectToInstagram()
    {
        // "Intent.ACTION_VIEW" ile, Intent nesnesinin belirtilen URI'yi açması gerektiğini belirttik.
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/atakann_alkan/"));

        startActivity(intent);
    }
    private fun redirectToGitHub()
    {
        // "Intent.ACTION_VIEW" ile, Intent nesnesinin belirtilen URI'yi açması gerektiğini belirttik.
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/atakannalkan"));

        startActivity(intent);
    }
    private fun redirectToWebsite()
    {
        // "Intent.ACTION_VIEW" ile, Intent nesnesinin belirtilen URI'yi açması gerektiğini belirttik.
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://atakanalkan.com/"));

        startActivity(intent);
    }

}