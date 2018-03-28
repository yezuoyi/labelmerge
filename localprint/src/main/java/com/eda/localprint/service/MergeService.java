package com.eda.localprint.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eda.localprint.util.Img2PDFUtil;
import com.omniselling.common.util.Helper;

@Component
public class MergeService {

	public List<String> merge(String businessnum, List<String> pathlist) {

		String newLabelPath = "/home/wms/uploadfile/label/" + File.separator;

		String outPdfFileName = "";
		List<String> pdfFileList = new ArrayList<String>();
		List<String> htmlFileList = new ArrayList<String>();
		List<String> fileNameList = new ArrayList<String>();

		for (String key : pathlist) {
			if (key != null && !key.endsWith(".html")) {
				pdfFileList.add(key);
			} else if (key != null) {
				htmlFileList.add(key);
			}
		}
		if (htmlFileList != null && !htmlFileList.isEmpty()) {
			outPdfFileName = businessnum + System.currentTimeMillis() + "print.html";
			String downloadId = null;
			try {
				downloadId = createHtmlFileIncludingAllHtmlsForPrintingF(newLabelPath, htmlFileList, outPdfFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileNameList.add(downloadId);
		}

		if (pdfFileList != null && !pdfFileList.isEmpty()) {
			outPdfFileName = businessnum + System.currentTimeMillis() + "print.pdf";
			try {
				Img2PDFUtil.createPDFFromImageF(newLabelPath, outPdfFileName, pdfFileList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileNameList.add(outPdfFileName);
		}

		return fileNameList;
	}

	public String createHtmlFileIncludingAllHtmlsForPrinting(String pathToShippingLabelFile, List<String> fileNames,
			String mergedFileName) throws IOException {
		String pageBreak = "<div style=\"display: block; page-break-before: always;\"/>";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fileNames.size(); i++) {
			String path = pathToShippingLabelFile + fileNames.get(i);
			sb.append(Helper.readFile(path, StandardCharsets.UTF_8));
			sb.append(pageBreak);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pathToShippingLabelFile + mergedFileName);
			fos.write(sb.toString().getBytes());
			fos.flush();
		} finally {
			fos.close();
		}

		return mergedFileName;
	}

	/**
	 * 多个label单组装成一个
	 */
	public String createHtmlFileIncludingAllHtmlsForPrintingF(String outPath, List<String> filePaths,
			String mergedFileName) throws IOException {
		String pageBreak = "<div style=\"display: block; page-break-before: always;\"/>";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filePaths.size(); i++) {
			String path = filePaths.get(i);
			sb.append(Helper.readFile(path, StandardCharsets.UTF_8));
			sb.append(pageBreak);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath + mergedFileName);
			fos.write(sb.toString().getBytes());
			fos.flush();
		} finally {
			fos.close();
		}

		return mergedFileName;
	}
}
