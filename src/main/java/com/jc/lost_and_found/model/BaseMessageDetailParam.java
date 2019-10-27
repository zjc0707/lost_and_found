package com.jc.lost_and_found.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zjc
 * @date 2019/10/24
 */
@Data
public class BaseMessageDetailParam {
    private String title;
    private String content;
    private List<MultipartFile> files;
}
