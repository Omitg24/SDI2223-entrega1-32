package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_HomeView extends PO_NavView {
    /**
     * Método que compruba que se muestra el mensaje de bienvenida
     *
     * @param driver   driver
     * @param language idioma de la pagina
     */
    static public void checkWelcomeToPage(WebDriver driver, int language) {
        //Esperamos a que se cargue el saludo de bienvenida en Español
        SeleniumUtils.waitLoadElementsBy(driver, "text", p.getString("msg.signup", language),
                getTimeout());
    }

    /**
     * Método que devuelve el elemto con el texto de bienvenida
     *
     * @param driver   driver
     * @param language idioma de la pagina
     * @return lista con los elementos que contienen el texto de bienvenida
     */
    static public List<WebElement> getWelcomeMessageText(WebDriver driver, int language) {
        //Esperamos a que se cargue el saludo de bienvenida en Español
        return SeleniumUtils.waitLoadElementsBy(driver, "text", p.getString("welcome.message", language),
                getTimeout());
    }

    /**
     * Método que comprueba que se cambia el idioma de la pagina
     *
     * @param driver        driver
     * @param textLanguage1 texto de prueba para el primer idioma
     * @param textLanguage  texto de prueba para el segundo idioma
     * @param locale1       primer idioma
     * @param locale2       segundo idioma
     */
    static public void checkChangeLanguage(WebDriver driver, String textLanguage1, String textLanguage,
                                           int locale1, int locale2) {
        //Esperamos a que se cargue el saludo de bienvenida en Español
        PO_HomeView.checkWelcomeToPage(driver, locale1);
        //Cambiamos a segundo idioma
        PO_HomeView.changeLanguage(driver, textLanguage);
        //Comprobamos que el texto de bienvenida haya cambiado a segundo idioma
        PO_HomeView.checkWelcomeToPage(driver, locale2);
        //Volvemos a Español.
        PO_HomeView.changeLanguage(driver, textLanguage1);
        //Esperamos a que se cargue el saludo de bienvenida en Español
        PO_HomeView.checkWelcomeToPage(driver, locale1);
    }

}
