# Social Hive - Mobil Gönderi Paylaşım Uygulaması

<p align="center">
    <img alt="Uygulama Resmi" width="300px" src="https://r.resimlink.com/yesbWGK0RzQp.png">
</p>

## Uygulamaya Genel Bakış
Uygulama Kotlin programlama dili kullanılarak geliştirilmiş bir Android mobil uygulamasıdır. Kullanıcılar, uygulamayı indirerek kayıt olabilir, giriş yapabilir, gönderi(post) paylaşabilir ve hem kendi gönderilerini hem de diğer kullanıcıların gönderilerini çevrimiçi olarak görüntüleyebilir.

Projeyi genel olarak incelediğimizde, uygulamanın genel yapısı **"Instagram"** uygulamasından esinlenilerek geliştirilmiştir. Bunun dışında, uygulamanın nasıl bir yazılım mimarisi ile geliştirildiği ve nasıl kullanılacağı ile ilgili detaylı bilgi aşağıda verilmiştir.

## Kullanılan Teknolojiler
Uygulama **Android Studio** ortamında, **Kotlin** programlama dili kullanılarak geliştirilmiştir. Veri Tabanı kısmında ise, SQL Veritabanlarına göre kullanımı daha kolay olan **"Firebase"** Veri Tabanı kullanılmıştır. Bunun nedeni; Firebase ile geleneksel SQL Veritabanlarında olduğu gibi SQL sorguları yazmak zorunda olmadığımız, ve Mobil Geliştirme alanında Veri Tabanı işlemlerinin(Örn: Sunucu yönetimi, Analiz, Authentication vb..) çok daha kolay bir şekilde gerçekleştiriliyor olmasıdır.

Bu nedenle uygulamada Firebase Veri Tabanı tercih edilmiş olup, **Authentication, Firestore Database** ve **Stroage** bölümleri kullanılmıştır. Bunun dışında Firebase'deki görselleri kolay bir şekilde ön yüze aktarmak için de **[Picasso](https://square.github.io/picasso/)** adındaki framework, uygulama içine dahil edilerek kullanılmıştır.


## Uygulamayı Çalıştırmak
Uygulamayı sıfırdan çalıştırmak için ilgili dosyaları indirip açtıktan sonra, yapılması gereken ilk işlem; Firebase Veri Tabanı ile Uygulama arasındaki bağlantıyı yapmaktır.

Bunun için; Firebase hesabınızdan indirmiş olduğunuz **"google-services.json"** adlı dosyayı, uygulamanın **"Project"** görünümünden **"app"** adlı klasörün içerisine aktarmanız gerekmektedir. 
Bu sayede kullanmış olduğunuz **"Firebase"** Veri Tabanı ile uygulama arasındaki bağlantı yapılmış olacaktır.


## Tanıtım Videosu ve APK Versiyonu
Uygulamanın tanıtım videosuna ve son sürüm APK dosyasına aşağıdaki bağlantılardan ulaşabilirsiniz:

- Uygulamanın Tanıtım Videosu: **[Social Hive Tanıtım Videosu](https://youtu.be/EhNzU07TraM)**  
- Uygulamanın APK Versiyonu: **[Social Hive APK İndir](https://drive.google.com/drive/folders/1fgkq-8pryWihTXgWKZnXtRIlnaNKhwvt?usp=drive_link)**
