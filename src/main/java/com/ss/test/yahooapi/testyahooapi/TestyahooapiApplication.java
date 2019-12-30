package com.ss.test.yahooapi.testyahooapi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@SpringBootApplication
public class TestyahooapiApplication implements CommandLineRunner {

	static Logger LOG = Logger.getLogger(TestyahooapiApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(TestyahooapiApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}
	@Override
    public void run(String... args)  {
		LOG.info("EXECUTING : command line runner");
		try {
			readStock("INTC");
			readStock("TRPL4.SA");
			readStock("O");
			getMulti();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readStock(String symbol){
		LOG.info("Lendo Dados do Stock :"+symbol);
  
		try {
			
			Stock stock = YahooFinance.get(symbol);
 
			BigDecimal price = stock.getQuote().getPrice();
			BigDecimal change = stock.getQuote().getChangeInPercent();
			BigDecimal peg = stock.getStats().getPeg();
			BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
			
			System.out.println("Symbol :"+stock.getQuote().getSymbol());
			System.out.println("Name  :"+stock.getName());
			System.out.println("Price :"+price);
			System.out.println("change  :"+change +" %");
			System.out.println("peg  :"+peg);
			System.out.println("Anual dividend  :"+dividend +" %");

			stock.print();

		}catch(IOException  e){
			e.printStackTrace();
		} catch(Exception e2 ){
			e2.printStackTrace();

		}
	}

	//MULTIPLE STOCKS AT ONCE
	public static void getMulti() throws IOException  {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		Map<String, Stock> stocks = YahooFinance.get(symbols); // single request
		Stock intel = stocks.get("INTC");
		Stock airbus = stocks.get("AIR.PA");
		intel.print();
		airbus.print();
	}

	public static void getHistorical() throws IOException  {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -5); // from 5 years ago
		
		Stock google = YahooFinance.get("GOOG", from, to, Interval.WEEKLY);
		google.print();
	}

	//MULTIPLE STOCKS - INCLUDE HISTORICAL QUOTES
	public static void getHistoricalMulti() throws IOException  {
		String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
		// Can also be done with explicit from, to and Interval parameters
		Map<String, Stock> stocks = YahooFinance.get(symbols, true);
		Stock intel = stocks.get("INTC");
		Stock airbus = stocks.get("AIR.PA");
		intel.print();
		airbus.print();
	}


	// you could explicitly define the from, to and Interval parameters to force a new 
	//request. Check the javadoc for more variations on the getHistory method.
	public static void getHistoricalAlternativeHistoric() throws IOException  {

		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -1); // from 1 year ago
		
		Stock google = YahooFinance.get("GOOG");
		List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, Interval.DAILY);
		// googleHistQuotes is the same as google.getHistory() at this point
		// provide some parameters to the getHistory method to send a new request to Yahoo Finance
		googleHistQuotes.get(0).getSymbol();
		googleHistQuotes.get(0).getDate();
		googleHistQuotes.get(0).getClose();
	}

}
