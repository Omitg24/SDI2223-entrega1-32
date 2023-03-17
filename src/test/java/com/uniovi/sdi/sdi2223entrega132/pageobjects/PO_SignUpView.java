package com.uniovi.sdi.sdi2223entrega132.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_SignUpView extends PO_NavView {
    static public void fillForm(WebDriver driver, String emailP, String nameP, String lastNameP, String
            passwordP, String passwordConfP) {
        WebElement dni = driver.findElement(By.name("email"));
        dni.click();
        dni.clear();
        dni.sendKeys(emailP);
        WebElement name = driver.findElement(By.name("name"));
        name.click();
        name.clear();
        name.sendKeys(nameP);
        WebElement lastname = driver.findElement(By.name("lastName"));
        lastname.click();
        lastname.clear();
        lastname.sendKeys(lastNameP);
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordP);
        WebElement passwordConfirm = driver.findElement(By.name("passwordConfirm"));
        passwordConfirm.click();
        passwordConfirm.clear();
        passwordConfirm.sendKeys(passwordConfP);
        //Pulsar el boton de Alta.
        By boton = By.className("btn");
        driver.findElement(boton).click();
    }
}