package com.tschanz.v_bro.repo.persistence.xml.idref_parser;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/*
 remark: extra low-level style for high speed and minimal memory consumption when indexing large XMLs (~10sec/1GB)
 */
public class XmlIdRefParser {
    private static final int MAX_XML_TAG_LENTGH = 1024;
    private static final int MAX_ID_REF_LENTGH = 30;

    private final InputStream stream;

    private int byteCount = 0;

    @Getter private final List<XmlIdElementPosInfo> idElementPositions = new ArrayList<>();
    private final char[] tag = new char[MAX_XML_TAG_LENTGH];
    private final String idAttributePrefix;
    private final String[] idAttributeValuePrefixes;
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

    private final List<String> idRefs = new ArrayList<>();
    private final char[] idRefValue = new char[MAX_ID_REF_LENTGH];
    private final char[][] idRefPrefixes;
    private int idRefValueLenght = - 1;


    public XmlIdRefParser(
        @NonNull InputStream stream,
        @NonNull String idAttributeName,
        @NonNull List<String> idValuePrefixes
    ) {
        if (idAttributeName.isEmpty() || idValuePrefixes.size() == 0) {
            throw new IllegalArgumentException("idAttributeName / idValuePrefixes must not be empty");
        }

        this.stream = stream;
        this.idAttributePrefix = idAttributeName + "=" + "\"";
        this.idAttributeValuePrefixes = new String[idValuePrefixes.size()];
        this.idRefPrefixes = new char[idValuePrefixes.size()][];
        for (var i = 0; i < idValuePrefixes.size(); i++) {
            this.idAttributeValuePrefixes[i] = this.idAttributePrefix + idValuePrefixes.get(i);
            this.idRefPrefixes[i] = idValuePrefixes.get(i).toCharArray();
        }
    }


    public void parse() throws RepoException {
        try {
            while (true) {
                int c = this.stream.read();
                if (c == -1) {
                    break;
                } else {
                    this.byteCount++;
                    var currentChar = (char) c;
                    if (this.idRefValueLenght >= 0) {
                        this.parseIdRef(currentChar);
                    }
                    this.parseXmlTag(currentChar);
                }
            }

            stream.close();
        } catch (IOException exception) {
            throw new RepoException(exception);
        }
    }


    private void parseXmlTag(char c) {
        if (c == '<') {
            this.tagLength = 0;
            this.tagOpenBytePos = this.byteCount - 1;
        } else if (c == '>') {
            this.tagCloseBytePos = this.byteCount - 1;
            this.onNextTag();
            this.tagLength = -1;
            this.idRefValueLenght = 0; // enable id ref parsing
        } else if (tagLength >= 0) {
            this.tag[tagLength] = c;
            this.tagLength++;
        }
    }


    private void parseIdRef(char c) {
        if (this.idRefValueLenght < MAX_ID_REF_LENTGH && this.isAlphanumeric(c)) {
            this.idRefValue[this.idRefValueLenght] = c;
            this.idRefValueLenght++;
        } else {
            if (this.idRefValueLenght > 0 && this.startsWithIdRefPrefix()) {
                var text = new String(this.idRefValue, 0, this.idRefValueLenght);
                this.idRefs.add(text);
                this.idRefValueLenght = 0; // enable id ref parsing
            } else {
                this.idRefValueLenght = -1; // disable id ref parsing
            }
        }
    }


    private boolean isAlphanumeric(char c) {
        if (c >= 'a' && c <= 'z') {
            return true;
        } else if (c >= 'A' && c <= 'Z') {
            return true;
        } else if (c >= '0' && c <= '9') {
            return true;
        } else if (c == '_') {
            return true;
        } else {
            return false;
        }
    }


    private boolean startsWithIdRefPrefix() {
        for (var idValuePrefix : this.idRefPrefixes) {
            var isMatch = true;
            for (var pos = 0; pos < idValuePrefix.length; pos++) {
                if (this.idRefValue[pos] != idValuePrefix[pos]) {
                    isMatch = false;
                    break;
                }
            }

            if (isMatch) {
                return true;
            }
        }

        return false;
    }


    private void onNextTag() {
        if (this.isOpeningTag()) {
            this.nodeLevel++;

            if (this.elementNodeLevel == -1 && this.findElementAndId()) {
                this.elementNodeLevel = this.nodeLevel;
                this.elementName = new String(this.tag, 0, this.elementNameEndIdx);
                this.elementId = new String(this.tag, this.idAttributeStartIdx, this.idAttributeEndIdx - this.idAttributeStartIdx);
                this.elementTagOpenBytePos = this.tagOpenBytePos;
                this.idRefs.clear();
            }
        }

        if (this.isClosingTag()) { // can be the same like the opening tag
            if (this.elementNodeLevel == this.nodeLevel) {
                this.idElementPositions.add(new XmlIdElementPosInfo(this.elementName, this.elementId, this.elementTagOpenBytePos, this.tagCloseBytePos, new ArrayList<>(this.idRefs)));
                this.elementNodeLevel = -1;
            }

            this.nodeLevel--;
        }
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
        var attr = new String(this.tag, idx, this.tagLength - idx);

        for (var idValuePrefix : this.idAttributeValuePrefixes) {
            if (this.findAttributeValue(attr, idValuePrefix, idx)) {
                return true;
            }
        }

        return false;
    }


    private boolean findAttributeValue(String attr, String prefixPattern, int idxInTag) {
        int prefixIdx = attr.indexOf(prefixPattern);

        if (prefixIdx >= 0) {
            this.idAttributeStartIdx =idxInTag + prefixIdx + this.idAttributePrefix.length();
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
}
