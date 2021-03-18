package nl.saxion.cos;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {

	@Test
	void testHelloWorld() throws Exception {
		Compiler c = new Compiler();
		JasminBytecode code = c.compileFile("testFiles/test/print.pc", "main");
		assertNotNull(code);
		AssembledClass aClass = AssembledClass.assemble(code);

		// Run the class
		SandBox s = new SandBox();
		s.runClass(aClass);
		List<String> output = s.getOutput();

		// Check that output matches what we expect
		assertArrayEquals(new String[]{
				"Hello World!"
		}, output.toArray());
	}

    @Test
    void testForLoops() throws Exception {
        Compiler c = new Compiler();
        JasminBytecode code = c.compileFile("testFiles/ForLoop/forloops.pc", "main");
        assertNotNull(code);
        AssembledClass aClass = AssembledClass.assemble(code);

        // Run the class
        SandBox s = new SandBox();
        s.runClass(aClass);
        List<String> output = s.getOutput();

        // Check that output matches what we expect
        assertArrayEquals(new String[]{
        		"increasing for loop",
                "0",
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7",
				"8",
				"9",
				"decreasing for loop",
				"9",
				"8",
				"7",
				"6",
				"5",
				"4",
				"3",
				"2",
				"1",
				"0",
        }, output.toArray());
    }

    @Test
    void testIfStatements() throws Exception {
        Compiler c = new Compiler();
        JasminBytecode code = c.compileFile("testFiles/ifStatement/ifStatement.pc", "main");
        assertNotNull(code);
        AssembledClass aClass = AssembledClass.assemble(code);

        // Run the class
        SandBox s = new SandBox();
        s.runClass(aClass);
        List<String> output = s.getOutput();

        // Check that output matches what we expect
        assertArrayEquals(new String[]{
                ""
        }, output.toArray());
    }

    @Test
    void testMathEquation() throws Exception {
        Compiler c = new Compiler();
        JasminBytecode code = c.compileFile("testFiles/MathEquations/Math.pc", "main");
        assertNotNull(code);
        AssembledClass aClass = AssembledClass.assemble(code);

        // Run the class
        SandBox s = new SandBox();
        s.runClass(aClass);
        List<String> output = s.getOutput();

        // Check that output matches what we expect
        assertArrayEquals(new String[]{
                ""
        }, output.toArray());
    }
}