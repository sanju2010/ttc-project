

/* First created by JCasGen Thu Dec 02 15:48:29 CET 2010 */
package org.apache.uima.muLtext;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.MultextAnnotation;


/** 
 * Updated by JCasGen Thu Dec 02 15:52:07 CET 2010
 * XML source: /home/rocheteau-j/Repositories/SourceSup/uima-bundle/trunk/sandbox/ttc-project/resources/fr/univnantes/lina/uima/types/TTC Type System.xml
 * @generated */
public class Article extends MultextAnnotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Article.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Article() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Article(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Article(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Article(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
}

    