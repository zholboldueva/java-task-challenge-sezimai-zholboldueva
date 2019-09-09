import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataEvaluatingClass {

    public static void main(String args[]) {
        ArrayList<Data> l_dataList = new ArrayList<>();
        ArrayList<Operation> l_operationList = new ArrayList<>();
        readDataXmlFile(l_dataList);
        readOperationsXmlFile(l_operationList);
        createOutputData(l_dataList, l_operationList);
    }

    private static void createOutputData(ArrayList<Data> p_dataList, ArrayList<Operation> p_operationList) {
        try {
            DocumentBuilderFactory l_documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder l_documentBuilder = l_documentFactory.newDocumentBuilder();
            Document l_document = l_documentBuilder.newDocument();
            // root element
            Element l_rootElement = l_document.createElement("results");
            l_document.appendChild(l_rootElement);
            List<Result> l_resultList = getResult(p_dataList, p_operationList);

            for (Result p_result : l_resultList) {
                Element l_result = l_document.createElement("result");
                l_rootElement.appendChild(l_result);

                Attr l_attr = l_document.createAttribute("name");
                l_attr.setValue(p_result.getName());
                l_result.setAttributeNode(l_attr);
                l_result.setTextContent(p_result.getText());
            }

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory l_transformerFactory = TransformerFactory.newInstance();
            Transformer l_transformer = l_transformerFactory.newTransformer();
            DOMSource l_domSource = new DOMSource(l_document);
            StreamResult l_streamResult = new StreamResult(new File("OutputXml.xml"));

            l_transformer.transform(l_domSource, l_streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException p_pce) {
            p_pce.printStackTrace();
        } catch (TransformerException p_tfe) {
            p_tfe.printStackTrace();
        }
    }


    public static void readDataXmlFile(ArrayList<Data> p_dataList) {

        try {
            Document doc = getDocument("data.xml");
            NodeList nList = doc.getElementsByTagName("city");
            for (int i = 0; i < nList.getLength(); i++) {
                Data l_newData = new Data(nList.toString());
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    l_newData.setName(eElement.getAttribute("name"));
                    l_newData.setPopulation(eElement.getAttribute("population"));
                    if (eElement.hasChildNodes()) {
                        l_newData.setArea(eElement.getElementsByTagName("area").item(0).getTextContent());
                    }
                }
                p_dataList.add(l_newData);
            }
        } catch (Exception p_e) {
            p_e.printStackTrace();
        }
    }



    public static void readOperationsXmlFile(ArrayList<Operation> p_operationList) {
        try {

            Document l_doc = getDocument("operations.xml");
            NodeList nList = l_doc.getElementsByTagName("operation");
            for (int i = 0; i < nList.getLength(); i++) {
                Operation l_newData = new Operation(nList.toString());
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    l_newData.setName(eElement.getAttribute("name"));
                    l_newData.setType(eElement.getAttribute("type"));
                    l_newData.setFunc(eElement.getAttribute("func"));
                    l_newData.setAttrib(eElement.getAttribute("attrib"));
                    l_newData.setFilter(eElement.getAttribute("filter"));
                }
                p_operationList.add(l_newData);
            }
        } catch (Exception p_e) {
            p_e.printStackTrace();
        }
    }

    public static List<Result> getResult(List<Data> p_dataList, List<Operation> p_operationList) {
        List<Result> l_resultList = new ArrayList<>();
        Map<Operation, ArrayList<String>> l_dataMap = new HashMap<>();
        filter(p_dataList, p_operationList, l_dataMap);
        for (Map.Entry<Operation, ArrayList<String>> entry : l_dataMap.entrySet()) {
            Result l_res = new Result();
            l_res.setName(entry.getKey().getName());
            l_res.setText(String.format("%.2f", function(entry.getKey(), entry.getValue())));
            l_resultList.add(l_res);
        }
        return l_resultList;
    }

    private static Document getDocument(String s) throws ParserConfigurationException, SAXException, IOException {
        File l_fXmlFile = new File(s);
        DocumentBuilderFactory l_dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder l_dBuilder = l_dbFactory.newDocumentBuilder();
        Document doc = l_dBuilder.parse(l_fXmlFile);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static void filter(List<Data> p_dataList, List<Operation> p_operationList, Map<Operation, ArrayList<String>> p_dataMap) {
        for (Operation l_operation : p_operationList) {
            ArrayList<String> l_list = new ArrayList<>();
            String l_filter = l_operation.getFilter();
            Pattern l_pattern = Pattern.compile(l_filter);
            for (Data data : p_dataList) {
                Matcher l_matcher = l_pattern.matcher(data.getName());
                if (l_matcher.matches()) {
                    if (l_operation.getType().equals("attrib")) {
                        l_list.add(data.getPopulation());
                    } else if (l_operation.getType().equals("sub")) {
                        l_list.add(data.getArea());
                    }
                }
            }
            p_dataMap.put(l_operation, l_list);
        }
    }

    private static double function(Operation p_operation, ArrayList<String> p_dataList) {
        double[] p_doubleList = new double[p_dataList.size()];
        for (int i = 0; i < p_dataList.size(); ++i) {
            p_doubleList[i] = Double.parseDouble(p_dataList.get(i));
        }
        double k = 0.00;
        if (p_operation.getFunc().equals("average"))
            k = Arrays.stream(p_doubleList).average().getAsDouble();
        else if (p_operation.getFunc().equals("sum"))
            k = Arrays.stream(p_doubleList).sum();
        else if (p_operation.getFunc().equals("min")) {
            k = Arrays.stream(p_doubleList).min().getAsDouble();
        } else if (p_operation.getFunc().equals("max"))
            k = Arrays.stream(p_doubleList).max().getAsDouble();
        return k;
    }


}
