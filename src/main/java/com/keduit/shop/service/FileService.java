package com.keduit.shop.service;


import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
//파일 업로드와 삭제 처리를 담당하는 fileservice작성
@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)throws Exception{
            //universally unique identifier:범용 고유 식별자를 의미하며 중복이 되지 않는 유일한 값을 구성하고자 할 때 사용

        //랜덤을 주는 아이
        UUID uuid=UUID.randomUUID();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName=uuid.toString()+extension;
        String fileUploadFullUrl=uploadPath+"/"+saveFileName;
        //파일은 이미지 파일이니까 바이트 처리
        FileOutputStream fos=new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return saveFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        // 저장된 파일의 경로를 이용하여 파일객체를 생성.
        File deleteFile=new File(filePath);
        //해당 파일이 있으면 삭제
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }

    }
}
