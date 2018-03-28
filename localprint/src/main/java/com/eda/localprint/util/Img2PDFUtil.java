package com.eda.localprint.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Font;
import com.omniselling.common.util.Helper;

public class Img2PDFUtil
{
    private static final Logger logger = LoggerFactory.getLogger(Img2PDFUtil.class);

    public static void createPDFFromImageBad(String Path, String outPdfFileName, List<String> fileList)
            throws Exception
    {
        PDDocument doc = null;
        try
        {
            doc = new PDDocument();
            BufferedImage image;
            PDPage page;
            PDXObjectImage ximage;
            PDPageContentStream contentStream;
            PDDocument docTmp;
            float imgWidth;
            float imgHeight;

            PDFMergerUtility merger = new PDFMergerUtility();
            for (String file : fileList)
            {
                file = Path + file;
                if (file.toLowerCase().endsWith(".pdf"))
                {
                    docTmp = PDDocument.load(file);
                    if (docTmp.getNumberOfPages() == 0)
                    {
                        docTmp.close();
                        continue;
                    }
                    merger.appendDocument(doc, docTmp);
                    docTmp.close();
                    continue;
                }

                image = ImageIO.read(new File(file));
                //TMD...
                if (file.indexOf("USPS") != -1)
                {
                    SimpleDateFormat DateFormatMMddyyyy = new SimpleDateFormat("MM/dd/yyyy");
                    String date = DateFormatMMddyyyy.format(new Date());
                    image = getImg(image, date, 650, 1036);
                }
                imgWidth = image.getWidth();
                imgHeight = image.getHeight();
                ximage = new PDJpeg(doc, image, 1); //ximage = new PDPixelMap(doc, tmp_image);
                page = new PDPage(new PDRectangle(imgWidth, imgHeight));
                doc.addPage(page);
                contentStream = new PDPageContentStream(doc, page, true, false);
                contentStream.drawImage(ximage, 0, 0);
                //contentStream.drawXObject(ximage, 0, 0, image.getWidth(), image.getHeight());

                contentStream.close();

            }
            doc.save(Path + outPdfFileName);
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
        }
    }

    public static synchronized void createPDFFromImageF(String outPath, String outPdfFileName, List<String> filePath)
            throws Exception
    {
        PDDocument doc = null;
        try
        {
            doc = new PDDocument();
            BufferedImage image = null;
            PDPage page;
            PDXObjectImage ximage;
            PDPageContentStream contentStream;
            PDDocument docTmp;
            float imgWidth;
            float imgHeight;

            PDFMergerUtility merger = new PDFMergerUtility();
            for (String file : filePath)
            {
                try
                {
                if (file.toLowerCase().endsWith(".pdf"))
                {
                    docTmp = PDDocument.load(file);
                    if (docTmp.getNumberOfPages() == 0)
                    {
                        docTmp.close();
                        continue;
                    }
                    merger.appendDocument(doc, docTmp);
                    docTmp.close();
                    continue;
                }


                    image = ImageIO.read(new File(file));
                }
                catch (Exception e)
                {
                    Thread.sleep(2000);
                    logger.info(" ImageIO Read Exception:" + e.toString() + ",file path:" + file);
                    image = ImageIO.read(new File(file));
                }
                //TMD...
                if (file.indexOf("USPS") != -1)
                {
                    SimpleDateFormat DateFormatMMddyyyy = new SimpleDateFormat("MM/dd/yyyy");
                    String date = DateFormatMMddyyyy.format(new Date());
                    image = getImg(image, date, 650, 1036);
                }
                imgWidth = image.getWidth();
                imgHeight = image.getHeight();
                ximage = new PDJpeg(doc, image, 1); //ximage = new PDPixelMap(doc, tmp_image);
                page = new PDPage(new PDRectangle(imgWidth, imgHeight));
                doc.addPage(page);
                contentStream = new PDPageContentStream(doc, page, true, false);
                contentStream.drawImage(ximage, 0, 0);
                //contentStream.drawXObject(ximage, 0, 0, image.getWidth(), image.getHeight());

                contentStream.close();

            }
            doc.save(outPath + outPdfFileName);
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
        }
    }
    //usps的图片改日期方法
    public static BufferedImage getImg(BufferedImage imgBuf, String date, int x, int y)
    {
        int imageWidth = 110;
        int imageHeight = 35;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setColor(Color.black);
        java.awt.Font font = new java.awt.Font("arial", Font.NORMAL, 20);
        graphics.setFont(font);
        graphics.drawString(date, 0, 20);
        return Helper.ImgMerge(imgBuf, image, x, y);
    }

}
