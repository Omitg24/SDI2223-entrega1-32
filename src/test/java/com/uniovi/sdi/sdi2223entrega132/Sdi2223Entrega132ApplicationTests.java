package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import com.uniovi.sdi.sdi2223entrega132.services.InsertSampleDataService;
import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
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

    // 1. Público: Registrarse como usuario
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
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.empty",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.empty",
                PO_Properties.getSPANISH()) + "\n" + PO_HomeView.getP().getString("error.user.email.length",
                PO_Properties.getSPANISH()) + "\n" + PO_HomeView.getP().getString("error.user.email.format",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR03. Registro de Usuario con datos inválidos (repetición de contraseña inválida).
     * Realizada por: Omar
     */
    @Test
    @Order(3)
    public void PR03() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "uo123456@uniovi.es", "Adrián", "García Fernández", "123456", "654321");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.user.passwordConfirm.coincidence",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.user.passwordConfirm.coincidence",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR04. Registro de Usuario con datos inválidos (email existente).
     * Realizada por: Omar
     */
    @Test
    @Order(4)
    public void PR04() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_SignUpView.fillForm(driver, "user@email.com", "Adrián", "García Fernández", "123456", "123456");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "error.user.email.duplicate",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("error.user.email.duplicate",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    // 2. Usuario Registrado: Iniciar sesión
    /**
     * PR05. Inicio de sesión con datos válidos (administrador).
     * Realizada por: Omar
     */
    @Test
    @Order(5)
    public void PR05() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la sección de listar usuarios
        String checkText = "Usuarios";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR06. Inicio de sesión con datos válidos (usuario estándar).
     * Realizada por: Omar
     */
    @Test
    @Order(6)
    public void PR06() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "user@email.com", "123456");
        //Comprobamos que entramos en la sección de listado de ofertas propias
        String checkText = "Listado de ofertas propias";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR07. Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
     * Realizada por: Omar
     */
    @Test
    @Order(7)
    public void PR07() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "", "");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "msg.login.error",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("msg.login.error",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR08. Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña
     * incorrecta).
     * Realizada por: Omar
     */
    @Test
    @Order(8)
    public void PR08() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "user@email.com", "user");
        //Comprobamos los errores
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "msg.login.error",
                PO_Properties.getSPANISH());
        String checkText = PO_HomeView.getP().getString("msg.login.error",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR09. Hacer clic en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
     * sesión (Login).
     * Realizada por: Omar
     */
    @Test
    @Order(9)
    public void PR09() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Nos desconectamos
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-dark");
        //Comprobamos que hemos cerrado la sesión
        String checkText = "Identificarse";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.get(0).getText());
    }

    /**
     * PR10. Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
     * Realizada por: Omar
     */
    @Test
    @Order(10)
    public void PR10() {
        //Comprobamos que el elemento no está
        List<WebElement> result = driver.findElements(By.xpath("//a[contains(@href, '/logout')]"));
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * PR11. Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
     * sistema.
     * Realizada por: Omar
     */
    @Test
    @Order(11)
    public void PR11() {
        //Vamos al formulario de logueo
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-dark");
        //Rellenamos el formulario.
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr", PO_View.getTimeout());
        Assertions.assertEquals(2, result.size());
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

