package com.example.socialhive.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialhive.R
import com.example.socialhive.adapter.PostRecyclerAdapter
import com.example.socialhive.databinding.ActivityHomeBinding
import com.example.socialhive.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var postAdapter : PostRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db = Firebase.firestore

        postArrayList = ArrayList<Post>()
        getData()

        binding.recyclerViewHomeActivity.layoutManager = LinearLayoutManager(this); // Layout'un görünüm şeklini yazdık.
        postAdapter = PostRecyclerAdapter(postArrayList); // Post listesini Adapter'a gönderdik.
        binding.recyclerViewHomeActivity.adapter = postAdapter; // RecyclerView'ı Adapter'a bağladık
    }

    // GET TO POSTS
    private fun getData() {

        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null) { // Hata Varsa !
                Toast.makeText(this@HomeActivity, error.localizedMessage, Toast.LENGTH_LONG).show();

            } else { // Hata Yoksa !
                if (value != null) { // Değer null değilse !
                    if (!value.isEmpty) { // Value'nin içi de boş değilse !

                        val documents = value.documents; // Döküman listesini aldım.

                        postArrayList.clear(); // Bu dizi temizlenir ve boş bir şekilde başlar.
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR")) // Türkçe tarih ve saat formatı için Locale'ı Türkçe olarak belirttik.

                        for (document in documents) {
                            val comment = document.get("comment") as String; // Casting
                            val userEmail = document.get("userEmail") as String;
                            val downloadUrl = document.get("downloadUrl") as String;
                            val dateTime = document.getTimestamp("date"); // Timestamp'ı doğru şekilde alıyoruz

                            val myDateTime = dateFormat.format(dateTime!!.toDate());

                            val post = Post(userEmail, comment, downloadUrl, myDateTime); // Bu bilgileri ilgili sınıfa gönderdik.
                            postArrayList.add(post); // Ve oluşturduğumuz post bilgisini, listeye aktardık.
                        }

                        postAdapter.notifyDataSetChanged(); // Veriler GÜncellendi, Yeni verileri göster dedik.
                    }
                }
            }
        }
    }


    // ** MENU IS CREATE !
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater;
        menuInflater.inflate(R.menu.social_hive_menu, menu); // Mevcut menü ile, oluşturduğumuz menüyü bağladık.

        return super.onCreateOptionsMenu(menu)
    }

    // ** BINDING ITEMS WITH MENU !
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.user_logout) { // Menüdeki tıklanan item ile, benim istediğim item id'si aynı ise..
            auth.signOut(); // çıkış yapma işlemi.

            val intent = Intent(this@HomeActivity, MainActivity::class.java);
            startActivity(intent);
            finish(); // Bu aktiviteye artık geri dönülemez !

            Toast.makeText(this@HomeActivity,"Başarılı bir şekilde çıkış yapıldı !",Toast.LENGTH_LONG).show();
        } else if (item.itemId == R.id.add_post) {

            val intent = Intent(this@HomeActivity, UploadActivity::class.java);
            startActivity(intent);
        } else if (item.itemId == R.id.user_own_posts) {

            val intent = Intent(this@HomeActivity, UserPostsActivity::class.java);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item)
    }

}