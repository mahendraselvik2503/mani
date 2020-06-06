package net.nwie.awdtool;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ModifyXMLDOM {

    public static void main(String[] args) throws Exception {
        String filePath = "lob.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            //update attribute value
            updateAttributeValue(doc);
           
            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("lob_updated.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
            
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }
    }
    
  

    
    private static void updateAttributeValue(Document doc) throws Exception {
        NodeList nodes = doc.getElementsByTagName("AWD");
        Element element = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        //loop for each employee
       
            for (int i = 0; i < nodes.getLength(); i++) {
                element = (Element) nodes.item(i);
                NodeList cdataParent = element.getElementsByTagName("TASK");
                Element line = (Element) cdataParent.item(0);
                String task =getCdata(line);
                System.out.println("TASK: " + task);
                DocumentBuilder cdataBuilder = factory.newDocumentBuilder();

              //Use method to convert XML string content to XML Document object
              
                Document cdataDoc = cdataBuilder.parse(new InputSource(new StringReader(
                  getCdata(cdataParent.item(0))
                )));
                Node businessAreaName = cdataDoc.getElementsByTagName("businessAreaName").item(0);
                System.out.println("Content of businessAreaName in CDATA: " + getCdata(businessAreaName));
                
                NodeList iParent = cdataDoc.getElementsByTagName("fieldValues");
                System.out.println("iParent: " + iParent.getLength());
                
                for (int j = 0; j < iParent.getLength(); j++) {
                   // element = (Element) nodes.item(i);
                  cdataDoc = cdataBuilder.parse(new InputSource(new StringReader(
                            getCdata(cdataParent.item(0))
                          )));
                    NodeList insParent = cdataDoc.getElementsByTagName("filedValue");
                    
                    System.out.println("createInstance: " + insParent.item(0).getNodeName());
                    
                    for (int k= 0; k < insParent.getLength(); k++) {
                        element = (Element) nodes.item(i);
                      cdataDoc = cdataBuilder.parse(new InputSource(new StringReader(
                                getCdata(cdataParent.item(0))
                              )));
                         insParent = cdataDoc.getElementsByTagName("value");
                         System.out.println("value: " +insParent.item(0).getFirstChild().getNodeValue());
                        String output =  getCdata(insParent.item(0));
                        System.out.println("createInstance: " +output);
                         if (output.length()>2){
                             System.out.println("createInstance: " +output.substring(0,2));
                             insParent.item(0).getFirstChild().setNodeValue(output.substring(0,2));
                             System.out.println("value after: " +insParent.item(0).getFirstChild().getNodeValue());
                         }
                   
                }

                }
              }
        }
    
    public static String getCdata(Node parent) {
        NodeList cs = parent.getChildNodes();
        for(int i = 0; i < cs.getLength(); i++){
            Node c = cs.item(i);
            if(c instanceof CharacterData) {
                CharacterData cdata = (CharacterData)c;
                String content = cdata.getData().trim();
                if (content.length() > 0) {
                  return content;
                }
            }
        }
        return "";
    }



   

}


