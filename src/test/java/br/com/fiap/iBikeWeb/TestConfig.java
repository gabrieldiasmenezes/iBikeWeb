package br.com.fiap.iBikeWeb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class TestConfig {

    public static final String BASE_URL = "http://localhost:8080";
    public static final String EMAIL_ADMIN = "admin@ibike.com";
    public static final String SENHA_ADMIN = "123456";
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ============== FUNÇÃO DE LOGIN ==============
    protected void fazerLogin(String email, String senha) {
        driver.get(BASE_URL + "/login");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(senha);
        driver.findElement(By.id("btn-login")).click();
    }
}