package com.sun.jna.platform.win32.COM.util.office;

import java.io.File;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.office.MSExcel;
import com.sun.jna.platform.win32.COM.util.Factory;

public class MSOfficeDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MSOfficeDemo();
	}

	private String currentWorkingDir = new File("").getAbsolutePath() + File.separator;

	public MSOfficeDemo() {
		this.testMSWord();
		// this.testMSExcel();
	}

	public void testMSWord() {
		ComWord_Application msWordObject = null;
		ComIApplication msWord = null;
		try {
			msWordObject = Factory.INSTANCE.createObject(ComWord_Application.class);
			msWord = msWordObject.queryInterface(ComIApplication.class);

			System.out.println("MSWord version: " + msWord.getVersion());

			msWord.setVisible(true);
			// msWord.newDocument();
			msWord.getDocuments().Open(currentWorkingDir + "jnatest.doc");
			msWord.getSelection().TypeText("Hello from JNA! \n\n");
			// wait 10sec. before closing
			Thread.sleep(1000);
			// save in different formats
			// pdf format is only supported in MSWord 2007 and above
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestSaveAs.doc", WdSaveFormat.wdFormatDocument);
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestSaveAs.pdf", WdSaveFormat.wdFormatPDF);
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestSaveAs.rtf", WdSaveFormat.wdFormatRTF);
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestSaveAs.html", WdSaveFormat.wdFormatHTML);
			// close and save the document
			msWord.getActiveDocument().Close(false);
			msWord.getDocuments().Add();
			// msWord.openDocument(currentWorkingDir + "jnatest.doc", true);
			msWord.getSelection()
					.TypeText(
							"Hello from JNA! \n Please notice that JNA can control MS Word via the new COM interface! \nHere we are creating a new word document and we save it to the 'TEMP' directory!");
			// save with no user prompt
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestNewDoc1.docx", WdSaveFormat.wdFormatDocumentDefault);
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestNewDoc2.docx", WdSaveFormat.wdFormatDocumentDefault);
			msWord.getActiveDocument().SaveAs("C:\\TEMP\\jnatestNewDoc3.docx", WdSaveFormat.wdFormatDocumentDefault);
			// close and save the document
			msWord.getActiveDocument().Close(false);
			// open 3 documents
			msWord.getDocuments().Open("C:\\TEMP\\jnatestNewDoc1.docx");
			msWord.getSelection().TypeText("Hello some changes from JNA!\n");
			msWord.getDocuments().Open("C:\\TEMP\\jnatestNewDoc2.docx");
			msWord.getSelection().TypeText("Hello some changes from JNA!\n");
			msWord.getDocuments().Open("C:\\TEMP\\jnatestNewDoc3.docx");
			msWord.getSelection().TypeText("Hello some changes from JNA!\n");
			// save the document and prompt the user
			msWord.getDocuments().Save(false, WdOriginalFormat.wdPromptUser);
			// wait then close word
			msWord.Quit();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (COMException e) {
			if (e.getExcepInfo() != null) {
				System.out.println("bstrSource: " + e.getExcepInfo().bstrSource);
				System.out.println("bstrDescription: " + e.getExcepInfo().bstrDescription);
			}

			// print stack trace
			e.printStackTrace();
		} finally {
			if (msWord != null)
				msWord.Quit();
		}
	}

	public void testMSExcel() {
		MSExcel msExcel = null;

		try {
			msExcel = new MSExcel();
			System.out.println("MSExcel version: " + msExcel.getVersion());
			msExcel.setVisible(true);
			// msExcel.newExcelBook();
			msExcel.openExcelBook(currentWorkingDir + "jnatest.xls", true);
			msExcel.insertValue("A1", "Hello from JNA!");
			// wait 10sec. before closing
			Thread.currentThread().sleep(10000);
			// close and save the active sheet
			msExcel.closeActiveWorkbook(true);
			msExcel.setVisible(true);
			// msExcel.newExcelBook();
			msExcel.openExcelBook(currentWorkingDir + "jnatest.xls", true);
			msExcel.insertValue("A1", "Hello from JNA!");
			// close and save the active sheet
			msExcel.closeActiveWorkbook(true);
		} catch (Exception e) {
			e.printStackTrace();

			if (msExcel != null)
				msExcel.quit();
		}
	}
}
