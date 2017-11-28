package tablereader;

public interface SiTableReader{
	//共通
//	public static final int DEFAULT_DELIMITER = ',';
	
	//CsvReader用
	public static final int HEADER_CONV_UNSET = -1;
	public static final int END_OF_FILE = -1;

	
	//LineBuffer用
	public static final int END_OF_STRING = -1; /*LineBuffer.getToken()の戻り値用*/
	public static final int EOS_VACANT = -2;

}