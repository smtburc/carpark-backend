# Otopark Otomasyonu

## Projeyi Çalıştırma

### `java 17 ile yazılmıştır`

CarParkApplication Sınıfı çalıştırılarak proje ayağa kaldırılabilir.

[http://localhost:8086](http://localhost:8086)
ile backend ayağa kalkmaktadır

Proje aynı zamanda Dockerize edilmiştir.

### `mvn install -DskipTests`

Bu komut ile proje build edilir.

### `docker build --no-cache -t otopark-backend .`

Bu Komut ile docker image oluşturulabilir

### `docker run -d --name otopark-backend  -p 8086:8086 otopark-backend`

Bu Komut ile proje docker container içerisinde çalışacaktır

[http://localhost:8086](http://localhost:8086)
ile backend ayağa kalkmaktadır

## İstek

-         Tüm işlemler REST servisleri üzerinden ilerleyecek.

-         Servis kullanımlarına güvenlik kontrolleri yapılacaktır.

-         Park yeri bilgileri yönetimi olmayacaktır. Otoparkın kapasitesi 50 birimlik  araç alabilecek büyüklükte olacaktır.

-         Araç kategorileri A, B ve C olmak üzere 3 çeşit olacaktır.

                - A kategorisinde: Motosiklet, sedan, hatcback araçları olacaktır.

                - B kategorisinde: Jeep, SUV araçları olacaktır.

                - C kategorisinde: Minibüs, kamyonet araçları olacaktır.

-         A tipinden 1 kat, B tipinden 1.2 kat, C tipinden 2 kat ücret alınacaktır.

-         Baz ücret sistem tarafından yönetilebilir olacaktır.

-         Araçların park süreleri için 5 gün sonrası için bir alt kademeye düşürülecektir.

-         Araçların park etmesi gereken yeri sistem otomatik hesaplayacaktır. Park edecekleri alan hesaplaması, ücret katsayısına göre yapılacaktır. (Örn1)

-         Üyeler için %20 indirim uygulanacaktır.

-         Ödeme işlemi yazılmayacak. Kodlama sırasında “bu kısımda ödeme işlemi yapılmaktadır” şeklinde açıklama girilmesi yeterlidir.

-         Araç yerleştirmesi en optimum şekilde yapılacaktır


## Notlar

-       Güvenlik Jwt Token ile gerçekleştirilmiştir.

-       Giriş Bilgileri:    Kullanıcı adı: username, Şifre: password 

-       Hatalar Controller Advice ile ön yüze 400 Hata Kodu ile dönmektedir. Ön Taraf Bu Hata Koduna göre Modal Göstermektedir.

-       Projede Database Kullanılmamıştır. Veriler proje ayağa kalkarken oluşturulmuştur. Buna rağmen projedeki domain yapısı ve kullanım
        database varmış gibi standartlara uygun gerçekleştirilmiştir.

-       Repository Katmanında ApplicationScope Kullanılmasının nedeni Veritabanı Bağlantısı Olmamasıdır.

-       Park Alanı Düz 50 birim uzunlukta olduğu varsayılmıştır. Burada Sıralı olarak park edilen araçlar görünmektedir.
        Eğer bir araba çıkış yaparsa oralarda boşluklar oluşabilir. Yeni gelen araç için araç uzunluğu ve sonraki ihtimaller 
        recursive bir algoritma ile hesaplanarak en minimum kayıp olacak şekilde yerleştirilmektedir.
        
-       Araç yerleştirme algoritması dinamik olarak yazılmıştır. Araç uzunlukları değişse bile algoritma çalışmaktadır.

-       Service sınıfları ve bazı iş kuralları için Test Metodları da yazılmıştır.
