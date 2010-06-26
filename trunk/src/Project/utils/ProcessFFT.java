package Project.utils;

import Project.StaticManager;
import Project.email.SendMail;
import Project.test.DataStruct;
import Project.utils.FFT.Complex;
import Project.utils.FFT.DFT;
import Project.utils.FFT.FFT;

public class ProcessFFT {
    public static Complex[] processFFT(DataStruct[] data, String ip, int port) {
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
            if (dis <= 0.1) {
                isSame++;
            }
        }
        if (isSame/(N/2.0) >= 0.8) {
            System.out.println(String.format("%15s %4d %.2f 有週期特性", ip,port,(isSame/(N/2.0))));
            // TODO event 通知管理者 flow分析到有週期性網路訊息傳遞
            SendMail.getInstance().sendMail(String.format("%s\n%d\n%.3f\n有週期特性", ip, port, (isSame/(N/2.0))), StaticManager.FLOW_DETECTED, StaticManager.OPTION_SEVERE);
            /*for(int i=0; i<N; i++) {
                System.out.println( Complex.abs(x[i]) + " ... " + N*x2[i]);
            }*/
            return x;
        }
        return null;
    }
    public static int gcd(int m, int n) { 
        if(n != 0) return gcd(n, m % n); else return m; 
    }
}
