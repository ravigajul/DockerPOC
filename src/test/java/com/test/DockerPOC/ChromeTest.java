package com.test.DockerPOC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ChromeTest {
	URL remoteAddress;
	DesiredCapabilities capabilities;
	String fileName;

	@BeforeTest
	public void RunDockerBatchFiles() throws IOException, InterruptedException {
		fileName = System.getProperty("user.dir") + "\\docker-compose.log";
		File fi = new File(fileName);
		if (!fi.exists()) {
			fi.createNewFile();
		}
		FileReader file = new FileReader(fileName);
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("cmd /c start " + System.getProperty("user.dir") + "\\DockerUp.bat");
		Thread.sleep(3000);
		String fileName = System.getProperty("user.dir") + "\\docker-compose.log";
		boolean flag = false;
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.SECOND, 30);
		long stopTime = calender.getTimeInMillis();
		BufferedReader reader = null;
		while (System.currentTimeMillis() <= stopTime) {
			if (FileHelper.IsRegistrationComplete(file, "The node is registered to the hub and ready to use")) {
				System.out.println("Node registration is complete");
				flag = true;
				break;
			}
		}
		// reader.close();
		file.close();

		if (flag) {
			runtime.exec("cmd /c start " + System.getProperty("user.dir") + "\\ScaleChrome.bat");
			Thread.sleep(15000);
		}
	}

	@AfterTest
	public void StopDocker() throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("cmd /c start " + System.getProperty("user.dir") + "\\DockerDown.bat");
		Thread.sleep(3000);
		String fileName = System.getProperty("user.dir") + "\\docker-compose.log";
		FileReader file = new FileReader(fileName);
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.SECOND, 30);
		long stopTime = calender.getTimeInMillis();
		while (System.currentTimeMillis() <= stopTime) {
			if (FileHelper.IsRegistrationComplete(file, "selenium-hub exited with code")) {
				System.out.println("DockerShutdown");
				File fi = new File(fileName);
				if (fi.isFile()) {
					fi.deleteOnExit();
					System.out.println("File Deleted Successfully");
				}
				break;
			}
		}

		// reader.close();
		file.close();
	}

	@Test
	public void ChromeTest1() throws MalformedURLException {
		URL remoteAddress = new URL("http://localhost:4444/wd/hub");
		ChromeOptions capabilities = new ChromeOptions();

		RemoteWebDriver driver = new RemoteWebDriver(remoteAddress, capabilities);
		driver.get("http://google.com");
		System.out.println(driver.getTitle());

	}

	@Test
	public void ChromeTest2() throws MalformedURLException {
		URL remoteAddress = new URL("http://localhost:4444/wd/hub");
		ChromeOptions capabilities = new ChromeOptions();

		RemoteWebDriver driver = new RemoteWebDriver(remoteAddress, capabilities);
		driver.get("http://gmail.com");
		System.out.println(driver.getTitle());

	}

	@Test
	public void ChromeTest3() throws MalformedURLException {
		URL remoteAddress = new URL("http://localhost:4444/wd/hub");
		ChromeOptions capabilities = new ChromeOptions();
		RemoteWebDriver driver = new RemoteWebDriver(remoteAddress, capabilities);
		driver.get("http://yahoo.com");
		System.out.println(driver.getTitle());
	}
	/*
	 * @Test public void FireFoxTest1() throws MalformedURLException { URL
	 * remoteAddress = new URL("http://localhost:4444/wd/hub"); FirefoxOptions
	 * capabilities = new FirefoxOptions(); RemoteWebDriver driver = new
	 * RemoteWebDriver(remoteAddress, capabilities);
	 * driver.get("http://google.com"); System.out.println(driver.getTitle()); }
	 * 
	 */

}