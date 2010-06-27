package Project.utils;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Number;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import Project.config.Config;
import Project.struct.DataStruct;
import Project.utils.FFT.Complex;

/**
 * @author bbxp
 *
 */
public class ExcelUtil {
    public static void writeExcel(String ip, String dstip, int port, DataStruct[] data, boolean isScan, Complex[] fft) {
        File dir = new File("./test/"+ip);
        dir.mkdir();
        File scan = new File(dir.getPath()+"/scan");
        scan.mkdir();
        String filename = (isScan?scan.getPath():dir.getPath())+"/"+dstip+"_"+port+(isScan?"scan":"")+".xls";
        String imagefile = ((isScan?scan.getPath():dir.getPath())+"/"+(isScan?"scan":"")+dstip+"_"+port);
        JFreeChartUtil.createFFTImage(ip, dstip, port, isScan, imagefile, data, fft);
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            Number number = null;
            for (int i = 0 ; i < data.length ; i ++) {
                number = new Number(0,i+1,data[i].dataSize);
                sheet.addCell(number);
                number = new Number(1,i+1,data[i].same);
                sheet.addCell(number);
            }
            number = new Number(1,0,data.length);
            sheet.addCell(number);
            number = new Number(2,0,data[0].gcd);
            sheet.addCell(number);
            if (fft != null) {
                for (int i = 0 ; i < fft.length ; i ++) {
                    number = new Number(3,i+1,Complex.abs(fft[i]));
                    sheet.addCell(number);
                }
            }
            writeImage(sheet, imagefile);
            workbook.write();
            workbook.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } 
    }
    private static void writeImage(WritableSheet sheet, String imagefile) {
        File file = new File(imagefile+"_S.png");
        WritableImage a = new WritableImage(5, 0, Config.EXCELWIDTH, Config.EXCELHEIGHT, file);
        file = new File(imagefile+"_F.png");
        WritableImage b = new WritableImage(5, Config.EXCELHEIGHT+1, Config.EXCELWIDTH, Config.EXCELHEIGHT, file);
        sheet.addImage(a);
        sheet.addImage(b);
    }
}
