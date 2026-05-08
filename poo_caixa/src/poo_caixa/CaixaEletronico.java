package poo_caixa;

import java.util.ArrayList;

public class CaixaEletronico implements ICaixaEletronico {


    private int[][] cedulas = {
        {100, 100},
        {50,  200},
        {20,  300},
        {10,  350},
        {5,   450},
        {2,   500}
    };

    private int cotaMinima = 0;
    private ArrayList<String> extrato = new ArrayList<>();

    
    public String pegaRelatorioCedulas() {
        String resposta = "RELATÓRIO DE CÉDULAS\n";
        for (int i = 0; i < cedulas.length; i++) {
            resposta += "R$ " + cedulas[i][0] + " -> " + cedulas[i][1] + " notas\n";
        }
        return resposta;
    }

    @Override
    public String pegaValorTotalDisponivel() {
        return "Valor total disponível: R$ " + valorTotal();
    }

    @Override
    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        if (quantidade <= 0) {
            return "Quantidade inválida!";
        }
        for (int i = 0; i < cedulas.length; i++) {
            if (cedulas[i][0] == cedula) {
                cedulas[i][1] += quantidade;
                extrato.add("Reposição: " + quantidade + " cédulas de R$ " + cedula);
                return "Reposição realizada! Agora há " + cedulas[i][1] + " notas de R$ " + cedula;
            }
        }
        return "Cédula inválida! Use: 2, 5, 10, 20, 50 ou 100";
    }

    
    public String armazenaCotaMinima(Integer minimo) {
        if (minimo < 0) {
            return "Cota mínima inválida!";
        }
        cotaMinima = minimo;
        extrato.add("Cota mínima definida: R$ " + minimo);
        return "Cota mínima salva: R$ " + minimo;
    }

    @Override
    public String sacar(Integer valor) {

        if (valor <= 0) {
            return "Valor inválido para saque";
        }

        if (valorTotal() - valor < cotaMinima) {
            return "Caixa Vazio: Chame o Operador";
        }

        int[] usadas = new int[cedulas.length];

        if (!buscarCedulas(valor, 0, usadas)) {
            return "Saque não realizado por falta de cédulas";
        }

        int totalNotas = 0;
        for (int i = 0; i < usadas.length; i++) {
            totalNotas += usadas[i];
        }

        if (totalNotas > 30) {
            return "Saque não permitido: mais de 30 cédulas seriam emitidas";
        }

        for (int i = 0; i < cedulas.length; i++) {
            cedulas[i][1] -= usadas[i];
        }

        String resposta = "Saque realizado:\n";
        for (int i = 0; i < usadas.length; i++) {
            if (usadas[i] > 0) {
                resposta += "R$ " + cedulas[i][0] + " -> " + usadas[i] + " notas\n";
            }
        }

        resposta += "Saldo atual: R$ " + valorTotal();
        extrato.add("Saque: R$ " + valor + " | Saldo restante: R$ " + valorTotal());

        return resposta;
    }

    private boolean buscarCedulas(int valor, int indice, int[] usadas) {

        if (valor == 0) {
            return true;
        }

        if (indice >= cedulas.length) {
            return false;
        }

        int nota = cedulas[indice][0];
        int disponivel = cedulas[indice][1];

        int max = valor / nota;
        if (max > disponivel) {
            max = disponivel;
        }

        for (int qtd = max; qtd >= 0; qtd--) {
            usadas[indice] = qtd;

            if (buscarCedulas(valor - (qtd * nota), indice + 1, usadas)) {
                return true;
            }
        }

        usadas[indice] = 0;
        return false;
    }

    private int valorTotal() {
        int total = 0;
        for (int i = 0; i < cedulas.length; i++) {
            total += cedulas[i][0] * cedulas[i][1];
        }
        return total;
    }

    public ArrayList<String> getExtrato() {
        return extrato;
    }
}

