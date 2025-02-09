package view;

import dao.ProdutoDao;
import model.Produto;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

public class Main {
    ProdutoDao produtoDao = new ProdutoDao();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Main().menu();
    }

    public void menu() throws IOException, ClassNotFoundException {
        String[] opcoes = {"Cadastrar Produto", "Listar Produtos", "Editar Produto", "Deletar Produto", "Sair"};
        int opcao;

        do {
            opcao = JOptionPane.showOptionDialog(
                    null,
                    "Escolha uma opção:",
                    "------Menu do Supermercado------",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[4]
            );
            switch (opcao) {
                case 0:
                    cadastrarProduto();
                    break;
                case 1: listarProdutos(); break;
                case 2: editarProduto(); break;
                case 3: deletarProduto(); break;
                case 4:
                case -1:
                    JOptionPane.showMessageDialog(null, "Saindo do Menu...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção invalida");
            }
        }
        while (opcao != 4 && opcao != -1);
    }

    private void cadastrarProduto() {

        try{
            int id = Integer.parseInt(JOptionPane.showInputDialog("Digite o código do produto:"));
            String descricao = JOptionPane.showInputDialog("Digite a descrição do produto:");
            float preco = Float.parseFloat(JOptionPane.showInputDialog("Digite o valor do produto:"));
            String validadeString = JOptionPane.showInputDialog("Digite a data de validade do produto:");
            LocalDate validade = LocalDate.parse(validadeString);

            //produtoDao.adicionarProduto(new Produto(id, descricao, preco, validade));
            Produto novoProd = new Produto(id, descricao, preco, validade);
            produtoDao.adicionarProduto(novoProd);
            System.out.println(novoProd);
        }
        catch (IOException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto");
        }
    }

    private void listarProdutos() {
        try {
            Set<Produto> produtos = produtoDao.getProdutos();

            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null,"A lista de produtos está vazia");
            } else {
                StringBuilder listaProdutos = new StringBuilder("---Lista de Produtos---\n");
                for (Produto produto : produtos) {
                    listaProdutos.append(produto).append("\n");
                }
                JOptionPane.showMessageDialog(null, listaProdutos.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao listar os produtos: " + e.getMessage());
        }
    }

    private void editarProduto() {
        try {
            Set<Produto> produtos = produtoDao.getProdutos();
            String idString = JOptionPane.showInputDialog("Informe o id do produto que deseja editar: ");
            int id = Integer.parseInt(idString);
            for (Produto produto : produtos) {
                if (produto.getId() == id) {
                    String descricaoNova = JOptionPane.showInputDialog("Descrição: ", produto.getDescricao());
                    float precoNovo = Float.parseFloat(JOptionPane.showInputDialog("Preço:", produto.getPreco()));
                    LocalDate validadeNova = LocalDate.parse(JOptionPane.showInputDialog("Validade:", produto.getValidade().toString()));

                    Produto produtoEditado = new Produto(id, descricaoNova, precoNovo, validadeNova);
                    if (produtoDao.atualizarProduto(produtoEditado)) {
                        JOptionPane.showMessageDialog(null, "Produto editado com sucesso!");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao listar os produtos: " + e.getMessage());
        }
    }

    private void deletarProduto() {
        try {
            Set<Produto> produtos = produtoDao.getProdutos();
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "A lista de produtos está vazia");
                return;
            }
            else{
                String idString = JOptionPane.showInputDialog("Informe o id do produto que deseja deletar: ");
                int id = Integer.parseInt(idString);
                for (Produto produto : produtos) {
                    if (produto.getId() == id) {
                        produtoDao.removerProduto(produto);
                        JOptionPane.showMessageDialog(null, "Produto removido com sucesso!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao deletar o produto: " + e.getMessage());
        }
    }
}

