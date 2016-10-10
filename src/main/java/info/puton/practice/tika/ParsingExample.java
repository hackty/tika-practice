package info.puton.practice.tika;

import org.apache.tika.Tika;
import org.apache.tika.detect.EncodingDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.txt.*;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.apache.tika.sax.ToXMLContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.apache.tika.sax.xpath.Matcher;
import org.apache.tika.sax.xpath.MatchingContentHandler;
import org.apache.tika.sax.xpath.XPathParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyang on 2016/9/14.
 */
public class ParsingExample {

//    String filePath = "D:/programming/java/practice/tika-practice/src/main/resources/一碗阳春面.docx";
//    String filePath = "D:/programming/java/product/smartsearch/src/test/resources/徽商准备工作.txt";
    String filePath = "C:/Users/taoyang/Desktop/cmcc测试.txt";
//    String filePath = "C:/Users/taoyang/Desktop/徽商准备工作3.txt";

    public String parseTXT(){

        Parser parser = new TXTParser();
        InputStream input = null;
        try{
            Metadata metadata = new Metadata();
            input = new FileInputStream(filePath);
            ContentHandler handler = new BodyContentHandler();//当文件大于100000时，new BodyContentHandler(1024*1024*10);
            ParseContext context = new ParseContext();
            context.set(Parser.class,parser);
            parser.parse(input,handler, metadata,context);
            for(String name:metadata.names()) {
                System.out.println(name+":"+metadata.get(name));
            }
            System.out.println(handler.toString());
            return handler.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(input!=null)input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * 获取编码
     * @throws IOException
     * @throws Exception
     */
    public static String getEncode(byte[] data,String url){
        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();
        String encoding = match.getName();
        System.out.println("The Content in " + match.getName());
        CharsetMatch[] matches = detector.detectAll();
        System.out.println("All possibilities");
        for (CharsetMatch m : matches) {
            //System.out.println("CharsetName:" + m.getName() + " Confidence:"+ m.getConfidence());
        }
        return encoding;
    }
    public static String getEncode(InputStream data,String url) throws IOException{
        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();
        String encoding = match.getName();
        System.out.println("The Content in " + match.getName());

        CharsetMatch[] matches = detector.detectAll();
        System.out.println("All possibilities");
        for (CharsetMatch m : matches) {
            // System.out.println("CharsetName:" + m.getName() + " Confidence:"+ m.getConfidence());
        }
        return encoding;
    }

    public String parseByFacade() {
        Tika tika = new Tika();
        try {
            InputStream stream = new FileInputStream(filePath);
            return tika.parseToString(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String parseByAutoDetect() {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try {
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, handler, metadata);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.toString();
    }

    public String parseToPlainText() {
        BodyContentHandler handler = new BodyContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, handler, metadata);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.toString();
    }


    public String parseToHTML() {
        ContentHandler handler = new ToXMLContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, handler, metadata);
        }  catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.toString();
    }

    public String parseBodyToHTML() {
        ContentHandler handler = new BodyContentHandler(
                new ToXMLContentHandler());
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, handler, metadata);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.toString();
    }

    public String parseOnePartToHTML() {
        // Only get things under html -> body -> div (class=header)
        XPathParser xhtmlParser = new XPathParser("xhtml", XHTMLContentHandler.XHTML);
        Matcher divContentMatcher = xhtmlParser.parse("/xhtml:html/xhtml:body/xhtml:div/descendant::node()");
        ContentHandler handler = new MatchingContentHandler(
                new ToXMLContentHandler(), divContentMatcher);
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try {
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, handler, metadata);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.toString();
    }

    public List<String> parseToPlainTextChunks() {
        final List<String> chunks = new ArrayList<String>();
        final int MAXIMUM_TEXT_CHUNK_SIZE = 100;
        chunks.add("");
        ContentHandlerDecorator handler = new ContentHandlerDecorator() {
            @Override
            public void characters(char[] ch, int start, int length) {
                System.out.println("ch:"+ch);
                System.out.println("start:"+start);
                System.out.println("length:"+length);
                String lastChunk = chunks.get(chunks.size() - 1);
                System.out.println("lastChunk:"+lastChunk);
                String thisStr = new String(ch, start, length);
                System.out.println("thisStr:"+thisStr);

                if (lastChunk.length() + length > MAXIMUM_TEXT_CHUNK_SIZE) {
                    System.out.println("add:"+thisStr);
                    chunks.add(thisStr);
                } else {
                    System.out.println("else:"+thisStr);
                    chunks.set(chunks.size() - 1, lastChunk + thisStr);
                }
            }
        };

        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();

        try {
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, handler, metadata);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chunks;
    }

    public static void main(String[] args) {

        ParsingExample example = new ParsingExample();

        System.out.println(example.parseTXT());

//        System.out.println(example.parseByFacade());

//        System.out.println(example.parseByAutoDetect());

//        System.out.println(example.parseToPlainText());

//        System.out.println(example.parseToHTML());

//        System.out.println(example.parseBodyToHTML());

//        System.out.println(example.parseOnePartToHTML());

//        System.out.println(example.parseToPlainTextChunks().size());

    }

}
