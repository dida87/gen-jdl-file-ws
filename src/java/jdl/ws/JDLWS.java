/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dock.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
/**
 *
 * @author dida
 */

public class JDLWS {

    /**
     * This is a sample web service operation
     */
   
    //Directories
    final String gridDir = "/biomed/user/l/louacheni";
    final String diracDir = "/home/dida/DIRAC/scripts";
    final String filesDir = "/home/dida/DIRAC";
    final String gridInput = gridDir + "/scripts";
    final String gridFiles = gridDir + "/files";
    final String gridTools = gridDir + "/tools";
    final int nJob = 5;
    int j;
    String line="";
   
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    @WebMethod(operationName = "submitJob")
    public ArrayList<String> submitJob (@WebParam ( name = "file") String file ){
        ArrayList<String> jobID = new ArrayList<String>();
        //open dpf file
        int linenumb = 0;
        try{
            //final String fileDir = "/home/dida/DIRAC";  
            String fileInput = filesDir + "/" + "dpfFile.txt";
            if (!fileInput.equals("")){
            FileInputStream finput = new FileInputStream(fileInput);
            BufferedReader br = new BufferedReader(new InputStreamReader(finput));
            while ((line=br.readLine()) != null){
                linenumb++;
            }    
            }
        }catch (Exception e){
            e.printStackTrace();
           
        }
        //generate jdl file to submit job
        for (j = 1; j <= linenumb; j++){
            try{
            //generate jdl filess
            generateDockingJDL(file, j);
            } catch (Exception e){
                e.printStackTrace();
            }
       
   //execute command shell line
    //String scriptShell = "/home/dida/DIRAC/FILEjdl/docking-1007.jdl";
    //String command =  diracDir + "/dirac-wms-job-submit" + " " + scriptShell;
    //String command = diracDir + "/dirac-wms-job-submit" + " " + "/home/dida/DIRAC/FILEjdl" + "/docking-" + j + ".jdl";
    //String output = executeShellCommand(command);
    //jobID.add(output);
        }
         
    return jobID;    
}
   
    private ArrayList<String> generateDockingJDL(String file, int j) {
        ArrayList<String> files = new ArrayList<String>();
        ArrayList<String> lineFile = new ArrayList<String>();
        //String lineFile = "";
        String inputFile = filesDir + "/dpfFile.txt";
        try{
            if (!inputFile.equals("")){
            FileInputStream finput = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(finput));
            String line="";
            while ((line=br.readLine()) != null){
                 lineFile.add(line);
            } 
            return lineFile;
            }
        }catch (Exception e){
            e.printStackTrace();
           
        }
        File fileDir = new File (filesDir + "/" + file);
        //create file if it doesn't exist
        if (!fileDir.exists()){
            //create didrectory
            boolean create = fileDir.mkdir(); //or fileDir.mkdir();
            if (!create){
                System.out.println("Error creating directory" + fileDir);
            }
            String path = fileDir.getAbsolutePath();
            System.out.println("The path of the file is " + path);
        }
        //open file name
        String fileName = fileDir + "/docking-" + j + ".jdl";
        try{
            //write to file
            for ( int i = 0; i < lineFile.size(); i++){
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            //job name
            bw.write("JobName = \"docking-" + j + "-" + file + "\";");
            bw.newLine();
            //executable
            bw.write("Executable = \"" + filesDir + "/dock.sh\" ;");
            //bw.write("Executable = \"" +filesDir + "/dock.sh\";");
            bw.newLine();
            //argments
            bw.write("Arguments = \"" + lineFile.get(i)+ "\";");
            //bw.write("Arguments = \"" + file + "\";");
            bw.newLine();
            //output
            bw.write("StdOutput = \"stdOut\";");
            bw.newLine();
            //error
            bw.write("StdError = \"stdErr\";");
            bw.newLine();
            //inputsand box
            bw.write("InputSandbox = {\"LFN:"+ gridFiles +"/dpfFile.txt\","
                    + "\""+ filesDir + "/autodock.sh\","
                    + "\""+ filesDir + "/dock.sh\","
                    + "\"LFN:" + gridDir + "/fileDock.tar.gz\","
                    + "\"LFN:"+ gridFiles + "/ligand.txt\","
                    + "\"LFN:"+ gridFiles + "/1OKE.txt\","
                    + "\"LFN:"+ gridTools + "/autodock.tar.gz\","
                    + "\"LFN:"+ gridDir +"/dockFile.tar.gz\"};");
            bw.newLine();
            //outputSandbox
            bw.write("OutputSandbox = {\"stdout\", \"stdErr\", \"dockFile.tar.bz2\", \"dockFile1.tar.bz2\";}");
            bw.newLine();
           //close file
           bw.close();
            System.out.println("Done");
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        files.add(fileName);
        return files;
        }

    private String executeShellCommand(String command) {
        String lineCommand = "";
        try
        {
            Runtime env = Runtime.getRuntime();
            Process process = env.exec(command);
           InputStreamReader isr =  new InputStreamReader ( process.getInputStream() );
            BufferedReader input = new BufferedReader (isr);

            while((lineCommand = input.readLine()) != null )
            {
                System.out.println(lineCommand);
            }
            int exitVal = process.waitFor();
            System.out.println("Exited with error code "+ exitVal);
            process.getInputStream().close();
            input.close();
            process.destroy();
        }
        catch( Exception e)
        {
            //e.printStackTrace();
            lineCommand = "Error: " + e.getMessage();
        }
        return lineCommand;
    }
}






















 