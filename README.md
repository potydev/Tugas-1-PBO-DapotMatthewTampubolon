# Tugas-1-PBO-DapotMatthewTampubolon

Dokumentasi ini mengikuti format lengkap seperti referensi, namun isi disesuaikan dengan implementasi program Dapot (alur **jadwal pengumpulan laporan praktikum**).

# BAGIAN 1 — ANALISIS SISTEM

## 1. Identifikasi Class Utama

| Class                                   | Atribut                                                                                                       | Method                                                                                                                                                                                                                                                                                                  |
| :-------------------------------------- | :------------------------------------------------------------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **User** _(Abstract Class)_             | `id`, `username`, `password`, `role`                                                                          | `getId()`, `setId()`, `getUsername()`, `setUsername()`, `getPassword()`, `setPassword()`, `getRole()`, `setRole()`, `tampilkanMenu()`                                                                                                                                                              |
| **Mahasiswa** _(Extends User)_          | `mahasiswaId`, `nim`, `nama`, `jurusan`                                                                       | `getMahasiswaId()`, `setMahasiswaId()`, `getNim()`, `setNim()`, `getNama()`, `setNama()`, `getJurusan()`, `setJurusan()`, `tampilkanMenu()`                                                                                                                                                        |
| **Dosen** _(Extends User)_              | `dosenId`, `nidn`, `nama`, `departemen`                                                                       | `getDosenId()`, `setDosenId()`, `getNidn()`, `setNidn()`, `getNama()`, `setNama()`, `getDepartemen()`, `setDepartemen()`, `tampilkanMenu()`                                                                                                                                                        |
| **JadwalPengumpulan**                   | `id`, `dosenId`, `materi`, `tanggalPraktikum`, `batasAkhir`, `namaDosen`                                     | `getId()`, `setId()`, `getDosenId()`, `setDosenId()`, `getMateri()`, `setMateri()`, `getTanggalPraktikum()`, `setTanggalPraktikum()`, `getBatasAkhir()`, `setBatasAkhir()`, `getNamaDosen()`, `setNamaDosen()`                                                                                    |
| **LaporanPraktikum**                    | `id`, `jadwalId`, `mahasiswaId`, `judul`, `hasilPraktikum`, `tanggalKumpul`, `status`, `nilai`, `catatanDosen` | `getId()`, `setId()`, `getJadwalId()`, `setJadwalId()`, `getMahasiswaId()`, `setMahasiswaId()`, `getJudul()`, `setJudul()`, `getHasilPraktikum()`, `setHasilPraktikum()`, `getTanggalKumpul()`, `setTanggalKumpul()`, `getStatus()`, `setStatus()`, `getNilai()`, `setNilai()`, `getCatatanDosen()`, `setCatatanDosen()` |
| **DatabaseConfig**                      | `URL`, `USER`, `PASSWORD`, `connection`                                                                       | `getConnection()`                                                                                                                                                                                                                                                                                       |
| **BaseDAO** _(interface)_               | _-_                                                                                                           | `insert()`, `findById()`, `findAll()`, `update()`, `delete()`                                                                                                                                                                                                                                          |
| **UserDAO** _(interface)_               | _-_                                                                                                           | _(Mewarisi method BaseDAO)_ + `authenticate()`, `isUsernameExist()`                                                                                                                                                                                                                                    |
| **MahasiswaDAO** _(interface)_          | _-_                                                                                                           | _(Mewarisi method BaseDAO)_ + `findByNim()`, `isNimExist()`                                                                                                                                                                                                                                            |
| **DosenDAO** _(interface)_              | _-_                                                                                                           | _(Mewarisi method BaseDAO)_ + `findByNidn()`, `isNidnExist()`                                                                                                                                                                                                                                          |
| **JadwalPengumpulanDAO** _(interface)_  | _-_                                                                                                           | _(Mewarisi method BaseDAO)_ + `findByDosenId()`                                                                                                                                                                                                                                                         |
| **LaporanPraktikumDAO** _(interface)_   | _-_                                                                                                           | _(Mewarisi method BaseDAO)_ + `findByMahasiswaId()`, `findByDosenId()`, `updateNilaiDanCatatan()`                                                                                                                                                                                                    |

### Sistem ini menerapkan dua konsep utama:

1. **Data Access Object (DAO) Pattern**: Memisahkan logika akses data dari antarmuka/menu.
2. **Pemrograman Berorientasi Objek (OOP)**: Struktur program berpusat pada class, object, pewarisan, dan enkapsulasi.

## 2. Hubungan Antar Class

Semua atribut dalam class dideklarasikan `private` sebagai bentuk enkapsulasi.

**a) INHERITANCE (Pewarisan)**

- `Mahasiswa` extends `User`.
- `Dosen` extends `User`.
- Seluruh DAO interface (`UserDAO`, `MahasiswaDAO`, `DosenDAO`, `JadwalPengumpulanDAO`, `LaporanPraktikumDAO`) extends `BaseDAO<T>`.

**b) ABSTRACTION (Abstraksi)**

- `User` adalah abstract class dengan abstract method `tampilkanMenu()`.
- `BaseDAO<T>` adalah interface generic untuk kontrak CRUD.

**c) IMPLEMENTS (Implementasi Interface)**

- `UserDAOImpl` implements `UserDAO`
- `MahasiswaDAOImpl` implements `MahasiswaDAO`
- `DosenDAOImpl` implements `DosenDAO`
- `JadwalPengumpulanDAOImpl` implements `JadwalPengumpulanDAO`
- `LaporanPraktikumDAOImpl` implements `LaporanPraktikumDAO`

**d) DEPENDENCY (Ketergantungan/Asosiasi)**

- `JadwalPengumpulan` bergantung pada `Dosen` melalui `dosenId`.
- `LaporanPraktikum` bergantung pada `Mahasiswa` dan `JadwalPengumpulan` melalui `mahasiswaId` dan `jadwalId`.
- Semua class DAO implementation bergantung pada `DatabaseConfig.getConnection()`.
- `Main` bergantung pada seluruh DAO dan model class sebagai pusat alur.

## 3. Alasan Pemilihan Atribut dan Method

**a) Class User (Abstract)**

- Atribut `id`, `username`, `password`, `role` wajib untuk identitas dan autentikasi.
- `tampilkanMenu()` dibuat abstract karena menu tiap role berbeda.

**b) Class Mahasiswa**

- `nim`, `nama`, `jurusan` dibutuhkan sebagai identitas akademik mahasiswa.
- Menu mahasiswa fokus pada lihat jadwal, kumpul laporan, dan lihat nilai.

**c) Class Dosen**

- `nidn`, `nama`, `departemen` dibutuhkan untuk identitas pengampu praktikum.
- Menu dosen fokus pada membuat jadwal, mengecek laporan, dan memberi nilai.

**d) Class JadwalPengumpulan**

- `materi`, `tanggalPraktikum`, `batasAkhir` dipakai untuk mendefinisikan tugas dan tenggat.
- Tenggat ditetapkan otomatis `+3 hari` dari tanggal praktikum.

**e) Class LaporanPraktikum**

- `judul`, `hasilPraktikum`, `tanggalKumpul` untuk isi laporan mahasiswa.
- `status` (`TEPAT_WAKTU`, `TERLAMBAT`, `DINILAI`), `nilai`, dan `catatanDosen` untuk evaluasi.

**f) Class DatabaseConfig**

- Menggunakan Singleton agar koneksi database terpusat dan efisien.

**g) Interface BaseDAO<T>**

- Generic DAO dipakai agar operasi CRUD tidak ditulis ulang untuk setiap entitas.

# BAGIAN 2 — DESAIN DIAGRAM

## 1. Class Diagram

```mermaid
classDiagram

class User {
    <<abstract>>
    -int id
    -String username
    -String password
    -String role

    +getId() int
    +getUsername() String
    +getPassword() String
    +getRole() String

    +setId(id:int) void
    +setUsername(username:String) void
    +setPassword(password:String) void
    +setRole(role:String) void

    +tampilkanMenu() void
}

class Mahasiswa {
    -int mahasiswaId
    -String nim
    -String nama
    -String jurusan

    +getMahasiswaId() int
    +getNim() String
    +getNama() String
    +getJurusan() String

    +setMahasiswaId(id:int) void
    +setNim(nim:String) void
    +setNama(nama:String) void
    +setJurusan(jurusan:String) void

    +tampilkanMenu() void
}

class Dosen {
    -int dosenId
    -String nidn
    -String nama
    -String departemen

    +getDosenId() int
    +getNidn() String
    +getNama() String
    +getDepartemen() String

    +setDosenId(id:int) void
    +setNidn(nidn:String) void
    +setNama(nama:String) void
    +setDepartemen(dept:String) void

    +tampilkanMenu() void
}

class JadwalPengumpulan {
    -int id
    -int dosenId
    -String materi
    -Date tanggalPraktikum
    -Date batasAkhir
    -String namaDosen
}

class LaporanPraktikum {
    -int id
    -int jadwalId
    -int mahasiswaId
    -String judul
    -String hasilPraktikum
    -Date tanggalKumpul
    -String status
    -Integer nilai
    -String catatanDosen
}

User <|-- Mahasiswa
User <|-- Dosen
Dosen "1" --> "0..*" JadwalPengumpulan
Mahasiswa "1" --> "0..*" LaporanPraktikum
JadwalPengumpulan "1" --> "0..*" LaporanPraktikum
```

## 2. ERD

```mermaid
erDiagram

    users {
        int id PK
        varchar username
        varchar password
        enum role
    }

    dosen {
        int id PK
        int user_id FK
        varchar nidn
        varchar nama
        varchar departemen
    }

    mahasiswa {
        int id PK
        int user_id FK
        varchar nim
        varchar nama
        varchar jurusan
    }

    jadwal_pengumpulan {
        int id PK
        int dosen_id FK
        varchar materi
        date tanggal_praktikum
        date batas_akhir
    }

    laporan_praktikum {
        int id PK
        int jadwal_id FK
        int mahasiswa_id FK
        varchar judul
        text hasil_praktikum
        date tanggal_kumpul
        enum status
        int nilai
        text catatan_dosen
    }

    users ||--|| dosen : "user_id"
    users ||--|| mahasiswa : "user_id"
    dosen ||--o{ jadwal_pengumpulan : "dosen_id"
    jadwal_pengumpulan ||--o{ laporan_praktikum : "jadwal_id"
    mahasiswa ||--o{ laporan_praktikum : "mahasiswa_id"
```

## 3. Workflow

```mermaid
flowchart TD
    A[Mulai] --> B[Login User]
    B --> C{Role?}

    C -->|DOSEN| D[Menu Dosen]
    C -->|MAHASISWA| E[Menu Mahasiswa]

    D --> D1[Buat Jadwal Praktikum]
    D1 --> D2[Input Materi + Tanggal Praktikum]
    D2 --> D3[Set Batas Akhir = Tanggal + 3 Hari]
    D3 --> D4[Simpan Jadwal]

    D --> D5[Cek Laporan Masuk]
    D5 --> D6[Lihat Status TEPAT_WAKTU / TERLAMBAT]

    D --> D7[Beri Nilai & Catatan]
    D7 --> D8[Update Nilai + Catatan]
    D8 --> D9[Status jadi DINILAI]

    E --> E1[Lihat Jadwal Praktikum]
    E --> E2[Kumpulkan Laporan]
    E2 --> E3[Pilih Jadwal + Input Judul/Hasil]
    E3 --> E4[Simpan Tanggal Kumpul]
    E4 --> E5{Tanggal Kumpul <= Batas Akhir?}
    E5 -->|Ya| E6[Status = TEPAT_WAKTU]
    E5 -->|Tidak| E7[Status = TERLAMBAT]
    E6 --> E8[Simpan Laporan]
    E7 --> E8

    E --> E9[Lihat Status & Nilai]
    E9 --> E10[Tampil Nilai + Catatan Dosen]
```

# BAGIAN 3 — IMPLEMENTASI PROGRAM JAVA

Sistem Pengumpulan Tugas Laporan Praktikum adalah aplikasi berbasis **Command Line Interface (CLI)** menggunakan Java dan MySQL.

Sistem memiliki dua role utama:

| Role          | Deskripsi                                                                                                    |
| ------------- | ------------------------------------------------------------------------------------------------------------- |
| **Mahasiswa** | Melihat jadwal praktikum, mengumpulkan laporan, dan melihat nilai/catatan dosen.                           |
| **Dosen**     | Membuat jadwal praktikum, mengecek laporan masuk, dan memberi nilai/catatan laporan mahasiswa.             |

## Fitur-Fitur Utama Sistem

### 1. Menambahkan Data

- **Buat Jadwal Praktikum** — Dosen mengisi materi dan tanggal praktikum, lalu sistem menyimpan jadwal dengan batas akhir otomatis (`+3 hari`) melalui `JadwalPengumpulanDAO.insert()`.
- **Kumpulkan Laporan Praktikum** — Mahasiswa memilih jadwal, mengisi judul dan hasil praktikum, lalu sistem menyimpan melalui `LaporanPraktikumDAO.insert()`.

### 2. Menampilkan Data

- **Daftar Jadwal Praktikum** — Mahasiswa melihat semua jadwal dari dosen menggunakan `JadwalPengumpulanDAO.findAll()`.
- **Daftar Laporan Masuk** — Dosen melihat laporan mahasiswa menggunakan `LaporanPraktikumDAO.findByDosenId()`.
- **Status dan Nilai Laporan** — Mahasiswa melihat status keterlambatan, nilai, dan catatan menggunakan `LaporanPraktikumDAO.findByMahasiswaId()`.

### 3. Mengubah Status Data

- **Status Ketepatan Waktu Otomatis** — Saat insert laporan, sistem otomatis menentukan status `TEPAT_WAKTU` atau `TERLAMBAT` berdasarkan `tanggal_kumpul` dan `batas_akhir`.
- **Proses Penilaian** — Dosen mengisi nilai dan catatan melalui `LaporanPraktikumDAO.updateNilaiDanCatatan()`, lalu status menjadi `DINILAI`.

## Alur Kerja Sistem (Workflow) — Detail

1. Pengguna login melalui `UserDAO.authenticate()`.
2. Jika role **Dosen**, pengguna dapat membuat jadwal dan menilai laporan.
3. Jika role **Mahasiswa**, pengguna dapat melihat jadwal dan mengumpulkan laporan.
4. Sistem mengecek ketepatan waktu pengumpulan secara otomatis.
5. Dosen memberikan nilai dan catatan, mahasiswa melihat hasil penilaian.

## Teknologi dan Tools yang Digunakan

| Komponen           | Teknologi                        |
| ------------------ | -------------------------------- |
| Bahasa Pemrograman | **Java** (JDK)                   |
| Database           | **MySQL**                        |
| Driver Koneksi     | **MySQL Connector/J** (JDBC)     |
| Antarmuka          | **Command Line Interface (CLI)** |
| IDE                | Visual Studio Code               |

## Cara Menjalankan Program

Panduan detail ada di `RUNNING.md`. Ringkasnya:

```bash
cd /home/potydev/tugas/Tugas-1-PBO-DapotMatthewTampubolon
mysql -u pbo_user -pPbo!Praktikum2026 db_laporan_praktikum < database.sql
javac -cp "lib/*" -d bin $(find src -name "*.java")
java -cp "bin:lib/*" Main
```

# BAGIAN 4 — ANALISIS KONSEP PBO

## 1. Penerapan Class dan Object

Class dipakai sebagai blueprint (`User`, `Mahasiswa`, `Dosen`, `JadwalPengumpulan`, `LaporanPraktikum`), sedangkan object dibuat saat program berjalan, misalnya object user setelah login dan object laporan saat submit.

## 2. Penerapan Enkapsulasi

Seluruh atribut model dideklarasikan `private` dan diakses melalui getter/setter sehingga data lebih aman dan terkontrol.

## 3. Mengapa PBO Lebih Baik dari Prosedural?

| Aspek            | PBO                                                                                             | Prosedural                                                       |
| ---------------- | ----------------------------------------------------------------------------------------------- | ---------------------------------------------------------------- |
| Organisasi       | Kode dipisah per class (`model`, `dao`, `impl`, `config`)                                     | Kode cenderung menumpuk dalam fungsi panjang                     |
| Reusability      | Pewarisan `Mahasiswa`/`Dosen` dari `User` mengurangi duplikasi                                | Banyak bagian harus ditulis ulang                                |
| Skalabilitas     | Menambah fitur/role baru lebih mudah karena struktur modular                                   | Perubahan kecil bisa berdampak ke banyak fungsi                  |
| Keamanan Data    | Enkapsulasi (`private` + getter/setter)                                                         | Data rentan diubah sembarang jika tidak dibatasi                 |
| Maintainability  | DAO memisahkan SQL dari logika menu                                                             | SQL dan logika bisnis sering bercampur                           |

# BAGIAN 5 — REFLEKSI

## 1. Bagian yang Paling Sulit

Bagian paling menantang adalah memastikan konsistensi alur **deadline otomatis, status otomatis, dan grading** agar sinkron antara menu CLI, model, DAO, dan database.

## 2. Hal Baru yang Dipelajari tentang Konsep PBO

- Penerapan abstract class sebagai kontrak menu per role.
- Polimorfisme saat objek `User` diarahkan ke perilaku `Mahasiswa` atau `Dosen`.
- Generic DAO untuk mengurangi duplikasi operasi CRUD.
- Separation of concerns antara model, DAO, config, dan main flow.

## 3. Fitur Pengembangan Lanjutan

1. Registrasi akun mandiri untuk mahasiswa dan dosen.
2. Upload file laporan (PDF/DOCX) selain input teks.
3. Notifikasi tenggat otomatis (H-1 deadline).
4. Dashboard statistik pengumpulan dan keterlambatan.
5. Migrasi antarmuka dari CLI ke GUI/Web.
