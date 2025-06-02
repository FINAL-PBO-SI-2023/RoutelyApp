package routelyapp.model;

public class Bus {
    private Integer id;
    private String namaBus;
    private String nomorPolisi;
    private int kapasitas;

    public Bus(Integer id, String namaBus, String nomorPolisi, int kapasitas) {
        this.id = id;
        this.namaBus = namaBus;
        this.nomorPolisi = nomorPolisi;
        this.kapasitas = kapasitas;
    }

    public Integer getId() {
        return id;
    }

    public String getNamaBus() {
        return namaBus;
    }

    public void setNamaBus(String namaBus) {
        this.namaBus = namaBus;
    }

    public String getNomorPolisi() {
        return nomorPolisi;
    }

    public void setNomorPolisi(String nomorPolisi) {
        this.nomorPolisi = nomorPolisi;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
}
