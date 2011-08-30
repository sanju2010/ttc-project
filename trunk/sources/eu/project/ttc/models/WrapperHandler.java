package eu.project.ttc.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.uima.jcas.JCas;

public interface WrapperHandler {

        public void doEncode(JCas cas,OutputStream outputStream) throws IOException;
        
        public void doDecode(JCas cas,InputStream inputStream) throws IOException;
        
}