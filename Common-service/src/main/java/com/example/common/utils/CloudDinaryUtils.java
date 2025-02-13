package com.example.common.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.common.enums.ErrorCode;
import com.example.common.exception.AppException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URL;
import java.util.Map;



@Component
@AllArgsConstructor
public class CloudDinaryUtils {

    private final Cloudinary cloudinary;

    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "book-store" // Tên thư mục trên Cloudinary
            );
            Map data = this.cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return (String) data.get("secure_url");
        } catch (IOException io) {
            throw new AppException(ErrorCode.UploadImageFailed);
        }
    }

    public void deleteImage(String imgUrl) {
        String publicId = extractPublicId(imgUrl);
        System.out.println(publicId);
        try {
            cloudinary.uploader().destroy(publicId,  Map.of());

        } catch (Exception e) {
            throw new AppException(ErrorCode.DeleteImageFailed);
        }
    }

    public static String extractPublicId(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            String path = url.getPath(); // Lấy đường dẫn từ URL
            String publicIdWithExtension = path.substring(path.indexOf("book-store/") + 13); // Bỏ đoạn "image/upload/"
            return publicIdWithExtension.replaceFirst("\\.[^.]+$", ""); // Loại bỏ phần mở rộng
        } catch (Exception e) {
            throw new AppException(ErrorCode.InvalidUrlImage);
        }
    }
}
