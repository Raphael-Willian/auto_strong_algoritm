package org.example.models;

/**
 * Perfil simples para personalizar curvas de fadiga.
 * Pode ser expandido com histórico, ML, etc.
 */
public class AthleteProfile {
    // fator entre 0.8 (muito resistente) e 1.2 (mais propenso a fadiga)
    private double fatigueFactor = 1.0;

    // sensibilidade à potência (0..1): 0 = reage pouco, 1 = reage muito
    private double powerSensitivity = 0.5;

    public AthleteProfile() {}

    public AthleteProfile(double fatigueFactor, double powerSensitivity) {
        this.fatigueFactor = fatigueFactor;
        this.powerSensitivity = powerSensitivity;
    }

    public double getFatigueFactor() {
        return fatigueFactor;
    }

    public void setFatigueFactor(double fatigueFactor) {
        this.fatigueFactor = fatigueFactor;
    }

    public double getPowerSensitivity() {
        return powerSensitivity;
    }

    public void setPowerSensitivity(double powerSensitivity) {
        this.powerSensitivity = powerSensitivity;
    }
}
