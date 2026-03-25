package model;

import java.sql.Date;

public class LaporanPraktikum {
    private int id;
    private int jadwalId;
    private int mahasiswaId;
    private String judul;
    private String hasilPraktikum;
    private Date tanggalKumpul;
    private String status;
    private Integer nilai;
    private String catatanDosen;
    private String namaMahasiswa;
    private String materi;
    private Date batasAkhir;

    public LaporanPraktikum(int id, int jadwalId, int mahasiswaId, String judul, String hasilPraktikum, Date tanggalKumpul,
            String status, Integer nilai, String catatanDosen) {
        this.id = id;
        this.jadwalId = jadwalId;
        this.mahasiswaId = mahasiswaId;
        this.judul = judul;
        this.hasilPraktikum = hasilPraktikum;
        this.tanggalKumpul = tanggalKumpul;
        this.status = status;
        this.nilai = nilai;
        this.catatanDosen = catatanDosen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJadwalId() {
        return jadwalId;
    }

    public void setJadwalId(int jadwalId) {
        this.jadwalId = jadwalId;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHasilPraktikum() {
        return hasilPraktikum;
    }

    public void setHasilPraktikum(String hasilPraktikum) {
        this.hasilPraktikum = hasilPraktikum;
    }

    public Date getTanggalKumpul() {
        return tanggalKumpul;
    }

    public void setTanggalKumpul(Date tanggalKumpul) {
        this.tanggalKumpul = tanggalKumpul;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNilai() {
        return nilai;
    }

    public void setNilai(Integer nilai) {
        this.nilai = nilai;
    }

    public String getCatatanDosen() {
        return catatanDosen;
    }

    public void setCatatanDosen(String catatanDosen) {
        this.catatanDosen = catatanDosen;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public Date getBatasAkhir() {
        return batasAkhir;
    }

    public void setBatasAkhir(Date batasAkhir) {
        this.batasAkhir = batasAkhir;
    }
}