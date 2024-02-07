package mangeto;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class MangentTest {
	WebDriver driver = new ChromeDriver();
	Random rand = new Random();
	String NameOfTheItem = "Gym";
	String Website = "https://magento.softwaretestingboard.com/";
	ExtentReports extent;
	ExtentTest test;

	@BeforeSuite
	public void setUp() {
		extent = new ExtentReports();
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
		extent.attachReporter(sparkReporter);
	}

	@BeforeTest
	public void SetUp() {
		driver.get(Website);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	@Test()
	public void ChoosingTheItemOfFitness() throws InterruptedException {
		test = extent.createTest("ChoosingTheItemOfFitness");
		WebElement WomenSection = driver.findElement(By.id("ui-id-4"));
		WomenSection.click();
		WebElement Tees = driver
				.findElement(By.xpath("//*[@id=\"maincontent\"]/div[4]/div[1]/div[2]/div[1]/div[1]/a[1]/span"));
		Tees.click();
		List<WebElement> HeadLineName = driver.findElements(By.className("product-item-link"));
		try {
			for (int i = 0; i < HeadLineName.size(); i++) {
				WebElement eachItemHeadline = HeadLineName.get(i);
				String nameOfTheItem = eachItemHeadline.getText();
				if (nameOfTheItem.contains(NameOfTheItem)) {
					HeadLineName.get(i).click();
					selectRandomSize();
					selectRandomColor();
					addToCart();
					Thread.sleep(2000);
					driver.navigate().back();
				}
				HeadLineName = driver.findElements(By.className("product-item-link"));
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("We Can't Add This Item ");
		}
	}

	private void selectRandomSize() {
		List<WebElement> sizeElements = driver
				.findElements(By.xpath("//*[@id=\"product-options-wrapper\"]/div/div/div[1]/div/div"));
		int randomSize = rand.nextInt(sizeElements.size());
		WebElement size = sizeElements.get(randomSize);
		size.click();
	}

	private void selectRandomColor() {
		List<WebElement> colorElements = driver
				.findElements(By.xpath("//*[@id=\"product-options-wrapper\"]/div/div/div[2]/div/div"));
		int randomIndex = rand.nextInt(colorElements.size());
		WebElement color = colorElements.get(randomIndex);
		color.click();
	}

	private void addToCart() {
		driver.findElement(By.id("product-addtocart-button")).click();
	}

	@AfterTest
	public void tearDown() {
		extent.flush();
		driver.quit();
	}
}
