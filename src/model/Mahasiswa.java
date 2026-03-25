package model;

// Inheritance: Mahasiswa mewarisi User
public class Mahasiswa extends User {
    private int mahasiswaId;
    private String nim;
    private String nama;
    private String jurusan;

    public Mahasiswa(int id, String username, String password, int mahasiswaId, String nim, String nama, String jurusan) {
        super(id, username, password, "MAHASISWA");
        this.mahasiswaId = mahasiswaId;
        this.nim = nim;
        this.nama = nama;
        this.jurusan = jurusan;
    }

    // Enkapsulasi Getter & Setter
    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    // Polimorfisme: Override abstract method dari class User
    @Override
    public void tampilkanMenu() {
        System.out.println(" Menu Mahasiswa ");
        System.out.println("1. Lihat Jadwal Praktikum & Tenggat");
        System.out.println("2. Kumpulkan Laporan Praktikum");
        System.out.println("3. Lihat Status & Nilai Laporan");
        System.out.println("5. Logout");
    }
}
