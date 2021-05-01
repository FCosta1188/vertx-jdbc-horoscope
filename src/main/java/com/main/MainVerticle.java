package com.main;

import com.mysql.cj.xdevapi.JsonParser;
import com.operations.PreparedStatementOperation;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.JsonFactory;
import io.vertx.core.spi.json.JsonCodec;
import io.vertx.ext.web.Router;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainVerticle extends AbstractVerticle {

	private HoroscopeCalendar horoscope = new HoroscopeCalendar();
	private String msg = "";

	//TEST URL: http://localhost:8888/?year=2026&sign=Pisces&month=March&day=11
	@Override
	public void start(Promise<Void> startPromise) throws Exception {

		Router router = Router.router(vertx);

		router.route().handler(context -> {
			String address = context.request().connection().remoteAddress().toString();
			MultiMap queryParams = context.queryParams();

			int inputYear = 0;
			String inputSign = "";
			String inputMonth = "";
			int inputDay = 0;

			if (queryParams.get("year") == null) {
				msg = "Please insert year!";
			} else {
				inputYear = Integer.parseInt(queryParams.get("year"));

				if(queryParams.get("sign") != null)
					inputSign = queryParams.get("sign");
				if(queryParams.get("month") != null)
					inputMonth = queryParams.get("month");
				if(queryParams.get("day") != null)
					inputDay = Integer.parseInt(queryParams.get("day"));

				msg = horoscope.generateYearlyCalendar(inputYear);
			}

			PreparedStatementOperation pso = new PreparedStatementOperation();

			JsonObject jo = new JsonObject();
			//JsonObject joTest = new JsonObject();
			//joTest.put("day1", "score1");
			//joTest.put("day2", "score2");



			jo.put("remote address", address);
			jo.put("input year", queryParams.get("year"));
			jo.put("input sign", queryParams.get("sign"));
			jo.put("input month", queryParams.get("month"));
			jo.put("input day", queryParams.get("day"));
			jo.put("response info", msg);
			//jo.put("nested json object", joTest);

			if (inputYear != 0) {
				jo.put("number of records", String.valueOf(pso.selectRows("", "").size()));

				if (!inputSign.equals("")) {
					ArrayList<String> bestMonths = horoscope.getBestMonth(inputYear, inputSign);
					JsonObject bestMonthsJo = new JsonObject();
					for (String str : bestMonths) {
						String[] strArr = str.split(",");
						bestMonthsJo.put(strArr[0], strArr[1]);
					}
					jo.put("best month(s)", bestMonthsJo);

					if (!inputMonth.equals("") && inputDay != 0) {
						jo.put("horoscope advice", horoscope.getDailySentence(inputYear, inputSign, inputMonth, inputDay));
					} else {
						jo.put("horoscope advice", null);
					}
				}

			}//inputYear != 0

			context.json(jo); //generate json response

			//Save Json to file
			/*
			try {
				File file = new File("C:\\Users\\franc\\OneDrive\\Desktop\\test.json");
				if (file.createNewFile()) {
					System.out.println("File created: " + file.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			try {
				FileWriter fileWriter = new FileWriter("C:\\Users\\franc\\OneDrive\\Desktop\\test.json");
				fileWriter.write(jo.encodePrettily());
				fileWriter.close();
				System.out.println("Successfully wrote to the file.");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}*/

		});

		vertx.createHttpServer()
			.requestHandler(router)
			.listen(8888)
			.onSuccess(server -> System.out.println("\n***LISTENING***\nVertx test: HTTP server started on port " + server.actualPort()));

	}//start
}//Main Verticle
