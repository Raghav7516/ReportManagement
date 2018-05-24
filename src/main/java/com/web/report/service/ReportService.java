package com.web.report.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.report.model.StudentDetails;
import com.web.report.model.UserDetails;
import com.web.report.report.Report;
import com.web.report.repository.StudentRepository;
import com.web.report.repository.UserRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void userReport(HttpServletResponse response) throws JRException, IOException {
		List<UserDetails> user=userRepository.findAll();
		Report userReport=new Report(user);
		JasperPrint jp = userReport.getReport("UserReport");
		userReport.writeXlsxReport(jp, response, "UserReport");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void vehicleReport(HttpServletResponse response) throws JRException, IOException {
		List<StudentDetails> student=studentRepository.findAll();
		Report studentReport=new Report(student);
		JasperPrint jp = studentReport.getReport("StudentReport");
		studentReport.writeXlsxReport(jp, response, "StudentReport");
	}
}
