package sqlite.foreignKey;

import com.abubusoft.kripton.AbstractMapper;
import com.abubusoft.kripton.annotation.BindMap;
import com.abubusoft.kripton.common.PrimitiveUtils;
import com.abubusoft.kripton.escape.StringEscapeUtils;
import com.abubusoft.kripton.xml.XMLParser;
import com.abubusoft.kripton.xml.XMLSerializer;
import com.abubusoft.kripton.xml.XmlPullParser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.lang.Exception;
import java.lang.Override;

/**
 * This class is the shared preference binder defined for BeanA_6
 *
 * @see BeanA_6
 */
@BindMap(BeanA_6.class)
public class BeanA_6BindMap extends AbstractMapper<BeanA_6> {
  /**
   * reset shared preferences
   */
  @Override
  public int serializeOnJackson(BeanA_6 object, JsonGenerator jacksonSerializer) throws Exception {
    jacksonSerializer.writeStartObject();
    int fieldCount=0;

    // Serialized Field:

    // field beanA2Id (mapped with "beanA2Id")
    if (object.beanA2Id!=null)  {
      fieldCount++;
      jacksonSerializer.writeNumberField("beanA2Id", object.beanA2Id);
    }

    // field id (mapped with "id")
    fieldCount++;
    jacksonSerializer.writeNumberField("id", object.id);

    // field valueString2 (mapped with "valueString2")
    if (object.valueString2!=null)  {
      fieldCount++;
      jacksonSerializer.writeStringField("valueString2", object.valueString2);
    }

    jacksonSerializer.writeEndObject();
    return fieldCount;
  }

  /**
   * reset shared preferences
   */
  @Override
  public int serializeOnJacksonAsString(BeanA_6 object, JsonGenerator jacksonSerializer) throws Exception {
    jacksonSerializer.writeStartObject();
    int fieldCount=0;

    // Serialized Field:

    // field beanA2Id (mapped with "beanA2Id")
    if (object.beanA2Id!=null)  {
      jacksonSerializer.writeStringField("beanA2Id", PrimitiveUtils.writeLong(object.beanA2Id));
    }

    // field id (mapped with "id")
    jacksonSerializer.writeStringField("id", PrimitiveUtils.writeLong(object.id));

    // field valueString2 (mapped with "valueString2")
    if (object.valueString2!=null)  {
      fieldCount++;
      jacksonSerializer.writeStringField("valueString2", object.valueString2);
    }

    jacksonSerializer.writeEndObject();
    return fieldCount;
  }

  /**
   * reset shared preferences
   */
  @Override
  public void serializeOnXml(BeanA_6 object, XMLSerializer xmlSerializer, int currentEventType) throws Exception {
    if (currentEventType == 0) {
      xmlSerializer.writeStartElement("beanA_6");
    }

    // Persisted fields:

    // field beanA2Id (mapped with "beanA2Id")
    if (object.beanA2Id!=null)  {
      xmlSerializer.writeStartElement("beanA2Id");
      xmlSerializer.writeLong(object.beanA2Id);
      xmlSerializer.writeEndElement();
    }

    // field id (mapped with "id")
    xmlSerializer.writeStartElement("id");
    xmlSerializer.writeLong(object.id);
    xmlSerializer.writeEndElement();

    // field valueString2 (mapped with "valueString2")
    if (object.valueString2!=null) {
      xmlSerializer.writeStartElement("valueString2");
      xmlSerializer.writeCharacters(StringEscapeUtils.escapeXml10(object.valueString2));
      xmlSerializer.writeEndElement();
    }

    if (currentEventType == 0) {
      xmlSerializer.writeEndElement();
    }
  }

  /**
   * create new object instance
   */
  @Override
  public BeanA_6 parseOnJackson(JsonParser jacksonParser) throws Exception {
    BeanA_6 instance = new BeanA_6();
    String fieldName;
    if (jacksonParser.currentToken() == null) {
      jacksonParser.nextToken();
    }
    if (jacksonParser.currentToken() != JsonToken.START_OBJECT) {
      jacksonParser.skipChildren();
      return instance;
    }
    while (jacksonParser.nextToken() != JsonToken.END_OBJECT) {
      fieldName = jacksonParser.getCurrentName();
      jacksonParser.nextToken();

      // Parse fields:
      switch (fieldName) {
          case "beanA2Id":
            // field beanA2Id (mapped with "beanA2Id")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.beanA2Id=jacksonParser.getLongValue();
            }
          break;
          case "id":
            // field id (mapped with "id")
            instance.id=jacksonParser.getLongValue();
          break;
          case "valueString2":
            // field valueString2 (mapped with "valueString2")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.valueString2=jacksonParser.getText();
            }
          break;
          default:
            jacksonParser.skipChildren();
          break;}
    }
    return instance;
  }

  /**
   * create new object instance
   */
  @Override
  public BeanA_6 parseOnJacksonAsString(JsonParser jacksonParser) throws Exception {
    BeanA_6 instance = new BeanA_6();
    String fieldName;
    if (jacksonParser.getCurrentToken() == null) {
      jacksonParser.nextToken();
    }
    if (jacksonParser.getCurrentToken() != JsonToken.START_OBJECT) {
      jacksonParser.skipChildren();
      return instance;
    }
    while (jacksonParser.nextToken() != JsonToken.END_OBJECT) {
      fieldName = jacksonParser.getCurrentName();
      jacksonParser.nextToken();

      // Parse fields:
      switch (fieldName) {
          case "beanA2Id":
            // field beanA2Id (mapped with "beanA2Id")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.beanA2Id=PrimitiveUtils.readLong(jacksonParser.getText(), null);
            }
          break;
          case "id":
            // field id (mapped with "id")
            instance.id=PrimitiveUtils.readLong(jacksonParser.getText(), 0L);
          break;
          case "valueString2":
            // field valueString2 (mapped with "valueString2")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.valueString2=jacksonParser.getText();
            }
          break;
          default:
            jacksonParser.skipChildren();
          break;}
    }
    return instance;
  }

  /**
   * create new object instance
   */
  @Override
  public BeanA_6 parseOnXml(XMLParser xmlParser, int currentEventType) throws Exception {
    BeanA_6 instance = new BeanA_6();
    int eventType = currentEventType;
    boolean read=true;

    if (currentEventType == 0) {
      eventType = xmlParser.next();
    } else {
      eventType = xmlParser.getEventType();
    }
    String currentTag = xmlParser.getName().toString();
    String elementName = currentTag;
    // No attributes found

    //sub-elements
    while (xmlParser.hasNext() && elementName!=null) {
      if (read) {
        eventType = xmlParser.next();
      } else {
        eventType = xmlParser.getEventType();
      }
      read=true;
      switch(eventType) {
          case XmlPullParser.START_TAG:
            currentTag = xmlParser.getName().toString();
            switch(currentTag) {
                case "beanA2Id":
                  // property beanA2Id (mapped on "beanA2Id")
                  instance.beanA2Id=PrimitiveUtils.readLong(xmlParser.getElementAsLong(), null);
                break;
                case "id":
                  // property id (mapped on "id")
                  instance.id=PrimitiveUtils.readLong(xmlParser.getElementAsLong(), 0L);
                break;
                case "valueString2":
                  // property valueString2 (mapped on "valueString2")
                  instance.valueString2=StringEscapeUtils.unescapeXml(xmlParser.getElementText());
                break;
                default:
                break;
              }
            break;
            case XmlPullParser.END_TAG:
              if (elementName.equals(xmlParser.getName())) {
                currentTag = elementName;
                elementName = null;
              }
            break;
            case XmlPullParser.CDSECT:
            case XmlPullParser.TEXT:
              // no property is binded to VALUE o CDATA break;
            default:
            break;
        }
      }
      return instance;
    }
  }