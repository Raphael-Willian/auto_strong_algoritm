package org.example.models;

/**
 * Representa um único "evento" de sensor por repetição.
 * forceN: força média aplicada nessa repetição, em Newtons.
 * displacementM: deslocamento do movimento (m).
 * timeS: tempo da repetição (s).
 */
public class SensorReading {
    private final double forceN;
    private final double displacementM;
    private final double timeS;

    public SensorReading(double forceN, double displacementM, double timeS) {
        this.forceN = forceN;
        this.displacementM = displacementM;
        this.timeS = timeS;
    }

    public double getForceN() {
        return forceN;
    }

    public double getDisplacementM() {
        return displacementM;
    }

    public double getTimeS() {
        return timeS;
    }
}
