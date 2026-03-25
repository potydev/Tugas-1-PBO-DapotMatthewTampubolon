CREATE DATABASE IF NOT EXISTS db_laporan_praktikum;
USE db_laporan_praktikum;

-- Drop tables in reverse dependency order (FK first)
DROP TABLE IF EXISTS laporan_praktikum;
DROP TABLE IF EXISTS jadwal_pengumpulan;
DROP TABLE IF EXISTS logbook_bimbingan;
DROP TABLE IF EXISTS pengajuan_pembimbing;
DROP TABLE IF EXISTS proposal;
DROP TABLE IF EXISTS dosen;
DROP TABLE IF EXISTS mahasiswa;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('MAHASISWA', 'DOSEN') NOT NULL
);

CREATE TABLE mahasiswa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    nim VARCHAR(20) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    jurusan VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE    
);

CREATE TABLE dosen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    nidn VARCHAR(20) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    departemen VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE proposal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mahasiswa_id INT NOT NULL,
    judul VARCHAR(255) NOT NULL,
    latar_belakang TEXT,
    status ENUM('DIAJUKAN', 'DITERIMA', 'DITOLAK') DEFAULT 'DIAJUKAN',
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON DELETE CASCADE
);

CREATE TABLE pengajuan_pembimbing (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mahasiswa_id INT NOT NULL,
    dosen_id INT NOT NULL,
    status ENUM('MENUNGGU', 'DITERIMA', 'DITOLAK') DEFAULT 'MENUNGGU',
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON DELETE CASCADE,
    FOREIGN KEY (dosen_id) REFERENCES dosen(id) ON DELETE CASCADE
);

CREATE TABLE logbook_bimbingan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mahasiswa_id INT NOT NULL,
    dosen_id INT NOT NULL,
    tanggal DATE NOT NULL,
    kegiatan TEXT NOT NULL,
    catatan_dosen TEXT,
    status ENUM('DIAJUKAN', 'DISETUJUI') DEFAULT 'DIAJUKAN',
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON DELETE CASCADE,
    FOREIGN KEY (dosen_id) REFERENCES dosen(id) ON DELETE CASCADE
);

CREATE TABLE jadwal_pengumpulan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dosen_id INT NOT NULL,
    materi VARCHAR(150) NOT NULL,
    tanggal_praktikum DATE NOT NULL,
    batas_akhir DATE NOT NULL,
    FOREIGN KEY (dosen_id) REFERENCES dosen(id) ON DELETE CASCADE
);

CREATE TABLE laporan_praktikum (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jadwal_id INT NOT NULL,
    mahasiswa_id INT NOT NULL,
    judul VARCHAR(255) NOT NULL,
    hasil_praktikum TEXT NOT NULL,
    tanggal_kumpul DATE NOT NULL,
    status ENUM('TEPAT_WAKTU', 'TERLAMBAT', 'DINILAI') NOT NULL,
    nilai INT,
    catatan_dosen TEXT,
    FOREIGN KEY (jadwal_id) REFERENCES jadwal_pengumpulan(id) ON DELETE CASCADE,
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON DELETE CASCADE
);

INSERT INTO users (id, username, password, role) VALUES
(1, 'mhs1', '12345', 'MAHASISWA'),
(2, 'mhs2', '12345', 'MAHASISWA'),
(3, 'asisten1', '12345', 'DOSEN'),
(4, 'asisten2', '12345', 'DOSEN');

INSERT INTO mahasiswa (id, user_id, nim, nama, jurusan) VALUES
(1, 1, '231001001', 'Dapot Matthew', 'Informatika'),
(2, 2, '231001002', 'Andi Pratama', 'Sistem Informasi');

INSERT INTO dosen (id, user_id, nidn, nama, departemen) VALUES
(1, 3, '0011223344', 'Rina Asisten', 'Laboratorium Pemrograman'),
(2, 4, '0055667788', 'Budi Asisten', 'Laboratorium Basis Data');
