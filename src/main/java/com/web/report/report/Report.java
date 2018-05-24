package com.web.report.report;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Stretching;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class Report<T> {
	
	private final List<T> list = new ArrayList<T>();

	public Report(T data) {
		list.add(data);
	}
	public Report(List<T> data) {
		list.addAll(data);
	}
	public JasperPrint getReport(String type) throws JRException {
		DynamicReport dynamicReport=null;
		switch (type) {
		case "UserReport":
			dynamicReport = getDynamicUserReport(type);
			break;
		case "StudentReport":
			dynamicReport = getDynamicStudentReport(type);
			break;
		}
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(),
				new JRBeanCollectionDataSource(list));
		return jp;
	}

	public AbstractColumn createColumn(String property, Class<?> type, String title,Style style) throws ColumnBuilderException {
		AbstractColumn columnState = ColumnBuilder.getNew().setColumnProperty(property, type.getName()).setTitle(title).setHeaderStyle(style).setStyle(style).build();
		return columnState;
	}

	private DynamicReport getDynamicUserReport(String userType) {
		DynamicReportBuilder report = new DynamicReportBuilder();
		Style style=createHeaderStyle();
		AbstractColumn userId = createColumn("userId", Integer.class, "User Id",style);
		AbstractColumn userName = createColumn("userName", String.class, "User Name",style);
		AbstractColumn email = createColumn("email", String.class, "Email",style);
		AbstractColumn contact = createColumn("contact", String.class, "Contact Number",style);
		AbstractColumn userStatus = createColumn("userStatus", String.class, "User Status",style);
		
		report.setReportName(userType).addColumn(userId).addColumn(userName)
				.addColumn(email).addColumn(contact).addColumn(userStatus);
		report.setUseFullPageWidth(true);
		report.setIgnorePagination(true);
		return report.build();
	}
	private DynamicReport getDynamicStudentReport(String vehicleType) {
		DynamicReportBuilder report = new DynamicReportBuilder();
		Style style=createHeaderStyle();
		AbstractColumn studentId = createColumn("studentId", Integer.class, "Student Id",style);
		AbstractColumn studentName = createColumn("studentName", String.class, "Student Name",style);
		AbstractColumn studentAddress = createColumn("studentAddress", String.class, "Student Address",style);
		AbstractColumn contact = createColumn("contact", Long.class, "Contact",style);
		report.setReportName(vehicleType).addColumn(studentId).addColumn(studentName)
				.addColumn(studentAddress).addColumn(contact);
		report.setUseFullPageWidth(true);
		report.setIgnorePagination(true);
		return report.build();
	}
	public void writeXlsxReport(JasperPrint jp, HttpServletResponse response, final String reportName)
			throws IOException, JRException {
	 //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition",
				"attachment; filename=" + (reportName == null ? jp.getName() : reportName).replace('"', '_') + ".xls");
		response.setContentType("application/vnd.ms-excel");
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(jp));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setWhitePageBackground(true);
		configuration.setDetectCellType(true);
		configuration.setFontSizeFixEnabled(true);
		// set all your configuration like above
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		final byte[] rawBytes = xlsReport.toByteArray();
		response.setContentLength(rawBytes.length);

		final ByteArrayInputStream bais = new ByteArrayInputStream(rawBytes);

		final OutputStream outStream = response.getOutputStream();
		IOUtils.copy(bais, outStream);

		outStream.flush();

		IOUtils.closeQuietly(xlsReport);
		IOUtils.closeQuietly(bais);
		IOUtils.closeQuietly(outStream);
	}
	public static Style createHeaderStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.ARIAL_BIG_BOLD);
		sb.setBorder(Border.THIN());
		sb.setBorderBottom(Border.THIN());
		sb.setBorderColor(Color.BLACK);
		sb.setBackgroundColor(Color.LIGHT_GRAY);
		sb.setTextColor(Color.BLACK);
		sb.setHorizontalAlign(HorizontalAlign.CENTER);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setTransparency(Transparency.OPAQUE);
		sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
		sb.setPadding(5);
		return sb.build();
	}

}
