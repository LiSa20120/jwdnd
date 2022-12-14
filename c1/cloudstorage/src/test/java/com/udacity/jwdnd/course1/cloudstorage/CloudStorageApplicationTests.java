package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	private static final String FIRST_NAME = "firstname";
	private static final String LAST_NAME = "lastname";
	private static final String NOTE_TITLE = "notetitle";
	private static final String NOTE_DESC = "notedesc";
	private static final String URL = "https://test.co";

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testLoginPage() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/login");
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUnauthorizedAccess() throws InterruptedException {
		driver.get(String.format("http://localhost:%d/home", this.port));
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testBadUrl() {
		doMockSignUp(FIRST_NAME, LAST_NAME, "testBadUrl", "testBadUrl");
		doMockLogIn("testBadUrl", "testBadUrl");
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}

	@Test
	public void testRedirection() {
		doMockSignUp(FIRST_NAME, LAST_NAME, "testRedirection", "testRedirection");
			Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void testSignupAndLogin() throws InterruptedException {
		doMockSignUp(FIRST_NAME, LAST_NAME, "testSignupAndLogin", "testSignupAndLogin");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
		doMockLogIn("testSignupAndLogin", "testSignupAndLogin");
		Assertions.assertEquals("Home", driver.getTitle());
		var element = driver.findElement(By.id("logoutBtn"));
		element.click();
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteFunctions() throws InterruptedException {
		doMockSignUp(FIRST_NAME, LAST_NAME, "testNoteFunctions", "testNoteFunctions");
		doMockLogIn("testNoteFunctions", "testNoteFunctions");
		goToNotesTab();

		var showCredentialsModel = driver.findElement(By.id("show-notes-modal"));
		showCredentialsModel.click();
		Thread.sleep(500);

		var noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.sendKeys(NOTE_TITLE);

		var noteDesc = driver.findElement(By.id("note-description"));
		noteDesc.sendKeys(NOTE_DESC);

		saveNote();
		goToHome();
		goToNotesTab();

		var saveNotes = driver.findElement(By.id("saved-note-title"));
		Assertions.assertEquals(NOTE_TITLE, saveNotes.getText());
		Thread.sleep(500);

		var editNotesButton = driver.findElement(By.id("edit-note-btn"));
		editNotesButton.click();
		Thread.sleep(500);

		noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.sendKeys("1");

		saveNote();
		goToHome();
		goToNotesTab();

		saveNotes = driver.findElement(By.id("saved-note-title"));
		Assertions.assertEquals(NOTE_TITLE + "1", saveNotes.getText());

		var deleteNoteButton = driver.findElement(By.id("delete-note-btn"));
		deleteNoteButton.click();
		Thread.sleep(1000);

		goToHome();
		goToNotesTab();

		var ifNotePresent = driver.findElements(By.id("saved-note-url"))
				.isEmpty();
		Assertions.assertTrue(ifNotePresent);
	}

	@Test
	public void testCredentialFunctions() throws InterruptedException {
		doMockSignUp(FIRST_NAME, LAST_NAME, "testCredFn", "testCredFn");
		doMockLogIn("testCredFn", "testCredFn");
		goToCredsTab();

		var showCredentialsModel = driver.findElement(By.id("show-credentials-modal"));
		showCredentialsModel.click();
		Thread.sleep(500);

		var credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys(URL);

		var credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("testCredFn");

		var credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("testCredFn");

		saveCredential();
		goToHome();
		goToCredsTab();

		var saveCredentials = driver.findElement(By.id("saved-credential-url"));
		Assertions.assertEquals(URL, saveCredentials.getText());
		Thread.sleep(500);

		var editCredentialsButton = driver.findElement(By.id("edit-credential-btn"));
		editCredentialsButton.click();
		Thread.sleep(500);

		credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.sendKeys(".in");

		saveCredential();
		goToHome();
		goToCredsTab();

		saveCredentials = driver.findElement(By.id("saved-credential-url"));
		Assertions.assertEquals(URL + ".in", saveCredentials.getText());

		var deleteCredentialButton = driver.findElement(By.id("delete-credential-btn"));
		deleteCredentialButton.click();
		Thread.sleep(1000);

		goToHome();
		goToCredsTab();

		var ifCredentialPresent = driver.findElements(By.id("saved-credential-url"))
				.isEmpty();
		Assertions.assertTrue(ifCredentialPresent);
	}

	@Test
	public void testLargeUpload() {
		doMockSignUp(FIRST_NAME, LAST_NAME, "testLargeUpload", "testLargeUpload");
		doMockLogIn("testLargeUpload", "testLargeUpload");

		var webDriverWait = new WebDriverWait(driver, 2);
		var fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		var fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		var uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveSuccess")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 ??? Forbidden"));

	}

	private void goToHome() throws InterruptedException {
		var navigateHomeButton = driver.findElement(By.className("navigate-home"));
		navigateHomeButton.click();
		Thread.sleep(500);
	}
	private void goToNotesTab() throws InterruptedException {
		var notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		Thread.sleep(500);
	}

	private void goToCredsTab() throws InterruptedException {
		var credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();
		Thread.sleep(500);
	}
	private void saveNote() throws InterruptedException {
		var saveNote = driver.findElement(By.id("save-note"));
		saveNote.click();
		Thread.sleep(1000);
	}

	private void saveCredential() throws InterruptedException {
		var credentialSubmit = driver.findElement(By.id("save-credentials"));
		credentialSubmit.click();
		Thread.sleep(1000);
	}

	private void doMockSignUp(String firstname, String lastname, String username, String password) {
		var webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		var inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstname);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		var inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastname);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		var inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		var inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();
	}

	private void doMockLogIn(String username, String password) {
		driver.get("http://localhost:" + this.port + "/login");
		var webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

}
