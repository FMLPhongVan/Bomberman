package com.phongvan.bomberman.map;

import com.phongvan.bomberman.Bomberman;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class XMLConverter {
    public static String[] getXMLData(int level) {
        try {
            //File file = new File(XMLConverter.class.getResource("levels/level " + level + ".xml").toString());
            InputStream input = Bomberman.class.getResourceAsStream("levels/level " + level + ".tmx");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(input);
            doc.getDocumentElement().normalize();

            NodeList layer = doc.getElementsByTagName("layer");
            MapHandler.getInstance().setRows(Integer.parseInt(layer.item(0).getAttributes().getNamedItem("height").getNodeValue()));
            MapHandler.getInstance().setCols(Integer.parseInt(layer.item(0).getAttributes().getNamedItem("width").getNodeValue()));

            Node data = doc.getElementsByTagName("data").item(0);
            return data.getTextContent().split("\n");

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return null;
    }
}
