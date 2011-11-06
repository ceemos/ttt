package ttt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author besitzer
 */
public class fs {

    public static String curdir = System.getProperty("user.dir");
    public static String sep = File.separator;
    public static String psep = File.pathSeparator;

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    // Copies all files under srcDir to dstDir.
    // If dstDir does not exist, it will be created.
    public static void copyDirectory(File srcDir, File dstDir){
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdirs();
            }

            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                        new File(dstDir, children[i]));
            }
        } else {
            try {
                copy(srcDir, dstDir);
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }
    
    public static boolean run(File f, String... args){
        Process p;
        try {
            p = Runtime.getRuntime().exec(args, null, f);
            InputStream es = p.getErrorStream();
            InputStream is = p.getInputStream();
            new StreamPrinter(is, System.out).start();
            new StreamPrinter(es, System.err).start();

            p.waitFor();
            
        } catch (InterruptedException ex) {
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        
        return true; 
    }

    public static boolean  run(String... args){
        return run(null, args);
    }
    
    public static class StreamPrinter extends Thread {

        InputStream is;
        PrintStream out;
        
        public StreamPrinter(InputStream is, PrintStream out) {
            this.is = is;
            this.out = out;
        }

        public void run() {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            try {
                do {
                    String z = r.readLine();
                    if (z == null) break;
                    out.println(z);
                } while (true);
            } catch (IOException e) {
            }
            try {
                r.close();
            } catch (IOException ex) {
            }
        }
        
    }


}
