package org.example;


public class Elev {
    private int code;
    private String nume;
    private String prenume;
    private String initTata;
    private String datan;
    private int anStudiu;
    private float media;
    private int codc;

    public Elev(int code, String nume, String prenume, String initTata, String datan, int anStudiu, float media, int codc) {
        this.code = code;
        this.nume = nume;
        this.prenume = prenume;
        this.initTata = initTata;
        this.datan = datan;
        this.anStudiu = anStudiu;
        this.media = media;
        this.codc = codc;
    }

    public int getCode() {
        return code;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getInitTata() {
        return initTata;
    }

    public String getDatan() {
        return datan;
    }

    public int getAnStudiu() {
        return anStudiu;
    }

    public float getMedia() {
        return media;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setInitTata(String initTata) {
        this.initTata = initTata;
    }

    public void setDatan(String datan) {
        this.datan = datan;
    }

    public void setAnStudiu(int anStudiu) {
        this.anStudiu = anStudiu;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public void setCodc(int codc) {
        this.codc = codc;
    }

    public int getCodc() {
        return codc;
    }

}
