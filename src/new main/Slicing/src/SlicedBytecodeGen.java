import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Jiyoung Song
 * This class generates .java files that getnerate sliced bytecode
 */
public class SlicedBytecodeGen {

    /**
     * @param slicedFile - the name of sliced file generated from hammacher's javaslicer
     * @return slicedLinesInFile - hashmap that contains sliced line numbers for every class and method
     * @throws IOException
     */
    public HashMap<String, HashSet<Integer>> getSlicedLinesPerFile (String slicedFile) throws IOException{
        HashMap<String, HashSet<Integer>> slicedLinesInFile = new HashMap<String, HashSet<Integer>>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File((slicedFile+".txt"))));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] classNameAndLineNum = line.split(":");
            String[] lineNumAndInst = classNameAndLineNum[1].split(" ");
            if (slicedLinesInFile.containsKey(classNameAndLineNum[0])) {
                HashSet<Integer> lineNumbers = slicedLinesInFile.get(classNameAndLineNum[0]);
                lineNumbers.add(Integer.parseInt(lineNumAndInst[0]));
            } else {
                HashSet<Integer> lineNumbers = new HashSet<Integer>();
                lineNumbers.add(Integer.parseUnsignedInt(lineNumAndInst[0]));
                slicedLinesInFile.put(classNameAndLineNum[0],lineNumbers);
            }
        }
        return slicedLinesInFile;
    }

    /**
     *
     * @param slicedFile
     * @param subjectFiles - the name list of files that is target of slicing
     * @throws IOException
     */
    public void genSlicedClassFileGenerator (String slicedFile, String[] subjectFiles) throws IOException {
        for(String subjectFile: subjectFiles) {
            PrintWriter printWriter = new PrintWriter(new File((subjectFile+".java")));
            printWriter.println("import java.io.FileOutputStream;");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File((subjectFile+".txt"))));
            boolean isSliced = true;
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("public static byte[] dump () throws Exception {")) {
                    printWriter.println("public static void main(final String args[]) throws Exception {");
                    printWriter.println("FileOutputStream fos = new FileOutputStream(\"SlicingExample.class\");");
                    printWriter.println("fos.write(dump());");
                    printWriter.println("fos.close();");
                    printWriter.println("}");
                    isSliced = true;
                } else if (line.contains("mv.visitLineNumber")) {

                }
                if (isSliced)
                    printWriter.println(line);
            }
        }
    }
}
