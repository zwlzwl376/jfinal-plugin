package net.oschina.zwlzwl376.jfinal.plugin.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
  
public class FileScanner { 
    
    private static Logger log = Logger.getLogger(FileScanner.class);
    
	public static List<File> scannPage(String baseDir, String fileName){
	    List<File> fileList = new ArrayList<File>();
        findFiles(baseDir, fileName,fileList);    
        if (fileList.size() == 0) {     
            log.info("No File Fount.");     
        } 
        return fileList;
	}

    public static void findFiles(String baseDirName, String targetFileName, List<File> fileList) {     
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            log.info("Can't find file,'"+baseDir+"' not is a directory");  
        }  
        String tempName = null;          
        File tempFile;  
        File[] files = baseDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            tempFile = files[i];  
            if(tempFile.isDirectory()){  
                findFiles(tempFile.getAbsolutePath(), targetFileName, fileList);  
            }else if(tempFile.isFile()){  
                tempName = tempFile.getName();  
                if(wildcardMatch(targetFileName, tempName)){  
                    fileList.add(tempFile.getAbsoluteFile());  
                }  
            }  
        }  
    }     
      
    private static boolean wildcardMatch(String pattern, String str) {     
        int patternLength = pattern.length();     
        int strLength = str.length();     
        int strIndex = 0;     
        char ch;     
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {     
            ch = pattern.charAt(patternIndex);     
            if (ch == '*') {          
                while (strIndex < strLength) {     
                    if (wildcardMatch(pattern.substring(patternIndex + 1),     
                            str.substring(strIndex))) {     
                        return true;     
                    }     
                    strIndex++;     
                }     
            } else if (ch == '?') {       
                strIndex++;     
                if (strIndex > strLength) {
                    return false;     
                }     
            } else {     
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {     
                    return false;     
                }     
                strIndex++;     
            }     
        }     
        return (strIndex == strLength);     
    }   
}  