package BackEnd.UserSystem;

/**
 * PhoneNumberNonNumericException Exception
 * @author Anderson Santana
 */
public class PhoneNumberNonNumericException extends RuntimeException{
  
  /**
   * Default Constructor.
   */
  public PhoneNumberNonNumericException(){}
  
  /**
   * Constructor.
   * @param reasoon The reason of the exception.
   */
  public PhoneNumberNonNumericException(String reason){
    super(reason);
  }
}