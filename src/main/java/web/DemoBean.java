package web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ManagedBean
@SessionScoped
public class DemoBean {

    private Part file1;
    private String fileName;
    private long fileSize;
    private Date date;
    private String fileDate;

    private Pdf newPdf = new Pdf();
    private String description;
    private int flag, flag2;
    private String info;

    private ArrayList<Pdf> list = new ArrayList<>();
    private ArrayList<Long> listSize = new ArrayList<>();

    @EJB
    private PdfManager pdfManager;

    public String upload() throws IOException {
        fileName = getFileName(file1);
        getSize();
        checkSize();
        create();
        flag = pdfManager.getFlag();

        if(flag == 0 & flag2 == 0) {
            file1.write("C:\\Users\\HP\\Desktop\\Data\\" + getFileName(file1));
        }

        sendInfo();
        return "success";
    }




    public void checkSize(){

        flag2 = 0;
        flag = 0;

        for(int i = 0; i < listSize.size(); i++){

            if(fileSize == listSize.get(i)){
                flag2 = 1;
            }
        }
        pdfManager.setFlag2(flag2);
    }


    public void safePdf(String fileName2){

        info = "Plik " + fileName2 + " pojawie sie w pliku 'Saved' na pulpicie";

        try(FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Desktop\\Data\\" + fileName2);
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\HP\\Desktop\\Saved\\" + fileName2)){

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer, 0, buffer.length);
            outputStream.write(buffer, 0, buffer.length);

        }catch (IOException e){
            e.printStackTrace();
        }
    }



    private static String getFileName(Part part) {

        for (String cd : part.getHeader("content-disposition").split(";")) {

            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }


    public void create(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        fileDate = dateFormat.format(date);

        newPdf.setId(fileName);
        newPdf.setSize(fileSize);
        newPdf.setDescription(description);
        newPdf.setDate(fileDate);

        PdfEntity pdfEntity = new PdfEntity();
        pdfEntity.fromDto(newPdf);
        pdfManager.create(pdfEntity);

        list.add(newPdf);

        newPdf = new Pdf();
    }


    public List<Pdf> getProducts() {

        List<Pdf> result = new ArrayList<>();
        List<PdfEntity> entities = pdfManager.readList(0,100);
        listSize.clear();

        for(PdfEntity productEntity: entities){

            result.add(productEntity.toDto());
            listSize.add(productEntity.getSize());
        }
        return result;
    }


    public void sendInfo(){

        if(flag2 == 0){
            info = "Objekt " + fileName + " zostal dodany";
        }
        else    info = "Objekt " + fileName + " albo obiekt o identycznym rozmiarze juz istnieje";

    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void deleteProduct(String pdfId){
        pdfManager.delete(pdfId);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void getSize(){
        fileSize = file1.getSize()/1024;
    }

    public Part getFile1() {
        return file1;
    }

    public void setFile1(Part file1) {
        this.file1 = file1;
    }
}
