package org.example.utils;

import org.example.models.AthleteProfile;
import org.example.models.SensorReading;
import org.example.trainingtypes.TrainingType;

/**
 * Classe central: simula reps, aplica curva de fadiga variável,
 * prevê reps futuras e sugere ajuste de carga a cada repetição.
 */
public class DynamicAdjuster {

    private final AthleteProfile profile;
    // limiar relativo de falha (ex: 0.60 significa falha quando força < 60% da inicial)
    private double failureThreshold = 0.60;

    // taxa base de queda por repetição (porcentagem), usada como base
    private double baseDecayPerRep = 0.05; // 5% por rep (ajustável)

    public DynamicAdjuster(AthleteProfile profile) {
        this.profile = profile;
    }

    public void setFailureThreshold(double failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public void setBaseDecayPerRep(double baseDecayPerRep) {
        this.baseDecayPerRep = baseDecayPerRep;
    }

    /**
     * Simula quantas reps o atleta faria com a força inicial (F1),
     * usando uma curva de fadiga que depende de profile e de potência.
     *
     * @param initialReading leitura da 1ª repetição
     * @param trainingType tipo de treino (para faixa de reps)
     * @return número de reps simuladas até falha
     */
    public int simulateRepsFromFirst(SensorReading initialReading, TrainingType trainingType) {

        double F1 = initialReading.getForceN();
        double displacement = initialReading.getDisplacementM();
        double time = initialReading.getTimeS();

        double power = PhysicsUtils.calcPower(F1, displacement, time);

        // ajuste dinâmico do decay: atletas com maior powerSensitivity conservam melhor força
        double powerAdjustment = 1.0 - (profile.getPowerSensitivity() * normalizePower(power));
        // fatigueFactor >1 torna a fadiga maior
        double decay = baseDecayPerRep * profile.getFatigueFactor() * powerAdjustment;

        // garantir limites razoáveis
        decay = clamp(decay, 0.02, 0.12); // entre 2% e 12% por rep

        double currentForce = F1;
        int reps = 0;
        double threshold = F1 * failureThreshold;

        // simulação rep a rep
        while (currentForce >= threshold) {
            reps++;
            currentForce *= (1.0 - decay);
        }

        // limitar reps para não extrapolar infinitamente
        return Math.max(0, reps);
    }

    /**
     * Sugere nova carga (força em N) para o usuário falhar nas últimas reps do treino.
     * Strategy:
     *  - simula reps com F1
     *  - calcula o fator para mover repsSimuladas até repsTarget (repsMax-2)
     *  - sugere força de pico (F_sugerida). Retorna também conversão para kg se quiser.
     */
    public double suggestPeakForceForTarget(SensorReading initialReading, TrainingType trainingType) {
        int[] faixa = trainingType.getRepeticoes();
        int repsMax = faixa[faixa.length - 1];
        int targetFailRep = Math.max(1, repsMax - 2); // alvo de falha

        int repsSim = simulateRepsFromFirst(initialReading, trainingType);
        if (repsSim == 0) repsSim = 1; // evita divisão por zero

        // se já está no alvo, recomendamos F1
        if (repsSim == targetFailRep) {
            return initialReading.getForceN();
        }

        // Ajuste proporcional: se repsSim < target -> sugerir reduzir carga? na prática
        // Para aumentar reps (se repsSim < target) precisamos **diminuir** carga.
        // Para diminuir reps (se repsSim > target) precisamos **aumentar** carga.
        double factor = (double) targetFailRep / (double) repsSim;

        // ajuste sigmoidal leve para evitar saltos grandes
        factor = smoothFactor(factor);

        // Força sugerida = F1 * ajuste. Se factor <1 => diminuir força; >1 => aumentar força
        double suggestedForce = initialReading.getForceN() * factor;

        return suggestedForce;
    }

    /**
     * Dado sensor atual (por repetição), sugere ajuste incremental em kg para próxima repetição.
     * Retorna positivo = aumentar carga (mais difícil), negativo = reduzir.
     *
     * Ex.: se rep atual apresenta queda acentuada, sugerir reduzir carga.
     */
    public double suggestDeltaKgPerRep(SensorReading currentReading, SensorReading firstReading, TrainingType trainingType) {
        double currentForce = currentReading.getForceN();
        double F1 = firstReading.getForceN();

        // força relativa atual
        double rel = currentForce / F1;

        // se já perto do threshold => reduzir
        if (rel < failureThreshold + 0.05) {
            // sugerir reduzir 2-5% do peso equival. em kg
            double kg = PhysicsUtils.newtonToKg(currentForce);
            return -Math.max(0.5, kg * 0.03); // reduzir pelo menos 0.5kg ou 3%
        }

        // se está muito acima (pouca fadiga) => pode aumentar para chegar na zona alvo
        if (rel > failureThreshold + 0.20) {
            double kg = PhysicsUtils.newtonToKg(currentForce);
            return Math.max(0.5, kg * 0.03); // aumentar
        }

        return 0.0; // manter
    }

    // ---------- helpers ----------

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    // normalizar potência para [0,1] assumindo potência esperada até ~3000W (aprox)
    private static double normalizePower(double power) {
        double max = 3000.0;
        double v = power / max;
        return Math.max(0.0, Math.min(1.0, v));
    }

    // suaviza fator com função sigmoide leve
    private static double smoothFactor(double f) {
        // limitando range razoável (0.6 .. 1.4)
        double clamped = Math.max(0.6, Math.min(1.4, f));
        // aplicar leve suavização
        return 1.0 + 0.6 * (clamped - 1.0);
    }
}
