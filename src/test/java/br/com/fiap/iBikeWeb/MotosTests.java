package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class MotosTests extends TestConfig {

    private static final String LISTA_URL = "/motos";
    private static final String NOVO_URL = "/motos/novo";

    @Test
    @DisplayName("Cadastro de moto via formulário com sucesso")
    public void cadastroMotoComSucesso() {
        loginComoAdmin();
        irPara(NOVO_URL);
        preencherFormularioMoto("ABC1234", "Honda CG 160", "1");
        clicarBotaoSubmit();
        aguardarRedirecionamento(LISTA_URL);
        assertTrue(driver.getCurrentUrl().contains(LISTA_URL));
        assertTrue(motoNaLista("ABC1234"), "Moto deve aparecer na lista");
        assertTrue(statusMotoNaLista("ABC1234", "NO_PATIO"), "Status deve ser NO_PATIO");
    }

    @Test
    @DisplayName("Acesso ao formulário de cadastro sem login redireciona")
    public void acessoFormularioSemLogin() {
        irPara(NOVO_URL);
        aguardarRedirecionamento("/login");

        assertTrue(driver.getCurrentUrl().contains("/login"));
        assertTrue(elementoExiste(By.name("email")));
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
        loginComoAdmin();

        // Primeiro cadastro
        irPara(NOVO_URL);
        preencherFormularioMoto("DUP999", "Moto Duplicada", "1");
        clicarBotaoSubmit();
        aguardarRedirecionamento(LISTA_URL);

        // Segundo cadastro com mesma placa
        irPara(NOVO_URL);
        preencherFormularioMoto("DUP999", "Outra Moto", "1");
        clicarBotaoSubmit();

        String erro = obterMensagemErro();
        assertTrue(
            erro.toLowerCase().contains("placa") && erro.toLowerCase().contains("já"),
            "Erro de placa duplicada esperado. Encontrado: '" + erro + "'"
        );
    }
}