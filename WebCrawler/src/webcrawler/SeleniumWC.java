package webcrawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumWC {
	public static void main(String[] args) throws InterruptedException {
		int results = 0;
		boolean change = false;
		boolean none = true;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter keywords (type done when finished or clear to clear keywords): ");
		ArrayList<String> ar = new ArrayList<String>();
		String next = sc.next();
		while (!next.equals("done")) {
			if (next.equals("clear")) {
				System.out.print("Keywords have been cleared. \nEnter keywords: ");
				ar.clear();
			}
			else {
				ar.add(next);
			}
			next = sc.next();
		}
		if (ar.size() > 0) {
			WebDriver driver = startChrome();
			for (int i = 1; i <= 10; i++) {
				none = true;
				navigate(driver, i);
				String mainPageURL = driver.getCurrentUrl();
				System.out.println("\nPAGE " + i + " | " + driver
						.findElement(
								By.xpath("//*[@id=\"forum_Trading_9036925_18446744073709551615_area\"]/div[2]/div[2]"))
						.getText() + " which contains specified keywords.");
				// System.out.println("\"" + ar.get(j) + "\"");
				for (int x = 0; x < 15; x++) {
					// SKIP STICKY POSTS
					if (i == 1 && x == 0) {
						x = 2;
					}
					change = false;
					WebElement table = driver.findElement(By.className("forum_topics"));
					List<WebElement> rows = table.findElements(By.className("forum_topic_name"));
					List<WebElement> clickableRow = table.findElements(By.className("forum_topic_overlay"));
					for (int j = 0; j < ar.size(); j++) {
						if (rows.get(x).getText().toLowerCase().contains(ar.get(j).toLowerCase())) {
							results++;
							System.out.println("\nSteam User | "
									+ driver.findElements(By.className("forum_topic_op")).get(x).getText());
							System.out.println("Link to Post | " 
									+ clickableRow.get(x).getAttribute("href"));
							System.out.println("Last Post on Thread | " + driver
									.findElements(By.className("forum_topic_lastpost")).get(x).getText());
							System.out.println("Page " + i + " | Row " + (x + 1) + " | " + rows.get(x).getText());
							change = true;
							none = false;
						}
					}
					if (!change) {
						clickableRow.get(x).click();
						String content[] = driver.findElements(By.className("content")).get(4).getText()
								.split("\\r?\\n");
						for (int j = 0; j < ar.size(); j++) {
							for (int y = 0; y < content.length; y++) {
								if (content[y].toLowerCase().contains(ar.get(j).toLowerCase())) {
									results++;
									if (!change) {
										System.out.println("\nSteam User | " + driver
												.findElement(By.className("forum_op_author")).getText());
										System.out.println("Link to User | " 
												+ driver.findElement(By.className("forum_op_author")).getAttribute("href"));
										System.out.println("Link to Post | " 
												+ driver.getCurrentUrl());
										System.out.println("Thread Posted | "
												+ driver.findElement(By.className("date")).getAttribute("title"));
									}
									System.out.println(
											"Page " + i + " | Row " + (x + 1) + " | Page Line " + (y + 1) + " | " + content[y]);
									none = false;
									change = true;
								}
							}
						}
						driver.get(mainPageURL);
					}
				}
				if (none) {
					System.out.println("<none>");
				}
			}
			driver.close();
		}
		System.out.println("\nFound " + results + " results. Ending program...");
		sc.close();
	}

	public static boolean navigate(WebDriver driver, int index) {
		try {
			String url = "https://steamcommunity.com/app/252950/tradingforum/";
			if (index > 0) {
				url += "?fp=" + index;
			}
			driver.get(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static WebDriver startChrome() {
		String path = new File("").getAbsolutePath() + "\\third-party\\drivers\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", path);
		WebDriver chrome = new ChromeDriver();

		// chrome.manage().window().maximize();

		return chrome;
	}
}