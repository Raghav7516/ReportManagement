package com.web.report.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.report.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/download")
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	@GetMapping("/userReport")
	public void userReport(HttpServletResponse response) throws JRException, IOException {
		reportService.userReport(response);
	}
	
	@GetMapping("/studentReport")
	public void vehicleReport(HttpServletResponse response) throws JRException, IOException {
		reportService.vehicleReport(response);
	}
}
