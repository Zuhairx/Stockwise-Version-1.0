# StockWise V 1.0 - Tutorial Penggunaan

<div align="center">
<img width="256" height="256" alt="iconAplikasi" src="https://github.com/user-attachments/assets/22b9557a-f255-467d-96a6-2ad7f1130679" />
</div>

## Persiapan Awal

### 1. Persyaratan Sistem
- Java 21 terinstall
- MySQL Server terinstall
- Maven (untuk build dari source)

### 2. Setup Database 
1. Buat database baru di MySQL dengan nama `stockwise2_db`
2. Import file `database/stockwise2_db.sql` ke dalam database tersebut
3. Pastikan koneksi database sudah dikonfigurasi dengan benar (lihat `DBConnection.java`)

### 3. Menjalankan Aplikasi
- **Via Batch File**: Jalankan `run.bat` (untuk Windows)
- **Via Terminal**: `mvn clean install mvn clean javafx:run`
- **Via Maven**: `mvn clean compile exec:java`

## Panduan Penggunaan

<img width="515" height="791" alt="image" src="https://github.com/user-attachments/assets/f702cfd8-ef4a-438a-9e19-4e1f0aedc391" />

### Login ke Sistem
1. Jalankan aplikasi
2. Masukkan username dan password
3. Klik "Login"

<img width="515" height="845" alt="image" src="https://github.com/user-attachments/assets/8ec6cb33-c22a-4d38-b56b-a96e29accaa5" />

### Registrasi Pengguna Baru
1. Klik "Register" di halaman login
2. Isi username, password, dan konfirmasi password
3. Pilih role (ADMIN atau STAFF)
4. Klik "Register"

<img width="670" height="442" alt="image" src="https://github.com/user-attachments/assets/0b637ffe-6ef5-4ab8-b498-b96bfa7f840c" />


### Dashboard
Setelah login, Anda akan melihat dashboard dengan:
- Ringkasan total produk
- Ringkasan total transaksi
- Navigasi ke menu lainnya

<img width="902" height="1026" alt="image" src="https://github.com/user-attachments/assets/4db2e9b6-84f6-401a-8e06-1e9429658833" />

### Manajemen Produk
1. Klik menu "Products" di header
2. **Tambah Produk Baru**:
   - Isi Product Name, Category, Price
   - Klik "Add"
3. **Edit Produk**:
   - Pilih produk dari tabel
   - Ubah data yang diperlukan
   - Klik "Update"
4. **Lihat Riwayat Product**:
   - Semua product akan ditampilkan dalam tabel
5. **Hapus Produk**:
   - Pilih produk dari tabel
   - Klik "Delete Selected"
6. **Hapus Semua Produk**:
   - Pilih produk dari tabel
   - Klik "Delete All"
   - Konfirmasi penghapusan
7. **Import CSV**:
   - Klik "Import CSV"
   - Pilih file CSV yang berisi template data produk
   - Data akan diimpor dan diperbarui jika produk sudah ada
8. **Export CSV**:
   - Klik "Export CSV"
   - Pilih lokasi penyimpanan file

*notes: tombol clear field -> untuk menghapus semua teks field yang ada

<img width="1043" height="1020" alt="image" src="https://github.com/user-attachments/assets/8e18fc6a-b18e-4d80-a102-62ec32bbddd2" />

### Manajemen Transaksi
1. Klik menu "Transactions" di header
2. **Tambah Transaksi**:
   - Pilih Product Name
   - Pilih Type (IN untuk masuk, OUT untuk keluar)
   - Masukkan Quantity
   - Klik "Add"
3. **Edit Transaksi**:
   - Pilih transaksi dari tabel
   - Ubah data yang diperlukan
   - Klik "Update"
4. **Lihat Riwayat Transaksi**:
   - Semua transaksi akan ditampilkan dalam tabel
5. **Hapus transaksi**:
   - Pilih transaksi dari tabel
   - Klik "Delete Selected"
6. **Hapus Semua transaksi**:
   - Pilih transaksi dari tabel
   - Klik "Delete All"
   - Konfirmasi penghapusan
   - **Catatan**: Menghapus semua transaksi akan mereset stok semua produk menjadi 0
7. **Import CSV**:
   - Klik "Import CSV"
   - Pilih file CSV yang berisi data template transaksi
   - Data akan diimpor dan diperbarui jika transaksi sudah ada
8. **Export CSV**:
   - Klik "Export CSV"
   - Pilih lokasi penyimpanan file

<img width="1048" height="910" alt="image" src="https://github.com/user-attachments/assets/e3cdb879-862f-4a3a-8f40-e60508efe57b" />

### Manajemen Pengguna (khusus ADMIN) 
1. Klik menu "Users" di Dashboard (jika role ADMIN)
2. Lihat tabel daftar pengguna 
3. Edit username & role pengguna jika diperlukan dari dalam tabel
4. klik "refresh" jika data pengguna belum terupdate
5. klik "Delete" untuk mengahapus pengguna

## Struktur Database
<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/ef2e0c18-46aa-4b9f-b407-1f5bdec9446b" />

### Tabel `products`
- `product_id` (VARCHAR): ID unik produk
- `product_category` (VARCHAR): Kategori produk
- `product_name` (VARCHAR): Nama produk
- `price` (INT): Harga produk
- `stock` (INT): Jumlah stok
- `created_at` (TIMESTAMP): Waktu pembuatan
- `updated_at` (TIMESTAMP): Waktu terakhir update

### Tabel `transactions`
- `transaction_id` (VARCHAR): ID unik transaksi
- `product_id` (VARCHAR): ID produk terkait
- `quantity` (INT): Jumlah produk dalam transaksi
- `type` (ENUM): Tipe transaksi (IN/OUT)
- `transaction_date` (TIMESTAMP): Tanggal transaksi

### Tabel `users`
- `id` (INT): ID unik pengguna
- `username` (VARCHAR): Username
- `password` (VARCHAR): Password terenkripsi
- `role` (ENUM): Role pengguna (ADMIN/STAFF)
- `created_at` (TIMESTAMP): Waktu pembuatan akun

## Troubleshooting

### Tidak Bisa Koneksi Database
- Pastikan MySQL server sedang berjalan
- Periksa konfigurasi di `DBConnection.java`
- Pastikan database `stockwise2_db` sudah dibuat dan di-import

### Aplikasi Tidak Bisa Dijalankan
- Pastikan Java 21 terinstall
- Pastikan semua dependencies sudah didownload (jalankan `mvn clean install`)
- Periksa module path untuk JavaFX

### Error Saat Import CSV 
- Pastikan format CSV Product benar: no, product_id,product_category,product_name,price,stock
- Pastikan format CSV Transaction benar: no,product_id,product_name,type,quantity,transaction_date
- Header harus ada di baris pertama
- Data harus sesuai tipe (price dan stock harus angka)

## Dukungan
Untuk pertanyaan atau masalah, hubungi developer: ZuhairX
