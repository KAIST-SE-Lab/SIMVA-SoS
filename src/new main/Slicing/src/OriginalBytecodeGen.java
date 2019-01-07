import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class OriginalBytecodeGen {
    public void genOriginalBytecode (String[] args) throws IOException {
        for (String arg: args) {
            new ClassReader(OriginalBytecodeGen.class.getResourceAsStream((arg+".class")))
                    .accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(new File((arg+".txt")))), 0);
        }
    }
}