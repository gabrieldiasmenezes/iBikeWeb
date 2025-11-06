package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

public class MotosTests extends TestConfig {

    private static final String LISTA_URL = "/motos";
    private static final String NOVO_URL = "/motos/novo";

    @Test
    @DisplayName("Cadastro de moto via formulário com sucesso")
    public void cadastroMotoComSucesso() {
        // Faz login e vai para a página de nova moto
        loginComoAdmin();
        irPara(NOVO_URL);

        // Espera o campo de placa estar visível
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("placa"))).sendKeys("CAB-1234");

        // Preenche os outros campos
        driver.findElement(By.name("modelo")).sendKeys("Honda CG 160");
        driver.findElement(By.name("kmAtual")).sendKeys("10");

        // Submete o formulário
        clicarBotaoSubmit();

        // Aguarda o redirecionamento para a lista de motos
        aguardarRedirecionamento(LISTA_URL);

        // Verifica se foi redirecionado corretamente
        assertTrue(driver.getCurrentUrl().contains(LISTA_URL),
                "Deveria ser redirecionado para a lista de motos após salvar");

        // Verifica se a moto aparece na lista com status correto
        assertTrue(motoNaLista("CAB-1234"), "Moto deve aparecer na lista");
        assertTrue(statusMotoNaLista("CAB-1234", "NO_PATIO"), "Status deve ser NO_PATIO");
    }


    @Test
    @DisplayName("Acesso ao formulário de cadastro sem login redireciona")
    public void acessoFormularioSemLogin() {
        irPara(NOVO_URL);
        aguardarRedirecionamento("/login");

        assertTrue(driver.getCurrentUrl().contains("/login"));
        assertTrue(elementoExiste(By.name("username")));
    }

    @Test
    @DisplayName("Listagem mostra motos do pátio do admin logado")
    public void listagemMotosDoPatio() {
        loginComoAdmin();
        irPara(LISTA_URL);

        aguardarRedirecionamento(LISTA_URL);
        assertTrue(textoNaPagina("Lista de Motos") || textoNaPagina("motos"),
            "Página de listagem deve carregar corretamente");

        assertFalse(textoNaPagina("Internal Server Error"));
    }


    @Test
    @DisplayName("Cadastro com placa duplicada mostra erro de validação")
    public void cadastroPlacaDuplicada() {
        // Login como administrador
        loginComoAdmin();

        // 🔹 Primeiro cadastro
        irPara(NOVO_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("placa"))).sendKeys("DUP-9999");
        driver.findElement(By.name("modelo")).sendKeys("Moto Duplicada");
        driver.findElement(By.name("kmAtual")).sendKeys("10.0");
        clicarBotaoSubmit();

        // Aguarda o redirecionamento para a lista
        aguardarRedirecionamento(LISTA_URL);
        assertTrue(driver.getCurrentUrl().contains(LISTA_URL), "Deveria ser redirecionado para a lista de motos");

        // 🔹 Segundo cadastro com a MESMA placa
        irPara(NOVO_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("placa"))).sendKeys("DUP-9999");
        driver.findElement(By.name("modelo")).sendKeys("Outra Moto");
        driver.findElement(By.name("kmAtual")).sendKeys("5.0");
        clicarBotaoSubmit();

        // 🔹 Espera aparecer mensagem de erro
        String erro = obterMensagemErro();

        // 🔹 Valida o conteúdo da mensagem
        assertTrue(
            erro != null && erro.toLowerCase().contains("placa") && erro.toLowerCase().contains("já"),
            "Erro de placa duplicada esperado. Encontrado: '" + erro + "'"
        );

        // 🔹 Garante que continua na tela de cadastro (não redirecionou)
        assertTrue(driver.getCurrentUrl().contains("/motos/novo") || driver.getCurrentUrl().contains("/salvar"),
                "Deveria permanecer na página de cadastro em caso de erro");
    }

}