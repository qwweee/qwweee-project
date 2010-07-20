package Project.test;

import java.sql.SQLException;

import Project.StaticManager;
import Project.config.Config;
import Project.config.DBConfig;
import Project.db.DBFunction;
import Project.db.DatabaseFactory;
import Project.email.SendMail;
import Project.struct.DataStruct;
import Project.struct.FlowGroup;
import Project.test.TestDB;
import Project.utils.ExcelUtil;
import Project.utils.ProcessFFT;
import Project.utils.FFT.Complex;
import Project.utils.FFT.DFT;
import Project.utils.FFT.FFT;

public class TestFFT {
	
	public static void main(String[] args) throws SQLException {
		//Complex c1 = new Complex(0, 1);
		//System.out.println( Complex.multiply(c1, c1).toString() );
		//Complex w = Complex.nthRootOfUnity(0,4);
		//System.out.println(w.toString());
	    Config.Load();
        DBConfig.Load();
        DatabaseFactory.setDatabaseSettings(Config.DBDriver, Config.DBURL, Config.DBUser, Config.DBPassword, Config.DBMaxCon);
        DatabaseFactory.getInstance();
        DBFunction.getInstance();
	    String ip = "10.10.32.154";
	    FlowGroup[] list = DBFunction.getInstance().getFlowGroups(ip);
	    for (int i = 0 ; i < list.length ; i ++) {
	        DataStruct[] data = DBFunction.getInstance().getFlowsData(ip, list[i].ip, list[i].port, true);
	        if (data == null || data.length <= Config.GROUPCOUNT) {
	            //System.out.println(String.format("%15s %4d 無週期特性", list[i].ip,list[i].port));
	            continue;
	        }
	        Complex[] tmp = null;
	        if ((tmp = ProcessFFT.processFFT(data,list[i].ip,list[i].port)) != null) {
	            //System.out.println(i);
	            ExcelUtil.writeExcel(ip, list[i].ip, list[i].port, data, false, tmp);
	        }
	    }
	    /*DataStruct[] data = TestDB.getData(ip, list[index].ip, list[index].port);
		int N=data.length;
		Complex[] c = new Complex[N];
		double[] num = new double[N];
		for(int i=0; i<N; i++) {
			num[i] = data[i].dataSize;
			c[i] = new Complex(num[i],0);
		}
		
		Complex[] x = FFT.forward(c);
		double[] x2 = DFT.forwardMagnitude(num);
		//x = FFT.inverse(x);
		double dis = 0;
		int isSame = 0;
		for (int i = 0 ; i < N/2 ; i ++) {
		    dis = Complex.abs(x[i])-Complex.abs(x[(N/2)+i]);		    
		    if (dis <= 0.5) {
		        isSame++;
		    }
		}
		if (isSame/(N/2.0) >= 0.8) {
		    System.out.println("相同");
		}
		
		for(int i=0; i<N; i++) {
			System.out.println( Complex.abs(x[i]) + " ... " + N*x2[i]);
		}
		
		System.out.println("db: " + Math.log10(0.09*100+1));
		*/
//		for(int i=0; i<8; i++) {
//			int j =	FFT.bitReverse(i);
//			System.out.println(i + "-> " + j);
//		}
		
//		System.out.println( FFT.toBinary(6) );
		
//		int x = 2&2;
//		System.out.println( x );
		
		String str = null;
		if( str!=null && str.length()>0 && str.substring(0, 3).equalsIgnoreCase("ugu") ) {
			System.out.println("bikbik");
		}
		
	}
}

