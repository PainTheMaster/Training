package tablereader;

public interface SiTableReader{
	//����
//	public static final int DEFAULT_DELIMITER = ',';
	
	//CsvReader�p
	public static final int HEADER_CONV_UNSET = -1;
	public static final int END_OF_FILE = -1;

	
	//LineBuffer�p
	public static final int END_OF_STRING = -1; /*LineBuffer.getToken()�̖߂�l�p*/
	public static final int EOS_VACANT = -2;

}