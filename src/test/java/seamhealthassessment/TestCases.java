package seamhealthassessment;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases extends ExtentReportManager {

	public static WebDriver driver;

	String actualUrl;
	String baseUrl = "https://patient-satisfaction.seamhealth.support";
	SoftAssert softAssert;
	WebDriverWait wait;

	@BeforeTest
	public void SetUp() {
		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();
		driver.get(baseUrl);
		softAssert = new SoftAssert();
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	}

	@Test
	public void validEmailValidPassword() {

		String email = "xerelax126@roborena.com";
		String password = "password";

		String expectedUrl = "https://patient-satisfaction.seamhealth.support/dashboard-setup";
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		enterValidEmail(email);
		enterValidPassword(password);
		clickLoginButton();
		resetInputFields();

		wait.until(ExpectedConditions.urlContains(expectedUrl));

		softAssert.assertTrue(driver.getCurrentUrl().contains(expectedUrl));

	}

	@Test
	public void invalidEmailValidPassword() {

		String email = "xerela@roborena.com";
		String password = "password";

		enterValidEmail(email);
		enterValidPassword(password);
		clickLoginButton();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='txt-body']")));
		softAssert.assertTrue(isErrorMessageDisplayed());

		resetInputFields();

	}

	@Test(priority = 2)
	public void logoutFunctionality() {

		String email = "xerelax126@roborena.com";
		String password = "password";

		String expectedUrl = "https://patient-satisfaction.seamhealth.support/dashboard-setup";
//		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		System.out.println(driver.getCurrentUrl());
		wait.until(ExpectedConditions.urlContains(baseUrl));
		enterValidEmail(email);
		enterValidPassword(password);
		clickLoginButton();
		// resetInputFields();

		wait.until(ExpectedConditions.urlContains(expectedUrl));
		logout();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='txt-body']")));
		softAssert.assertTrue(IsUserLoggedIn());
//		wait.until(ExpectedConditions.urlContains(baseUrl));
//		softAssert.assertTrue(driver.getCurrentUrl().contains(baseUrl));

		resetInputFields();

	}

	@AfterClass
	public void TearDown() {

		if (driver != null) {
			driver.manage().deleteAllCookies();
			driver.quit();
		}
		softAssert.assertAll();
		System.out.println("Teardown successful");

	}

	// login page methods
	public void enterValidEmail(String email) {
		driver.findElement(By.xpath("//input[@id='signInEmail']")).sendKeys(email);
	}

	public void enterValidPassword(String password) {
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
	}

	public void enterInvalidEmail(String email) {
		driver.findElement(By.xpath("//input[@id='signInEmail']")).sendKeys(email);
	}

	public void enterInvalidPassword(String password) {
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
	}

	public void clickLoginButton() {

		driver.findElement(By.xpath("//button[@class='login-text']")).click();

	}

	public void logout() {
		driver.findElement(By.cssSelector(".initial-name.ml-1.image-height")).click();
		driver.findElement(By.xpath("//a[@class='dropBtn2 rlout']")).click();

		WebElement yesButton = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Yes')]")));

//			driver.findElement(By.xpath("//button[contains(text(), 'Yes')]")).click();
		yesButton.click();

		// button[contains(text(), 'OK')]
		// input[@id='user name']

	}

	public void resetInputFields() {
		driver.findElement(By.xpath("//input[@id='signInEmail']")).clear();
		driver.findElement(By.xpath("//input[@id='password']")).clear();

	}

	public boolean isErrorMessageDisplayed() {

		if (driver.findElement(By.xpath("//div[@class='txt-body']")).isDisplayed() && driver
				.findElement(By.xpath("//div[@class='txt-body']")).getText().contains("Incorrect Credentials")) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isLogoutSuccessful() {


		if (driver.findElement(By.xpath("//div[@class='txt-body']")).isDisplayed() && driver
				.findElement(By.xpath("//div[@class='txt-body']")).getText().contains("Logged out successfully")) {
			return true;
		} else {
			return false;
		}

	}

	public boolean IsUserLoggedIn() {
		
		if (driver.findElement(By.xpath("//p[@class='workspace']")).isDisplayed() && driver
				.findElement(By.xpath("//p[@class='workspace']")).getText().contains("Test")) {
			return true;
		} else {
			return false;
		}
	}

}
