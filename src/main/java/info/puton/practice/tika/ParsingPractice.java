package info.puton.practice.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by taoyang on 2016/9/18.
 */
public class ParsingPractice {

    String filePath = "D:/programming/java/practice/tika-practice/src/main/resources/test.docx";

    public String parseToPlainText() {
        ContentHandler handler = new BodyContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            File file =  new File(filePath);
            InputStream stream = TikaInputStream.get(file.toPath());
            parser.parse(stream, handler, metadata);

            for (String name : metadata.names()) {
                System.out.println(name + ":" + metadata.get(name));
            }

            System.out.println(handler.toString());

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.toString();
    }

    public static void main(String[] args) {

        ParsingPractice parsingPractice = new ParsingPractice();

        parsingPractice.parseToPlainText();
    }

}
