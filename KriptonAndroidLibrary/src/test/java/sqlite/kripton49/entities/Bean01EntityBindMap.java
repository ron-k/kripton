package sqlite.kripton49.entities;

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
 * This class is binder map for Bean01Entity
 *
 * @see Bean01Entity
 */
@BindMap(Bean01Entity.class)
public class Bean01EntityBindMap extends AbstractMapper<Bean01Entity> {
  @Override
  public int serializeOnJackson(Bean01Entity object, JsonGenerator jacksonSerializer) throws
      Exception {
    jacksonSerializer.writeStartObject();
    int fieldCount=0;

    // Serialized Field:

    // field id (mapped with "id")
    if (object.getId()!=null)  {
      fieldCount++;
      jacksonSerializer.writeNumberField("id", object.getId());
    }

    // field text (mapped with "text")
    if (object.getText()!=null)  {
      fieldCount++;
      jacksonSerializer.writeStringField("text", object.getText());
    }

    jacksonSerializer.writeEndObject();
    return fieldCount;
  }

  @Override
  public int serializeOnJacksonAsString(Bean01Entity object, JsonGenerator jacksonSerializer) throws
      Exception {
    jacksonSerializer.writeStartObject();
    int fieldCount=0;

    // Serialized Field:

    // field id (mapped with "id")
    if (object.getId()!=null)  {
      jacksonSerializer.writeStringField("id", PrimitiveUtils.writeLong(object.getId()));
    }

    // field text (mapped with "text")
    if (object.getText()!=null)  {
      fieldCount++;
      jacksonSerializer.writeStringField("text", object.getText());
    }

    jacksonSerializer.writeEndObject();
    return fieldCount;
  }

  /**
   * method for xml serialization
   */
  @Override
  public void serializeOnXml(Bean01Entity object, XMLSerializer xmlSerializer, int currentEventType)
      throws Exception {
    if (currentEventType == 0) {
      xmlSerializer.writeStartElement("bean01Entity");
    }

    // Persisted fields:

    // field id (mapped with "id")
    if (object.getId()!=null)  {
      xmlSerializer.writeStartElement("id");
      xmlSerializer.writeLong(object.getId());
      xmlSerializer.writeEndElement();
    }

    // field text (mapped with "text")
    if (object.getText()!=null) {
      xmlSerializer.writeStartElement("text");
      xmlSerializer.writeCharacters(StringEscapeUtils.escapeXml10(object.getText()));
      xmlSerializer.writeEndElement();
    }

    if (currentEventType == 0) {
      xmlSerializer.writeEndElement();
    }
  }

  /**
   * parse with jackson
   */
  @Override
  public Bean01Entity parseOnJackson(JsonParser jacksonParser) throws Exception {
    Bean01Entity instance = new Bean01Entity();
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
          case "id":
            // field id (mapped with "id")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.setId(jacksonParser.getLongValue());
            }
          break;
          case "text":
            // field text (mapped with "text")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.setText(jacksonParser.getText());
            }
          break;
          default:
            jacksonParser.skipChildren();
          break;}
    }
    return instance;
  }

  /**
   * parse with jackson
   */
  @Override
  public Bean01Entity parseOnJacksonAsString(JsonParser jacksonParser) throws Exception {
    Bean01Entity instance = new Bean01Entity();
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
          case "id":
            // field id (mapped with "id")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.setId(PrimitiveUtils.readLong(jacksonParser.getText(), null));
            }
          break;
          case "text":
            // field text (mapped with "text")
            if (jacksonParser.currentToken()!=JsonToken.VALUE_NULL) {
              instance.setText(jacksonParser.getText());
            }
          break;
          default:
            jacksonParser.skipChildren();
          break;}
    }
    return instance;
  }

  /**
   * parse xml
   */
  @Override
  public Bean01Entity parseOnXml(XMLParser xmlParser, int currentEventType) throws Exception {
    Bean01Entity instance = new Bean01Entity();
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
                case "id":
                  // property id (mapped on "id")
                  instance.setId(PrimitiveUtils.readLong(xmlParser.getElementAsLong(), null));
                break;
                case "text":
                  // property text (mapped on "text")
                  instance.setText(StringEscapeUtils.unescapeXml(xmlParser.getElementText()));
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
