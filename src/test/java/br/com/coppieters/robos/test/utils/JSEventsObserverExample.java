package br.com.coppieters.robos.test.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JSEventsObserverExample {
	WebClient webClient;
	private HtmlPage paginaDeTeste;

	@Before
	public void init() throws Exception {
		webClient = new WebClient();
		File file = new File("src/test/resources/teste.html");
		paginaDeTeste = (HtmlPage) webClient.getPage("file://"+file.getAbsolutePath());
	}

	@Test
	public void pegaAlertDefault() throws Exception {
		final List<String> collectedAlerts = new ArrayList<String>();
		
		webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

		DomElement btn = paginaDeTeste.getElementById("btnAlerta");
		btn.click();

		Assert.assertTrue(collectedAlerts.contains("foo"));
	}

	@Test
	public void alertHandlerCustomizado() throws Exception {
		webClient.setAlertHandler(new AlertHandler() {
			@Override
			public void handleAlert(Page page, String message) {
				Assert.assertTrue("foo".equals(message));
			}
		});
		DomElement btn = paginaDeTeste.getElementById("btnAlerta");
		btn.click();
	}

	@Test
	public void repassaAoJavaConfirmacao() throws Exception {
		final List<String> collectedAlerts = new ArrayList<String>();
		webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

		ConfirmHandler okHandler = new ConfirmHandler() {
			@Override
			public boolean handleConfirm(Page page, String message) {
				boolean value = JOptionPane.showConfirmDialog(null, message) == JOptionPane.YES_OPTION;
				Assert.assertTrue(value);
				
				return value;
			}
		};
		webClient.setConfirmHandler(okHandler);

		DomElement btn = paginaDeTeste.getElementById("btnConfirma");
		btn.click();
	}

	@Test
	public void alertHandlerCustomizadoClicandoEmOk() throws Exception {
		webClient.setAlertHandler(new AlertHandler() {
			@Override
			public void handleAlert(Page page, String message) {
				System.out.println("JSEventsObserverExample.alertHandlerCustomizadoClicandoEmOk().new AlertHandler() {...}.handleAlert()");
				System.out.println(message);
			}
		});
		paginaDeTeste.getElementById("btnAlerta").click();
		// impossible for hour :<
	}
	
	@After
	public void end() {
		this.webClient.close();
	}
}
