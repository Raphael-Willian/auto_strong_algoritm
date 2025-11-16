package org.example;

import org.example.models.AthleteProfile;
import org.example.models.SensorReading;
import org.example.trainingtypes.Forca;
import org.example.trainingtypes.Hipertrofia;
import org.example.trainingtypes.Resistencia;
import org.example.trainingtypes.TrainingType;
import org.example.utils.DynamicAdjuster;
import org.example.utils.PhysicsUtils;

import java.util.Scanner;

/**
 * Demo console. Substitua as leituras simuladas por leituras reais do sensor.
 */
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // perfil de atleta (poderá ser carregado por usuário)
        AthleteProfile profile = new AthleteProfile(1.0, 0.5);
        DynamicAdjuster adjuster = new DynamicAdjuster(profile);

        TrainingType hipertrofia = new Hipertrofia();
        TrainingType forca = new Forca();
        TrainingType resistencia = new Resistencia();

        System.out.println("=== Smart Trainer Simulator ===");

        while (true) {
            System.out.println("\nEscolha o tipo de treino:");
            System.out.println("1 - Força");
            System.out.println("2 - Hipertrofia");
            System.out.println("3 - Resistência");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            int op = sc.nextInt();
            if (op == 0) break;

            TrainingType treino = switch (op) {
                case 1 -> forca;
                case 2 -> hipertrofia;
                case 3 -> resistencia;
                default -> null;
            };
            if (treino == null) continue;

            System.out.println("Insira leitura do sensor da 1ª repetição:");
            System.out.print("Força (N): ");
            double F1 = sc.nextDouble();
            System.out.print("Deslocamento (m) da repetição (ex: 0.4): ");
            double disp = sc.nextDouble();
            System.out.print("Tempo da repetição (s) (ex: 0.8): ");
            double time = sc.nextDouble();

            SensorReading first = new SensorReading(F1, disp, time);

            // 1) prever quantas reps faria com essa força
            int repsSim = adjuster.simulateRepsFromFirst(first, treino);

            // 2) calcular carga sugerida (força pico) para falhar nas últimas reps da faixa
            double suggestedForce = adjuster.suggestPeakForceForTarget(first, treino);
            double suggestedKg = PhysicsUtils.newtonToKg(suggestedForce);

            // 3) calcular potência/velocidade da 1ª rep
            double vel = PhysicsUtils.calcVelocity(disp, time);
            double power = PhysicsUtils.calcPower(F1, disp, time);

            System.out.println("\n--- Resultado inicial ---");
            System.out.printf("Reps estimadas com F1: %d reps\n", repsSim);
            System.out.printf("Velocidade média (m/s): %.3f\n", vel);
            System.out.printf("Potência média (W): %.2f\n", power);
            System.out.printf("Carga sugerida (força): %.2f N -> %.2f kg\n", suggestedForce, suggestedKg);

            // 4) Simular stream de reps em tempo real (exemplo: sensor real enviará lidas por rep)
            System.out.println("\nSimular stream de sensor de reps? (1-sim / 0-não)");
            int sim = sc.nextInt();
            if (sim == 1) {
                // pedimos o usuário inserir leituras por repetição (ou usar dados gravados)
                SensorReading current = first;
                int repCount = 1;
                while (true) {
                    // mostrar sugestão dinâmica com base na leitura atual
                    double deltaKg = adjuster.suggestDeltaKgPerRep(current, first, treino);
                    if (Math.abs(deltaKg) > 0.001) {
                        String action = deltaKg > 0 ? "AUMENTAR" : "DIMINUIR";
                        System.out.printf("SUGESTÃO (rep %d): %s carga em %.2f kg\n", repCount, action, Math.abs(deltaKg));
                    } else {
                        System.out.printf("SUGESTÃO (rep %d): Manter carga\n", repCount);
                    }

                    System.out.println("Inserir próxima leitura do sensor (ou '0' na força para encerrar stream):");
                    System.out.print("Força (N): ");
                    double fn = sc.nextDouble();
                    if (fn == 0) break;
                    System.out.print("Deslocamento (m): ");
                    double d = sc.nextDouble();
                    System.out.print("Tempo (s): ");
                    double t = sc.nextDouble();

                    SensorReading next = new SensorReading(fn, d, t);
                    repCount++;

                    // atualizar estado
                    current = next;
                }
            }

            System.out.println("\n--- FIM DO CICLO ---");
        }

        sc.close();
        System.out.println("Finalizado.");
    }
}
