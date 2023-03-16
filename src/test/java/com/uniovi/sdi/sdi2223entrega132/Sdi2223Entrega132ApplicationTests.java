package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import com.uniovi.sdi.sdi2223entrega132.services.InsertSampleDataService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.uniovi.sdi.sdi2223entrega132.pageobjects.*;

import java.util.List;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega132ApplicationTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private InsertSampleDataService insertSampleDataService;
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "geckodriver-v0.30.0-win64.exe";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8090";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
        usersRepository.deleteAll();

        // Metemos otra vez los datos iniciales de prueba
        insertSampleDataService.init();
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    /**
     * PR01. Registro de Usuario con datos válidos.
     * Realizada por: Omar
     */
    @Test
    @Order(1)
    public void PR01() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "uo123456@uniovi.es", "Adrián", "García Fernández", "123456", "123456");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        String checkText = "Listado de ofertas propias";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR02. Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
     * Realizada por: Omar
     */
    @Test
    @Order(2)
    public void PR02() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "", "", "", "123456", "123456");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.empty",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.empty",
                PO_Properties.getSPANISH()) + "\n" + PO_HomeView.getP().getString("error.user.email.length",
                PO_Properties.getSPANISH()) + "\n" + PO_HomeView.getP().getString("error.user.email.format",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

//    @Test
//    @Order(15)
//    public void PR15(){
//        // Iniciamos sesión como usuario estandar
//        PO_PrivateView.login(driver, "user@email.com", "123456");
//
//        //Pinchamos en la opción de menú de ofertas: //li[contains(@id, 'offers-menu')]/a
//        PO_PrivateView.checkViewAndClick(driver, "free", "//li[contains(@id, 'offers-menu')]/a",0);
//        //Esperamos a que aparezca la opción de añadir oferta: //a[contains(@href, 'offer/add')]
//        PO_PrivateView.checkViewAndClick(driver, "free", "//a[contains(@href, 'offer/add')]",0);
//
//        // Rellenamos el formulario de alta de oferta con datos validos
//        PO_PrivateView.fillFormAddOffer(driver, "PruebaTitulo", "PruebaDescripcion", "0.21");
//
//        // Comprobamos que la oferta recien añadida sale en la lista de ofertas propias
//        // del usuario
//        PO_PrivateView.checkElement(driver, "PruebaTitulo");
//        PO_PrivateView.checkElement(driver, "PruebaDescripcion");
//        PO_PrivateView.checkElement(driver,"0.21");
//
//        // Hacemos logout
//        PO_PrivateView.logout(driver);
//    }
}

