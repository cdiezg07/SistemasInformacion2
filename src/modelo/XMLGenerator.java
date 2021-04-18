/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XMLGenerator {

    static String name;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    DOMImplementation implementation;
    static Document document;
    Element raiz;
    String nomArchivo;

    public XMLGenerator(String _name) throws Exception {
        name = _name;
        /*key = new ArrayList<String>();
        value = new ArrayList<String>();*/
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        implementation = builder.getDOMImplementation();
        document = implementation.createDocument(null, name, null);
        document.setXmlVersion("1.0");
        raiz = document.getDocumentElement();
    }

    public XMLGenerator(String _name, String nomArchivo) throws Exception {
        name = _name;
        this.nomArchivo = nomArchivo;
        /*key = new ArrayList<String>();
        value = new ArrayList<String>();*/
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        implementation = builder.getDOMImplementation();
        document = implementation.createDocument(null, name, null);
        document.setXmlVersion("1.0");
        raiz = document.getDocumentElement();
    }

    public void addItem(String s, ArrayList<String> sk, ArrayList<String> sV) {
        Element itemNode = document.createElement(s);
        for (int i = 0; i < sk.size(); i++) {
            //Item Node

            //Key Node
            Element keyNode = document.createElement(sk.get(i));
            Text nodeKeyValue = document.createTextNode(sV.get(i));
            keyNode.appendChild(nodeKeyValue);
            //append keyNode and valueNode to itemNode
            itemNode.appendChild(keyNode);
        }
        //pegamos el elemento a la raiz "Documento"
        raiz.appendChild(itemNode);
    }

    public void addItem(String s, ArrayList<String> sk, ArrayList<String> sV, String atribute) {
        Element itemNode = document.createElement(s);
        itemNode.setAttribute("id", atribute);
        for (int i = 0; i < sk.size(); i++) {
            //Item Node

            //Key Node
            Element keyNode = document.createElement(sk.get(i));
            Text nodeKeyValue = document.createTextNode(sV.get(i));
            keyNode.appendChild(nodeKeyValue);
            //append keyNode and valueNode to itemNode
            itemNode.appendChild(keyNode);
        }
        //pegamos el elemento a la raiz "Documento"
        raiz.appendChild(itemNode);
    }

    public void generate() throws Exception {
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        Result result = new StreamResult(new java.io.File("./resources/" + this.nomArchivo + ".xml")); //nombre del archivo
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }
}
