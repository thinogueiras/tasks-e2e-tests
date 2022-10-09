package qa.thinogueiras.tasks.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TasksTest {
	
	private static WebDriver driver;
	private static WebElement element;
	private static ChromeOptions options;	
	
	@BeforeAll
	public static void setup() {
		WebDriverManager.chromedriver().setup();
		options = new ChromeOptions();		
		options.addArguments("--start-maximized");
		options.addArguments("--incognito");
		driver = new ChromeDriver(options);
	}
	
	@BeforeEach
	public void go() {
		driver.get("http://localhost:8001/tasks/");		
	}
	
	@AfterAll
	public static void tearDown() {
		driver.quit();
	}
	
	@Test
	public void deveValidarMenssagemDaTagP() {
		element = driver.findElement(By.cssSelector("p[class=\"lead\"]"));
		assertEquals("A very simple task management tool", element.getText());
	}
	
	@Test
	public void deveSalvarTaskComSucesso() {		
		driver.findElement(By.id("addTodo")).click();
		driver.findElement(By.cssSelector("input[id=\"task\"]")).sendKeys("Teste5");
		driver.findElement(By.cssSelector("input[id=\"dueDate\"]")).sendKeys("14/11/2033");
		driver.findElement(By.cssSelector("input[id=\"saveButton\"]")).click();		
	}
	
	@Test
	public void nãoDeveSalvarTaskComDataInválida() {		
		driver.findElement(By.id("addTodo")).click();
		driver.findElement(By.cssSelector("input[id=\"task\"]")).sendKeys("Teste via Selenium - Data Inválida");
		driver.findElement(By.cssSelector("input[id=\"dueDate\"]")).sendKeys("14/11/2010");
		driver.findElement(By.cssSelector("input[id=\"saveButton\"]")).click();
		element = driver.findElement(By.cssSelector("[class=\"alert alert-danger\"]"));		
		assertEquals("Due date must not be in past", element.getText());
	}
	
	@Test
	public void nãoDeveAdicionarTaskComDescriçãoNula() {		
		driver.findElement(By.id("addTodo")).click();
		driver.findElement(By.cssSelector("input[id=\"task\"]")).sendKeys("");
		driver.findElement(By.cssSelector("input[id=\"dueDate\"]")).sendKeys("14/11/2010");
		driver.findElement(By.cssSelector("input[id=\"saveButton\"]")).click();
		element = driver.findElement(By.cssSelector("[class=\"alert alert-danger\"]"));		
		assertEquals("Fill the task description", element.getText());
	}
	
	@Test
	public void nãoDeveAdicionarTaskComDataNula() {		
		driver.findElement(By.id("addTodo")).click();
		driver.findElement(By.cssSelector("input[id=\"task\"]")).sendKeys("Teste via Selenium - Data Nula");
		driver.findElement(By.cssSelector("input[id=\"dueDate\"]")).sendKeys("");
		driver.findElement(By.cssSelector("input[id=\"saveButton\"]")).click();
		element = driver.findElement(By.cssSelector("[class=\"alert alert-danger\"]"));		
		assertEquals("Fill the due date", element.getText());
	}
}