

/* First created by JCasGen Fri Feb 24 09:30:03 CET 2012 */
package eu.project.ttc.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Fri Feb 24 09:30:03 CET 2012
 * XML source: /home/rocheteau-j/Repositories/GoogleCode/ttc-term-suite/trunk/resources/eu/project/ttc/types/TermSuiteTypeSystem.xml
 * @generated */
public class MultiWordTermAnnotation extends TermAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MultiWordTermAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected MultiWordTermAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public MultiWordTermAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public MultiWordTermAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public MultiWordTermAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
}

    