package SoSModelSlicer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SoSSlicer {

    public static void main (String[] args) throws IOException, InterruptedException {

        SoSSlicer sosSlicer = new SoSSlicer();
        // Step 1. Collecting subject Java files.
        sosSlicer.commandExecuter("cmd.exe /c dir /b /s *.java > ./javaFiles.txt", "1", "Collecting subject Java files.");

        ArrayList<String> javaFileList = sosSlicer.fileReader(".\\JavaFiles.txt");

        String files = "";
        for (String fileName : javaFileList) {
            if (fileName.contains("SoSSlicer.java"))
                continue;
            files = files+" "+fileName;
        }

        // Step 2. Compiling the subject Java files.
        sosSlicer.commandExecuter("cmd.exe /c javac -source 1.6 -target 1.6 -g "+files+ " -encoding UTF-8", "2", "Compling the subject Java files.");

        // Step 3. Tracing the subject Java files.
        sosSlicer.commandExecuter("cmd.exe /c java -javaagent:./lib/tracer.jar=tracefile:SoStrace.trace -cp ./bin/slicingInput new_simvasos.simulation.Simulation_Firefighters", "3", "Tracing subject Java files.");

        // Step 4. Slicing the subject Java files.
        sosSlicer.commandExecuter("cmd.exe /c java -Xmx2g -jar ./lib/slicer.jar -p SoStrace.trace " + "new_simvasos.simulation.Simulation_Firefighters.main:30:*" + " > SlicedSoS.txt", "4", "Slicing subject Java files.");

        // Step 5. Making sliced contents compilable & executable.

        // Step 5-1. Collecting sliced classes and line numbers.
        HashMap<String, HashSet<Integer>> slicedLinesPerClass= sosSlicer.setSlicedLinesPerClass();
        System.out.println("Succeed step 5-1: Collecing slices classes and line numbers.");

        // Step 5-2. Copying input files to the output folder.
        String prevSourcePath = "";
        for (String javaFile : javaFileList) {
            if (javaFile.contains("SoSSlicer.java"))
                continue;
            String[] folders = javaFile.split("\\\\");
            String folderName = "";
            for (int i = 0; i<folders.length-1; i++) {
                folderName = folderName +folders[i] +"\\";
            }
            if (prevSourcePath.equals("") || (!prevSourcePath.equals("") && !prevSourcePath.equals(folderName))) {
                prevSourcePath = folderName;
                File source = new File(folderName);
                sosSlicer.copyDirectory (source, folderName.replace("slicingInput", "slicingOutput"));
            }
        }
        System.out.println("Succeed step 5-2: Copying input files to the output folder.");

        // Step 5-3. Applying ORBS to make the sliced program executable.
        System.out.println("Step 5-3 starts.");
        for(String fileName: javaFileList) {
            String compactFileName = sosSlicer.isFileToBeSliced(fileName, slicedLinesPerClass.keySet());

            if (!compactFileName.equals("")) {
                System.out.println("***Current sliced file name: "+compactFileName);
                ArrayList<String> programLines = sosSlicer.fileReader(fileName);
                int programSize = programLines.size();
                ArrayList<Boolean> areSlicedLines = new ArrayList<Boolean>();
                for (int i = 0; i<programSize; i++){
                    areSlicedLines.add(false);
                }
                String newFilePath = fileName.replace("slicingInput", "slicingOutput");
                for (int i = programSize; i>0; i--) {
                    if (sosSlicer.isCollectedLine(i, slicedLinesPerClass.get(compactFileName)))
                        continue;
                    File newFile = new File(newFilePath);

                    // Should it be removed? true -> yes, false -> no
                    areSlicedLines.set(i-1, true);
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
                    for (int j = 0; j<programLines.size(); j++) {
                        if (!areSlicedLines.get(j))
                            pw.println(programLines.get(j));
                    }
                    pw.close();

                    String newfiles = "";
                    for (String newfileName : javaFileList) {
                        String modifiedFilePath = newfileName.replace("slicingInput", "slicingOutput");
                        if (fileName.contains("SoSSlicer.java"))
                            continue;
                        newfiles = newfiles+" "+modifiedFilePath;
                    }
                    boolean isSucceed = sosSlicer.errorCommandExecuter("cmd.exe /c javac -source 1.6 -target 1.6 "+newfiles+ " -encoding UTF-8", i);
                    if (!isSucceed)
                        areSlicedLines.set(i-1, false);

                    newFile.delete();
                }

                File finalSlicedFile = new File(newFilePath);
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(finalSlicedFile)));
                for (int i = 0; i<programSize; i++) {
                    if (!areSlicedLines.get(i))
                        pw.println(programLines.get(i));
                }
                pw.close();
            }
        }
    }

    public void copyDirectory(File sourceLocation, String destLocation)
            throws IOException {
        File targetFolder = new File(destLocation);
        if (sourceLocation.isDirectory()) {
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            String[] destDirs = destLocation.split("\\\\");
            File[] children = sourceLocation.listFiles();
            for (int i = 0; i < children.length; i++) {
                String[] sourceDirs = children[i].getPath().split("\\\\");
                if (destDirs.length == (sourceDirs.length-1)) {
                    copyDirectory(new File(sourceLocation, children[i].getName()),
                            destLocation);
                }
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetFolder + "\\"+ sourceLocation.getName(), true);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }



    public boolean errorCommandExecuter (String command, int lineNum) throws InterruptedException, IOException {
        Runtime run = Runtime.getRuntime();
        final Process proc;
        proc = run.exec(command);
        proc.getInputStream().close();
        proc.getOutputStream().close();
        proc.getErrorStream().close();
        int rst = proc.waitFor();
        proc.destroy();
        if (rst == 0) {
            System.out.println("Succeed removing line " + lineNum);
            return true;
        } else {
            System.out.println("Fail removing line " + lineNum);
            return false;
        }
    }

    public void commandExecuter (String command, String step, String message) throws InterruptedException, IOException {
        Runtime run = Runtime.getRuntime();
        final Process proc;
        proc = run.exec(command);
        new Thread(new Runnable() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;

                try {
                    while ((line = input.readLine()) != null)
                        System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        int rst = proc.waitFor();
        proc.destroy();
        if (rst == 0)
            System.out.println("Succeed step "+step+": "+message);
        else
            System.out.println("Fail step "+step+": "+message);
    }

    public ArrayList<String> fileReader (String fileName) {
        ArrayList<String> javaFileList = new ArrayList<String>();
        try {
            File file = new File(fileName);
            FileReader inputFileReader;
            inputFileReader = new FileReader(file);
            BufferedReader lineReader = new BufferedReader(inputFileReader);
            String line = "";
            while ((line = lineReader.readLine()) != null) {
                javaFileList.add(line);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return javaFileList;
    }

    public boolean isCollectedLine (int lineNum, HashSet<Integer> lines) {
        for (int line: lines) {
            if(line == lineNum) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, HashSet<Integer>> setSlicedLinesPerClass () throws IOException {
        HashMap<String, HashSet<Integer>> slicedLinesPerFile = new HashMap<String, HashSet<Integer>>();
        File slicedInstructionFile = new File("SlicedSoS.txt");
        if (slicedInstructionFile.isFile()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(slicedInstructionFile));
            String line = "";
            while (!(line = bufferedReader.readLine()).contains("finished")) {}
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.contains(":"))
                    continue;
                String[] classNameAndLineNum = line.split(":");
                String[] lineNumAndInst = classNameAndLineNum[1].split(" ");
                String[] untilClassName = classNameAndLineNum[0].split("\\.");
                String keyClassName = untilClassName[0];
                for (int i = 1; i<untilClassName.length-1; i++) {
                    keyClassName = keyClassName+"\\"+untilClassName[i];
                }
                boolean isContained = false;
                for (String key: slicedLinesPerFile.keySet()) {
                    if (key.equals(keyClassName))
                        isContained = true;
                }
                if (isContained) {
                    HashSet<Integer> lineNumbers = slicedLinesPerFile.get(keyClassName);
                    lineNumbers.add(Integer.parseInt(lineNumAndInst[0].trim()));
                } else {
                    HashSet<Integer> lineNumbers = new HashSet<Integer>();
                    lineNumbers.add(Integer.parseInt(lineNumAndInst[0].trim()));
                    slicedLinesPerFile.put(keyClassName,lineNumbers);
                }
            }
        }
        return slicedLinesPerFile;
    }

    public String isFileToBeSliced (String fileName, Set<String> keySet) {
        for (String key: keySet) {
            if (fileName.contains(key))
                return key;
        }
        return "";
    }
}