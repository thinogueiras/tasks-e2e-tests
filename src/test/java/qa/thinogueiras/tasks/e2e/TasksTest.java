package qa.thinogueiras.tasks.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TasksTest {
	
	private static WebDriver driver;
	private static ChromeOptions options;	
	private WebElement element;
	
	public String getTextRecordFromTable(String record) {	    
	    List<WebElement> lines = driver.findElements(By.xpath("//table//tr//td"));	    
	    String text = "0";
	    for(int i = 0; i < lines.size(); i++) {	        
	        if(lines.get(i).getText().equals(record)) {	            
	            text = record;
	            break;
	        }
	    }
	    return text;	  
	}
	
	public WebDriver setup() {
		WebDriverManager.chromedriver().setup();
		options = new ChromeOptions();		
		options.addArguments("--start-maximized");
		options.addArguments("--incognito");
		driver = new ChromeDriver(options);
		driver.get("http://localhost:8001/tasks/");
		return driver;
	}
	
	@BeforeEach
	public void go() {		
		driver = setup();		
	}	
	
	@AfterEach
	public void tearDown() {
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
		String sendText = "Teste via Selenium " + new Random().nextInt();
		driver.findElement(By.cssSelector("input[id=\"task\"]")).sendKeys(sendText);
		driver.findElement(By.cssSelector("input[id=\"dueDate\"]")).sendKeys("14/11/2033");
		driver.findElement(By.cssSelector("input[id=\"saveButton\"]")).click();
		element = driver.findElement(By.cssSelector("p[class=\"alert alert-success\"]"));		
		assertEquals("Success!", element.getText());		
		assertEquals(sendText, getTextRecordFromTable(sendText));
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
