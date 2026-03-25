# Cara Menjalankan Sistem Pengumpulan Laporan Praktikum

## 📋 Persyaratan
- **Java**: JDK 8+
- **MySQL**: 5.7+ (service running)
- **MySQL User**: `pbo_user` dengan password `Pbo!Praktikum2026` (sudah di-setup)
- **Library**: `lib/mysql-connector-java-x.x.x.jar` (sudah tersedia)

## 🛠️ Setup Awal (One-Time)

### 1. Pastikan MySQL Service Running
```bash
sudo systemctl status mysql
# Jika tidak running:
sudo systemctl start mysql
```

### 2. Buat User MySQL (jika belum ada)
```bash
mysql -u root -p
# Di MySQL prompt:
CREATE USER 'pbo_user'@'localhost' IDENTIFIED BY 'Pbo!Praktikum2026';
GRANT ALL PRIVILEGES ON db_laporan_praktikum.* TO 'pbo_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Setup Database & Schema
```bash
cd /home/potydev/tugas/Tugas-1-PBO-DapotMatthewTampubolon
mysql -u pbo_user -pPbo!Praktikum2026 db_laporan_praktikum < database.sql
```

### 4. Compile Aplikasi
```bash
cd /home/potydev/tugas/Tugas-1-PBO-DapotMatthewTampubolon
javac -cp "lib/*" -d bin $(find src -name "*.java")
```

## ▶️ Menjalankan Aplikasi

```bash
cd /home/potydev/tugas/Tugas-1-PBO-DapotMatthewTampubolon
java -cp "bin:lib/*" Main
```

Aplikasi akan:
1. ✅ Tampilkan menu login
2. ✅ Koneksi ke database `db_laporan_praktikum`
3. ✅ Menampilkan prompt interaktif

## 👥 User Credentials untuk Testing

| Username | Password | Role | Nama |
|----------|----------|------|------|
| `mhs1` | `12345` | Mahasiswa | Dapot Matthew (NIM: 231001001) |
| `mhs2` | `12345` | Mahasiswa | Andi Pratama (NIM: 231001002) |
| `asisten1` | `12345` | Dosen | Rina Asisten (NIDN: 0011223344) |
| `asisten2` | `12345` | Dosen | Budi Asisten (NIDN: 0055667788) |

## 🧪 Workflow Testing (Manual)

### Scenario: Dosen Membuat Jadwal → Mahasiswa Submit → Dosen Grade

#### Step 1: Dosen (asisten1) Login & Buat Jadwal
```
Login: asisten1 / 12345
Menu: 1 (Buat Jadwal Praktikum)
Input:
  - Materi: Sorting Implementation
  - Tanggal Praktikum: 2025-01-15
  - (Sistem auto-set Batas Akhir: 2025-01-18 ← +3 hari)
```

#### Step 2: Mahasiswa (mhs1) Login & Lihat Jadwal
```
Login: mhs1 / 12345
Menu: 1 (Lihat Jadwal Praktikum & Tenggat)
Output: Lihat jadwal dari asisten1 dengan batas tenggat
```

#### Step 3: Mahasiswa Submit Laporan
```
Menu: 2 (Kumpulkan Laporan Praktikum)
Pilih: Jadwal ID dari Step 1
Input:
  - Judul: Sorting Result
  - Hasil Praktikum: Praktikum sorting berhasil dengan bubble sort implementation
  - (Sistem auto-set Tanggal Kumpul: Hari ini)
  - (Sistem auto-check Status: TEPAT_WAKTU atau TERLAMBAT)
```

#### Step 4: Dosen Cek & Beri Nilai
```
Login: asisten1 / 12345
Menu: 2 (Cek Laporan Masuk)
Output: Lihat laporan dari mhs1 (status: TEPAT_WAKTU)

Menu: 3 (Beri Nilai & Catatan)
Pilih: Laporan dari mhs1
Input:
  - Nilai: 85
  - Catatan: Sangat bagus, implementasi bubble sort sudah benar
```

#### Step 5: Mahasiswa Lihat Nilai
```
Login: mhs1 / 12345
Menu: 3 (Lihat Status & Nilai Laporan)
Output: Lihat nilai 85 + catatan dari asisten1
```

## 📊 Database Schema

### Tabel Utama:
- **users**: Autentikasi (username, password, role)
- **mahasiswa**: Data mahasiswa (NIM, nama, jurusan)
- **dosen**: Data dosen (NIDN, nama, departemen)
- **jadwal_pengumpulan**: Jadwal praktikum dengan deadline +3 hari
- **laporan_praktikum**: Laporan yang dikumpulkan + status + nilai

### Status Laporan:
- `TEPAT_WAKTU`: Dikumpulkan sebelum/pada batas akhir
- `TERLAMBAT`: Dikumpulkan setelah batas akhir
- `DINILAI`: Sudah diberi nilai oleh dosen

## 🔍 Troubleshooting

### Error: "Koneksi database gagal"
- Cek MySQL service running: `sudo systemctl status mysql`
- Cek user/password: pastikan `pbo_user` ada dengan password `Pbo!Praktikum2026`
- Cek database: `mysql -u pbo_user -pPbo!Praktikum2026 -e "USE db_laporan_praktikum; SHOW TABLES;"`

### Error: "LOGIN GAGAL"
- Pastikan username & password sesuai tabel di atas
- Cek seed data: `mysql -u pbo_user -pPbo!Praktikum2026 db_laporan_praktikum -e "SELECT * FROM users;"`

### Re-setup Database (jika perlu clean slate)
```bash
cd /home/potydev/tugas/Tugas-1-PBO-DapotMatthewTampubolon
# database.sql sudah punya DROP IF EXISTS, jadi aman untuk re-import:
mysql -u pbo_user -pPbo!Praktikum2026 db_laporan_praktikum < database.sql
```

## 📁 File Structure
```
Tugas-1-PBO-DapotMatthewTampubolon/
├── src/
│   ├── Main.java                          # Entry point aplikasi
│   ├── model/
│   │   ├── User.java                      # Base class (abstract)
│   │   ├── Mahasiswa.java                 # Inherits User
│   │   ├── Dosen.java                     # Inherits User
│   │   ├── JadwalPengumpulan.java          # Model jadwal
│   │   └── LaporanPraktikum.java           # Model laporan
│   ├── dao/
│   │   ├── BaseDAO.java                   # Interface generic CRUD
│   │   ├── UserDAO.java                   # Auth interface
│   │   ├── JadwalPengumpulanDAO.java       # Jadwal DAO interface
│   │   ├── LaporanPraktikumDAO.java        # Laporan DAO interface
│   │   └── impl/
│   │       ├── UserDAOImpl.java
│   │       ├── JadwalPengumpulanDAOImpl.java
│   │       └── LaporanPraktikumDAOImpl.java
│   └── config/
│       └── DatabaseConfig.java            # Singleton JDBC config
├── bin/                                    # Compiled .class files
├── lib/                                    # mysql-connector-java JAR
├── database.sql                           # Schema + seed data
├── README.md                              # Analysis & Class Diagram
├── RUNNING.md                             # This file (how to run)
└── TEST_MANUAL.sh                         # Manual testing guide
```

## ✅ Verifikasi Instalasi

Jalankan checklist ini sebelum submit:

```bash
# 1. Cek MySQL service
sudo systemctl status mysql | grep "active"

# 2. Cek database & user
mysql -u pbo_user -pPbo!Praktikum2026 -e "SELECT 1;"

# 3. Cek seed data
mysql -u pbo_user -pPbo!Praktikum2026 db_laporan_praktikum -e "SELECT COUNT(*) FROM users;"
# Output: 4 (2 mahasiswa + 2 dosen)

# 4. Cek compile
cd /home/potydev/tugas/Tugas-1-PBO-DapotMatthewTampubolon
javac -cp "lib/*" -d bin $(find src -name "*.java") && echo "✅ Compile OK"

# 5. Cek run (tekan Ctrl+C setelah lihat "Menu Awal")
java -cp "bin:lib/*" Main
```

---
**Last Updated**: March 25, 2026
**Status**: ✅ Database synced, Code compiled, Ready for testing
