package poo_caixa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class InterfaceCaixa extends JFrame {

    private CaixaEletronico caixa = new CaixaEletronico();
    private JTextArea area;

    public InterfaceCaixa() {
        setTitle("Caixa Eletrônico");
        setSize(500, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(18, 18, 30));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel botoes = new JPanel(new GridLayout(0, 1, 10, 10));
        botoes.setBackground(new Color(18, 18, 30));

        botoes.add(criarBotao("Efetuar Saque",        new Color(0, 200, 140),  e -> sacar()));
        botoes.add(criarBotao("Relatório de Cédulas", new Color(255, 140, 0),  e -> area.setText(caixa.pegaRelatorioCedulas())));
        botoes.add(criarBotao("Valor Total Disponível",new Color(0, 140, 255), e -> area.setText(caixa.pegaValorTotalDisponivel())));
        botoes.add(criarBotao("Reposição de Cédulas", new Color(170, 80, 255), e -> repor()));
        botoes.add(criarBotao("Cota Mínima",          new Color(255, 80, 120), e -> cota()));
        botoes.add(criarBotao("Sair / Extrato",        new Color(220, 60, 60),  e -> sair()));

        area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 15));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(28, 28, 42));
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(area);

        main.add(botoes, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);

        add(main);
        area.setText("Bem-vindo! Escolha uma operação.");
    }

    private JButton criarBotao(String texto, Color cor, ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.addActionListener(acao);
        return botao;
    }

    private void sacar() {
        String valor = JOptionPane.showInputDialog(this, "Digite o valor do saque (R$):");
        if (valor == null) return;
        try {
            area.setText(caixa.sacar(Integer.parseInt(valor.trim())));
        } catch (NumberFormatException e) {
            area.setText("Valor inválido! Digite apenas números inteiros.");
        }
    }

    private void repor() {
        String[] opcoes = {"100", "50", "20", "10", "5", "2"};
        String cedula = (String) JOptionPane.showInputDialog(
            this,
            "Selecione a cédula:",
            "Reposição de Cédulas",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );
        if (cedula == null) return;

        String qtd = JOptionPane.showInputDialog(this, "Quantidade de notas de R$ " + cedula + ":");
        if (qtd == null) return;

        try {
            area.setText(caixa.reposicaoCedulas(Integer.parseInt(cedula), Integer.parseInt(qtd.trim())));
        } catch (NumberFormatException e) {
            area.setText("Valor inválido para reposição!");
        }
    }

    private void cota() {
        String valor = JOptionPane.showInputDialog(this, "Digite a cota mínima do caixa (R$):");
        if (valor == null) return;
        try {
            area.setText(caixa.armazenaCotaMinima(Integer.parseInt(valor.trim())));
        } catch (NumberFormatException e) {
            area.setText("Valor inválido para cota mínima!");
        }
    }

    private void sair() {
        // Monta o extrato usando os dados reais do CaixaEletronico
        ArrayList<String> ops = caixa.getExtrato();

        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════\n");
        sb.append("        EXTRATO DO CAIXA\n");
        sb.append("════════════════════════════\n\n");

        if (ops.isEmpty()) {
            sb.append("Nenhuma operação realizada.\n");
        } else {
            for (int i = 0; i < ops.size(); i++) {
                sb.append((i + 1)).append(". ").append(ops.get(i)).append("\n");
            }
        }

        sb.append("\n════════════════════════════\n");
        sb.append(caixa.pegaValorTotalDisponivel()).append("\n");
        sb.append("════════════════════════════\n");

        area.setText(sb.toString());

        // Pergunta se deseja realmente sair
        int confirmar = JOptionPane.showConfirmDialog(
            this,
            "Deseja encerrar o atendimento?",
            "Sair",
            JOptionPane.YES_NO_OPTION
        );
        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceCaixa().setVisible(true));
    }
}