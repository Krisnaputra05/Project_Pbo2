# Projek_PBO-API
## SISTEM PEMBAYARAN SUBSCRIPTION SEDERHANA
Program ini bertujuan untuk mengembangkan backend API untuk Sistem Pembayaran Subscription Sederhana yang akan memberikan respons dalam format JSON. API ini mendukung beberapa metode permintaan, yaitu:
*GET* : untuk mendapatkan daftar atau detail data dari entitas.
*POST* : untuk membuat entitas baru.
*PUT* : untuk memperbarui data dari entitas yang ada.
*DELETE* : untuk menghapus data dari entitas.
Data yang digunakan dalam Sistem Pembayaran Subscription Sederhana ini akan disimpan dalam database SQLite. Selain itu, API yang telah dibuat akan diuji menggunakan aplikasi Postman.

## Menggunakan Program
## Request Method Get
##### CUSTOMER
 `GET/customers`
Untuk mendapatkan semua user, dapat menggunakan url seperti berikut.  `http://localhost:9023/customers`
Hasil eksekusi url diatas dapat dilihat sebagai berikut.

![WhatsApp Image 2024-06-21 at 15 21 21_c678dc4a](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/6e622d88-aba8-4627-8751-5ecf315cb08d)

`GET/customers/{id}`
 Untuk mendapatkan user berdasarkan id, dapat menggunakan url sebagai berikut.
`http://localhost:9023/customers/2`
Hasil eksekusi dapat dilihat sebagai berikut.
 
![WhatsApp Image 2024-06-21 at 15 22 00_ce042ea2](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/ec8d0f42-4e71-4e6a-b33e-b472ac16f8f6)

`GET/customers/{id}/cards`
Untuk mendapatkan user berdasarkan kartu kredit/debit milik pelanggan, dapat menggunakan url sebagai berikut.
`http://localhost:9023/customers/1/cards`
Hasil eksekusi dapat dilihat sebagai berikut.
 
![WhatsApp Image 2024-06-21 at 15 27 46_17750612](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/96d8b664-932d-4b7d-981b-e9c2bfcbc09c)

`GET/customers/{id}/subscriptions`
Untuk mendapatkan user berdasarkan semua subscriptions milik pelanggan, dapat menggunakan url sebagai berikut.
`http://localhost:9023/customers/1/subscriptions`
Hasil eksekusi dapat dilihat sebagai berikut.
 
![WhatsApp Image 2024-06-21 at 15 30 53_797a0167](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/291d7df1-4271-4a74-aeaf-51a478dd1c91)

##### SUBSCRIPTIONS
 `GET /subscriptions`
 Untuk mendapatkan daftar semua subscriptions dapat menggunakan url berikut `http://localhost:9023/subscriptions`
Hasil eksekusi dapat dilihat sebagai berikut
 
 ![WhatsApp Image 2024-06-21 at 15 32 28_6112fa35](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/2b064444-158a-4d1a-b29b-e6c73963f720)
 
`GET /subscriptions/{id}`
  Untuk mendapatkan Informasi subscriptions,customer:id, first_name,last_name,subscription_items:quantity,amount,item:id,name,price,type dapat menggunakan url berikut `http://localhost:9023/subscriptions/1`
Hasil eksekusi dapat dilihat sebagai berikut
 
  ![WhatsApp Image 2024-06-21 at 15 34 56_51f5ec28](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/cd9da997-0c5d-46ca-8abc-f1b7b1d4df28)
  
 ##### ITEMS
 `GET /items` 
 Untuk mendapatkan  daftar semua produk dapat menggunakan url berikut `http://localhost:9023/items`
Hasil eksekusi dapat dilihat sebagai berikut
 
![WhatsApp Image 2024-06-21 at 15 39 16_f8587418](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/6b9dcacb-e49b-4576-816c-7deb8c0dbaa1)

`GET /items/{id}`
 Untuk mendapatkan  informasi produk dapat menggunakan url berikut `http://localhost:9023/items`
Hasil eksekusi dapat dilihat sebagai berikut
 
 ![WhatsApp Image 2024-06-21 at 15 39 37_5fbb3323](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/7cfa0436-2642-4bc3-a71b-2ed6f0338189)

### Request Method POST
 `POST /items`
 
![WhatsApp Image 2024-06-21 at 15 42 46_a3af8498](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/2f503add-ac18-4576-a06b-9790d819f204)
Pada contoh diatas, '/items' dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan output seperti berikut :

`1 rows inserted!`

 `POST /subscriptions`
 
 ![WhatsApp Image 2024-06-21 at 15 48 45_fc971f7e](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/1a20baf0-fdaf-422a-ab12-56c90b9e4759)
 Pada contoh diatas, '/subscriptions' dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan output seperti berikut :

`1 rows inserted!`

### Request Method PUT
 `PUT /items/{id}`
 
![WhatsApp Image 2024-06-21 at 15 58 00_0487047b](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/18d3818d-0589-4fad-a187-a49f73005374)
Pada contoh diatas '/items' dan '/5' dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan output sebagai berikut :

`1 rows updated!`

### Request Method DELETE
 `DELETE /items/{id}`
 
 ![WhatsApp Image 2024-06-21 at 15 59 40_4ef9ecea](https://github.com/KmngDamar/Project_Pbo1/assets/147182969/48724f90-ba46-4fe7-98df-9ace83e675a6)
Pada contoh diatas, '/items' dan '/5' dapat diganti sesuai dengan kebutuhan Anda. Jika dieksekusi, maka akan menghasilkan output sebagai berikut :

`1 rows deleted!`
# PENUTUP
SEKIAN PENJELASAN DARI KAMI SEMOGA MUDAH DIMENGERTI✨

### I GEDE KRISNA PRADNYA PUTRA (230555023)
### I GUSTI KOMANG DAMAR ARI SUPUTRA (2305551165)
