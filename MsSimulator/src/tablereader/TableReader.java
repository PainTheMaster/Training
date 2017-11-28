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
	
	
	
	

	
	//ヘッダー指定有り。
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
	
	

	
	//ヘッダ指定なし。
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
		
//		header_conv = new int[MAX_COL_NUM];	削除予定
		
		this.delimiter = delimiter;
		
		analyzeTopLine();
		headerGiven = false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//ヘッダー行を解析して、String[] titleで与えられたキーワードに対応する列を求めるメソッド。
	//本メソッドが走ったあとはString[i]に一致するヘッダタイトルがheader_conf[i]番目(オフセット0)のカラムだと分かる状態になっている。
	private void analyzeHeader(String[] header) {
		String linebuf = null;
		int from, to, next_delim, last_delim, len_linebuf;
		int idx_col;		//現在扱っている列のためのカウンタ。
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
			
			if(next_delim == from) {		 	//これは、セルが空白だった場合。
				if(next_delim == len_linebuf-1)	//行端が','だった場合ループを抜けられるようにtoを定めておく必要がある。
					to = next_delim;
				else
					from = next_delim+1;
				
				idx_col++;	
				continue;					//セルが空白ならループ内のこれ以降の処理をとばしてしまい、文字列比較は行わない。
			}
			else if (next_delim > from) {	//まともな内容が有った場合。
				to = next_delim - 1;
			}
			else {							//next_delimが-1だった場合。つまり、もうfrom以降に','がなかった場合、行末まで読む
				to = len_linebuf -1;		//暗黙のうちに一文字以上の内容が保証されている。
			}
			
			for(int i = 0; i <= num_header_title-1; i++) {							//このforループで与えられたタイトルとのマッチを確認する。
				if(header_conv[i] == HEADER_CONV_UNSET) {
					match = linebuf.regionMatches(from, header[i], 0, to-from+1);
					if(match) {														//マッチであれば、タイトルに対応した列番号をしまっておく。
						header_conv[i] = idx_col;									//i番目のヘッダに対応したカラムはheader_conv[i]番目。
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
		idx_current_line = 1;		//ヘッダがある場合は最初の行はヘッダでデータは無いので次の行から。この変数はgetLineElemsで使う。
		

	}
	
	
	
	//ヘッダーのキーワードが与えられていない時用。単に先頭行を読んで列数をnum_table_colに入れる。先頭行もデータで、あとで解析する必要があるのでtoplineに入れておく。
	private void analyzeTopLine() {

		int from, next_delim;
		int idx_col;		//現在扱っている列のためのカウンタ。


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
		idx_current_line = 0;			//ヘッダが設定されていないときは最初の行からデータなので、次も0行目を読ませる。

	}
	
	
	
	
	
	//呼び出されるごとに１行を読み込み、各要素をval[]に格納しデータが入っていれば随伴配列filled[]にtrueを。そうでなければfalseを入れる。
	//戻り値は読み込んだデータ数。読み込んだときにファイル終端であればEND_OF_FILEを返す。
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
		
		
		lb = new LineBuffer(line, delimiter);	//LineBufferクラスはgetTokenなどのサービスを提供する。
		
		
		idx_elem = 0;
		token = new StringBuilder();
		
		do {
			gt_ans = lb.getToken(token);
			
			if(token.length() == 0) {						//データが空だった場合
				val[idx_elem] = 0.0;
				filled[idx_elem] = false;
			}
			else {											//データとしてなにかしら有ったた場合
				val[idx_elem] = Double.parseDouble(token.toString());
				filled[idx_elem] = true;
			}
			
			idx_elem++;
			
		}
		while(gt_ans != END_OF_STRING);
		

		return idx_elem; //要素数を返す
	}
	
	
	
	//呼び出されるごとに１行を読み込み、各要素をstringbuilder[]に格納しデータが入っていれば随伴配列filled[]にtrueを。そうでなければfalseを入れる。
	//戻り値は読み込んだデータ数。読み込んだときにファイル終端であればEND_OF_FILEを返す。
	スタブ。テーブルの各要素をStringBuilderオブジェクトに入れて返すメソッド。
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
		
		
		lb = new LineBuffer(line, delimiter);	//LineBufferクラスはgetTokenなどのサービスを提供する。
		
		
		idx_elem = 0;
		token = new StringBuilder();
		
		do {
			gt_ans = lb.getToken(token);
			
			if(token.length() == 0) {						//データが空だった場合
				stringbuilder[idx_elem].setLength(0);;
				filled[idx_elem] = false;
			}
			else {											//データとしてなにかしら有ったた場合
				stringbuilder[idx_elem]=token;
				//val[idx_elem] = Double.parseDouble(token.toString());
				filled[idx_elem] = true;
			}
			
			idx_elem++;
			
		}
		while(gt_ans != END_OF_STRING);
		

		return idx_elem; //要素数を返す
	}
	
	
	
	
	
	
	
	//呼び出しごとにヘッダ解析済みのcsvファイルから一行を読んで行く。
	//戻り値はgelLineElemsの戻り値で要素数。普通の行であれば要素(トークン)数でファイル終端であればEND_OF_FILE
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
				tmp_col = header_conv[i];						//i番目のヘッダタイトルに対応した行はheader_conv[i]番目。
				
				if((filled[i] = filled_buf[tmp_col]) == true)
					data[i] = data_buf[tmp_col];
				else
					data[i] = 0.0;
			}
		}
		
		return rtn_gLE;
		
	}
	
	
	
	
	
	//ストリームを閉じる
	public void close() {
		try {
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}





//一行に相当するStringを受け取って解析サービスを提供するクラス。
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
		
		if(next_delim == gt_pos) {			//これは、セルが空白だった場合。
			if(next_delim == len_str-1) {		//行右端の','に当たったた場合ループを抜けられるようにtoを定めておく必要がある。
				gt_pos = len_str-1;
				return_token.setLength(0);;
				ans = END_OF_STRING;
			}
			else {							//空白だったが行末では無かった場合。
				return_token.setLength(0);
				ans = 0;
				gt_pos++;
			}
		}
		else if (next_delim > gt_pos) {		//行内部で内容が有った場合
			to = next_delim - 1;
			return_token.setLength(0);
			return_token.append(s.substring(gt_pos, to+1));
			ans = to - gt_pos + 1;
			
			//posの移動を行う
			if(next_delim < len_str-1)		//next_delimがまだ行末ではない場合
				gt_pos = next_delim + 1;
			else
				gt_pos = next_delim;
		}
		else {								//next_delimが-1だった場合。つまり、行末で内容が有った場合。
			to = len_str -1;				//暗黙のうちに一文字以上の内容が保証されている。
			return_token.setLength(0);
			return_token.append(s.substring(gt_pos, to+1));
			ans = END_OF_STRING;
		}			

		return ans;
	}
	
	

	
}






