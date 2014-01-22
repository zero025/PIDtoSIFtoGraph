package de.bioquant.cytoscape.ConnectOwlAndXmlOfPID.FileReader;

import java.io.File;

public abstract class AbstractFileReaderTest {

	private String testFilepath;

	public AbstractFileReaderTest() {
		File path= new File("");
		testFilepath=path.getAbsolutePath()+"/resource/test/";
		testFilepath=testFilepath.replace('\\', '/');
	}

	/**
	 * @return the testFilepath
	 */
	public String getTestFilepath() {
		return testFilepath;
	}
	
	

}
