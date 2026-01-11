# StockWise V 1.0 - Tutorial Penggunaan

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

### Login ke Sistem
1. Jalankan aplikasi
2. Masukkan username dan password
3. Klik "Login"

**Akun Default**:
- Username: admin
- Password: admin123 (atau sesuai yang ada di database)

### Registrasi Pengguna Baru
1. Klik "Register" di halaman login
2. Isi username, password, dan konfirmasi password
3. Pilih role (ADMIN atau STAFF)
4. Klik "Register"

### Dashboard
Setelah login, Anda akan melihat dashboard dengan:
- Ringkasan total produk
- Ringkasan transaksi terbaru
- Navigasi ke menu lainnya

### Manajemen Produk
1. Klik menu "Products" di header
2. **Tambah Produk Baru**:
   - Klik "Add Product"
   - Isi Product ID, Category, Name, Price, Stock
   - Klik "Save"
3. **Edit Produk**:
   - Pilih produk dari tabel
   - Klik "Edit"
   - Ubah data yang diperlukan
   - Klik "Update"
4. **Hapus Produk**:
   - Pilih produk dari tabel
   - Klik "Delete"
   - Konfirmasi penghapusan
5. **Import CSV**:
   - Klik "Import CSV"
   - Pilih file CSV dengan format: product_id,product_category,product_name,price,stock
6. **Export CSV**:
   - Klik "Export CSV"
   - Pilih lokasi penyimpanan file

### Manajemen Transaksi
1. Klik menu "Transactions" di header
2. **Tambah Transaksi**:
   - Klik "Add Transaction"
   - Pilih Product ID
   - Masukkan Quantity
   - Pilih Type (IN untuk masuk, OUT untuk keluar)
   - Klik "Save"
3. **Lihat Riwayat Transaksi**:
   - Semua transaksi akan ditampilkan dalam tabel
   - Filter berdasarkan tanggal atau produk jika diperlukan

### Manajemen Pengguna (khusus ADMIN)
1. Klik menu "Users" di header (jika role ADMIN)
2. Lihat daftar pengguna
3. Edit role pengguna jika diperlukan

## Struktur Database

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
- Pastikan format CSV benar: product_id,product_category,product_name,price,stock
- Header harus ada di baris pertama
- Data harus sesuai tipe (price dan stock harus angka)

## Dukungan
Untuk pertanyaan atau masalah, hubungi developer: ZuhairX
