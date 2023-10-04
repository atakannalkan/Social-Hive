package com.example.socialhive.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.socialhive.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>; // Intent ile gidip bir veri alacağımız değişken.
    private lateinit var permissionLauncher : ActivityResultLauncher<String>;
    var selectedPicture : Uri? = null;
    private lateinit var auth : FirebaseAuth;
    private lateinit var firestore : FirebaseFirestore;
    private lateinit var storage : FirebaseStorage;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        registerLauncher();

        auth = Firebase.auth;
        firestore = Firebase.firestore;
        storage = Firebase.storage;
    }


    fun upload(view: View) {
        // Burada 3 aşama var 1- Resmi Storage'e Yükleme 2- Yüklenen Resmin URL bilgisini Alma 3- Verileri Firestroe'a Yüklemek.

        if (editTextComment.text.isNullOrBlank()) {
            Toast.makeText(this@UploadActivity,"HATA!: Açıklama alanı boş bırakılamaz !", Toast.LENGTH_LONG).show();
        } else {
            // Universal Unique ID
            val uuid = UUID.randomUUID() // Bu sınıf bize eşsiz bir değer veriyor.
            val imageName = "${uuid}.jpg";

            val reference = storage.reference; // Firebasedeki Storage.
            val imageReference = reference.child("images/${imageName}"); // Storage'de yeni bir klasör oluşturup, içine resmi gönderiyoruz.

            if (selectedPicture != null) {
                val imageSize = contentResolver.openFileDescriptor(selectedPicture!!, "r")!!.statSize; // Bize "byte" türünden bir size döner.

                if (imageSize / (1024 * 1024).toDouble() > 1.5) // "byte" bilgisini "megabyte"a çevirdik.
                {
                    Toast.makeText(this@UploadActivity,"HATA!: Resmin boyutu 1.5 Megabyte'ı geçemez !", Toast.LENGTH_LONG).show();

                } else {
                    imageReference.putFile(selectedPicture!!).addOnSuccessListener { // Dosya Yükleme Başarılıysa !

                        val uploadPictureReference = storage.reference.child("images").child(imageName); // Yüklenen resmin bilgisini aldık (downloadUrl özelliğini almak için)
                        uploadPictureReference.downloadUrl.addOnSuccessListener {
                            val downloadUrl = it.toString(); // Yüklenen resmin url bilgisini aldık.

                            // Documentation: "https://firebase.google.com/docs/firestore/quickstart?hl=en&authuser=0#kotlin+ktx"
                            if (auth.currentUser != null) {
                                val postMap = hashMapOf<String, Any>()
                                postMap.put("downloadUrl", downloadUrl);
                                postMap.put("userEmail", auth.currentUser!!.email!!)
                                postMap.put("comment", editTextComment.text.toString());
                                postMap.put("date", Timestamp.now())

                                firestore.collection("Posts")
                                    .add(postMap)
                                    .addOnSuccessListener {
                                        val intent = Intent(this@UploadActivity, HomeActivity::class.java);
                                        startActivity(intent)
                                        finish();

                                        Toast.makeText(this@UploadActivity, "Gönderi başarıyla paylaşıldı !", Toast.LENGTH_LONG).show();

                                    }.addOnFailureListener {
                                        Toast.makeText(this@UploadActivity, "Hata!: ${it.localizedMessage}", Toast.LENGTH_LONG).show();
                                    }
                            }
                        }
                    }.addOnFailureListener { // İşlem Hatalıysa !
                        Toast.makeText(this, "Hata!: ${it.localizedMessage}", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }



    fun selectImage(view: View) {
        // Daha önce izin alınmadıysa.
        if (ContextCompat.checkSelfPermission(this@UploadActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Rationale (Gösterileceğine Android karar verir)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Snackbar.make(view, "Galeriye erişim izni gerekli !", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", View.OnClickListener { // LENGTH_INDEFINITE: Kullanıcı butona basana kadar bekle.
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE); // İznimizi istiyoruz.
                }).show();

            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE); // İznimizi istiyoruz.
            }

        } else { // Zaten izin aldıysak
            // ACTION_PICK: Oraya gidip bir veri alacağız.
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery); // Galeriye gidiyorum.
        }
    }

    private fun registerLauncher() {

        // Activiteye bir veri almak için gidiyoruz.
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK)  { // Kullanıcı görsel seçtiyse.
                val intentFromResult = result.data;
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data;
                    selectedPicture?.let {
                        imageView.setImageURI(it);
                    }
                }
            }
        }

        // İznimizi isteyeceğiz
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) { // İzin verildiyse
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery); // Galeriye gidiyorum.

            } else { // İzin verilmediyse
                Toast.makeText(this@UploadActivity,"Resim seçmek için galeriye erişim izni gerekli !", Toast.LENGTH_LONG).show();
            }
        }
    }
}