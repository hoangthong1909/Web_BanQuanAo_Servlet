package JPAUtils;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FileUtil {

    public static File saveFileUpload(String nameFolder, Part part) {
        File folderUpload = new File("/PH15021_HoangTrungThong_31032002/src/main/webapp/images/" + nameFolder);
        if (!folderUpload.exists()) {
            folderUpload.mkdirs();
        }

        File file = new File(folderUpload, part.getSubmittedFileName());
//        System.out.println(file.getAbsolutePath());
        try {
            if (file.exists()){
            part.write(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}