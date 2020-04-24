package com.ng.ui.study.anim;

/**
 * 描述:
 *
 * @author Jzn
 * @date 2020-04-24
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class MySaxHandler extends DefaultHandler {

    //声明一个装载PieAnimShape类型的List
    private List<PieAnimShape> mList;
    //声明一个PieAnimShape类型的变量
    private PieAnimShape pieAnimShape;
    private List<String> points;
    //声明一个字符串变量
    private String content;

    /**
     * MySaxHandler的构造方法
     *
     * @param list 装载返回结果的List对象
     */
    public MySaxHandler(List<PieAnimShape> list) {
        this.mList = list;
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        content = new String(ch, start, length);
    }

    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if ("shape".equals(localName)) {
            pieAnimShape = new PieAnimShape();
            points = new ArrayList<>();
        }
    }

    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);

        if ("point".equals(localName)) {
            points.add(content);
        } else if ("shape".equals(localName)) {
            pieAnimShape.setPoints(points);
            mList.add(pieAnimShape);
        }
    }
}