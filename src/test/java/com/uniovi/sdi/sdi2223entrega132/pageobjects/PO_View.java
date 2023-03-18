package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import com.uniovi.sdi.sdi2223entrega132.util.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_View {

    protected static PO_Properties p = new PO_Properties("messages");
    protected static int timeout = 2;

    public static int getTimeout() {
        return timeout;
    }

    public static void setTimeout(int timeout) {
        PO_View.timeout = timeout;
    }

    public static PO_Properties getP() {
        return p;
    }

    public static void setP(PO_Properties p) {
        PO_View.p = p;
    }

    /**
     * Espera por la visibilidad de un texto correspondiente a la propiedad key en el idioma locale en la vista actualmente cargandose en driver..
     *
     * @param driver: apuntando al navegador abierto actualmente.
     * @param key:    clave del archivo de propiedades.
     * @param locale: Retorna el índice correspondient al idioma. 0 p.SPANISH y 1 p.ENGLISH.
     * @return Se retornará la lista de elementos resultantes de la búsqueda.
     */
    static public List<WebElement> checkElementByKey(WebDriver driver, String key, int locale) {
        return SeleniumUtils.waitLoadElementsBy(driver, "text", p.getString(key, locale), getTimeout());
    }

    /**
     * Espera por la visibilidad de un elemento/s en la vista actualmente cargandose en driver..
     *
     * @param driver: apuntando al navegador abierto actualmente.
     * @param type:
     * @param text:
     * @return Se retornará la lista de elementos resultantes de la búsqueda.
     */
    static public List<WebElement> checkElementBy(WebDriver driver, String type, String text) {
        return SeleniumUtils.waitLoadElementsBy(driver, type, text, getTimeout());
    }
}
