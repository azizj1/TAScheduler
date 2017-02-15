import java.io.File;

public interface FileFormat {
	/*
	 * INSTRUCTOR FILE
	 * [lab1]
	 * section= 800 <= n <= 820
	 * startTime=military time
	 * endTime=military time
	 * day=M, T, W, R, or F
	 * .
	 * .
	 * .
	 * [labN]
	 * Section=800 <= n <= 820
	 * starttime=digitaltime
	 * endtime=digital time
	 * day=M, T, W, R, or F
	 * 
	 * [ta1]
	 * name=white spaces and letters
	 * id=string alphanumeric with first character a letter
	 * numSec= 1, 2, or 3
	 * .
	 * .
	 * .
	 * [taM]
	 * name=String: white spaces and letters
	 * id= String: alphanumeric with first character a letter
	 * numSec= 1, 2, or 3
	 * 
	 * [constraint]
	 * minPrefSec= 0 <= n <= N, where N = number of labs
	 */
	
	/*
	 * TA FILE
	 * [info]
	 * Name=name
	 * Id=id
	 * 
	 * [class1]
	 * Starttime=digitaltime
	 * Endtime=digitaltime
	 * Day=Combination of M, T, W, R, F
	 * .
	 * .
	 * .
	 * [classN]
	 * Starttime=digitaltime
	 * Endtime=digitaltime
	 * Day=Combination of M, T, W, R, F
	 * 
	 * [Avoid1]
	 * Starttime=digitaltime
	 * Endtime=digitaltime
	 * Day=Combination of M, T, W, R, F
	 * Reason=String
	 * .
	 * .
	 * .
	 * [avoidM]
	 * Starttime=digitaltime
	 * Endtime=digitaltime
	 * Day=Combination of M, T, W, R, F
	 * Reason=String
	 * 
	 * [pref]
	 * Pref1=secNum
	 * Pref2=secNum
	 * .
	 * .
	 * .
	 * PrefT=secNum
	 */
	String 
		STARTTIME_IDENTIFIER = "starttime", 
		ENDTIME_IDENTIFIER = "endtime", 
		DAY_REGEX = "days?", 
		TA_NAME_IDENTIFIER = "name", 
		ID_IDENTIFIER = "id",
		NUMSEC_IDENTIFIER = "numsec",
		LAB_SEC_IDENTIFIER = "section", 
		MIN_PREF_SEC_IDENTIFIER = "minprefsec",
		REASON_IDENTIFIER = "reason",
		PREF_REGEX = "pref\\d+";
	
	void add(IniFile file);
	File getFile();
}
