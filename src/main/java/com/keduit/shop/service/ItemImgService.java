package com.keduit.shop.service;

import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    //application properties에 등록한 itemImgLocation의 값을 가져와서 itemImgLocation 변수에 넣음.
    @Value("${itemImgLocation}")
    private String itemImgLocation;


    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception{
        String oriImgName=multipartFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";
        //파일 업로드
        //등록 여부를 확인 후 fileservice.uploadfiles()를 호출
        //이 때 저장할 경로와 파일의 이름, 이미지 파일의 바이트 배열을 파라미터로 전달.
        if(!StringUtils.isEmpty(oriImgName)){
            imgName=fileService.uploadFile(itemImgLocation, oriImgName, multipartFile.getBytes());
            imgUrl="/images/item/"+imgName;

            //webMveconfig 클래스에서 "/images/**로 설정했으며, uploadPath프로퍼티 경로인
            // "F:/shop/"아래 item폴더에 이미지를 저장하므로 상품이미지 경로는 "images/item/"을 붙인다.
        }
        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile)throws Exception{
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg=itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }

            String oriImgName=itemImgFile.getOriginalFilename();
            String imgName=fileService.uploadFile(itemImgLocation, oriImgName,itemImgFile.getBytes());
            String imgUrl="/images/item/"+imgName;
            savedItemImg.updateItemImg(oriImgName,imgName, imgUrl);
        }
    }

}
