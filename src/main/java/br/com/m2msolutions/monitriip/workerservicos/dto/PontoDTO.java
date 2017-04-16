package br.com.m2msolutions.monitriip.workerservicos.dto;

/**
 * Created by Rodrigo Ribeiro on 15/04/17.
 */
public class PontoDTO {

    private String id;
    private String localidade;
    private String municipio;
    private String uf;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

}
