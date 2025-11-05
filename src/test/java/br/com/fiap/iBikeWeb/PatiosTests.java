package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PatiosTests extends TestConfig {

    private static final String LISTA_URL = "/patios";

    @Test
    @DisplayName("Criação de pátio com dados válidos")
    public void criarPatioComSucesso() {
        loginComoAdmin();
        irPara(LISTA_URL);

        clicarBotaoPorTexto("Novo Pátio");
        preencherFormularioPatio("Pátio Leste", "50");
        clicarBotaoSubmit();

        aguardarRedirecionamento(LISTA_URL);
        assertTrue(patioNaLista("Pátio Leste"), "Pátio deve aparecer na lista");
        assertTrue(statusPatioNaLista("Pátio Leste", "ATIVO"), "Status deve ser ATIVO");
    }

    @Test
    @DisplayName("Tentativa de criar pátio com capacidade negativa")
    public void criarPatioCapacidadeNegativa() {
        loginComoAdmin();
        irPara(LISTA_URL);
        clicarBotaoPorTexto("Novo Pátio");

        preencherFormularioPatio("Pátio Norte", "-10");
        clicarBotaoSubmit();

        String erro = obterMensagemErro();
        assertTrue(
            erro.toLowerCase().contains("capacidade") && 
            erro.toLowerCase().contains("positivo"),
            "Erro de validação esperado. Encontrado: '" + erro + "'"
        );
        assertTrue(driver.getCurrentUrl().contains("/patios"));
    }


    @Test
    @DisplayName("Tentativa de criar pátio com nome vazio")
    public void criarPatioNomeVazio() {
        loginComoAdmin();
        irPara(LISTA_URL);
        clicarBotaoPorTexto("Novo Pátio");

        // Garante nome vazio
        driver.findElement(By.name("nome")).clear();
        driver.findElement(By.name("capacidade")).clear();
        driver.findElement(By.name("capacidade")).sendKeys("30");
        clicarBotaoSubmit();

        String erro = obterMensagemErro();
        assertTrue(
            erro.toLowerCase().contains("nome") && 
            erro.toLowerCase().contains("obrigatório"),
            "Erro de nome obrigatório esperado. Encontrado: '" + erro + "'"
        );
        assertTrue(driver.getCurrentUrl().contains("/patios"));
    }
}