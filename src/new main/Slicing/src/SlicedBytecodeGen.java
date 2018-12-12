import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Jiyoung Song
 * This class generates .java files that getnerate sliced bytecode
 */
public class SlicedBytecodeGen {
    // slicedLinesPerFile - hashmap that contains sliced lines for each class
    private HashMap<String, HashSet<Integer>> slicedLinesPerFile = new HashMap<String, HashSet<Integer>>();

    /**
     * set sliced lines for each class (slicedLinesInFile) when file name is given
     * @param slicedFile - the name of sliced file generated from hammacher's javaslicer
     * @return slicedLinesInFile - hashmap that contains sliced line numbers for every class and method
     * @throws IOException
     */
    public void setSlicedLinesPerFile (String slicedFile) throws IOException{
        HashMap<String, HashSet<Integer>> slicedLinesInFile = new HashMap<String, HashSet<Integer>>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File((slicedFile+".txt"))));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] classNameAndLineNum = line.split(":");
            String[] lineNumAndInst = classNameAndLineNum[1].split(" ");
            if (slicedLinesInFile.containsKey(classNameAndLineNum[0])) {
                HashSet<Integer> lineNumbers = slicedLinesInFile.get(classNameAndLineNum[0]);
                lineNumbers.add(Integer.parseInt(lineNumAndInst[0].trim()));
            } else {
                HashSet<Integer> lineNumbers = new HashSet<Integer>();
                lineNumbers.add(Integer.parseInt(lineNumAndInst[0].trim()));
                slicedLinesInFile.put(classNameAndLineNum[0],lineNumbers);
            }
        }
        this.slicedLinesPerFile = slicedLinesInFile;
    }

    /**
     * get private variable slicedLinesInFile
     * @return slicedLinesInFile
     */
    public HashMap<String, HashSet<Integer>> getSlicedLinesPerFile () {
        return slicedLinesPerFile;
    }

    /**
     * generate a sliced file.
     * @param slicedFile
     * @param subjectFiles - the name list of files that is target of slicing
     * @throws IOException
     */
    public void genSlicedClassFileGenerator (String slicedFile, String[] subjectFiles) throws IOException {
        String currentClassName = "";
        String currentMethodName = "";

        for(String subjectFile: subjectFiles) {
            PrintWriter printWriter = new PrintWriter(new File((subjectFile+".java")));
            printWriter.println("import java.io.FileOutputStream;");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File((subjectFile+".txt"))));
            boolean isSliced = true;
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("public static byte[] dump () throws Exception {")) {
                    printWriter.println("public static void main(final String args[]) throws Exception {");
                    printWriter.println("FileOutputStream fos = new FileOutputStream(\""+slicedFile+".class\");");
                    printWriter.println("fos.write(dump());");
                    printWriter.println("fos.close();");
                    printWriter.println("}");
                    isSliced = true;
                } else if (line.contains("cw.visit")) {
                    String[] classVisitInst = line.split(",");
                    currentClassName = classVisitInst[2].replace("\"", "").trim();
                } else if (line.contains("cw.visitMethod")) {
                    String[] methodVisitInst = line.split(",");
                    currentMethodName = methodVisitInst[1].replace("\"", "").trim();
                } else if (line.contains("mv.visitLineNumber")) {
                    String[] visitLineNumInst = line.split(",()");
                    isSliced = checkIsSlicedLine(Integer.parseInt(visitLineNumInst[1].trim()), slicedLinesPerFile.get(currentClassName+"."+currentMethodName));
                }
                if (!isSliced)
                    printWriter.println(line);
                else {
                    if(line.contains("new Label()") || line.contains("mv.visitLabel"))
                        printWriter.println(line);
                }

            }
        }
    }

    /**
     *
     * @param lineNum
     * @param slicedInst
     * @return
     */
    public boolean checkIsSlicedLine (int lineNum, HashSet<Integer> slicedInst) {
        for(Integer i : slicedInst) {
            if (lineNum == i)
                return true;
        }
        return false;
    }
}
