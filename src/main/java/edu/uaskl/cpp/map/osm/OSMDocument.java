package edu.uaskl.cpp.map.osm;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.uaskl.cpp.model.graph.interfaces.*;
import edu.uaskl.cpp.model.graph.*;
import edu.uaskl.cpp.model.edge.*;
import edu.uaskl.cpp.model.node.*;
import edu.uaskl.cpp.map.meta.*;

public class OSMDocument implements GraphCreator<NodeCpp<Way>, EdgeCpp<Way>> {
	private HashMap<Long, WayNodeOSM> nodesById = new HashMap<Long, WayNodeOSM>();
	private ArrayList<Way> ways = new ArrayList<Way>();

	private class OSMHandler extends DefaultHandler {
		private Way currWay = null;

		@Override
		// FIXME: Requires all nodes to be listed before ways.
		public void startElement(String uri, String localName, String qName,
		                          Attributes attributes) throws SAXException {
			WayNodeOSM node;
			try {
				switch(localName) {
					// FIXME: Parse this correctly.
					case "node":
						node = new WayNodeOSM(
							Double.parseDouble(attributes.getValue(uri, "longitude")),
							Double.parseDouble(attributes.getValue(uri, "latitude")),
							Long.parseLong(attributes.getValue(uri, "id")),
							new Date(),
							Long.parseLong(attributes.getValue(uri, "changeset")));
						nodesById.put(node.getId(), node);
						break;
					case "way":
						currWay = new Way();
						ways.add(currWay);
						break;
					case "nd":
						if(currWay == null)
							throw new RuntimeException("Node reference outside of way in OSM document.");
						else {
							node = nodesById.get(
								Long.parseLong(attributes.getValue(uri, "id")));
							currWay.getNodes().add(node);
						}
						break;
				}
			// TODO: Be more specific about these exceptions.
			} catch(NullPointerException e) {
		    	throw new RuntimeException("Required attribute not found in OSM node or way.", e);
		    } catch(NumberFormatException e) {
		    	throw new RuntimeException("Invalid number format in OSM node.", e);
		    }
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			switch(localName) {
				case "way":
					currWay = null;
					break;
			}
		}
	}

	private static final String schemaPath = "examples/osm-0.6-ssis.xsd";

	private static SAXParser parser = null;

	private String uri;
	
	private void configureParser() {
		if(parser == null) {
			Schema schema;
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();

			try {
				SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				schema = schemaFactory.newSchema(new File(schemaPath));
			} catch(IllegalArgumentException e) {
				throw new RuntimeException("Error creating XML schema.", e);
			} catch(SAXException e) {
				throw new RuntimeException("Error parsing XML schema.", e);
			} catch(NullPointerException e) {
				throw new RuntimeException("Error opening XML schema file.", e);
			}
	
			try {
				parserFactory.setSchema(schema);
			} catch(FactoryConfigurationError e) {
				throw new RuntimeException("Error creating XML parser factory.", e);
			}

			try {
				parser = parserFactory.newSAXParser();
			} catch(ParserConfigurationException e) {
				throw new RuntimeException("Error creating XML parser.", e);
			} catch(SAXException e) {
				throw new RuntimeException("Error creating XML parser.", e);
			}
		}
	}

	@Override
	// TODO: Actually create the graph.
	public GraphUndirected<Way> create() {return null;}
	
	public void parse() throws IOException {
		try {
			parser.parse(uri, new OSMHandler());
		} catch(IOException e) {
			throw new IOException("I/O error parsing OSM document.", e);
		} catch(SAXException e) {
			throw new RuntimeException("Error parsing OSM document.", e);
		}
	}

	public OSMDocument(String uri) {
		this.uri = uri;
		configureParser();
	}
}
