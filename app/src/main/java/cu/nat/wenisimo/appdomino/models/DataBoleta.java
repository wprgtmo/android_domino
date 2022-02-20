package cu.nat.wenisimo.appdomino.models;

public class DataBoleta {
    private Integer id;
    private String Datas;
    private Integer Puntos1;
    private Integer TantosAcumulados1;
    private Integer Puntos2;
    private Integer TantosAcumulados2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatas() {
        return Datas;
    }

    public void setDatas(String datas) {
        Datas = datas;
    }

    public Integer getPuntos1() {
        return Puntos1;
    }

    public void setPuntos1(Integer puntos1) {
        Puntos1 = puntos1;
    }

    public Integer getPuntos2() {
        return Puntos2;
    }

    public void setPuntos2(Integer puntos2) {
        Puntos2 = puntos2;
    }

    public Integer getTantosAcumulados1() {
        return TantosAcumulados1;
    }

    public void setTantosAcumulados1(Integer tantosAcumulados1) {
        TantosAcumulados1 = tantosAcumulados1;
    }

    public Integer getTantosAcumulados2() {
        return TantosAcumulados2;
    }

    public void setTantosAcumulados2(Integer tantosAcumulados2) {
        TantosAcumulados2 = tantosAcumulados2;
    }
}
