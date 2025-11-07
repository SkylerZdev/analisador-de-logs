package br.edu.icev.aed.forense;

import java.util.Objects;

public class Vertice {
    
    String sessaoID, recurso;
    int distancia;
    Vertice origem;
    Boolean entrouBusca;

    public Vertice (String sessaoID, String recurso, Boolean entrouBusca){
        this.sessaoID = sessaoID;
        this.recurso = recurso;
        this.entrouBusca = entrouBusca;
    }

    @Override
    public String toString() {
        return "Vertice [sessaoID=" + sessaoID + ", recurso=" + recurso + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Vertice v)) return false;
        return Objects.equals(sessaoID, v.sessaoID) && Objects.equals(recurso, v.recurso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessaoID, recurso);
    }

}
