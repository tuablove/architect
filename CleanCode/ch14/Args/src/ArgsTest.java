import junit.framework.TestCase;

public class ArgsTest extends TestCase{
	public void testCreateWithNoSchemaOrArguments() throws Exception{
		Args args = new Args("", new String[0]);
		assertEquals(0, args.carinality());
	}
	
	public void testWithNoSchemaButWithMultipleArguments() throws Exception { 
		try {
			new Args("", new String[]{"-x", "-y"});
			fail();
		} catch (ArgsException e) {
			assertEquals(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT, e.getErrorCode());
			assertEquals('x', e.getErrorArgumentId()); 
		}
	}
}
