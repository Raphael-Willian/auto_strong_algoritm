package org.example.utils;

/**
 * Utilitários físicos: conversões, velocidade, potência, conversão N->kg (aprox).
 */
public class PhysicsUtils {

    public static double newtonToKg(double newton) {
        return newton / 9.8;
    }

    // velocidade média (m/s)
    public static double calcVelocity(double displacementM, double timeS) {
        if (timeS <= 0) return 0;
        return displacementM / timeS;
    }

    // potência média (W) = força * velocidade
    public static double calcPower(double forceN, double displacementM, double timeS) {
        double vel = calcVelocity(displacementM, timeS);
        return forceN * vel;
    }
}
