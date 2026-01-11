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
- **Via Command Line**: `java --module-path target\lib --add-modules javafx.controls,javafx.fxml -jar target\stockwise-1.0-SNAPSHOT.jar`
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

<img width="675" height="383" alt="image" src="https://github.com/user-attachments/assets/1b6ace6b-f031-4eec-8234-8b5d32e403c0" />

### Dashboard
Setelah login, Anda akan melihat dashboard dengan:
- Ringkasan total produk
- Ringkasan transaksi terbaru
- Navigasi ke menu lainnya

<img width="906" height="1023" alt="image" src="https://github.com/user-attachments/assets/baccf035-327d-4200-abd1-0b89bc1e01b7" />

### Manajemen Produk
1. Klik menu "Products" di header
2. **Tambah Produk Baru**:
   - Klik "Add Product"
   - Isi Product Name, Category, Price
   - Klik "Add"
3. **Edit Produk**:
   - Pilih produk dari tabel
   - Ubah data yang diperlukan
   - Klik "Update"
4. **Hapus Produk**:
   - Pilih produk dari tabel
   - Klik "Delete Selected"
4. **Hapus Semua Produk**:
   - Pilih produk dari tabel
   - Klik "Delete All"
   - Konfirmasi penghapusan

<img width="1045" height="1000" alt="image" src="https://github.com/user-attachments/assets/898232d1-f1c4-4d23-aaa7-614204d1c19f" />

### Manajemen Transaksi
1. Klik menu "Transactions" di header
2. **Tambah Transaksi**:
   - Klik "Add Transaction"
   - Pilih Product Name
   - Pilih Type (IN untuk masuk, OUT untuk keluar)
   - Masukkan Quantity
   - Klik "Submit"
3. **Lihat Riwayat Transaksi**:
   - Semua transaksi akan ditampilkan dalam tabel
4. **Hapus transaksi**:
   - Pilih transaksi dari tabel
   - Klik "Delete Selected"
4. **Hapus Semua transaksi**:
   - Pilih transaksi dari tabel
   - Klik "Delete All"
   - Konfirmasi penghapusan
6. **Export CSV**:
   - Klik "Export CSV"
   - Pilih lokasi penyimpanan file

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

## Dukungan
Untuk pertanyaan atau masalah, hubungi developer: ZuhairX
