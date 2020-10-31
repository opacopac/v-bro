package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/*
 remark: extra low-level style for speed and minimal memory consumption when indexing large XMLs (~2sec/500MB)
 */
public class ElementLutParser {
    private static final int MAX_TAG_LENTGH = 1024;
    private static final String ID_ATTRIBUTE_PREFIX = XmlRepoService.ID_ATTRIBUTE_NAME + "=" + "\"";
    private static final String ID_ATTRIBUTE_VALUE_PREFIX1 = ID_ATTRIBUTE_PREFIX + XmlRepoService.ID_VALUE_PREFIX_1;
    private static final String ID_ATTRIBUTE_VALUE_PREFIX2 = ID_ATTRIBUTE_PREFIX + XmlRepoService.ID_VALUE_PREFIX_2;

    private InputStream stream;
    private int byteCount = 0;
    private char currentChar;
    private final char[] tag = new char[MAX_TAG_LENTGH];
    private int tagLength = -1;
    private int tagOpenBytePos = -1;
    private int tagCloseBytePos = -1;
    private int elementNameEndIdx = -1;
    private int elementTagOpenBytePos = -1;
    private String elementName;
    private String elementId;
    private int idAttributeStartIdx = -1;
    private int idAttributeEndIdx = -1;
    private int nodeLevel = -1;
    private int elementNodeLevel = -1;


    public ElementLutParser() {
    }


    public List<XmlElementLutInfo> readElementLut(InputStream stream) throws RepoException {
        this.stream = stream;
        List<XmlElementLutInfo> elements = new ArrayList<>();

        try {
            while (this.nextTag()) {
                if (this.isOpeningTag()) {
                    this.nodeLevel++;

                    if (this.elementNodeLevel == -1 && this.findElementAndId()) {
                        this.elementNodeLevel = this.nodeLevel;
                        this.elementName = new String(this.tag, 0, this.elementNameEndIdx);
                        this.elementId = new String(this.tag, this.idAttributeStartIdx, this.idAttributeEndIdx - this.idAttributeStartIdx);
                        this.elementTagOpenBytePos = this.tagOpenBytePos;
                    }

                    if (this.elementName != null && this.elementName.equals("haltestelle")) {
                        int a = 1;
                    }
                }

                if (this.isClosingTag()) { // can be the same like the opening tag
                    if (this.elementNodeLevel == this.nodeLevel) {
                        elements.add(new XmlElementLutInfo(this.elementName, this.elementId, this.elementTagOpenBytePos, this.tagCloseBytePos));
                        this.elementNodeLevel = -1;
                    }

                    this.nodeLevel--;
                }
            }
        } catch (IOException exception) {
            throw new RepoException(exception);
        }

        return elements;
    }


    private boolean findElementAndId() {
        this.elementNameEndIdx = 0;

        while (this.elementNameEndIdx < this.tagLength && !isWhitespace(this.tag[this.elementNameEndIdx])) {
            this.elementNameEndIdx++;
        }

        if (this.elementNameEndIdx + 1 < this.tagLength) {
            return this.findId(this.elementNameEndIdx + 1);
        } else {
            return false;
        }
    }


    private boolean findId(int idx) {
        String attr = new String(this.tag, idx, this.tagLength - idx);

        if (this.findAttributeValue(attr, ID_ATTRIBUTE_VALUE_PREFIX1, idx)) {
            return true;
        } else if (this.findAttributeValue(attr, ID_ATTRIBUTE_VALUE_PREFIX2, idx)) {
            return true;
        } else {
            return false;
        }
    }


    private boolean findAttributeValue(String attr, String prefixPattern, int idxInTag) {
        int prefixIdx = attr.indexOf(prefixPattern);

        if (prefixIdx >= 0) {
            this.idAttributeStartIdx =idxInTag + prefixIdx + ID_ATTRIBUTE_PREFIX.length();
            this.idAttributeEndIdx = this.idAttributeStartIdx;
            while (this.idAttributeEndIdx <= this.tagLength && this.tag[this.idAttributeEndIdx] != '"') {
                this.idAttributeEndIdx++;
            }

            return true;
        } else {
            return false;
        }
    }


    private boolean isWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n';
    }


    private boolean isOpeningTag() {
        return this.tag[0] != '/';
    }


    private boolean isClosingTag() {
        return this.tag[0] == '/' || this.tag[tagLength - 1] == '/' || this.tag[0] == '?' || this.tag[0] == '!';
    }


    private boolean nextTag() throws IOException {
        int tagLength = -1;

        while(this.nextChar()) {
            if (this.currentChar == '<') {
                tagLength = 0;
                this.tagOpenBytePos = this.byteCount - 1;
            } else if (this.currentChar == '>') {
                this.tagLength = tagLength;
                this.tagCloseBytePos = this.byteCount - 1;
                return true;
            } else if (tagLength >= 0) {
                this.tag[tagLength] = this.currentChar;
                tagLength++;
            }
        }

        return false;
    }


    private boolean nextChar() throws IOException {
        int c = this.stream.read();
        if (c == -1) {
            return false;
        } else {
            this.currentChar = (char)c;
            this.byteCount += 1;
            return true;
        }
    }
}
