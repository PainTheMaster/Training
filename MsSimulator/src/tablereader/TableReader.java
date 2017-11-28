package tablereader;

import java.io.*;



public class TableReader implements SiTableReader {
	
	private FileReader fr;
	private BufferedReader br;
	
	String topline;
	
	private int num_header_title, num_table_col, idx_current_line;
	private int[] header_conv;
	
	int delimiter;
	boolean headerGiven;
	
	
	
	

	
	//�w�b�_�[�w��L��B
	public TableReader(String filename, String[] header_title, int delimiter) {
		File csvfile = new File(filename);
		
		try {
			fr = new FileReader(csvfile);
			br = new BufferedReader(fr);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		idx_current_line = 0;

		num_header_title = header_title.length;
		header_conv = new int[num_header_title];
		for(int i = 0; i <= num_header_title-1; i++)
			header_conv[i] = HEADER_CONV_UNSET;	
		
		this.delimiter = delimiter;
		
		analyzeHeader(header_title);
		headerGiven = true;
	}
	
	

	
	//�w�b�_�w��Ȃ��B
	public TableReader(String filename, int delimiter) {
		File csvfile = new File(filename);
		
		try {
			fr = new FileReader(csvfile);
			br = new BufferedReader(fr);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		idx_current_line = 0;
		
//		header_conv = new int[MAX_COL_NUM];	�폜�\��
		
		this.delimiter = delimiter;
		
		analyzeTopLine();
		headerGiven = false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//�w�b�_�[�s����͂��āAString[] title�ŗ^����ꂽ�L�[���[�h�ɑΉ����������߂郁�\�b�h�B
	//�{���\�b�h�����������Ƃ�String[i]�Ɉ�v����w�b�_�^�C�g����header_conf[i]�Ԗ�(�I�t�Z�b�g0)�̃J�������ƕ������ԂɂȂ��Ă���B
	private void analyzeHeader(String[] header) {
		String linebuf = null;
		int from, to, next_delim, last_delim, len_linebuf;
		int idx_col;		//���݈����Ă����̂��߂̃J�E���^�B
		boolean match;
				
		
		try {
			linebuf = br.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		last_delim = linebuf.lastIndexOf(delimiter);
		len_linebuf = linebuf.length();
		
		idx_col = 0;
		from = 0;
		to = 0;
		do {
			next_delim = linebuf.indexOf(delimiter, from);
			
			if(next_delim == from) {		 	//����́A�Z�����󔒂������ꍇ�B
				if(next_delim == len_linebuf-1)	//�s�[��','�������ꍇ���[�v�𔲂�����悤��to���߂Ă����K�v������B
					to = next_delim;
				else
					from = next_delim+1;
				
				idx_col++;	
				continue;					//�Z�����󔒂Ȃ烋�[�v���̂���ȍ~�̏������Ƃ΂��Ă��܂��A�������r�͍s��Ȃ��B
			}
			else if (next_delim > from) {	//�܂Ƃ��ȓ��e���L�����ꍇ�B
				to = next_delim - 1;
			}
			else {							//next_delim��-1�������ꍇ�B�܂�A����from�ȍ~��','���Ȃ������ꍇ�A�s���܂œǂ�
				to = len_linebuf -1;		//�Öق̂����Ɉꕶ���ȏ�̓��e���ۏ؂���Ă���B
			}
			
			for(int i = 0; i <= num_header_title-1; i++) {							//����for���[�v�ŗ^����ꂽ�^�C�g���Ƃ̃}�b�`���m�F����B
				if(header_conv[i] == HEADER_CONV_UNSET) {
					match = linebuf.regionMatches(from, header[i], 0, to-from+1);
					if(match) {														//�}�b�`�ł���΁A�^�C�g���ɑΉ�������ԍ������܂��Ă����B
						header_conv[i] = idx_col;									//i�Ԗڂ̃w�b�_�ɑΉ������J������header_conv[i]�ԖځB
						break;
					}
				}
			}
			
			if(next_delim == len_linebuf-1)
				from = next_delim;
			else
				from = next_delim+1;
			
			idx_col++;			
		}
		while(to < last_delim);		

		
		for(int i = 0; i<=num_header_title-1; i++) {
			if(header_conv[i] == HEADER_CONV_UNSET)
				System.out.println("Header \"" + header[i] + "\" was not found in the .csv file");
		}
		
		num_table_col = idx_col;
		idx_current_line = 1;		//�w�b�_������ꍇ�͍ŏ��̍s�̓w�b�_�Ńf�[�^�͖����̂Ŏ��̍s����B���̕ϐ���getLineElems�Ŏg���B
		

	}
	
	
	
	//�w�b�_�[�̃L�[���[�h���^�����Ă��Ȃ����p�B�P�ɐ擪�s��ǂ�ŗ񐔂�num_table_col�ɓ����B�擪�s���f�[�^�ŁA���Ƃŉ�͂���K�v������̂�topline�ɓ���Ă����B
	private void analyzeTopLine() {

		int from, next_delim;
		int idx_col;		//���݈����Ă����̂��߂̃J�E���^�B


		try {
			topline = br.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		from = 0;
		idx_col = 0;
		while((next_delim = topline.indexOf(delimiter, from)) != -1) {
			from = next_delim+1;
			idx_col++;
		}
		
		
		num_table_col = idx_col+1;
		idx_current_line = 0;			//�w�b�_���ݒ肳��Ă��Ȃ��Ƃ��͍ŏ��̍s����f�[�^�Ȃ̂ŁA����0�s�ڂ�ǂ܂���B

	}
	
	
	
	
	
	//�Ăяo����邲�ƂɂP�s��ǂݍ��݁A�e�v�f��val[]�Ɋi�[���f�[�^�������Ă���ΐ����z��filled[]��true���B�����łȂ����false������B
	//�߂�l�͓ǂݍ��񂾃f�[�^���B�ǂݍ��񂾂Ƃ��Ƀt�@�C���I�[�ł����END_OF_FILE��Ԃ��B
	public int getLineElems(double[] val, boolean[] filled) {
		
		int idx_elem, gt_ans;
		String line = null;
		StringBuilder token;
		LineBuffer lb;
		
		
		
		if(idx_current_line == 0) {
			line = topline;
		}
		else{
			try {
				line = br.readLine();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		idx_current_line++;
		
		if(line == null) {
			for(int i = 0; i <= num_table_col -1; i++) {
				val[i] = 0.0;
				filled[i] = false;
			}
			return END_OF_FILE;
		}
		
		
		lb = new LineBuffer(line, delimiter);	//LineBuffer�N���X��getToken�Ȃǂ̃T�[�r�X��񋟂���B
		
		
		idx_elem = 0;
		token = new StringBuilder();
		
		do {
			gt_ans = lb.getToken(token);
			
			if(token.length() == 0) {						//�f�[�^���󂾂����ꍇ
				val[idx_elem] = 0.0;
				filled[idx_elem] = false;
			}
			else {											//�f�[�^�Ƃ��ĂȂɂ�����L�������ꍇ
				val[idx_elem] = Double.parseDouble(token.toString());
				filled[idx_elem] = true;
			}
			
			idx_elem++;
			
		}
		while(gt_ans != END_OF_STRING);
		

		return idx_elem; //�v�f����Ԃ�
	}
	
	
	
	//�Ăяo����邲�ƂɂP�s��ǂݍ��݁A�e�v�f��stringbuilder[]�Ɋi�[���f�[�^�������Ă���ΐ����z��filled[]��true���B�����łȂ����false������B
	//�߂�l�͓ǂݍ��񂾃f�[�^���B�ǂݍ��񂾂Ƃ��Ƀt�@�C���I�[�ł����END_OF_FILE��Ԃ��B
	�X�^�u�B�e�[�u���̊e�v�f��StringBuilder�I�u�W�F�N�g�ɓ���ĕԂ����\�b�h�B
	public int getLineElemsStr(StringBuilder[] stringbuilder, boolean[] filled) {
		
		int idx_elem, gt_ans;
		String line = null;
		StringBuilder token;
		LineBuffer lb;
		
		
		
		if(idx_current_line == 0) {
			line = topline;
		}
		else{
			try {
				line = br.readLine();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		idx_current_line++;
		
		if(line == null) {
			for(int i = 0; i <= num_table_col -1; i++) {
				stringbuilder[i].setLength(0);;
				filled[i] = false;
			}
			return END_OF_FILE;
		}
		
		
		lb = new LineBuffer(line, delimiter);	//LineBuffer�N���X��getToken�Ȃǂ̃T�[�r�X��񋟂���B
		
		
		idx_elem = 0;
		token = new StringBuilder();
		
		do {
			gt_ans = lb.getToken(token);
			
			if(token.length() == 0) {						//�f�[�^���󂾂����ꍇ
				stringbuilder[idx_elem].setLength(0);;
				filled[idx_elem] = false;
			}
			else {											//�f�[�^�Ƃ��ĂȂɂ�����L�������ꍇ
				stringbuilder[idx_elem]=token;
				//val[idx_elem] = Double.parseDouble(token.toString());
				filled[idx_elem] = true;
			}
			
			idx_elem++;
			
		}
		while(gt_ans != END_OF_STRING);
		

		return idx_elem; //�v�f����Ԃ�
	}
	
	
	
	
	
	
	
	//�Ăяo�����ƂɃw�b�_��͍ς݂�csv�t�@�C�������s��ǂ�ōs���B
	//�߂�l��gelLineElems�̖߂�l�ŗv�f���B���ʂ̍s�ł���Ηv�f(�g�[�N��)���Ńt�@�C���I�[�ł����END_OF_FILE
	public int analyzeLine(double[] data, boolean[] filled) {
		
		int rtn_gLE, tmp_col;
		
		double[] data_buf = new double[num_table_col];
		boolean[] filled_buf = new boolean[num_table_col];
		
		rtn_gLE = getLineElems(data_buf, filled_buf);
		if(headerGiven == false) {
			num_header_title = num_table_col;
			for(int i = 0; i <= num_header_title -1; i++ )
				header_conv[i] = i;
		}
		
		if(rtn_gLE != END_OF_FILE) {
			for(int i = 0; i <= num_header_title-1; i++) {
				tmp_col = header_conv[i];						//i�Ԗڂ̃w�b�_�^�C�g���ɑΉ������s��header_conv[i]�ԖځB
				
				if((filled[i] = filled_buf[tmp_col]) == true)
					data[i] = data_buf[tmp_col];
				else
					data[i] = 0.0;
			}
		}
		
		return rtn_gLE;
		
	}
	
	
	
	
	
	//�X�g���[�������
	public void close() {
		try {
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}





//��s�ɑ�������String���󂯎���ĉ�̓T�[�r�X��񋟂���N���X�B
class LineBuffer implements SiTableReader{
	private int gt_pos, len_str;
	private String s = null;
	int delimiter;
	
	
	public LineBuffer(String given_str, int delimiter) {
		s = given_str;
		
		gt_pos = 0;

		len_str = s.length();
		
		this.delimiter = delimiter;
	}
	
	
	int getToken(StringBuilder return_token) {
		int next_delim, to;
		int ans=0;
		
		next_delim = s.indexOf(delimiter, gt_pos);
		
		if(next_delim == gt_pos) {			//����́A�Z�����󔒂������ꍇ�B
			if(next_delim == len_str-1) {		//�s�E�[��','�ɓ����������ꍇ���[�v�𔲂�����悤��to���߂Ă����K�v������B
				gt_pos = len_str-1;
				return_token.setLength(0);;
				ans = END_OF_STRING;
			}
			else {							//�󔒂��������s���ł͖��������ꍇ�B
				return_token.setLength(0);
				ans = 0;
				gt_pos++;
			}
		}
		else if (next_delim > gt_pos) {		//�s�����œ��e���L�����ꍇ
			to = next_delim - 1;
			return_token.setLength(0);
			return_token.append(s.substring(gt_pos, to+1));
			ans = to - gt_pos + 1;
			
			//pos�̈ړ����s��
			if(next_delim < len_str-1)		//next_delim���܂��s���ł͂Ȃ��ꍇ
				gt_pos = next_delim + 1;
			else
				gt_pos = next_delim;
		}
		else {								//next_delim��-1�������ꍇ�B�܂�A�s���œ��e���L�����ꍇ�B
			to = len_str -1;				//�Öق̂����Ɉꕶ���ȏ�̓��e���ۏ؂���Ă���B
			return_token.setLength(0);
			return_token.append(s.substring(gt_pos, to+1));
			ans = END_OF_STRING;
		}			

		return ans;
	}
	
	

	
}






