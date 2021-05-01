package com.main;

import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.JsonParser;
import com.operations.PreparedStatementOperation;
import com.util.DBRow;
import com.util.Months;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
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

			//Get and validate user input query parameters
			int inputYear = 0;
			String inputSign = "";
			String inputMonth = "";
			int inputDay = 0;

			//year
			if (queryParams.get("year") == null) {
				msg = "Year cannot be empty!";
			} else {
				if(!StringUtils.isStrictlyNumeric(String.valueOf(queryParams.get("year")))) {
					msg = "Invalid input year: not an integer number";
				} else {
					inputYear = Integer.parseInt(queryParams.get("year"));
					msg = horoscope.generateYearlyCalendar(inputYear);
				}

				//sign
				if(queryParams.get("sign") != null) {
					String[] signs = horoscope.getSIGNS();
					for (String s : signs) {
						if (s.toLowerCase().equals(queryParams.get("sign").toLowerCase())) {
							inputSign = queryParams.get("sign");
							break;
						}
					}

					if (inputSign.equals(""))
						msg += "; invalid input sign (valid options: " + Arrays.toString(signs) + ")";
				}//input sign not null

				//month
				if(queryParams.get("month") != null) {
					for (Months m : Months.values()) {
						if (m.getMonthName().toLowerCase().equals(queryParams.get("month").toLowerCase())) {
							inputMonth = queryParams.get("month");
							break;
						}
					}

					if (inputMonth.equals(""))
						msg += "; invalid input month";
				}//input month not null

				//day
				if(queryParams.get("day") != null) {
					if(!StringUtils.isStrictlyNumeric(String.valueOf(queryParams.get("day")))) {
						msg += "; invalid input day: not an integer number";
					} else {
						if (!inputMonth.equals("")) {
							for (Months m : Months.values()) {
								if (inputMonth.toLowerCase().equals(m.getMonthName().toLowerCase()) && Integer.parseInt(queryParams.get("day")) >= 1 && Integer.parseInt(queryParams.get("day")) <= m.getMonthDays(inputYear))
									inputDay = Integer.parseInt(queryParams.get("day"));
							}
						}
					}

					if (inputDay == 0) {
						msg += "; invalid input day for " + inputMonth + " " + inputYear;
					}
				}//input day not null

			}//input year not null

			JsonObject jo = new JsonObject();
			jo.put("remote address", address);
			jo.put("input year", queryParams.get("year"));
			jo.put("input sign", queryParams.get("sign"));
			jo.put("input month", queryParams.get("month"));
			jo.put("input day", queryParams.get("day"));
			jo.put("response info", msg);

			PreparedStatementOperation pso = new PreparedStatementOperation();

			//valid input year
			if (inputYear != 0) {
				jo.put("number of database entries in " + inputYear, String.valueOf(pso.selectRows("", "").size()));

				//Best sign(s) for given year
				ArrayList<String> bestSigns = horoscope.getBestSign(inputYear);
				JsonObject bestSignsJo = new JsonObject();
				for (String str : bestSigns) {
					String[] strArr = str.split(",");
					bestSignsJo.put(strArr[0], "average score " + strArr[1]);
				}
				jo.put("best sign(s) in " + inputYear, bestSignsJo);

				//valid input sign
				if (!inputSign.equals("")) {
					//Month score list
					for (Months month : Months.values()) {
						JsonObject monthJo = new JsonObject();

						for (int i = 1; i <= month.getMonthDays(inputYear); i++) {
							DBRow row = pso.selectRow(inputYear, inputSign, month.getMonthName(), i);
							monthJo.put(String.valueOf(i), "day score " + row.getScore());
						}

						jo.put(month.getMonthName() + " (" + inputSign + ")" , monthJo);
					}

					//Best mont(s) for given sign
					ArrayList<String> bestMonths = horoscope.getBestMonth(inputYear, inputSign);
					JsonObject bestMonthsJo = new JsonObject();
					for (String str : bestMonths) {
						String[] strArr = str.split(",");
						bestMonthsJo.put(strArr[0], "average score " + strArr[1]);
					}
					jo.put("best month(s) for " + inputSign + " in " + inputYear, bestMonthsJo);

					//valid input month and day
					if (!inputMonth.equals("") && inputDay != 0) {
						//Sentence for given day
						jo.put("horoscope advice for " + inputSign + " on " + inputMonth + " " + inputDay + Months.getDaySuffix(String.valueOf(inputDay)) + ", " + inputYear,
								horoscope.getDailySentence(inputYear, inputSign, inputMonth, inputDay));
					}//valid input month and day

				}//valid input sign

			}//valid input year

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
