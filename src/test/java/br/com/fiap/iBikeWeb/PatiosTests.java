package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class PatiosTests extends TestConfig {

    private static final String LISTA_URL = "/patios";

    @Test
    @DisplayName("Admin consegue visualizar a lista de pátios ativos")
    public void listarPatiosAtivos() {
        loginComoAdmin();
        irPara(LISTA_URL);

        // Verifica se há tabela/lista de pátios
        assertTrue(
            textoNaPagina("Lista de Pátios") || elementoExiste(By.cssSelector("table")),
            "Página deve exibir tabela de pátios."
        );

        // Verifica se há pelo menos uma linha no tbody (ou seja, pátio listado)
        boolean possuiLinhas = elementoExiste(By.xpath("//table//tbody//tr"));
        assertTrue(possuiLinhas, "Deve haver pelo menos um pátio listado na tabela.");
    }

    @Test
    @DisplayName("Admin edita pátio existente com sucesso")
    public void editarPatioComSucesso() {
        loginComoAdmin();
        irPara(LISTA_URL);

        // Clica em editar o primeiro pátio da lista
        clicarBotaoPorTexto("Editar");

        // Preenche novo nome e capacidade
        preencherFormularioPatio("Pátio Atualizado", "150");
        clicarBotaoSubmit();

        // Aguarda voltar para lista
        aguardarRedirecionamento(LISTA_URL);

        // Verifica se atualização refletiu na lista
        assertTrue(patioNaLista("Pátio Atualizado"), "O nome do pátio deve ter sido atualizado.");
    }

    @Test
    @DisplayName("Admin tenta salvar pátio com capacidade inválida e vê erro (validação do navegador)")
    public void editarPatioCapacidadeInvalida() {
        loginComoAdmin();
        irPara(LISTA_URL);

        clicarBotaoPorTexto("Editar");

        preencherFormularioPatio("Pátio Teste Capacidade", "-20");

        // Antes de clicar em "Salvar", capturamos a mensagem de validação do navegador
        WebElement campoCapacidade = driver.findElement(By.name("capacidade"));

        // Tenta submeter — o navegador impedirá o envio
        clicarBotaoSubmit();

        // Captura a mensagem nativa de validação HTML5
        String validationMessage = (String) ((JavascriptExecutor) driver)
            .executeScript("return arguments[0].validationMessage;", campoCapacidade);

        // Usa também o método padrão, caso o backend retorne algo
        String erro = obterMensagemErro();

        // Teste passa se aparecer erro do navegador ou do backend
        assertTrue(
            (!validationMessage.isEmpty() &&
                (validationMessage.toLowerCase().contains("maior ou igual")
                || validationMessage.toLowerCase().contains("greater than or equal")
                || validationMessage.toLowerCase().contains("positivo")
                || validationMessage.toLowerCase().contains("positive")
                || validationMessage.toLowerCase().contains("válido")
                || validationMessage.toLowerCase().contains("valid")))
            || erro.toLowerCase().contains("capacidade")
            || textoNaPagina("capacidade deve ser positiva"),
            "Deve exibir mensagem de erro do navegador ou do sistema. Encontrado: " + validationMessage + " / " + erro
        );

        // Verifica que continua na página de edição (não redirecionou)
        assertTrue(driver.getCurrentUrl().contains("/patios/editar"),
            "Deve permanecer na página de edição (envio bloqueado pelo navegador).");
    }

    @Test
    @DisplayName("Funcionário tenta acessar /patios e não tem permissão")
    public void funcionarioNaoTemAcessoAosPatios() {
        loginComoFuncionario();
        irPara(LISTA_URL);

        // Espera até 5 segundos para o redirecionamento acontecer
        wait.until(driver -> 
            driver.getCurrentUrl().contains("/acesso-negado") ||
            driver.getCurrentUrl().contains("/home") ||
            textoNaPagina("Acesso Negado") ||
            textoNaPagina("403")
        );

        boolean foiRedirecionado =
            driver.getCurrentUrl().contains("/acesso-negado") ||
            driver.getCurrentUrl().contains("/home") ||
            textoNaPagina("Acesso Negado") ||
            textoNaPagina("403 Forbidden");

        assertTrue(
            foiRedirecionado,
            "Funcionário não deve acessar a página de pátios."
        );
    }
}
