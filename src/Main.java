import dao.JadwalPengumpulanDAO;
import dao.LaporanPraktikumDAO;
import dao.UserDAO;
import dao.impl.JadwalPengumpulanDAOImpl;
import dao.impl.LaporanPraktikumDAOImpl;
import dao.impl.UserDAOImpl;
import model.Dosen;
import model.JadwalPengumpulan;
import model.LaporanPraktikum;
import model.Mahasiswa;
import model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        UserDAO userDao = new UserDAOImpl();
        JadwalPengumpulanDAO jadwalDao = new JadwalPengumpulanDAOImpl();
        LaporanPraktikumDAO laporanDao = new LaporanPraktikumDAOImpl();

        while (true) {
            System.out.println("  SISTEM PENGUMPULAN TUGAS LAPORAN PRAKTIKUM  ");
            System.out.println("================================================");
            System.out.println("Menu Awal:");
            System.out.println("1. Login");
            System.out.println("0. Keluar Aplikasi");
            System.out.print("Pilih menu (1/0): ");
            
            String pilihan = scanner.nextLine();
            
            if ("0".equals(pilihan)) {
                System.out.println("Terima kasih telah menggunakan sistem ini. Sampai jumpa!");
                break;
            }

            if (!"1".equals(pilihan)) {
                System.out.println("Pilihan tidak valid, silakan ketik 1 atau 0.\n");
                continue;
            }

            System.out.println("\nSILAKAN LOGIN");
            System.out.print("Username : ");
            String username = scanner.nextLine();

            System.out.print("Password : ");
            String password = scanner.nextLine();

            User user = userDao.authenticate(username, password);
            if (user == null) {
                System.out.println("LOGIN GAGAL! Username atau Password salah.\n");
                continue;
            }

            if (user instanceof Mahasiswa) {
                Mahasiswa mahasiswa = (Mahasiswa) user;
                boolean loggedIn = true;
                while (loggedIn) {
                    System.out.println("\nSelamat datang, " + mahasiswa.getNama() + " (" + mahasiswa.getNim() + ")");
                    mahasiswa.tampilkanMenu();
                    System.out.print("Pilih aksi (1-3,5): ");
                    String aksi = scanner.nextLine();

                    switch (aksi) {
                        case "1":
                            List<JadwalPengumpulan> semuaJadwal = jadwalDao.findAll();
                            if (semuaJadwal.isEmpty()) {
                                System.out.println("Belum ada jadwal praktikum dari dosen.");
                            } else {
                                System.out.println("\nDAFTAR JADWAL & TENGGAT PENGUMPULAN:");
                                for (JadwalPengumpulan jadwal : semuaJadwal) {
                                    System.out.println("ID Jadwal: [" + jadwal.getId() + "] | Materi: " + jadwal.getMateri());
                                    System.out.println("Tanggal Praktikum: " + jadwal.getTanggalPraktikum() + " | Batas Akhir: "
                                            + jadwal.getBatasAkhir() + " | Dosen: " + jadwal.getNamaDosen());
                                    System.out.println("--------------------------------------------------------");
                                }
                            }
                            break;

                        case "2":
                            List<JadwalPengumpulan> jadwalAktif = jadwalDao.findAll();
                            if (jadwalAktif.isEmpty()) {
                                System.out.println("Belum ada jadwal praktikum. Anda belum bisa mengumpulkan laporan.");
                                break;
                            }

                            System.out.println("\nPILIH JADWAL UNTUK LAPORAN:");
                            for (JadwalPengumpulan jadwal : jadwalAktif) {
                                System.out.println("[" + jadwal.getId() + "] " + jadwal.getMateri() +
                                        " | Praktikum: " + jadwal.getTanggalPraktikum() +
                                        " | Tenggat: " + jadwal.getBatasAkhir());
                            }

                            System.out.print("Masukkan ID Jadwal: ");
                            try {
                                int jadwalId = Integer.parseInt(scanner.nextLine());
                                JadwalPengumpulan jadwalDipilih = jadwalDao.findById(jadwalId);
                                if (jadwalDipilih == null) {
                                    System.out.println("ID jadwal tidak ditemukan.");
                                    break;
                                }

                                System.out.print("Judul Laporan: ");
                                String judul = scanner.nextLine();
                                System.out.print("Hasil Praktikum / Ringkasan Laporan: ");
                                String hasil = scanner.nextLine();
                                System.out.print("Tanggal Kumpul (YYYY-MM-DD): ");
                                Date tanggalKumpul = Date.valueOf(scanner.nextLine());

                                String status = tanggalKumpul.after(jadwalDipilih.getBatasAkhir())
                                        ? "TERLAMBAT"
                                        : "TEPAT_WAKTU";

                                LaporanPraktikum laporan = new LaporanPraktikum(
                                        0,
                                        jadwalDipilih.getId(),
                                        mahasiswa.getMahasiswaId(),
                                        judul,
                                        hasil,
                                        tanggalKumpul,
                                        status,
                                        null,
                                        null
                                );

                                laporanDao.insert(laporan);
                                System.out.println("Status pengumpulan Anda: " + status);
                                if ("TERLAMBAT".equals(status)) {
                                    System.out.println("Perhatian: laporan lewat tenggat dan dapat ditolak sesuai kebijakan dosen.");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Input tidak valid. Pastikan format benar (ID angka, tanggal YYYY-MM-DD).");
                            }
                            break;

                        case "3":
                            List<LaporanPraktikum> laporanMahasiswa = laporanDao.findByMahasiswaId(mahasiswa.getMahasiswaId());
                            if (laporanMahasiswa.isEmpty()) {
                                System.out.println("Belum ada laporan yang Anda kumpulkan.");
                            } else {
                                System.out.println("\nSTATUS LAPORAN ANDA:");
                                for (LaporanPraktikum laporan : laporanMahasiswa) {
                                    System.out.println("ID Laporan: [" + laporan.getId() + "] | Materi: " + laporan.getMateri());
                                    System.out.println("Judul: " + laporan.getJudul());
                                    System.out.println("Tanggal Kumpul: " + laporan.getTanggalKumpul() +
                                            " | Tenggat: " + laporan.getBatasAkhir());
                                    System.out.println("Status: " + laporan.getStatus() +
                                            " | Nilai: " + (laporan.getNilai() != null ? laporan.getNilai() : "Belum dinilai"));
                                    System.out.println("Catatan Dosen: "
                                            + (laporan.getCatatanDosen() != null ? laporan.getCatatanDosen() : "-"));
                                    System.out.println("--------------------------------------------------------");
                                }
                            }
                            break;

                        case "5":
                            loggedIn = false;
                            System.out.println("Logout berhasil.\n");
                            break;

                        default:
                            System.out.println("Pilihan tidak valid.");
                    }
                }
            } else if (user instanceof Dosen) {
                Dosen dosen = (Dosen) user;
                boolean loggedIn = true;
                while (loggedIn) {
                    System.out.println("\nSelamat datang, Dosen " + dosen.getNama() + " (" + dosen.getNidn() + ")");
                    dosen.tampilkanMenu();
                    System.out.print("Pilih aksi (1-4): ");
                    String aksi = scanner.nextLine();

                    switch (aksi) {
                        case "1":
                            try {
                                System.out.print("Materi Praktikum: ");
                                String materi = scanner.nextLine();
                                System.out.print("Tanggal Praktikum (YYYY-MM-DD): ");
                                Date tanggalPraktikum = Date.valueOf(scanner.nextLine());

                                LocalDate batas = tanggalPraktikum.toLocalDate().plusDays(3);
                                JadwalPengumpulan jadwal = new JadwalPengumpulan(
                                        0,
                                        dosen.getDosenId(),
                                        materi,
                                        tanggalPraktikum,
                                        Date.valueOf(batas)
                                );
                                jadwalDao.insert(jadwal);
                                System.out.println(
                                        "Tenggat otomatis ditetapkan 3 hari setelah praktikum: " + jadwal.getBatasAkhir());
                            } catch (IllegalArgumentException e) {
                                System.out.println("Format tanggal salah. Gunakan YYYY-MM-DD.");
                            }
                            break;

                        case "2":
                            List<LaporanPraktikum> laporanMasuk = laporanDao.findByDosenId(dosen.getDosenId());
                            if (laporanMasuk.isEmpty()) {
                                System.out.println("Belum ada laporan masuk untuk Anda.");
                            } else {
                                System.out.println("\nDAFTAR LAPORAN MASUK:");
                                for (LaporanPraktikum laporan : laporanMasuk) {
                                    System.out.println(
                                            "ID: [" + laporan.getId() + "] | Mahasiswa: " + laporan.getNamaMahasiswa());
                                    System.out.println(
                                            "Materi: " + laporan.getMateri() + " | Judul: " + laporan.getJudul());
                                    System.out.println("Tgl Kumpul: " + laporan.getTanggalKumpul() +
                                            " | Tenggat: " + laporan.getBatasAkhir());
                                    System.out.println("Status Tenggat: " + laporan.getStatus() +
                                            " | Nilai: "
                                            + (laporan.getNilai() != null ? laporan.getNilai() : "Belum dinilai"));
                                    System.out.println("--------------------------------------------------------");
                                }
                            }
                            break;

                        case "3":
                            List<LaporanPraktikum> daftarNilai = laporanDao.findByDosenId(dosen.getDosenId());
                            if (daftarNilai.isEmpty()) {
                                System.out.println("Belum ada laporan untuk dinilai.");
                                break;
                            }

                            for (LaporanPraktikum laporan : daftarNilai) {
                                System.out.println("[" + laporan.getId() + "] " + laporan.getNamaMahasiswa() +
                                        " | " + laporan.getJudul() + " | Status: " + laporan.getStatus());
                            }

                            System.out.print("Masukkan ID laporan yang ingin dinilai: ");
                            try {
                                int laporanId = Integer.parseInt(scanner.nextLine());
                                LaporanPraktikum laporan = laporanDao.findById(laporanId);
                                if (laporan == null) {
                                    System.out.println("ID laporan tidak ditemukan.");
                                    break;
                                }

                                if (!laporan.getBatasAkhir().after(laporan.getTanggalKumpul())
                                        && !laporan.getBatasAkhir().equals(laporan.getTanggalKumpul())) {
                                    System.out.println("Laporan ini TERLAMBAT. Silakan tentukan kebijakan nilai sesuai aturan.");
                                }

                                System.out.print("Masukkan Nilai (0-100): ");
                                int nilai = Integer.parseInt(scanner.nextLine());
                                if (nilai < 0 || nilai > 100) {
                                    System.out.println("Nilai harus antara 0-100.");
                                    break;
                                }

                                System.out.print("Masukkan Catatan Dosen: ");
                                String catatan = scanner.nextLine();
                                laporanDao.updateNilaiDanCatatan(laporanId, nilai, catatan);
                            } catch (NumberFormatException e) {
                                System.out.println("Input angka tidak valid.");
                            }
                            break;

                        case "4":
                            loggedIn = false;
                            System.out.println("Logout berhasil.\n");
                            break;

                        default:
                            System.out.println("Pilihan tidak valid.");
                    }
                }
            }
        }

        scanner.close();
    }
}
