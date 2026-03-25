package model;

import java.sql.Date;

public class JadwalPengumpulan {
    private int id;
    private int dosenId;
    private String materi;
    private Date tanggalPraktikum;
    private Date batasAkhir;
    private String namaDosen;

    public JadwalPengumpulan(int id, int dosenId, String materi, Date tanggalPraktikum, Date batasAkhir) {
        this.id = id;
        this.dosenId = dosenId;
        this.materi = materi;
        this.tanggalPraktikum = tanggalPraktikum;
        this.batasAkhir = batasAkhir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDosenId() {
        return dosenId;
    }

    public void setDosenId(int dosenId) {
        this.dosenId = dosenId;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public Date getTanggalPraktikum() {
        return tanggalPraktikum;
    }

    public void setTanggalPraktikum(Date tanggalPraktikum) {
        this.tanggalPraktikum = tanggalPraktikum;
    }

    public Date getBatasAkhir() {
        return batasAkhir;
    }

    public void setBatasAkhir(Date batasAkhir) {
        this.batasAkhir = batasAkhir;
    }

    public String getNamaDosen() {
        return namaDosen;
    }

    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }
}