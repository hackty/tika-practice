package info.puton.practice.tika;

import info.puton.practice.tika.model.FileFullText;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by taoyang on 2016/9/18.
 */
public class FileExtractPractice {

    public FileFullText extract(File file) {
        FileFullText fileFullText = new FileFullText();
        String fileKey = UUID.randomUUID().toString();
        fileFullText.setFileKey(fileKey);//fileKey
        fileFullText.setFileName(file.getName());//fileName
        ContentHandler handler = new BodyContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            InputStream stream = TikaInputStream.get(file.toPath());
            parser.parse(stream, handler, metadata);
//            for (String name : metadata.names()) {
//                System.out.println(name + ":" + metadata.get(name));
//            }
            String author = metadata.get("Author");//author
            fileFullText.setAuthor(author);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = metadata.getDate(Property.get("Last-Modified"));
            String lastModified = sdf.format(date);
            fileFullText.setModifyDate(lastModified);//modifyDate
            String content = handler.toString();
            fileFullText.setContent(content);//content
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileFullText;
    }

    public static void main(String[] args) {

        String filePath = "D:/programming/java/practice/tika-practice/src/main/resources/一碗阳春面.docx";

        FileExtractPractice extractPractice = new FileExtractPractice();

        FileFullText fileFullText = extractPractice.extract(new File(filePath));

        System.out.println(fileFullText);

    }

}
