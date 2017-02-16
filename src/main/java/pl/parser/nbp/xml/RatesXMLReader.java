package pl.parser.nbp.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import pl.parser.nbp.command.CommandLineOptions;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static pl.parser.nbp.xml.RatesXMLTags.TAG_BUY;
import static pl.parser.nbp.xml.RatesXMLTags.TAG_CURRENCY;
import static pl.parser.nbp.xml.RatesXMLTags.TAG_POSITION;
import static pl.parser.nbp.xml.RatesXMLTags.TAG_PUBLISH_DATE;
import static pl.parser.nbp.xml.RatesXMLTags.TAG_SELL;

/**
 * @author activey
 */
public class RatesXMLReader extends DefaultHandler {

    private static final String RATES_URL_TEMPLATE = "http://www.nbp.pl/kursy/xml/%s.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesXMLReader.class);

    private String ratesFileId;
    private CommandLineOptions options;
    private BiConsumer<Double, Double> sellBuyValueConsumer;

    private String currentTag;
    private String currentTagValue;

    private LocalDate publishDate;
    private String currencyCode;
    private double sellValue;
    private double buyValue;

    public RatesXMLReader(String ratesFileId, CommandLineOptions options,
                          BiConsumer<Double, Double> sellBuyValueConsumer) {
        this.ratesFileId = ratesFileId;
        this.options = options;
        this.sellBuyValueConsumer = sellBuyValueConsumer;
    }

    public void readRates() throws SAXException, IOException {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            URL ratesXmlUrl = getUrlForRates();
            saxParser.parse(ratesXmlUrl.openStream(), this);
        } catch (ParserConfigurationException e) {
            LOGGER.error("SAX parser configuration error occurred.", e);
        }
    }

    private URL getUrlForRates() throws MalformedURLException {
        return new URL(format(RATES_URL_TEMPLATE, ratesFileId));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // setting name of tag that is currently being processed
        currentTag = qName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String text = new String(ch, start, length).trim();
        if (text.length() > 0) {
            // reading current tag contents
            currentTagValue = text;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // reading publish date
        if (isCurrentTag(TAG_PUBLISH_DATE)) {
            readPublishDateFromCurrentTagValue();
        }
        // reading currency
        if (isCurrentTag(TAG_CURRENCY)) {
            readCurrencyCodeFromCurrentTagValue();
        }

        // reading sell and buy values
        if (isCurrentTag(TAG_SELL)) {
            readSellValueFromCurrentTagValue();
        }
        if (isCurrentTag(TAG_BUY)) {
            readBuyValueFromCurrentTagValue();
        }

        // finishing reading position and emitting values
        if (qName.equals(TAG_POSITION)) {
            finishReadingPosition();
        }
    }

    private void readPublishDateFromCurrentTagValue() {
        publishDate = LocalDate.parse(currentTagValue, ISO_DATE);
    }

    private void readCurrencyCodeFromCurrentTagValue() {
        currencyCode = currentTagValue;
    }

    private void readSellValueFromCurrentTagValue() {
        sellValue = readDouble(currentTagValue);
    }

    private void readBuyValueFromCurrentTagValue() {
        buyValue = readDouble(currentTagValue);
    }

    private double readDouble(String stringDoubleValue) {
        DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance();
        formatSymbols.setDecimalSeparator(',');

        DecimalFormat doubleFormatter = new DecimalFormat("#.####", formatSymbols);
        try {
            return doubleFormatter.parse(stringDoubleValue).doubleValue();
        } catch (ParseException e) {
            LOGGER.error("An error has occurred while parsing double value: {}.", stringDoubleValue, e);
            return 0;
        }
    }

    private void finishReadingPosition() {
        // checking if date is between given range from options object
        if (!publishDateMatches()) {
            return;
        }
        // checking currency match
        if (!currencyMatches()) {
            return;
        }
        // transferring values
        sellBuyValueConsumer.accept(sellValue, buyValue);
    }

    private boolean publishDateMatches() {
        return (publishDate.isEqual(options.getStartDate()) || publishDate.isAfter(options.getStartDate()))
                && (publishDate.isEqual(options.getEndDate()) || publishDate.isBefore(options.getEndDate()));
    }

    private boolean currencyMatches() {
        return currencyCode.equals(options.getCurrencyCode());
    }

    private boolean isCurrentTag(String tagName) {
        return currentTag.equals(tagName);
    }
}
