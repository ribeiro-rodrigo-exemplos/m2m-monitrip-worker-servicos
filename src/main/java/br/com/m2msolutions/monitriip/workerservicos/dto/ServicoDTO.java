package br.com.m2msolutions.monitriip.workerservicos.dto;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by Rodrigo Ribeiro on 15/04/17.
 */

public class ServicoDTO {

    private String codDestino;
    private String codOrigem;
    private String data;
    private String destino;
    private String horarioSaida;
    private String linha;
    private String motorista1;
    private String numServico;
    private String origem;

    @XStreamOmitField
    private PontoDTO pontoOrigem;
    @XStreamOmitField
    private PontoDTO pontoDestino;

    public String getCodDestino() {
        return codDestino;
    }

    public void setCodDestino(String codDestino) {
        this.codDestino = codDestino;
    }

    public String getCodOrigem() {
        return codOrigem;
    }

    public void setCodOrigem(String codOrigem) {
        this.codOrigem = codOrigem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {

        this.destino = destino;

    }

    public String getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(String horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getMotorista1() {
        return motorista1;
    }

    public void setMotorista1(String motorista1) {
        this.motorista1 = motorista1;
    }

    public String getNumServico() {
        return numServico;
    }

    public void setNumServico(String numServico) {
        this.numServico = numServico;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {

        this.origem = origem;
    }

    public PontoDTO getPontoOrigem() {
        return pontoOrigem;
    }

    public void setPontoOrigem(PontoDTO pontoOrigem) {

        this.pontoOrigem = pontoOrigem;
    }

    public PontoDTO getPontoDestino() {
        return pontoDestino;
    }

    public void setPontoDestino(PontoDTO pontoDestino) {

        this.pontoDestino = pontoDestino;
    }
}
