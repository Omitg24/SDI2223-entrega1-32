package com.uniovi.sdi.sdi2223entrega132.util;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SeleniumUtils {

	
	/**
	 * Aborta si el "texto" no está presente en la página actual
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param text: texto a buscar
	 */
	static public void textIsPresentOnPage(WebDriver driver, String text)
	{
		List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
		Assertions.assertTrue(list.size() > 0, "Texto " + text + " no localizado!");
	}

	/**
	 * Aborta si el "texto" está presente en la página actual
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param text: texto a buscar
	 */
	static public void textIsNotPresentOnPage(WebDriver driver, String text)
	{
		List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
		Assertions.assertEquals(0, list.size(), "Texto " + text + " no está presente !");
	}

	/**
	 * Aborta si el "texto" está presente en la página actual tras timeout segundos.
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param text: texto a buscar
	 * @param timeout: el tiempo máximo que se esperará por la aparición del texto a buscar
	 */
	static public void waitTextIsNotPresentOnPage(WebDriver driver, String text, int timeout)
	{
		Boolean resultado = 
				(new WebDriverWait(driver, timeout)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(text(),'" + text + "')]")));

		Assertions.assertTrue(resultado);
	}


	/**
	 * Espera por la visibilidad de un elemento/s en la vista actualmente cargandose en driver. Para ello se empleará una consulta xpath.
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param xpath: consulta xpath.
	 * @param timeout: el tiempo máximo que se esperará por la aparición del elemento a buscar con xpath
	 * @return  Se retornará la lista de elementos resultantes de la búsqueda con xpath.
	 */
	static public List<WebElement> waitLoadElementsByXpath(WebDriver driver, String xpath, int timeout)
	{
		WebElement result =
				(new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		Assertions.assertNotNull(result);
		return driver.findElements(By.xpath(xpath));
	}

	/**
	 * Espera por la visibilidad de un elemento/s en la vista actualmente cargandose en driver. Para ello se empleará una consulta xpath 
	 * según varios criterios..
	 * 
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param criterio: "id" or "class" or "text" or "@attribute" or "free". Si el valor de criterio es free es una expresion xpath completa. 
	 * @param text: texto correspondiente al criterio.
	 * @param timeout: el tiempo máximo que se esperará por la apareción del elemento a buscar con criterio/text.
	 * @return Se retornará la lista de elementos resultantes de la búsqueda.
	 */
	static public List<WebElement> waitLoadElementsBy(WebDriver driver, String criterio, String text, int timeout)
	{
		String searchCriterio;
		switch (criterio) {
			case "id":
				searchCriterio = "//*[contains(@id,'" + text + "')]";
				break;
			case "class":
				searchCriterio = "//*[contains(@class,'" + text + "')]";
				break;
			case "text":
				searchCriterio = "//*[contains(text(),'" + text + "')]";
				break;
			case "free":
				searchCriterio = text;
				break;
			default:
				searchCriterio = "//*[contains(" + criterio + ",'" + text + "')]";
				break;
		}

		return waitLoadElementsByXpath(driver, searchCriterio, timeout);
	}


	/**
	 * PROHIBIDO USARLO PARA VERSIÓN FINAL.
	 * Esperar "segundos" durante la ejecucion del navegador 
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param seconds: Segundos de bloqueo de la ejecución en el navegador.
	 */
	static public void waitSeconds(WebDriver driver, int seconds){

		//noinspection SynchronizationOnLocalVariableOrMethodParameter
		synchronized(driver){
			try {
				driver.wait(seconds * 1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
